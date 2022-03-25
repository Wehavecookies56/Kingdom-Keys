package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SavepointTileEntity extends BlockEntity implements Tickable {
	public SavepointTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_SAVEPOINT.get(), pos, state);
	}

	int ticks = 0;
	double a = 0;

	@Override
	public void tick() {
		ticks++;
		if (a < 0)
			a = 1800;
		// Don't do anything unless it's active
		double r = 0.7D;
		double cx = worldPosition.getX() + 0.5;
		double cy = worldPosition.getY() + 0.5;
		double cz = worldPosition.getZ() + 0.5;

		a -= 5; // Speed and distance between particles
		double x = cx + (r * Math.cos(Math.toRadians(a)));
		double z = cz + (r * Math.sin(Math.toRadians(a)));

		double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
		double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

		//((ServerWorld)world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, x, (cy + 1) - (a / 1800), z, 1, 0, 0, 0,1);
		//((ServerWorld)world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, x2, (cy + 0.5) - (a / 1800), z2, 1, 0, 0, 0,1);

		level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, (cy - 0.5) - (-a / 1800), z, 0.0D, 0.0D, 0.0D);
		level.addParticle(ParticleTypes.HAPPY_VILLAGER, x2, (cy + 0.5) - (a / 1800), z2, 0.0D, 0.0D, 0.0D);
	}

}

