package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSyncGlobalCapability;

public class EntityGravity extends ThrowableEntity {

	int maxTicks = 100;
	PlayerEntity player;

	public EntityGravity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public EntityGravity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_GRAVITY, world);
	}

	public EntityGravity(World world) {
		super(ModEntities.TYPE_GRAVITY, world);
		this.preventEntitySpawning = true;
	}

	public EntityGravity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_GRAVITY, player, world);
		this.player = player;
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

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(ticksExisted > 2)
			world.addParticle(ParticleTypes.DRAGON_BREATH, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		int radius = 2;
		double freq = 0.5;
		double X = getPosX();
		double Y = getPosY();
		double Z = getPosZ();
		
		 for (double x = X - radius; x <= X + radius; x+=freq) {
            for (double y = Y - radius; y <= Y + radius; y+=freq) {
                for (double z = Z - radius; z <= Z + radius; z+=freq) {
                    if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radius * radius) {
                    	world.addParticle(ParticleTypes.DRAGON_BREATH, x, y+1, z, 0, 0, 0);
                    }
                }
            }
        }
		if (!world.isRemote) {
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, getBoundingBox().grow(2.0D, 2.0D, 2.0D).offset(-1.0D, -1.0D, -1.0D));
	        if (!list.isEmpty()) {
	            for (int i = 0; i < list.size(); i++) {
	                Entity e = (Entity) list.get(i);
	                if (e instanceof LivingEntity) {
	                	IGlobalCapabilities gProps = ModCapabilities.getGlobal((LivingEntity) e);
						gProps.setFlatTicks(100); // Just in case it goes below (shouldn't happen)
						
						if (e instanceof LivingEntity) //This should sync the state of this entity (player or mob) to all the clients around to stop render it flat
							PacketHandler.syncToAllAround((LivingEntity)e, gProps);
						
						e.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1);
	                }
	            }
	        }
	        
	        remove();
	        
			/*EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getThrower()) {
					IGlobalCapabilities gProps = ModCapabilities.getGlobal(target);
					gProps.setFlatTicks(100); // Just in case it goes below (shouldn't happen)
					
					if (target instanceof LivingEntity) //This should sync the state of this entity (player or mob) to all the clients around to stop render it flat
						PacketHandler.syncToAllAround(target, gProps);
					
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1);
					
					remove();
				}
			} else { // Block (not ERTR)
			
				remove();
			}*/
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
