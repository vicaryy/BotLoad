package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.InstagramFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.file_service.InstagramFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InstagramDownloader implements Downloader {
    private final static Logger logger = LoggerFactory.getLogger(TwitterDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final InstagramFileService instagramFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    private final Converter converter;

    private final DownloaderManager downloaderManager;

    private final List<String> availableExtensions = List.of("mp4", "mp3", "m4a", "flac", "wav");

    @Override
    public FileResponse download(FileRequest request) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getConnectingToInstagram());

        // GETTING FILE INFO
        FileResponse response = getFileInfo(request, processBuilder);

        // checks if file already exists in repository
        if (request.getId3TagData() == null)
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

        processBuilder.command(commands.fileInfoInstagram(request.getURL()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                amountOfFiles++;

                FileInfo fileInfo = gson.fromJson(line, FileInfo.class);
                String URL = fileInfo.getURL();
                if (URL == null || !URL.contains("instagram.com/")) {
                    throw new InvalidBotRequestException(
                            info.getNoVideo(),
                            String.format("No video in Instagram URL '%s' and other service URL in description.", request.getURL()));
                }

                if (amountOfFiles > 1 && !specify) {
                    throw new InvalidBotRequestException(
                            info.getMultiVideo(),
                            String.format("Instagram URL '%s' is a multi-video link and user do not specify which video he want.", request.getURL()));
                }

                if (amountOfFiles > multiVideoMaxAmount) {
                    throw new InvalidBotRequestException(
                            info.getMultiVideoAmountTooHigh(),
                            String.format("Amount of multi-video Instagram URL '%s' is too high, more than 15.", request.getURL()));
                }

                if (amountOfFiles == multiVideoNumber) {
                    fileInfoInJson = line;
                }
            }
        }

        if (amountOfFiles == 0) {
            throw new InvalidBotRequestException(
                    info.getNoVideo(),
                    String.format("No video in Instagram URL '%s'", request.getURL()));
        }

        if (fileInfoInJson.isEmpty() && multiVideoNumber > amountOfFiles) {
            throw new InvalidBotRequestException(
                    info.getReceivedWrongNumberInMultiVideo(amountOfFiles, multiVideoNumber),
                    String.format("No video in multi-video Instagram URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);

        if (fileInfo.isLive()) {
            throw new InvalidBotRequestException(
                    info.getLiveVideo(),
                    String.format("Live video in Instagram URL '%s'.", request.getURL()));
        }

        FileResponse fileResponse = mapper.map(fileInfo, getServiceName());
        fileResponse.setMultiVideoNumber(multiVideoNumber);
        fileResponse.setExtension(request.getExtension());
        fileResponse.setPremium(request.isPremium());
        fileResponse.setEditMessageText(request.getEditMessageText());
        return fileResponse;
    }


    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<InstagramFileEntity> instagramFileEntity = instagramFileService.findByInstagramIdAndExtensionAndQuality(
                response.getServiceId(),
                response.getExtension(),
                response.isPremium() ? "premium" : "standard");

        if (instagramFileEntity.isPresent() && converter.MBToBytes(instagramFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(instagramFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(converter.MBToBytes(instagramFileEntity.get().getSize()));
        }
        return response;
    }


    public FileResponse downloadFile(FileResponse response, ProcessBuilder processBuilder) throws IOException {
        String fileName = downloaderManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        EditMessageText editMessageText = response.getEditMessageText();
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getFileDownloading());

        logger.info("[download] Downloading Instagram file '{}'", response.getServiceId());
        processBuilder.command(commands.downloadInstagram(fileName, response));
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


    @Override
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    @Override
    public String getServiceName() {
        return "instagram";
    }
}
