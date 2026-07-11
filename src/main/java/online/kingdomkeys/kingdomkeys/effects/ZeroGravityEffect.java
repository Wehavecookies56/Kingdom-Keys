package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCZeroGravityPacket;

public class ZeroGravityEffect extends MobEffect {
    public ZeroGravityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    int maxDuration;
    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        MobEffectInstance effect = pLivingEntity.getEffect(ModMobEffects.ZERO_GRAVITY);
        maxDuration = Math.max(maxDuration, effect.getDuration());
        int actualDuration = maxDuration - effect.getDuration();
        if(actualDuration > 2) {
            pLivingEntity.setDeltaMovement(new Vec3(0, 0, 0));
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        livingEntity.addDeltaMovement(new Vec3(0,amplifier,0));
        livingEntity.setNoGravity(true);

        if(livingEntity instanceof ServerPlayer player) {
            PacketHandler.sendTo(new SCZeroGravityPacket(true), player);
        }
        maxDuration = 0;
        super.onEffectStarted(livingEntity, amplifier);
    }
}