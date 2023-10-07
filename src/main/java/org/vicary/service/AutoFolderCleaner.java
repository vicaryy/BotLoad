package org.vicary.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.vicary.configuration.BotInfo;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AutoFolderCleaner implements Runnable {

    private final ActiveRequestService activeRequestService;
    private final static Logger logger = LoggerFactory.getLogger(AutoFolderCleaner.class);
    private final static int CLEAN_FOLDER_DELAY = 10000;
    private File fileDestination;


    @PostConstruct
    private void startThread() {
        fileDestination = new File(BotInfo.getDownloadDestination());
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            checkingDelay();
            int amountOfActiveUsers = activeRequestService.countActiveUsers();

            if (amountOfActiveUsers == 0) {
                String[] files = FileUtil.listFiles(fileDestination);
                int amountOfFiles = files.length;

                if (amountOfFiles > 0) {
                    cleanFolder(amountOfFiles);
                }
            }
        }
    }


    private void checkingDelay() {
        try {
            Thread.sleep(CLEAN_FOLDER_DELAY);
        } catch (InterruptedException ignored) {
        }
    }

    private void cleanFolder(int amountOfFiles) {
        String fileWord = amountOfFiles == 1 ? "file" : "files";
        try {
            FileUtils.cleanDirectory(fileDestination);
            logger.info("[Auto Folder Cleaner] Deleted {} {}.", amountOfFiles, fileWord);
        } catch (IOException e) {
            logger.warn("[Auto Folder Cleaner] Failed in deleting {} {}.", amountOfFiles, fileWord);
        }
    }
}
