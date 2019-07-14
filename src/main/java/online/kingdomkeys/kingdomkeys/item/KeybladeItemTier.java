package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

/**
 * ItemTier for the keyblades
 * Attack damage set in constructor as the other fields are the same across all keyblades
 */
public class KeybladeItemTier implements IItemTier {

    float attackDamage;

    public KeybladeItemTier(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public float getEfficiency() {
        return 8.0F;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 30;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.EMPTY;
    }
}
