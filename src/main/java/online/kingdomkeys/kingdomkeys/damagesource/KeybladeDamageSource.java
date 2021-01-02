package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KeybladeDamageSource {

	public static DamageSource causeKeybladeDamage(Hand hand, PlayerEntity player){
		if(hand == Hand.MAIN_HAND) {
			return new EntityDamageSource("keybladeMainhand", player);
		} else {
			return new EntityDamageSource("keybladeOffhand", player);
		}
	}
	
	public static ItemStack getKeybladeDamageStack(DamageSource damageSource, PlayerEntity player) {
		switch(damageSource.damageType) {
		case "player":
			if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
				return player.getHeldItemMainhand();
			}
			break;
		case "keybladeOffhand":
			if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
				return player.getHeldItemOffhand();
			}
		}
		return null;
		
	}
		
}
