package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
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

public class FireEntity extends BaseMagicProjectile {

	public FireEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public FireEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_FIRE.get(), player, world);
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
            float trackingSpeed = 40F;
			shoot(getDeltaMovement().x + x / trackingSpeed, getDeltaMovement().y + y / trackingSpeed, getDeltaMovement().z + z / trackingSpeed, 2F, 0);
		}
		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2) {
			float radius = 0.2F;
			for (int i = 0; i < 1; ++i) {
				double t = Math.random() * 360;
				double s = Math.random() * 360;
				double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double y = getY() + (radius * Math.cos(Math.toRadians(t)));
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
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}
					if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						target.setRemainingFireTicks(5);
						damageEntity(target);
					}
				}
			}

			if (brtResult != null) {
				BlockPos blockpos = brtResult.getBlockPos();
				BlockState blockstate = level().getBlockState(blockpos);

				((ServerLevel)level()).sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 30, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D,0.08);

				if(blockstate.getBlock() == Blocks.WET_SPONGE) {
					level().setBlockAndUpdate(blockpos, Blocks.SPONGE.defaultBlockState());
				}
				if (CampfireBlock.canLight(blockstate) || CandleBlock.canLight(blockstate) || CandleCakeBlock.canLight(blockstate)) {
					level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, true), 11);
				}
			}
			remove(RemovalReason.KILLED);
		}
	}
}
