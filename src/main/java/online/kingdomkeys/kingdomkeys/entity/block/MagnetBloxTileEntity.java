package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.block.GhostBloxBlock;
import online.kingdomkeys.kingdomkeys.block.MagnetBloxBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.awt.*;
import java.util.List;

public class MagnetBloxTileEntity extends BlockEntity {
	public MagnetBloxTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_MAGNET_BLOX.get(), pos, state);
	}

	int ticks = 0;

	// Loop through each block in the direction3facing for a given range and returns the nunmber of blocks it goes without hitting one
	// Returns the original range if nothing is hit
	int calculateActualRange(Direction facing, int range) {
		int actualRange = range;
		for (int i = 0; i < range; i++) {
			BlockState current = level.getBlockState(worldPosition.relative(facing, i + 1));
			if (current.getBlock() == ModBlocks.ghostBlox.get()) {
				if (current.getValue(GhostBloxBlock.VISIBLE)) {
					actualRange = i;
					break;
				}
			} else {
				if (current.getBlock() != Blocks.AIR && current.canOcclude()) {
					actualRange = i;
					break;
				}
			}
		}
		return actualRange;
	}

	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		MagnetBloxTileEntity TE = (MagnetBloxTileEntity) blockEntity;
		TE.ticks++;
		// Don't do anything unless it's active
		if (state.getValue(MagnetBloxBlock.ACTIVE)) {
			Direction facing = state.getValue(MagnetBloxBlock.FACING);
			int range = TE.calculateActualRange(facing, state.getValue(MagnetBloxBlock.RANGE));
			if (range > 0) {
				boolean attract = state.getValue(MagnetBloxBlock.ATTRACT);
				if (level.isClientSide() && TE.ticks % (11 - range) == 0) { //TODO spawn less trails if range is less
					ClientUtils.spawnRandomMiniTrail(pos, facing, range, attract);
				}

				List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(0, 0, 0, 1, 1, 1).expandTowards(range * facing.getNormal().getX(), range * facing.getNormal().getY(), range * facing.getNormal().getZ()).move(pos));

				// No reason to do anymore if there are no entities in range
				if (!entities.isEmpty()) {
					double strength = 0.75;
					for (Entity e : entities) {
						Vec3 pushDir = TE.toVector3f(facing);
						strength = attract ? -strength : strength;
						e.setDeltaMovement(pushDir.normalize().multiply(strength, strength, strength));
					}
				}
			}
		}
	}

	public Vec3 toVector3f(Direction facing) {
		return new Vec3((float) facing.getStepX(), (float) facing.getStepY(), (float) facing.getStepZ());
	}
}
