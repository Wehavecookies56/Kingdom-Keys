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

public class LaserDomeCoreEntity extends ThrowableEntity {

	int maxTicks = 240;
	List<LaserDomeShotEntity> list = new ArrayList<LaserDomeShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	Set<Integer> usedIndexes = new HashSet<Integer>();
	float dmg;

	double dmgMult;
	float radius = 15;
	int space = 12;
	int shotsPerTick = 3;

	public LaserDomeCoreEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public LaserDomeCoreEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_LASER_DOME.get(), world);
	}

	public LaserDomeCoreEntity(World world) {
		super(ModEntities.TYPE_LASER_DOME.get(), world);
		this.preventEntitySpawning = true;
	}

	public LaserDomeCoreEntity(World world, PlayerEntity player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_LASER_DOME.get(), player, world);
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

		this.dmgMult = ModConfigs.limitLaserDomeMult;

		// world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(),
		// getPosZ(), 1, 1, 0);
		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		double X = getPosX();
		double Y = getPosY();
		double Z = getPosZ();

		if (getCaster() != null) {
			if (ticksExisted >= 0 && ticksExisted < 20) {
				double t = ticksExisted * 5;
				for (int s = 1; s < 360; s += space) {
					double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = Y + (radius * Math.cos(Math.toRadians(t)));
					LaserDomeShotEntity bullet = new LaserDomeShotEntity(world, getCaster(), dmg * dmgMult);
					bullet.setPosition(x, y, z);
					bullet.setMaxTicks(maxTicks - 20);
					bullet.shoot(this.getPosX() - bullet.getPosX(), this.getPosY() - bullet.getPosY(), this.getPosZ() - bullet.getPosZ(), 0.001f, 0);
					list.add(bullet);
					world.addEntity(bullet);
				}

				this.setMotion(0, 0, 0);
				this.velocityChanged = true;

			} else if (ticksExisted == 40) { // Get all targets right before starting to shoot
				updateList();

			} else if (ticksExisted > 40 && !targetList.isEmpty()) {
				if (ticksExisted == 80) {
					updateList();
				}

				for (int i = 0; i < shotsPerTick; i++) {
					int num;
					do {
						num = rand.nextInt(list.size());
					} while (usedIndexes.contains(num) && usedIndexes.size() != list.size());
					usedIndexes.add(num);

					Entity target = this;
					int targetIndex = rand.nextInt(targetList.size());
					target = targetList.get(targetIndex);

					if (target != null && target.isAlive() && getCaster() != null) {
						LaserDomeShotEntity bullet = list.get(num);
						bullet.shoot(target.getPosX() - (bullet.getPosX() + world.rand.nextDouble() - 0.5D), target.getPosY() - bullet.getPosY(), target.getPosZ() - (bullet.getPosZ() + world.rand.nextDouble() - 0.5D), 2f, 0);
						world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 1F);
					}

				}
			}
		}
		super.tick();
	}

	private void updatePos(float r) {
		for (LaserDomeShotEntity shot : list) {
			double x = getPosX() + (r * Math.cos(Math.toRadians(shot.ticksExisted * 9)));
			double z = getPosZ() + (r * Math.sin(Math.toRadians(shot.ticksExisted * 9)));
			shot.setPosition(x, getPosY() + 1, z);
			shot.shoot(this.getPosX() - shot.getPosX(), this.getPosY() - shot.getPosY(), this.getPosZ() - shot.getPosZ(), 0.001f, 0);
		}
	}

	private void updateList() {
		List<Entity> tempList = world.getEntitiesWithinAABBExcludingEntity(getCaster(), getBoundingBox().grow(radius, radius, radius));
		Party casterParty = ModCapabilities.getWorld(world).getPartyFromMember(getCaster().getUniqueID());

		if(casterParty != null && !casterParty.getFriendlyFire()) {
			for (Party.Member m : casterParty.getMembers()) {
				tempList.remove(world.getPlayerByUuid(m.getUUID()));
			}
		} else {
			tempList.remove(getShooter());
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

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(LaserDomeCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(LaserDomeCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

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
