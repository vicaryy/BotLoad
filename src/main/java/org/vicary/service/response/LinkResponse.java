package org.vicary.service.response;

import com.mpatric.mp3agic.ID3v1Genres;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendDocument;
import org.vicary.api_request.send.SendVideo;
import org.vicary.format.MarkdownV2;
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.model.ID3TagData;
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

    private final ID3TagService id3TagService;


    public void sendFile(FileRequest request, Downloader downloader, FileService fileService) throws Exception {
        final String chatId = request.getChatId();
        // getting file
        FileResponse response = downloader.download(request);
        response.setChatId(chatId);


        if (request.getId3TagData() != null)
            response.setId3TagData(request.getId3TagData());

        if (response.getId3TagData() != null)
            id3TagService.addID3Tag(response);

        // sending audio to telegram chat
        logger.info("[send] Sending file '{}' to chatId '{}'", response.getServiceId(), chatId);
        quickSender.chatAction(chatId, "upload_document");
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getSending());
        Message sendFileMessage;
        if (response.getExtension().equals("mp3") || response.getExtension().equals("m4a")) {
            sendFileMessage = requestService.sendRequest(getSendAudio(response));
            response.setTelegramFileId(sendFileMessage.getAudio().getFileId());
            if (response.getDuration() == 0)
                response.setDuration(sendFileMessage.getAudio().getDuration());
        } else if (response.getExtension().equals("mp4")) {
            sendFileMessage = requestService.sendRequest(getSendVideo(response));
            response.setTelegramFileId(sendFileMessage.getVideo().getFileId());
            if (response.getDuration() == 0)
                response.setDuration(sendFileMessage.getVideo().getDuration());
        } else {
            sendFileMessage = requestService.sendRequest(getSendDocument(response));
            response.setTelegramFileId(sendFileMessage.getDocument().getFileId());
        }
        quickSender.editMessageText(request.getEditMessageText(), getReceivedFileInfo(response));
        logger.info("[send] File sent successfully.");


        // saving file to repository
        if (request.getId3TagData() == null && !fileService.existsInRepo(response)) {
            fileService.saveInRepo(response);
        }

        // deleting downloaded files
        if (response.getDownloadedFile().getFile() != null)
            terminalExecutor.removeFile(response.getDownloadedFile().getFile());
        if (response.getThumbnail() != null)
            terminalExecutor.removeFile(response.getThumbnail().getFile());
    }


    public String getReceivedFileInfo(FileResponse response) {
        StringBuilder fileInfo = new StringBuilder();
        final String title = response.getTitle();
        final String duration = converter.secondsToMinutes(response.getDuration());
        final String size = converter.bytesToMB(response.getSize());
        final String extension = response.getExtension();
        final String quality = response.isPremium() ? "premium" : "standard";

        fileInfo.append(MarkdownV2.apply(info.getReceived()).toItalic().newlineAfter().get());
        if (response.getId3TagData() != null) {
            ID3TagData data = response.getId3TagData();
            if (data.getArtist() != null) {
                fileInfo.append(info.getArtist());
                fileInfo.append(MarkdownV2.apply(data.getArtist()).get());
            }
            if (data.getTitle() != null) {
                fileInfo.append(info.getTitle());
                fileInfo.append(MarkdownV2.apply(data.getTitle()).get());
            }
            if (data.getAlbum() != null) {
                fileInfo.append(info.getAlbum());
                fileInfo.append(MarkdownV2.apply(data.getAlbum()).get());
            }
            if (data.getReleaseYear() != null) {
                fileInfo.append(info.getReleaseYear());
                fileInfo.append(MarkdownV2.apply(data.getReleaseYear()).get());
            }
            if (data.getGenre() != null) {
                fileInfo.append(info.getGenre());
                fileInfo.append(MarkdownV2.apply(ID3v1Genres.GENRES[data.getGenre()]).get());
            }
        } else {
            fileInfo.append(info.getTitle());
            fileInfo.append(MarkdownV2.apply(title).get());
        }

        if (!duration.equals("0:00")) {
            fileInfo.append(info.getDuration());
            fileInfo.append(MarkdownV2.apply(duration).get());
        }

        fileInfo.append(info.getSize());
        fileInfo.append(MarkdownV2.apply(size).get());

        fileInfo.append(info.getExtension());
        fileInfo.append(MarkdownV2.apply(extension).get());

        fileInfo.append(info.getQuality());
        fileInfo.append(MarkdownV2.apply(quality).get());

        return fileInfo.toString();
    }

    public SendAudio getSendAudio(FileResponse response) {
        String title = null;
        String artist = null;
        if (response.getId3TagData() != null) {
            title = response.getId3TagData().getTitle();
            artist = response.getId3TagData().getArtist();
        }

        SendAudio sendAudio = SendAudio.builder()
                .chatId(response.getChatId())
                .audio(response.getDownloadedFile())
                .title(title)
                .performer(artist)
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

    public SendDocument getSendDocument(FileResponse response) {
        return SendDocument.builder()
                .chatId(response.getChatId())
                .document(response.getDownloadedFile())
                .build();
    }
}
