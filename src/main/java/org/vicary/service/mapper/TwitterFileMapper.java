package org.vicary.service.mapper;

import org.springframework.stereotype.Component;
import org.vicary.model.twitter.TwitterFileInfo;
import org.vicary.model.twitter.TwitterFileResponse;

@Component
public class TwitterFileMapper {
    public TwitterFileResponse map(TwitterFileInfo fileInfo) {
        return TwitterFileResponse.builder()
                .url(fileInfo.getURL())
                .twitterId(fileInfo.getId())
                .title(fileInfo.getTitle())
                .duration(fileInfo.getDuration())
                .extension(fileInfo.getExtension())
                .build();
    }
}
