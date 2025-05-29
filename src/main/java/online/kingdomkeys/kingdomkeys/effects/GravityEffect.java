package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;

public class GravityEffect extends MobEffect {
    public GravityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
        if (pLivingEntity instanceof Player player) {
            if (player.getForcedPose() != Pose.SWIMMING) {
                player.setForcedPose(Pose.SWIMMING);
            }

        }

        pLivingEntity.setDeltaMovement(0, -4, 0);
        pLivingEntity.hurtMarked = true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        IGlobalCapabilities globalData = ModCapabilities.getGlobal(pLivingEntity);
        PacketHandler.syncToAllAround(pLivingEntity, globalData);

        if (pLivingEntity instanceof ServerPlayer player) {
            PacketHandler.sendTo(new SCRecalculateEyeHeight(), player);
        }

        if (pLivingEntity instanceof Player pl) {
            if (pl.getForcedPose() != null && !ModCapabilities.getPlayer(pl).getIsGliding()) {
                pl.setForcedPose(null);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
