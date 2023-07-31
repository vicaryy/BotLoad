package org.vicary.api_object.input_media;

import org.vicary.api_object.ApiObject;

public interface InputMedia extends ApiObject {
    String getType();

    String getMedia();
}
