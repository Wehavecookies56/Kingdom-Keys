package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class LimitCastEvent extends Event implements ICancellableEvent {
    private final LivingEntity caster;
    private final ResourceLocation limitID;

    public LimitCastEvent(LivingEntity caster, ResourceLocation limitID) {
        this.caster = caster;
        this.limitID = limitID;
    }

    public LivingEntity getCaster() { return caster; }
    public ResourceLocation getLimitID() { return limitID; }
}