package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MagicDamageSource extends EntityDamageSource {
    public MagicDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        return new TranslatableComponent("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), entity.getDisplayName().getString());
    }
}
