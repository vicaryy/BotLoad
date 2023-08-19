package org.vicary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.command.TerminalCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class TerminalExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TerminalExecutor.class);
    private static final TerminalCommand commands = new TerminalCommand();

    public static void removeFile(File file) {
        if (file == null || !file.exists()) {
            logger.warn("File '{}' does not exists.", file.getName());
            return;
        }

        String fileName = file.getName();
        File fileDirectory = new File(file.getParent());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands.getRemoveFile(), fileName);
        processBuilder.directory(fileDirectory);
        try {
            processBuilder.start();
            logger.info("[remove] Removing original file {}", fileName);
        } catch (IOException ex) {
            logger.warn("[remove] Failed in removing file {}: {}", fileName, ex.getMessage());
        }
    }

    public static File renameFile(File file, String newFileName) {
        String oldFileName = file.getName();
        File fileDirectory = new File(file.getParent());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands.getRenameCommand(), oldFileName, newFileName);
        processBuilder.directory(fileDirectory);
        try {
            Process process = processBuilder.start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.print(line);
                }
            }
            logger.info("[rename] Renaming file '{}' to: '{}'", oldFileName, newFileName);
        } catch (IOException ex) {
            logger.warn("[remove] Failed in renaming file {}: {}", oldFileName, ex.getMessage());
            return file;
        }
        return new File(fileDirectory + "/" + newFileName);
    }
}
