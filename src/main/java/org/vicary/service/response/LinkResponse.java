package org.vicary.service.response;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendVideo;
import org.vicary.format.MarkdownV2;
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.*;
import org.vicary.service.downloader.Downloader;
import org.vicary.service.file_service.FileService;
import org.vicary.service.quick_sender.QuickSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkResponse {
    private final static Logger logger = LoggerFactory.getLogger(LinkResponse.class);

    private final ResponseInfo info;

    private final RequestService requestService;

    private final QuickSender quickSender;

    private final Converter converter;

    private final TerminalExecutor terminalExecutor;


    public void sendFile(FileRequest request, Downloader downloader, FileService fileService) throws Exception {
        final String chatId = request.getChatId();
        // getting file
        FileResponse response = downloader.download(request);
        response.setChatId(chatId);


        // sending audio to telegram chat
        logger.info("[send] Sending file '{}' to chatId '{}'", response.getId(), chatId);
        quickSender.chatAction(chatId, "upload_document");
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getSending());
        Message sendFileMessage;
        if (response.getExtension().equals("mp3")) {
            sendFileMessage = requestService.sendRequest(getSendAudio(response));
            response.setTelegramFileId(sendFileMessage.getAudio().getFileId());
            if (response.getDuration() == 0)
                response.setDuration(sendFileMessage.getAudio().getDuration());
        } else if (response.getExtension().equals("mp4")) {
            sendFileMessage = requestService.sendRequest(getSendVideo(response));
            response.setTelegramFileId(sendFileMessage.getVideo().getFileId());
            if (response.getDuration() == 0)
                response.setDuration(sendFileMessage.getVideo().getDuration());
        }
        quickSender.editMessageText(request.getEditMessageText(), getReceivedFileInfo(response, downloader.getServiceName()));
        logger.info("[send] File sent successfully.");

        // saving file to repository
        if (!fileService.existsInRepo(response)) {
            fileService.saveInRepo(response);
        }

        // deleting downloaded files
        if (response.getDownloadedFile().getFile() != null)
            terminalExecutor.removeFile(response.getDownloadedFile().getFile());
        if (response.getThumbnail() != null)
            terminalExecutor.removeFile(response.getThumbnail().getFile());
    }

    public String getReceivedFileInfo(FileResponse response, String serviceName) {
        StringBuilder fileInfo = new StringBuilder();

        final String title = response.getTitle();
        final String artist = response.getArtist();
        final String track = response.getTrack();
        final String album = response.getAlbum();
        final String releaseYear = response.getReleaseYear();
        final String duration = converter.secondsToMinutes(response.getDuration());
        final String size = converter.bytesToMB(response.getSize());
        final String extension = response.getExtension();
        final String quality = response.isPremium() ? "premium" : "standard";
        final boolean youtube = serviceName.equals("youtube");

        fileInfo.append(MarkdownV2.apply(info.getReceived()).toItalic().newlineAfter().get());
        if (youtube) {
            if (track == null) {
                fileInfo.append(info.getTitle());
                fileInfo.append(MarkdownV2.apply(title).get());
            }
            if (artist != null) {
                fileInfo.append(info.getArtist());
                fileInfo.append(MarkdownV2.apply(artist).get());
            }
            if (track != null) {
                fileInfo.append(info.getTrack());
                fileInfo.append(MarkdownV2.apply(track).get());
            }
            if (album != null) {
                fileInfo.append(info.getAlbum());
                fileInfo.append(MarkdownV2.apply(album).get());
            }
            if (releaseYear != null) {
                fileInfo.append(info.getReleaseYear());
                fileInfo.append(MarkdownV2.apply(releaseYear).get());
            }
        }
        if (!youtube) {
            fileInfo.append(info.getTitle());
            fileInfo.append(MarkdownV2.apply(title).get());
        }
        if (!duration.equals("0:00")) {
            fileInfo.append(info.getDuration());
            fileInfo.append(MarkdownV2.apply(duration).get());
        }
        if (size != null) {
            fileInfo.append(info.getSize());
            fileInfo.append(MarkdownV2.apply(size).get());
        }
        if (extension != null) {
            fileInfo.append(info.getExtension());
            fileInfo.append(MarkdownV2.apply(extension).get());
        }
        fileInfo.append(info.getQuality());
        fileInfo.append(MarkdownV2.apply(quality).get());

        return fileInfo.toString();
    }

    public SendAudio getSendAudio(FileResponse response) {
        SendAudio sendAudio = SendAudio.builder()
                .chatId(response.getChatId())
                .audio(response.getDownloadedFile())
                .title(response.getTrack())
                .performer(response.getArtist())
                .duration(response.getDuration())
                .build();
        if (response.getDownloadedFile().getFileId() == null)
            sendAudio.setThumbnail(response.getThumbnail());
        return sendAudio;
    }

    public SendVideo getSendVideo(FileResponse response) {
        return SendVideo.builder()
                .chatId(response.getChatId())
                .video(response.getDownloadedFile())
                .duration(response.getDuration())
                .build();
    }
}
