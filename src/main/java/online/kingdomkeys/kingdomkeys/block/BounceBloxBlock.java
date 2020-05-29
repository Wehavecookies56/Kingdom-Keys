package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BounceBloxBlock extends BaseBlock {
	private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

	public BounceBloxBlock(Properties properties) {
		super(properties);
	}

	// Negate fall damage when fallen on if the entity is not sneaking
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.isSneaking()) {
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		} else {
			entityIn.onLivingFall(fallDistance, 0.0F);
		}
	}

	/**
	 * Method to make entities bounce
	 * 
	 * @param entity The entity bouncing
	 */
	private void bounce(Entity entity) {
		double bounceFactor = 1;
		entity.setMotion(new Vec3d(entity.getMotion().getX(), bounceFactor, entity.getMotion().getZ()));
		entity.move(MoverType.SELF, entity.getMotion());
		entity.fallDistance = 0;
	}

	// Bounce when landed on if the entity is not sneaking
	@Override
	public void onLanded(IBlockReader worldIn, Entity entityIn) {
		if (Math.abs(entityIn.getMotion().getY()) < 0.1D && !entityIn.isCrouching()) {
			bounce(entityIn);
		} else {
			super.onLanded(worldIn, entityIn);
		}
	}

	// Bounce when walked on if the entity is not sneaking
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if (Math.abs(entityIn.getMotion().getY()) < 0.1D && !entityIn.isCrouching()) {
			bounce(entityIn);
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return collisionShape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		double x = entity.getMotion().x;
		double z = entity.getMotion().z;
		float force = 1;

		if (entity instanceof LivingEntity && ((LivingEntity) entity).onGround) {
			force = 3;
		}

		if (pos.south().equals(entity.getPosition()) || pos.south().down().equals(entity.getPosition())) {
			z = force;
		} else if (pos.north().equals(entity.getPosition()) || pos.north().down().equals(entity.getPosition())) {
			z = -force;
		}

		if (pos.east().equals(entity.getPosition()) || pos.east().down().equals(entity.getPosition())) {
			x = force;
		} else if (pos.west().equals(entity.getPosition()) || pos.west().down().equals(entity.getPosition())) {
			x = -force;
		}

		if (!entity.isSneaking()) {
			entity.setMotion(new Vec3d(x, entity.getMotion().getY(), z));
		}
		super.onEntityCollision(state, world, pos, entity);
	}
}
