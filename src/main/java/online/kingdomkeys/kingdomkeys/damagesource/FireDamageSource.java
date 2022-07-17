package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class FireDamageSource extends MagicDamageSource{

    public FireDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    public static DamageSource getFireDamage(Entity entity) {
        return new FireDamageSource(KKResistanceType.fire.toString(), entity);
    }

}