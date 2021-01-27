package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.damagesource.KeybladeDamageSource;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

public class DamageCalculation {
    /*public static float fireMultiplier = 0.8F;
    public static float blizzardMultiplier = 1;
    public static float aeroMultiplier = 0.2F;*/

    /**
     * Magic
     */
    public static float getMagicDamage(PlayerEntity player, int level, ItemStack stack) {
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
	            damage = (float) (keyblade.getMagic(stack) + playerData.getMagic());
	
	            switch (playerData.getActiveDriveForm()) {
	                case Strings.Form_Wisdom:
	                    damage = damage * 2;
	                    break;
	                case Strings.Form_Master:
	                    damage = (float) (damage * 2.25);
	                    break;
	                case Strings.Form_Final:
	                    damage = (float) (damage * 2.5);
	                    break;
	            }
	
	            switch (level) {
	                case 1:
	                    finalDamage = damage;
	                    break;
	                case 2:
	                    finalDamage = (float) (damage + (0.1 * damage));
	                    break;
	                case 3:
	                    finalDamage = (float) (damage + (0.2 * damage));
	                    break;
	            }
            }

            return finalDamage;//TODO (float) (finalDamage * MainConfig.items.damageMultiplier);
        } else {
            return 0;
        }
    }
    /**
     * Magic
     */
    public static float getOrgMagicDamage(PlayerEntity player, int level, IOrgWeapon weapon) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            float damage = 0;
            float finalDamage = 0;

            damage = (float) (weapon.getMagic() + playerData.getMagic());

            switch (level) {
                case 1:
                    finalDamage = damage;
                    break;
                case 2:
                    finalDamage = (float) (damage + (0.1 * damage));
                    break;
                case 3:
                    finalDamage = (float) (damage + (0.2 * damage));
                    break;
            }
            //System.out.println("Magic: "+finalDamage);

            return finalDamage; //(float) (finalDamage * MainConfig.items.damageMultiplier);
        } else {
            return 0;
        }
    }
    /**
     * Magic
     */
    public static float getMagicDamage(PlayerEntity player, int level) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

            float finalDamage = 0;

            if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.MAIN_HAND), ItemStack.EMPTY)) {
            	if(player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                    finalDamage = getMagicDamage(player, level, player.getHeldItemMainhand());
            	} else if(player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
            		finalDamage = getOrgMagicDamage(player, level, (IOrgWeapon) player.getHeldItemMainhand().getItem());
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
  
    /**
     * Strength
     
    public static float getStrengthDamage(DamageSource source, PlayerEntity player) {
        if (player != null) {
            float finalDamage = 0;
            
            ItemStack stack = KeybladeDamageSource.getKeybladeDamageStack(source, player);
            if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.MAIN_HAND), ItemStack.EMPTY) && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                finalDamage = getKBStrengthDamage(player, player.getHeldItemMainhand());
            } else if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.MAIN_HAND), ItemStack.EMPTY) && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
                finalDamage = getOrgStrengthDamage(player, player.getHeldItemMainhand());
            }
            return finalDamage;
        } else {
            return 0;
        }
    }
    */
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
