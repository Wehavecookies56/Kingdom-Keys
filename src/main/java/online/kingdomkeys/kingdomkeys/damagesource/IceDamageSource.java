package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class IceDamageSource extends MagicDamageSource{

    public IceDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    public static DamageSource getIceDamage(Entity entity) {
        return new IceDamageSource(KKResistanceType.ice.toString(), entity);
    }

}