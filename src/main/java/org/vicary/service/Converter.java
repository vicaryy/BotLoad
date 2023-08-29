package org.vicary.service;

import org.springframework.stereotype.Component;

@Component
public class Converter {
    public String bytesToMB(long Bytes) {
        double sizeInMB = (double) Bytes / (1024 * 1024);
        return String.format("%.2fMB", sizeInMB);
    }

    public Long MBToBytes(String MB) {
        if (MB == null || MB.isEmpty())
            return 0L;

        MB = MB.replaceFirst("MB", "");
        MB = MB.replaceFirst(",", ".");
        double Megabytes = Double.parseDouble(MB);
        return (long) (Megabytes * (1024 * 1024));
    }

    public String secondsToMinutes(int seconds) {
        boolean negative = seconds < 0;
        int minutes = seconds < 0 ? -seconds / 60 : seconds / 60;
        int sec = seconds < 0 ? -seconds % 60 : seconds % 60;
        return String.format("%s%d:%02d", negative ? "-" : "", minutes, sec);
    }
}
