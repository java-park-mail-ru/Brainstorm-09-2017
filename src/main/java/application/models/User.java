package application.models;

import application.views.ErrorResponse.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.regex.Pattern;


public class User {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("login")
    private String login;
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String  password;
    @JsonProperty("email")
    private String email;

    @JsonProperty("numberOfGames")
    private Long numberOfGames;
    @JsonProperty("record")
    private Long record;

    @JsonProperty("created")
    private Timestamp created;
    @JsonProperty("updated")
    private Timestamp updated;


    public User(@Nullable String login,
                @Nullable String password,
                @Nullable String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    @JsonCreator
    public User(@Nullable @JsonProperty("id") Long id,
                @Nullable @JsonProperty("login") String login,
                @Nullable @JsonProperty("password") String password,
                @Nullable @JsonProperty("email") String email,
                @Nullable @JsonProperty("numberOfGames") Long numberOfGames,
                @Nullable @JsonProperty("record") Long record,
                @Nullable @JsonProperty("created") Timestamp created,
                @Nullable @JsonProperty("updated") Timestamp updated) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.numberOfGames = numberOfGames;
        this.record = record;
        this.created = created;
        this.updated = updated;
    }


    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[.a-z0-9_-]+@[a-z0-9_.-]+\\.[a-z]{2,6}$");
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^[\\w\\d]{3,20}$");
    private static final Pattern PWD_PATTERN = Pattern.compile("^\\S{3,30}$");

    public @NotNull Optional<ErrorCode> emailValidator() {
        if(!EMAIL_PATTERN.matcher(email).matches()) {
            return Optional.of(ErrorCode.NOT_VALID_EMAIL);
        }
        return Optional.empty();
    }

    public @NotNull Optional<ErrorCode> loginValidator() {
        if(!LOGIN_PATTERN.matcher(login).matches()) {
            return Optional.of(ErrorCode.NOT_VALID_LOGIN);
        }
        return Optional.empty();
    }

    public @NotNull Optional<ErrorCode> passwordValidator() {
        if(!PWD_PATTERN.matcher(password).matches()) {
            return Optional.of(ErrorCode.NOT_VALID_PWD);
        }
        return Optional.empty();
    }


    public Long getId() { return id; }
    public String getLogin() { return login;  }
    @JsonIgnore
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Long getNumberOfGames() { return numberOfGames; }
    public Long getRecord() { return record; }
    @JsonIgnore
    public Timestamp getCreated() { return created; }
    @JsonIgnore
    public Timestamp getUpdated() { return updated; }

    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRecord(Long record) { this.record = record; }

    public void incNumberOfGames() { this.numberOfGames++; }

    @JsonProperty("created")
    public String getCreatedAsString() {
        return created.toString();
    }

    @JsonProperty("updated")
    public String getUpdatedAsString() {
        return updated.toString();
    }
}
