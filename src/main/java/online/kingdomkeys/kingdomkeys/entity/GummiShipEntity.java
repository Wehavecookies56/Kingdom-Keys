package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class GummiShipEntity extends Entity {// PigEntity {

	public final static int MAX_TICKS = 30;
	private String data;

	public GummiShipEntity(EntityType<? extends Entity> type, Level world) {
		super(ModEntities.TYPE_GUMMI_SHIP.get(), world);
	}

	public GummiShipEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_GUMMI_SHIP.get(), world);
	}

	public GummiShipEntity(Level world) {
		super(ModEntities.TYPE_GUMMI_SHIP.get(), world);
	}

	@Override
	public void tick() {
		super.tick();
	}

	public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                ;
    }
	private static final EntityDataAccessor<String> DATA = SynchedEntityData.defineId(GummiShipEntity.class, EntityDataSerializers.STRING);

	public String getData() {
		return data;
	}

	public void setData(String name) {
		this.entityData.set(DATA, name);
		this.data = name;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(DATA)) {
			this.data = this.getDataDataManager();
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("Data", this.getData());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setData(compound.getString("Data"));
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA, "");
	}

	public String getDataDataManager() {
		return this.entityData.get(DATA);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

/*	@Override
	public boolean canWalkOnFluid(Fluid fluid) {
		if (this.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.OAK_PLANKS) {
			if (this.getEquippedStack(EquipmentSlot.CHEST).getTag().getInt("type") == 0) {
				return FluidTags.WATER.contains(fluid);
			}
		}
		return false;
	}*/

	
	/*@Override
	public float getSaddledSpeed() {

		return 1;
	}*/

	/*@Override
	protected void registerGoals() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canBeRiddenInWater() {
		return true;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
	}

	@Override
	protected SoundEvent getFallSound(int distance) {
		return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
	}

	@Override
	protected SoundEvent getSplashSound() {
		return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
	}

	@Override
	protected int calculateFallDamage(float distance, float damageMultiplier) {
		return 0;
	}

	@Override
	public int getMaxFallHeight() {
		return 400;
	}

	@Override
	public boolean preventDespawn() {
		return true;
	}

	/*
	@Override
	protected boolean movesIndependently() {
		if (this.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.STICK) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canMoveVoluntarily() {
		if (this.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.STICK) {
			return true;
		}
		return false;
	}* /

	@Override
	public boolean canBePushed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canBeSteered() {
		return true;
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
		if (!player.getEntityWorld().isRemote && player.getHeldItem(hand) == ItemStack.EMPTY && hand == Hand.MAIN_HAND) {
			player.startRiding(this, true);
			return ActionResultType.PASS;
		}
		return ActionResultType.FAIL;
	}

	@Override
	public boolean isInvulnerable() {
		return !dead;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		if (damageSource != DamageSource.OUT_OF_WORLD) {
			return true;
		}
		return false;
	}

	
	@Override
	public void updatePassenger(Entity passenger) {
		int extra = 0;
		passenger.fallDistance = 0;
		if (this.getControllingPassenger() instanceof PlayerEntity) {
			if (passenger == this.getControllingPassenger()) {
				passenger.setPosition(this.getPosX(), this.getPosY() + 0.5 + extra, this.getPosZ());
			} else {
				Vector3d dir = this.getMotion().inverse();
				int vv = this.getPassengers().indexOf(passenger);
				passenger.setPosition(this.getControllingPassenger().getPosX() + dir.getX() * vv, this.getControllingPassenger().getPosY() + dir.getY() * vv, this.getControllingPassenger().getPosZ() + dir.getZ() * vv);
			}
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canCollide(Entity entity) {
		return true;
	}*/

}
