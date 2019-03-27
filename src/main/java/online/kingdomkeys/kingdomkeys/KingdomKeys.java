package online.kingdomkeys.kingdomkeys;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.proxy.ClientProxy;
import online.kingdomkeys.kingdomkeys.proxy.IProxy;
import online.kingdomkeys.kingdomkeys.proxy.ServerProxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("kingdomkeys")
public class KingdomKeys {

    public static final Logger LOGGER = LogManager.getLogger();

    public static String MODID = "kingdomkeys";

    //The proxy instance created for the current dist double lambda prevents class being loaded on the other dist
    @SuppressWarnings("Convert2MethodRef")
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    //No organization weapons yet
    public static ItemGroup orgWeaponsGroup = new ItemGroup(Strings.organizationGroup) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModItems.kingdomKeyD);
            }
        };
    //Group for keyblades
    public static ItemGroup keybladesGroup = new ItemGroup(Strings.keybladesGroup) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.kingdomKey);
        }
    };
    //Group for most of the items/blocks in the mod
    public static ItemGroup miscGroup = new ItemGroup(Strings.miscGroup) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.normalBlox);
        }
    };

    public KingdomKeys() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        //Run setup on proxies
        proxy.setup(event);
    }
}
