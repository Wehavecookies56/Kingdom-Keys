package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.AirStepTargetEntity;

import javax.annotation.Nullable;

public class AirStepBlock extends BaseBlock implements EntityBlock, INoDataGen {

	public AirStepBlock(Properties properties) {
		super(properties);
	}

	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Block.box(0,0,0,0,0,0);
	}

	//Collision hitbox touch
	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return super.getShape(state, worldIn, pos, context);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == ModEntities.TYPE_AIRSTEP_TARGET_TE.get() ? AirStepTargetEntity::tick : null;//EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_AIRSTEP_TARGET_TE.get().create(pPos, pState);
	}

}