package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

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

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
		// TODO Auto-generated method stub
		return false;
	}
}
