package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class SeedBulletEntity extends ThrowableProjectile {

    private int ticks = 80;

    protected SeedBulletEntity(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public SeedBulletEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_SEED_BULLET.get(), world);
    }

    protected SeedBulletEntity(double x, double y, double z, Level worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), x, y, z, worldIn);
    }

    public SeedBulletEntity(LivingEntity livingEntityIn, Level worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), livingEntityIn, worldIn);
    }
    
    @Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
    
    @Override
    protected void onHit(HitResult result) {
    	if(!level.isClientSide) {
	        if (result instanceof EntityHitResult){
	        	Entity target = ((EntityHitResult) result).getEntity();
	            ((LivingEntity)getOwner()).doHurtTarget(target);
	        }
    	}
        remove(RemovalReason.KILLED);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
    	if(this.tickCount >= ticks) {
    		this.remove(RemovalReason.KILLED);
    	}
    	super.tick();
    }

    @Override
    protected void defineSynchedData() {

    }
}
