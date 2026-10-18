package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.ProjectileHits;

public class SeedBulletEntity extends ThrowableProjectile {

    private final int ticks = 80;

    public SeedBulletEntity(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public SeedBulletEntity(double x, double y, double z, Level worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), x, y, z, worldIn);
    }

    public SeedBulletEntity(LivingEntity livingEntityIn, Level worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), livingEntityIn, worldIn);
    }
    
    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide && getOwner() instanceof LivingEntity shooter) {
            LivingEntity target = ProjectileHits.target(result, shooter);
            if (target != null) {
                shooter.doHurtTarget(target);
            }
        }
        remove(RemovalReason.KILLED);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

    }

    @Override
    public void tick() {
    	if(this.tickCount >= ticks) {
    		this.remove(RemovalReason.KILLED);
    	}
    	super.tick();
    }
}
