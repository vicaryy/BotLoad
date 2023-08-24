package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.format.MarkdownV2;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwitterDownloader {
    private final static Logger logger = LoggerFactory.getLogger(TwitterDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final TwitterFileService twitterFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    public FileResponse download(FileRequest request) throws WebClientRequestException, IllegalArgumentException, NoSuchElementException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        EditMessageText editMessageText = request.getEditMessageText();

        // SENDING INFO ABOUT CONNECTING TO TWITTER
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConnectingToTwitter());

        // GETTING TWITTER FILE INFO
        FileResponse response = getFileInfo(request, processBuilder);

        // CHECKS IF FILE ALREADY EXISTS IN REPOSITORY
        response = getFileFromRepository(response);
        if (response.getDownloadedFile() != null)
            return response;


        // IF FILE DOES NOT EXIST IN REPOSITORY THEN DOWNLOAD
        String fileSizeInProcess = null;
        String fileName = FileManager.getFileNameFromTitle(response.getTitle(), response.getExtension());
        String filePath = commands.getDownloadDestination() + fileName;
        boolean fileDownloaded = false;
        editMessageText.setText(editMessageText.getText() + info.getFileDownloading());

        processBuilder.command(commands.getDownloadTwitterFile(fileName, response.getURL(), response.getMultiVideoNumber()));
        Process process = processBuilder.start();
        // SENDING INFO ABOUT DOWNLOADING FILE
        logger.info("[download] Downloading Twitter file '{}'", response.getId());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!fileDownloaded) {
                    updateMessageTextDownload(request.getEditMessageText(), line);
                    if (request.getEditMessageText().getText().contains("100%")) {
                        logger.info("[download] Successfully downloaded file '{}'", response.getId());
                        fileDownloaded = true;
                    }
                }

                if (fileSizeInProcess == null) {
                    fileSizeInProcess = FileManager.getFileSizeInProcess(line);
                    if (fileSizeInProcess != null && !FileManager.checkFileSizeProcess(fileSizeInProcess)) {
                        quickSender.editMessageText(editMessageText, info.getFileTooBig());
                        logger.warn("Size of file '{}' is too big. File size: {}", response.getId(), fileSizeInProcess);
                        process.destroy();
                    }
                }
            }
        }
        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            long fileSize = downloadedFile.length();
            if (!FileManager.isFileSizeValid(fileSize)) {
                quickSender.editMessageText(editMessageText, info.getFileTooBig());
                logger.warn("Size of file '{}' is too big. File size: {}", response.getId(), Converter.bytesToMB(fileSize));
                throw new IllegalArgumentException("File size cannot be more than 50MB." +
                                                   "\nFile size: " + Converter.bytesToMB(fileSize));
            }

            response.setSize(fileSize);
            response.setDownloadedFile(InputFile.builder()
                    .file(downloadedFile)
                    .build());
        } else {
            quickSender.editMessageText(editMessageText, info.getErrorInDownloading());
            throw new NoSuchElementException(String.format("File '%s' did not download.", response.getId()));
        }
        response.setEditMessageText(editMessageText);
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
                    quickSender.editMessageText(request.getEditMessageText(), info.getNoVideo());
                    throw new IllegalArgumentException(String.format("No video in Twitter URL '%s' and other service URL in description.", request.getURL()));
                }

                if (amountOfFiles > 1 && !specify) {
                    quickSender.editMessageText(request.getEditMessageText(), info.getMultiVideo());
                    throw new IllegalArgumentException(String.format("Twitter URL '%s' is a multi-video link and user do not specify which video he want.", request.getURL()));
                }

                if (amountOfFiles > multiVideoMaxAmount) {
                    quickSender.editMessageText(request.getEditMessageText(), info.getMultiVideoAmountTooHigh());
                    throw new IllegalArgumentException(String.format("Amount of multi-video Twitter URL '%s' is too high, more than 15.", request.getURL()));
                }

                if (amountOfFiles == multiVideoNumber) {
                    fileInfoInJson = line;
                }
            }
        } catch (IOException ex) {
            process.destroy();
            throw new IOException(ex.getMessage());
        }

        if (amountOfFiles == 0) {
            quickSender.editMessageText(request.getEditMessageText(), info.getNoVideo());
            throw new IllegalArgumentException(String.format("No video in Twitter URL '%s'", request.getURL()));
        }

        if (fileInfoInJson.isEmpty() && multiVideoNumber > amountOfFiles) {
            quickSender.editMessageText(request.getEditMessageText(), info.getReceivedWrongNumberInMultiVideo(amountOfFiles, multiVideoNumber));
            throw new IllegalArgumentException(String.format("No video in multi-video Twitter URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);
        FileResponse fileResponse = mapper.map(fileInfo);
        fileResponse.setMultiVideoNumber(multiVideoNumber);
        fileResponse.setExtension(request.getExtension());
        fileResponse.setPremium(request.isPremium());
        return fileResponse;
    }

    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<TwitterFileEntity> twitterFileEntity = twitterFileService.findByTwitterId(response.getId());

        if (twitterFileEntity.isPresent() && Converter.MBToBytes(twitterFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(twitterFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(Converter.MBToBytes(twitterFileEntity.get().getSize()));
        }
        return response;
    }

    public void updateMessageTextDownload(EditMessageText editMessageText, String line) {
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
    }
}
