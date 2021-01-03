package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class KeybladeDamageSource extends EntityDamageSource {
	public KeybladeDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}

	public static DamageSource causeKeybladeDamage(Hand hand, PlayerEntity player) {
		if (hand == Hand.MAIN_HAND) {
			return new KeybladeDamageSource("keybladeMainhand", player);
		} else {
			return new KeybladeDamageSource("keybladeOffhand", player);
		}
	}

	@Override
	public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
		return new TranslationTextComponent("keybladedamage.death", entityLivingBaseIn.getDisplayName().getFormattedText(), damageSourceEntity.getDisplayName().getFormattedText());
	}

}
