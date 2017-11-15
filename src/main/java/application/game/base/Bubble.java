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

    @JsonProperty("bubbleRadius")
    private Float bubbleRadius;

    private static final AtomicLong ID_GENERATOR = new AtomicLong();


    public Bubble(Coords coords, Float growthRate, Float bubbleRadius) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.growthRate = growthRate;
        this.coords = coords;
        this.bubbleRadius = bubbleRadius;
    }


    public void grow(Long frameTime) {
        bubbleRadius += growthRate * frameTime / 20;
    }


    public Long getId() {
        return id;
    }
}
