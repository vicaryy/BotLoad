package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.springframework.stereotype.Service;
import org.vicary.pattern.YoutubePattern;
import org.vicary.service.Converter;
import org.vicary.service.FileManager;
import org.vicary.service.file_service.YouTubeFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class YouTubeDownloader implements Downloader {
    private final static Logger logger = LoggerFactory.getLogger(YouTubeDownloader.class);

    private final YouTubeFileService youTubeFileService;

    private final FileInfoMapper mapper;

    private final YtDlpCommand commands;

    private final DownloaderInfo info;

    private final QuickSender quickSender;

    private final Gson gson;

    private final List<String> availableExtensions = List.of("mp3");

    @Override
    public FileResponse download(FileRequest request) throws IllegalArgumentException, NoSuchElementException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        EditMessageText editMessageText = request.getEditMessageText();

        // getting youtube file info
        FileResponse response = getFileInfo(request, processBuilder);

        // checks if file already exists in repository
        response = getFileFromRepository(response);
        if (response.getDownloadedFile() != null)
            return response;

        // if file is not in repo then download FILE
        String fileName = FileManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        String fileSizeInProcess = null;
        boolean fileDownloaded = false;
        boolean fileConverted = false;
        editMessageText.setText(editMessageText.getText() + info.getFileDownloading());
        processBuilder.command(commands.getDownloadYouTubeFile(fileName, response.getId(), response.getExtension(), response.isPremium()));
        Process process = processBuilder.start();
        logger.info("[download] Downloading YouTube file '{}'", response.getId());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (!fileDownloaded) {
                    editMessageText = updateMessageTextDownload(request.getEditMessageText(), line);
                    if (request.getEditMessageText().getText().contains("100%")) {
                        logger.info("[download] Successfully downloaded file '{}'", response.getId());
                        fileDownloaded = true;
                    }
                }

                if (!fileConverted && FileManager.isFileConvertingInProcess(line)) {
                    quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConverting(request.getExtension()));
                    logger.info("[convert] Successfully converted to {} file '{}'", response.getExtension(), response.getId());
                    fileConverted = true;
                }

                if (fileSizeInProcess == null) {
                    fileSizeInProcess = FileManager.getFileSizeInProcess(line);
                    if (fileSizeInProcess != null && !FileManager.checkFileSizeProcess(fileSizeInProcess)) {
                        process.destroy();
                        throw new InvalidBotRequestException(
                                info.getFileTooBig(),
                                String.format("Size of file '%s' is too big. File Size: '%s'", response.getId(), fileSizeInProcess));
                    }
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

        // downloading thumbnail
        final String thumbnailName = response.getTitle() + ".jpg";
        String thumbnailPath = "";
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getThumbnailDownloading());

        processBuilder.command(commands.getDownloadYouTubeThumbnail(thumbnailName, response.getId()));
        process = processBuilder.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            logger.info("[download] Downloading thumbnail to file '{}'", response.getId());
            String line;
            while ((line = br.readLine()) != null) {
                editMessageText = updateMessageTextDownload(editMessageText, line);

                if (line.equals("[download] Destination: /Users/vicary/desktop/folder/" + thumbnailName))
                    thumbnailPath = commands.getDownloadDestination() + thumbnailName;
            }
        }

        File downloadedThumbnail = new File(thumbnailPath);
        if (downloadedThumbnail.exists())
            logger.info("[download] Successfully downloaded thumbnail to file '{}'", response.getId());
        else
            logger.warn("Thumbnail to file '{}' did not download.", response.getId());

        response.setThumbnail(InputFile.builder()
                .file(new File(thumbnailPath))
                .isThumbnail(true)
                .build());
        // setting other stuff
        response.setEditMessageText(editMessageText);
        return response;
    }

    @Override
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<YouTubeFileEntity> youTubeFileEntity = youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                response.getId(),
                response.getExtension(),
                response.isPremium() ? "premium" : "standard");

        if (youTubeFileEntity.isPresent() && Converter.MBToBytes(youTubeFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(youTubeFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(Converter.MBToBytes(youTubeFileEntity.get().getSize()));
        }
        return response;
    }

    public FileResponse getFileInfo(FileRequest request, ProcessBuilder processBuilder) throws IOException {
        String fileInfoInJson = "";
        String youtubeId = YoutubePattern.getYoutubeId(request.getURL());

        processBuilder.command(commands.getDownloadFileInfo(youtubeId));
        Process process = processBuilder.start();
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getConnectingToYoutube());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null)
                fileInfoInJson = line;
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
        if (fileInfoInJson.isEmpty()) {
            throw new InvalidBotRequestException(
                    info.getNoVideo(),
                    String.format("No video in YouTube URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);
        FileResponse fileResponse = mapper.map(fileInfo);
        fileResponse.setPremium(request.isPremium());
        fileResponse.setExtension(request.getExtension());
        return fileResponse;
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