package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class ThundagaShotEntity extends BaseMagicProjectile {
	public ThundagaShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ThundagaShotEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		this(ModEntities.TYPE_THUNDAGASHOT.get(), world, player, dmgMult, lockOnEntity);
	}

	public ThundagaShotEntity(EntityType<? extends ThundagaShotEntity> entityType, Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(entityType, player, world);
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockOnEntity;
		setDamageType(KKDamageTypes.LIGHTNING);
	}

	@Override
	public void tick() {
		float radius = 0.5F;
		for (int i = 0; i < 1; ++i) {
			double t = Math.random() * 360;
			double s = Math.random() * 360;
			double radT = Math.toRadians(t);
			double sinT = Math.sin(radT);
			double radS = Math.toRadians(s);
			double x = getX() + (radius * Math.cos(radS) * sinT);
			double z = getZ() + (radius * Math.sin(radS) * sinT);
			double y = getY() + (radius * Math.cos(radT));
			level().addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0, 0);
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		//super.onHit(rtRes);
		if (!level().isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			LivingEntity target = null;
			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity t) {
				target = t;
			}

			if (target != null) {
				if (target != getOwner()) {
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}
					if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						damageEntity(target);
						target.invulnerableTime = 10;
						level().playSound(null, position().x(), position().y(), position().z(), ModSounds.zap.get(), SoundSource.PLAYERS, 1F, 0.8F);
					}
				}
			}

			super.onHit(rtRes);
			float radius = 2F;

			List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
			list = Utils.removePartyMembersFromList((Player) getOwner(), list);
			if (target != null) { //If was direct impact remove the target from the explosion damage
				list.remove(target);
			}

			for (int a = 0; a < 360; a += 5) {
				double angle = Math.toRadians(a);
				double dirX = Math.sin(angle);
				double dirZ = Math.cos(angle);

				double x = getX() + 1 * dirX;
				double z = getZ() + 1 * dirZ;

				((ServerLevel) level()).sendParticles(ParticleTypes.ELECTRIC_SPARK, x, getY() + 1, z, 0, dirX, 0, dirZ, 1);
			}
			level().playSound(null, position().x(), position().y(), position().z(), ModSounds.zap.get(), SoundSource.PLAYERS, 1F, 0.8F);

			if (!list.isEmpty()) {
				for (Entity e : list) {
					if (e instanceof LivingEntity ent) {
						damageEntity(ent);
					}
				}
			}

			remove(RemovalReason.KILLED);
		}
	}
}