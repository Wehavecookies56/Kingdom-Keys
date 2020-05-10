package online.kingdomkeys.kingdomkeys;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import online.kingdomkeys.kingdomkeys.worldgen.JigsawJank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.handler.CapabilityEventsHandler;
import online.kingdomkeys.kingdomkeys.handler.DataGeneration;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.proxy.IProxy;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;
import online.kingdomkeys.kingdomkeys.proxy.ProxyServer;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static KingdomKeys instance;

	public static final String MODID = "kingdomkeys";
	public static final String MODNAME = "Kingdom Keys";
	public static final String MODVER = "2.0";
	public static final String MCVER = "1.15.2";

	// The proxy instance created for the current dist double lambda prevents class
	// being loaded on the other dist
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(), () -> () -> new ProxyServer());

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

		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);


		MinecraftForge.EVENT_BUS.register(this);
		// Client
		MinecraftForge.EVENT_BUS.register(new ProxyClient());
		MinecraftForge.EVENT_BUS.register(new DataGeneration());

		// Server
		MinecraftForge.EVENT_BUS.register(new EntityEvents());
		MinecraftForge.EVENT_BUS.register(new CapabilityEventsHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		// Run setup on proxies
		proxy.setup(event);
		ModCapabilities.register();
		DeferredWorkQueue.runLater(() -> {
			PacketHandler.register();

		});

		List v = new ArrayList();
		v.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house"),1)));
		List s = new ArrayList();
		v.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_snowy"),1)));
		List t = new ArrayList();
		v.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_taiga"),1)));
		List sa = new ArrayList();
		v.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_savanna"),1)));
		List d = new ArrayList();
		v.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_desert"),1)));

		JigsawJank.create().append(new ResourceLocation("minecraft",  "village/plains/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() {
				return v;
			}
		});
		JigsawJank.create().append(new ResourceLocation("minecraft",  "village/desert/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() {
				return d;
			}
		});
		JigsawJank.create().append(new ResourceLocation("minecraft",  "village/savanna/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() {
				return sa;
			}
		});
		JigsawJank.create().append(new ResourceLocation("minecraft",  "village/taiga/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return t; }
		});
		JigsawJank.create().append(new ResourceLocation("minecraft",  "village/snowy/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return s; }
		});
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerAboutToStartEvent event) {
		this.registerResourceLoader(event.getServer().getResourceManager());
	}

	//TODO do this with non deprecated stuff, works for now
	private void registerResourceLoader(final IReloadableResourceManager resourceManager) {
		resourceManager.addReloadListener((IResourceManagerReloadListener)manager -> {
			KeybladeDataLoader.loadData(resourceManager);
			//OrganizationDataLoader.loadData(resourceManager);
		});
	}

	@SubscribeEvent
	public void hitEntity(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			if (player.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
				KeybladeItem heldKeyblade = (KeybladeItem) player.getHeldItemMainhand().getItem();
				// TODO add player's strength stat
				// TODO improved damage calculation
				event.setAmount(heldKeyblade.getStrength(heldKeyblade.getKeybladeLevel()));
			}
		}
	}
}
