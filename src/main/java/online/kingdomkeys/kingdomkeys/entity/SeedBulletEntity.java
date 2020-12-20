package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class SeedBulletEntity extends ThrowableEntity {

    private LivingEntity ent;
    private int ticks = 30;

    protected SeedBulletEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public SeedBulletEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_SEED_BULLET.get(), world);
    }

    protected SeedBulletEntity(double x, double y, double z, World worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), x, y, z, worldIn);
    }

    public SeedBulletEntity(LivingEntity livingEntityIn, World worldIn) {
        super(ModEntities.TYPE_SEED_BULLET.get(), livingEntityIn, worldIn);
        this.ent = livingEntityIn;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result instanceof EntityRayTraceResult) {
            ((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, ent), 6);
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    public void tick() {
        if (ticks <= 0) {
            ticks = 30;
            this.remove();
        } else {
            ticks--;
        }
    }

    @Override
    protected void registerData() {

    }
}
