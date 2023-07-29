package org.example.service.youtube;

import org.example.api_request.InputFile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class YoutubeDownloader {
    private final String commandName = "yt-dlp";
    private final String fileExtension = "-x";
    private final String commandFormat = "--audio-format";
    private final String format = "mp3";
    private final String audioQuality = "--audio-quality";
    private final String quality = "0";
    private final String commandPath = "-o";
    private final String path = "/Users/vicary/desktop/folder/";
    private final String fileName = "%(title)s.%(ext)s";
    private final String thumbnailLink = "https://i.ytimg.com/vi/";
    private final String thumbnailType = "/mqdefault.jpg";
    private final String youtubeLink = "https://youtu.be/";
    private final String embedThumbnail = "--embed-thumbnail";
    private final String maxFileSize = "--max-filesize";
    private final String fileSize = "45M";
    private final String mp3Extension = "mp3";


    public YouTubeFile getMp3(String youtubeId) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        String mp3Path = null;
        String fileSize = null;

        processBuilder.command(commandName, fileExtension, commandFormat, format, embedThumbnail, maxFileSize, this.fileSize, commandPath, path + fileName, youtubeLink + youtubeId);
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
                if (mp3Path == null)
                    mp3Path = getMp3Path(line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mp3Path != null) {
            return YouTubeFile.builder()
                    .youtubeId(youtubeId)
                    .file(InputFile.builder()
                            .file(new File(mp3Path))
                            .build())
                    .extension(mp3Extension)
                    .size(fileSize)
                    .build();
        }
        System.out.println("File size: " + fileSize);
        return null;
    }

    public InputFile getThumbnail(String videoId) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        final String thumbnailName = generateUniqueName() + ".jpg";
        String thumbnailPath = null;

        processBuilder.command(commandName, commandPath, path + thumbnailName, thumbnailLink + videoId + thumbnailType);
        try {
            Process process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.equals("[download] Destination: /Users/vicary/desktop/folder/" + thumbnailName))
                    thumbnailPath = "/Users/vicary/desktop/folder/" + thumbnailName;
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

    private boolean checkFileSize(String fileSize) {
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

    private String getMp3Path(String line) {
        if (line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/"))
            return line.substring(28);
        return null;
    }

    private String generateUniqueName() {
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


    public InputFile getMp4(String videoId) {
        String newMp4Path = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandName, "-f", "worst[ext=mp4]", commandPath, path + fileName, youtubeLink + videoId);

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

    public InputFile getM4a(String videoId) {
        String newM4aPath = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandName, "-f", "m4a", commandPath, path + fileName, youtubeLink + videoId);

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
        String deleteCommand = "rm";
        String fileName = inputFile.getFile().getName();
        ProcessBuilder processBuilder = new ProcessBuilder(deleteCommand, fileName);
        processBuilder.directory(new File(path));

        try {
            processBuilder.start();
            System.out.printf("Deleting original file %s%s\n", path, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}