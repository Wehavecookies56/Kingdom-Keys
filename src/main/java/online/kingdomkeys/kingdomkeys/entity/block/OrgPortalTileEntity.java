package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.client.render.block.SavePointBlockEntityRenderer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import javax.annotation.Nullable;
import java.util.UUID;

public class OrgPortalTileEntity extends BlockEntity {
	UUID uuid;

	public SavePointBlockEntityRenderer.SavePointParticle[] particles = new SavePointBlockEntityRenderer.SavePointParticle[2];
	public long lastUpdateTick = -1;

	public OrgPortalTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_ORG_PORTAL_TE.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
		super.saveAdditional(pTag, registries);
		if (uuid != null)
			pTag.putUUID("uuid", uuid);
	}

	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
		super.loadAdditional(pTag, registries);
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
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		this.loadAdditional(tag, registries);
	}
}