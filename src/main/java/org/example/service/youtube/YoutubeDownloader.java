package org.example.service.youtube;

import org.example.api_request.InputFile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class YoutubeDownloader {
    private final String commandName = "yt-dlp";
    private final String fileExtension = "-x";
    private final String commandFormat = "--audio-format";
    private final String format = "mp3";
    private final String audioQuality = "--audio-quality";
    private final String quality = "0";
    private final String commandPath = "-o";
    private final String path = "/Users/vicary/desktop/folder";
    private final String fileName = "/%(title)s.%(ext)s";

    public InputFile getMp3(String link) {
        String newMp3Path = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandName, fileExtension, commandFormat, format, commandPath, path + fileName, link);

        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/"))
                    newMp3Path = line.substring(28);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newMp3Path != null) {
            InputFile audio = InputFile.builder()
                    .file(new File(newMp3Path))
                    .build();
            return audio;
        }
        return null;
    }

    public InputFile getMp4(String link) {
        String newMp4Path = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandName,"-f", "worst[ext=mp4]", commandPath, path + fileName, link);

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

    public InputFile getM4a(String link) {
        String newM4aPath = null;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandName,"-f", "m4a", commandPath, path + fileName, link);

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
            System.out.printf("Deleting original file %s/%s\n", path, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}