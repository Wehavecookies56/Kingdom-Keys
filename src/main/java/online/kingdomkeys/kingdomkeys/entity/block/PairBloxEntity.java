package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.PairBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class PairBloxEntity extends Entity {

	private static final EntityDataAccessor<Integer> PAIR = SynchedEntityData.defineId(PairBloxEntity.class, EntityDataSerializers.INT);
	private int pair = 0;
	

	public PairBloxEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public PairBloxEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_PAIR_BLOX.get(), world);
	}

	public PairBloxEntity(Level world, double x, double y, double z, int pair) {
		this(ModEntities.TYPE_PAIR_BLOX.get(), world);
		this.setPos(x+0.5, y, z+0.5);
		this.setPair(pair);
		this.xo = x+0.5;
		this.yo = y;
		this.zo = z+0.5;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(PAIR, 0);
	}

	@Override
	public boolean isPickable() {
		return this.isAlive();
	}

	@Override
	public void tick() {
		this.xo = this.blockPosition().getX();
		this.yo = this.blockPosition().getY();
		this.zo = this.blockPosition().getZ();
		
		this.move(MoverType.SELF, this.getDeltaMovement().add(0, -1, 0));
		//this.handleWaterMovement();
		if(tickCount >= 5) {
			this.level.setBlockAndUpdate(this.blockPosition(), ModBlocks.pairBlox.get().defaultBlockState().setValue(PairBloxBlock.PAIR, getPair()));
			this.remove(false);
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("Pair", this.getPair());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.setPair(compound.getInt("Pair"));
	}

	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions entitySize) {
		return 0.0F;
	}

	public void setPair(int pair) {
		this.entityData.set(PAIR, pair);
		this.pair = pair;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (PAIR.equals(key)) {
			this.pair = this.getPairDataManager();
		}
	}

	public int getPairDataManager() {
		return this.entityData.get(PAIR);
	}

	public int getPair() {
		return this.pair;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
