package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.FileManager;
import org.vicary.service.file_service.TwitterFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwitterDownloader implements Downloader {
    private final static Logger logger = LoggerFactory.getLogger(TwitterDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final TwitterFileService twitterFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    private final Converter converter;

    private final FileManager fileManager;

    private final List<String> availableExtensions = List.of("mp4");


    public FileResponse download(FileRequest request) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getConnectingToTwitter());

        // GETTING TWITTER FILE INFO
        FileResponse response = getFileInfo(request, processBuilder);

        // CHECKS IF FILE ALREADY EXISTS IN REPOSITORY
        getFileFromRepository(response);
        if (response.getDownloadedFile() != null)
            return response;

        // IF FILE DOES NOT EXIST IN REPOSITORY THEN DOWNLOAD
        downloadFile(response, processBuilder);

        return response;
    }


    public FileResponse getFileInfo(FileRequest request, ProcessBuilder processBuilder) throws IOException {
        String fileInfoInJson = "";
        int amountOfFiles = 0;
        int multiVideoNumber = request.getMultiVideoNumber() == 0 ? 1 : request.getMultiVideoNumber();
        boolean specify = request.getMultiVideoNumber() != 0;
        final int multiVideoMaxAmount = 15;

        processBuilder.command(commands.getDownloadFileInfoTwitter(request.getURL()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                amountOfFiles++;

                FileInfo fileInfo = gson.fromJson(line, FileInfo.class);
                String uploaderUrl = fileInfo.getUploaderURL();
                if (uploaderUrl == null || !uploaderUrl.contains("twitter.com/")) {
                    throw new InvalidBotRequestException(
                            info.getNoVideo(),
                            String.format("No video in Twitter URL '%s' and other service URL in description.", request.getURL()));
                }

                if (amountOfFiles > 1 && !specify) {
                    throw new InvalidBotRequestException(
                            info.getMultiVideo(),
                            String.format("Twitter URL '%s' is a multi-video link and user do not specify which video he want.", request.getURL()));
                }

                if (amountOfFiles > multiVideoMaxAmount) {
                    throw new InvalidBotRequestException(
                            info.getMultiVideoAmountTooHigh(),
                            String.format("Amount of multi-video Twitter URL '%s' is too high, more than 15.", request.getURL()));
                }

                if (amountOfFiles == multiVideoNumber) {
                    fileInfoInJson = line;
                }
            }
        }

        if (amountOfFiles == 0) {
            throw new InvalidBotRequestException(
                    info.getNoVideo(),
                    String.format("No video in Twitter URL '%s'", request.getURL()));
        }

        if (fileInfoInJson.isEmpty() && multiVideoNumber > amountOfFiles) {
            throw new InvalidBotRequestException(
                    info.getReceivedWrongNumberInMultiVideo(amountOfFiles, multiVideoNumber),
                    String.format("No video in multi-video Twitter URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);

        if (fileInfo.isLive()) {
            throw new InvalidBotRequestException(
                    info.getLiveVideo(),
                    String.format("Live video in Twitter URL '%s'.", request.getURL()));
        }

        FileResponse fileResponse = mapper.map(fileInfo);
        fileResponse.setMultiVideoNumber(multiVideoNumber);
        fileResponse.setExtension(request.getExtension());
        fileResponse.setPremium(request.isPremium());
        fileResponse.setEditMessageText(request.getEditMessageText());
        return fileResponse;
    }


    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<TwitterFileEntity> twitterFileEntity = twitterFileService.findByTwitterId(response.getId());

        if (twitterFileEntity.isPresent() && converter.MBToBytes(twitterFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(twitterFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(converter.MBToBytes(twitterFileEntity.get().getSize()));
        }
        return response;
    }


    public FileResponse downloadFile(FileResponse response, ProcessBuilder processBuilder) throws IOException {
        String fileName = fileManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        EditMessageText editMessageText = response.getEditMessageText();
        editMessageText.setText(editMessageText.getText() + info.getFileDownloading());

        logger.info("[download] Downloading Twitter file '{}'", response.getId());
        processBuilder.command(commands.getDownloadTwitterFile(fileName, response.getURL(), response.getMultiVideoNumber()));
        Process process = processBuilder.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (fileManager.isFileDownloadingInProcess(line)) {
                    updateDownloadProgressInEditMessageText(editMessageText, line);

                    if (fileManager.isFileDownloadedInProcess(line)) {
                        logger.info("[download] Successfully downloaded file '{}'", response.getId());
                    }
                    if (!fileManager.isFileSizeValidInProcess(line)) {
                        process.destroy();
                        throw new InvalidBotRequestException(
                                info.getFileTooBig(),
                                String.format("Size of file '%s' is too big. File Size: '%s'", response.getId(), fileManager.getFileSizeInProcess(line)));
                    }
                }
            }
        }

        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            long fileSize = downloadedFile.length();
            if (!fileManager.isFileSizeValid(fileSize)) {
                throw new InvalidBotRequestException(
                        info.getFileTooBig(),
                        String.format("Size of file '%s' is too big. File Size: '%s'", response.getId(), converter.bytesToMB(fileSize)));
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
        return response;
    }


    public void updateDownloadProgressInEditMessageText(EditMessageText editMessageText, String line) {
        String progress = fileManager.getDownloadFileProgressInProcess(line);
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
    }


    @Override
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    @Override
    public String getServiceName() {
        return "twitter";
    }
}
