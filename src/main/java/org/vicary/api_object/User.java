package org.vicary.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class User implements ApiObject {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("is_bot")
    private Boolean isBot;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("language_code")
    private String languageCode;

    @JsonProperty("is_premium")
    private boolean isPremium;

    @JsonProperty("added_to_attachment_menu")
    private boolean addedToAttachmentMenu;

    @JsonProperty("can_join_groups")
    private boolean canJoinGroups;

    @JsonProperty("can_read_all_group_messages")
    private boolean canReadAllGroupMessages;

    @JsonProperty("supports_inline_queries")
    private boolean supportsInlineQueries;

    public User() {
    }
}
