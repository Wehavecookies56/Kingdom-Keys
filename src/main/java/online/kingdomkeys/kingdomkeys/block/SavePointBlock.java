package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class SavePointBlock extends BaseBlock implements EntityBlock, INoDataGen {
	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	boolean linked;
	public SavePointBlock(Properties properties, boolean linked) {
		super(properties);
		this.linked = linked;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return collisionShape;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return collisionShape;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Block.box(0D, 0D, 0D, 16.0D, 2.0D, 16.0D);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if(!worldIn.isClientSide) {
	    	((ServerPlayer)player).setRespawnPosition(worldIn.dimension(), pos.above(), 0F, true, false);
			player.displayClientMessage(Component.translatable("block.minecraft.set_spawn"), true);
		} else {
			player.playSound(ModSounds.savespawn.get(), 1F, 1F);
		}
		return InteractionResult.CONSUME;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof Player player) {
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (playerData != null) {
				String list = linked ? ModConfigs.linkedSavePointRecovers : ModConfigs.savePointRecovers;
				if(list.contains("HP") && player.getHealth() < playerData.getMaxHP()){
					player.heal(1);
					showParticles(player,world,pos);
				}
				if(list.contains("HUNGER") && player.getFoodData().getFoodLevel() < 20){
					player.getFoodData().eat(1, 1);
					showParticles(player,world,pos);
				}
				if(list.contains("MP") && playerData.getMP() < playerData.getMaxMP()){
					playerData.addMP(1);
					showParticles(player,world,pos);
				}
				if(list.contains("FOCUS") && playerData.getFocus() < playerData.getMaxFocus()){
					playerData.addFocus(1);
					showParticles(player,world,pos);
				}
				if(list.contains("DRIVE") && playerData.getDP() < playerData.getMaxDP()){
					playerData.addDP(5);
					showParticles(player,world,pos);
				}
			}
		}
		super.entityInside(state, world, pos, entity);
	}

	public void showParticles(Player player, Level world, BlockPos pos){
		if (player.tickCount % 5 == 0) {
			player.playSound(ModSounds.savepoint.get(), 1F, 1F);
		}
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.2, pos.getY() + 2.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.2, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.8, pos.getY() + 2.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.8, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == ModEntities.TYPE_SAVEPOINT.get() ? SavepointTileEntity::tick : null;//EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_SAVEPOINT.get().create(pPos, pState);
	}
}
