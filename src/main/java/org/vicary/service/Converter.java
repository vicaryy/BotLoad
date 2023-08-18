package org.vicary.service;

public class Converter {
    public static String bytesToMB(Long Bytes) {
        double sizeInMB = (double) Bytes / (1024 * 1024);
        return String.format("%.2fMB", sizeInMB);
    }

    public static Long MBToBytes(String MB) {
        MB = MB.replaceFirst("MB", "");
        MB = MB.replaceFirst(",", ".");
        double Megabytes = Double.parseDouble(MB);
        return (long) (Megabytes * (1024 * 1024));
    }

    public static String secondsToMinutes(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d", minutes, sec);
    }
}
