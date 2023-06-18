package org.example.api_object.video;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class VideoChatStarted implements ApiObject {

    private VideoChatStarted() {
    }
}
