package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
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
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class FiragaEntity extends BaseMagicProjectile {

	public FiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FiragaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		this(ModEntities.TYPE_FIRAGA.get(), world, player, dmgMult,lockOnEntity);
	}

	public FiragaEntity(EntityType<? extends FiragaEntity> entityType, Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(entityType, player, world);
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockOnEntity;
		setDamageType(KKDamageTypes.FIRE);
	}

	public List<SimpleParticleType> getParticles(){
		return List.of(ParticleTypes.FLAME);
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

		if(this.lockOnEntity != null && tickCount > 0) {
			double x = (this.lockOnEntity.getX() - this.getX());
			double y = (this.lockOnEntity.getY() - this.getY());
			double z = (this.lockOnEntity.getZ() - this.getZ());
			float trackingSpeed = 26F;
			shoot(getDeltaMovement().x + x / trackingSpeed, getDeltaMovement().y + y / trackingSpeed, getDeltaMovement().z + z / trackingSpeed, 2F, 0);
		}

		if(tickCount > 2) {
			float radius = 0.6F;
			for(int i = 0; i < 1; ++i) {
				double t = Math.random() * 360;
				double s = Math.random() * 360;
				double radT = Math.toRadians(t);
				double sinT = Math.sin(radT);
				double radS = Math.toRadians(s);
				double x = getX() + (radius * Math.cos(radS) * sinT);
				double z = getZ() + (radius * Math.sin(radS) * sinT);
				double y = getY() + (radius * Math.cos(radT));
				for (SimpleParticleType p : getParticles()) {
					level().addParticle(p, x, y, z, 0, 0, 0);
				}
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

			LivingEntity target = null;
			if(ertResult != null && ertResult.getEntity() instanceof LivingEntity t){
				target = t;
			}

			if (target != null) {
				if (target != getOwner()) {
					if (target.getEffect(ModMobEffects.FREEZE) != null) {
						target.removeEffect(ModMobEffects.FREEZE);
					}
					if (Utils.canHarm(getOwner(), target)) {
						target.setRemainingFireTicks(15);
						damageEntity(target);
						target.invulnerableTime = 0;
					}
				}
			}

			float radius = 1.5F;

			interactWithBlocks(rtRes, radius);

			List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
			list = Utils.removePartyMembersFromList((Player)getOwner(), list);
			if(target != null) { //If was direct impact remove the target from the explosion damage
				list.remove(target);
			}

			for(SimpleParticleType p : getParticles()) {
				((ServerLevel)level()).sendParticles(p, getX(), getY(), getZ(), 200/getParticles().size(), Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D,0.1);
			}

			remove(RemovalReason.KILLED);
		}
	}
}