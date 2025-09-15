package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FreezeEffect extends MobEffect {
    public FreezeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        pLivingEntity.setIsInPowderSnow(true);
        if (pLivingEntity.getRandom().nextInt(10) < 2) {
            double d0 = pLivingEntity.getX() + 0.5D + pLivingEntity.getRandom().nextDouble() * 3.0D / 16.0D;
            double d1 = pLivingEntity.getY() + 1D;
            double d2 = pLivingEntity.getZ() + 0.5D + pLivingEntity.getRandom().nextDouble() / 16.0D;
            double d4 = pLivingEntity.getRandom().nextDouble() * 0.6D - 0.3D;
            double d5 = pLivingEntity.getRandom().nextDouble() * 0.6D - 0.3D;
            if (pLivingEntity.level().isClientSide) {
                pLivingEntity.level().addParticle(ParticleTypes.SNOWFLAKE, d0 + d5, d1, d2 + d4, 0.0D, 0.05D, 0.0D);
            } else {
                ServerLevel serverLevel = (ServerLevel) pLivingEntity.level();
                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, pLivingEntity.getX() + (pLivingEntity.getRandom().nextDouble() * 0.5D), d1, pLivingEntity.getZ() + (pLivingEntity.getRandom().nextDouble() * 0.5D), 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
