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

	List<RagnarokShotEntity> list = new ArrayList<>();

	public PrismRainCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.maxTicks = 100;
	}

	public PrismRainCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), world, player, targets, dmg);
		this.maxTicks = 100;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (isExpired()) {
			this.remove(RemovalReason.KILLED);
		}

		level().addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0, 0, 0);

		double X = getX();
		double Y = getY()+1;
		double Z = getZ();
		
		if (getCaster() != null && getTargets() != null) {
			if (tickCount == 1) {
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
		super.tick();
	}

	private int getColor(int i) {
		return switch (i) {
			case 0 -> 0xFFFFFF;
			case 1 -> 0xFF0000;
			case 2 -> 0x00FF00;
			case 3 -> 0x0000FF;
			case 4 -> 0xFF00FF;
			case 5 -> 0xFFFF00;
			case 6 -> 0x00FFFF;
			default -> 0;
		};
	}

	@Override
	protected void onHit(HitResult rtRes) {

	}

}
