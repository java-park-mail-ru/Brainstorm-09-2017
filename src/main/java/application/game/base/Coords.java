package application.game.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coords {
    @JsonProperty("x")
    private Float xCoord;
    @JsonProperty("y")
    private Float yCoord;
    @JsonProperty("z")
    private Float zCoord;


    @JsonCreator
    public Coords(@JsonProperty("x") Float xCoord,
                  @JsonProperty("y") Float yCoord,
                  @JsonProperty("z") Float zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    @JsonProperty("x")
    public Float getX() {
        return xCoord;
    }

    public void setX(Float xCoord) {
        this.xCoord = xCoord;
    }

    @JsonProperty("y")
    public Float getY() {
        return yCoord;
    }

    public void setY(Float yCoord) {
        this.yCoord = yCoord;
    }

    @JsonProperty("z")
    public Float getZ() {
        return zCoord;
    }

    public void setZ(Float zCoord) {
        this.zCoord = zCoord;
    }
}
