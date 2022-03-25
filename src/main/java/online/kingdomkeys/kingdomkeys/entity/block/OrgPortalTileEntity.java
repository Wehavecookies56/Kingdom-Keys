package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.entity.BlockEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class OrgPortalTileEntity extends BlockEntity implements Tickable {
	UUID uuid;

	public OrgPortalTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_ORG_PORTAL_TE.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		if (uuid != null)
			pTag.putUUID("uuid", uuid);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		if(pTag.hasUUID("uuid"))
			uuid = pTag.getUUID("uuid");
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
		setChanged();
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}
	
	int ticks = 0;
	double a = 0;

	@Override
	public void tick() {
		ticks++;
		if (a < 0)
			a = 1800;
		// Don't do anything unless it's active
		double r = 0.5D;
		double cx = worldPosition.getX() + 0.5;
		double cy = worldPosition.getY() + 0.2;
		double cz = worldPosition.getZ() + 0.5;

		a -= 4; // Speed and distance between particles
		double x = cx + (r * Math.cos(Math.toRadians(a)));
		double z = cz + (r * Math.sin(Math.toRadians(a)));

		double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
		double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

		level.addParticle(new DustParticleOptions(new Vector3f(0.2F, 0F,0.4F), 1F), x, (cy), z, 0.0D, 0.0D, 0.0D);
		level.addParticle(new DustParticleOptions(new Vector3f(0.2F, 0F,0.4F), 1F), x2, (cy), z2, 0.0D, 0.0D, 0.0D);
	}

}