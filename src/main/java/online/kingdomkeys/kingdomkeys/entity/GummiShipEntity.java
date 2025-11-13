package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.block.GummiWeaponBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.datagen.init.BlockTagsGen;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class GummiShipEntity extends KKVehicleEntity implements IEntityWithComplexSpawn {

	CompoundTag data;
    int fuel;
	public GummiStructure structure;
	public ShipStats shipStats;

    public GummiShipEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}



    public record ShipStats(float speed, int weight, int armour, List<Vec3> firepower, List<Vec3> passengerSlots) {
		public float getEffectiveSpeed(){
            return speed() / (weight() * 0.05F);
        }
	}

	public GummiShipEntity(Level world, GummiStructure gummiStruct) {
		this(ModEntities.TYPE_GUMMI_SHIP.get(), world);
		structure = gummiStruct;
		this.setData(structure.serializeNBT(level().registryAccess()));
		this.refreshDimensions();
	}

	int weaponCounter = 0;
	public void fire(Player player, boolean rightClick) {
        if(getFuel() > 0) {
            boolean xEven = Utils.isStructureEven(structure)[0];
            boolean zEven = Utils.isStructureEven(structure)[1];

            Vec3 weaponPos = shipStats.firepower.get(weaponCounter++);
            BlockState weapon = structure.getBlocks()[(int)weaponPos.x][(int)weaponPos.y][(int)weaponPos.z];

            Vec3 posInShip = new Vec3(structure.getWidth()/2-weaponPos.x() + (xEven ? -0.5F: 0), (structure.getHeight()/2F)+weaponPos.y()-structure.getHeight()/2, structure.getDepth()/2-weaponPos.z()+ (zEven ? 0F: 0.5F)+0.5F).yRot(-this.getYRot() * 0.017453292F);
            Vec3 finalPos = new Vec3(posInShip.x + getX(),posInShip.y + getY(),posInShip.z + getZ());

            //TODO change for weapons shot() method
            if(weapon.getBlock() instanceof GummiWeaponBlock wpn){
                wpn.shoot(player,this,finalPos);
            }

            if (weaponCounter >= shipStats.firepower().size())
                weaponCounter = 0;
        }
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide && !this.isRemoved()) {
			if (this.isInvulnerableTo(source)) {
				return false;
			} else {
				if(this.getPassengers().contains(source.getEntity())){
					return false;
				}
				if(source.getEntity() instanceof Player player && structure != null){
					if(player.isCrouching() && structure.getOwnerID().equals(player.getUUID()) && player.getMainHandItem().getItem() == ModItems.gummiPhone.get()){
						ItemStack stack = player.getMainHandItem();
                        if(stack.has(ModComponents.GUMMI_STRUCTURE)){
                            player.displayClientMessage(Component.translatable("There's already a gummi ship stored in your gummi phone"), true);
                        } else {
                            stack.set(ModComponents.GUMMI_STRUCTURE, structure);
                            stack.set(ModComponents.GUMMI_DAMAGE, getDamage());
                            stack.set(ModComponents.GUMMI_FUEL, getFuel());

                            player.displayClientMessage(Component.translatable("Stored gummi ship in your gummi phone"), true);
                            ((ServerLevel) level()).sendParticles(ParticleTypes.FIREWORK, this.getX(), this.getY() + 1, this.getZ(), Utils.getRealGummiStructureSize(structure).getX() * Utils.getRealGummiStructureSize(structure).getY() * Utils.getRealGummiStructureSize(structure).getZ(), 0, 0, 0, 0.2);
                            this.kill();
                        }
						return false;
					}
				}
				this.setHurtDir(-this.getHurtDir());
				this.setHurtTime(10);
				this.markHurt();
				this.setDamage(this.getDamage() + amount);
				this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
				boolean flag = source.getEntity() instanceof Player player && player.getAbilities().instabuild && player.isCrouching();
				if (flag) { //If creative player hits a ship
					this.destroy(source);
				}
				//If accumulated damage > defense
				if (this.getDamage() > getArmour()) {//&& !this.shouldSourceDestroy(source)) {
					this.destroy(source);
				}

				return true;
			}
		} else {
			return true;
		}
	}

	protected Item[] getDropItems() {
		ArrayList<Item> items = new ArrayList<>();
		int sizeX = structure.getBlocks().length;
		int sizeY = structure.getBlocks()[0].length;
		int sizeZ = structure.getBlocks()[0][0].length;

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				for (int z = 0; z < sizeZ; z++) {
					BlockState state = structure.getBlocks()[x][y][z];
					if (state != null && !state.isAir()) {
						Block block = state.getBlock();
						if (block.builtInRegistryHolder().is(BlockTagsGen.GUMMI_DROPS)) {
							items.add(block.asItem());
						} else {
							int number = level().random.nextInt(100);
							if (number < ModConfigs.gummiBlocksDropPercent) {
								items.add(block.asItem());
							}
						}
					}
				}
			}
		}

		return items.toArray(new Item[0]);
	}

	@Override
	protected void destroy(DamageSource source) {
		//Vec3i gummiSize = Utils.getRealGummiStructureSize(structure);
		//int size = Math.max(Math.max(gummiSize.getX(), gummiSize.getY()), gummiSize.getZ());
		if(structure.containsBlock(Blocks.PISTON)){
			float size = structure.getBlockCount(Blocks.PISTON) / 10F;
			if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
				level().explode(this,this.getX(),this.getY(),this.getZ(), size, Level.ExplosionInteraction.BLOCK);
			} else {
				level().explode(this,this.getX(),this.getY(),this.getZ(), size, Level.ExplosionInteraction.NONE);
			}
		}


		destroy(this.getDropItems());
	}

	public void destroy(Item[] dropItems) {
		this.kill();
		if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			Vec3i gummiSize = Utils.getRealGummiStructureSize(structure);
			int scatterRadius = Math.max(Math.max(gummiSize.getX(), gummiSize.getY()), gummiSize.getZ());
			for(Item dropItem : dropItems) {
				ItemStack itemstack = new ItemStack(dropItem);
				itemstack.set(DataComponents.CUSTOM_NAME, this.getCustomName());

				int randomX = level().random.nextInt(scatterRadius*2)-scatterRadius;
				int randomY = level().random.nextInt(scatterRadius*2)-scatterRadius;
				int randomZ = level().random.nextInt(scatterRadius*2)-scatterRadius;

				ItemEntity itementity = new ItemEntity(this.level(), this.getX()+randomX, this.getY()+randomY, this.getZ()+randomZ, itemstack);
				itementity.setDefaultPickUpDelay();
				if (this.captureDrops() != null) {
					this.captureDrops().add(itementity);
				} else {
					this.level().addFreshEntity(itementity);
				}

				//this.spawnAtLocation(itemstack);
			}
		}

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

	float getArmour(){
		return getShipStats().armour;
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
        boolean xEven = Utils.isStructureEven(structure)[0];
        boolean zEven = Utils.isStructureEven(structure)[1];
		return (new Vec3(structure.getWidth()/2-x + (xEven ? -0.5F: 0), (structure.getHeight()/2F)+y-structure.getHeight()/2, structure.getDepth()/2-z + (zEven ? 0.5F: 0))).yRot(-this.getYRot() * 0.017453292F);
		// return super.getPassengerAttachmentPoint(entity,dimensions,partialTick);
	}

	public float currentSpeed = 0F;
	private final float acceleration = 0.02F;
	private final float deceleration = 0.02F;
	private final float brake = 0.5F;

	public float currentRotationSpeed = 0F;
	private final float rotationAcceleration = 0.08F;
	private final float rotationDeceleration = 0.08F;

	public float currentVerticalSpeed = 0F;
	private final float ascendAcceleration = 0.04F;
	private final float descendAcceleration = 0.04F;

	@Override
	void controlBoat() {
        if(getFuel() <= 0){
            inputLeft = inputRight = inputUp = inputForward = inputBackward = false;
            inputDown = true;
        }

		if (this.isVehicle()) {
			//Forward / Backwards
			float targetSpeed = 0F;
			if (this.inputForward)
				targetSpeed = getEffectiveSpeed();
			else if (this.inputBackward)
				targetSpeed = -getEffectiveSpeed()/2F;

			float delta = targetSpeed - currentSpeed;

			if(targetSpeed == 0){
				currentSpeed += delta * brake;
				if (currentSpeed < targetSpeed)
					currentSpeed = targetSpeed;
			} else {
				if (delta > 0) {
					// Forward
                    if(currentSpeed < shipStats.speed()) { //Top cap so it doesn't go faster than intended
                        currentSpeed += delta * acceleration;
                    } else {
                        currentSpeed = shipStats.speed();
                    }
					if (currentSpeed > targetSpeed)
						currentSpeed = targetSpeed;
                } else {
					// Backwards
                    if(currentSpeed > -shipStats.speed()) {
                        currentSpeed += delta * deceleration;
                    } else {
                        currentSpeed = -shipStats.speed();
                    }
					if (currentSpeed < targetSpeed)
						currentSpeed = targetSpeed;
				}
			}

			// Left / Right
			float targetRotation = 0F;

			if (this.inputLeft)
				targetRotation = -getEffectiveSpeed() * 5; //TODO change blocks for wings maybe?
			else if (this.inputRight)
				targetRotation = getEffectiveSpeed() * 5;

			float rotationDelta = targetRotation - currentRotationSpeed;
			if(targetRotation == 0){
				currentRotationSpeed += rotationDelta * brake;
				if (Math.abs(currentRotationSpeed) < targetRotation)
					currentRotationSpeed = targetRotation;

			} else {
				if (rotationDelta > 0) {
					currentRotationSpeed += rotationDelta * rotationAcceleration;
					if (currentRotationSpeed > targetRotation)
						currentRotationSpeed = targetRotation;
				} else {
					currentRotationSpeed += rotationDelta * rotationDeceleration;
					if (currentRotationSpeed < targetRotation)
						currentRotationSpeed = targetRotation;
				}
			}
			this.deltaRotation = currentRotationSpeed;
			this.setYRot(this.getYRot() + this.deltaRotation);

			// UP / Down
			float targetVertical = 0;

			if (this.inputUp)
				targetVertical = getEffectiveSpeed();
			else if (this.inputDown)
				targetVertical = -getEffectiveSpeed()*2;

			float verticalDelta = targetVertical - currentVerticalSpeed;

			if(targetVertical == 0){
				currentVerticalSpeed += verticalDelta * brake;
				if (Math.abs(currentVerticalSpeed) < targetVertical)
					currentVerticalSpeed = targetVertical;
			} else {
				if (verticalDelta > 0) {
					// Forward
					currentVerticalSpeed += verticalDelta * ascendAcceleration;
					if (currentVerticalSpeed > targetVertical)
						currentVerticalSpeed = targetVertical;
				} else {
					// Backwards
					currentVerticalSpeed += verticalDelta * descendAcceleration;
					if (currentVerticalSpeed < targetVertical)
						currentVerticalSpeed = targetVertical;
				}
			}

			this.setDeltaMovement(this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * currentSpeed), currentVerticalSpeed,(Math.cos(this.getYRot() * 0.017453292F) * currentSpeed)));
            //move(MoverType.SELF,this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * currentSpeed), currentVerticalSpeed,(Math.cos(this.getYRot() * 0.017453292F) * currentSpeed)));
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
        //setFuel(200);
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

        if(!level().isClientSide && this.shipStats != null) {
            int fuelConsumption = (int)(shipStats.speed * 0.2F);

            if(getYRot() != prevRot){ //If rotates remove half of what moving takes
                remFuel((int) Math.max(fuelConsumption * 0.3F,1));
            }
            boolean moved = this.position().distanceToSqr(prevX, prevY, prevZ) > 0.0001D;
            //If moves take as much fuel as engine power the ship has
            if (moved && !getPassengers().isEmpty() && getFuel() > 0) {
                if(getY() < prevY){//If it's landing use 70% of the fuel it should
                    remFuel((int) Math.max(fuelConsumption * 0.7F,1));
                } else if(getY() > prevY){//If it's taking off use 130% of the fuel it should
                    remFuel((int) Math.max(fuelConsumption * 1.3F,1));
                } else {
                    remFuel(fuelConsumption);
                }

            }

            prevX = getX();
            prevY = getY();
            prevZ = getZ();
            prevRot = getYRot();
        }
	}
    private double prevX, prevY, prevZ, prevRot;

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
    private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(GummiShipEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA, new CompoundTag());
        pBuilder.define(FUEL, 0);
    }

	public CompoundTag getData() {
		return data;
	}

	public void setData(CompoundTag struct) {
		this.entityData.set(DATA, struct);
		structure = new GummiStructure(level().registryAccess(), struct);
	}

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.entityData.set(FUEL, fuel);
        this.fuel = fuel;
    }

    public void addFuel(int fuel) {
        setFuel(Math.min(getFuel() + fuel, getMaxFuel()));
    }

    public void remFuel(int fuel) {
        setFuel(Math.max(getFuel() - fuel,0));
    }

    public int getMaxFuel(){
        return Utils.getFEStatsPerLevel(getShipLevel())[0]/2;
    }

    public int getShipLevel(){
        return ((structure.getWidth() - 5) / 2);
    }

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (key.equals(DATA)) {
			CompoundTag tag = this.entityData.get(DATA);
			structure = new GummiStructure(level().registryAccess(), tag);
		}
        if (key.equals(FUEL)) {
            this.fuel = this.entityData.get(FUEL);
        }
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.put("data",structure.serializeNBT(this.level().registryAccess()));
        compound.putInt("fuel", fuel);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setData(compound.getCompound("data"));
        this.setFuel(compound.getInt("fuel"));
	}

	public CompoundTag getDataManager() {
		return this.entityData.get(DATA);
	}

    public int getFuelManager() {
        return this.entityData.get(FUEL);
    }

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = structure.serializeNBT(level().registryAccess());
		buf.writeNbt(nbt);
        buf.writeInt(fuel);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		if (nbt != null) {
			structure = new GummiStructure(level().registryAccess(), nbt);
			this.setData(nbt);
		}
        this.setFuel(buf.readInt());
    }
}
