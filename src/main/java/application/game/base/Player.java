package application.game.base;

import application.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;


public class Player {
    @JsonProperty("userProfile")
    private @NotNull User userProfile;

    @JsonProperty("score")
    private @NotNull Long score;

    public Player(@NotNull User userProfile) {
        this.userProfile = userProfile;
        score = 0L;
    }

    public @NotNull User getUserProfile() {
        return userProfile;
    }

    public @NotNull Long getUserId() {
        return userProfile.getId();
    }

    public @NotNull Long getScore() {
        return score;
    }

    public void addPoints(Long points) {
        score += points;
    }
}
