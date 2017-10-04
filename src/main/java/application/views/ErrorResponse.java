package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    @JsonProperty("code") private final Integer code;
    @JsonProperty("msg") private final String msg;

    public enum ErrorCode {
        UNKNOWN_ERROR("Unknown error"),
        NOT_VALID_EMAIL("Not valid email"),
        NOT_VALID_LOGIN("Not valid login"),
        NOT_VALID_PWD("Not valid password"),
        USER_DUPLICATE("There is a user with the same login"),
        USER_NOT_FOUND("User not found"),
        AUTHORISATION_FAILED("Authorization failed"),
        UNAUTHORIZED("User not authorized");

        private String msg;

        ErrorCode(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }


    @JsonCreator
    public ErrorResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public  ErrorResponse(ErrorCode code) {
        this.code = code.ordinal();
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

