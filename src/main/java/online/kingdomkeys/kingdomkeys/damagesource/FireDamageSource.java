package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class FireDamageSource extends MagicDamageSource{

    public FireDamageSource(String damageTypeIn, Entity damageSourceEntityIn, Entity trueEntity) {
        super(damageTypeIn, damageSourceEntityIn, trueEntity);
    }

    public static DamageSource getFireDamage(Entity directEntity, Entity indirectEntity) {
        return new FireDamageSource(KKResistanceType.fire.toString(), directEntity, indirectEntity);
    }

}