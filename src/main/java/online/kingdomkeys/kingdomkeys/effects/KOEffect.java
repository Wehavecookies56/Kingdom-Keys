package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class KOEffect extends MobEffect {
    public KOEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        super.applyEffectTick(entity, pAmplifier);
        if(entity.tickCount % 20 == 0) {
            if(entity.getHealth() - 1 <= 0) {
                entity.kill();
            } else {
                entity.setHealth(entity.getHealth() - 1);
            }
        }
        entity.setYRot(0);
        entity.setYBodyRot(0);
        entity.setXRot(0);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
