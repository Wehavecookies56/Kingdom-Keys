package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BounceBloxBlock extends BaseBlock {
	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

	public BounceBloxBlock(Properties properties) {
		super(properties);
	}

	// Negate fall damage when fallen on if the entity is not sneaking
	@Override
	public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.isShiftKeyDown()) {
			super.fallOn(worldIn, state, pos, entityIn, fallDistance);
		} else {
			entityIn.causeFallDamage(fallDistance, 0.0F, entityIn.damageSources().fall());
		}
	}

	/**
	 * Method to make entities bounce
	 * 
	 * @param entity The entity bouncing
	 */
	private void bounce(Entity entity) {
		double bounceFactor = 1;
		entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x(), bounceFactor, entity.getDeltaMovement().z()));
		entity.move(MoverType.SELF, entity.getDeltaMovement());
		entity.fallDistance = 0;
	}

	// Bounce when landed on if the entity is not sneaking
	@Override
	public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
		if (Math.abs(entityIn.getDeltaMovement().y()) < 0.1D && !entityIn.isCrouching()) {
			bounce(entityIn);
		} else {
			super.updateEntityAfterFallOn(worldIn, entityIn);
		}
	}

	// Bounce when walked on if the entity is not sneaking
	@Override
	public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
		if (Math.abs(entityIn.getDeltaMovement().y()) < 0.1D && !entityIn.isCrouching()) {
			bounce(entityIn);
		}
		super.stepOn(worldIn, pos, state, entityIn);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return collisionShape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		double x = entity.getDeltaMovement().x;
		double z = entity.getDeltaMovement().z;
		float force = 1;

		if (entity instanceof LivingEntity && entity.onGround()) {
			force = 3;
		}

		if (pos.south().equals(entity.blockPosition()) || pos.south().below().equals(entity.blockPosition())) {
			z = force;
		} else if (pos.north().equals(entity.blockPosition()) || pos.north().below().equals(entity.blockPosition())) {
			z = -force;
		}

		if (pos.east().equals(entity.blockPosition()) || pos.east().below().equals(entity.blockPosition())) {
			x = force;
		} else if (pos.west().equals(entity.blockPosition()) || pos.west().below().equals(entity.blockPosition())) {
			x = -force;
		}

		if (!entity.isShiftKeyDown()) {
			entity.setDeltaMovement(new Vec3(x, entity.getDeltaMovement().y(), z));
		}
		super.entityInside(state, world, pos, entity);
	}
}
