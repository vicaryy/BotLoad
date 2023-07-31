package org.vicary.api_object.inline_query.inline_query_result;

import org.vicary.api_object.ApiObject;

public interface InlineQueryResult extends ApiObject {
    String getType();
    String getId();
}
