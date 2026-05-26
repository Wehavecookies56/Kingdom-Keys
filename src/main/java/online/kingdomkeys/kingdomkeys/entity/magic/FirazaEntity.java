package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

public class FirazaEntity extends BaseMagicProjectile {

	public FirazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public FirazaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_FIRAZA.get(), player, world);
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
            float trackingSpeed = 20F;
            shoot(getDeltaMovement().x + x / trackingSpeed, getDeltaMovement().y + y / trackingSpeed, getDeltaMovement().z + z / trackingSpeed, 2F, 0);
        }

        //world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2) {
			float radius = 1F;
			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getY() + (radius * Math.cos(Math.toRadians(t)));
					level().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
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
						target.setRemainingFireTicks(30);
						damageEntity(target);
					}
				}
			}
			
			float radius = 6F;
			
			if (brtResult != null) {
				BlockPos ogBlockPos = brtResult.getBlockPos();

				for(int x=(int)(ogBlockPos.getX()-radius);x<ogBlockPos.getX()+radius;x++) {
					for(int y=(int)(ogBlockPos.getY()-radius);y<ogBlockPos.getY()+radius;y++) {
						for(int z=(int)(ogBlockPos.getZ()-radius);z<ogBlockPos.getZ()+radius;z++) {
							BlockPos blockpos = new BlockPos(x,y,z);
							BlockState blockstate = level().getBlockState(blockpos);
							if(blockstate.getBlock() == Blocks.WET_SPONGE) {
								level().setBlockAndUpdate(blockpos, Blocks.SPONGE.defaultBlockState());
							}
							if(blockstate.hasProperty(BlockStateProperties.LIT)) {
								level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, true), 11);
							}
						}
					}
				}
			}
			
			if(getOwner() instanceof Player) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadiusExcludingParty((Player) getOwner(), radius);
	
				((ServerLevel)level()).sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 1000, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.3);
				
				if (!list.isEmpty()) {
                    for (Entity e : list) {
                        if (e instanceof LivingEntity ent) {
                            e.setRemainingFireTicks(25);
                            damageEntity(ent);

                            if (ent.getEffect(ModMobEffects.FREEZE) != null) {
                                ent.removeEffect(ModMobEffects.FREEZE);
                            }
                        }
                    }
				}
			}
			remove(RemovalReason.KILLED);
		}
	}
}
