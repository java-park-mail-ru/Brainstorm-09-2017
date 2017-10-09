package application.views;

import application.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.Objects;

public class RecordResponse {
    @JsonProperty("login")          private String  login;
    @JsonProperty("numberOfGames")  private Integer numberOfGames;
    @JsonProperty("record")         private Integer record;


    public RecordResponse(User user) {
        login = user.getLogin();
        numberOfGames = user.getNumberOfGames();
        record = user.getRecord();
    }


    public String getLogin() {
        return login;
    }

    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    public Integer getRecord() {
        return record;
    }


    public static class Compare implements Comparator<RecordResponse>
    {
        @Override
        public int compare(RecordResponse obj1, RecordResponse obj2)
        {
            if (!Objects.equals(obj2.record, obj1.record)) {
                return obj2.record - obj1.record;
            } else {
                return obj2.numberOfGames - obj1.numberOfGames;
            }
        }
    }
}
