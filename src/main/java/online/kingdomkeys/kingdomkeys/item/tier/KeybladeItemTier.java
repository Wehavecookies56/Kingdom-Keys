package online.kingdomkeys.kingdomkeys.item.tier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * ItemTier for the keyblades
 * Attack damage set in constructor as the other fields are the same across all keyblades
 */
public class KeybladeItemTier implements Tier {

    float attackDamage;
    int uses;

    public KeybladeItemTier(float attackDamage) {
        this(attackDamage, 0);
    }

    public KeybladeItemTier(float attackDamage, int uses) {
        this.attackDamage = attackDamage;
        this.uses = uses;
    }

    @Override
    public int getUses() {
        return uses;
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
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return null;
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
