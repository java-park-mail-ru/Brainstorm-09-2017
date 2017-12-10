package application.models;

import application.views.ErrorResponse.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonProperty("localRecord")
    private Long localRecord;

    @JsonProperty("theme")
    private Integer theme;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX")
    private Timestamp created;
    @JsonProperty("updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX")
    private Timestamp updated;


    public User() {
    }

    public User(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public User(Long id, String login, String password, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

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
                @Nullable @JsonProperty("localRecord") Long localRecord,
                @Nullable @JsonProperty("theme") Integer theme,
                @Nullable @JsonProperty("created") Timestamp created,
                @Nullable @JsonProperty("updated") Timestamp updated) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.numberOfGames = numberOfGames;
        this.record = record;
        this.localRecord = localRecord;
        this.theme = theme;
        this.created = created;
        this.updated = updated;
    }


    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[.a-z0-9_-]+@[a-z0-9_.-]+\\.[a-z]{2,6}$");
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^[\\w\\d]{3,20}$");
    private static final Pattern PWD_PATTERN = Pattern.compile("^\\S{3,30}$");

    public @NotNull Optional<ErrorCode> emailValidator() {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return Optional.of(ErrorCode.NOT_VALID_EMAIL);
        }
        return Optional.empty();
    }

    public @NotNull Optional<ErrorCode> loginValidator() {
        if (!LOGIN_PATTERN.matcher(login).matches()) {
            return Optional.of(ErrorCode.NOT_VALID_LOGIN);
        }
        return Optional.empty();
    }

    public @NotNull Optional<ErrorCode> passwordValidator() {
        if (!PWD_PATTERN.matcher(password).matches()) {
            return Optional.of(ErrorCode.NOT_VALID_PWD);
        }
        return Optional.empty();
    }


    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Long getNumberOfGames() {
        return numberOfGames;
    }

    public Long getRecord() {
        return record;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public Long getLocalRecord() {
        return localRecord;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public void incNumberOfGames() {
        this.numberOfGames++;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public void setLocalRecord(Long localRecord) {
        this.localRecord = localRecord;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final User user = (User) obj;

        if (id != null ? !id.equals(user.id) : user.id != null) {
            return false;
        }
        return login != null ? login.equals(user.login) : user.login == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        final int hashConst = 31;
        result = hashConst * result + (login != null ? login.hashCode() : 0);
        return result;
    }
}
