package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MagicDamageSource extends DamageSource {
    public MagicDamageSource(Holder<DamageType> damageTypeIn, Entity damageSourceEntityIn, Entity indirectEntity) {
        super(damageTypeIn, damageSourceEntityIn, indirectEntity);
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        return Component.translatable("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), entityLivingBaseIn.getDisplayName().getString());
    }
}
