package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class WatergaEntity extends BaseMagicProjectile {

	public WatergaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public WatergaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_WATERGA.get(), player, world);
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.WATER);
	}

	double a = 0;

	@Override
	public void tick() {
		if(getOwner() == null)
			return;

		if(tickCount <= 1) {
			this.setDeltaMovement(0, 0, 0);

		} else if (tickCount < 25) { //Shield
			setPos(getOwner().getX(), getY(), getOwner().getZ());
			double radius = 1.4D;
			double cx = getX();
			double cy = getY();
			double cz = getZ();

			a += 100; //Speed and distance between particles
			double radA = Math.toRadians(a);
			double cosA = Math.cos(radA);
			double sinA = Math.sin(radA);
			double x = cx + (radius * cosA);
			double z = cz + (radius * sinA);

			double x2 = cx + (radius * cosA);
			double z2 = cz + (radius * -sinA);

			if(!level().isClientSide) {
				((ServerLevel) level()).sendParticles(ParticleTypes.DRIPPING_WATER, x,  (cy+0.5) - a / 1080D, z, 1, 0,0,0, 0.5);
				((ServerLevel) level()).sendParticles(ParticleTypes.DOLPHIN, x2, (cy+0.5) - a / 1080D, z2, 1, 0,0,0, 0.5);
			}
			List<Entity> list = this.level().getEntities(getOwner(), getOwner().getBoundingBox().inflate(radius), Entity::isAlive);

			if (!list.isEmpty() && list.getFirst() != this) {
				for (Entity entity : list) {
					if (entity instanceof LivingEntity ent) {
						damageEntity(ent);
					}
				}
			}
		} else { //Projectile
			shootFromRotation(getOwner(), getOwner().getXRot(), getOwner().getYRot(), 0, 1.75F, 0);
			getOwner().level().playSound(null, getOwner().blockPosition(), SoundEvents.PLAYER_SWIM, SoundSource.PLAYERS, 1F, 1F);

			hurtMarked = true;
			float radius = 0.4F;
			for (int t = 1; t < 360; t += 30) {
				double radT = Math.toRadians(t);
				double sinT = Math.sin(radT);
				double y = getY() + (radius * Math.cos(radT));
				for (int s = 1; s < 360 ; s += 30) {
					double radS = Math.toRadians(s);
					double x = getX() + (radius * Math.cos(radS) * sinT);
					double z = getZ() + (radius * Math.sin(radS) * sinT);
					if(!level().isClientSide)
						((ServerLevel) level()).sendParticles(ParticleTypes.DOLPHIN, x, y, z, 1, 0,0,0, 0.5);
				}
			}

		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		super.onHit(rtRes);
		if (!level().isClientSide) {

			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity target) {

				if (target.isOnFire()) {
					target.clearFire();
				} else {
					if (target != getOwner()) {
						if (Utils.canHarm(getOwner(), target)) {
							damageEntity(target);
							remove(RemovalReason.KILLED);
						}
					}
				}
			}

			float radius = 2F;

			interactWithBlocks(rtRes, radius);

			if (getOwner() instanceof Player) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(this, radius);

				for (int t = 1; t < 360; t += 10) {
					double radT = Math.toRadians(t);
					double sinT = Math.sin(radT);
					double cosT = Math.cos(radT);
					for (int s = 1; s < 360 ; s += 10) {
						double radS = Math.toRadians(s);
						double cosS = Math.cos(radS);
						double sinS = Math.sin(radS);
						for(int r = 1; r <= radius; r++) {
							double x = getX() + (r * cosS * sinT);
							double z = getZ() + (r * sinS * sinT);
							double y = getY() + (r * cosT);
							((ServerLevel) level()).sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 1, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.5);
						}
					}
				}

				Party casterParty = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());

				if (!list.isEmpty()) {
					for (LivingEntity e : list) {
						if (e.isOnFire()) {
							e.clearFire();
						} else {
							if(!Utils.isEntityInParty(casterParty, e) && e != getOwner()) {
								damageEntity(e);
							}
						}
					}
				}
			}
			remove(RemovalReason.KILLED);
		}
	}
}