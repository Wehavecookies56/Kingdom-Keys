package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class GuardEvent extends Event {
    private final Player player;

    private GuardEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Posted when the keyblade goes up. Server side only
     * Cancelling leaves the guard down: no window opens, no cooldown is spent, and the player is free to try
     * again on the next click.
     */
    public static class Start extends GuardEvent implements ICancellableEvent {
        public Start(Player player) {
            super(player);
        }
    }

    /**
     * Posted when the guard actually stops a hit. Server side only.
     * Cancelling lets the hit through as though the guard had not been there. The window is left as it was, so
     * a later blow in the same combo can still be blocked.
     */
    public static class Blocked extends GuardEvent implements ICancellableEvent {
        private final DamageSource source;
        private final float amount;

        public Blocked(Player player, DamageSource source, float amount) {
            super(player);
            this.source = source;
            this.amount = amount;
        }

        /** What was about to hit the player */
        public DamageSource getSource() {
            return source;
        }

        /** How much the hit would have done had it landed */
        public float getAmount() {
            return amount;
        }
    }
}
