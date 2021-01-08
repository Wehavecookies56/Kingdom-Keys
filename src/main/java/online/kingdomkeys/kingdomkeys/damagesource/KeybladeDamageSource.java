package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KeybladeDamageSource extends EntityDamageSource {
	public KeybladeDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}

	public static DamageSource causeOffhandKeybladeDamage(PlayerEntity player) {
		if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof KeybladeItem)
			return new KeybladeDamageSource("keybladeOffhand", player);
		else
			return DamageSource.causePlayerDamage(player);
	}

	@Override
	public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
		return new TranslationTextComponent("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), damageSourceEntity.getDisplayName().getString());
	}

}
