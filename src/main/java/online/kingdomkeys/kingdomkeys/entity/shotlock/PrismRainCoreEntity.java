package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.ArrayList;
import java.util.List;

public class PrismRainCoreEntity extends BaseShotlockCoreEntity {

	// Last tick of the outward spread - past this the core is only waiting on its bullets
	private static final int EXPAND_END_TICK = 10;

	List<RagnarokShotEntity> list = new ArrayList<>();

	// Prism Rain cycles its bullets through these instead of using one flat colour
	private static final int[] PALETTE = {0xFFFFFF, 0xFF0000, 0x00FF00, 0x0000FF, 0xFF00FF, 0xFFFF00, 0x00FFFF};

	public PrismRainCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.maxTicks = 100;
		this.shotStyle.palette = PALETTE;
	}

	public PrismRainCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), world, player, targets, dmg);
		this.maxTicks = 100;
		this.shotStyle.palette = PALETTE;
	}

	@Override
	public boolean launchesCaster() {
		return true;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (isExpired()) {
			dropCaster();
			this.remove(RemovalReason.KILLED);
		}

		level().addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0, 0, 0);

		double X = getX();
		double Y = getY()+1;
		double Z = getZ();
		
		if (getCaster() != null && getTargets() != null) {
			// Held every tick this core is alive, and picked up by the follow-up minigame afterwards
			holdCasterAirborne();

			if (tickCount == 1) {
				// Up first, so the ring opens around the caster in mid-air instead of clipping the floor
				launchCasterUpwards();
				level().playSound(null, this.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1, 1);
				for(int i = 0; i< getTargets().size();i++) {
					Entity target = getTargets().get(i);
					if(target != null) {
						RagnarokShotEntity bullet = new RagnarokShotEntity(level(), getCaster(), target, dmg);
						bullet.setColor(getColor(i%7));
						float r = 0.3F;
						double offset_amount = -1.5;
						double alpha = Math.toRadians(getCaster().getYRot());
						double theta = 2 * Math.PI / getTargets().size();
						double x = X + offset_amount * Math.sin(alpha) + r * ((Math.cos(i * theta) + Math.sin(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.cos(alpha) + (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));
						double y = Y + r * ((Math.cos(alpha) * Math.sin(i * theta)) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(i * theta) * Math.sin(alpha));
						double z = Z - offset_amount * Math.cos(alpha) + r * (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta)) * Math.cos(alpha) + (Math.cos(i * theta) + Math.cos(alpha) * Math.cos(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));

						bullet.setPos(x,y,z);
						bullet.setMaxTicks(maxTicks + 20);
						list.add(bullet);
						level().addFreshEntity(bullet);
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

		// Same as the Ragnarok core: everything is fired on tick 1, so once the bullets are spent
		// there's no reason to keep idling until maxTicks.
		if (tickCount > EXPAND_END_TICK && !hasLiveShots(list)) {
			dropCaster();
			this.remove(RemovalReason.KILLED);
		}

		super.tick();
	}

	private int getColor(int i) {
		return PALETTE[Math.floorMod(i, PALETTE.length)];
	}

	@Override
	protected void onHit(HitResult rtRes) {

	}

}
