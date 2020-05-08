package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModMaterials {

    //TODO should be an API thing
    public static IForgeRegistry<Material> registry;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        @SubscribeEvent
        public static void registerMaterialRegistry(RegistryEvent.NewRegistry event) {
            //Create material registry
            registry = new RegistryBuilder<Material>().setName(new ResourceLocation(KingdomKeys.MODID, "materials")).setType(Material.class).create();
        }

        @SubscribeEvent
        public static void registerMaterials(RegistryEvent.Register<Material> event) {
            //PLACEHOLDER MATERIAL
            event.getRegistry().registerAll(
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingCrystal, ModItems.blazingCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingGem, ModItems.blazingGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingShard, ModItems.blazingShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingStone, ModItems.blazingStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningCrystal, ModItems.lightningCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningGem, ModItems.lightningGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningShard, ModItems.lightningShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningStone, ModItems.lightningStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidCrystal, ModItems.lucidCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidGem, ModItems.lucidGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidShard, ModItems.lucidShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidStone, ModItems.lucidStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilCrystal, ModItems.tranquilCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilGem, ModItems.tranquilGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilShard, ModItems.tranquilShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilStone, ModItems.tranquilStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyCrystal, ModItems.energyCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyGem, ModItems.energyGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyShard, ModItems.energyShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyStone, ModItems.energyStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightCrystal, ModItems.twilightCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightGem, ModItems.twilightGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightShard, ModItems.twilightShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightStone, ModItems.twilightStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceCrystal, ModItems.remembranceCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceGem, ModItems.remembranceGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceShard, ModItems.remembranceShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceStone, ModItems.remembranceStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityCrystal, ModItems.serenityCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityGem, ModItems.serenityGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityShard, ModItems.serenityShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityStone, ModItems.serenityStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkCrystal, ModItems.darkCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkGem, ModItems.darkGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkShard, ModItems.darkShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkStone, ModItems.darkStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseCrystal, ModItems.denseCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseGem, ModItems.denseGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseShard, ModItems.denseShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseStone, ModItems.denseStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightCrystal, ModItems.brightCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightGem, ModItems.brightGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightShard, ModItems.brightShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightStone, ModItems.brightStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostCrystal, ModItems.frostCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostGem, ModItems.frostGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostShard, ModItems.frostShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostStone, ModItems.frostStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilCrystal, ModItems.mythrilCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilGem, ModItems.mythrilGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilShard, ModItems.mythrilShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilStone, ModItems.mythrilStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerCrystal, ModItems.powerCrystal),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerGem, ModItems.powerGem),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerShard, ModItems.powerShard),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerStone, ModItems.powerStone),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_OrichalcumPlus, ModItems.orichalcumPlus),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_Orichalcum, ModItems.orichalcum),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_ManifestIllusion, ModItems.manifestIllusion),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LostIllusion, ModItems.lostIllusion));
        }

    }

}
