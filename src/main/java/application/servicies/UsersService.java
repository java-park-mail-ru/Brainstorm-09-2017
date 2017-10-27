package application.servicies;

import application.models.User;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import application.views.RecordResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@PropertySource("classpath:game.properties")
public class UsersService {
    private final NamedParameterJdbcTemplate template;

    @Value("${game.topRecordsCount}")
    private Integer topRecordsCount;


    @Autowired
    public UsersService(NamedParameterJdbcTemplate template) {
        this.template = template;
    }


    private static final RowMapper<User> USER_MAPPER = (res, num) -> new User(
            res.getLong("id"),
            res.getString("login"),
            res.getString("password"),
            res.getString("email"),
            res.getLong("number_of_games"),
            res.getLong("record"),
            res.getInt("theme"),
            res.getTimestamp("created"),
            res.getTimestamp("updated")
    );


    private static final RowMapper<RecordResponse> RECORD_MAPPER = (res, num) -> new RecordResponse(
            (long) num,
            res.getString("login"),
            res.getLong("number_of_games"),
            res.getLong("record")
    );


    public List create(User credentials) {
        final List<ErrorCode> errors = new ArrayList<>();
        credentials.emailValidator().ifPresent(errors::add);
        credentials.loginValidator().ifPresent(errors::add);
        credentials.passwordValidator().ifPresent(errors::add);

        if (!errors.isEmpty()) {
            return errors.stream().map(ErrorResponse::new).collect(Collectors.toList());
        }

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", credentials.getLogin());
        params.addValue("password", hashpw(credentials.getPassword()));
        params.addValue("email", credentials.getEmail());
        try {
            template.update("INSERT INTO person(login, password, email)"
                    + " VALUES (:login,:password,:email) RETURNING id", params, keyHolder);
            return Collections.EMPTY_LIST;          // Возвращаяю пустой список ошибок
        } catch (DuplicateKeyException e) {
            return new ErrorResponse(ErrorCode.USER_DUPLICATE).toList();
        }
    }


    public List update(Long id, User credentials) {
        // Проверяю, что есть что-то на изменение
        if (credentials.getEmail() == null && credentials.getPassword() == null) {
            return new ErrorResponse(ErrorCode.NOTHING_TO_CHANGE).toList();
        }
        final List<ErrorCode> errors = new ArrayList<>();
        if (credentials.getEmail() != null) {
            credentials.emailValidator().ifPresent(errors::add);
        }
        if (credentials.getPassword() != null) {
            credentials.passwordValidator().ifPresent(errors::add);
        }
        if (!errors.isEmpty()) {
            return errors.stream().map(ErrorResponse::new).collect(Collectors.toList());
        }

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        final StringBuilder values = new StringBuilder();
        if (credentials.getEmail() != null) {
            params.addValue("email", credentials.getEmail());
            values.append("email = :email, ");
        }
        if (credentials.getPassword() != null) {
            params.addValue("password", hashpw(credentials.getPassword()));
            values.append("password = :password, ");
        }
        final Integer count = template.update("UPDATE person SET " + values + "updated = now() WHERE id=:id", params);

        if (count == 0) {
            return new ErrorResponse(ErrorCode.USER_NOT_FOUND).toList();
        }
        return Collections.EMPTY_LIST;          // Возвращаяю пустой список ошибок
    }


    public @Nullable User findUserById(Long id) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            return template.queryForObject("SELECT * FROM person WHERE id = :id", params, USER_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public @Nullable User findUserByLogin(String login) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", login);
        try {
            return template.queryForObject("SELECT * FROM person WHERE login = :login", params, USER_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public @Nullable User login(User credentials) {
        final User user = findUserByLogin(credentials.getLogin());
        if (user == null || !checkpw(credentials.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }


    public List<RecordResponse> getRecords() {
        return template.query("SELECT login, number_of_games, record FROM person WHERE record > 0 "
                + "ORDER BY record DESC, number_of_games LIMIT " + topRecordsCount, RECORD_MAPPER
        );
    }


    public List setTheme(Long id, User body) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("theme", body.getTheme());
        final Integer count = template.update("UPDATE person SET theme = :theme WHERE id = :id", params);

        if (count == 0) {
            return new ErrorResponse(ErrorCode.USER_NOT_FOUND).toList();
        }
        return Collections.EMPTY_LIST;          // Возвращаяю пустой список ошибок
    }



    private String hashpw(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt());
    }


    public static Boolean checkpw(String pwd, String storedHash) {
        return BCrypt.checkpw(pwd, storedHash);
    }


    void clearDB() {
        template.update("TRUNCATE TABLE person CASCADE", new MapSqlParameterSource());
    }


    public @Nullable User auth(HttpSession httpSession) {
        final Long userId = (Long) httpSession.getAttribute("userId");
        return findUserById(userId);
    }
}
