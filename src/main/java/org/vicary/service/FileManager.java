package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FileManager {

    private final Converter converter;

    public boolean isFileSizeValid(long fileSize) {
        long sizeInMB = fileSize / (1024 * 1024);
        return sizeInMB <= 50;
    }

    public String getFileNameFromTitle(String title, String extension) {
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

    public String getFileSizeInProcess(String line) {
        if (line.startsWith("[download] File is larger than max-filesize")) {
            long size = 0;
            String[] arraySplit = line.split("\\(");
            size = Arrays.stream(arraySplit[1].split(" "))
                    .findFirst()
                    .map(Long::parseLong)
                    .orElse(0L);
            return converter.bytesToMB(size);
        } else {
            String[] s = line.split(" ");
            for (String a : s)
                if (a.contains("MiB") || a.contains("KiB"))
                    return a;
            return "";
        }
    }

    public boolean isFileSizeValidInProcess(String line) {
        if (line.startsWith("[download] File is larger than max-filesize"))
            return false;
        String fileSize = getFileSizeInProcess(line);
        if (fileSize.isEmpty() || fileSize.endsWith("KiB"))
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

    public String getDownloadFileProgressInProcess(String line) {
        String[] s = line.split(" ");
        for (String a : s)
            if (a.contains("%"))
                return MarkdownV2.apply(a).get();
        return null;
    }

    public boolean isFileDownloadingInProcess(String line) {
        return line.startsWith("[download]");
    }

    public boolean isFileDownloadedInProcess(String line) {
        return line.contains("100%");
    }

    public boolean isFileConvertingInProcess(String line) {
        return line.startsWith("[ExtractAudio]");
    }
}
