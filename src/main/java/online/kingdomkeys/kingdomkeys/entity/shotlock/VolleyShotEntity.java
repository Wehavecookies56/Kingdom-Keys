package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import org.joml.Vector3f;

import java.awt.*;

public class VolleyShotEntity extends BaseShotlockShotEntity {
	private static final int RADIAL_RETARGET_DELAY_TICKS = 30; // ~1.5s before it starts homing in on the target
	private static final double RADIAL_BURST_SPEED = 0.8D;

	private boolean zigzag = false;
	private boolean waterVisual = false;
	private boolean applyPoison = false;
	private boolean explodeOnHit = false;
	private boolean radialBurst = false;
	private int zigzagPhase = 0;

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

	public void setRadialBurst(boolean radialBurst) {
		this.radialBurst = radialBurst;
	}

	public VolleyShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public VolleyShotEntity(Level world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world, player, target, dmg);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > 1) {
			Color color = new Color(getColor());
			level().addParticle(new DustParticleOptions(new Vector3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F), 1F), getX(), getY(), getZ(), 1,1,1);

			if (waterVisual && level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.SPLASH, getX(), getY(), getZ(), 4, 0.2, 0.2, 0.2, 0.05);
				serverLevel.sendParticles(ParticleTypes.FALLING_WATER, getX(), getY(), getZ(), 2, 0.15, 0.15, 0.15, 0.01);
			}
		}
		
		if(tickCount == 1 && radialBurst) {
			// A random outward direction, mostly horizontal (a little vertical spread) so it reads as
			// "bursting outward around the caster" rather than firing straight up/down half the time.
			double angle = level().random.nextDouble() * 2 * Math.PI;
			double upness = (level().random.nextDouble() - 0.3D) * 0.6D;
			Vec3 dir = new Vec3(Math.cos(angle), upness, Math.sin(angle)).normalize();
			this.setDeltaMovement(dir.scale(RADIAL_BURST_SPEED));
		}

		if (radialBurst) {
			if (tickCount >= RADIAL_RETARGET_DELAY_TICKS && (tickCount - RADIAL_RETARGET_DELAY_TICKS) % 10 == 0) {
				updateMovement();
			}
		} else if (tickCount % 10 == 0) {
			updateMovement();
		}
		
		super.tick();
		applySpiralWobble();
	}

	private void updateMovement() {
		if(getTarget() != null) {
			if(getTarget().isAlive()) {
				double dx = getTarget().getX() - this.getX();
				double dy = (getTarget().getY() + (getTarget().getBbHeight() / 2.0F) - this.getBbHeight()) - getY() + 0.5;
				double dz = getTarget().getZ() - this.getZ();

				if (zigzag) {
					// Adds a perpendicular offset that flips side each update, weaving toward the
					// target instead of a straight/spiral line - it still ends up hitting the target,
					// same as the wobble, just a much wider and more deliberate weave.
					Vec3 dir = new Vec3(dx, dy, dz);
					if (dir.lengthSqr() > 1E-6) {
						dir = dir.normalize();
						Vec3 upRef = Math.abs(dir.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
						Vec3 side = dir.cross(upRef).normalize();
						float sign = (zigzagPhase++ % 2 == 0) ? 1F : -1F;
						Vec3 offset = side.scale(sign * 0.9D);
						dx += offset.x;
						dy += offset.y;
						dz += offset.z;
					}
				}

				this.shoot(dx, dy, dz, 1, 0);
			} else {
				if(getOwner() != null)
					this.shootFromRotation(this, getOwner().getXRot(), getOwner().getYRot(), 0, 1, 0); // Work in progress
			}
		}
	}

	@Override
	protected void onHit(HitResult rtRes) {
		super.onHit(rtRes);
		if (!level().isClientSide) {
			if (rtRes instanceof EntityHitResult ertResult) {
				if (ertResult.getEntity() instanceof LivingEntity target) {
                    if (target != getOwner()) {
						target.invulnerableTime = 0;
						target.hurt(buildDamageSource(target), dmg);

						if (applyPoison) {
							target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0, false, true, true));
						}

						if (explodeOnHit && level() instanceof ServerLevel serverLevel) {
							serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
							level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.6F, 1.3F);
						}

						super.remove(RemovalReason.KILLED);
					}
				}
			}
			remove(RemovalReason.KILLED);
		}
	}
	
	
}
