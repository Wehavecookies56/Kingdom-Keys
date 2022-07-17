package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SonicBladeCoreEntity extends ThrowableProjectile{
	int maxTicks = 260;
	List<VolleyShotEntity> list = new ArrayList<VolleyShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	float dmg;
	
	public SonicBladeCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public SonicBladeCoreEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_SHOTLOCK_SONIC_BLADE.get(), world);
	}

	public SonicBladeCoreEntity(Level world) {
		super(ModEntities.TYPE_SHOTLOCK_SONIC_BLADE.get(), world);
		this.blocksBuilding = true;
	}

	public SonicBladeCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_SONIC_BLADE.get(), player, world);
		setCaster(player.getUUID());
		String targetIDS = "";
		for(Entity t : targets) {
			targetIDS+=","+t.getId();
		}
		setTarget(targetIDS.substring(1));
		this.targetList = targets;
		this.dmg = dmg;
        player.startAutoSpinAttack(20 * targets.size());

	}

	@Override
	public void tick() {
		if(tickCount % 8 == 1) {
			//System.out.println(getTarget());
			if (getCaster() != null && getTarget() != null) {
				BlockPos pos = getTarget().blockPosition();
				float speedFactor = 0.5F;
				getCaster().setDeltaMovement((pos.getX() - getCaster().getX()) * speedFactor, (pos.getY() - getCaster().getY()) * speedFactor, (pos.getZ() - getCaster().getZ()) * speedFactor);
	
				if (level.isClientSide) {
					getCaster().hurtMarked = true;
				}
					
				double dx = getCaster().getX() - getTarget().getX();
	            double dz = getCaster().getZ() - getTarget().getZ();
	            double dy = getCaster().getY() - (getTarget().getY() + (getTarget().getBbHeight() / 2.0F)-getCaster().getBbHeight());
	            double yaw = Math.atan2(dz, dx) * 180 / Math.PI;
	            double pitch = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180 / Math.PI;
	            double distance = getCaster().distanceTo(getTarget());
	            
	            float rYaw = (float) Mth.wrapDegrees(yaw - getCaster().getXRot()) + 90;
	            float rPitch = (float) pitch - (float) (10.0F / Math.sqrt(distance)) + (float) (distance * Math.PI / 90);
	            /*
	            float f = getCaster().getXRot();
	            float f1 = getCaster().getYRot();
	            
	            getCaster().setYRot((float)((double)getCaster().getYRot() + (double)rYaw * 0.15D));
	            getCaster().setXRot((float)((double)getCaster().getXRot() - (double)-(rPitch - getCaster().getXRot()) * 0.15D));
	            getCaster().xRotO = getCaster().getXRot() - f;
	            getCaster().yRotO += getCaster().getYRot() - f1;*/
	            Player player = getCaster();
	            
                /*float f7 = rPitch;
                float f = rYaw;
                float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 3.0F * ((1.0F + (float)1) / 4.0F);
                f1 *= f5 / f4;
                f2 *= f5 / f4;
                f3 *= f5 / f4;
                player.push((double)f1, (double)f2, (double)f3);*/
                /*if (player.isOnGround()) { //Leap
                   float f6 = 1.1999999F;
                   player.move(MoverType.SELF, new Vec3(0.0D, f6, 0.0D));
                }*/
	
	            if (getCaster().getVehicle() != null) {
	            	getCaster().getVehicle().onPassengerTurned(getCaster());
	            }
	            	            
	           // updateMovement();
	            setActualTargetIndex(getActualTargetIndex()+1);
			} else {
				kill();
				return;
			}
			
		}
		
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > 1) {
			Color color = new Color(255,255,255);
			level.addParticle(new DustParticleOptions(new Vector3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F), 1F), getOwner().getX(), getOwner().getY()+1, getOwner().getZ(), 1,1,1);
		}
		
		if(tickCount % 4 == 0) {
			double r = 1.5D;
            AABB aabb = new AABB(getOwner().position().x, getOwner().position().y, getOwner().position().z, getOwner().position().x + 1, getOwner().position().y + 1, getOwner().position().z + 1).inflate(r, 0, r);
    		List<LivingEntity> list = getOwner().level.getEntitiesOfClass(LivingEntity.class, aabb);
    		list.remove(getOwner());
    		
            for(LivingEntity enemy : list) {
				enemy.hurt(DamageSource.thrown(this, this.getOwner()), dmg);
			}

		}
		
		super.tick();
	}

	private void updateMovement() {
		if(getTarget() != null) {
			if(getTarget().isAlive()) {
				this.shoot(getTarget().getX() - this.getX(), (getTarget().getY() + (getTarget().getBbHeight() / 2.0F) - this.getBbHeight()) - getY() + 0.5, getTarget().getZ() - this.getZ(), 1, 0);
			} else {
				if(getOwner() != null)
					this.shootFromRotation(this, getOwner().getXRot(), getOwner().getYRot(), 0, 1, 0); // Work in progress
			}
			
			//Damage
		}
	}

	/*@Override
	protected void onHit(HitResult rtRes) {
		if (!level.isClientSide) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getOwner()) {
					target.hurt(DamageSource.thrown(this, this.getOwner()), dmg);
					super.remove(RemovalReason.KILLED);
				}
			}
			remove(RemovalReason.KILLED);
		}
	}*/
	
	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	private Entity getTarget() {
		//System.out.println(getTargets().size() +" "+ getActualTargetIndex());
		if(getTargets().size() <= getActualTargetIndex())
			return null;
		return getTargets().get(getActualTargetIndex());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putString("TargetsUUID", this.entityData.get(TARGETS));
			compound.putInt("ActualTargetIndex", this.entityData.get(ACTUAL_TARGET_INDEX));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGETS, compound.getString("TargetUUID"));
		this.entityData.set(ACTUAL_TARGET_INDEX, compound.getInt("ActualTargetIndex"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(SonicBladeCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<String> TARGETS = SynchedEntityData.defineId(SonicBladeCoreEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> ACTUAL_TARGET_INDEX = SynchedEntityData.defineId(SonicBladeCoreEntity.class, EntityDataSerializers.INT);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}
	
	public int getActualTargetIndex() {
		return this.getEntityData().get(ACTUAL_TARGET_INDEX);
	}
	
	public void setActualTargetIndex(int actual) {
		this.entityData.set(ACTUAL_TARGET_INDEX, actual);
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
		this.entityData.define(ACTUAL_TARGET_INDEX, 0);
	}
	
}
