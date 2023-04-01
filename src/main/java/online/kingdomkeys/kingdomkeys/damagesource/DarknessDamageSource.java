package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class DarknessDamageSource extends MagicDamageSource {
    public DarknessDamageSource(Holder<DamageType> damageTypeIn, Entity damageSourceEntityIn, Entity trueEntity) {
        super(damageTypeIn, damageSourceEntityIn, trueEntity);
    }

    public static DamageSource getDarknessDamage(Entity directEntity, Entity indirectEntity) {
        return new DarknessDamageSource(Registries.DAMAGE_TYPE, directEntity, indirectEntity);
    }

}