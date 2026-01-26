package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class KOEffect extends MobEffect {

    public KOEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity.tickCount % 20 == 0) {
            if(livingEntity.getHealth() - 1 <= 0) {
                livingEntity.kill();
                return false;
            } else {
                livingEntity.setHealth(livingEntity.getHealth() - 1);
            }
        }
        livingEntity.setYRot(0);
        livingEntity.setYBodyRot(0);
        livingEntity.setXRot(0);
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
