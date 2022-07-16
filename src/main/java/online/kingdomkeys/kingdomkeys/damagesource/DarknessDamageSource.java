package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DarknessDamageSource extends MagicDamageSource {
    public DarknessDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super("dark", damageSourceEntityIn );
    }

    public static DamageSource getDarknessDamage(Player player){
        return new DarknessDamageSource("dark", player);
    }

}