package online.kingdomkeys.kingdomkeys.synthesis.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModMaterials {

    public static DeferredRegister<Material> MATERIALS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "materials"), KingdomKeys.MODID);

    public static Supplier<IForgeRegistry<Material>> registry = MATERIALS.makeRegistry(RegistryBuilder::new);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        public static Material createMaterial(Supplier<Item> item, String name) {
            return new Material(KingdomKeys.MODID + ":" + Strings.SM_Prefix + name, item);
        }

        @SubscribeEvent
        public static void registerMaterials(RegisterEvent event) {
            List<Material> materialList = Arrays.asList(
                    createMaterial(ModItems.soothing_crystal, Strings.SM_SoothingCrystal),
                    createMaterial(ModItems.soothing_gem, Strings.SM_SoothingGem),
                    createMaterial(ModItems.soothing_stone, Strings.SM_SoothingStone),
                    createMaterial(ModItems.soothing_shard, Strings.SM_SoothingShard),
                    createMaterial(ModItems.wellspring_crystal, Strings.SM_WellspringCrystal),
                    createMaterial(ModItems.wellspring_gem, Strings.SM_WellspringGem),
                    createMaterial(ModItems.wellspring_shard, Strings.SM_WellspringShard),
                    createMaterial(ModItems.wellspring_stone, Strings.SM_WellspringStone),
                    createMaterial(ModItems.fluorite, Strings.SM_Fluorite),
                    createMaterial(ModItems.damascus, Strings.SM_Damascus),
                    createMaterial(ModItems.adamantite, Strings.SM_Adamantite),
                    createMaterial(ModItems.electrum, Strings.SM_Electrum),
                    createMaterial(ModItems.hungry_crystal, Strings.SM_HungryCrystal),
                    createMaterial(ModItems.hungry_gem, Strings.SM_HungryGem),
                    createMaterial(ModItems.hungry_shard, Strings.SM_HungryShard),
                    createMaterial(ModItems.hungry_stone, Strings.SM_HungryStone),
                    createMaterial(ModItems.blazing_crystal, Strings.SM_BlazingCrystal),
                    createMaterial(ModItems.blazing_gem, Strings.SM_BlazingGem),
                    createMaterial(ModItems.blazing_shard, Strings.SM_BlazingShard),
                    createMaterial(ModItems.blazing_stone, Strings.SM_BlazingStone),
                    createMaterial(ModItems.lightning_crystal, Strings.SM_LightningCrystal),
                    createMaterial(ModItems.lightning_gem, Strings.SM_LightningGem),
                    createMaterial(ModItems.lightning_shard, Strings.SM_LightningShard),
                    createMaterial(ModItems.lightning_stone, Strings.SM_LightningStone),
                    createMaterial(ModItems.lucid_crystal, Strings.SM_LucidCrystal),
                    createMaterial(ModItems.lucid_gem, Strings.SM_LucidGem),
                    createMaterial(ModItems.lucid_shard, Strings.SM_LucidShard),
                    createMaterial(ModItems.lucid_stone, Strings.SM_LucidStone),
                    createMaterial(ModItems.tranquility_crystal, Strings.SM_TranquilityCrystal),
                    createMaterial(ModItems.tranquility_gem, Strings.SM_TranquilityGem),
                    createMaterial(ModItems.tranquility_shard, Strings.SM_TranquilityShard),
                    createMaterial(ModItems.tranquility_stone, Strings.SM_TranquilityStone),
                    createMaterial(ModItems.twilight_crystal, Strings.SM_TwilightCrystal),
                    createMaterial(ModItems.twilight_gem, Strings.SM_TwilightGem),
                    createMaterial(ModItems.twilight_shard, Strings.SM_TwilightShard),
                    createMaterial(ModItems.twilight_stone, Strings.SM_TwilightStone),
                    createMaterial(ModItems.remembrance_crystal, Strings.SM_RemembranceCrystal),
                    createMaterial(ModItems.remembrance_gem, Strings.SM_RemembranceGem),
                    createMaterial(ModItems.remembrance_shard, Strings.SM_RemembranceShard),
                    createMaterial(ModItems.remembrance_stone, Strings.SM_RemembranceStone),
                    createMaterial(ModItems.writhing_crystal, Strings.SM_WrithingCrystal),
                    createMaterial(ModItems.writhing_gem, Strings.SM_WrithingGem),
                    createMaterial(ModItems.writhing_shard, Strings.SM_WrithingShard),
                    createMaterial(ModItems.writhing_stone, Strings.SM_WrithingStone),
                    createMaterial(ModItems.betwixt_crystal, Strings.SM_BetwixtCrystal),
                    createMaterial(ModItems.betwixt_gem, Strings.SM_BetwixtGem),
                    createMaterial(ModItems.betwixt_shard, Strings.SM_BetwixtShard),
                    createMaterial(ModItems.betwixt_stone, Strings.SM_BetwixtStone),
                    createMaterial(ModItems.frost_crystal, Strings.SM_FrostCrystal),
                    createMaterial(ModItems.frost_gem, Strings.SM_FrostGem),
                    createMaterial(ModItems.frost_shard, Strings.SM_FrostShard),
                    createMaterial(ModItems.frost_stone, Strings.SM_FrostStone),
                    createMaterial(ModItems.mythril_crystal, Strings.SM_MythrilCrystal),
                    createMaterial(ModItems.mythril_gem, Strings.SM_MythrilGem),
                    createMaterial(ModItems.mythril_shard, Strings.SM_MythrilShard),
                    createMaterial(ModItems.mythril_stone, Strings.SM_MythrilStone),
                    createMaterial(ModItems.pulsing_crystal, Strings.SM_PulsingCrystal),
                    createMaterial(ModItems.pulsing_gem, Strings.SM_PulsingGem),
                    createMaterial(ModItems.pulsing_shard, Strings.SM_PulsingShard),
                    createMaterial(ModItems.pulsing_stone, Strings.SM_PulsingStone),
                    createMaterial(ModItems.orichalcumplus, Strings.SM_OrichalcumPlus),
                    createMaterial(ModItems.orichalcum, Strings.SM_Orichalcum),
                    createMaterial(ModItems.manifest_illusion, Strings.SM_ManifestIllusion),
                    createMaterial(ModItems.lost_illusion, Strings.SM_LostIllusion),
                    createMaterial(ModItems.stormy_crystal, Strings.SM_StormyCrystal),
                    createMaterial(ModItems.stormy_gem, Strings.SM_StormyGem),
                    createMaterial(ModItems.stormy_stone, Strings.SM_StormyStone),
                    createMaterial(ModItems.stormy_shard, Strings.SM_StormyShard),
                    createMaterial(ModItems.sinister_crystal, Strings.SM_SinisterCrystal),
                    createMaterial(ModItems.sinister_gem, Strings.SM_SinisterGem),
                    createMaterial(ModItems.sinister_stone, Strings.SM_SinisterStone),
                    createMaterial(ModItems.sinister_shard, Strings.SM_SinisterShard),
                    createMaterial(ModItems.evanescent_crystal, Strings.SM_EvanescentCrystal),
                    createMaterial(ModItems.illusory_crystal, Strings.SM_IllusoryCrystal),

                    createMaterial(ModItems.incompleteKibladeChain, Strings.incompleteKibladeChain),
                    createMaterial(ModItems.mirageSplitChain, Strings.mirageSplitChain),
                    createMaterial(ModItems.nightmaresEndChain, Strings.nightmaresEndChain)
            );
            event.register(registry.get().getRegistryKey(), helper -> {
                materialList.forEach(material -> helper.register(material.getRegistryName().getPath(), material));
            });

        }

    }

}
