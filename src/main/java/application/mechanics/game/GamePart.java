package application.mechanics.game;

public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

    Snap<? extends GamePart> takeSnap();
}
