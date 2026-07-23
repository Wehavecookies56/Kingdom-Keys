package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LightBarrageCoreEntity extends ThrowableProjectile {

	private static final int MAX_RISE_TICKS = 35;
	private static final double RISE_HEIGHT = 4D;
	private static final int ORB_COUNT = 20;
	private static final int ORB_INTERVAL_TICKS = 5;
	private static final float ORB_SPEED = 0.6F;

	private float dmg;
	private int orbsFired = 0;
	private double startY = Double.NaN;
	private double holdY = Double.NaN;
	private boolean holding = false;
	private boolean finished = false;

	public LightBarrageCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
		this.noPhysics = true;
	}

	public LightBarrageCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_LIGHT_BARRAGE.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (!(getOwner() instanceof Player caster) || !caster.isAlive()) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (Double.isNaN(startY)) {
			startY = caster.getY();
		}

		if (!level().isClientSide) {
			if (finished) {
				this.remove(RemovalReason.KILLED);
				return;
			}

			if (!holding) {
				if (caster.getY() >= startY + RISE_HEIGHT || tickCount >= MAX_RISE_TICKS) {
					holding = true;
					holdY = caster.getY();
					caster.removeEffect(MobEffects.LEVITATION);
					level().playSound(null, caster.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1F, 1.5F);
				}
			} else {
				caster.setDeltaMovement(caster.getDeltaMovement().x * 0.5D, 0D, caster.getDeltaMovement().z * 0.5D);
				if (Math.abs(caster.getY() - holdY) > 0.05D) {
					caster.teleportTo(caster.getX(), holdY, caster.getZ());
				}
				caster.fallDistance = 0;

				if (orbsFired < ORB_COUNT) {
					if (tickCount % ORB_INTERVAL_TICKS == 0) {
						fireOrb(caster);
						orbsFired++;
					}
				} else {
					finished = true;
				}
			}
		}

		this.setPos(caster.getX(), caster.getY() + caster.getEyeHeight(), caster.getZ());
		super.tick();
	}

	private void fireOrb(Player caster) {
		LightOrbEntity orb = new LightOrbEntity(level(), caster, dmg);
		Vec3 eyePos = caster.getEyePosition();
		orb.setPos(eyePos.x, eyePos.y, eyePos.z);
		orb.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0F, ORB_SPEED, 0F);
		level().addFreshEntity(orb);
		level().playSound(null, caster.blockPosition(), ModSounds.lightBeam.get(), SoundSource.PLAYERS, 0.8F, 1F);

	}

	@Override
	protected void onHit(HitResult result) {}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
	}
}
