package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class LightningDamageSource extends MagicDamageSource{
    public LightningDamageSource(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    public static DamageSource getLightningDamage(Player player) {
        return new LightningDamageSource("lightning", player);
    }

}
