package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.client.render.block.SavePointBlockEntityRenderer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SavepointTileEntity extends BlockEntity {
	public SavePointBlockEntityRenderer.SavePointParticle[] particles = new SavePointBlockEntityRenderer.SavePointParticle[2];
	public long lastUpdateTick = -1;

	public SavepointTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_SAVEPOINT.get(), pos, state);
	}

	private UUID id = UUID.randomUUID();

	public UUID getID() {
		return id;
	}

	private int heal = Utils.SAVEPOINT_START,
				hunger = Utils.SAVEPOINT_START,
				magic = Utils.SAVEPOINT_START,
				drive = Utils.SAVEPOINT_START,
				focus = Utils.SAVEPOINT_START;

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
		setChanged();
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
		setChanged();
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
		setChanged();
	}

	public int getDrive() {
		return drive;
	}

	public void setDrive(int drive) {
		this.drive = drive;
		setChanged();
	}

	public int getFocus() {
		return focus;
	}

	public void setFocus(int focus) {
		this.focus = focus;
		setChanged();
	}

	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
		super.loadAdditional(pTag, registries);
		if (getBlockState().getValue(SavePointBlock.TIER) != SavePointStorage.SavePointType.NORMAL) {
			id = pTag.getUUID("savepoint_id");
		}
		heal = pTag.getInt("heal");
		hunger = pTag.getInt("hunger");
		magic = pTag.getInt("magic");
		drive = pTag.getInt("drive");
		focus = pTag.getInt("focus");
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
		if (getBlockState().getValue(SavePointBlock.TIER) != SavePointStorage.SavePointType.NORMAL) {
			pTag.putUUID("savepoint_id", id);
		}
		pTag.putInt("heal",heal);
		pTag.putInt("hunger",hunger);
		pTag.putInt("magic",magic);
		pTag.putInt("drive",drive);
		pTag.putInt("focus",focus);
		super.saveAdditional(pTag, registries);
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

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}