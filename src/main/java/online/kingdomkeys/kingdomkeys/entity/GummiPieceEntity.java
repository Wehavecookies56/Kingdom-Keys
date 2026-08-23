package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GummiPieceEntity extends Entity {

	private static final EntityDataAccessor<BlockState> STATE = SynchedEntityData.defineId(GummiPieceEntity.class, EntityDataSerializers.BLOCK_STATE);
	private static final EntityDataAccessor<BlockPos> TARGET = SynchedEntityData.defineId(GummiPieceEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(GummiPieceEntity.class, EntityDataSerializers.INT);

	private static final double SPEED = 0.45;
	private static final int MIN_DURATION = 16;
	private static final double ARC_SHARE = 0.35, ARC_LIMIT = 3;
	public static final float START_SCALE = 0.1F;

	private Vec3 origin = Vec3.ZERO;

	public GummiPieceEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}

	public static GummiPieceEntity create(Level level, Vec3 from, BlockPos target, BlockState state) {
		GummiPieceEntity piece = new GummiPieceEntity(ModEntities.TYPE_GUMMI_PIECE.get(), level);
		Vec3 to = target.getCenter();

		piece.setPos(from);
		piece.origin = from;
		piece.entityData.set(STATE, state);
		piece.entityData.set(TARGET, target);
		piece.entityData.set(DURATION, Math.max(MIN_DURATION, (int) Math.ceil(from.distanceTo(to) / SPEED)));

		return piece;
	}

	public BlockState getState() {
		return entityData.get(STATE);
	}

	public BlockPos getTarget() {
		return entityData.get(TARGET);
	}

	public float getProgress(float partialTicks) {
		return Math.min(1F, (tickCount + partialTicks) / entityData.get(DURATION));
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide) {
			return;
		}

		Vec3 to = getTarget().getCenter();

		if (tickCount >= entityData.get(DURATION)) {
			setPos(to);
			land(to);
			return;
		}

		setPos(along(to, (double) tickCount / entityData.get(DURATION)));
	}

	private Vec3 along(Vec3 to, double t) {
		double eased = t * t * (3 - 2 * t);
		double arc = Math.min(ARC_LIMIT, origin.distanceTo(to) * ARC_SHARE) * Math.sin(Math.PI * t);

		return origin.lerp(to, eased).add(0, arc, 0);
	}

	private void land(Vec3 to) {
		BlockState state = getState();
		BlockPos target = getTarget();

		if (level().getBlockState(target).canBeReplaced()) {
			level().setBlockAndUpdate(target, state);
		} else {
			spawnAtLocation(state.getBlock());
		}

		discard();
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(STATE, Blocks.AIR.defaultBlockState());
		builder.define(TARGET, BlockPos.ZERO);
		builder.define(DURATION, 1);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		origin = new Vec3(compound.getDouble("OriginX"), compound.getDouble("OriginY"), compound.getDouble("OriginZ"));
		entityData.set(TARGET, NbtUtils.readBlockPos(compound, "Target").orElse(blockPosition()));
		entityData.set(DURATION, Math.max(1, compound.getInt("Duration")));

		if (compound.contains("State")) {
			entityData.set(STATE, NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK), compound.getCompound("State")));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putDouble("OriginX", origin.x);
		compound.putDouble("OriginY", origin.y);
		compound.putDouble("OriginZ", origin.z);
		compound.put("Target", NbtUtils.writeBlockPos(getTarget()));
		compound.putInt("Duration", entityData.get(DURATION));
		compound.put("State", NbtUtils.writeBlockState(getState()));
	}

	@Override
	public boolean isPickable() {
		return false;
	}
}
