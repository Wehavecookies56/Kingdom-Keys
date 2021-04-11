package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeShotEntity;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class ShotlockCoreEntity extends ThrowableEntity {

	int maxTicks = 60;
	List<ShotlockShotEntity> list = new ArrayList<ShotlockShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	//Set<Integer> usedIndexes = new HashSet<Integer>();
	float dmg;

	double dmgMult;
	float radius = 15;
	int space = 12;
	int shotsPerTick = 1;

	public ShotlockCoreEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ShotlockCoreEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_SHOTLOCK_CORE.get(), world);
	}

	public ShotlockCoreEntity(World world) {
		super(ModEntities.TYPE_SHOTLOCK_CORE.get(), world);
		this.preventEntitySpawning = true;
	}

	public ShotlockCoreEntity(World world, PlayerEntity player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_CORE.get(), player, world);
		setCaster(player.getUniqueID());
		String targetIDS = "";
		for(Entity t : targets) {
			targetIDS+=","+t.getUniqueID();
		}
		setTarget(targetIDS.substring(1));
		this.targetList = targets;
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
	
	//int targetIndex = 0;

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

		System.out.println(getTargets().size());
		if (getCaster() != null) {
			if (ticksExisted >= 0 && ticksExisted < 20) {
				
				for(int i = 0; i < targetList.size();i++) {
					Entity target = targetList.get(i);
					ShotlockShotEntity bullet = new ShotlockShotEntity(world, getCaster(), target, dmg * dmgMult);
					bullet.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
					bullet.setMaxTicks(maxTicks + 20);
					//bullet.shoot(this.getPosX() - bullet.getPosX(), this.getPosY() - bullet.getPosY(), this.getPosZ() - bullet.getPosZ(), 0.001f, 0);
					list.add(bullet);
					world.addEntity(bullet);
				}
			}/* else if (ticksExisted > 20 && !targetList.isEmpty()) {
				for (int i = 0; i < shotsPerTick; i++) {
					//Entity target = this;
					//int targetIndex = rand.nextInt(targetList.size());
					Entity target = targetList.get(targetIndex++);

					if (target != null && target.isAlive() && getCaster() != null) {
						ShotlockShotEntity bullet = list.get(i);
						bullet.shoot(target.getPosX() - bullet.getPosX(), target.getPosY() - bullet.getPosY(), target.getPosZ() - bullet.getPosZ(), 2f, 0);
						world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 1F);
					}

				}
			}*/
		}
		super.tick();
	}

	/*private void updatePos(float r) {
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
			tempList.remove(func_234616_v_());
		}

		targetList.clear();
		for (Entity t : tempList) {
			if (!(t instanceof LaserDomeShotEntity || t instanceof ItemDropEntity || t instanceof ItemEntity || t instanceof ExperienceOrbEntity)) {
				targetList.add(t);
			}
		}
	}*/

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
			compound.putString("TargetsUUID", this.dataManager.get(TARGETS));
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGETS, compound.getString("TargetUUID"));
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(ShotlockCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<String> TARGETS = EntityDataManager.createKey(ShotlockCoreEntity.class, DataSerializers.STRING);

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	public List<Entity> getTargets() {
		List<Entity> list = new ArrayList<Entity>();
		String[] ids = this.getDataManager().get(TARGETS).split(",");
		System.out.println(this.getDataManager().get(TARGETS) + " : "+ ids.length);
		for(String id : ids) {
			System.out.println(id);
		//	list.add(world.entitEntitByID(UUID.fromString(id)));
		}
		return list;
	}

	public void setTarget(String lists) {
		this.dataManager.set(TARGETS, lists);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGETS, "");
	}
}
