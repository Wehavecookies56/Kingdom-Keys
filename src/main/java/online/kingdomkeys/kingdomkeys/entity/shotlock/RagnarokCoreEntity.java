package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class RagnarokCoreEntity extends ThrowableProjectile {

	int maxTicks = 100;
	List<RagnarokShotEntity> list = new ArrayList<RagnarokShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	float dmg;

	public RagnarokCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public RagnarokCoreEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), world);
	}

	public RagnarokCoreEntity(Level world) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), world);
		this.blocksBuilding = true;
	}

	public RagnarokCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), player, world);
		setCaster(player.getUUID());
		String targetIDS = "";
		for(Entity t : targets) {
			targetIDS+=","+t.getId();
		}
		setTarget(targetIDS.substring(1));
		this.targetList = targets;
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

		level.addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0, 0, 0);

		double X = getX();
		double Y = getY()+1;
		double Z = getZ();
		
		if (getCaster() != null && getTargets() != null) {
			if (tickCount == 1) {
				level.playSound(null, this.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1, 1);
				for(int i = 0; i< getTargets().size();i++) {
					Entity target = getTargets().get(i);
					if(target != null) {
						RagnarokShotEntity bullet = new RagnarokShotEntity(level, getCaster(), target, dmg);
						bullet.setColor(16757273);
						float r = 0.3F;
						double offset_amount = -1.5;
						double alpha = Math.toRadians(getCaster().getYRot());
						double theta = 2 * Math.PI / getTargets().size();
						double x = X + offset_amount * Math.sin(alpha) + r * ((Math.cos(i * theta) + Math.sin(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.cos(alpha) + (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));
						double y = Y + r * ((Math.cos(alpha) * Math.sin(i * theta)) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(i * theta) * Math.sin(alpha));
						double z = Z - offset_amount * Math.cos(alpha) + r * (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta)) * Math.cos(alpha) + (Math.cos(i * theta) + Math.cos(alpha) * Math.cos(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));

						bullet.setPos(x,y,z);
						bullet.setMaxTicks(maxTicks + 20);
						//bullet.shoot(this.getPosX() - bullet.getPosX(), this.getPosY() - bullet.getPosY(), this.getPosZ() - bullet.getPosZ(), 0.001f, 0);
						list.add(bullet);
						level.addFreshEntity(bullet);
					}
				}
			} else if(tickCount > 4 && tickCount < 10) {
				for(int i = 0; i< list.size();i++) {
					RagnarokShotEntity bullet = list.get(i);
					float posI = i + tickCount*2;
					float r = 0.3F*tickCount;
					double offset_amount = -2;
					double alpha = Math.toRadians(getCaster().getYRot());
					double theta = 2 * Math.PI / getTargets().size();
					double x = X + offset_amount * Math.sin(alpha) + r * ((Math.cos(posI * theta) + Math.sin(alpha) * Math.sin(alpha) * (1 - Math.cos(posI * theta))) * Math.cos(alpha) + (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(posI * theta))) * Math.sin(alpha));
					double y = Y + r * ((Math.cos(alpha) * Math.sin(posI * theta)) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(posI * theta) * Math.sin(alpha));
					double z = Z - offset_amount * Math.cos(alpha) + r * (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(posI * theta)) * Math.cos(alpha) + (Math.cos(posI * theta) + Math.cos(alpha) * Math.cos(alpha) * (1 - Math.cos(posI * theta))) * Math.sin(alpha));

					bullet.setPos(x,y,z);		
				}
			}
		}
		super.tick();
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
			compound.putString("TargetsUUID", this.entityData.get(TARGETS));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGETS, compound.getString("TargetUUID"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(RagnarokCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<String> TARGETS = SynchedEntityData.defineId(RagnarokCoreEntity.class, EntityDataSerializers.STRING);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public List<Entity> getTargets() {
		List<Entity> list = new ArrayList<Entity>();
		String[] ids = this.getEntityData().get(TARGETS).split(",");
		
		for(String id : ids) {
		
			if(!id.equals(""))
				list.add(level.getEntity(Integer.parseInt(id)));
		}
		return list;
	}

	public void setTarget(String lists) {
		this.entityData.set(TARGETS, lists);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(OWNER, Optional.of(new UUID(0L, 0L)));
		this.entityData.define(TARGETS, "");
	}
}
