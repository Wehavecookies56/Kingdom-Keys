package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.container.PedestalScreen;
import online.kingdomkeys.kingdomkeys.client.gui.container.SynthesisBagScreen;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, KingdomKeys.MODID);

    public static final RegistryObject<ContainerType<?>>
        SYNTHESIS_BAG = createContainer("synthesis_bag", SynthesisBagContainer::fromNetwork),
        PEDESTAL = createContainer("pedestal_container", PedestalContainer::createContainerClientSide);
    ;

    public static <M extends Container> RegistryObject<ContainerType<?>> createContainer(String name, IContainerFactory<M> container) {
        ContainerType<M> newContainer = IForgeContainerType.create(container);
        RegistryObject<ContainerType<?>> result = CONTAINERS.register(name, newContainer.delegate);
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    public static <M extends Container, U extends Screen & IHasContainer<M>> void registerGUIFactory(ContainerType<M> container, ScreenManager.IScreenFactory<M, U> guiFactory) {
        ScreenManager.registerFactory(container, guiFactory);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerGUIFactories() {
        registerGUIFactory((ContainerType<SynthesisBagContainer>) (ModContainers.SYNTHESIS_BAG.get()), SynthesisBagScreen::new);
        registerGUIFactory((ContainerType<PedestalContainer>) (ModContainers.PEDESTAL.get()), PedestalScreen::new);
    }

}
