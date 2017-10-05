package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    @JsonProperty("code") private final Integer code;
    @JsonProperty("msg") private final String msg;

    public enum ErrorCode {
        UNKNOWN_ERROR(0, "Unknown error"),
        NOT_VALID_EMAIL(1, "Not valid email"),
        NOT_VALID_LOGIN(2, "Not valid login"),
        NOT_VALID_PWD(3, "Not valid password"),
        USER_DUPLICATE(4, "There is a user with the same login"),
        USER_NOT_FOUND(5, "User not found"),
        AUTHORISATION_FAILED(6, "Authorization failed"),
        UNAUTHORIZED(7, "User not authorized");

        private Integer code;
        private String msg;

        ErrorCode(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public Integer getCode() { return code; }
    }


    @JsonCreator
    public ErrorResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public  ErrorResponse(ErrorCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }


    // Возвращает список с первым элементом this
    public ErrorResponseList toList() {
        final ErrorResponseList errors = new ErrorResponseList();
        return errors.add(this);
    }


    @Override
    public String toString() {
        return "ERROR №" + code + ": " + msg + ". ";
    }

    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}

