package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.UUID;

public class SavepointTileEntity extends BlockEntity {
	public static float[] WARP_COLOR = new float[]{0.6F, 1F, 1F};
	public static float[] SAVEPOINT_COLOR = new float[]{0.3F, 1F, 0.3F};

	public SavepointTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_SAVEPOINT.get(), pos, state);
	}
	long ticks;
	//TODO Change savepoint type to tier
	private UUID id = UUID.randomUUID();

	public UUID getID() {
		return id;
	}

	private int
			heal = 20,
			hunger = 20,
			magic = 20,
			drive = 20,
			focus = 20
	;

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
	public void load(CompoundTag pTag) {
		super.load(pTag);
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
	protected void saveAdditional(CompoundTag pTag) {
		if (getBlockState().getValue(SavePointBlock.TIER) != SavePointStorage.SavePointType.NORMAL) {
			pTag.putUUID("savepoint_id", id);
		}
		pTag.putInt("heal",heal);
		pTag.putInt("hunger",hunger);
		pTag.putInt("magic",magic);
		pTag.putInt("drive",drive);
		pTag.putInt("focus",focus);
		super.saveAdditional(pTag);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

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

			float[] color = state.getValue(SavePointBlock.TIER) == SavePointStorage.SavePointType.WARP ? WARP_COLOR : SAVEPOINT_COLOR;

			level.addParticle(new DustParticleOptions(new Vector3f(color[0],color[1],color[2]), 1F), x, (cy - 0.5) - (-savepoint.ticks / 1800F), z, 0.0D, 0.0D, 0.0D);
			level.addParticle(new DustParticleOptions(new Vector3f(color[0],color[1],color[2]), 1F), x2, (cy + 0.5) - (savepoint.ticks / 1800F), z2, 0.0D, 0.0D, 0.0D);
		}
	}
}