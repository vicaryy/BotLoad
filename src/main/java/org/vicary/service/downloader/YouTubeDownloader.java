package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.springframework.stereotype.Service;
import org.vicary.pattern.YoutubePattern;
import org.vicary.service.Converter;
import org.vicary.service.TerminalExecutor;
import org.vicary.service.YouTubeFileService;
import org.vicary.service.mapper.FileInfoMapper;
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

    private final FileInfoMapper mapper;

    private final YtDlpCommand commands;

    private final DownloaderInfo info;

    private final QuickSender quickSender;

    private final Gson gson;


    public FileResponse download(FileRequest request) throws WebClientRequestException, IllegalArgumentException, NoSuchElementException, IOException {
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
        String filePath = getFileNameFromTitle(response.getTitle(), response.getExtension());
        String fileSize = null;
        boolean fileDownloaded = false;
        boolean fileConverted = false;
        editMessageText.setText(editMessageText.getText() + info.getFileDownloading());
        processBuilder.command(commands.getDownloadYouTubeFile(response));
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

                if (!fileConverted && isFileConverting(line)) {
                    quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConverting(request.getExtension()));
                    logger.info("[convert] Successfully converted to {} file '{}'", response.getExtension(), response.getId());
                    fileConverted = true;
                }

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

            String oldFileName = downloadedFile.getName();
            downloadedFile = correctFilePath(downloadedFile, request.getExtension());

            if (!oldFileName.equals(downloadedFile.getName()))
                quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getRenaming());

            response.setSize(downloadedFileSize);
            response.setDownloadedFile(InputFile.builder()
                    .file(downloadedFile)
                    .build());
        } else {
            quickSender.editMessageText(editMessageText, info.getErrorInDownloading());
            throw new NoSuchElementException(String.format("File '%s' did not download.", response.getId()));
        }

        // downloading thumbnail
        final String thumbnailName = generateUniqueName() + ".jpg";
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
            quickSender.editMessageText(request.getEditMessageText(), info.getNoVideo());
            throw new IllegalArgumentException(String.format("No video in YouTube URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);
        FileResponse fileResponse = mapper.map(fileInfo);
        fileResponse.setPremium(request.isPremium());
        fileResponse.setExtension(request.getExtension());
        return fileResponse;
    }

    public String getFileNameFromTitle(String title, String extension) {
        int maxFileNameLength = 59;
        String newTitle = title;

        if (newTitle.length() > maxFileNameLength)
            newTitle = newTitle.substring(0, 59);

        newTitle = newTitle.replaceAll("&|⧸⧹", "and");
        newTitle = newTitle.replaceAll("[/⧸||｜–\\\\]", "-");

        if (newTitle.length() > maxFileNameLength)
            newTitle = newTitle.substring(0, 59);

        return newTitle + "." + extension;
    }

    public EditMessageText updateMessageTextDownload(EditMessageText editMessageText, String line) {
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
            quickSender.editMessageText(editMessageText, info.getFileTooBig());
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