package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DarkVolleyCoreEntity extends ThrowableProjectile {

	int maxTicks = 260;
	List<VolleyShotEntity> list = new ArrayList<>();
	List<Entity> targetList = new ArrayList<>();
	float dmg;
	private int shotColor = 4921675;
	private net.minecraft.resources.ResourceKey<net.minecraft.world.damagesource.DamageType> element = null;
	private boolean zigzag = false;
	private boolean waterVisual = false;
	private boolean applyPoison = false;
	private boolean explodeOnHit = false;

	public void setShotColor(int color) {
		this.shotColor = color;
	}

	public void setElement(net.minecraft.resources.ResourceKey<net.minecraft.world.damagesource.DamageType> element) {
		this.element = element;
	}

	public void setZigzag(boolean zigzag) {
		this.zigzag = zigzag;
	}

	public void setWaterVisual(boolean waterVisual) {
		this.waterVisual = waterVisual;
	}

	public void setApplyPoison(boolean applyPoison) {
		this.applyPoison = applyPoison;
	}

	public void setExplodeOnHit(boolean explodeOnHit) {
		this.explodeOnHit = explodeOnHit;
	}

	public DarkVolleyCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public DarkVolleyCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_DARK_VOLLEY.get(), player, world);
		setCaster(player.getUUID());
		String targetIDS = "";
		for(Entity t : targets) {
            if(t != null) {
                targetIDS += "," + t.getId();
            }
		}
		setTarget(targetIDS.substring(1));
		this.targetList = targets;
		this.dmg = dmg;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	int i = 0;

	@Override
	public void tick() {
		if (this.tickCount > maxTicks || getCaster() == null) {
			this.remove(RemovalReason.KILLED);
		}

		if (getCaster() != null && getTargets() != null && !getTargets().isEmpty() && getTargets().size() > i) {
			if (tickCount >= 0 && tickCount % 2 == 1) {
				
				Entity target = getTargets().get(i++);
				if(target != null) {
					VolleyShotEntity bullet = new VolleyShotEntity(level(), getCaster(), target, dmg);
					bullet.setColor(shotColor);
					bullet.setElement(element);
					bullet.setZigzag(zigzag);
					bullet.setWaterVisual(waterVisual);
					bullet.setApplyPoison(applyPoison);
					bullet.setExplodeOnHit(explodeOnHit);
					bullet.setPos(Utils.randomWithRange(this.getX()-2, this.getX()+2), Utils.randomWithRange(this.getY()-2, this.getY()+2)+1F, Utils.randomWithRange(this.getZ()-2, this.getZ()+2));
					bullet.setMaxTicks(maxTicks + 20);
					//bullet.shoot(this.getPosX() - bullet.getPosX(), this.getPosY() - bullet.getPosY(), this.getPosZ() - bullet.getPosZ(), 0.001f, 0);
					list.add(bullet);
					level().addFreshEntity(bullet);
					level().playSound(null, this.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1, 1);
				}
			}
			
			if(getTargets().size() <= i) {
				this.remove(RemovalReason.KILLED);
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
		if (this.entityData.get(OWNER).isPresent()) {
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

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(DarkVolleyCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<String> TARGETS = SynchedEntityData.defineId(DarkVolleyCoreEntity.class, EntityDataSerializers.STRING);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public List<Entity> getTargets() {
		List<Entity> list = new ArrayList<Entity>();
		String[] ids = this.getEntityData().get(TARGETS).split(",");
		
		for(String id : ids) {
		
			if(!id.equals(""))
				list.add(level().getEntity(Integer.parseInt(id)));
		}
		return list;
	}

	public void setTarget(String lists) {
		this.entityData.set(TARGETS, lists);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(TARGETS, "");
	}
}
