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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;

import java.util.LinkedList;

public class GummiShipEntity extends KKVehicleEntity implements IEntityWithComplexSpawn {

	CompoundTag data;
	public GummiStructure structure;
	public ShipStats ShipStats;

	public GummiShipEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	public static class ShipStats{
        public float speed;
        public int weight;
        public List<Vec3> passengerSlots = new ArrayList<>();
    
		public ShipStats(float speed, int weight, LinkedList<Vec3> passengerSlots){
			this.speed = speed;
			this.weight = weight;
			this.passengerSlots = passengerSlots;

			public int getWeight(){
				return weight;
			}
			public void setWeight(int weight){
				this.weight = weight
			}

			public float getSpeed(){
				return speed;
			}
			public void setSpeed(float speed){
				this.speed = speed
			}

			public LinkedList<Vec3> getPassengers(){
				return passengers;
			}
			public void setPassengers(LinkedList<Vec3> passengers){
				this.passengers = passengers
			}
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
		return getShipStats().passengerSlots.size()+1; //Passengers + rider
	}

	float getSpeed() {
		return getShipStats().speed;
	}

	int getWeight() {
		return getShipStats().weight;
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
		double x=0, y=0, z=0;
		int i = getPassengers().indexOf(entity); //return index of the entity in the big array
		x = getShipStats().passengerSlots.get(i).x();
		y = getShipStats().passengerSlots.get(i).y();
		z = getShipStats().passengerSlots.get(i).z();
		return (new Vec3(structure.getWidth()/2-x, (structure.getHeight()/2)-y-0.5F, structure.getDepth()/2-z)).yRot(-this.getYRot() * 0.017453292F);
	}

	private ShipStats getShipStats() {
		if(shipStats == null){
			shipStats = new ShipStats(speed, weight, passengers)
		}
		float speed = 0;
		LinkedList<Vec3> passengers = new LinkedList<>();
		int weight = 0;

		int sizeX = structure.getBlocks().length;
		int sizeY = structure.getBlocks()[0].length;
		int sizeZ = structure.getBlocks()[0][0].length;

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				for (int z = 0; z < sizeZ; z++) {
					BlockState state = structure.getBlocks()[x][y][z];
					if (state != null && !state.isAir()) {
						weight++;//TODO make heavier blocks
						if(state.getBlock() instanceof SlabBlock){
							passengers.addFirst(new Vec3(x,y,z));
						}
						if(state.getBlock() instanceof StairBlock){
							passengers.add(new Vec3(x,y,z));
						}
						if(state.getBlock() == Blocks.OBSIDIAN){
							weight++;
						} else if(state.getBlock() == Blocks.PISTON){
							speed+=0.5F;
						} else if(state.getBlock() == Blocks.STICKY_PISTON){
							speed++;
						}

					}
				}
			}
		}

		shipStats.setWeight(weight);
		shipStats.setSpeed(speed);
		shipStats.setPassengers(passengers);
		return shipStats;
	}

	void controlBoat() {
		if (this.isVehicle()) {
			float f = 0.0F;
			if (this.inputLeft) {
				this.deltaRotation-=getSpeed() / (getWeight() * 0.02F);
			}

			if (this.inputRight) {
				this.deltaRotation+=getSpeed() / (getWeight() * 0.02F);
			}

			this.setYRot(this.getYRot() + this.deltaRotation);
			if (this.inputForward) {
				f += getSpeed() / (getWeight() * 0.05F);
			}

			if (this.inputBackward) {
				f -= getSpeed() / (getWeight() * 0.05F);
			}

			Vec3 motion = this.getDeltaMovement();

			if (this.inputUp) {
				motion = motion.add(0, getSpeed() / (getWeight() * 0.05F), 0);
			}
			if (this.inputDown) {
				motion = motion.add(0, -getSpeed() / (getWeight() * 0.05F), 0);
			}

			this.setDeltaMovement(this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * f), motion.y(), Math.cos(this.getYRot() * 0.017453292F) * f));
		}
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
