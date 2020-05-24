package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

public class DamageCalculation {
    /*public static float fireMultiplier = 0.8F;
    public static float blizzardMultiplier = 1;
    public static float aeroMultiplier = 0.2F;*/

    /**
     * Magic
     */
    public static float getMagicDamage(PlayerEntity player, int level, KeybladeItem keyblade) {
        if (player != null) {
        	IPlayerCapabilities props = ModCapabilities.get(player);
        	float damage = 0;
            float finalDamage = 0;

            damage = (float) (keyblade.getMagic() + props.getMagic());

            switch (props.getActiveDriveForm()) {
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

            return finalDamage;//TODO (float) (finalDamage * MainConfig.items.damageMultiplier);
        } else {
            return 0;
        }
    }
    /**
     * Magic
     */
    public static float getMagicDamage(PlayerEntity player, int level, IOrgWeapon weapon) {
        if (player != null) {
        	IPlayerCapabilities props = ModCapabilities.get(player);

            float damage = 0;
            float finalDamage = 0;

            damage = (float) (weapon.getMagic() + props.getMagic());

            switch (props.getActiveDriveForm()) {
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
        	IPlayerCapabilities props = ModCapabilities.get(player);

            float finalDamage = 0;

            if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.MAIN_HAND), ItemStack.EMPTY) && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                finalDamage = getMagicDamage(player, level, (KeybladeItem) player.getHeldItemMainhand().getItem());
            } else {
                finalDamage = props.getMagic();
            }
            return finalDamage; //(float) (finalDamage * MainConfig.items.damageMultiplier);
        } else {
            return 0;
        }
    }


    /**
     * Strength
     */
    public static float getStrengthDamage(PlayerEntity player, KeybladeItem keyblade) {
        if (player != null) {
        	IPlayerCapabilities props = ModCapabilities.get(player);
            float damage = 0;
            float finalDamage = 0;

            damage = (float) (keyblade.getStrength() + props.getStrength());
            System.out.println(damage);

            switch (props.getActiveDriveForm()) {
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

            finalDamage = damage; //(float) (damage * MainConfig.items.damageMultiplier);
            return finalDamage;
        } else {
            return 0;
        }
    }
    /**
     * Strength
     */
    public static float getOrgStrengthDamage(PlayerEntity player, ItemStack weapon) {
        if (player != null) {
        	IPlayerCapabilities props = ModCapabilities.get(player);
            float damage = 0;
            float finalDamage = 0;
            if (weapon.getItem() instanceof IOrgWeapon) {
                damage = (float) (((IOrgWeapon) weapon.getItem()).getStrength() + props.getStrength());

                switch (props.getActiveDriveForm()) {
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

               /* if (weapon.getItem() instanceof ItemClaymore) {
                    if (weapon.getItemDamage() == 1)
                        damage *= 1.25F;
                }*/
                finalDamage = damage; //(float) (damage * MainConfig.items.damageMultiplier);
            }
            return finalDamage;
        } else {
            return 0;
        }
    }
    /**
     * Strength
     */
    public static float getStrengthDamage(PlayerEntity player) {
        if (player != null) {
            float finalDamage = 0;

            if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.MAIN_HAND), ItemStack.EMPTY) && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof KeybladeItem) {
                finalDamage = getStrengthDamage(player, (KeybladeItem) player.getHeldItemMainhand().getItem());
            } else if (!ItemStack.areItemStacksEqual(player.getHeldItem(Hand.MAIN_HAND), ItemStack.EMPTY) && player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof IOrgWeapon) {
                finalDamage = getOrgStrengthDamage(player, player.getHeldItemMainhand());
            }
            return finalDamage;
        } else {
            return 0;
        }
    }
}
