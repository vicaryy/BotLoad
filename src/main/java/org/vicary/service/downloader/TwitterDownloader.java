package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.TwitterCommand;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.info.TwitterDownloaderInfo;
import org.vicary.model.twitter.TwitterFileInfo;
import org.vicary.model.twitter.TwitterFileRequest;
import org.vicary.model.twitter.TwitterFileResponse;
import org.vicary.model.youtube.YouTubeFileInfo;
import org.vicary.model.youtube.YouTubeFileResponse;
import org.vicary.service.Converter;
import org.vicary.service.TerminalExecutor;
import org.vicary.service.TwitterFileService;
import org.vicary.service.mapper.TwitterFileMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class TwitterDownloader {

    private final static Logger logger = LoggerFactory.getLogger(TwitterDownloader.class);

    private final QuickSender quickSender;

    private final TwitterDownloaderInfo info;

    private final TwitterCommand commands;

    private final TwitterFileService twitterFileService;

    private final TwitterFileMapper mapper;

    private final Gson gson;

    public TwitterFileResponse download(TwitterFileRequest request) throws WebClientRequestException, IllegalArgumentException, NoSuchElementException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        EditMessageText editMessageText = request.getEditMessageText();
        String extension = request.getExtension();

        String filePath = "";
        String fileSize = null;

        // getting youtube file info
        editMessageText = quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConnectingToTwitter());
        TwitterFileResponse response = getFileInfo(request, processBuilder);
        response.setExtension(extension);
        response.setPremium(request.getPremium());
        response.setUrl(request.getUrl());

        // checks if file already exists in repository
        response = getFileFromRepository(response);

        // if file is not in repo then download FILE
        if (response.getDownloadedFile() == null) {
            filePath = String.format("%s%s.%s", commands.getPath(), response.getTitle(), extension);
            editMessageText.setText(editMessageText.getText() + info.getFileDownloading());
            logger.info("[download] Downloading Twitter file '{}'", response.getTwitterId());

            processBuilder.command(commands.downloadFile(response));
            Process process = processBuilder.start();
            boolean fileDownloaded = false;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {

                    if (!fileDownloaded) {
                        editMessageText = updateMessageTextDownload(request.getEditMessageText(), line);
                        if (request.getEditMessageText().getText().contains("100%")) {
                            logger.info("[download] Successfully downloaded file '{}'", response.getTwitterId());
                            fileDownloaded = true;
                        }
                    }

                    if (fileSize == null) {
                        fileSize = getFileSize(line);
                        if (fileSize != null && !checkFileSizeProcessBuilder(fileSize)) {
                            editMessageText = quickSender.editMessageText(editMessageText, info.getFileTooBig() + info.getFileTooBigExplanation());
                            logger.warn("Size of file '{}' is too big. File size: {}", response.getTwitterId(), fileSize);
                            process.destroy();
                        }
                    }
                }
            }
        }

        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            Long downloadedFileSize = downloadedFile.length();
            checkFileSize(downloadedFileSize, editMessageText, response.getTwitterId());

            String oldFileName = downloadedFile.getName();
            downloadedFile = correctFilePath(downloadedFile, extension);

            if (!oldFileName.equals(downloadedFile.getName()))
                editMessageText = quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getRenaming());

            response.setSize(downloadedFileSize);
            response.setDownloadedFile(InputFile.builder()
                    .file(downloadedFile)
                    .build());
        } else {
            quickSender.editMessageText(editMessageText, info.getErrorInDownloading() + info.getTryAgainLater());
            throw new NoSuchElementException(String.format("File '%s' did not download.", response.getTwitterId()));
        }
        response.setEditMessageText(editMessageText);
        return response;
    }

    public TwitterFileResponse getFileInfo(TwitterFileRequest request, ProcessBuilder processBuilder) throws IOException {
        StringBuilder sb = new StringBuilder();

        processBuilder.command(commands.downloadFileInfo(request.getUrl()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }

        if (sb.isEmpty()) {
            quickSender.editMessageText(request.getEditMessageText(), info.getNoVideo() + info.getNoVideoExplanation());
            throw new IllegalArgumentException(String.format("Twitter file info is null, twitter url '%s'", request.getUrl()));
        }
        TwitterFileInfo twitterFileInfo = gson.fromJson(sb.toString(), TwitterFileInfo.class);
        return mapper.map(twitterFileInfo);
    }

    public TwitterFileResponse getFileFromRepository(TwitterFileResponse response) {
        Optional<TwitterFileEntity> twitterFileEntity = twitterFileService.findByTwitterId(response.getTwitterId());

        if (twitterFileEntity.isPresent() && Converter.MBToBytes(twitterFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(twitterFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(Converter.MBToBytes(twitterFileEntity.get().getSize()));
        }
        return response;
    }

    public EditMessageText updateMessageTextDownload(EditMessageText editMessageText, String line) {
        String progress = getDownloadProgress(line);
        if (progress != null) {
            String oldText = editMessageText.getText();
            String[] splitOldText = oldText.split(" ");
            StringBuilder newText = new StringBuilder();

            for (String s : splitOldText)
                if (s.equals(splitOldText[splitOldText.length - 1]))
                    newText.append("\\[" + progress + "\\]_");
                else
                    newText.append(s + " ");

            if (!oldText.contentEquals(newText))
                quickSender.editMessageText(editMessageText, newText.toString());
        }
        return editMessageText;
    }

    public Boolean isFileConverting(String line) {
        return line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/");
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


    public File correctFilePath(File file, String extension) {
        int maxFileNameLength = 63;
        String oldFileName = file.getName();
        String newFileName = oldFileName;

        newFileName = newFileName.replaceAll("&|⧸⧹", "and");
        newFileName = newFileName.replaceAll("[/⧸||｜–\\\\]", "-");

        if (newFileName.length() > maxFileNameLength)
            newFileName = newFileName.substring(0, 59) + "." + extension;

        if (newFileName.equals(oldFileName))
            return file;

        return TerminalExecutor.renameFile(file, newFileName);
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
            quickSender.editMessageText(editMessageText, info.getFileTooBig() + info.getFileTooBigExplanation());
            logger.warn("Size of file '{}' is too big. File size: {}MB", youtubeId, fileSize);
            throw new IllegalArgumentException("File size cannot be more than 50MB." +
                                               "\n Your file size: " + fileSize + "MB.");
        }
    }

    public String generateUniqueName() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        for (int i = 0; i < 10; i++)
            stringBuilder.append(ThreadLocalRandom.current().nextInt(0, 10));

        return stringBuilder.toString();
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
