package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SavepointTileEntity extends TileEntity implements ITickableTileEntity {
	public SavepointTileEntity() {
		super(ModEntities.TYPE_SAVEPOINT.get());
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
		double cx = pos.getX() + 0.5;
		double cy = pos.getY() + 0.5;
		double cz = pos.getZ() + 0.5;

		a -= 10; // Speed and distance between particles
		double x = cx + (r * Math.cos(Math.toRadians(a)));
		double z = cz + (r * Math.sin(Math.toRadians(a)));

		double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
		double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

		//((ServerWorld)world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, x, (cy + 1) - (a / 1800), z, 1, 0, 0, 0,1);
		//((ServerWorld)world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, x2, (cy + 0.5) - (a / 1800), z2, 1, 0, 0, 0,1);

		world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, (cy + 1.3) - (a / 1800), z, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, x2, (cy + 0.5) - (a / 1800), z2, 0.0D, 0.0D, 0.0D);
	}

}

