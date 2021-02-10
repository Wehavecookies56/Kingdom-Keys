package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class KKOreBlock extends BaseBlock {

	public KKOreBlock(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	protected int getExperience(Random rand) {
		if (this == ModBlocks.stormyOre.get() ||
				this == ModBlocks.writhingOre.get() || 
				this == ModBlocks.betwixtOre.get() || 
				this == ModBlocks.pulsingOre.get() || 
				this == ModBlocks.writhingOreE.get() ||
				this == ModBlocks.pulsingOreE.get() ||
				this == ModBlocks.sinisterOre.get()) {
			return MathHelper.nextInt(rand, 3, 7);
		} else if (this == ModBlocks.prizeBlox.get()) {
			return MathHelper.nextInt(rand, 4, 8);
		} else if (this == ModBlocks.rarePrizeBlox.get()) {
			return MathHelper.nextInt(rand, 7, 12);
		} else {
			return MathHelper.nextInt(rand, 0, 2);
		}
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(RANDOM) : 0;
	}
}
