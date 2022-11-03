package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class KKOreBlock extends BaseBlock {

	public KKOreBlock(Properties properties) {
		super(properties);
	}

	protected int getExperience(Random rand) {
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
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}
