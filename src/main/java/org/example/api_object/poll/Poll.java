package org.example.api_object.poll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class Poll implements ApiObject {
    @JsonProperty("id")
    private String id;

    @JsonProperty("question")
    private String question;

    @JsonProperty("options")
    private List<PollOption> options;

    @JsonProperty("total_voter_count")
    private Integer totalVoterCount;

    @JsonProperty("is_closed")
    private Boolean isClosed;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("type")
    private String type;

    @JsonProperty("allows_multiple_answers")
    private Boolean allowsMultipleAnswers;

    @JsonProperty("correct_option_id")
    private Integer correctOptionId;

    @JsonProperty("explanation")
    private String explanation;

    @JsonProperty("explanation_entities")
    private List<MessageEntity> explanationEntities;

    @JsonProperty("open_period")
    private Integer openPeriod;

    @JsonProperty("close_date")
    private Integer closeDate;

    private Poll() {
    }
}
