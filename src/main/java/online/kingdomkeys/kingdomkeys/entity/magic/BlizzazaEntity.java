package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.IceDamageSource;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class BlizzazaEntity extends ThrowableProjectile {

	int maxTicks = 120;
	float dmgMult = 1;
	public BlizzazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BlizzazaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_BLIZZAZA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	float radius = 6F;

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

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
				for (int s = 1; s < 360 ; s += 50) {
					double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getY() + (radius * Math.cos(Math.toRadians(t)));
					level().addParticle(ParticleTypes.CLOUD, x,y,z, 0, 0, 0);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			if (rtRes instanceof EntityHitResult ertResult && ertResult.getEntity() instanceof LivingEntity target) {
				if (target.isOnFire()) {
					target.clearFire();
				} else if (target != getOwner()) {
                    Party p = null;
                    if (getOwner() != null) {
                        p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
                    }
                    if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
                        float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 1.4F : 2;
                        target.hurt(IceDamageSource.getIceDamage(this, this.getOwner()), dmg * dmgMult);
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
					for (int s = 1; s < 360 ; s += 20) {
						double x = getX() + (r * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = getZ() + (r * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = getY() + (r * Math.cos(Math.toRadians(t)));
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
                                float baseDmg = DamageCalculation.getMagicDamage((Player) this.getOwner()) * 1.4F;
                                float dmg = this.getOwner() instanceof Player ? baseDmg : 2;
                                e.hurt(IceDamageSource.getIceDamage(this, player), dmg);
                            }
                        }
                    }
				}
			}
			remove(RemovalReason.KILLED);
		}

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

	}
}
