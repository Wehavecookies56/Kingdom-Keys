package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class SeedBulletEntity extends ThrowableProjectile {

    private LivingEntity ent;
    private int ticks = 80;

    protected SeedBulletEntity(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public SeedBulletEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_SEED_BULLET.get(), world);
    }

    protected SeedBulletEntity(double x, double y, double z, Level worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), x, y, z, worldIn);
    }

    public SeedBulletEntity(LivingEntity livingEntityIn, Level worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), livingEntityIn, worldIn);
        this.ent = livingEntityIn;
    }
    
    @Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
    
    @Override
    protected void onHit(HitResult result) {
        if (result instanceof EntityHitResult) {
            ((EntityHitResult) result).getEntity().hurt(DamageSource.thrown(this, ent), 6);
        }
        this.remove(false);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
    	if(this.tickCount >= ticks) {
    		this.remove(false);
    	}
    	super.tick();
    }

    @Override
    protected void defineSynchedData() {

    }
}
