package org.vicary.service.downloader;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.command.YtDlpCommand;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.FileManager;
import org.vicary.service.file_service.TikTokFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoundCloudDownloader implements Downloader {

    private final static Logger logger = LoggerFactory.getLogger(SoundCloudDownloader.class);

    private final QuickSender quickSender;

    private final DownloaderInfo info;

    private final YtDlpCommand commands;

    private final TikTokFileService tiktokFileService;

    private final FileInfoMapper mapper;

    private final Gson gson;

    private final Converter converter;

    private final FileManager fileManager;

    private final List<String> availableExtensions = List.of("mp3", "m4a", "flac", "wav");

    @Override
    public FileResponse download(FileRequest request) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(commands.getDownloadDestination()));
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getConnectingToTwitter());

        // GETTING FILE INFO
        FileResponse response = getFileInfo(request, processBuilder);

        // CHECKS IF FILE ALREADY EXISTS IN REPOSITORY
        getFileFromRepository(response);
        if (response.getDownloadedFile() != null)
            return response;

        // IF FILE DOES NOT EXIST IN REPOSITORY THEN DOWNLOAD
        downloadFile(response, processBuilder);

        return response;
    }

    public FileResponse getFileInfo(FileRequest request) {

    }

    @Override
    public List<String> getAvailableExtensions() {
        return availableExtensions;
    }

    @Override
    public String getServiceName() {
        return "soundcloud";
    }
}
