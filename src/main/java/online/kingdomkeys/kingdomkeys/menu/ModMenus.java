package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.container.*;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.function.Supplier;

//NOTE: they call containers menus in mojang mappings

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, KingdomKeys.MODID);

    public static final Supplier<MenuType<SynthesisBagMenu>> SYNTHESIS_BAG = createMenu("synthesis_bag", SynthesisBagMenu::fromNetwork);
    public static final Supplier<MenuType<PedestalMenu>> PEDESTAL = createMenu("pedestal_container", PedestalMenu::new);
    public static final Supplier<MenuType<MagicalChestMenu>> MAGICAL_CHEST = createMenu("magical_chest", MagicalChestMenu::new);
    public static final Supplier<MenuType<GummiHangarMenu>> GUMMI_HANGAR = createMenu("gummi_hangar_container", GummiHangarMenu::new);
    public static final Supplier<MenuType<PauldronMenu>> PAULDRON = createMenu("pauldron", PauldronMenu::fromNetwork);

    public static <M extends AbstractContainerMenu> Supplier<MenuType<M>> createMenu(String name, IContainerFactory<M> container) {
        return MENUS.register(name, () -> new MenuType<>(container, FeatureFlags.DEFAULT_FLAGS));
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerGUIFactories(RegisterMenuScreensEvent event) {
        event.register(ModMenus.SYNTHESIS_BAG.get(), SynthesisBagScreen::new);
        event.register(ModMenus.PEDESTAL.get(), PedestalScreen::new);
        event.register(ModMenus.MAGICAL_CHEST.get(), MagicalChestScreen::new);
        event.register(ModMenus.GUMMI_HANGAR.get(), GummiHangarScreen::new);
        event.register(ModMenus.PAULDRON.get(), PauldronScreen::new);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.ItemHandler.ITEM, (object, context) -> new SynthesisBagInventory(object), ModItems.synthesisBag.get());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModEntities.TYPE_PEDESTAL.get(), (object, context) -> object.inventory.get());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModEntities.TYPE_MAGICAL_CHEST.get(), (object, context) -> object.inventory.get());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModEntities.TYPE_GUMMI_HANGAR.get(), (object, context) -> object.inventory.get());
        event.registerItem(Capabilities.ItemHandler.ITEM, (object, context) -> new PauldronInventory(object), ModItems.terra_Shoulder.get(), ModItems.aqua_Shoulder.get(), ModItems.ventus_Shoulder.get(), ModItems.eraqus_Shoulder.get(), ModItems.xehanort_Shoulder.get(), ModItems.nightmareVentus_Shoulder.get(), ModItems.ux_Shoulder.get());
    }

}
