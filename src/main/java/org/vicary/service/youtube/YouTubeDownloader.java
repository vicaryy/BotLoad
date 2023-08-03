package org.vicary.service.youtube;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.model.YouTubeFileInfo;
import org.vicary.model.YouTubeFileRequest;
import org.springframework.stereotype.Service;
import org.vicary.model.YouTubeFileResponse;
import org.vicary.repository.YoutubeFileRepository;
import org.vicary.service.RequestService;
import org.vicary.service.mapper.YouTubeFileMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private final YoutubeFileRepository youtubeFileRepository;
    private final YouTubeFileMapper mapper;
    private final Gson gson = new Gson();

    public YouTubeFileResponse downloadMp3(YouTubeFileRequest request) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        final String connectingToYoutube = "\nConnecting to YouTube...";
        final String fileDownloadingInfo = "\nDownloading file... [0.0%]";
        final String thumbDownloadingInfo = "\nDownloading thumbnail... [0.0%]";
        final String convertingInfo = String.format("\nConverting to %s...", request.getExtension());
        final String renamingInfo = "\nRenaming...";
        boolean fileDownloaded = false;
        boolean fileConverted = false;

        String quality = request.getPremium() ? "0" : "5";
        String extension = request.getExtension();

        String filePath = null;
        String fileSize = null;

        // getting youtube file info
        request.getEditMessageText().setText(request.getEditMessageText().getText() + connectingToYoutube);
        YouTubeFileResponse response = getFileInfo(request, processBuilder);

        // checks if file already exists in repository
        response.setDownloadedFile(getFileFromRepository(request));

        // if file is not in repo then download FILE
        if (response.getDownloadedFile() == null) {
            processBuilder.command(ytDlpCommand, fileExtensionCommand, audioFormatCommand, extension, audioQualityCommand, quality, embedThumbnailCommand, maxFileSizeCommand, this.maxFileSize, pathCommand, path + defaultFileName, youtubeUrl + request.getYoutubeId());
            Process process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);

                if (!fileDownloaded) {
                    if (line.contains("[download]") && !request.getEditMessageText().getText().contains(fileDownloadingInfo))
                        request.getEditMessageText().setText(request.getEditMessageText().getText() + fileDownloadingInfo);
                    request.setEditMessageText(updateMessageTextDownload(request.getEditMessageText(), line));
                    if (request.getEditMessageText().getText().endsWith("[100%]"))
                        fileDownloaded = true;
                }

                if (!fileConverted && isFileConverting(line)) {
                    request.getEditMessageText().setText(request.getEditMessageText().getText() + convertingInfo);
                    requestService.sendRequestAsync(request.getEditMessageText());
                    fileConverted = true;
                }

                if (fileSize == null) {
                    fileSize = getFileSize(line);
                    if (fileSize != null && !checkFileSize(fileSize))
                        process.destroy();
                }
                if (filePath == null)
                    filePath = getMp3Path(line);
            }

            if (filePath != null) {
                String oldFilePath = filePath;
                filePath = correctFilePath(filePath, extension);
                if (oldFilePath.equals(filePath)) {
                    request.getEditMessageText().setText(request.getEditMessageText().getText() + renamingInfo);
                    requestService.sendRequestAsync(request.getEditMessageText());
                }
                response.setDownloadedFile(InputFile.builder()
                        .file(new File(correctFilePath(filePath, extension)))
                        .build());
            }
        }

        // downloading thumbnail
        final String thumbnailName = generateUniqueName() + ".jpg";
        String thumbnailPath = null;
        request.getEditMessageText().setText(request.getEditMessageText().getText() + thumbDownloadingInfo);

        processBuilder.command(ytDlpCommand, pathCommand, path + thumbnailName, thumbnailLink + request.getYoutubeId() + thumbnailType);
        Process process = processBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);

            request.setEditMessageText(updateMessageTextDownload(request.getEditMessageText(), line));

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
        response.setEditMessageText(request.getEditMessageText());
        response.setExtension(request.getExtension());
        response.setPremium(request.getPremium());

        return response;
    }

    public InputFile getFileFromRepository(YouTubeFileRequest request) {
        YouTubeFileEntity youTubeFileEntity = youtubeFileRepository.findByYoutubeIdAndExtensionAndQuality(
                request.getYoutubeId(),
                request.getExtension(),
                request.getPremium() ? "premium" : "standard");
        if (youTubeFileEntity != null) {
            return InputFile.builder()
                    .fileId(youTubeFileEntity.getFileId())
                    .build();
        }
        return null;
    }

    public YouTubeFileResponse getFileInfo(YouTubeFileRequest request, ProcessBuilder processBuilder) throws Exception {
        StringBuilder sb = new StringBuilder();

        processBuilder.command(ytDlpCommand, fileInfoCommand, youtubeUrl + request.getYoutubeId());
        Process process = processBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        YouTubeFileInfo youTubeFileInfo = gson.fromJson(sb.toString(), YouTubeFileInfo.class);
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(prettyGson.toJson(youTubeFileInfo));
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
                    newText.append("[" + progress + "]");
                else
                    newText.append(s + " ");

            editMessageText.setText(newText.toString());
            if (!oldText.contentEquals(newText))
                requestService.sendRequestAsync(editMessageText);
        }
        return editMessageText;
    }

    public Boolean isFileConverting(String line) {
        if (line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/"))
            return true;
        return false;
    }

    public String getDownloadProgress(String line) {
        if (line.contains("[download]")) {
            String[] s = line.split(" ");
            for (String a : s)
                if (a.contains("%"))
                    return a;
        }
        return null;
    }

    public String correctFilePath(String filePath, String extension) throws Exception {
        String oldFileName = filePath.replaceFirst(path, "");
        String newFileName = oldFileName;

        newFileName = newFileName.replaceAll("&|⧸⧹", "and");
        newFileName = newFileName.replaceAll("[/⧸||｜–]", "-");

        if (newFileName.length() > 64)
            newFileName = newFileName.substring(0, 60) + "." + extension;

        if (newFileName.equals(oldFileName))
            return filePath;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(renameCommand, oldFileName, newFileName);
        processBuilder.directory(new File(path));
        processBuilder.start();
        System.out.printf("[rename] Renaming file to %s\n", newFileName);
        return path + newFileName;
    }

//    public YouTubeFileRequest downloadThumbnail(YouTubeFileRequest request) throws Exception {
//        final String thumbDownloadingInfo = "\nDownloading thumbnail... [0.0%]";
//        final String thumbnailName = generateUniqueName() + ".jpg";
//        String thumbnailPath = null;
//        request.getEditMessageText().setText(request.getEditMessageText().getText() + thumbDownloadingInfo);
//
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command(ytDlpCommand, pathCommand, path + thumbnailName, thumbnailLink + request.getYoutubeId() + thumbnailType);
//        Process process = processBuilder.start();
//        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//
//            request.setEditMessageText(updateMessageTextDownload(request.getEditMessageText(), line));
//
//            if (line.equals("[download] Destination: /Users/vicary/desktop/folder/" + thumbnailName))
//                thumbnailPath = this.path + thumbnailName;
//        }
//
//        if (thumbnailPath != null) {
//            request.setThumbnail(InputFile.builder()
//                    .file(new File(thumbnailPath))
//                    .isThumbnail(true)
//                    .build());
//        }
//        return request;
//    }

    public boolean checkFileSize(String fileSize) {
        if (!fileSize.endsWith("MiB") && !fileSize.endsWith("KiB"))
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

    public String getMp3Path(String line) {
        if (line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/"))
            return line.substring(28);
        return null;
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


    public InputFile downloadMp4(String videoId) {
        String newMp4Path = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(ytDlpCommand, "-f", "worst[ext=mp4]", pathCommand, path + defaultFileName, youtubeUrl + videoId);

        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("[download] Destination: /Users/vicary/desktop/folder/"))
                    newMp4Path = line.substring(24);
                System.out.println(newMp4Path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newMp4Path != null) {
            InputFile video = InputFile.builder()
                    .file(new File(newMp4Path))
                    .build();
            return video;
        }
        return null;
    }

    public InputFile downloadM4a(String videoId) {
        String newM4aPath = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(ytDlpCommand, "-f", "m4a", pathCommand, path + defaultFileName, youtubeUrl + videoId);

        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("[download] Destination: /Users/vicary/desktop/folder/"))
                    newM4aPath = line.substring(24);
                System.out.println(newM4aPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newM4aPath != null) {
            InputFile audio = InputFile.builder()
                    .file(new File(newM4aPath))
                    .build();
            return audio;
        }
        return null;
    }

    public boolean deleteFile(InputFile inputFile) {
        String fileName = inputFile.getFile().getName();
        ProcessBuilder processBuilder = new ProcessBuilder(deleteCommand, fileName);
        processBuilder.directory(new File(path));

        try {
            processBuilder.start();
            System.out.printf("[delete] Deleting original file %s%s\n", path, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}