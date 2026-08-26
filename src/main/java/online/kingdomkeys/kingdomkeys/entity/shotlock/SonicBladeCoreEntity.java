package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SonicBladeCoreEntity extends BaseShotlockCoreEntity {
	List<VolleyShotEntity> list = new ArrayList<>();
	private ResourceKey<DamageType> element = null; // null = original generic damage - set to a KKDamageTypes entry for elemental reskins (e.g. Absolute Zero)
	private Color particleColor = new Color(255,255,255);

	public void setElement(ResourceKey<DamageType> element) {
		this.element = element;
	}

	public void setParticleColor(Color color) {
		this.particleColor = color;
	}

	private DamageSource buildDamageSource(LivingEntity target) {
		if (element != null) {
			return KKDamageTypes.getElementalDamage(element, this, getOwner());
		}
		return target.damageSources().thrown(this, getOwner());
	}
	
	@Override
	public boolean movesCaster() {
		return true; // this one dashes the player from target to target
	}

	public SonicBladeCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.maxTicks = 260;
	}

	public SonicBladeCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_SONIC_BLADE.get(), world, player, targets, dmg);
		this.maxTicks = 260;
	}

	@Override
	public void tick() {
		if(getCaster() == null) {
			this.remove(RemovalReason.KILLED);
			return;
		}
		getCaster().startAutoSpinAttack(10, 4, getCaster().getMainHandItem());

		if(tickCount % 8 == 1) {
			if (getCaster() != null && getTarget() != null) {
				BlockPos pos = getTarget().blockPosition();
				float speedFactor = 0.4F;
				getCaster().setDeltaMovement((pos.getX() - getCaster().getX()) * speedFactor, (pos.getY() + 1 - getCaster().getY()) * speedFactor, (pos.getZ() - getCaster().getZ()) * speedFactor);
	
				if (level().isClientSide) {
					getCaster().hurtMarked = true;
				}
	
	            if (getCaster().getVehicle() != null) {
	            	getCaster().getVehicle().onPassengerTurned(getCaster());
	            }
	            setActualTargetIndex(getActualTargetIndex()+1);
			} else {
				kill();
				return;
			}
			
		}
		
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > 1 && getOwner() != null && level() instanceof ServerLevel serverLevel) {
			double ex = getOwner().getX();
			double ey = getOwner().getY() + 1;
			double ez = getOwner().getZ();
			Color color = particleColor;

			// Base color cloud, now with actual spread/count instead of one lonely particle a tick.
			serverLevel.sendParticles(new DustParticleOptions(new Vector3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F), 1.2F), ex, ey, ez, 5, 0.35, 0.5, 0.35, 0.02);

			// An actual orbiting ring around the caster - matches BBS's own description of these as
			// "dashing around surrounded by an elemental aura" instead of just a particle color swap.
			double angleBase = Math.toRadians(tickCount * 25D);
			for (int i = 0; i < 4; i++) {
				double angle = angleBase + i * (Math.PI / 2D);
				double ox = ex + Math.cos(angle) * 0.7D;
				double oz = ez + Math.sin(angle) * 0.7D;

				if (element != null && element.equals(KKDamageTypes.ICE)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, ox, ey, oz, 2, 0.05, 0.15, 0.05, 0.01);
				} else if (element != null && element.equals(KKDamageTypes.LIGHT)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD, ox, ey, oz, 2, 0.05, 0.1, 0.05, 0.01);
				} else if (element != null && element.equals(KKDamageTypes.LIGHTNING)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, ox, ey, oz, 2, 0.05, 0.15, 0.05, 0.02);
				} else if (element != null && element.equals(KKDamageTypes.DARKNESS)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, ox, ey, oz, 1, 0.05, 0.1, 0.05, 0.005);
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, ox, ey, oz, 1, 0.05, 0.1, 0.05, 0.005);
				}
			}

			// A little extra punctuation on top of the ring, unique per element.
			if (element != null && element.equals(KKDamageTypes.ICE)) {
				serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL, ex, ey, ez, 3, 0.3, 0.3, 0.3, 0.02);
			} else if (element != null && element.equals(KKDamageTypes.LIGHT)) {
				serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH, ex, ey, ez, 1, 0, 0, 0, 0);
			} else if (element != null && element.equals(KKDamageTypes.LIGHTNING)) {
				serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, ex, ey, ez, 4, 0.3, 0.4, 0.3, 0.05);
			}
		}
		
		if(tickCount % 4 == 0) {
			double r = 1.5D;
            AABB aabb = new AABB(getOwner().position().x, getOwner().position().y, getOwner().position().z, getOwner().position().x + 1, getOwner().position().y + 1, getOwner().position().z + 1).inflate(r, 0, r);
    		List<LivingEntity> list = getOwner().level().getEntitiesOfClass(LivingEntity.class, aabb);
    		list.remove(getOwner());
    		
            for(LivingEntity enemy : list) {
            	enemy.hurt(buildDamageSource(enemy), dmg);
            	if (element != null && element.equals(KKDamageTypes.ICE)) {
            		enemy.addEffect(new MobEffectInstance(online.kingdomkeys.kingdomkeys.effects.ModMobEffects.FREEZE, 40, 50, false, true, true));
				}
			}

		}
		
		super.tick();
	}

	
	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	private Entity getTarget() {
		if(getTargets().size() <= getActualTargetIndex())
			return null;
		return getTargets().get(getActualTargetIndex());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ActualTargetIndex", this.entityData.get(ACTUAL_TARGET_INDEX));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(ACTUAL_TARGET_INDEX, compound.getInt("ActualTargetIndex"));
	}

	/** Which of the locked-on targets the blade is dashing at right now - only this core tracks one. */
	private static final EntityDataAccessor<Integer> ACTUAL_TARGET_INDEX = SynchedEntityData.defineId(SonicBladeCoreEntity.class, EntityDataSerializers.INT);

	public int getActualTargetIndex() {
		return this.getEntityData().get(ACTUAL_TARGET_INDEX);
	}

	public void setActualTargetIndex(int actual) {
		this.entityData.set(ACTUAL_TARGET_INDEX, actual);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(ACTUAL_TARGET_INDEX, 0);
	}
}
