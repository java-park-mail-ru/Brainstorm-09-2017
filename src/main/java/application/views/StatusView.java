package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusView {
    @JsonProperty private final String status;

    @JsonCreator
    public StatusView(@JsonProperty("error") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
