package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class LightningDamageSource extends MagicDamageSource{
    public LightningDamageSource(Holder<DamageType> damageTypeIn, Entity damageSourceEntityIn, Entity trueEntity) {
        super(damageTypeIn, damageSourceEntityIn, trueEntity);
    }

    public static DamageSource getLightningDamage(Entity directEntity, Entity indirectEntity) {
		return directEntity.damageSources().source(KKDamageTypes.LIGHTNING,directEntity,indirectEntity);
    }
}
