package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeShotEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class GummiShipEntity extends KKVehicleEntity implements IEntityWithComplexSpawn {

	CompoundTag data;
	public GummiStructure structure;
	public ShipStats shipStats;

	public GummiShipEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	public record ShipStats(float speed, int weight, List<Vec3> firepower, List<Vec3> passengerSlots) {
		public float getEffectiveSpeed(){
            return speed() / (weight() * 0.05F);
        }
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(DATA, new CompoundTag());
	}

	public GummiShipEntity(Level world, GummiStructure gummiStruct) {
		this(ModEntities.TYPE_GUMMI_SHIP.get(), world);
		structure = gummiStruct;
		this.setData(structure.serializeNBT(level().registryAccess()));
		this.refreshDimensions();
	}

	int weaponCounter = 0;
	public void fire(Player player, boolean rightClick) {
		ThrowableProjectile blizzard = new LaserDomeShotEntity(player.level(), player, 10);
		player.level().addFreshEntity(blizzard);
		Vec3 weaponPos = shipStats.firepower.get(weaponCounter++);
		Vec3 posInShip = new Vec3(structure.getWidth()/2-weaponPos.x(), (structure.getHeight()/2F)+weaponPos.y()-structure.getHeight()/2, structure.getDepth()/2-weaponPos.z()).yRot(-this.getYRot() * 0.017453292F);
		Vec3 finalPos = new Vec3(posInShip.x+getX(),posInShip.y+getY(),posInShip.z+getZ());
		blizzard.setPos(finalPos);
		blizzard.shootFromRotation(this, player.getXRot(), player.getYRot(), 0, 1F, 0);
		level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1F, 1F);

		if(weaponCounter >= shipStats.firepower().size())
			weaponCounter = 0;
	}

	@Override
	protected int getMaxPassengers() {
		return getShipStats().passengerSlots.size(); //Passengers
	}

	float getSpeed() {
		return getShipStats().speed;
	}

	float getEffectiveSpeed(){
		return getShipStats().getEffectiveSpeed();
	}

	int getWeight() {
		return getShipStats().weight;
	}

	private ShipStats getShipStats(){
		if(shipStats == null){
			shipStats = Utils.getShipStats(structure);
		}
		return shipStats;
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
		int i = getPassengers().indexOf(entity); //return index of the entity in the big array
		double x = getShipStats().passengerSlots.get(i).x();
		double y = getShipStats().passengerSlots.get(i).y();
		double z = getShipStats().passengerSlots.get(i).z();
		return (new Vec3(structure.getWidth()/2-x, (structure.getHeight()/2F)+y-structure.getHeight()/2, structure.getDepth()/2-z)).yRot(-this.getYRot() * 0.017453292F);
		// return super.getPassengerAttachmentPoint(entity,dimensions,partialTick);
	}

	void controlBoat() {
		if (this.isVehicle()) {
			float f = 0.0F;
			if (this.inputLeft) {
				this.deltaRotation-=getEffectiveSpeed()*4;
			}

			if (this.inputRight) {
				this.deltaRotation+=getEffectiveSpeed()*4;
			}

			this.setYRot(this.getYRot() + this.deltaRotation);
			if (this.inputForward) {
				f += getEffectiveSpeed();
			}

			if (this.inputBackward) {
				f -= getEffectiveSpeed();
			}

			Vec3 motion = this.getDeltaMovement();

			if (this.inputUp) {
				motion = motion.add(0, getEffectiveSpeed(), 0);
			}
			if (this.inputDown) {
				motion = motion.add(0, -getEffectiveSpeed(), 0);
			}

			//this.setXRot(lerp(this.getXRot(), cameraX, 5));
			//this.setYRot(lerp(Mth.positiveModulo(this.getYRot(), 360), Mth.positiveModulo(cameraY, 360), 3));
			//this.getControllingPassenger().setYBodyRot(this.getYRot());
			//this.getControllingPassenger().

			this.setDeltaMovement(this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * f), motion.y(), Math.cos(this.getYRot() * 0.017453292F) * f));
		}
	}

	private float lerp(float current, float target, float delta) {
		if (Math.abs(target - current) < delta) {
			return target;
		} else {
			return current + Math.signum(target - current) * delta;
		}
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		if(passenger instanceof Player) //If it's a player allow them
			return super.canAddPassenger(passenger);
		// Otherwise only allow them if there's an empty slot after they mount
		return getPassengers().size() < getShipStats().passengerSlots().size() - 1;
	}


	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.scalable(Math.max(Utils.getRealGummiStructureSize(structure).getX(), Utils.getRealGummiStructureSize(structure).getZ()), Utils.getRealGummiStructureSize(structure).getY());
	}

	@Override
	public void tick() {
		super.tick();
		if (structure == null || structure.getBlocks().length == 0) {
			this.kill();
		} else {
			boolean empty = true;
			for (int x = 0; x < structure.getWidth(); x++) {
				for (int y = 0; y < structure.getHeight(); y++) {
					for (int z = 0; z < structure.getDepth(); z++) {
						if (structure.getBlocks()[x][y][z] != null) {
							empty = false;
						}
					}
				}
			}
			if (empty) {
				this.kill();
			} else {
				this.refreshDimensions();
			}
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (!this.level().isClientSide) {
			player.startRiding(this);
		}
		return InteractionResult.sidedSuccess(this.level().isClientSide);
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
	private static final EntityDataAccessor<CompoundTag> DATA = SynchedEntityData.defineId(GummiShipEntity.class, EntityDataSerializers.COMPOUND_TAG);

	public CompoundTag getData() {
		return data;
	}

	public void setData(CompoundTag struct) {
		this.entityData.set(DATA, struct);
		structure = new GummiStructure(level().registryAccess(), struct);
	}


	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (key.equals(DATA)) {
			CompoundTag tag = this.entityData.get(DATA);
			structure = new GummiStructure(level().registryAccess(), tag);
		}
	}


	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.put("data",structure.serializeNBT(this.level().registryAccess()));

	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setData(compound.getCompound("data"));

	}

	public CompoundTag getDataManager() {
		return this.entityData.get(DATA);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = structure.serializeNBT(level().registryAccess());
		buf.writeNbt(nbt);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		if (nbt != null) {
			structure = new GummiStructure(level().registryAccess(), nbt);
			this.setData(nbt);
		}
	}
}
