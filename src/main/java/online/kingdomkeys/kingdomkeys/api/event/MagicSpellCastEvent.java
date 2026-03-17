package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;


public class MagicSpellCastEvent extends Event implements ICancellableEvent {
    private final LivingEntity caster;
    private final ResourceLocation spellId;
    private final int level;

    public MagicSpellCastEvent(LivingEntity caster, ResourceLocation spellId, int level) {
        this.caster = caster;
        this.spellId = spellId;
        this.level = level;
    }

    public LivingEntity getCaster() { return caster; }
    public ResourceLocation getSpellID() { return spellId; }
    public int getLevel() { return level; }
}