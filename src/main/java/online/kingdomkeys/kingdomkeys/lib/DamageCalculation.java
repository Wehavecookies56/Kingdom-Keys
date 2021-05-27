package online.kingdomkeys.kingdomkeys.lib;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
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
	            if(!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
	            	DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(playerData.getActiveDriveForm()));
	            	damage *= form.getMagMult();
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
            return finalDamage;
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
	
	            if(!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
	            	DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(playerData.getActiveDriveForm()));
	            	damage *= form.getStrMult();
	            }
            }
            finalDamage = damage + getSharpnessDamage(stack);
           
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("###.##",otherSymbols);
            return Float.parseFloat(df.format(finalDamage));
            
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
                finalDamage = damage + getSharpnessDamage(stack);
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
