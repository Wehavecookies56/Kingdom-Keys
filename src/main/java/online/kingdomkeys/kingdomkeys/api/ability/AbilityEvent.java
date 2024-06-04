package online.kingdomkeys.kingdomkeys.api.ability;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import online.kingdomkeys.kingdomkeys.ability.Ability;

public abstract class AbilityEvent extends Event {

    private final Ability ability;
    private final Player player;
    private final int index;
    private final boolean client;

    private AbilityEvent(Ability ability, int index, Player player, boolean client) {
        this.ability = ability;
        this.player = player;
        this.client = client;
        this.index = index;
    }

    public Ability getAbility() {
        return ability;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isClient() {
        return client;
    }

    /**
     * @return Index for duplicate abilities, for growth abilities this will be the level
     */
    public int getIndex() {
        return index;
    }

    /**
     * Event is posted when an ability is about to be equipped on both the clientside and the serverside
     * Cancelling this event will stop the ability from being equipped, cancelling it clientside will stop the packet from being sent and thus stops it serverside
     */
    @Cancelable
    public static class Equip extends AbilityEvent {
        public Equip(Ability ability, int index, Player player, boolean client) {
            super(ability, index, player, client);
        }
    }

    /**
     * Event is posted when an ability is about to be unequipped on both the clientside and the serverside
     * Cancelling this event will stop the ability from being unequipped, cancelling it clientside will stop the packet from being sent and thus stops it serverside
     */
    @Cancelable
    public static class Unequip extends AbilityEvent {
        public Unequip(Ability ability, int index, Player player, boolean client) {
            super(ability, index, player, client);
        }
    }
}
