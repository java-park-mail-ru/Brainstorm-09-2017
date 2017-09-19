package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    @JsonProperty("error")
    private final String error;

    @JsonCreator
    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
