package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.SoundCloudFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileInfoThumbnail;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.file_service.SoundCloudFileService;
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
public class SoundCloudDownloader implements Downloader {

    private final static Logger logger = LoggerFactory.getLogger(SoundCloudDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final SoundCloudFileService soundcloudFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    private final Converter converter;

    private final DownloaderManager downloaderManager;

    private final List<String> availableExtensions = List.of("mp3", "m4a", "flac", "wav");

    @Override
    public FileResponse download(FileRequest request) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getConnectingToSoundCloud());

        // getting SoundCloud file info
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
        int amountOfFiles = 0;
        int multiVideoNumber = request.getMultiVideoNumber() == 0 ? 1 : request.getMultiVideoNumber();
        boolean specify = request.getMultiVideoNumber() != 0;
        final int multiVideoMaxAmount = 50;

        processBuilder.command(commands.fileInfoSoundCloud(request.getURL()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                amountOfFiles++;

                FileInfo fileInfo = gson.fromJson(line, FileInfo.class);
                String uploaderUrl = fileInfo.getUploaderURL();
                if (uploaderUrl == null || !uploaderUrl.contains("soundcloud.com/")) {
                    throw new InvalidBotRequestException(
                            info.getNoAudio(),
                            String.format("No music in SoundCloud URL '%s' and other service URL in description.", request.getURL()));
                }

                if (amountOfFiles > 1 && !specify) {
                    throw new InvalidBotRequestException(
                            info.getMultiAudio(),
                            String.format("SoundCloud URL '%s' is a multi-audio link and user do not specify which audio he want.", request.getURL()));
                }

                if (amountOfFiles > multiVideoMaxAmount) {
                    throw new InvalidBotRequestException(
                            info.getMultiAudioAmountTooHigh(),
                            String.format("Amount of multi-audio SoundCloud URL '%s' is too high, more than 50.", request.getURL()));
                }

                if (amountOfFiles == multiVideoNumber) {
                    fileInfoInJson = line;
                }
            }
        }

        if (amountOfFiles == 0) {
            throw new InvalidBotRequestException(
                    info.getNoAudio(),
                    String.format("No audio in SoundCloud URL '%s'", request.getURL()));
        }

        if (fileInfoInJson.isEmpty() && multiVideoNumber > amountOfFiles) {
            throw new InvalidBotRequestException(
                    info.getReceivedWrongNumberInMultiAudio(amountOfFiles, multiVideoNumber),
                    String.format("No audio in multi-audio SoundCloud URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);

        if (fileInfo.getFormat().contains("preview")) {
            throw new InvalidBotRequestException(
                    info.getPreviewAudio(),
                    String.format("Preview audio in SoundCloud URL '%s'.", request.getURL()));
        }

        if (fileInfo.isLive()) {
            throw new InvalidBotRequestException(
                    info.getLiveAudio(),
                    String.format("Live in SoundCloud URL '%s'.", request.getURL()));
        }

        System.out.println(fileInfo);
        FileResponse fileResponse = mapper.map(fileInfo, getServiceName());
        fileResponse.setThumbnailURL(getProperThumbnailURL(fileInfo.getThumbnails()));
        fileResponse.setMultiVideoNumber(multiVideoNumber);
        fileResponse.setExtension(request.getExtension());
        fileResponse.setPremium(request.isPremium());
        fileResponse.setEditMessageText(request.getEditMessageText());
        return fileResponse;
    }

    public String getProperThumbnailURL(List<FileInfoThumbnail> thumbnails) {
        return thumbnails.stream()
                .filter(e -> e.getResolution().equals("300x300"))
                .map(FileInfoThumbnail::getURL)
                .findFirst()
                .orElse("");
    }


    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<SoundCloudFileEntity> soundCloudFileEntity = soundcloudFileService.findBySoundcloudIdAndExtensionAndQuality(
                response.getServiceId(),
                response.getExtension(),
                response.isPremium() ? "premium" : "standard");

        if (soundCloudFileEntity.isPresent() && converter.MBToBytes(soundCloudFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(soundCloudFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(converter.MBToBytes(soundCloudFileEntity.get().getSize()));
        }
        return response;
    }


    public FileResponse downloadFile(FileResponse response, ProcessBuilder processBuilder) throws IOException {
        String fileName = downloaderManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        EditMessageText editMessageText = response.getEditMessageText();
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getFileDownloading());

        logger.info("[download] Downloading SoundCloud file '{}'", response.getServiceId());
        processBuilder.command(commands.downloadSoundCloud(fileName, response));
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
        processBuilder.command(commands.downloadThumbnailSoundCloud(thumbnailName, response.getThumbnailURL()));
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
        return "soundcloud";
    }
}
