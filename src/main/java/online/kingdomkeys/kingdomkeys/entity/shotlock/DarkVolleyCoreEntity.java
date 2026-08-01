package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
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

public class DarkVolleyCoreEntity extends BaseShotlockCoreEntity {

	List<VolleyShotEntity> list = new ArrayList<>();
	private int shotColor = 4921675;
	private ResourceKey<DamageType> element = null;
	private boolean zigzag = false;
	private boolean waterVisual = false;
	private boolean applyPoison = false;
	private boolean explodeOnHit = false;
	private boolean radialBurst = false;

	public void setShotColor(int color) {
		this.shotColor = color;
		this.shotStyle.colour = color;
	}

	public void setElement(ResourceKey<DamageType> element) {
		this.element = element;
		this.shotStyle.element = element;
	}

	public void setZigzag(boolean zigzag) {
		this.zigzag = zigzag;
	}

	public void setWaterVisual(boolean waterVisual) {
		this.waterVisual = waterVisual;
		this.shotStyle.waterVisual = waterVisual;
	}

	public void setApplyPoison(boolean applyPoison) {
		this.applyPoison = applyPoison;
		this.shotStyle.applyPoison = applyPoison;
	}

	public void setExplodeOnHit(boolean explodeOnHit) {
		this.explodeOnHit = explodeOnHit;
	}

	public void setRadialBurst(boolean radialBurst) {
		this.radialBurst = radialBurst;
	}

	public DarkVolleyCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.maxTicks = 260;
		this.shotStyle.colour = shotColor;
	}

	public DarkVolleyCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_DARK_VOLLEY.get(), world, player, targets, dmg);
		this.maxTicks = 260;
		this.shotStyle.colour = shotColor;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	int i = 0;

	@Override
	public void tick() {
		if (isExpired()) {
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
					bullet.setRadialBurst(radialBurst);
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

}
