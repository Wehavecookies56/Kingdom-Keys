package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

public class DamageCalculation {

    /**
     * Magic KB
     */
    public static float getMagicDamage(PlayerEntity player, ItemStack stack) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        	float damage = 0;

            KeybladeItem keyblade = null;
            if(stack.getItem() instanceof KeychainItem) {
            	keyblade = ((KeychainItem) stack.getItem()).getKeyblade();
            } else if(stack.getItem() instanceof KeybladeItem) {
            	keyblade = (KeybladeItem) stack.getItem();
            }
            
            if(keyblade != null) {
	            damage = (float) (keyblade.getMagic(stack) + playerData.getMagic());
	
	            switch (playerData.getActiveDriveForm()) {
	                case Strings.Form_Wisdom:
	                    damage = damage * 1.5F;
	                    break;
	                case Strings.Form_Master:
	                    damage = (float) (damage * 1.75);
	                    break;
	                case Strings.Form_Final:
	                    damage = (float) (damage * 2F);
	                    break;
	            }

            }
            return damage;
        } else {
            return 0;
        }
    }
    /**
     * Magic org
     */
    public static float getOrgMagicDamage(PlayerEntity player, IOrgWeapon weapon) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            float damage = (float) (weapon.getMagic() + playerData.getMagic());
            return damage;
        } else {
            return 0;
        }
    }
    /**
     * Magic generic
     */
    public static float getMagicDamage(PlayerEntity player) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

            float finalDamage = 0;

            if (player.getHeldItem(Hand.MAIN_HAND) != null && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof KeybladeItem || player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
            	if(player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                    finalDamage = getMagicDamage(player, player.getHeldItemMainhand());
            	} else if(player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
            		finalDamage = getOrgMagicDamage(player, (IOrgWeapon) player.getHeldItemMainhand().getItem());
            	}
            } else {
                finalDamage = playerData.getMagic();
            }
            return finalDamage; //(float) (finalDamage * MainConfig.items.damageMultiplier);
        } else {
            return 0;
        }
    }


    /**
     * Strength
     */
    public static float getKBStrengthDamage(PlayerEntity player, ItemStack stack) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            float damage = 0;
            float finalDamage = 0;

            KeybladeItem keyblade = null;
            if(stack.getItem() instanceof KeychainItem) {
            	keyblade = ((KeychainItem) stack.getItem()).getKeyblade();
            } else if(stack.getItem() instanceof KeybladeItem) {
            	keyblade = (KeybladeItem) stack.getItem();
            }
            
            if(keyblade != null) {
            
	            damage = (float) (keyblade.getStrength(stack) + playerData.getStrength());
	            //System.out.println(damage);
	
	            switch (playerData.getActiveDriveForm()) {
	                case Strings.Form_Valor:
	                    damage = (float) (damage * 1.5);
	                    break;
	                case Strings.Form_Limit:
	                    damage = (float) (damage * 1.2);
	                    break;
	                case Strings.Form_Master:
	                    damage = (float) (damage * 1.5);
	                    break;
	                case Strings.Form_Final:
	                    damage = (float) (damage * 1.7);
	                    break;
	            }

            }
            finalDamage = damage + getSharpnessDamage(stack); //(float) (damage * MainConfig.items.damageMultiplier);
            return finalDamage;
        } else {
            return 0;
        }
    }
    
    /**
     * Strength
     */
    public static float getOrgStrengthDamage(PlayerEntity player, ItemStack stack) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            float damage = 0;
            float finalDamage = 0;
            if (stack.getItem() instanceof IOrgWeapon) {
            	IOrgWeapon org = (IOrgWeapon) stack.getItem();
                damage = (float) org.getStrength() + playerData.getStrength();
                finalDamage = damage + getSharpnessDamage(stack); //(float) (damage * MainConfig.items.damageMultiplier);
            }
            return finalDamage;
        } else {
            return 0;
        }
    }
  
    
    public static float getSharpnessDamage(ItemStack stack) {
		ListNBT nbttaglist = stack.getEnchantmentTagList();
    	float sharpnessDamage = 0;
		for (int i = 0; i < nbttaglist.size(); i++) {
			String id = nbttaglist.getCompound(i).getString("id");
			int lvl = nbttaglist.getCompound(i).getShort("lvl");

			// System.out.println(Enchantment.getEnchantmentByID(id).getName());
			if (id.equals("minecraft:sharpness")) {
				sharpnessDamage = getSharpnessDamageFromLevel(lvl);
			}
		}
		return sharpnessDamage;
    }
    
    private static float getSharpnessDamageFromLevel(float lvl) {
		return lvl / 2F + 0.5F;
	}
}
