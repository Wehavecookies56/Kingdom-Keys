package online.kingdomkeys.kingdomkeys.entity;

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
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiPlacementType;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiWeaponBlock;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.datagen.init.BlockTagsGen;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GummiShipEntity extends KKVehicleEntity implements IEntityWithComplexSpawn {

	CompoundTag data;
	int fuel;
	public GummiStructure structure;
	public ShipStats shipStats;

	public GummiShipEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	public record ShipStats(float speed, int weight, int armour, List<Vec3> firepower, HashMap<GummiWeaponBlock.ShotType,Integer> impact, List<Vec3> passengerSlots, int mobility) {
		public float getEffectiveSpeed(){
			return speed() / (weight() * 0.5F);
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
		if (getFuel() <= 0)
			return;

		boolean xEven = Utils.isStructureEven(structure)[0];
		boolean zEven = Utils.isStructureEven(structure)[1];

		Vec3 weaponPos = shipStats.firepower.get(weaponCounter);
		BlockState weapon = structure.getBlocks()[(int) weaponPos.x][(int) weaponPos.y][(int) weaponPos.z];

		Quarter quarter = weapon.getValue(GummiWeaponBlock.QUARTER);

		float xOff = 0, zOff = 0;
		if(weapon.getBlock() instanceof GummiWeaponBlock mb && mb.isMultiBlock()){
			xOff = mb.getOffsetToCannon()[0];
			zOff = mb.getOffsetToCannon()[2];
		}

		if(quarter == Quarter.TOP){
			xOff *= -1;
			zOff *= -1;
		}

		Vec3 posInShip = new Vec3(structure.getWidth() / 2 - weaponPos.x() + (xEven ? -0.5F : 0)+xOff, (structure.getHeight() / 2F) + weaponPos.y() - structure.getHeight() / 2, structure.getDepth() / 2 - weaponPos.z() + (zEven ? 0F : 0.5F) + 0.5F + zOff)
				.xRot(-this.getXRot() * ((float) Math.PI / 180F))
				.yRot(-this.getYRot() * ((float) Math.PI / 180F));
		Vec3 weaponPosWorld = posInShip.add(getX(), getY(), getZ());
		Vec3 lookDir = player.getLookAngle();
		Vec3 eyePos = player.getEyePosition(1.0f);
		Vec3 targetPoint = eyePos.add(lookDir.scale(60)); //Higher the number the further away they converge
		Vec3 compensatedDir = targetPoint.subtract(weaponPosWorld).normalize();

		if (weapon.getBlock() instanceof GummiWeaponBlock wpn && getFuel() > wpn.getFuelPerShot()) {
			wpn.shoot(player, player.level(), this, weaponPosWorld, compensatedDir);
			weaponCounter++;
			if (weaponCounter >= shipStats.firepower().size())
				weaponCounter = 0;
		}
	}

	public void boost(Player player){
		// Seems entity push is not needed in server
		if(shipStats.impact() == null || shipStats.impact().isEmpty()){
			return;
		}
		int size = 0;
		if(shipStats.impact().containsKey(GummiWeaponBlock.ShotType.WATER)){
			size+=1;
		}
		if(shipStats.impact().containsKey(GummiWeaponBlock.ShotType.WATERA)){
			size+=2;
		}
		if(shipStats.impact().containsKey(GummiWeaponBlock.ShotType.WATERGA)){
			size+=4;
		}

		int power = 0;
		for(Map.Entry<GummiWeaponBlock.ShotType,Integer> e : shipStats.impact().entrySet()){
			power += e.getValue();
		}

		size *= 2; //Scale it for wider area

		GummiImpactEntity shot = new GummiImpactEntity(level(), player, power);
		level().addFreshEntity(shot);
		shot.setPos(position());
		shot.shootFromRotation(this, getXRot(), getYRot() - size, 0, 3F, 0);

		GummiImpactEntity shot2 = new GummiImpactEntity(level(), player, power);
		level().addFreshEntity(shot2);
		shot2.setPos(position());
		shot2.shootFromRotation(this, getXRot(), getYRot() + size, 0, 3F, 0);

		shot.setLinked(shot2);
		shot2.setLinked(shot);
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
							player.displayClientMessage(Component.translatable("kingdomkeys.gummi.phone.already_stored"), true);
						} else {
							stack.set(ModComponents.GUMMI_STRUCTURE, structure);
							stack.set(ModComponents.GUMMI_DAMAGE, getDamage());
							stack.set(ModComponents.GUMMI_FUEL, getFuel());

							player.displayClientMessage(Component.translatable("kingdomkeys.gummi.phone.stored"), true);
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
						if(block instanceof GummiBlockBase gummi) {
							if (gummi.getPlacementType() == GummiPlacementType.MULTIBLOCK2D) {
								if (!(state.getValue(GummiBlockBase.X) == 0 && state.getValue(GummiBlockBase.Z) == 0)) {
									continue; //skip fake blocks
								}
							} else if (gummi.getPlacementType() == GummiPlacementType.MULTIBLOCK3D) {
								if (!(state.getValue(GummiBlockBase.X) == 0 && state.getValue(GummiBlockBase.Y) == 0 && state.getValue(GummiBlockBase.Z) == 0)) {
									continue; //skip fake blocks
								}
							}
						}


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

	int isXEven = -1;
	int isZEven = -1;

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
		int i = getPassengers().indexOf(entity); //return index of the entity in the big array
		double x = getShipStats().passengerSlots.get(i).x();
		double y = getShipStats().passengerSlots.get(i).y();
		double z = getShipStats().passengerSlots.get(i).z();
		if(isXEven == -1 || isZEven == -1) {
			isXEven = Utils.isStructureEven(structure)[0] ? 1 : 0;
			isZEven = Utils.isStructureEven(structure)[1] ? 1 : 0;
		}
		boolean xEven = isXEven == 1;
		boolean zEven = isZEven == 1;

		return (new Vec3(structure.getWidth()/2-x + (xEven ? -0.5F: 0), (structure.getHeight()/2F)+y-structure.getHeight()/2, structure.getDepth()/2-z + (zEven ? 0.5F: 0)))
				.xRot(-this.getXRot() * 0.017453292F)
				.yRot(-this.getYRot() * 0.017453292F);
	}

	public float currentSpeed = 0F;
	private final float acceleration = 0.02F;
	private final float deceleration = 0.02F;
	private final float brake = 0.5F;

	//private boolean flightType3D = false;

	public float currentRotationSpeed = 0F;
	private final float legacyRotationAcceleration = 0.08F;
	private final float legacyRotationDeceleration = 0.08F;
	private final float baseYawTurnRate = 3F;

	public float currentVerticalSpeed = 0F;
	public float currentStrafeSpeed = 0F;
	private final float ascendAcceleration = 0.04F;
	private final float descendAcceleration = 0.04F;

	// How far up/down the ship is allowed to tilt to follow the pilot's look direction, and how fast
	// (degrees per tick) it turns to catch up to that target pitch.
	private final float maxShipPitch = 60F;
	private final float pitchTurnRate = 3F;

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

			if (isFlightType3D()) {
				float previousYRot = this.getYRot();
				float yawTurnRate = baseYawTurnRate * (getShipStats().mobility() * 0.05F);
				float yawDelta = Mth.wrapDegrees(this.cameraY - this.getYRot());
				float yawStep = Mth.clamp(yawDelta, -yawTurnRate, yawTurnRate);
				this.setYRot(this.getYRot() + yawStep);
				this.currentRotationSpeed = yawStep;
				this.deltaRotation = this.getYRot() - previousYRot;

				float targetPitch = Mth.clamp(this.cameraX, -maxShipPitch, maxShipPitch);
				float pitchDelta = Mth.wrapDegrees(targetPitch - this.getXRot());
				float pitchStep = Mth.clamp(pitchDelta, -pitchTurnRate, pitchTurnRate);
				this.setXRot(this.getXRot() + pitchStep);
			} else {
				// ============ Original 2D key-steered flight ============
				// Left / Right
				float targetRotation = 0F;

				if (this.inputLeft)
					targetRotation = -getEffectiveSpeed() * 3 * (getShipStats().mobility()*0.05F);
				else if (this.inputRight)
					targetRotation = getEffectiveSpeed() * 3 * (getShipStats().mobility()*0.05F);

				float rotationDelta = targetRotation - currentRotationSpeed;
				if(targetRotation == 0){
					currentRotationSpeed += rotationDelta * brake;
					if (Math.abs(currentRotationSpeed) < targetRotation)
						currentRotationSpeed = targetRotation;
				} else {
					if (rotationDelta > 0) {
						currentRotationSpeed += rotationDelta * legacyRotationAcceleration;
						if (currentRotationSpeed > targetRotation)
							currentRotationSpeed = targetRotation;
					} else {
						currentRotationSpeed += rotationDelta * legacyRotationDeceleration;
						if (currentRotationSpeed < targetRotation)
							currentRotationSpeed = targetRotation;
					}
				}
				this.deltaRotation = currentRotationSpeed;
				this.setYRot(this.getYRot() + this.deltaRotation);

				// Re-level the pitch back to 0 ("parking") instead of following the camera.
				float pitchDelta = Mth.wrapDegrees(0F - this.getXRot());
				float pitchStep = Mth.clamp(pitchDelta, -pitchTurnRate, pitchTurnRate);
				this.setXRot(this.getXRot() + pitchStep);
			}

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

			if (isFlightType3D()) {
				float targetStrafe = 0;
				if (this.inputLeft)
					targetStrafe = -getEffectiveSpeed();
				else if (this.inputRight)
					targetStrafe = getEffectiveSpeed();

				float strafeDelta = targetStrafe - currentStrafeSpeed;
				if (targetStrafe == 0) {
					currentStrafeSpeed += strafeDelta * brake;
					if (Math.abs(currentStrafeSpeed) < 0.001F) currentStrafeSpeed = 0;
				} else {
					if (strafeDelta > 0) {
						currentStrafeSpeed += strafeDelta * ascendAcceleration;
						if (currentStrafeSpeed > targetStrafe) currentStrafeSpeed = targetStrafe;
					} else {
						currentStrafeSpeed += strafeDelta * descendAcceleration;
						if (currentStrafeSpeed < targetStrafe) currentStrafeSpeed = targetStrafe;
					}
				}

				// Forward/backward follows the ship's look direction (yaw + pitch).
				// A/D strafe laterally with their own independent speed, not tied to currentSpeed.
				Vec3 lookDirection = this.calculateViewVector(this.getXRot(), this.getYRot());
				Vec3 strafeDirection = lookDirection.cross(new Vec3(0, 1, 0)).normalize();
				this.setDeltaMovement(this.getDeltaMovement()
						.add(lookDirection.scale(currentSpeed))
						.add(strafeDirection.scale(currentStrafeSpeed))
						.add(0, currentVerticalSpeed, 0));
			} else {
				currentStrafeSpeed = 0;
				this.setDeltaMovement(this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * currentSpeed), currentVerticalSpeed,(Math.cos(this.getYRot() * 0.017453292F) * currentSpeed)));
			}
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
			BlockState[][][] blocks = structure.getBlocks();
			// Stop scanning as soon as we find the first non-null block - we only need to know
			// whether the structure is empty or not, no need to keep checking every remaining cell
			// (this runs every tick, for every gummi ship, and ships can easily be 20x20x20+ cells).
			outer:
			for (int x = 0; x < structure.getWidth(); x++) {
				for (int y = 0; y < structure.getHeight(); y++) {
					for (int z = 0; z < structure.getDepth(); z++) {
						if (blocks[x][y][z] != null) {
							empty = false;
							break outer;
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
			int fuelConsumption = (int)(shipStats.speed * ModConfigs.fuelConsumeFactor);

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
	private static final EntityDataAccessor<Boolean> FLIGHT_TYPE_3D = SynchedEntityData.defineId(GummiShipEntity.class, EntityDataSerializers.BOOLEAN);

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(DATA, new CompoundTag());
		pBuilder.define(FUEL, 0);
		pBuilder.define(FLIGHT_TYPE_3D, false);
	}

	public CompoundTag getData() {
		return data;
	}

	public void setData(CompoundTag struct) {
		this.entityData.set(DATA, struct);
		structure = new GummiStructure(level().registryAccess(), struct);
	}

	public int getFuel() {
		return ModConfigs.SERVER.gummiShipFuelSystem.get() ? fuel : 100000;
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

	public boolean isFlightType3D() {
		return this.entityData.get(FLIGHT_TYPE_3D);
	}

	public void setFlightType3D(boolean flightType3D) {
		this.entityData.set(FLIGHT_TYPE_3D, flightType3D);
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
		compound.putBoolean("flightType3D", isFlightType3D());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setData(compound.getCompound("data"));
		this.setFuel(compound.getInt("fuel"));
		this.setFlightType3D(!compound.contains("flightType3D") || compound.getBoolean("flightType3D"));
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
		buf.writeBoolean(isFlightType3D());
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buf) {
		CompoundTag nbt = buf.readNbt();
		if (nbt != null) {
			structure = new GummiStructure(level().registryAccess(), nbt);
			this.setData(nbt);
		}
		this.setFuel(buf.readInt());
		this.setFlightType3D(buf.readBoolean());
	}

	@Override
	protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
		if (isFlightType3D()) {
			if (!this.hasPassenger(passenger)) return;
			Vec3 offset = this.getPassengerAttachmentPoint(passenger, passenger.getDimensions(Pose.SITTING), 1.0F);
			Vec3 worldPos = new Vec3(this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);
			callback.accept(passenger, worldPos.x, worldPos.y, worldPos.z);
		} else {
			super.positionRider(passenger, callback);
		}
	}

	@Override
	public void onPassengerTurned(Entity entityToUpdate) {
		if (!isFlightType3D()) {
			super.onPassengerTurned(entityToUpdate);
		}
	}
}