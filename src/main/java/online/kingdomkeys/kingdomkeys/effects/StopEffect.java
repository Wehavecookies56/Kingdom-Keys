package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class StopEffect extends MobEffect {
    public StopEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            if (player.isCreative())
                return false;
        }
        pLivingEntity.setDeltaMovement(0, 0, 0);
        pLivingEntity.hurtMarked = true;

        if (pLivingEntity instanceof Mob m) {
            m.setTarget(null);
        }

        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}