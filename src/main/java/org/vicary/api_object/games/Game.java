package org.vicary.api_object.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.Animation;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.PhotoSize;
import org.vicary.api_object.message.MessageEntity;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Game implements ApiObject {
    /**
     * Represents a game.
     *
     * @param title        Title of the game.
     * @param description  Description of the game.
     * @param photo        Photo that will be displayed in the game message in chats.
     * @param text         Optional. Brief description of the game or high scores included in the game message.
     * @param textEntities Optional. Special entities that appear in text.
     * @param animation    Optional. Animation that will be displayed in the game message in chats.
     */
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("photo")
    private List<PhotoSize> photo;

    @JsonProperty("text")
    private String text;

    @JsonProperty("text_entities")
    private List<MessageEntity> textEntities;

    @JsonProperty("animation")
    private Animation animation;

    private Game() {}
}
