package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.TikTokFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.FileManager;
import org.vicary.service.file_service.TikTokFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TikTokDownloader implements Downloader {

    private final static Logger logger = LoggerFactory.getLogger(TikTokDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final TikTokFileService tiktokFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    private final List<String> availableExtensions = List.of("mp4");


    @Override
    public FileResponse download(FileRequest request) throws IllegalArgumentException, NoSuchElementException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        EditMessageText editMessageText = request.getEditMessageText();

        // SENDING INFO ABOUT CONNECTING TO TIKTOK
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConnectingToTikTok());

        // GETTING TIKTOK FILE INFO
        FileResponse response = getFileInfo(request, processBuilder);

        // CHECKS IF FILE ALREADY EXISTS IN REPOSITORY
        response = getFileFromRepository(response);
        if (response.getDownloadedFile() != null)
            return response;


        // IF FILE DOES NOT EXIST IN REPOSITORY THEN DOWNLOAD
        String fileSizeInProgress = null;
        String fileName = FileManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        boolean fileDownloaded = false;
        editMessageText.setText(editMessageText.getText() + info.getFileDownloading());

        processBuilder.command(commands.getDownloadTikTokFile(fileName, response.getURL()));
        Process process = processBuilder.start();
        // SENDING INFO ABOUT DOWNLOADING FILE
        logger.info("[download] Downloading TikTok file '{}'", response.getId());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                logger.debug(line);
                if (!fileDownloaded) {
                    editMessageText = updateMessageTextDownload(request.getEditMessageText(), line);
                    if (request.getEditMessageText().getText().contains("100%")) {
                        logger.info("[download] Successfully downloaded file '{}'", response.getId());
                        fileDownloaded = true;
                    }
                }

                if (isFileSizeTooBigInProcess(line)) {
                    process.destroy();
                    throw new InvalidBotRequestException(
                            info.getFileTooBig(),
                            String.format("Size of file '%s' is too big. File Size: '%s'", response.getId(), Converter.bytesToMB(getFileSizeInProcess(line))));
                }
            }
        }
        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            long fileSize = downloadedFile.length();
            if (!FileManager.isFileSizeValid(fileSize)) {
                throw new InvalidBotRequestException(
                        info.getFileTooBig(),
                        String.format("Size of file '%s' is too big. File Size: '%s'", response.getId(), Converter.bytesToMB(fileSize)));
            }
            response.setSize(fileSize);
            response.setDownloadedFile(InputFile.builder()
                    .file(downloadedFile)
                    .build());
        } else {
            throw new DownloadedFileNotFoundException(
                    info.getErrorInDownloading(),
                    String.format("File '%s' has not been downloaded", response.getId()));
        }
        response.setEditMessageText(editMessageText);
        return response;
    }

    @Override
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    @Override
    public String getServiceName() {
        return "tiktok";
    }

    public boolean isFileSizeTooBigInProcess(String line) {
        return line.startsWith("[download] File is larger than max-filesize");
    }

    public Long getFileSizeInProcess(String line) {
        long size = 0;
        if (line.startsWith("[download] File is larger than max-filesize")) {
            String[] arraySplit = line.split("\\(");
            size = Arrays.stream(arraySplit[1].split(" "))
                    .findFirst()
                    .map(Long::parseLong)
                    .orElse(0L);
        }
        return size;
    }

    public FileResponse getFileInfo(FileRequest request, ProcessBuilder processBuilder) throws IOException {
        String fileInfoInJson = "";

        processBuilder.command(commands.getDownloadFileInfo(request.getURL()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileInfoInJson = line;
            }
        } catch (IOException ex) {
            process.destroy();
            throw new IOException(ex.getMessage());
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);

        if (fileInfo == null) {
            throw new InvalidBotRequestException(
                    info.getNoVideo(),
                    String.format("No video in TikTok URL '%s'", request.getURL()));
        }

        String uploaderUrl = fileInfo.getUploaderURL();
        if (uploaderUrl == null || !uploaderUrl.contains("tiktok.com/")) {
            throw new InvalidBotRequestException(
                    info.getNoVideo(),
                    String.format("No video in TikTok URL '%s' and other service URL in description.", request.getURL()));
        }

        if (fileInfo.isLive()) {
            throw new InvalidBotRequestException(
                    info.getLiveVideo(),
                    String.format("Live video in TikTok URL '%s'.", request.getURL()));
        }

        FileResponse response = mapper.map(fileInfo);
        response.setExtension(request.getExtension());
        response.setPremium(request.isPremium());
        return response;
    }

    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<TikTokFileEntity> tiktokFile = tiktokFileService.findByTikTokId(response.getId());

        if (tiktokFile.isPresent() && Converter.MBToBytes(tiktokFile.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(tiktokFile.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(Converter.MBToBytes(tiktokFile.get().getSize()));
        }
        return response;
    }

    public EditMessageText updateMessageTextDownload(EditMessageText editMessageText, String line) {
        String progress = FileManager.getDownloadFileProgressInProcess(line);
        if (progress != null) {
            String oldText = editMessageText.getText();
            String[] splitOldText = oldText.split(" ");
            StringBuilder newText = new StringBuilder();

            for (String s : splitOldText)
                if (s.equals(splitOldText[splitOldText.length - 1]))
                    newText.append("\\[").append(progress).append("\\]_");
                else
                    newText.append(s).append(" ");

            if (!oldText.contentEquals(newText))
                quickSender.editMessageText(editMessageText, newText.toString());
        }
        return editMessageText;
    }
}
