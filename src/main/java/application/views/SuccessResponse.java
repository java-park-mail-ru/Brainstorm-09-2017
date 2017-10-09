package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResponse {
    @JsonProperty("msg")
    private final String msg;

    @JsonCreator
    public SuccessResponse(@JsonProperty("msg") String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
