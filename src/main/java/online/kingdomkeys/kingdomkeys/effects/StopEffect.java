package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class StopEffect extends MobEffect {
    public StopEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        pLivingEntity.setDeltaMovement(0, 0, 0);
        pLivingEntity.hurtMarked = true;

        if (pLivingEntity instanceof Mob) {
            ((Mob) pLivingEntity).setTarget(null);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        IGlobalCapabilities globalData = ModCapabilities.getGlobal(pLivingEntity);
        if (pLivingEntity instanceof Mob) {
            ((Mob) pLivingEntity).setNoAi(false);
        }

        if (globalData.getStopDamage() > 0 && globalData.getStopCaster() != null) {
            pLivingEntity.hurt(StopDamageSource.getStopDamage(Utils.getPlayerByName(pLivingEntity.level(), globalData.getStopCaster().toLowerCase())), globalData.getStopDamage() / 2);
        }

        if (pLivingEntity instanceof ServerPlayer)
            PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayer) pLivingEntity);
        globalData.setStopDamage(0);
        globalData.setStopCaster(null);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
