
package online.kingdomkeys.kingdomkeys.loot;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;


//Thank you Curios for the example !
// modified to work with the LuckyLucky effect.

public class FortuneBonusModifier extends LootModifier
{
    protected FortuneBonusModifier(ILootCondition[] conditions)
    {
        super(conditions);
    }

    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
    {
        final String hasLuckyLuckyBonus = "HasLuckyLuckyBonus";

        ItemStack tool = context.get(LootParameters.TOOL);

        if (tool != null && (!tool.hasTag() || tool.getTag() == null || !tool.getTag().getBoolean(hasLuckyLuckyBonus)))
        {
            Entity entity = context.get(LootParameters.THIS_ENTITY);
            BlockState blockState = context.get(LootParameters.BLOCK_STATE);

            if (blockState != null && entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entity;

                //bonus for lucky amplifier.
                IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
                int totalFortuneBonus = playerData.getNumberOfAbilitiesEquipped(Strings.luckyLucky);

                if (totalFortuneBonus > 0)
                {
                    ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();

                    fakeTool.getOrCreateTag().putBoolean(hasLuckyLuckyBonus, true);

                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(fakeTool);
                    enchantments.put(Enchantments.FORTUNE, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, fakeTool) + totalFortuneBonus);

                    EnchantmentHelper.setEnchantments(enchantments, fakeTool);

                    LootContext.Builder builder = new LootContext.Builder(context);
                    builder.withParameter(LootParameters.TOOL, fakeTool);

                    LootContext newContext = builder.build(LootParameterSets.BLOCK);
                    LootTable lootTable = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(blockState.getBlock().getLootTable());

                    return lootTable.generate(newContext);
                }
            }
        }

        //otherwise return the context that was passed in. no modification needed.
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FortuneBonusModifier>
    {
        public Serializer()
        {
            KingdomKeys.LOGGER.info("Fortune bonus modifier setting up");
        }

        public FortuneBonusModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions)
        {
            return new FortuneBonusModifier(conditions);
        }

        public JsonObject write(FortuneBonusModifier instance)
        {
            return this.makeConditions(instance.conditions);
        }
    }
}
