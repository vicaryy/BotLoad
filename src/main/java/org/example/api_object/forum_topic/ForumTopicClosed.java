package org.example.api_object.forum_topic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class ForumTopicClosed implements ApiObject {

    private ForumTopicClosed() {
    }
}
