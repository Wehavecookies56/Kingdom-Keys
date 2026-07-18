package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
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
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class BlizzazaEntity extends BaseMagicProjectile {
	int freezeTime;

	public BlizzazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BlizzazaEntity(Level world, LivingEntity player, float dmgMult, int freezeTime) {
		super(ModEntities.TYPE_BLIZZAZA.get(), player, world);
		this.dmgMult = dmgMult;
		this.freezeTime = freezeTime;
		setDamageType(KKDamageTypes.ICE);
	}

	float radius = 6F;

	@Override
	public void tick() {
		if(ModConfigs.blizzardChangeBlocks && !level().isClientSide && level().getBlockState(blockPosition()) != Blocks.AIR.defaultBlockState()) {
			for(int x=(int)(getX()-radius/2);x<getX()+radius/2;x++) {
				for(int y=(int)(getY());y<getY()+1;y++) {
					for(int z=(int)(getZ()-radius/2);z<getZ()+radius/2;z++) {
						if ((getX() - x) * (getX() - x) + (getY() - y) * (getY() - y) + (getZ() - z) * (getZ() - z) <= radius/2 * radius/2) {
							BlockPos blockpos = new BlockPos(x,y,z);
							BlockState blockstate = level().getBlockState(blockpos);
							if(blockstate == Blocks.WATER.defaultBlockState()){
								level().setBlockAndUpdate(blockpos, Blocks.ICE.defaultBlockState());
							} else if(blockstate == Blocks.LAVA.defaultBlockState()){
								level().setBlockAndUpdate(blockpos, Blocks.OBSIDIAN.defaultBlockState());
							}
						}
					}
				}
			}
			remove(RemovalReason.KILLED);
		}

		if (tickCount > 2) {
			float radius = 0.5F;
			for (int t = 1; t < 360; t += 50) {
				double radT = Math.toRadians(t);
				double sinT = Math.sin(radT);
				double y = getY() + (radius * Math.cos(radT));
				for (int s = 1; s < 360 ; s += 50) {
					double radS = Math.toRadians(s);
					double x = getX() + (radius * Math.cos(radS) * sinT);
					double z = getZ() + (radius * Math.sin(radS) * sinT);
					level().addParticle(ParticleTypes.CLOUD, x,y,z, 0, 0, 0);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		super.onHit(rtRes);
		if (!level().isClientSide) {
			if (rtRes instanceof EntityHitResult ertResult && ertResult.getEntity() instanceof LivingEntity target) {
				if (target != getOwner()) {
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}

					if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
						damageEntity(target);

						if (!target.isOnFire()) {
							MobEffectInstance freeze = target.getEffect(ModMobEffects.FREEZE);
							int duration = freezeTime;
							if (freeze != null) {
								duration += freeze.getDuration();
							}
							target.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, duration, 0, false, false));
						}
					}
				}
			}

			if (rtRes instanceof BlockHitResult brtResult) {
				BlockPos ogBlockPos = brtResult.getBlockPos();

				for(int x=(int)(ogBlockPos.getX()-radius);x<ogBlockPos.getX()+radius;x++) {
					for(int y=(int)(ogBlockPos.getY()-radius);y<ogBlockPos.getY()+radius;y++) {
						for(int z=(int)(ogBlockPos.getZ()-radius);z<ogBlockPos.getZ()+radius;z++) {
							BlockPos blockpos = new BlockPos(x,y,z);
							BlockState blockstate = level().getBlockState(blockpos);
							if(blockstate.hasProperty(BlockStateProperties.LIT))
								level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, false), 11);
						}
					}
				}
			}

			if (getOwner() instanceof Player player) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(this, radius);
				int r = 2;
				for (int t = 1; t < 360; t += 20) {
					double radT = Math.toRadians(t);
					double sinT = Math.sin(radT);
					double y = getY() + (r * Math.cos(radT));
					for (int s = 1; s < 360 ; s += 20) {
						double radS = Math.toRadians(s);
						double x = getX() + (r * Math.cos(radS) * sinT);
						double z = getZ() + (r * Math.sin(radS) * sinT);
						((ServerLevel) level()).sendParticles(ParticleTypes.CLOUD, x, y+1, z, 1, 0,0,0, 0);
					}
				}

				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerLevel) level()).sendParticles(ParticleTypes.CLOUD, getX(), getY()+i, getZ(), 3, 0,0,0, 0.2);
				}

				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerLevel) level()).sendParticles(ParticleTypes.CLOUD, getX()+i, getY(), getZ(), 3, 0,0,0, 0.2);
				}

				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerLevel) level()).sendParticles(ParticleTypes.CLOUD, getX(), getY(), getZ()+i, 3, 0,0,0, 0.2);
				}

				Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

				if (!list.isEmpty()) {
					for (LivingEntity e : list) {
						if (e.isOnFire()) {
							e.clearFire();
						} else {
							if (!Utils.isEntityInParty(casterParty, e) && e != getOwner()) {
								damageEntity(e);

								MobEffectInstance freeze = e.getEffect(ModMobEffects.FREEZE);
								int duration = freezeTime;
								if (freeze != null) {
									duration += freeze.getDuration();
								}

								e.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, duration, 0, false, false));
							}
						}
					}
				}
			}
			remove(RemovalReason.KILLED);
		}

	}
}