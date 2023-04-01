package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class FireDamageSource extends MagicDamageSource{

    public FireDamageSource(Holder<DamageType> damageTypeIn, Entity damageSourceEntityIn, Entity trueEntity) {
        super(damageTypeIn, damageSourceEntityIn, trueEntity);
    }
    
    public static DamageSource getFireDamage(Entity directEntity, Entity indirectEntity) {
		return directEntity.damageSources().source(KKDamageTypes.FIRE,directEntity,indirectEntity);        
    }

}