package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class CrawlingFiragaEntity extends FireEntity {
	public CrawlingFiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public CrawlingFiragaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_CRAWLINGFIRAGA.get(), world, player, dmgMult, lockOnEntity, Tier.FIRAGA);
	}

	@Override
	protected void onImpact(HitResult hit) {
		LivingEntity struck = struck(hit);
		// It crawls on through whoever it touches, only the ground makes it burst
		if (struck != null) {
			struck.removeEffect(ModMobEffects.FREEZE);
			if (canHarm(struck)) {
				struck.setRemainingFireTicks(15);
				damageEntity(struck);
				struck.invulnerableTime = 10;
				level().playSound(null, position().x(), position().y(), position().z(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
			}
			return;
		}

		burst(hit, null, 1.5F, 200);
		remove(RemovalReason.KILLED);
	}
}
