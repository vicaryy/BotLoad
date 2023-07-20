package org.example.api_object.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class GameHighScore {
    /**
     * Represents one row of the high scores table for a game.
     *
     * @param position The position in the high score table for the game.
     * @param user     The user.
     * @param score    The score.
     */
    @JsonProperty("position")
    private Integer position;

    @JsonProperty("user")
    private User user;

    @JsonProperty("score")
    private Integer score;
}
