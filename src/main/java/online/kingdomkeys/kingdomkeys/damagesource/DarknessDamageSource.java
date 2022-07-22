package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class DarknessDamageSource extends MagicDamageSource {
    public DarknessDamageSource(String damageTypeIn, Entity damageSourceEntityIn, Entity trueEntity) {
        super(damageTypeIn, damageSourceEntityIn, trueEntity);
    }

    public static DamageSource getDarknessDamage(Entity directEntity, Entity indirectEntity) {
        return new DarknessDamageSource(KKResistanceType.darkness.toString(), directEntity, indirectEntity);
    }

}