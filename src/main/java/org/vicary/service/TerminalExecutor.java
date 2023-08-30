package org.vicary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class TerminalExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TerminalExecutor.class);
    private final static String REMOVE_COMMAND = "rm";
    private final static String REMOVE_COMMAND_ALL_FILES = "-f";
    private final static String REMOVE_COMMAND_ALL_FILES_STAR = "*";
    private final static String RENAME_COMMAND = "mv";

    public boolean removeFile(File file) {
        if (file == null || !file.exists()) {
            logger.warn("File to remove does not exists.");
            return false;
        }

        String fileName = file.getName();
        File fileDirectory = new File(file.getParent());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(REMOVE_COMMAND, fileName);
        processBuilder.directory(fileDirectory);
        try {
            Process process = processBuilder.start();
            process.waitFor();
            logger.info("[remove] Removing original file {}", fileName);
            return true;
        } catch (IOException | InterruptedException ex) {
            logger.warn("[remove] Failed in removing file {}: {}", fileName, ex.getMessage());
            return false;
        }
    }

    public File renameFile(File file, String newFileName) {
        String oldFileName = file.getName();
        File fileDirectory = new File(file.getParent());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(RENAME_COMMAND, oldFileName, newFileName);
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
