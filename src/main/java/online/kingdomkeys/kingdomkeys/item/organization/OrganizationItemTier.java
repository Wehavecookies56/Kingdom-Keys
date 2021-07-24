package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * ItemTier for the keyblades
 * Attack damage set in constructor as the other fields are the same across all keyblades
 */
public class OrganizationItemTier implements Tier {

    float attackDamage;

    public OrganizationItemTier(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public float getSpeed() {
        return 8.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public int getEnchantmentValue() {
        return 30;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
}
