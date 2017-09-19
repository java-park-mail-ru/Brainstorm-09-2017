package application.views;

import application.models.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {
    private Integer id;
    private String login;
    private String email;

    @JsonCreator
    public UserData(@JsonProperty("id") Integer id, @JsonProperty("login") String login,
                    @JsonProperty("email") String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public UserData(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
