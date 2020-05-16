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
                    new Material("kingdomkeys:material_apple" , Items.APPLE),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingCrystal, ModItems.blazingCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingGem, ModItems.blazingGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingShard, ModItems.blazingShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BlazingStone, ModItems.blazingStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningCrystal, ModItems.lightningCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningGem, ModItems.lightningGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningShard, ModItems.lightningShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LightningStone, ModItems.lightningStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidCrystal, ModItems.lucidCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidGem, ModItems.lucidGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidShard, ModItems.lucidShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LucidStone, ModItems.lucidStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilCrystal, ModItems.tranquilCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilGem, ModItems.tranquilGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilShard, ModItems.tranquilShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TranquilStone, ModItems.tranquilStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyCrystal, ModItems.energyCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyGem, ModItems.energyGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyShard, ModItems.energyShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_EnergyStone, ModItems.energyStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightCrystal, ModItems.twilightCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightGem, ModItems.twilightGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightShard, ModItems.twilightShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_TwilightStone, ModItems.twilightStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceCrystal, ModItems.remembranceCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceGem, ModItems.remembranceGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceShard, ModItems.remembranceShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_RemembranceStone, ModItems.remembranceStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityCrystal, ModItems.serenityCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityGem, ModItems.serenityGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityShard, ModItems.serenityShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_SerenityStone, ModItems.serenityStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkCrystal, ModItems.darkCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkGem, ModItems.darkGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkShard, ModItems.darkShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DarkStone, ModItems.darkStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseCrystal, ModItems.denseCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseGem, ModItems.denseGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseShard, ModItems.denseShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_DenseStone, ModItems.denseStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightCrystal, ModItems.brightCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightGem, ModItems.brightGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightShard, ModItems.brightShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_BrightStone, ModItems.brightStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostCrystal, ModItems.frostCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostGem, ModItems.frostGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostShard, ModItems.frostShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_FrostStone, ModItems.frostStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilCrystal, ModItems.mythrilCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilGem, ModItems.mythrilGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilShard, ModItems.mythrilShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_MythrilStone, ModItems.mythrilStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerCrystal, ModItems.powerCrystal.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerGem, ModItems.powerGem.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerShard, ModItems.powerShard.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_PowerStone, ModItems.powerStone.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_OrichalcumPlus, ModItems.orichalcumPlus.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_Orichalcum, ModItems.orichalcum.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_ManifestIllusion, ModItems.manifestIllusion.get()),
                    new Material(KingdomKeys.MODID+":mat_" + Strings.SM_LostIllusion, ModItems.lostIllusion.get()));
        }

    }

}
