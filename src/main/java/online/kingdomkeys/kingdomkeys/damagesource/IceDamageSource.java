package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class IceDamageSource extends MagicDamageSource{

    public IceDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    public static DamageSource getIceDamage(Player player) {
        return new IceDamageSource("ice", player);
    }

}