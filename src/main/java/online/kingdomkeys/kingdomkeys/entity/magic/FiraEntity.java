package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class FiraEntity extends BaseMagicProjectile {

	public FiraEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public FiraEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_FIRA.get(), player, world);
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockOnEntity;
		setDamageType(KKDamageTypes.FIRE);
	}

	@Override
	public void tick() {
		if(this.lockOnEntity != null && tickCount > 0) {
			double x = (this.lockOnEntity.getX() - this.getX());
			double y = (this.lockOnEntity.getY() - this.getY());
			double z = (this.lockOnEntity.getZ() - this.getZ());
			float trackingSpeed = 32F;
			shoot(getDeltaMovement().x + x / trackingSpeed, getDeltaMovement().y + y / trackingSpeed, getDeltaMovement().z + z / trackingSpeed, 2F, 0);
		}

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2) {
			float radius = 0.4F;
			for(int i = 0; i < 1; ++i) {
				double t = Math.random() * 360;
				double s = Math.random() * 360;
				double radT = Math.toRadians(t);
				double sinT = Math.sin(radT);
				double radS = Math.toRadians(s);
				double x = getX() + (radius * Math.cos(radS) * sinT);
				double z = getZ() + (radius * Math.sin(radS) * sinT);
				double y = getY() + (radius * Math.cos(radT));
				level().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		super.onHit(rtRes);
		if (!level().isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity target) {
				if (target != getOwner()) {
					if (target.getEffect(ModMobEffects.FREEZE) != null) {
						target.removeEffect(ModMobEffects.FREEZE);
					}
					if (Utils.canHarm(getOwner(), target)) {
						target.setRemainingFireTicks(10);
						damageEntity(target);
					}
				}
			}

			if (brtResult != null) {
				((ServerLevel)level()).sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 90, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D,0.1);
			}

			interactWithBlocks(rtRes, 0);
			remove(RemovalReason.KILLED);
		}
	}

}