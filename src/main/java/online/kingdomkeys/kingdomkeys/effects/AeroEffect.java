package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class AeroEffect extends MobEffect {
    public AeroEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        //use amplifier for magic level
        switch(pAmplifier) {
            case 0:

                break;
            case 1:
                if (pLivingEntity.tickCount % 20 == 0) {
                    float radius = 0.4F;
                    List<LivingEntity> list = Utils.getLivingEntitiesInRadius(pLivingEntity, radius);
                    if (!list.isEmpty()) {
                        for (Entity e : list) {
                            if (pLivingEntity instanceof Player player) {
                                e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player) * 0.033F);
                            }
                        }
                    }
                }
                break;
            case 2:
                if (pLivingEntity.tickCount % 10 == 0) {
                    float radius = 0.6F;
                    List<LivingEntity> list = Utils.getLivingEntitiesInRadius(pLivingEntity, radius);
                    if (!list.isEmpty()) {
                        for (Entity e : list) {
                            if (pLivingEntity instanceof Player player) {
                                e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player) * 0.066F);
                            }
                        }
                    }
                }
                break;
            case 3:

                break;
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}