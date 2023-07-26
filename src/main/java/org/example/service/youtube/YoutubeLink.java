package org.example.service.youtube;

import org.example.api_request.InputFile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class YoutubeLink {
    private final String commandName = "yt-dlp";
    private final String fileExtension = "-x";
    private final String commandFormat = "--audio-format";
    private final String format = "mp3";
    private final String audioQuality = "--audio-quality";
    private final String quality = "0";
    private final String commandPath = "-o";
    private final String path = "/Users/vicary/desktop/folder";
    private final String fileName = "/%(title)s.%(ext)s";

    public InputFile getAudio(String link) {

        String newAudioPath = null;

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
                    newAudioPath = line.substring(28);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newAudioPath != null) {
            InputFile audio = InputFile.builder()
                    .file(new File(newAudioPath))
                    .build();
            return audio;
        }
        return null;
    }

    public boolean deleteAudio(InputFile audio) {
        String deleteCommand = "rm";
        String fileName = audio.getFile().getName();
        ProcessBuilder processBuilder = new ProcessBuilder(deleteCommand, fileName);
        processBuilder.directory(new File(path));

        try {
            processBuilder.start();
            System.out.printf("Deleting original file %s/%s", path, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}