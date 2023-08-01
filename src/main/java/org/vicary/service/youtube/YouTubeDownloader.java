package org.vicary.service.youtube;

import org.vicary.api_request.InputFile;
import org.vicary.model.YouTubeFileRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;

@Service
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

    public InputFile downloadMp3(YouTubeFileRequest request) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String quality = request.getPremium() ? "0" : "5";
        String extension = request.getExtension();

        String filePath = null;
        String fileSize = null;

        processBuilder.command(ytDlpCommand, fileExtensionCommand, audioFormatCommand, extension, audioQualityCommand, quality, embedThumbnailCommand, maxFileSizeCommand, this.maxFileSize, pathCommand, path + defaultFileName, youtubeUrl + request.getYoutubeId());
        try {
            Process process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (fileSize == null) {
                    fileSize = getFileSize(line);
                    if (fileSize != null && !checkFileSize(fileSize))
                        process.destroy();
                }
                if (filePath == null)
                    filePath = getMp3Path(line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (filePath != null) {
            return InputFile.builder()
                    .file(new File(correctFilePath(filePath, extension)))
                    .build();
        }
        return null;
    }

    public String correctFilePath(String filePath, String extension) {
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
        try {
            processBuilder.start();
            System.out.printf("[rename] Renaming file to %s\n", newFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path + newFileName;
    }

    public InputFile downloadThumbnail(String videoId) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        final String thumbnailName = generateUniqueName() + ".jpg";
        String thumbnailPath = null;

        processBuilder.command(ytDlpCommand, pathCommand, path + thumbnailName, thumbnailLink + videoId + thumbnailType);
        try {
            Process process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.equals("[download] Destination: /Users/vicary/desktop/folder/" + thumbnailName))
                    thumbnailPath = this.path + thumbnailName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (thumbnailPath != null) {
            return InputFile.builder()
                    .file(new File(thumbnailPath))
                    .isThumbnail(true)
                    .build();
        }
        return null;
    }

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