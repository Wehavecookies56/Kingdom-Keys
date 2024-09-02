
package online.kingdomkeys.kingdomkeys.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

//Thank you Curios for the example!
// modified to work with the LuckyLucky effect.

public class FortuneBonusModifier extends LootModifier {
    public static final Supplier<MapCodec<FortuneBonusModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, FortuneBonusModifier::new)));

    protected FortuneBonusModifier(LootItemCondition[] conditions)
    {
        super(conditions);
    }

    @Nonnull
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		final String hasLuckyLuckyBonus = "HasLuckyLuckyBonus";

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);


        if (tool != null && (!tool.hasTag() || tool.getTag() == null || !tool.getTag().getBoolean(hasLuckyLuckyBonus)))
        {
            Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
            BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
            BlockEntity blockEntity = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
            Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

            if (blockState != null && entity instanceof Player player)
            {
                // bonus for lucky amplifier.
				PlayerData playerData = PlayerData.get(player);
				int totalFortuneBonus = playerData.getNumberOfAbilitiesEquipped(Strings.luckyLucky);

				if (totalFortuneBonus > 0) {
					ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();

					fakeTool.getOrCreateTag().putBoolean(hasLuckyLuckyBonus, true);

                    Holder<Enchantment> fortune = player.level().registryAccess().holderOrThrow(Enchantments.FORTUNE);
                    fakeTool.enchant(fortune, fakeTool.getEnchantmentLevel(fortune) + totalFortuneBonus);

                    if (origin == null) {
                        origin = player.position();
                    }

                    LootParams.Builder builder = new LootParams.Builder((ServerLevel) player.level());
                    builder.withParameter(LootContextParams.TOOL, fakeTool);
                    builder.withParameter(LootContextParams.BLOCK_STATE, blockState);
                    builder.withParameter(LootContextParams.ORIGIN, origin);
                    builder.withParameter(LootContextParams.BLOCK_ENTITY, blockEntity);

                    LootParams newContext = builder.create(LootContextParamSets.BLOCK);
                    LootTable lootTable = context.getLevel().getServer().reloadableRegistries().getLootTable(blockState.getBlock().getLootTable());

                    return lootTable.getRandomItems(newContext);
                }
            }
        }

		// otherwise return the context that was passed in. no modification needed.
		return generatedLoot;
	}

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return (MapCodec<? extends IGlobalLootModifier>) CODEC;
    }
}
