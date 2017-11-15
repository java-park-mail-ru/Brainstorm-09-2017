package application.game.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coords {
    @JsonProperty("x")
    private Float x;
    @JsonProperty("y")
    private Float y;
    @JsonProperty("z")
    private Float z;


    @JsonCreator
    public Coords(@JsonProperty("x") Float x,
                  @JsonProperty("y") Float y,
                  @JsonProperty("z") Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }
}
