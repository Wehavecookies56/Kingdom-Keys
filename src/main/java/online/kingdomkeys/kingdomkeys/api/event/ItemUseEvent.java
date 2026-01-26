package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;

public class ItemUseEvent extends Event implements ICancellableEvent {
    private final LivingEntity caster;
    private final KKPotionItem potion;

    public ItemUseEvent(LivingEntity caster, KKPotionItem potion) {
        this.caster = caster;
        this.potion = potion;
    }

    public LivingEntity getCaster() { return caster; }
    public KKPotionItem getPotion() { return potion; }
}