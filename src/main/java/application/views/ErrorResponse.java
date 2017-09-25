package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    @JsonProperty("code") private final Integer code;
    @JsonProperty("msg")  private final String  msg;

    public enum ErrorCode {
        UNKNOWN_ERROR,
        NOT_VALID_EMAIL,
        NOT_VALID_LOGIN,
        NOT_VALID_PWD,
        USER_DUPLICATE,
        USER_NOT_FOUND,
        AUTHORISATION_FAILED,
        UNAUTHORIZED
    }

    @JsonCreator
    public ErrorResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public  ErrorResponse(ErrorCode code) {
        this.code = code.ordinal();
        switch (code) {
            case UNKNOWN_ERROR: this.msg = "Unknown error"; break;
            case NOT_VALID_EMAIL: this.msg = "Not valid email"; break;
            case NOT_VALID_LOGIN: this.msg = "Not valid login"; break;
            case NOT_VALID_PWD: this.msg = "Not valid password"; break;
            case USER_DUPLICATE: this.msg = "There is a user with the same login"; break;
            case USER_NOT_FOUND: this.msg = "User not found"; break;
            case AUTHORISATION_FAILED: this.msg = "Authorization failed"; break;
            case UNAUTHORIZED: this.msg = "User not authorized"; break;
            default: this.msg = "Unknown error"; break;
        }
    }

    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}

