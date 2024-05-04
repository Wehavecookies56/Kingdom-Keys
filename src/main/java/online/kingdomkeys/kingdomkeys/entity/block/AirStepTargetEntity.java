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
	UUID uuid;

	public AirStepTargetEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_AIRSTEP_TARGET_TE.get(), pos, state);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}


	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		Random pRandom = new Random();
		level.addParticle(ParticleTypes.END_ROD, pos.getX()+pRandom.nextDouble(), pos.getY()+pRandom.nextDouble(), pos.getZ()+pRandom.nextDouble(), 0,0, 0);
	}

}