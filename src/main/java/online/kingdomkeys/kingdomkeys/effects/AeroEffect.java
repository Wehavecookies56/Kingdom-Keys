package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class AeroEffect extends MobEffect {
    public AeroEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        switch(pAmplifier) {
            case 0:

                break;
            case 1:
                if (pLivingEntity.tickCount % 20 == 0) {
                    float radius = 0.4F;
                    hurtNearby(pLivingEntity, radius);
                }
                break;
            case 2:
                if (pLivingEntity.tickCount % 10 == 0) {
                    float radius = 0.6F;
                    hurtNearby(pLivingEntity, radius);
                }
                break;
            case 3:

                break;
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    void hurtNearby(LivingEntity pLivingEntity, float radius) {
        List<LivingEntity> list = Utils.getLivingEntitiesInRadius(pLivingEntity, radius);
        if (!list.isEmpty() && pLivingEntity instanceof Player player) {
            for (Entity e : list) {
                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR,player,player), DamageCalculation.getMagicDamage(player) * 0.01F);
                e.push(new Vec3(e.getX() - player.getX(),e.getY() - player.getY(),e.getZ() - player.getZ()).scale(1.1F));
            }
        }
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}