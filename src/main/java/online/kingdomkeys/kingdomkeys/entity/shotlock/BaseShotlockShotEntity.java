package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

import java.util.Optional;
import java.util.UUID;

public class BaseShotlockShotEntity extends ThrowableProjectile{
	int maxTicks = 100;
	public float dmg;

	public BaseShotlockShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	private static final EntityDataAccessor<Float> INITIAL_YAW = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> INITIAL_PITCH = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.FLOAT);

	public BaseShotlockShotEntity(EntityType<? extends ThrowableProjectile> type, Level world, LivingEntity player, Entity target, double dmg) {
		super(type, player, world);
		this.dmg = (float)dmg;
		setTarget(target.getId());

		// Face the same way the caster is looking right away, instead of defaulting to yaw/pitch 0
		// (north, level) until it actually starts moving. Regular entity rotation only reliably syncs
		// to the client through the normal movement packets, which don't fire while the entity is just
		// sitting there with zero velocity (e.g. during a charge-up phase) - so a stationary entity's
		// initial yaw/pitch set server-side never actually reaches the client that way. Syncing it
		// through SynchedEntityData instead guarantees it always replicates, regardless of movement.
		this.entityData.set(INITIAL_YAW, player.getYRot());
		this.entityData.set(INITIAL_PITCH, player.getXRot());
		this.setYRot(player.getYRot());
		this.setXRot(player.getXRot());
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}

	public float getInitialYaw() {
		return this.entityData.get(INITIAL_YAW);
	}

	public float getInitialPitch() {
		return this.entityData.get(INITIAL_PITCH);
	}

	protected void reassertInitialRotationIfStationary() {
		if (getDeltaMovement().lengthSqr() < 1.0E-6) {
			float yaw = getInitialYaw();
			float pitch = getInitialPitch();
			this.setYRot(yaw);
			this.setXRot(pitch);
			this.yRotO = yaw;
			this.xRotO = pitch;
		}
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void remove(RemovalReason reason) {
		if(tickCount > 20) {
			super.remove(RemovalReason.KILLED);
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	private static final double SPIRAL_RADIUS = 0.12D;
	private static final double SPIRAL_SPEED = 0.5D; // radians per tick
	private Vec3 lastWobbleOffset = Vec3.ZERO;

	protected void applySpiralWobble() {
		Vec3 velocity = getDeltaMovement();
		if (velocity.lengthSqr() < 1E-6)
			return;

		Vec3 dir = velocity.normalize();
		Vec3 upRef = Math.abs(dir.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
		Vec3 side = dir.cross(upRef).normalize();
		Vec3 up = side.cross(dir).normalize();

		double phase = getId() * 0.7D; // per-shot offset so multiple shots don't spiral identically
		double angle = tickCount * SPIRAL_SPEED + phase;

		Vec3 newOffset = side.scale(Math.cos(angle) * SPIRAL_RADIUS).add(up.scale(Math.sin(angle) * SPIRAL_RADIUS));
		Vec3 delta = newOffset.subtract(lastWobbleOffset);
		this.setPos(getX() + delta.x, getY() + delta.y, getZ() + delta.z);
		lastWobbleOffset = newOffset;
	}

	@Override
	protected void onHit(HitResult pResult) {
		if(!level().isClientSide) {
			if(getOwner() != null && getOwner() instanceof Player owner) {
	    		PlayerData playerData = PlayerData.get(owner);
	    		if(playerData != null) {
	    			if(playerData.getNumberOfAbilitiesEquipped(ModAbilities.HP_GAIN) > 0) {
	    				owner.heal(playerData.getNumberOfAbilitiesEquipped(ModAbilities.HP_GAIN)*2);
	    			}
	    		}
	    	}
		}
	}
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putInt("TargetUUID", this.entityData.get(TARGET));
			compound.putInt("Color", this.entityData.get(COLOR));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGET, compound.getInt("TargetUUID"));
		this.entityData.set(COLOR, compound.getInt("Color"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.INT);

	private static final EntityDataAccessor<String> ELEMENT = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<ItemStack> VISUAL_ITEM = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.ITEM_STACK);

	public void setVisualItem(ItemStack stack) {
		this.entityData.set(VISUAL_ITEM, stack == null ? ItemStack.EMPTY : stack);
	}

	public ItemStack getVisualItem() {
		return this.entityData.get(VISUAL_ITEM);
	}

	public void setElement(ResourceKey<DamageType> element) {
		this.entityData.set(ELEMENT, element == null ? "" : element.location().toString());
	}

	public ResourceKey<DamageType> getElement() {
		String value = this.entityData.get(ELEMENT);
		if (value.isEmpty())
			return null;
		return ResourceKey.create(Registries.DAMAGE_TYPE, KingdomKeys.rl(value));
	}

	public DamageSource buildDamageSource(LivingEntity target) {
		ResourceKey<DamageType> element = getElement();
		if (element != null) {
			return KKDamageTypes.getElementalDamage(element, this, getOwner());
		}
		return target.damageSources().thrown(this, getOwner());
	}

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(TARGET));
	}

	public void setTarget(int i) {
		this.entityData.set(TARGET, i);
	}

	public int getColor() {
		return this.getEntityData().get(COLOR);
	}
	
	public void setColor(int color) {
		this.entityData.set(COLOR, color);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(TARGET, 0);
		pBuilder.define(COLOR, 0);
		pBuilder.define(ELEMENT, "");
		pBuilder.define(VISUAL_ITEM, ItemStack.EMPTY);
		pBuilder.define(INITIAL_YAW, 0F);
		pBuilder.define(INITIAL_PITCH, 0F);
	}
}
