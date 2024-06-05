package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import online.kingdomkeys.kingdomkeys.lib.SoAState;

/**
 * Event for when the choice in the Station of Awakening is finalised, after the stats have been applied, and when the choice is reset using the command
 */
public class ChoiceEvent extends Event {

    private final SoAState choice;
    private final SoAState sacrifice;
    private final Player player;

    public ChoiceEvent(Player player, SoAState choice, SoAState sacrifice) {
        this.choice = choice;
        this.sacrifice = sacrifice;
        this.player = player;
    }

    /**
     * @return The first choice, will either be WARRIOR, GUARDIAN, MYSTIC and NONE if the choice has been reset
     */
    public SoAState getChoice() {
        return choice;
    }

    /**
     * @return The second choice, will either be WARRIOR, GUARDIAN, MYSTIC and NONE if the choice has been reset
     */
    public SoAState getSacrifice() {
        return sacrifice;
    }

    public Player getPlayer() {
        return player;
    }
}
