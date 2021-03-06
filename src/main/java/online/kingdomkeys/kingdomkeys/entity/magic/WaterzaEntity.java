package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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

public class WaterzaEntity extends ThrowableEntity {

	int maxTicks = 100;
	PlayerEntity player;
	String caster;
	float dmgMult = 1;
	
	public WaterzaEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public WaterzaEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_WATERZA.get(), world);
	}

	public WaterzaEntity(World world) {
		super(ModEntities.TYPE_WATERZA.get(), world);
		this.preventEntitySpawning = true;
	}

	public WaterzaEntity(World world, PlayerEntity player, float dmgMult) {
		super(ModEntities.TYPE_WATERZA.get(), player, world);
		this.player = player;
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
	
	double a = 0;

	@Override
	public void tick() {
		for (PlayerEntity playerFromList : world.getPlayers()) {
			if(playerFromList.getDisplayName().getString().equals(getCaster())) {
				player = playerFromList;
				break;
			}
		}	
		
		if (this.ticksExisted > maxTicks || player == null) {
			this.remove();
		}
		
		if(ticksExisted <= 1) {
			this.setMotion(0, 0, 0);
			
		} else if (ticksExisted < 25) { //Shield
			setPosition(player.getPosX(), getPosY(), player.getPosZ());
    		float radius = 1.4F;
			double cx = getPosX();
			double cy = getPosY();
			double cz = getPosZ();

			a+=100; //Speed and distance between particles
			double x = cx + (radius * Math.cos(Math.toRadians(a)));
			double z = cz + (radius * Math.sin(Math.toRadians(a)));

			double x2 = cx + (radius * Math.cos(Math.toRadians(-a)));
			double z2 = cz + (radius * Math.sin(Math.toRadians(-a)));

			world.addParticle(ParticleTypes.DRIPPING_WATER, x, (cy+0.5) - a / 1080D, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.DOLPHIN, x2, (cy+0.5) - a / 1080D, z2, 0.0D, 0.0D, 0.0D);
			
			List<LivingEntity> list = Utils.getLivingEntitiesInRadiusExcludingParty(player, radius);

	        if (!list.isEmpty()) {
				float baseDmg = DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 0.6F;
				float dmg = this.getShooter() instanceof PlayerEntity ? baseDmg : 2;
	            for (int i = 0; i < list.size(); i++) {
	                Entity e = (Entity) list.get(i);
	                if (e instanceof LivingEntity) {
						e.attackEntityFrom(DamageSource.causeThrownDamage(this, (PlayerEntity) this.getShooter()), dmg);
	                }
	            }
	        }

		} else { //Projectile
			setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.PLAYERS, 1F, 1F);

			velocityChanged = true;
			float radius = 0.4F;
			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = getPosX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getPosZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getPosY() + (radius * Math.cos(Math.toRadians(t)));
					world.addParticle(ParticleTypes.DOLPHIN, x, y, z, 0, 0, 0);
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
						if(p == null || (p.getMember(target.getUniqueID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
							float dmg = this.getShooter() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 1.3F : 2;
							target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg * dmgMult);
						}
					}
				}
			}
			
			float radius = 6F;
			if (getShooter() instanceof PlayerEntity) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(this, radius);
				
				for(int r = 2; r <= radius; r+=2) {
					for (int t = 1; t < 360; t += 10) {
						for (int s = 1; s < 360 ; s += 10) {
							double x = getPosX() + (r * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double z = getPosZ() + (r * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double y = getPosY() + (r * Math.cos(Math.toRadians(t)));
							((ServerWorld) world).spawnParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 1, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.5);
						}
					}
				}


				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						LivingEntity e = list.get(i);
						if (e.isBurning()) {
							e.extinguish();
						} else {
							float baseDmg = DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 1.2F;
							float dmg = this.getShooter() instanceof PlayerEntity ? baseDmg : 2;
							e.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg * dmgMult);
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
		compound.putString("caster", this.getCaster());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setCaster(compound.getString("caster"));
	}

	private static final DataParameter<String> CASTER = EntityDataManager.createKey(MagnetEntity.class, DataSerializers.STRING);

	public String getCaster() {
		return caster;
	}

	public void setCaster(String name) {
		this.dataManager.set(CASTER, name);
		this.caster = name;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(CASTER)) {
			this.caster = this.getCasterDataManager();
		}
	}

	@Override
	protected void registerData() {
		this.dataManager.register(CASTER, "");
	}

	public String getCasterDataManager() {
		return this.dataManager.get(CASTER);
	}
}
