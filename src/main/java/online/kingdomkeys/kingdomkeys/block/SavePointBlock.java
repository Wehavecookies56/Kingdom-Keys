package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SavePointBlock extends BaseBlock {
	private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	public SavePointBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return collisionShape;
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return collisionShape;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return Block.makeCuboidShape(0D, 0D, 0D, 16.0D, 2.0D, 16.0D);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		// Tried to make animation here but random tick f*cks it all
		super.animateTick(state, world, pos, random);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
	    	((ServerPlayerEntity)player).func_242111_a(worldIn.getDimensionKey(), pos.up(), 0F, true, false);
			player.sendStatusMessage(new TranslationTextComponent("block.minecraft.set_spawn"), true);
		} else {
			player.playSound(ModSounds.savespawn.get(), 1F, 1F);
		}
		return ActionResultType.CONSUME;
	}

	UUID lastPlayedSoundPlayer = null;
	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				
			if (playerData.getMP() < playerData.getMaxMP() || player.getHealth() < playerData.getMaxHP() || player.getFoodStats().getFoodLevel() < 20) { // TODO add the rest of things that you get back
				if(player.ticksExisted % 5 == 0) {
					player.playSound(ModSounds.savepoint.get(), 1F, 1F);
				}
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.2, pos.getY()+2.5, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+2.5, pos.getZ()+0.2, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.8, pos.getY()+2.5, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+2.5, pos.getZ()+0.8, 0.0D, 0.0D, 0.0D);

				playerData.addMP(1);
				player.heal(1);
				player.getFoodStats().addStats(1, 1);
			}
		}
		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModEntities.TYPE_SAVEPOINT.get().create();
	}
}
