package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.ArrayList;
import java.util.List;

public class RagnarokCoreEntity extends BaseShotlockCoreEntity {

	List<RagnarokShotEntity> list = new ArrayList<>();
	private int shotColor = 16757273;
	private ResourceKey<DamageType> element = null;
	private ItemStack visualItem = ItemStack.EMPTY;
	private boolean applyPoison = false;

	public void setApplyPoison(boolean applyPoison) {
		this.applyPoison = applyPoison;
	}

	public void setShotColor(int color) {
		this.shotColor = color;
	}

	public void setElement(ResourceKey<DamageType> element) {
		this.element = element;
	}

	public void setVisualItem(ItemStack stack) {
		this.visualItem = stack;
	}

	public RagnarokCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.maxTicks = 100;
	}

	public RagnarokCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
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
						bullet.setColor(shotColor);
						bullet.setElement(element);
						bullet.setVisualItem(visualItem);
						bullet.setApplyPoison(applyPoison);
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

	@Override
	protected void onHit(HitResult rtRes) {

	}

}
