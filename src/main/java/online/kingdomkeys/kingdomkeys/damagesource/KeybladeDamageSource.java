package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KeybladeDamageSource extends EntityDamageSource {
	public KeybladeDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}

	public static DamageSource causeOffhandKeybladeDamage(Player player) {
		if(player.getOffhandItem() != null && player.getOffhandItem().getItem() instanceof KeybladeItem)
			return new KeybladeDamageSource("keybladeOffhand", player);
		else
			return DamageSource.playerAttack(player);
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
		return new TranslatableComponent("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), entity.getDisplayName().getString());
	}

}
