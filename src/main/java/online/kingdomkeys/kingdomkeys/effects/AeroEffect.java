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
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
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
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR,player,player), DamageCalculation.getMagicDamage(player) * 0.033F);
                                Vec3 vec = new Vec3(e.getX() - player.getX(), e.getY() - player.getY(), e.getZ() - player.getZ()).scale(1.1F);
                                e.push(vec.x,vec.y,vec.z);
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
                                e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR,player,player), DamageCalculation.getMagicDamage(player) * 0.066F);
                                Vec3 vec = new Vec3(e.getX() - player.getX(), e.getY() - player.getY(), e.getZ() - player.getZ()).scale(1.2F);
                                e.push(vec.x,vec.y,vec.z);

                            }
                        }
                    }
                }
                break;
            case 3:

                break;
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
