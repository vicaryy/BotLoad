package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YouTubeCommand;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.info.YouTubeDownloaderInfo;
import org.vicary.model.youtube.YouTubeFileInfo;
import org.vicary.model.youtube.YouTubeFileRequest;
import org.springframework.stereotype.Service;
import org.vicary.model.youtube.YouTubeFileResponse;
import org.vicary.service.Converter;
import org.vicary.service.TerminalExecutor;
import org.vicary.service.YouTubeFileService;
import org.vicary.service.mapper.YouTubeFileMapper;
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
public class YouTubeDownloader {
    private final static Logger logger = LoggerFactory.getLogger(YouTubeDownloader.class);

    private final YouTubeFileService youTubeFileService;

    private final YouTubeFileMapper mapper;

    private final YouTubeCommand commands;

    private final YouTubeDownloaderInfo info;

    private final QuickSender quickSender;

    private final Gson gson;

    public YouTubeFileResponse download(YouTubeFileRequest request) throws WebClientRequestException, IllegalArgumentException, NoSuchElementException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        boolean fileDownloaded = false;
        boolean fileConverted = false;
        EditMessageText editMessageText = request.getEditMessageText();
        String extension = request.getExtension();

        String filePath = "";
        String fileSize = null;

        // getting youtube file info
        editMessageText = quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConnectingToYoutube());
        YouTubeFileResponse response = getFileInfo(request, processBuilder);
        response.setExtension(extension);
        response.setPremium(request.getPremium());


        // checks if file already exists in repository
        response = getFileFromRepository(response);

        // if file is not in repo then download FILE
        if (response.getDownloadedFile() == null) {
            filePath = String.format("%s%s.%s", commands.getPath(), response.getTitle(), extension);
            editMessageText.setText(editMessageText.getText() + info.getFileDownloading());
            processBuilder.command(commands.getFileCommand(response));
            Process process = processBuilder.start();
            logger.info("[download] Downloading YouTube file '{}'", response.getYoutubeId());

            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {

                    if (!fileDownloaded) {
                        editMessageText = updateMessageTextDownload(request.getEditMessageText(), line);
                        if (request.getEditMessageText().getText().contains("100%")) {
                            logger.info("[download] Successfully downloaded file '{}'", response.getYoutubeId());
                            fileDownloaded = true;
                        }
                    }

                    if (!fileConverted && isFileConverting(line)) {
                        editMessageText = quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConverting(extension));
                        logger.info("[convert] Successfully converted to {} file '{}'", response.getExtension(), response.getYoutubeId());
                        fileConverted = true;
                    }

                    if (fileSize == null) {
                        fileSize = getFileSize(line);
                        if (fileSize != null && !checkFileSizeProcessBuilder(fileSize)) {
                            editMessageText = quickSender.editMessageText(editMessageText, info.getFileTooBig() + info.getUpTo50Mb());
                            logger.warn("Size of file '{}' is too big. File size: {}", response.getYoutubeId(), fileSize);
                            process.destroy();
                        }
                    }
                }
            }
        }

        File downloadedFile = new File(filePath);
        if (downloadedFile.exists()) {
            Long downloadedFileSize = downloadedFile.length();
            checkFileSize(downloadedFileSize, editMessageText, response.getYoutubeId());

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
            throw new NoSuchElementException(String.format("File '%s' did not download.", response.getYoutubeId()));
        }

        // downloading thumbnail
        final String thumbnailName = generateUniqueName() + ".jpg";
        String thumbnailPath = "";
        editMessageText = quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getThumbnailDownloading());

        processBuilder.command(commands.getThumbnailCommand(thumbnailName, request.getYoutubeId()));
        Process process = processBuilder.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            logger.info("[download] Downloading thumbnail to file '{}'", response.getYoutubeId());
            String line;
            while ((line = br.readLine()) != null) {
                editMessageText = updateMessageTextDownload(editMessageText, line);

                if (line.equals("[download] Destination: /Users/vicary/desktop/folder/" + thumbnailName))
                    thumbnailPath = commands.getPath() + thumbnailName;
            }
        }

        File downloadedThumbnail = new File(thumbnailPath);
        if (downloadedThumbnail.exists())
            logger.info("[download] Successfully downloaded thumbnail to file '{}'", response.getYoutubeId());
        else
            logger.warn("Thumbnail to file '{}' did not download.", response.getYoutubeId());

        response.setThumbnail(InputFile.builder()
                .file(new File(thumbnailPath))
                .isThumbnail(true)
                .build());
        // setting other stuff
        response.setEditMessageText(editMessageText);
        return response;
    }

    public YouTubeFileResponse getFileFromRepository(YouTubeFileResponse response) {
        Optional<YouTubeFileEntity> youTubeFileEntity = youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                response.getYoutubeId(),
                response.getExtension(),
                response.getPremium() ? "premium" : "standard");

        if (youTubeFileEntity.isPresent() && Converter.MBToBytes(youTubeFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(youTubeFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(Converter.MBToBytes(youTubeFileEntity.get().getSize()));
        }
        return response;
    }

    public YouTubeFileResponse getFileInfo(YouTubeFileRequest request, ProcessBuilder processBuilder) throws IOException {
        StringBuilder sb = new StringBuilder();

        processBuilder.command(commands.getFileInfoCommand(request.getYoutubeId()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }

        YouTubeFileInfo youTubeFileInfo = gson.fromJson(sb.toString(), YouTubeFileInfo.class);
        return mapper.map(youTubeFileInfo);
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
            quickSender.editMessageText(editMessageText, info.getFileTooBig() + info.getUpTo50Mb());
            throw new IllegalArgumentException(String.format("Size of file '%s' is too big. File size: %s", youtubeId, fileSize));
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