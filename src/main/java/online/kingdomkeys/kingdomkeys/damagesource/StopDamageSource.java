package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class StopDamageSource extends EntityDamageSource {
	public StopDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}

	public static DamageSource getStopDamage(Player player) {
		return new StopDamageSource("stop", player);
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
		return new TranslatableComponent("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), entity.getDisplayName().getString());
	}

}
