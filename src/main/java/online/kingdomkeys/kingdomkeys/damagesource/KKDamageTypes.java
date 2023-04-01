package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public interface KKDamageTypes {
    ResourceKey<DamageType> DARKNESS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KKResistanceType.darkness.toString()));
    ResourceKey<DamageType> FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KKResistanceType.fire.toString()));
    ResourceKey<DamageType> LIGHTNING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KKResistanceType.lightning.toString()));
    ResourceKey<DamageType> ICE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KKResistanceType.ice.toString()));
    //ResourceKey<DamageType> STOP = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KKResistanceType.));

}
