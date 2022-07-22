package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class LightningDamageSource extends MagicDamageSource{
    public LightningDamageSource(String damageTypeIn, Entity damageSourceEntityIn, Entity trueEntity) {
        super(damageTypeIn, damageSourceEntityIn, trueEntity);
    }

    public static DamageSource getLightningDamage(Entity directEntity, Entity indirectEntity) {
        return new LightningDamageSource(KKResistanceType.lightning.toString(), directEntity, indirectEntity);
    }

}
