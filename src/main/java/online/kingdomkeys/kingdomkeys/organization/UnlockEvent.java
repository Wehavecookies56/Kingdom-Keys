package online.kingdomkeys.kingdomkeys.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class UnlockEvent extends Event {

    private final OrganizationUnlock unlock;
    private final PlayerEntity player;

    public UnlockEvent(PlayerEntity player, OrganizationUnlock unlock) {
        this.player = player;
        this.unlock = unlock;
    }

    public OrganizationUnlock getUnlock() {
        return unlock;
    }

    public PlayerEntity getPlayer() {
        return player;
    }
}
