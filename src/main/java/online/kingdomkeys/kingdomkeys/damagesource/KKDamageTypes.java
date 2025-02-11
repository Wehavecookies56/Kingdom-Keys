package online.kingdomkeys.kingdomkeys.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class KKDamageTypes {
	
	public static void bootstrap(BootstapContext<DamageType> context){
        context.register(KKDamageTypes.DARKNESS, new DamageType(KKResistanceType.darkness.toString(), 0.1F));
        context.register(KKDamageTypes.FIRE, new DamageType(KKResistanceType.fire.toString(), 0.1F));
        context.register(KKDamageTypes.LIGHTNING, new DamageType(KKResistanceType.lightning.toString(), 0.1F));
        context.register(KKDamageTypes.ICE, new DamageType(KKResistanceType.ice.toString(), 0.1F));
        context.register(KKDamageTypes.STOP, new DamageType("stop", 0.1F));
        context.register(KKDamageTypes.OFFHAND, new DamageType("offhand", 0.1F));
        context.register(KKDamageTypes.LIGHT, new DamageType(KKResistanceType.light.toString(),0.1F));
    }
	
    public static final ResourceKey<DamageType> DARKNESS = register(KKResistanceType.darkness.toString());
    public static final ResourceKey<DamageType> FIRE = register(KKResistanceType.fire.toString());
    public static final ResourceKey<DamageType> LIGHTNING = register(KKResistanceType.lightning.toString());
    public static final ResourceKey<DamageType> ICE = register(KKResistanceType.ice.toString());
    public static final ResourceKey<DamageType> STOP = register("stop");
    public static final ResourceKey<DamageType> OFFHAND = register("offhand");
    public static final ResourceKey<DamageType> LIGHT = register(KKResistanceType.light.toString());
    
    private static ResourceKey<DamageType> register(String name){
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(KingdomKeys.MODID, name));
    }

    public static DamageSource getElementalDamage(ResourceKey<DamageType> element, Entity directEntity, Entity indirectEntity) {
        return directEntity.damageSources().source(element, directEntity, indirectEntity);
    }

}
