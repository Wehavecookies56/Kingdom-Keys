package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public abstract class ItemDropEntity extends Entity {
	public int delayBeforeCanPickup;
	public int value;
	private Player closestPlayer;

	public ItemDropEntity(EntityType<? extends Entity> type, Level worldIn, double x, double y, double z, int expValue) {
		this(type, worldIn);
		this.setPos(x, y, z);
		this.setYRot((float) (this.random.nextDouble() * 360.0D));
		this.setDeltaMovement((this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D);
		this.value = expValue;
		this.delayBeforeCanPickup = 20;
	}

	public ItemDropEntity(EntityType<ItemDropEntity> type, PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(type, world);
	}
	
	public ItemDropEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}
	
	protected boolean isMovementNoisy() {
		return false;
	}

	protected void defineSynchedData() {
	}

	public void tick() {
		super.tick();
		if (this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		if (this.isEyeInFluid(FluidTags.WATER)) {
			this.applyFloatMotion();
		} else if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
		}
		//TODO fix the vec3 to Vec3i
		if (this.level.getFluidState(new BlockPos((int)this.position().x, (int)this.position().y, (int)this.position().z)).is(FluidTags.LAVA)) {
			this.setDeltaMovement(((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
			this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
		}

		if (!this.level.noCollision(this.getBoundingBox())) {
			this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
		}

		double maxDist = 8.0D;
		if (this.closestPlayer == null || this.closestPlayer.distanceToSqr(this) > Math.pow(maxDist,2)) {
			this.closestPlayer = this.level.getNearestPlayer(this, maxDist);
		}

		if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
			this.closestPlayer = null;
		}

		if (this.closestPlayer != null) {
			Vec3 vec3d = new Vec3(this.closestPlayer.getX() - this.getX(), this.closestPlayer.getY() + (double) this.closestPlayer.getEyeHeight() / 2.0D - this.getY(), this.closestPlayer.getZ() - this.getZ());
			double d1 = vec3d.lengthSqr();
			if (d1 < Math.pow(maxDist,2)) {
				double d2 = 1.0D - Math.sqrt(d1) / maxDist;
				this.setDeltaMovement(this.getDeltaMovement().add(vec3d.normalize().scale(d2 * d2 * 0.1D)));
			}
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		float f = 0.98F;
		if (this.onGround) {
			BlockPos pos = new BlockPos(((int) this.getX()), (int)(this.getY() - 1.0D), (int)this.getZ());
			f = this.level.getBlockState(pos).getFriction(this.level, pos, this) * 0.98F;
		}

		this.setDeltaMovement(this.getDeltaMovement().multiply((double) f, 0.98D, (double) f));
		if (this.onGround) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
		}

	}

	private void applyFloatMotion() {
		Vec3 vec3d = this.getDeltaMovement();
		this.setDeltaMovement(vec3d.x * (double) 0.99F, Math.min(vec3d.y + (double) 5.0E-4F, (double) 0.06F), vec3d.z * (double) 0.99F);
	}

	protected void dealFireDamage(int amount) {
		this.hurt(this.damageSources().onFire(), (float) amount);
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean hurt(DamageSource source, float amount) {
		if (this.level.isClientSide || this.isRemoved())
			return false; // Forge: Fixes MC-53850
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			this.markHurt();
			return false;
		}
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("Value", this.value);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditionalSaveData(CompoundTag compound) {
		this.value = compound.getInt("Value");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void playerTouch(Player entityIn) {
		if (!this.level.isClientSide) {
			if (this.delayBeforeCanPickup == 0) {
				onPickup(entityIn);
				this.playSound(getPickupSound(), 1F, 1F);
				this.remove(RemovalReason.KILLED);
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(entityIn)), (ServerPlayer)entityIn);
			}

		}
	}

	abstract void onPickup(Player entityIn);
	abstract SoundEvent getPickupSound();

	/**
	 * Returns the XP value of this XP orb.
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Returns a number from 1 to 10 based on how much XP this orb is worth. This is
	 * used by RenderXPOrb to determine what texture to use.
	 */
	@OnlyIn(Dist.CLIENT)
	public int getTextureByXP() {
		if (this.value >= 2477) {
			return 10;
		} else if (this.value >= 1237) {
			return 9;
		} else if (this.value >= 617) {
			return 8;
		} else if (this.value >= 307) {
			return 7;
		} else if (this.value >= 149) {
			return 6;
		} else if (this.value >= 73) {
			return 5;
		} else if (this.value >= 37) {
			return 4;
		} else if (this.value >= 17) {
			return 3;
		} else if (this.value >= 7) {
			return 2;
		} else {
			return this.value >= 3 ? 1 : 0;
		}
	}

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	public boolean isAttackable() {
		return false;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
	}

}