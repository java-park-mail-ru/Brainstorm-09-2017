package application.game.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicLong;


public class Bubble {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("growthRate")
    private Float growthRate;

    @JsonProperty("coords")
    private Coords coords;

    @JsonProperty("radius")
    private Float radius;

    @JsonProperty("maxRadius")
    private Float maxRadius;

    private static final AtomicLong ID_GENERATOR = new AtomicLong();


    public Bubble(Coords coords, Float growthRate, Float radius, Float maxRadius) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.growthRate = growthRate;
        this.coords = coords;
        this.radius = radius;
        this.maxRadius = maxRadius;
    }


    public void grow(Long frameTime) {
        radius += growthRate * frameTime / 20;
    }


    public Boolean isBurst() {
        return radius > maxRadius;
    }


    public Long getId() {
        return id;
    }

    public Float getRadius() {
        return radius;
    }

    public Coords getCoords() {
        return coords;
    }

    public Float getMaxRadius() {
        return maxRadius;
    }
}
