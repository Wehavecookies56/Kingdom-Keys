package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class StopDamageSource extends EntityDamageSource {
	public StopDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}

	public static DamageSource getStopDamage(PlayerEntity player) {
		return new StopDamageSource("stop", player);
	}

	@Override
	public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
		return new TranslationTextComponent("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), damageSourceEntity.getDisplayName().getString());
	}

}
