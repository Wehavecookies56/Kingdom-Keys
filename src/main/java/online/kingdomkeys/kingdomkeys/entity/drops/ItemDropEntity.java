package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public abstract class ItemDropEntity extends Entity {

	public int delayBeforeCanPickup;
	public int value;

	private Player closestPlayer;
	private static final EntityDataAccessor<Integer> VALUE = SynchedEntityData.defineId(ItemDropEntity.class, EntityDataSerializers.INT);
	private final MutableBlockPos cachedPos = new MutableBlockPos();

	public ItemDropEntity(EntityType<? extends Entity> type, Level worldIn, double x, double y, double z, int expValue) {
		this(type, worldIn);
		this.setPos(x, y, z);
		this.setYRot((float) (this.random.nextDouble() * 360.0D));
		this.setDeltaMovement((this.random.nextDouble() * 0.2D - 0.1D) * 2.0D, this.random.nextDouble() * 0.4D, (this.random.nextDouble() * 0.2D - 0.1D) * 2.0D);
		setValue(expValue);
		this.delayBeforeCanPickup = 20;
	}

	public ItemDropEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	public void tick() {
		if (tickCount > 2400) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		super.tick();

		if (this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();

		boolean inWater = this.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value());

		if (inWater) {
			applyFloatMotion();
		} else if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
		}

		if (tickCount % 10 == 0) {
			cachedPos.set(this.getX(), this.getY(), this.getZ());
			if (this.level().getFluidState(cachedPos).is(FluidTags.LAVA)) {
				this.setDeltaMovement((this.random.nextFloat() - this.random.nextFloat()) * 0.2F, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
				this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
			}
		}

		if (!this.level().noCollision(this.getBoundingBox())) {
			this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) * 0.5D, this.getZ());
		}

		double baseMaxDist = 8.0D;

		if (tickCount % 10 == 0) {
			if (this.closestPlayer == null || this.closestPlayer.isSpectator() || this.closestPlayer.distanceToSqr(this) > (baseMaxDist * baseMaxDist)) {
				this.closestPlayer = this.level().getNearestPlayer(this, baseMaxDist);
			}
		}

		if (this.closestPlayer != null) {
			PlayerData playerData = PlayerData.get(closestPlayer);
			if (playerData != null) {

				double maxDist = 8 + (playerData.getNumberOfAbilitiesEquipped(ModAbilities.TREASURE_MAGNET) * 2);
				double maxDistSqr = maxDist * maxDist;

				double dx = this.closestPlayer.getX() - this.getX();
				double dy = this.closestPlayer.getY() + this.closestPlayer.getEyeHeight() * 0.5D - this.getY();
				double dz = this.closestPlayer.getZ() - this.getZ();

				double distSqr = dx * dx + dy * dy + dz * dz;

				if (distSqr < maxDistSqr && distSqr > 0.0001D) {
					double dist = Math.sqrt(distSqr);
					double factor = (1.0D - dist / maxDist);
					factor = factor * factor * 0.1D;

					double inv = 1.0D / dist;

					this.setDeltaMovement(this.getDeltaMovement().add(dx * inv * factor, dy * inv * factor, dz * inv * factor));
				}
			}
		}

		this.move(MoverType.SELF, this.getDeltaMovement());

		if (this.onGround() && this.getDeltaMovement().lengthSqr() < 0.0001D) {
			this.setDeltaMovement(Vec3.ZERO);
			return;
		}

		float friction = 0.98F;

		if (this.onGround()) {
			cachedPos.set(this.getX(), this.getY() - 1.0D, this.getZ());
			friction = this.level().getBlockState(cachedPos).getFriction(this.level(), cachedPos, this) * 0.98F;
		}

		this.setDeltaMovement(this.getDeltaMovement().multiply(friction, 0.98D, friction));

		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
		}
	}

	private void applyFloatMotion() {
		Vec3 vec3d = this.getDeltaMovement();
		this.setDeltaMovement(vec3d.x * 0.99D, Math.min(vec3d.y + 5.0E-4D, 0.06D), vec3d.z * 0.99D);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.level().isClientSide || this.isRemoved())
			return false;

		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			this.markHurt();
			return false;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("value", getValue());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		setValue(compound.getInt("value"));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(VALUE)) {
			this.value = this.entityData.get(VALUE);
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int v) {
		this.entityData.set(VALUE, v);
		this.value = v;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(VALUE, 0);
	}

	@Override
	public void playerTouch(Player entityIn) {
		if (!this.level().isClientSide) {
			if (this.delayBeforeCanPickup == 0) {
				onPickup(entityIn);
				this.playSound(getPickupSound(), 1F, 1F);
				this.remove(RemovalReason.KILLED);
				PacketHandler.sendTo(new SCSyncPlayerData(entityIn), (ServerPlayer) entityIn);
			}
		}
	}

	abstract void onPickup(Player entityIn);

	abstract SoundEvent getPickupSound();

	@Override
	public boolean isAttackable() {
		return false;
	}
}