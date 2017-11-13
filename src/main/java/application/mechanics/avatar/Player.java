package application.mechanics.avatar;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import application.mechanics.game.GameObject;
import application.mechanics.game.GamePart;
import application.mechanics.game.Snap;
import application.mechanics.internal.MechanicsTimeService;

import java.util.HashMap;
import java.util.Map;

public class Player extends GameObject {
    private final @NotNull User userProfile;

    public Player(@NotNull User userProfile, @NotNull MechanicsTimeService timeService) {
        this.userProfile = userProfile;
        addPart(MousePart.class, new MousePart());
        addPart(MechanicPart.class, new MechanicPart(timeService));
    }

    public @NotNull User getUserProfile() {
        return userProfile;
    }

    public @NotNull Long getUserId() {
        return userProfile.getId();
    }

    @Override
    public @NotNull ServerPlayerSnap getSnap() {
        return ServerPlayerSnap.snapPlayer(this);
    }

    public static class ServerPlayerSnap implements Snap<Player> {
        private Long userId;

        Map<String, Snap<? extends GamePart>> gameParts;

        public Long getUserId() {
            return userId;
        }

        public Map<String, Snap<? extends GamePart>> getGameParts() {
            return gameParts;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public static @NotNull ServerPlayerSnap snapPlayer(@NotNull Player player) {
            final ServerPlayerSnap serverPlayerSnap = new ServerPlayerSnap();
            serverPlayerSnap.userId = player.getUserProfile().getId();
            serverPlayerSnap.gameParts = new HashMap<>();
            player.getPartSnaps().forEach(part -> serverPlayerSnap.gameParts.put(part.getClass().getSimpleName(), part));
            return serverPlayerSnap;
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "user=" + userProfile +
                '}';
    }
}
