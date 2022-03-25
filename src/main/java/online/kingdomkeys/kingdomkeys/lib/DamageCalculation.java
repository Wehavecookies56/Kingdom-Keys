package online.kingdomkeys.kingdomkeys.lib;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
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
    public static float getMagicDamage(Player player, ItemStack stack) {
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
	            damage = (float) (keyblade.getMagic(stack) + playerData.getMagic(true));
	            if(!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
	            	DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
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
    public static float getOrgMagicDamage(Player player, IOrgWeapon weapon) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            float damage = (float) (weapon.getMagic() + playerData.getMagic(true));
            return damage;
        } else {
            return 0;
        }
    }
    /**
     * Magic generic
     */
    public static float getMagicDamage(Player player) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

            float finalDamage = 0;

            if (player.getItemInHand(InteractionHand.MAIN_HAND) != null && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof KeybladeItem || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
            	if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                    finalDamage = getMagicDamage(player, player.getMainHandItem());
            	} else if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
            		finalDamage = getOrgMagicDamage(player, (IOrgWeapon) player.getMainHandItem().getItem());
            	}
            } else {
                finalDamage = playerData.getMagic(true);
            }
            return finalDamage;
        } else {
            return 0;
        }
    }


    /**
     * Strength
     */
    public static float getKBStrengthDamage(Player player, ItemStack stack) {
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
            
	            damage = (float) (keyblade.getStrength(stack) + playerData.getStrength(true));
	
	            if(!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
	            	DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
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
    public static float getOrgStrengthDamage(Player player, ItemStack stack) {
        if (player != null) {
        	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            float damage = 0;
            float finalDamage = 0;
            if (stack.getItem() instanceof IOrgWeapon) {
            	IOrgWeapon org = (IOrgWeapon) stack.getItem();
                damage = (float) org.getStrength() + playerData.getStrength(true);
                finalDamage = damage + getSharpnessDamage(stack);
            }
            return finalDamage;
        } else {
            return 0;
        }
    }
  
    
    public static float getSharpnessDamage(ItemStack stack) {
		ListTag nbttaglist = stack.getEnchantmentTags();
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
