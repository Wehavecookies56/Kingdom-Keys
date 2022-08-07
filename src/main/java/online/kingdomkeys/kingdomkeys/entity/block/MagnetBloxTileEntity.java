package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.List;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.block.GhostBloxBlock;
import online.kingdomkeys.kingdomkeys.block.MagnetBloxBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MagnetBloxTileEntity extends BlockEntity {
	public MagnetBloxTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_MAGNET_BLOX.get(), pos, state);
	}

	int ticks = 0;

	// Loop through each block in the direction3facing for a given range and returns
	// the nunmber of blocks it goes without hitting one
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
				if (current.canOcclude() && current.getBlock() != Blocks.AIR) {
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
			// Not very useful if it's 0
			if (range > 0) {
				boolean attract = state.getValue(MagnetBloxBlock.ATTRACT);

				if (TE.ticks % 5 == 0) {
					int[] colors = { 1, 0, 0 };
					if (!attract) {
						colors[0] = 0;
						colors[1] = 0;
						colors[2] = 1;
					}

					for (double i = 0.7; i < range; i += 0.3) {
						float scale = 1+ (float) i / 6;
						if (facing == Direction.NORTH) {
							level.addParticle(new DustParticleOptions(new Vector3f(colors[0], colors[1], colors[2]), scale), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 - i, 0,0,0);
						} else if (facing == Direction.EAST) {
							level.addParticle(new DustParticleOptions(new Vector3f(colors[0], colors[1], colors[2]), scale), pos.getX() + 0.5 + i, pos.getY() + 0.5, pos.getZ() + 0.5,0,0,0);
						} else if (facing == Direction.SOUTH) {
							level.addParticle(new DustParticleOptions(new Vector3f(colors[0], colors[1], colors[2]), scale), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 + i,0,0,0);
						} else if (facing == Direction.WEST) {
							level.addParticle(new DustParticleOptions(new Vector3f(colors[0], colors[1], colors[2]), scale), pos.getX() + 0.5 - i, pos.getY() + 0.5, pos.getZ() + 0.5, 0,0,0);
						} else if (facing == Direction.UP) {
							level.addParticle(new DustParticleOptions(new Vector3f(colors[0], colors[1], colors[2]), scale), pos.getX() + 0.5, pos.getY() + 0.5 + i, pos.getZ() + 0.5, 0,0,0);
						} else if (facing == Direction.DOWN) {
							level.addParticle(new DustParticleOptions(new Vector3f(colors[0], colors[1], colors[2]), scale), pos.getX() + 0.5, pos.getY() + 0.5 - i, pos.getZ() + 0.5, 0,0,0);
						}
					}
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
