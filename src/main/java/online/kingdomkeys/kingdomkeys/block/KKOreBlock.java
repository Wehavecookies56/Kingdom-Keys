package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KKOreBlock extends BaseBlock {

	public KKOreBlock(Properties properties) {
		super(properties.requiresCorrectToolForDrops());
	}

	protected int getExperience(RandomSource rand) {
		if (this == ModBlocks.stormyOre.get() ||
				this == ModBlocks.writhingOre.get() || 
				this == ModBlocks.betwixtOre.get() || 
				this == ModBlocks.pulsingOre.get() || 
				this == ModBlocks.writhingOreE.get() ||
				this == ModBlocks.pulsingOreE.get() ||
				this == ModBlocks.sinisterOre.get()) {
			return Mth.nextInt(rand, 3, 7);
		} else if (this == ModBlocks.prizeBlox.get()) {
			return Mth.nextInt(rand, 4, 8);
		} else if (this == ModBlocks.rarePrizeBlox.get()) {
			return Mth.nextInt(rand, 7, 12);
		} else {
			return Mth.nextInt(rand, 0, 2);
		}
	}

	@Override
	public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool) {
		int drop = breaker != null ? this.getExperience(breaker.getRandom()) : 0;
		return tool.getEnchantmentLevel(level.registryAccess().holderOrThrow(Enchantments.SILK_TOUCH)) == 0 ? drop : 0;
	}
}
