package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;

import java.util.*;

import net.minecraft.world.entity.Entity.RemovalReason;

public class LaserCircleCoreEntity extends ThrowableProjectile {

	int maxTicks = 70;
	List<LaserDomeShotEntity> list = new ArrayList<LaserDomeShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	Set<Integer> usedIndexes = new HashSet<Integer>();
	float dmg;

	float radius = 4;
	int space;
	int shotsPerTick;

	public LaserCircleCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public LaserCircleCoreEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_LASER_CIRCLE.get(), world);
	}

	public LaserCircleCoreEntity(Level world) {
		super(ModEntities.TYPE_LASER_CIRCLE.get(), world);
		this.blocksBuilding = true;
	}

	public LaserCircleCoreEntity(Level world, Player player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_LASER_CIRCLE.get(), player, world);
		setCaster(player.getUUID());
		setTarget(target.getUUID());
		this.dmg = dmg;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks || getCaster() == null) {
			this.remove(RemovalReason.KILLED);
		}

		//level.addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0, 0, 0);

		double X = getX();
		double Y = getY();
		double Z = getZ();

		if (getCaster() != null) {
			if (tickCount >= 0 && tickCount <= 40 && tickCount % 2 == 0) {
				double x = X + (radius * Math.cos(Math.toRadians(tickCount * 9)));
				double z = Z + (radius * Math.sin(Math.toRadians(tickCount * 9)));
				LaserDomeShotEntity bullet = new LaserDomeShotEntity(level, getCaster(), dmg);
				bullet.setPos(x, Y + 1, z);
				bullet.setMaxTicks(maxTicks);
				bullet.shoot(this.getX() - bullet.getX(), this.getY() - bullet.getY()+1, this.getZ() - bullet.getZ(), 0.001f, 0);
				level.playSound(getCaster(), getCaster().blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1F, 1F);
				list.add(bullet);
				level.addFreshEntity(bullet);

				this.setDeltaMovement(0, 0, 0);
				this.hurtMarked = true;

				/*if(getTarget() != null) {
					this.setPosition(getTarget().getPosX(), getTarget().getPosY(), getTarget().getPosZ());
					updatePos(radius);
				}*/
			} else if (tickCount == 60) {
				updateList();//Get all entities in the radius
				Entity target = this;
				if(targetList.size() > 1) {
					targetList.remove(this);
				} //If there are more entities than the controller remove it and track a random of the left ones
				int targetIndex = random.nextInt(targetList.size());
				target = targetList.get(targetIndex);
				
				for (LaserDomeShotEntity bullet : list) {
					if (target != null && target.isAlive() && getCaster() != null) {
						bullet.shoot(target.getX() - bullet.getX(), target.getY() - bullet.getY()+1, target.getZ() - bullet.getZ(), 1.5f, 0);
					}
				}
				level.playSound(getCaster(), getCaster().blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1F, 1F);

			}
		}

		super.tick();
	}

	private void updatePos(float r) {
		for(LaserDomeShotEntity shot : list) {
			double x = getX() + (r * Math.cos(Math.toRadians(shot.tickCount * 9)));
			double z = getZ() + (r * Math.sin(Math.toRadians(shot.tickCount * 9)));
			shot.setPos(x, getY() + 1, z);
			shot.shoot(this.getX() - shot.getX(), this.getY() - shot.getY(), this.getZ() - shot.getZ(), 0.001f, 0);
		}
	}

	private void updateList() {
		List<Entity> tempList = level.getEntities(getCaster(), getBoundingBox().inflate(radius, radius, radius));
		Party casterParty = ModCapabilities.getWorld(level).getPartyFromMember(getCaster().getUUID());

		if(casterParty != null && !casterParty.getFriendlyFire()) {
			for (Party.Member m : casterParty.getMembers()) {
				tempList.remove(level.getPlayerByUUID(m.getUUID()));
			}
		} else {
			tempList.remove(getOwner());
		}

		targetList.clear();
		for (Entity t : tempList) {
			if (!(t instanceof LaserDomeShotEntity || t instanceof ItemDropEntity || t instanceof ItemEntity || t instanceof ExperienceOrb)) {
				targetList.add(t);
			}
		}
	}

	@Override
	protected void onHit(HitResult rtRes) {

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putString("TargetUUID", this.entityData.get(TARGET).get().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(LaserCircleCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(LaserCircleCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public Player getTarget() {
		return this.getEntityData().get(TARGET).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(TARGET).get()) : null;
	}

	public void setTarget(UUID uuid) {
		this.entityData.set(TARGET, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(OWNER, Optional.of(new UUID(0L, 0L)));
		this.entityData.define(TARGET, Optional.of(new UUID(0L, 0L)));
	}
}