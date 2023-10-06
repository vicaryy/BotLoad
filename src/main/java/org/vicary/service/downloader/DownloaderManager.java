package org.vicary.service.downloader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.format.MarkdownV2;
import org.vicary.service.Converter;
import org.vicary.service.quick_sender.QuickSender;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DownloaderManager {

    private final Converter converter;

    private final QuickSender quickSender;

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
        if (fileSize.endsWith("KiB") || fileSize.isEmpty())
            return true;
        if (!fileSize.endsWith("MiB"))
            return false;

        StringBuilder sb = new StringBuilder();
        for (char c : fileSize.toCharArray()) {
            if (c == '.')
                break;
            sb.append(c);
        }
        return Integer.parseInt(sb.toString()) <= 49;
    }

    public String getDownloadProgressInProcess(String line) {
        String[] s = line.split(" ");
        for (String a : s)
            if (a.contains("%"))
                return a;
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


    public EditMessageText updateDownloadProgressInEditMessageText(EditMessageText editMessageText, String line) {
        String progress = getDownloadProgressInProcess(line);

        if (progress != null && isProgressDifferenceProper(editMessageText.getText(), progress)) {
            String oldText = editMessageText.getText();
            String[] splitOldText = oldText.split(" ");
            StringBuilder newText = new StringBuilder();

            for (String s : splitOldText)
                if (s.equals(splitOldText[splitOldText.length - 1]))
                    newText.append(MarkdownV2.apply("[" + progress + "]").get() + "_");
                else
                    newText.append(s).append(" ");

            if (!oldText.contentEquals(newText))
                quickSender.editMessageText(editMessageText, newText.toString());
        }
        return editMessageText;
    }

    public boolean isProgressDifferenceProper(String editMessageTextText, String newProgress) {
        final int differenceInProgress = 2;
        String[] oldProgressArray = editMessageTextText.split(" ");
        try {
            double oldProgressInDouble = Double.parseDouble(oldProgressArray[oldProgressArray.length - 1].replaceAll("[\\\\%_\\[\\]]", ""));
            double newProgressInDouble = Double.parseDouble(newProgress.replaceFirst("%", ""));
            if (newProgressInDouble - oldProgressInDouble > differenceInProgress || newProgressInDouble == 100)
                return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

}
