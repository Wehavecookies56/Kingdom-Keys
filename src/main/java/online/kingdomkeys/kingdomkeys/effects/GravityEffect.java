package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.GlobalData;

public class GravityEffect extends MobEffect {
    public GravityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            if (player.getForcedPose() != Pose.SWIMMING) {
                player.setForcedPose(Pose.SWIMMING);
            }
        }
        pLivingEntity.setDeltaMovement(0, -4, 0);
        pLivingEntity.hurtMarked = true;
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}