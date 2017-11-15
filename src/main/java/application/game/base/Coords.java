package application.game.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coords {
    @JsonProperty("x")
    private Float coordX;
    @JsonProperty("y")
    private Float coordY;
    @JsonProperty("z")
    private Float coordZ;


    @JsonCreator
    public Coords(@JsonProperty("x") Float coordX,
                  @JsonProperty("y") Float coordY,
                  @JsonProperty("z") Float coordZ) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.coordZ = coordZ;
    }

    @JsonProperty("x")
    public Float getX() {
        return coordX;
    }

    public void setX(Float coordX) {
        this.coordX = coordX;
    }

    @JsonProperty("y")
    public Float getY() {
        return coordY;
    }

    public void setY(Float coordY) {
        this.coordY = coordY;
    }

    @JsonProperty("z")
    public Float getZ() {
        return coordZ;
    }

    public void setZ(Float coordZ) {
        this.coordZ = coordZ;
    }
}
