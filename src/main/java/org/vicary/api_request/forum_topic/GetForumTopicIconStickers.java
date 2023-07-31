package org.vicary.api_request.forum_topic;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.stickers.Sticker;
import org.vicary.api_request.ApiRequestList;
import org.vicary.end_point.EndPoint;

@Data
@NoArgsConstructor
@Builder
public class GetForumTopicIconStickers implements ApiRequestList<Sticker> {
    /**
     * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user.
     * Requires no parameters.
     */

    @Override
    public Sticker getReturnObject() {
        return new Sticker();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_FORUM_TOPIC_ICON_STICKERS.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
