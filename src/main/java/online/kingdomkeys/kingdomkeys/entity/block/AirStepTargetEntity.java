package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class AirStepTargetEntity extends BlockEntity {
	public AirStepTargetEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_AIRSTEP_TARGET_TE.get(), pos, state);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}


	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		level.addAlwaysVisibleParticle(ParticleTypes.END_ROD, pos.getX()+level.random.nextDouble(), pos.getY()+level.random.nextDouble(), pos.getZ()+level.random.nextDouble(), 0,0, 0);
	}

}