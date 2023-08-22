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
import org.vicary.entity.TikTokFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.TikTokFileService;
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
public class TikTokDownloader {

    private final static Logger logger = LoggerFactory.getLogger(TikTokDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final TikTokFileService tiktokFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    public FileResponse download(FileRequest request) throws WebClientRequestException, IllegalArgumentException, NoSuchElementException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        EditMessageText editMessageText = request.getEditMessageText();

        // SENDING INFO ABOUT CONNECTING TO TIKTOK
        quickSender.editMessageText(editMessageText, editMessageText.getText() + info.getConnectingToTikTok());

        // GETTING TIKTOK FILE INFO
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

        processBuilder.command(commands.getDownloadTikTokFile(response));
        Process process = processBuilder.start();
        // SENDING INFO ABOUT DOWNLOADING FILE
        logger.info("[download] Downloading TikTok file '{}'", response.getId());
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
            if (!line.contains("tiktok.com/")) {
                quickSender.editMessageText(editMessageText, info.getNoVideo());
                throw new IllegalArgumentException("TikTok URL without video but in description is link to other service.");
            }
    }

    public FileResponse getFileInfo(FileRequest request, ProcessBuilder processBuilder) throws IOException {
        String fileInfoInJson = "";

        processBuilder.command(commands.getDownloadFileInfo(request.getURL()));
        Process process = processBuilder.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileInfoInJson = line;
            }
        } catch (IOException ex) {
            process.destroy();
            throw new IOException(ex.getMessage());
        }

        if (fileInfoInJson.isEmpty()) {
            quickSender.editMessageText(request.getEditMessageText(), info.getNoVideo());
            throw new IllegalArgumentException(String.format("No video in TikTok URL '%s'", request.getURL()));
        }

        FileInfo fileInfo = gson.fromJson(fileInfoInJson, FileInfo.class);
        String uploaderUrl = fileInfo.getUploaderURL();
        if (uploaderUrl == null || !uploaderUrl.contains("tiktok.com/")) {
            quickSender.editMessageText(request.getEditMessageText(), info.getNoVideo());
            throw new IllegalArgumentException(String.format("No video in TikTok URL '%s' and other service URL in description.", request.getURL()));
        }
        FileResponse response = mapper.map(fileInfo);
        response.setExtension(request.getExtension());
        response.setPremium(request.isPremium());
        return response;
    }

    public FileResponse getFileFromRepository(FileResponse response) {
        Optional<TikTokFileEntity> tiktokFile = tiktokFileService.findByTwitterId(response.getId());

        if (tiktokFile.isPresent() && Converter.MBToBytes(tiktokFile.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(tiktokFile.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(Converter.MBToBytes(tiktokFile.get().getSize()));
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
                    newText.append("\\[").append(progress).append("\\]_");
                else
                    newText.append(s).append(" ");

            if (!oldText.contentEquals(newText))
                quickSender.editMessageText(editMessageText, newText.toString());
        }
        return editMessageText;
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
