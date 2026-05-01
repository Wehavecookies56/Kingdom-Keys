package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public abstract class BaseMagicProjectile extends ThrowableProjectile {
	int maxTicks = 100;
	float dmgMult;
	LivingEntity lockOnEntity;
	ResourceKey<DamageType> damageType;

	public BaseMagicProjectile(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BaseMagicProjectile(EntityType<? extends ThrowableProjectile> type, LivingEntity player, Level world) {
		super(type, player, world);
	}

	public void setDamageType(ResourceKey<DamageType> type){
		this.damageType = type;
	}

	public float getTotalDamage(){
		float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
		return dmg * dmgMult;
	}

	public void damageEntity(LivingEntity e){
		if(e.level().isClientSide || damageType == null) //Client side might crash cause damage-related values are only set server-wide
			return;

		e.hurt(KKDamageTypes.getElementalDamage(damageType,this, this.getOwner()), getTotalDamage());
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		super.tick();
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
