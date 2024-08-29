package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StopDamageSource extends DamageSource {
	public StopDamageSource(Holder<DamageType> damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}

	public static DamageSource getStopDamage(Entity directEntity) {
		return directEntity.damageSources().source(KKDamageTypes.STOP,directEntity,directEntity);
    }
	
	public static DamageSource getStopDamage(Entity directEntity, Entity indirectEntity) {
		return directEntity.damageSources().source(KKDamageTypes.STOP,directEntity,indirectEntity);
    }

	@Override
	public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
		return Component.translatable("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), entityLivingBaseIn.getDisplayName().getString());
	}

}
