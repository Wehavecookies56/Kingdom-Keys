package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;


public class DriveFormCastEvent extends Event implements ICancellableEvent {
    private final LivingEntity caster;
    private final ResourceLocation spellId;

    public DriveFormCastEvent(LivingEntity caster, ResourceLocation spellId) {
        this.caster = caster;
        this.spellId = spellId;
    }

    public LivingEntity getCaster() { return caster; }
    public ResourceLocation getSpellID() { return spellId; }
}