package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class LaserCircleCoreEntity extends ThrowableEntity {

	int maxTicks = 70;
	List<LaserDomeShotEntity> list = new ArrayList<LaserDomeShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	Set<Integer> usedIndexes = new HashSet<Integer>();
	float dmg;

	double dmgMult;
	float radius = 4;
	int space;
	int shotsPerTick;

	public LaserCircleCoreEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public LaserCircleCoreEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_LASER_CIRCLE.get(), world);
	}

	public LaserCircleCoreEntity(World world) {
		super(ModEntities.TYPE_LASER_CIRCLE.get(), world);
		this.preventEntitySpawning = true;
	}

	public LaserCircleCoreEntity(World world, PlayerEntity player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_LASER_CIRCLE.get(), player, world);
		setCaster(player.getUniqueID());
		setTarget(target.getUniqueID());
		this.dmg = dmg;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks || getCaster() == null) {
			this.remove();
		}

		this.dmgMult = ModConfigs.limitLaserCircleMult;

		// world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(),
		// getPosZ(), 1, 1, 0);
		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		double X = getPosX();
		double Y = getPosY();
		double Z = getPosZ();

		if (getCaster() != null) {
			if (ticksExisted >= 0 && ticksExisted <= 40 && ticksExisted % 2 == 0) {
				double x = X + (radius * Math.cos(Math.toRadians(ticksExisted * 9)));
				double z = Z + (radius * Math.sin(Math.toRadians(ticksExisted * 9)));
				LaserDomeShotEntity bullet = new LaserDomeShotEntity(world, getCaster(), dmg * dmgMult);
				bullet.setPosition(x, Y + 1, z);
				bullet.setMaxTicks(maxTicks);
				bullet.shoot(this.getPosX() - bullet.getPosX(), this.getPosY() - bullet.getPosY()+1, this.getPosZ() - bullet.getPosZ(), 0.001f, 0);
				world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 1F);
				list.add(bullet);
				world.addEntity(bullet);

				this.setMotion(0, 0, 0);
				this.velocityChanged = true;

				/*if(getTarget() != null) {
					this.setPosition(getTarget().getPosX(), getTarget().getPosY(), getTarget().getPosZ());
					updatePos(radius);
				}*/
			} else if (ticksExisted == 60) {
				updateList();//Get all entities in the radius
				Entity target = this;
				if(targetList.size() > 1) {
					targetList.remove(this);
				} //If there are more entities than the controller remove it and track a random of the left ones
				int targetIndex = rand.nextInt(targetList.size());
				target = targetList.get(targetIndex);
				
				for (LaserDomeShotEntity bullet : list) {
					if (target != null && target.isAlive() && getCaster() != null) {
						world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 1F);
						bullet.shoot(target.getPosX() - bullet.getPosX(), target.getPosY() - bullet.getPosY()+1, target.getPosZ() - bullet.getPosZ(), 1.5f, 0);
					}
				}
			}
		}

		super.tick();
	}

	private void updatePos(float r) {
		for(LaserDomeShotEntity shot : list) {
			double x = getPosX() + (r * Math.cos(Math.toRadians(shot.ticksExisted * 9)));
			double z = getPosZ() + (r * Math.sin(Math.toRadians(shot.ticksExisted * 9)));
			shot.setPosition(x, getPosY() + 1, z);
			shot.shoot(this.getPosX() - shot.getPosX(), this.getPosY() - shot.getPosY(), this.getPosZ() - shot.getPosZ(), 0.001f, 0);
		}
	}

	private void updateList() {
		List<Entity> tempList = world.getEntitiesWithinAABBExcludingEntity(getCaster(), getBoundingBox().grow(radius, radius, radius));
		Party casterParty = ModCapabilities.getWorld(world).getPartyFromMember(getCaster().getUniqueID());

		if (casterParty != null) {
			for (Party.Member m : casterParty.getMembers()) {
				tempList.remove(world.getPlayerByUuid(m.getUUID()));
			}
		} else {
			tempList.remove(getThrower());
		}

		targetList.clear();
		for (Entity t : tempList) {
			if (!(t instanceof LaserDomeShotEntity || t instanceof ItemDropEntity || t instanceof ItemEntity || t instanceof ExperienceOrbEntity)) {
				targetList.add(t);
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.dataManager.get(OWNER).get().toString());
			compound.putString("TargetUUID", this.dataManager.get(TARGET).get().toString());
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(LaserCircleCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(LaserCircleCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	public PlayerEntity getTarget() {
		return this.getDataManager().get(TARGET).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(TARGET).get()) : null;
	}

	public void setTarget(UUID uuid) {
		this.dataManager.set(TARGET, Optional.of(uuid));
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGET, Optional.of(new UUID(0L, 0L)));
	}
}
