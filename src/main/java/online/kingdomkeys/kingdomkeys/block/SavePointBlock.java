package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

import java.util.Random;

public class SavePointBlock extends BaseBlock {
	private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	public SavePointBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return collisionShape;
	}

	double a = 0;

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		// Tried to make animation here but random tick f*cks it all
		super.animateTick(state, world, pos, random);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			IPlayerCapabilities props = ModCapabilities.get(player);
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
				
			if (props.getMP() < props.getMaxMP() || player.getHealth() < props.getMaxHP()) { // TODO add the rest of things that you get back
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.2, pos.getY()+2.5, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+2.5, pos.getZ()+0.2, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.8, pos.getY()+2.5, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY()+2.5, pos.getZ()+0.8, 0.0D, 0.0D, 0.0D);

				props.addMP(1);
				player.heal(1);
			}
		}
		super.onEntityCollision(state, world, pos, entity);
	}
}
