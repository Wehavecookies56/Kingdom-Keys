package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
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
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class EntityMagnet extends ThrowableEntity {

	int maxTicks = 100;
	PlayerEntity player;

	public EntityMagnet(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public EntityMagnet(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_MAGNET, world);
	}

	public EntityMagnet(World world) {
		super(ModEntities.TYPE_MAGNET, world);
		this.preventEntitySpawning = true;
	}

	public EntityMagnet(World world, PlayerEntity player) {
		super(ModEntities.TYPE_MAGNET, player, world);
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
		
		if(ticksExisted >= 5) {
			this.setMotion(0, 0, 0);
			this.velocityChanged = true;
			
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, this.getBoundingBox().grow(8.0D, 4.0D, 8.0D).offset(-4.0D, -1.0D, -4.0D));
			//System.out.println(world.isRemote+" "+player);
	        if (!list.isEmpty()) {
	            for (int i = 0; i < list.size(); i++) {
	                Entity e = (Entity) list.get(i);
	                if (e instanceof LivingEntity) {
	            		//e.setMotion(motionIn);
	                	double d = e.getPosX() - getPosX();
						double d1 = e.getPosZ() - getPosZ();
						((LivingEntity) e).knockBack(e, 1, d, d1);
						if(e.getPosY() < this.getPosY()-0.5) {
							e.setMotion(0, 0.5F, 0);
						}
	                }
	            }
	        }
			
		}
		
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		
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
