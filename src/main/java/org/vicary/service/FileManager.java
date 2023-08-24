package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class FileManager {
    private static Logger logger = LoggerFactory.getLogger(FileManager.class);

    public static boolean isFileSizeValid(long fileSize) {
        long sizeInMB = fileSize / (1024 * 1024);
        return sizeInMB <= 50;
    }

    public static String getFileNameFromTitle(String title, String extension) {
        int maxFileNameLength = 59;
        String newTitle = title;

        if (newTitle.length() > maxFileNameLength)
            newTitle = newTitle.substring(0, 59);

        newTitle = newTitle.replaceAll("&|⧸⧹", "and");
        newTitle = newTitle.replaceAll("[/⧸|｜–\\\\:]", "-");

        if (newTitle.length() > maxFileNameLength)
            newTitle = newTitle.substring(0, 59);

        return newTitle + "." + extension;
    }

    public static String getFileSizeInProcess(String line) {
        if (line.contains("[download]")) {
            String[] s = line.split(" ");
            for (String a : s)
                if (a.contains("MiB") || a.contains("KiB"))
                    return a;
        }
        return null;
    }

    public static boolean checkFileSizeProcess(String fileSize) {
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
        return Integer.parseInt(sb.toString()) <= 49;
    }

    public static String getDownloadFileProgressInProcess(String line) {
        if (line.contains("[download]")) {
            String[] s = line.split(" ");
            for (String a : s)
                if (a.contains("%"))
                    return MarkdownV2.apply(a).get();
        }
        return null;
    }

    public static boolean isFileConvertingInProcess(String line) {
        return line.startsWith("[ExtractAudio] Destination: /Users/vicary/desktop/folder/");
    }
}
