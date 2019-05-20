package online.kingdomkeys.kingdomkeys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.gui.GuiCommandMenu;
import online.kingdomkeys.kingdomkeys.client.gui.GuiDrive;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHP;
import online.kingdomkeys.kingdomkeys.client.gui.GuiMP;
import online.kingdomkeys.kingdomkeys.client.gui.GuiPlayerPortrait;
import online.kingdomkeys.kingdomkeys.corsair.CorsairTickHandler;
import online.kingdomkeys.kingdomkeys.corsair.KeyboardManager;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ItemKeyblade;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.proxy.ClientProxy;
import online.kingdomkeys.kingdomkeys.proxy.IProxy;
import online.kingdomkeys.kingdomkeys.proxy.ServerProxy;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;

@Mod("kingdomkeys")
public class KingdomKeys {

    public static final Logger LOGGER = LogManager.getLogger();

    public static KingdomKeys instance;
	public KeyboardManager keyboardManager;

    //The proxy instance created for the current dist double lambda prevents class being loaded on the other dist
    @SuppressWarnings("Convert2MethodRef")
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ItemGroup orgWeaponsGroup = new ItemGroup(Strings.organizationGroup) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModItems.eternalFlames);
            }
        };
    public static ItemGroup keybladesGroup = new ItemGroup(Strings.keybladesGroup) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.kingdomKey);
        }
    };
    public static ItemGroup miscGroup = new ItemGroup(Strings.miscGroup) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.normalBlox);
        }
    };

    public KingdomKeys() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		this.keyboardManager = new KeyboardManager();

        MinecraftForge.EVENT_BUS.register(this);
        //Client
        MinecraftForge.EVENT_BUS.register(new GuiCommandMenu());
        MinecraftForge.EVENT_BUS.register(new GuiPlayerPortrait());
        MinecraftForge.EVENT_BUS.register(new GuiHP());
        MinecraftForge.EVENT_BUS.register(new GuiMP());
        MinecraftForge.EVENT_BUS.register(new GuiDrive());
        MinecraftForge.EVENT_BUS.register(new InputHandler());
        MinecraftForge.EVENT_BUS.register(new CorsairTickHandler(keyboardManager));
		this.keyboardManager.showLogo();

        for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
            ClientRegistry.registerKeyBinding(key.getKeybind());

        //Server
        MinecraftForge.EVENT_BUS.register(new EntityEvents());
    }

    private void setup(final FMLCommonSetupEvent event) {
        //Run setup on proxies
        proxy.setup(event);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        this.registerResourceLoader(event.getServer().getResourceManager());
    }

    private void registerResourceLoader(final IReloadableResourceManager resourceManager) {
        resourceManager.addReloadListener(manager -> {
            KeybladeDataLoader.loadData(manager);
        });

        resourceManager.addReloadListener(manager -> {
            OrganizationDataLoader.loadData(manager);
        });
    }

    @SubscribeEvent
    public void hitEntity(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (player.getHeldItemMainhand().getItem() instanceof ItemKeyblade) {
                ItemKeyblade heldKeyblade = (ItemKeyblade) player.getHeldItemMainhand().getItem();
                //TODO add player's strength stat
                //TODO improved damage calculation
                event.setAmount(heldKeyblade.getStrength(heldKeyblade.getKeybladeLevel()));
            }
        }
    }

    public KeyboardManager getKeyboardManager() {
		return this.keyboardManager;
	}
}
