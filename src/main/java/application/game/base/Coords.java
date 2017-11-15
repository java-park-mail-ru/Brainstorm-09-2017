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
    public Float getCoordX() {
        return coordX;
    }

    public void setCoordX(Float coordX) {
        this.coordX = coordX;
    }

    @JsonProperty("y")
    public Float getCoordY() {
        return coordY;
    }

    public void setCoordY(Float coordY) {
        this.coordY = coordY;
    }

    @JsonProperty("z")
    public Float getCoordZ() {
        return coordZ;
    }

    public void setCoordZ(Float coordZ) {
        this.coordZ = coordZ;
    }
}
