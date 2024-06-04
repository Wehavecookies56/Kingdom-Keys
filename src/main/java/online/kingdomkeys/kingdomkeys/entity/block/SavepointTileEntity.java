package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import org.joml.Vector3f;

public class SavepointTileEntity extends BlockEntity {
	public SavepointTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_SAVEPOINT.get(), pos, state);
	}
	long ticks;

	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		if(blockEntity instanceof SavepointTileEntity savepoint) {
			if (savepoint.ticks > 1800)
				savepoint.ticks = 0;

			// Don't do anything unless it's active
			double r = 0.7D;
			double cx = pos.getX() + 0.5;
			double cy = pos.getY() + 0.5;
			double cz = pos.getZ() + 0.5;

			savepoint.ticks += 10; // Speed and distance between particles
			double x = cx + (r * Math.cos(Math.toRadians(savepoint.ticks)));
			double z = cz + (r * Math.sin(Math.toRadians(savepoint.ticks)));

			double x2 = cx + (r * Math.cos(Math.toRadians(-savepoint.ticks)));
			double z2 = cz + (r * Math.sin(Math.toRadians(-savepoint.ticks)));

			level.addParticle(new DustParticleOptions(new Vector3f(0F, 1F, 0F), 1F), x, (cy - 0.5) - (-savepoint.ticks / 1800F), z, 0.0D, 0.0D, 0.0D);
			level.addParticle(new DustParticleOptions(new Vector3f(0.3F, 1F, 0.3F), 1F), x2, (cy + 0.5) - (savepoint.ticks / 1800F), z2, 0.0D, 0.0D, 0.0D);
		}
	}
}

