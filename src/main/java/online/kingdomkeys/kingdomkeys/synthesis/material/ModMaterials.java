package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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

        public static Material createMaterial(Item item, String name) {
            return new Material(KingdomKeys.MODID + ":" + Strings.SM_Prefix + name, item);
        }

        @SubscribeEvent
        public static void registerMaterials(RegistryEvent.Register<Material> event) {
            event.getRegistry().registerAll(
                    //new Material("kingdomkeys:material_apple", Items.APPLE),
                    createMaterial(ModItems.soothing_crystal.get(), Strings.SM_SoothingCrystal),
                    createMaterial(ModItems.soothing_gem.get(), Strings.SM_SoothingGem),
                    createMaterial(ModItems.soothing_stone.get(), Strings.SM_SoothingStone),
                    createMaterial(ModItems.soothing_shard.get(), Strings.SM_SoothingShard),
                    createMaterial(ModItems.wellspring_crystal.get(), Strings.SM_WellspringCrystal),
                    createMaterial(ModItems.wellspring_gem.get(), Strings.SM_WellspringGem),
                    createMaterial(ModItems.wellspring_shard.get(), Strings.SM_WellspringShard),
                    createMaterial(ModItems.wellspring_stone.get(), Strings.SM_WellspringStone),
                    createMaterial(ModItems.fluorite.get(), Strings.SM_Fluorite),
                    createMaterial(ModItems.damascus.get(), Strings.SM_Damascus),
                    createMaterial(ModItems.adamantite.get(), Strings.SM_Adamantite),
                    createMaterial(ModItems.electrum.get(), Strings.SM_Electrum),
                    createMaterial(ModItems.hungry_crystal.get(), Strings.SM_HungryCrystal),
                    createMaterial(ModItems.hungry_gem.get(), Strings.SM_HungryGem),
                    createMaterial(ModItems.hungry_shard.get(), Strings.SM_HungryShard),
                    createMaterial(ModItems.hungry_stone.get(), Strings.SM_HungryStone),
                    createMaterial(ModItems.blazing_crystal.get(), Strings.SM_BlazingCrystal),
                    createMaterial(ModItems.blazing_gem.get(), Strings.SM_BlazingGem),
                    createMaterial(ModItems.blazing_shard.get(), Strings.SM_BlazingShard),
                    createMaterial(ModItems.blazing_stone.get(), Strings.SM_BlazingStone),
                    createMaterial(ModItems.lightning_crystal.get(), Strings.SM_LightningCrystal),
                    createMaterial(ModItems.lightning_gem.get(), Strings.SM_LightningGem),
                    createMaterial(ModItems.lightning_shard.get(), Strings.SM_LightningShard),
                    createMaterial(ModItems.lightning_stone.get(), Strings.SM_LightningStone),
                    createMaterial(ModItems.lucid_crystal.get(), Strings.SM_LucidCrystal),
                    createMaterial(ModItems.lucid_gem.get(), Strings.SM_LucidGem),
                    createMaterial(ModItems.lucid_shard.get(), Strings.SM_LucidShard),
                    createMaterial(ModItems.lucid_stone.get(), Strings.SM_LucidStone),
                    createMaterial(ModItems.tranquility_crystal.get(), Strings.SM_TranquilityCrystal),
                    createMaterial(ModItems.tranquility_gem.get(), Strings.SM_TranquilityGem),
                    createMaterial(ModItems.tranquility_shard.get(), Strings.SM_TranquilityShard),
                    createMaterial(ModItems.tranquility_stone.get(), Strings.SM_TranquilityStone),
                    createMaterial(ModItems.twilight_crystal.get(), Strings.SM_TwilightCrystal),
                    createMaterial(ModItems.twilight_gem.get(), Strings.SM_TwilightGem),
                    createMaterial(ModItems.twilight_shard.get(), Strings.SM_TwilightShard),
                    createMaterial(ModItems.twilight_stone.get(), Strings.SM_TwilightStone),
                    createMaterial(ModItems.remembrance_crystal.get(), Strings.SM_RemembranceCrystal),
                    createMaterial(ModItems.remembrance_gem.get(), Strings.SM_RemembranceGem),
                    createMaterial(ModItems.remembrance_shard.get(), Strings.SM_RemembranceShard),
                    createMaterial(ModItems.remembrance_stone.get(), Strings.SM_RemembranceStone),
                    createMaterial(ModItems.writhing_crystal.get(), Strings.SM_WrithingCrystal),
                    createMaterial(ModItems.writhing_gem.get(), Strings.SM_WrithingGem),
                    createMaterial(ModItems.writhing_shard.get(), Strings.SM_WrithingShard),
                    createMaterial(ModItems.writhing_stone.get(), Strings.SM_WrithingStone),
                    createMaterial(ModItems.betwixt_crystal.get(), Strings.SM_BetwixtCrystal),
                    createMaterial(ModItems.betwixt_gem.get(), Strings.SM_BetwixtGem),
                    createMaterial(ModItems.betwixt_shard.get(), Strings.SM_BetwixtShard),
                    createMaterial(ModItems.betwixt_stone.get(), Strings.SM_BetwixtStone),
                    createMaterial(ModItems.frost_crystal.get(), Strings.SM_FrostCrystal),
                    createMaterial(ModItems.frost_gem.get(), Strings.SM_FrostGem),
                    createMaterial(ModItems.frost_shard.get(), Strings.SM_FrostShard),
                    createMaterial(ModItems.frost_stone.get(), Strings.SM_FrostStone),
                    createMaterial(ModItems.mythril_crystal.get(), Strings.SM_MythrilCrystal),
                    createMaterial(ModItems.mythril_gem.get(), Strings.SM_MythrilGem),
                    createMaterial(ModItems.mythril_shard.get(), Strings.SM_MythrilShard),
                    createMaterial(ModItems.mythril_stone.get(), Strings.SM_MythrilStone),
                    createMaterial(ModItems.pulsing_crystal.get(), Strings.SM_PulsingCrystal),
                    createMaterial(ModItems.pulsing_gem.get(), Strings.SM_PulsingGem),
                    createMaterial(ModItems.pulsing_shard.get(), Strings.SM_PulsingShard),
                    createMaterial(ModItems.pulsing_stone.get(), Strings.SM_PulsingStone),
                    createMaterial(ModItems.orichalcumplus.get(), Strings.SM_OrichalcumPlus),
                    createMaterial(ModItems.orichalcum.get(), Strings.SM_Orichalcum),
                    createMaterial(ModItems.manifest_illusion.get(), Strings.SM_ManifestIllusion),
                    createMaterial(ModItems.lost_illusion.get(), Strings.SM_LostIllusion),
                    createMaterial(ModItems.stormy_crystal.get(), Strings.SM_StormyCrystal),
                    createMaterial(ModItems.stormy_gem.get(), Strings.SM_StormyGem),
                    createMaterial(ModItems.stormy_stone.get(), Strings.SM_StormyStone),
                    createMaterial(ModItems.stormy_shard.get(), Strings.SM_StormyShard),
                    createMaterial(ModItems.sinister_crystal.get(), Strings.SM_SinisterCrystal),
                    createMaterial(ModItems.sinister_gem.get(), Strings.SM_SinisterGem),
                    createMaterial(ModItems.sinister_stone.get(), Strings.SM_SinisterStone),
                    createMaterial(ModItems.sinister_shard.get(), Strings.SM_SinisterShard),
                    createMaterial(ModItems.evanescent_crystal.get(), Strings.SM_EvanescentCrystal),
                    createMaterial(ModItems.illusory_crystal.get(), Strings.SM_IllusoryCrystal),
            		
                    createMaterial(ModItems.incompleteKibladeChain.get(), Strings.incompleteKibladeChain),
                    createMaterial(ModItems.mirageSplitChain.get(), Strings.mirageSplitChain),
                    createMaterial(ModItems.nightmaresEndChain.get(), Strings.nightmaresEndChain));
        }

    }

}
