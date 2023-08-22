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
import org.vicary.service.TwitterFileService;
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
        String fileSize = null;
        String fileName = getFileNameFromTitle(response.getTitle());
        String filePath = commands.getDownloadDestination() + fileName;
        boolean fileDownloaded = false;
        editMessageText.setText(editMessageText.getText() + info.getFileDownloading());

        processBuilder.command(commands.getDownloadTwitterFile(response));
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

                checkExtractingUrl(line, editMessageText);

                if (fileSize == null) {
                    fileSize = getFileSize(line);
                    if (fileSize != null && !checkFileSizeProcessBuilder(fileSize)) {
                        quickSender.editMessageText(editMessageText, info.getFileTooBig());
                        logger.warn("Size of file '{}' is too big. File size: {}", response.getId(), fileSize);
                        process.destroy();
                    }
                }
            }
        }
        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            long downloadedFileSize = downloadedFile.length();
            checkFileSize(downloadedFileSize, editMessageText, response.getId());

            response.setSize(downloadedFileSize);
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

    public void checkExtractingUrl(String line, EditMessageText editMessageText) {
        if (line.contains("Extracting URL:"))
            if (!line.contains("twitter.com/")) {
                quickSender.editMessageText(editMessageText, info.getNoVideo());
                throw new IllegalArgumentException("Twitter URL without video but in description is link to other service.");
            }
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
        String progress = getDownloadProgress(line);
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

    public String getDownloadProgress(String line) {
        if (line.contains("[download]")) {
            String[] s = line.split(" ");
            for (String a : s)
                if (a.contains("%"))
                    return MarkdownV2.apply(a).get();
        }
        return null;
    }

    public String getFileNameFromTitle(String title) {
        int maxFileNameLength = 59;
        String newTitle = title;

        if (newTitle.length() > maxFileNameLength)
            newTitle = newTitle.substring(0, 59);

        newTitle = newTitle.replaceAll("&|⧸⧹", "and");
        newTitle = newTitle.replaceAll("[/⧸||｜–\\\\]", "-");

        if (newTitle.length() > maxFileNameLength)
            newTitle = newTitle.substring(0, 59);

        return newTitle + ".mp4";
    }

    public boolean checkFileSizeProcessBuilder(String fileSize) {
        if (fileSize.endsWith("KiB"))
            return true;
        if (!fileSize.endsWith("MiB"))
            return false;

        StringBuilder sb = new StringBuilder();
        sb.append(0);
        for (char c : fileSize.toCharArray()) {
            if (c == '.')
                break;
            sb.append(c);
        }
        return Integer.parseInt(sb.toString()) <= 45;
    }

    public void checkFileSize(Long size, EditMessageText editMessageText, String youtubeId) {
        long fileSize = size / (1024 * 1024);
        if (fileSize > 50) {
            quickSender.editMessageText(editMessageText, info.getFileTooBig());
            logger.warn("Size of file '{}' is too big. File size: {}MB", youtubeId, fileSize);
            throw new IllegalArgumentException("File size cannot be more than 50MB." +
                                               "\n Your file size: " + fileSize + "MB.");
        }
    }

    public String getFileSize(String line) {
        if (line.contains("[download]")) {
            String[] s = line.split(" ");
            for (String a : s)
                if (a.contains("MiB") || a.contains("KiB"))
                    return a;
        }
        return null;
    }

}
