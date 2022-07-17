package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class DarknessDamageSource extends MagicDamageSource {
    public DarknessDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super("dark", damageSourceEntityIn );
    }

    public static DamageSource getDarknessDamage(Entity entity){
        return new DarknessDamageSource(KKResistanceType.ice.toString(), entity);
    }

}