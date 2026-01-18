package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ReactionCommandCastEvent extends Event implements ICancellableEvent {
    private final LivingEntity caster;
    private final ResourceLocation rcID;

    public ReactionCommandCastEvent(LivingEntity caster, ResourceLocation rcID) {
        this.caster = caster;
        this.rcID = rcID;
    }

    public LivingEntity getCaster() { return caster; }
    public ResourceLocation getRCID() { return rcID; }
}