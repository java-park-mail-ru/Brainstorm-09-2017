package application.views;

import application.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RecordResponse {
    @JsonProperty("topPosition") private Long  topPosition;
    @JsonProperty("login") private String  login;
    @JsonProperty("numberOfGames") private Long numberOfGames;
    @JsonProperty("record") private Long record;


    public RecordResponse(Long topPosition, User user) {
        this.topPosition = topPosition;
        this.login = user.getLogin();
        this.numberOfGames = user.getNumberOfGames();
        this.record = user.getRecord();
    }

    public RecordResponse(Long topPosition, String login, Long numberOfGames, Long record) {
        this.topPosition = topPosition;
        this.login = login;
        this.numberOfGames = numberOfGames;
        this.record = record;
    }

    public String getLogin() {
        return login;
    }

    public Long getNumberOfGames() {
        return numberOfGames;
    }

    public Long getRecord() {
        return record;
    }

    public Long getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(Long topPosition) {
        this.topPosition = topPosition;
    }
}
