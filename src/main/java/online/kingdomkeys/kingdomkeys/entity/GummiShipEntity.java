package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
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

	public record ShipStats(float speed, int weight, List<Vec3> passengerSlots) {
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
		return (new Vec3(structure.getWidth()/2-x, (structure.getHeight()/2F)+y-3F, structure.getDepth()/2-z)).yRot(-this.getYRot() * 0.017453292F);
		// return super.getPassengerAttachmentPoint(entity,dimensions,partialTick);
	}

	void controlBoat() {
		if (this.isVehicle()) {
			float f = 0.0F;
			if (this.inputLeft) {
				this.deltaRotation-=getEffectiveSpeed()*3;
			}

			if (this.inputRight) {
				this.deltaRotation+=getEffectiveSpeed()*3;
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

			this.setDeltaMovement(this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * f), motion.y(), Math.cos(this.getYRot() * 0.017453292F) * f));
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
		return EntityDimensions.scalable(Math.max(getRealDimensions().getX(), getRealDimensions().getZ()), getRealDimensions().getY());
	}

	public Vec3i getRealDimensions(){
		int sizeX = structure.getBlocks().length;
		int sizeY = structure.getBlocks()[0].length;
		int sizeZ = structure.getBlocks()[0][0].length;

		int minX = sizeX, maxX = -1;
		int minY = sizeY, maxY = -1;
		int minZ = sizeZ, maxZ = -1;

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				for (int z = 0; z < sizeZ; z++) {
					BlockState state = structure.getBlocks()[x][y][z];
					if (state != null && !state.isAir()) {
						if (x < minX) minX = x;
						if (x > maxX) maxX = x;
						if (y < minY) minY = y;
						if (y > maxY) maxY = y;
						if (z < minZ) minZ = z;
						if (z > maxZ) maxZ = z;
					}
				}
			}
		}

		if (maxX == -1) {
			return new Vec3i(0, 0, 0);
		}

		int realWidth  = (maxX - minX) + 1;
		int realHeight = (maxY - minY) + 1;
		int realDepth  = (maxZ - minZ) + 1;

		return new Vec3i(realWidth, realHeight, realDepth);
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
