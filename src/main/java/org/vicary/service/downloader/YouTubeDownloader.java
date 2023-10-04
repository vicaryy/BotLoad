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
import org.vicary.pattern.Pattern;
import org.vicary.service.Converter;
import org.vicary.service.file_service.YouTubeFileService;
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
public class YouTubeDownloader implements Downloader {
    private final static Logger logger = LoggerFactory.getLogger(YouTubeDownloader.class);

    private final YouTubeFileService youTubeFileService;

    private final FileInfoMapper mapper;

    private final YtDlpCommand commands;

    private final DownloaderInfo info;

    private final QuickSender quickSender;

    private final Gson gson;

    private final Pattern pattern;

    private final Converter converter;

    private final DownloaderManager downloaderManager;

    private final List<String> availableExtensions = List.of("mp3", "m4a", "flac", "wav");

    @Override
    public FileResponse download(FileRequest request) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getConnectingToYoutube());

        // getting YouTube file info
        FileResponse response = getFileInfo(request, processBuilder);

        // checks if file already exists in repository
        if (request.getId3TagData() == null)
            getFileFromRepository(response);

        if (response.getDownloadedFile() != null)
            return response;

        // if file is not in repo then download FILE
        downloadFile(response, processBuilder);

        // downloading thumbnail
        downloadThumbnail(response, processBuilder);

        return response;
    }


    public FileResponse getFileInfo(FileRequest request, ProcessBuilder processBuilder) throws IOException {
        String fileInfoInJson = "";
        String youtubeId = pattern.getYoutubeId(request.getURL());

        processBuilder.command(commands.fileInfoYouTube(youtubeId));
        Process process = processBuilder.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null)
                fileInfoInJson = line;
        }


        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);

        if (fileInfo == null) {
            throw new InvalidBotRequestException(
                    info.getNoVideo(),
                    String.format("No video in YouTube URL '%s'", request.getURL()));
        }


        if (fileInfo.isLive()) {
            throw new InvalidBotRequestException(
                    info.getLiveVideo(),
                    String.format("Live video in YouTube URL '%s'.", request.getURL()));
        }

        FileResponse fileResponse = mapper.map(fileInfo, getServiceName());
        fileResponse.setPremium(request.isPremium());
        fileResponse.setExtension(request.getExtension());
        fileResponse.setEditMessageText(request.getEditMessageText());
        return fileResponse;
    }


    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<YouTubeFileEntity> youTubeFileEntity = youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                response.getServiceId(),
                response.getExtension(),
                response.isPremium() ? "premium" : "standard");

        if (youTubeFileEntity.isPresent() && converter.MBToBytes(youTubeFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(youTubeFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(converter.MBToBytes(youTubeFileEntity.get().getSize()));
        }
        return response;
    }


    public FileResponse downloadFile(FileResponse response, ProcessBuilder processBuilder) throws IOException {
        EditMessageText editMessageText = response.getEditMessageText();
        String fileName = downloaderManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getFileDownloading());

        logger.info("[download] Downloading YouTube file '{}'", response.getServiceId());
        processBuilder.command(commands.downloadYouTube(fileName, response));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (downloaderManager.isFileDownloadingInProcess(line)) {
                    downloaderManager.updateDownloadProgressInEditMessageText(editMessageText, line);

                    if (downloaderManager.isFileDownloadedInProcess(line)) {
                        logger.info("[download] Successfully downloaded file '{}'", response.getServiceId());
                    }
                    if (!downloaderManager.isFileSizeValidInProcess(line)) {
                        process.destroy();
                        throw new InvalidBotRequestException(
                                info.getFileTooBig(),
                                String.format("Size of file '%s' is too big. File Size: '%s'", response.getServiceId(), downloaderManager.getFileSizeInProcess(line)));
                    }
                }

                if (downloaderManager.isFileConvertingInProcess(line)) {
                    quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConverting(response.getExtension()));
                    logger.info("[convert] Converting file '{}' to {}", response.getServiceId(), response.getExtension());
                }
            }
        }

        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            long fileSize = downloadedFile.length();
            if (!downloaderManager.isFileSizeValid(fileSize)) {
                throw new InvalidBotRequestException(
                        info.getFileTooBig(),
                        String.format("Size of file '%s' is too big. File Size: '%s'", response.getServiceId(), converter.bytesToMB(fileSize)));
            }
            response.setSize(fileSize);
            response.setDownloadedFile(InputFile.builder()
                    .file(downloadedFile)
                    .build());
        } else {
            throw new DownloadedFileNotFoundException(
                    info.getErrorInDownloading(),
                    String.format("File '%s' has not been downloaded", response.getServiceId()));
        }
        return response;
    }


    public FileResponse downloadThumbnail(FileResponse response, ProcessBuilder processBuilder) throws IOException {
        final String thumbnailName = response.getTitle() + ".jpg";
        EditMessageText editMessageText = response.getEditMessageText();
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getThumbnailDownloading());

        logger.info("[download] Downloading thumbnail to file '{}'", response.getServiceId());
        processBuilder.command(commands.downloadThumbnailYoutube(thumbnailName, response.getServiceId()));
        Process process = processBuilder.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (downloaderManager.isFileDownloadingInProcess(line)) {
                    downloaderManager.updateDownloadProgressInEditMessageText(editMessageText, line);
                }
            }
        }

        String thumbnailPath = commands.getDownloadDestination() + thumbnailName;
        File downloadedThumbnail = new File(thumbnailPath);
        if (downloadedThumbnail.exists())
            logger.info("[download] Successfully downloaded thumbnail to file '{}'", response.getServiceId());
        else
            logger.warn("Thumbnail to file '{}' did not download.", response.getServiceId());

        response.setThumbnail(InputFile.builder()
                .file(downloadedThumbnail)
                .isThumbnail(true)
                .build());
        return response;
    }

    @Override
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    @Override
    public String getServiceName() {
        return "youtube";
    }
}