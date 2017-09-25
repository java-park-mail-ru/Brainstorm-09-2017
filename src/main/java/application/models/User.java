package application.models;

import application.services.UserService;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

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


    public @Nullable ErrorResponse emailValidator() {
        final String ePattern = "^[.a-z0-9_-]+@[a-z0-9_.-]+\\.[a-z]{2,6}$";
        return !Pattern.compile(ePattern).matcher(email).matches() ? new ErrorResponse(ErrorCode.NOT_VALID_EMAIL) : null;
    }

    public @Nullable ErrorResponse loginValidator() {
        return !Pattern.compile("^[\\w\\d]{3,10}$").matcher(login).matches() ?  new ErrorResponse(ErrorCode.NOT_VALID_LOGIN) : null;
    }

    public @Nullable ErrorResponse passwordValidator() {
        return !Pattern.compile("^\\S{3,16}$").matcher(password).matches() ? new ErrorResponse(ErrorCode.NOT_VALID_PWD) : null;
    }
}
