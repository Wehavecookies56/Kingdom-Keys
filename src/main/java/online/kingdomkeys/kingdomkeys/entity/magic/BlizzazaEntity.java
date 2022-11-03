package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
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
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.IceDamageSource;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class BlizzazaEntity extends ThrowableProjectile {

	int maxTicks = 120;
	float dmgMult = 1;
	public BlizzazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BlizzazaEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_BLIZZAZA.get(), world);
	}

	public BlizzazaEntity(Level world) {
		super(ModEntities.TYPE_BLIZZAZA.get(), world);
		this.blocksBuilding = true;
	}

	public BlizzazaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_BLIZZAZA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if(ModConfigs.blizzardChangeBlocks && !level.isClientSide) {
			if (level.getBlockState(blockPosition()) == Blocks.WATER.defaultBlockState()) {
				level.setBlockAndUpdate(blockPosition(), Blocks.ICE.defaultBlockState());
				remove(RemovalReason.KILLED);
			} else if(level.getBlockState(blockPosition()) == Blocks.LAVA.defaultBlockState()){
				level.setBlockAndUpdate(blockPosition(), Blocks.OBSIDIAN.defaultBlockState());
				remove(RemovalReason.KILLED);
			}
		}


		if (tickCount > 2) {
			float radius = 0.5F;
			for (int t = 1; t < 360; t += 50) {
				for (int s = 1; s < 360 ; s += 50) {
					double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getY() + (radius * Math.cos(Math.toRadians(t)));
					level.addParticle(ParticleTypes.CLOUD, x,y,z, 0, 0, 0);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level.isClientSide) {

			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target.isOnFire()) {
					target.clearFire();
				} else {
					if (target != getOwner()) {
						Party p = null;
						if (getOwner() != null) {
							p = ModCapabilities.getWorld(getOwner().level).getPartyFromMember(getOwner().getUUID());
						}
						if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
							float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 1.4F : 2;
							target.hurt(IceDamageSource.getIceDamage(this, this.getOwner()), dmg * dmgMult);
						}
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
							BlockState blockstate = level.getBlockState(blockpos);
							if(blockstate.hasProperty(BlockStateProperties.LIT))
								level.setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(false)), 11);
						}
					}
				}
			}
			
			if (getOwner() instanceof Player) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(this, radius);
				int r = 2;
				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = getX() + (r * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = getZ() + (r * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = getY() + (r * Math.cos(Math.toRadians(t)));
						((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, x, y+1, z, 1, 0,0,0, 0);
					}
				}
				
				
				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, getX(), getY()+i, getZ(), 3, 0,0,0, 0.2);
				}
				
				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, getX()+i, getY(), getZ(), 3, 0,0,0, 0.2);
				}
				
				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, getX(), getY(), getZ()+i, 3, 0,0,0, 0.2);
				}

				Party casterParty = ModCapabilities.getWorld(getOwner().level).getPartyFromMember(getOwner().getUUID());

				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						LivingEntity e = list.get(i);
						if (e.isOnFire()) {
							e.clearFire();
						} else {
							if(!Utils.isEntityInParty(casterParty, e) && e != getOwner()) {
								float baseDmg = DamageCalculation.getMagicDamage((Player) this.getOwner()) * 1.4F;
								float dmg = this.getOwner() instanceof Player ? baseDmg : 2;
								e.hurt(IceDamageSource.getIceDamage(this, this.getOwner()), dmg);
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
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}
}
