package application.models;

import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
    private Integer numberOfGames;
    @JsonProperty("record")
    private Integer record;

    @JsonProperty("createdDate")
    private Date createdDate;
    @JsonProperty("updatedDate")
    private Date updatedDate;

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @JsonCreator
    public User(@Nullable @JsonProperty("id") Integer id,
                @Nullable @JsonProperty("login") String login,
                @Nullable @JsonProperty("password") String password,
                @Nullable @JsonProperty("email") String email) {
        this.id = id != null ? id : ID_GENERATOR.getAndIncrement();
        this.login = login;
        this.password = password;
        this.email = email;
        // FIXME: Временно
        final Random random = new Random();
        this.numberOfGames = random.nextInt(100);
        this.record = random.nextInt(100000);
        //this.numberOfGames = 0;
        //this.record = 0;
        this.createdDate = new Date();
        this.updatedDate = new Date();
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
    public Integer getNumberOfGames() { return numberOfGames; }
    public Integer getRecord() { return record; }
    public Date getCreatedDate() { return createdDate; }
    public Date getUpdatedDate() { return updatedDate; }

    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRecord(Integer record) { this.record = record; }
    public void setUpdatedDate() { this.updatedDate = new Date(); }

    public void incNumberOfGames() { this.numberOfGames++; }
}
