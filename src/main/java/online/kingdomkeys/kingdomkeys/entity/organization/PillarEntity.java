package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PillarEntity extends Entity {

	private static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(PillarEntity.class, EntityDataSerializers.BLOCK_STATE);
	private static final EntityDataAccessor<Float> PILLAR_HEIGHT = SynchedEntityData.defineId(PillarEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PILLAR_RADIUS = SynchedEntityData.defineId(PillarEntity.class, EntityDataSerializers.FLOAT);

	private int lifetimeTicks = 60;

	public PillarEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}

	public void setup(BlockState state, float height, float radius, int lifetimeTicks) {
		this.entityData.set(BLOCK_STATE, state);
		this.entityData.set(PILLAR_HEIGHT, height);
		this.entityData.set(PILLAR_RADIUS, radius);
		this.lifetimeTicks = lifetimeTicks;
	}

	public BlockState getPillarBlockState() {
		return this.entityData.get(BLOCK_STATE);
	}

	public float getPillarHeight() {
		return this.entityData.get(PILLAR_HEIGHT);
	}

	public float getPillarRadius() {
		return this.entityData.get(PILLAR_RADIUS);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide && this.tickCount > lifetimeTicks) {
			this.discard();
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(BLOCK_STATE, Blocks.STONE.defaultBlockState());
		builder.define(PILLAR_HEIGHT, 3F);
		builder.define(PILLAR_RADIUS, 0.5F);
	}

	@Override
	protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}
}
