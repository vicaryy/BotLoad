package org.vicary.service.youtube;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.model.YouTubeFileInfo;
import org.vicary.model.YouTubeFileRequest;
import org.springframework.stereotype.Service;
import org.vicary.model.YouTubeFileResponse;
import org.vicary.service.RequestService;
import org.vicary.service.YouTubeFileService;
import org.vicary.service.mapper.YouTubeFileMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class YouTubeDownloader {
    private final String ytDlpCommand = "yt-dlp";
    private final String fileExtensionCommand = "-x";
    private final String audioFormatCommand = "--audio-format";
    private final String audioQualityCommand = "--audio-quality";
    private final String pathCommand = "-o";
    private final String path = "/Users/vicary/desktop/folder/";
    private final String defaultFileName = "%(title)s.%(ext)s";
    private final String thumbnailLink = "https://i.ytimg.com/vi/";
    private final String thumbnailType = "/mqdefault.jpg";
    private final String youtubeUrl = "https://youtu.be/";
    private final String embedThumbnailCommand = "--embed-thumbnail";
    private final String maxFileSizeCommand = "--max-filesize";
    private final String maxFileSize = "45M";
    private final String deleteCommand = "rm";
    private final String renameCommand = "mv";
    private final String fileInfoCommand = "-j";

    private final RequestService requestService;
    private final YouTubeFileService youTubeFileService;
    private final YouTubeFileMapper mapper;
    private final Gson gson = new Gson();

    public YouTubeFileResponse download(YouTubeFileRequest request) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        final String connectingToYoutube = MarkdownV2.apply("Connecting to YouTube...").toItalic().newlineBefore().get();
        final String fileDownloadingInfo = MarkdownV2.apply("Downloading file... [0.0%]").toItalic().newlineBefore().get();
        final String thumbDownloadingInfo = MarkdownV2.apply("Downloading thumbnail... [0.0%]").toItalic().newlineBefore().get();
        final String fileTooBigInfo = MarkdownV2.apply("File is too big!").toBold().newlineAfter().newlineAfter().get();
        final String upTo50MBInfo = MarkdownV2.apply("Sorry but i can upload files up to 50MB (Telegrams fault).\nThings may change in time.").get();
        final String convertingInfo = MarkdownV2.apply(String.format("Converting to %s...", request.getExtension())).toItalic().newlineBefore().get();
        final String renamingInfo = MarkdownV2.apply("Renaming...").toItalic().newlineBefore().get();
        boolean fileDownloaded = false;
        boolean fileConverted = false;
        boolean premium = request.getPremium();
        EditMessageText editMessageText = request.getEditMessageText();

        String quality = premium ? "0" : "5";
        String extension = request.getExtension();

        String filePath = null;
        String fileSize = null;

        // getting youtube file info
        editMessageText = sendEditMessageText(editMessageText, editMessageText.getText() + connectingToYoutube);
        YouTubeFileResponse response = getFileInfo(request, processBuilder);
        response.setExtension(extension);
        response.setPremium(request.getPremium());


        // checks if file already exists in repository
        response = getFileFromRepository(response);

        // if file is not in repo then download FILE
        if (response.getDownloadedFile() == null) {
            filePath = String.format("%s%s.%s", path, response.getTitle(), extension);
            editMessageText.setText(editMessageText.getText() + fileDownloadingInfo);
            processBuilder.command(ytDlpCommand, fileExtensionCommand, audioFormatCommand, extension, audioQualityCommand, quality, embedThumbnailCommand, maxFileSizeCommand, this.maxFileSize, pathCommand, filePath, youtubeUrl + request.getYoutubeId());
            Process process = processBuilder.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);

                    if (!fileDownloaded) {
                        editMessageText = updateMessageTextDownload(request.getEditMessageText(), line);
                        if (request.getEditMessageText().getText().endsWith("[100%]"))
                            fileDownloaded = true;
                    }

                    if (!fileConverted && isFileConverting(line)) {
                        editMessageText = sendEditMessageText(editMessageText, editMessageText.getText() + convertingInfo);
                        fileConverted = true;
                    }

                    if (fileSize == null) {
                        fileSize = getFileSize(line);
                        if (fileSize != null && !checkFileSize(fileSize)) {
                            editMessageText = sendEditMessageText(editMessageText, fileTooBigInfo + upTo50MBInfo);
                            process.destroy();
                        }
                    }
                }
            }
            if (new File(filePath).exists()) {
                response.setSize(new File(filePath).length());
                String oldFilePath = filePath;
                filePath = correctFilePath(filePath, extension);
                if (!oldFilePath.equals(filePath)) {
                    editMessageText = sendEditMessageText(editMessageText, editMessageText.getText() + renamingInfo);
                }
                File downladedFile = new File(filePath);
                response.setDownloadedFile(InputFile.builder()
                        .file(downladedFile)
                        .build());
            }
        }

        // downloading thumbnail
        final String thumbnailName = generateUniqueName() + ".jpg";
        String thumbnailPath = null;
        editMessageText.setText(editMessageText.getText() + thumbDownloadingInfo);

        processBuilder.command(ytDlpCommand, pathCommand, path + thumbnailName, thumbnailLink + request.getYoutubeId() + thumbnailType);
        Process process = processBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);

            editMessageText = updateMessageTextDownload(editMessageText, line);

            if (line.equals("[download] Destination: /Users/vicary/desktop/folder/" + thumbnailName))
                thumbnailPath = this.path + thumbnailName;
        }

        if (thumbnailPath != null) {
            response.setThumbnail(InputFile.builder()
                    .file(new File(thumbnailPath))
                    .isThumbnail(true)
                    .build());
        }

        // setting other stuff
        response.setEditMessageText(editMessageText);
        response.setExtension(extension);
        response.setPremium(premium);
        return response;
    }

    public YouTubeFileResponse getFileFromRepository(YouTubeFileResponse response) {
        Optional<YouTubeFileEntity> youTubeFileEntity = youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                response.getYoutubeId(),
                response.getExtension(),
                response.getPremium() ? "premium" : "standard");

        if (youTubeFileEntity.isPresent() && convertMBToBytes(youTubeFileEntity.get().getSize()) < 20000000) {
            InputFile file = InputFile.builder()
                    .fileId(youTubeFileEntity.get().getFileId())
                    .build();
            response.setDownloadedFile(file);
            response.setSize(convertMBToBytes(youTubeFileEntity.get().getSize()));
        }
        return response;
    }

    public YouTubeFileResponse getFileInfo(YouTubeFileRequest request, ProcessBuilder processBuilder) throws Exception {
        StringBuilder sb = new StringBuilder();

        processBuilder.command(ytDlpCommand, fileInfoCommand, youtubeUrl + request.getYoutubeId());
        Process process = processBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        while (br.readLine() != null)
            sb.append(br.readLine());

        YouTubeFileInfo youTubeFileInfo = gson.fromJson(sb.toString(), YouTubeFileInfo.class);
        return mapper.map(youTubeFileInfo);
    }

    public EditMessageText updateMessageTextDownload(EditMessageText editMessageText, String line) throws Exception {
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
                sendEditMessageText(editMessageText, newText.toString());
        }
        return editMessageText;
    }

    public EditMessageText sendEditMessageText(EditMessageText editMessageText, String text) throws Exception {
        editMessageText.setText(text);
        requestService.sendRequestAsync(editMessageText);
        return editMessageText;
    }

    public Boolean isFileConverting(String line) {
        return line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/");
    }

    private Long convertMBToBytes(String MB) {
        MB = MB.replaceFirst("MB", "");
        MB = MB.replaceFirst(",", ".");
        double Megabytes = Double.parseDouble(MB);
        return (long) (Megabytes * (1024 * 1024));
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


    public String correctFilePath(String filePath, String extension) throws Exception {
        int maxFileNameLength = 63;
        String oldFileName = filePath.replaceFirst(path, "");
        String newFileName = oldFileName;

        newFileName = newFileName.replaceAll("&|⧸⧹", "and");
        newFileName = newFileName.replaceAll("[/⧸||｜–\\\\]", "-");

        if (newFileName.length() > maxFileNameLength)
            newFileName = newFileName.substring(0, 59) + "." + extension;

        if (newFileName.equals(oldFileName))
            return filePath;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(renameCommand, oldFileName, newFileName);
        processBuilder.directory(new File(path));
        Process process = processBuilder.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (br.readLine() != null)
                System.out.println(br.readLine());
        }
        System.out.println("[rename] Renaming file to " + newFileName);
        return path + newFileName;
    }

    public boolean checkFileSize(String fileSize) {
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

    public void deleteFile(InputFile inputFile) throws Exception {
        String fileName = inputFile.getFile().getName();
        ProcessBuilder processBuilder = new ProcessBuilder(deleteCommand, fileName);
        processBuilder.directory(new File(path));

        processBuilder.start();
        System.out.printf("[delete] Deleting original file %s%s\n", path, fileName);
    }
}