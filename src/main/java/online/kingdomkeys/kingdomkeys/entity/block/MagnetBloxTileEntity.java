package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import online.kingdomkeys.kingdomkeys.block.MagnetBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MagnetBloxTileEntity extends TileEntity implements ITickableTileEntity {
	public MagnetBloxTileEntity() {
		super(ModEntities.TYPE_MAGNET_BLOX.get());
	}

	int ticks = 0;

	// Loop through each block in the direction facing for a given range and returns
	// the nunmber of blocks it goes without hitting one
	// Returns the original range if nothing is hit
	int calculateActualRange(Direction facing, int range) {
		int actualRange = range;
		for (int i = 0; i < range; i++) {
			BlockState current = world.getBlockState(pos.offset(facing, i + 1));
			if (current.isSolid() && current.getBlock() != Blocks.AIR) {
				actualRange = i;
				break;
			}
		}
		return actualRange;
	}

	@Override
	public void tick() {
		ticks++;
		// Don't do anything unless it's active
		if (getBlockState().get(MagnetBloxBlock.ACTIVE)) {
			Direction facing = getBlockState().get(MagnetBloxBlock.FACING);
			int range = calculateActualRange(facing, getBlockState().get(MagnetBloxBlock.RANGE));
			// Not very useful if it's 0
			if (range > 0) {
				boolean attract = getBlockState().get(MagnetBloxBlock.ATTRACT);

				if (ticks % 5 == 0) {
					int[] colors = { 1, 0, 0 };
					if (!attract) {
						colors[0] = 0;
						colors[1] = 0;
						colors[2] = 1;
					}
					
					for (double i = 0.7; i < range; i += 0.3) {
						if (facing == Direction.NORTH) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 - i, colors[0], colors[1], colors[2]);
						} else if (facing == Direction.EAST) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5 + i, pos.getY() + 0.5, pos.getZ() + 0.5, colors[0], colors[1], colors[2]);
						} else if (facing == Direction.SOUTH) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5 + i, colors[0], colors[1], colors[2]);
						} else if (facing == Direction.WEST) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5 - i, pos.getY() + 0.5, pos.getZ() + 0.5, colors[0], colors[1], colors[2]);
						} else if (facing == Direction.UP) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5, pos.getY() + 0.5 + i, pos.getZ() + 0.5, colors[0], colors[1], colors[2]);
						} else if (facing == Direction.DOWN) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5, pos.getY() + 0.5 - i, pos.getZ() + 0.5, colors[0], colors[1], colors[2]);
						}
					}
				}

				List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(0, 0, 0, 1, 1, 1).expand(range * facing.getDirectionVec().getX(), range * facing.getDirectionVec().getY(), range * facing.getDirectionVec().getZ()).offset(pos));

				// No reason to do anymore if there are no entities in range
				if (!entities.isEmpty()) {
					double strength = 0.75;
					for (Entity e : entities) {
						Vec3d ePos = e.getPositionVec();
						Vec3d blockPos;
						if(e instanceof LivingEntity) {
							blockPos = new Vec3d(getPos().getX() + 0.5, getPos().getY(), getPos().getZ() + 0.5);
						} else {
							blockPos = new Vec3d(getPos().getX() + 0.5, getPos().getY()+0.5, getPos().getZ() + 0.5);
						}
						// Attract
						if (attract) {
							Vec3d blockDir = blockPos.subtract(ePos);
							e.setMotion(blockDir.normalize().mul(strength, strength, strength));
							// Repel
						} else {
							Vec3d pushDir = new Vec3d(facing.getDirectionVec());
							e.setMotion(pushDir.normalize().mul(strength, strength, strength));
						}
					}
				}
			}
		}
	}
}
