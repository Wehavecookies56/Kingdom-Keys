package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DamageCalculation {

    /**
     * Magic KB
     */
    public static float getMagicDamage(Player player, ItemStack stack) {
        if (player != null) {
        	PlayerData playerData = PlayerData.get(player);
        	if(playerData == null)
            	return 0;

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
	            	DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
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
        	PlayerData playerData = PlayerData.get(player);
        	if(playerData == null)
            	return 0;

            return (float) (weapon.getMagic() + playerData.getMagic(true));
        } else {
            return 0;
        }
    }
    /**
     * Magic generic
     */
    public static float getMagicDamage(Player player) {
        if (player != null) {
        	PlayerData playerData = PlayerData.get(player);
        	if(playerData == null)
            	return 0;
        	
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
     * Strength generic
     */
    public static float getStrengthDamage(Player player) {
        if (player != null) {
            PlayerData playerData = PlayerData.get(player);
            if(playerData == null)
                return 0;

            float finalDamage = 0;

            if (!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof KeybladeItem || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
                if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                    finalDamage = getKBStrengthDamage(player, player.getMainHandItem());
                } else if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
                    finalDamage = getOrgStrengthDamage(player, player.getMainHandItem());
                }
            } else {
                finalDamage = playerData.getStrength(true);
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
        	PlayerData playerData = PlayerData.get(player);
        	if(playerData == null)
            	return 0;

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
	            	DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
	            	damage *= form.getStrMult();
	            }
            }
            finalDamage = damage;// + getSharpnessDamage(stack);
           
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
        	PlayerData playerData = PlayerData.get(player);
            float damage = 0;
            float finalDamage = 0;
            if (stack.getItem() instanceof IOrgWeapon) {
            	IOrgWeapon org = (IOrgWeapon) stack.getItem();
                damage = (float) org.getStrength() + playerData.getStrength(true);
                finalDamage = damage;// + getSharpnessDamage(stack);
            }
            return finalDamage;
        } else {
            return 0;
        }
    }
  
    
    public static float getSharpnessDamage(ItemStack stack, RegistryAccess registryAccess) {
		int sharpnessLevel = stack.getEnchantmentLevel(registryAccess.holderOrThrow(Enchantments.SHARPNESS));
    	return getSharpnessDamageFromLevel(sharpnessLevel);
    }
    
    private static float getSharpnessDamageFromLevel(float lvl) {
		return lvl / 2F + 0.5F;
	}
	
}
