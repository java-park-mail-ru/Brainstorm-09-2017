package application.models;

import application.services.UserService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class User {
    private Long id;
    private String login;
    private String password;
    private String email;

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @JsonCreator
    public User(@JsonProperty("id") Integer id,
                @JsonProperty("login") String login,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email) {
        this.id = id != null ? id : ID_GENERATOR.getAndIncrement();
        this.login = login;
        this.password = password;
        this.email = email;
    }


    public Long getId()         { return id; }
    public String getLogin()    { return login;  }
    @JsonIgnore
    public String getPassword() { return password; }
    public String getEmail()    { return email; }

    public void setId(Long id) { this.id = id; }
    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }


    public String emailValidator() {
        final String ePattern = "^[.a-z0-9_-]+@[a-z0-9_-]+\\.[a-z]{2,6}$";
        return !Pattern.compile(ePattern).matcher(email).matches() ? "Not valid email. " : "";
    }

    public String loginValidator() {
        return !Pattern.compile("^[\\w\\d]{3,10}$").matcher(login).matches() ?  "Not valid login. " : "";
    }

    public String passwordValidator() {
        return !Pattern.compile("^\\S{3,16}$").matcher(password).matches() ? "Not valid password. " : "";
    }
}
