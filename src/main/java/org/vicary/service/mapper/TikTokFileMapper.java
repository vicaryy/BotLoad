package org.vicary.service.mapper;

import org.springframework.stereotype.Component;
import org.vicary.model.tiktok.TikTokFileInfo;
import org.vicary.model.tiktok.TikTokFileResponse;

@Component
public class TikTokFileMapper {
    public TikTokFileResponse map(TikTokFileInfo fileInfo) {
        return TikTokFileResponse.builder()
                .URL(fileInfo.getURL())
                .tiktokId(fileInfo.getId())
                .title(fileInfo.getTitle())
                .duration(fileInfo.getDuration())
                .extension(fileInfo.getExtension())
                .build();
    }
}
