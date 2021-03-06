package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public abstract class ItemDropEntity extends Entity {
	public int delayBeforeCanPickup;
	public int value;
	private PlayerEntity closestPlayer;

	public ItemDropEntity(EntityType<? extends Entity> type, World worldIn, double x, double y, double z, int expValue) {
		this(type, worldIn);
		this.setPosition(x, y, z);
		this.rotationYaw = (float) (this.rand.nextDouble() * 360.0D);
		this.setMotion((this.rand.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D, this.rand.nextDouble() * 0.2D * 2.0D, (this.rand.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D);
		this.value = expValue;
		this.delayBeforeCanPickup = 20;
	}

	public ItemDropEntity(EntityType<ItemDropEntity> type, FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(type, world);
	}
	
	public ItemDropEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	
	protected boolean canTriggerWalking() {
		return false;
	}

	protected void registerData() {
	}

	public void tick() {
		super.tick();
		if (this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
		if (this.areEyesInFluid(FluidTags.WATER)) {
			this.applyFloatMotion();
		} else if (!this.hasNoGravity()) {
			this.setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));
		}

		if (this.world.getFluidState(new BlockPos(this.getPositionVec())).isTagged(FluidTags.LAVA)) {
			this.setMotion((double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F), (double) 0.2F, (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
			this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		if (!this.world.hasNoCollisions(this.getBoundingBox())) {
			this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());
		}

		double maxDist = 8.0D;
		if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > Math.pow(maxDist,2)) {
			this.closestPlayer = this.world.getClosestPlayer(this, maxDist);
		}

		if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
			this.closestPlayer = null;
		}

		if (this.closestPlayer != null) {
			Vector3d vec3d = new Vector3d(this.closestPlayer.getPosX() - this.getPosX(), this.closestPlayer.getPosY() + (double) this.closestPlayer.getEyeHeight() / 2.0D - this.getPosY(), this.closestPlayer.getPosZ() - this.getPosZ());
			double d1 = vec3d.lengthSquared();
			if (d1 < Math.pow(maxDist,2)) {
				double d2 = 1.0D - Math.sqrt(d1) / maxDist;
				this.setMotion(this.getMotion().add(vec3d.normalize().scale(d2 * d2 * 0.1D)));
			}
		}

		this.move(MoverType.SELF, this.getMotion());
		float f = 0.98F;
		if (this.onGround) {
			BlockPos pos = new BlockPos(this.getPosX(), this.getPosY() - 1.0D, this.getPosZ());
			f = this.world.getBlockState(pos).getSlipperiness(this.world, pos, this) * 0.98F;
		}

		this.setMotion(this.getMotion().mul((double) f, 0.98D, (double) f));
		if (this.onGround) {
			this.setMotion(this.getMotion().mul(1.0D, -0.9D, 1.0D));
		}

	}

	private void applyFloatMotion() {
		Vector3d vec3d = this.getMotion();
		this.setMotion(vec3d.x * (double) 0.99F, Math.min(vec3d.y + (double) 5.0E-4F, (double) 0.06F), vec3d.z * (double) 0.99F);
	}

	protected void dealFireDamage(int amount) {
		this.attackEntityFrom(DamageSource.IN_FIRE, (float) amount);
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.world.isRemote || this.removed)
			return false; // Forge: Fixes MC-53850
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			this.markVelocityChanged();
			return false;
		}
	}

	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("Value", this.value);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound) {
		this.value = compound.getInt("Value");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(PlayerEntity entityIn) {
		if (!this.world.isRemote) {
			if (this.delayBeforeCanPickup == 0) {
				onPickup(entityIn);
				this.playSound(getPickupSound(), 1F, 1F);
				this.remove();
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(entityIn)), (ServerPlayerEntity)entityIn);
			}

		}
	}

	abstract void onPickup(PlayerEntity entityIn);
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
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}