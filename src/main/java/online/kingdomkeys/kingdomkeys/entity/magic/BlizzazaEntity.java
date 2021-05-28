package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class BlizzazaEntity extends ThrowableEntity {

	int maxTicks = 120;
	float dmgMult = 1;
	public BlizzazaEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public BlizzazaEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_BLIZZAZA.get(), world);
	}

	public BlizzazaEntity(World world) {
		super(ModEntities.TYPE_BLIZZAZA.get(), world);
		this.preventEntitySpawning = true;
	}

	public BlizzazaEntity(World world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_BLIZZAZA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		if (world.getBlockState(getPosition()).getBlockState() == Blocks.WATER.getDefaultState()) {
			world.setBlockState(getPosition(), Blocks.ICE.getDefaultState());
			remove();
		} else if(world.getBlockState(getPosition()).getBlockState() == Blocks.LAVA.getDefaultState()){
			world.setBlockState(getPosition(), Blocks.OBSIDIAN.getDefaultState());
			remove();
		}

		if (ticksExisted > 2) {
			float radius = 0.5F;
			for (int t = 1; t < 360; t += 50) {
				for (int s = 1; s < 360 ; s += 50) {
					double x = getPosX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getPosZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getPosY() + (radius * Math.cos(Math.toRadians(t)));
					world.addParticle(ParticleTypes.CLOUD, x,y,z, 0, 0, 0);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {

			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target.isBurning()) {
					target.extinguish();
				} else {
					if (target != getShooter()) {
						Party p = null;
						if (getShooter() != null) {
							p = ModCapabilities.getWorld(getShooter().world).getPartyFromMember(getShooter().getUniqueID());
						}
						if (p == null || (p.getMember(target.getUniqueID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
							float dmg = this.getShooter() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 1.2F : 2;
							target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg * dmgMult);
						}
					}
				}
			}
			
			float radius = 6F;
			if (getShooter() instanceof PlayerEntity) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(this, radius);
				int r = 2;
				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360 ; s += 20) {
						double x = getPosX() + (r * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = getPosZ() + (r * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = getPosY() + (r * Math.cos(Math.toRadians(t)));
						((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, x, y+1, z, 1, 0,0,0, 0);
					}
				}
				
				
				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, getPosX(), getPosY()+i, getPosZ(), 3, 0,0,0, 0.2);
				}
				
				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, getPosX()+i, getPosY(), getPosZ(), 3, 0,0,0, 0.2);
				}
				
				for(float i = -5; i <= 5; i+=0.5F) {
					((ServerWorld) world).spawnParticle(ParticleTypes.CLOUD, getPosX(), getPosY(), getPosZ()+i, 3, 0,0,0, 0.2);
				}


				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						LivingEntity e = list.get(i);
						if (e.isBurning()) {
							e.extinguish();
						} else {
							float baseDmg = DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 1.2F;
							float dmg = this.getShooter() instanceof PlayerEntity ? baseDmg : 2;
							e.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg);
						}
					}
				}
			}
			remove();
		}

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}
}
