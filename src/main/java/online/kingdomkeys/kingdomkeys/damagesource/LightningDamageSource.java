package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class LightningDamageSource extends MagicDamageSource{
    public LightningDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    public static DamageSource getLightningDamage(Entity entity) {
        return new LightningDamageSource(KKResistanceType.ice.toString(), entity);
    }

}
