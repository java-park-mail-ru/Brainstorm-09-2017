package application.game;

import application.game.base.Bubble;
import application.game.base.Coords;
import application.game.resources.BubbleResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.Date;
import java.util.Random;


public class BubbleFactory {
    private Long blisteringPeriod;
    private Date lastProduceTime;

    private static final Float CUBE_SIZE = 9f;
    private static BubbleResource resource;


    public BubbleFactory(Long blisteringPeriod) throws IOException {
        this.blisteringPeriod = blisteringPeriod;
        lastProduceTime = new Date((new Date().getTime()) - blisteringPeriod);

        final ObjectMapper objectMapper = new ObjectMapper();
        resource = objectMapper.readValue(Resources.getResource("BubbleResource.json"), BubbleResource.class);
    }


    public @Nullable Bubble produce() {
        if (!canProduce()) {
            return null;
        }
        lastProduceTime = new Date();
        return createBubble();
    }


    protected Boolean canProduce() {
        final Date now = new Date();
        return now.getTime() - lastProduceTime.getTime() >= blisteringPeriod;
    }


    protected Bubble createBubble() {
        final Random rand = new Random();
        final Float halfSize = CUBE_SIZE / 2;
        final Coords coords;
        // TODO: 4 грани, потому что на фронте еще не реализовано вращение по вертикали.
        final Integer faceCount = 4;
        switch (rand.nextInt(faceCount)) {
            case 0:
                coords = new Coords(
                        rand.nextFloat() * CUBE_SIZE - halfSize,
                        rand.nextFloat() * CUBE_SIZE - halfSize,
                        -halfSize
                );
                break;
            case 1:
                coords = new Coords(
                        -halfSize,
                        rand.nextFloat() * CUBE_SIZE - halfSize,
                        rand.nextFloat() * CUBE_SIZE - halfSize
                );
                break;
            case 2:
                coords = new Coords(
                        rand.nextFloat() * CUBE_SIZE - halfSize,
                        rand.nextFloat() * CUBE_SIZE - halfSize,
                        halfSize
                );
                break;
            default:
                coords = new Coords(
                        halfSize,
                        rand.nextFloat() * CUBE_SIZE - halfSize,
                        rand.nextFloat() * CUBE_SIZE - halfSize
                );
                break;
        }

        return new Bubble(coords, resource.getGrowthRate(), resource.getStartRadius(), resource.getMaxRadius());
    }


    public void setBlisteringPeriod(Long blisteringPeriod) {
        this.blisteringPeriod = blisteringPeriod;
    }
}
