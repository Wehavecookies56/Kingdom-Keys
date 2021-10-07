package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SavePointBlock extends BaseBlock {
	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	public SavePointBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return collisionShape;
	}

	double a = 0;

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		// Tried to make animation here but random tick f*cks it all
		super.animateTick(state, world, pos, random);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if(!worldIn.isClientSide) {
			player.setSleepingPos(pos.offset(0, 1, 0));
			player.displayClientMessage(new TranslatableComponent("block.minecraft.bed.set_spawn"), true);

		}
		return InteractionResult.CONSUME;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			double r = 0.7D;
			double cx = pos.getX() + 0.5;
			double cy = pos.getY() + 0.5;
			double cz = pos.getZ() + 0.5;

			a -= 5; // Speed and distance between particles
			double x = cx + (r * Math.cos(Math.toRadians(a)));
			double z = cz + (r * Math.sin(Math.toRadians(a)));

			double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
			double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

			world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, (cy + 1.3) - (a / 1800), z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.HAPPY_VILLAGER, x2, (cy + 0.5) - (a / 1800), z2, 0.0D, 0.0D, 0.0D);

			// if(a > 1800) a = 0;
			if (a < 0)
				a = 1800;
				
			if (playerData.getMP() < playerData.getMaxMP() || player.getHealth() < playerData.getMaxHP() || player.getFoodData().getFoodLevel() < 20) { // TODO add the rest of things that you get back
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.2, pos.getY()+2.5, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+2.5, pos.getZ()+0.2, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.8, pos.getY()+2.5, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+2.5, pos.getZ()+0.8, 0.0D, 0.0D, 0.0D);

				playerData.addMP(1);
				player.heal(1);
				player.getFoodData().eat(1, 1);
			}
		}
		super.entityInside(state, world, pos, entity);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
		// TODO Auto-generated method stub
		return false;
	}
}
