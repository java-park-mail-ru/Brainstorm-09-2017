package application.game.resources;

import com.fasterxml.jackson.annotation.JsonProperty;


public class BubbleResource {
    @JsonProperty("growthRate")
    private Float growthRate;

    @JsonProperty("startRadius")
    private Float startRadius;

    @JsonProperty("maxRadius")
    private Float maxRadius;


    public BubbleResource(@JsonProperty("growthRate") Float growthRate,
                          @JsonProperty("startRadius") Float startRadius,
                          @JsonProperty("maxRadius") Float maxRadius) {
        this.growthRate = growthRate;
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
    }


    public Float getStartRadius() {
        return startRadius;
    }

    public Float getMaxRadius() {
        return maxRadius;
    }

    public Float getGrowthRate() {
        return growthRate;
    }
}
