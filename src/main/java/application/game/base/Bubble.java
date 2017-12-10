package application.game.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicLong;

@JsonIgnoreProperties(ignoreUnknown = true)
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


    public Bubble(Coords coords,
                  Float growthRate,
                  Float radius,
                  Float maxRadius) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.growthRate = growthRate;
        this.coords = coords;
        this.radius = radius;
        this.maxRadius = maxRadius;
    }

    @JsonCreator
    public Bubble(@JsonProperty("id") Long id,
                  @JsonProperty("coords") Coords coords,
                  @JsonProperty("growthRate") Float growthRate,
                  @JsonProperty("radius") Float radius,
                  @JsonProperty("maxRadius") Float maxRadius) {
        this.id = id == null ? ID_GENERATOR.getAndIncrement() : id;
        this.growthRate = growthRate;
        this.coords = coords;
        this.radius = radius;
        this.maxRadius = maxRadius;
    }


    public void grow(Long frameTime) {
        final Long millsInSec = 1000L;
        radius += growthRate * frameTime / millsInSec;
    }


    @JsonProperty("isBurst")
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

    public Float getGrowthRate() {
        return growthRate;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Bubble bubble = (Bubble) obj;

        return id.equals(bubble.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
