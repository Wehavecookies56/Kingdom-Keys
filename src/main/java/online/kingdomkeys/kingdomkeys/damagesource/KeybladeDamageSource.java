package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KeybladeDamageSource extends DamageSource {
	public KeybladeDamageSource(Holder<DamageType> damageTypeIn, Entity damageSourceEntityIn) {
		super(damageTypeIn, damageSourceEntityIn);
	}
	
	public static DamageSource causeOffhandKeybladeDamage(Player player) {
		if(player.getOffhandItem() != null && player.getOffhandItem().getItem() instanceof KeybladeItem) {
			return player.damageSources().source(KKDamageTypes.OFFHAND, player, player);
		} else
			return player.damageSources().playerAttack(player);
	}

	/* public static DamageSource getFireDamage(Entity directEntity, Entity indirectEntity) {
			return directEntity.damageSources().source(KKDamageTypes.FIRE,directEntity,indirectEntity);        
	    }*/
	@Override
	public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
		return Component.translatable("keybladedamage.death", entityLivingBaseIn.getDisplayName().getString(), entityLivingBaseIn.getDisplayName().getString());
	}

}
