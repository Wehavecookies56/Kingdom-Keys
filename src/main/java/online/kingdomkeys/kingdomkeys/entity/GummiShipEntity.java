package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class GummiShipEntity extends Entity {// PigEntity {

	public final static int MAX_TICKS = 30;
	private String data;

	public GummiShipEntity(EntityType<? extends Entity> type, World world) {
		super(ModEntities.TYPE_GUMMI_SHIP.get(), world);
	}

	public GummiShipEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_GUMMI_SHIP.get(), world);
	}

	public GummiShipEntity(World world) {
		super(ModEntities.TYPE_GUMMI_SHIP.get(), world);
	}

	@Override
	public void tick() {
		super.tick();
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 1.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 0.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.0D)
				.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0D)
                ;
    }
	private static final DataParameter<String> DATA = EntityDataManager.createKey(GummiShipEntity.class, DataSerializers.STRING);

	public String getData() {
		return data;
	}

	public void setData(String name) {
		this.dataManager.set(DATA, name);
		this.data = name;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(DATA)) {
			this.data = this.getDataDataManager();
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putString("Data", this.getData());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setData(compound.getString("Data"));
	}

	@Override
	protected void registerData() {
		this.dataManager.register(DATA, "");
	}

	public String getDataDataManager() {
		return this.dataManager.get(DATA);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
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
