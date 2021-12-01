package online.kingdomkeys.kingdomkeys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.KKDimensionCommand;
import online.kingdomkeys.kingdomkeys.command.KKDriveLevelCommand;
import online.kingdomkeys.kingdomkeys.command.KKDrivePointsCommand;
import online.kingdomkeys.kingdomkeys.command.KKExpCommand;
import online.kingdomkeys.kingdomkeys.command.KKHeartsCommand;
import online.kingdomkeys.kingdomkeys.command.KKLevelCommand;
import online.kingdomkeys.kingdomkeys.command.KKMaterialCommand;
import online.kingdomkeys.kingdomkeys.command.KKMunnyCommand;
import online.kingdomkeys.kingdomkeys.command.KKPayMunnyCommand;
import online.kingdomkeys.kingdomkeys.command.KKRecipeCommand;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.datagen.DataGeneration;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.entity.MobSpawnings;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.LevelingDataLoader;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.MagicDataLoader;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.proxy.IProxy;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;
import online.kingdomkeys.kingdomkeys.proxy.ProxyServer;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeDataLoader;
import online.kingdomkeys.kingdomkeys.world.biome.ModBiomes;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;
import online.kingdomkeys.kingdomkeys.world.features.OreGeneration;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static KingdomKeys instance;

	public static final String MODID = "kingdomkeys";
	public static final String MODNAME = "Kingdom Keys";
	public static final String MODVER = "2.0.4.3";
	public static final String MCVER = "1.16.5";

	// The proxy instance created for the current dist double lambda prevents class being loaded on the other dist
	public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> ProxyServer::new);

	public static CreativeModeTab orgWeaponsGroup = new CreativeModeTab(Strings.organizationGroup) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.eternalFlames.get());
		}
	};
	public static CreativeModeTab keybladesGroup = new CreativeModeTab(Strings.keybladesGroup) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.kingdomKey.get());
		}
	};
	public static CreativeModeTab miscGroup = new CreativeModeTab(Strings.miscGroup) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModBlocks.normalBlox.get());
		}
	};
	
	public KingdomKeys() {
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModSounds.SOUNDS.register(modEventBus);
		ModEntities.TILE_ENTITIES.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);

        ModEntities.ENTITIES.register(modEventBus);

		ModFeatures.FEATURES.register(modEventBus);
		ModBiomes.BIOMES.register(modEventBus);
		//ModParticles.PARTICLES.register(modEventBus);

		modEventBus.addGenericListener(Feature.class, this::registerFeatures);
		modEventBus.addListener(this::setup);

		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.SERVER, ModConfigs.SERVER_SPEC);

		MinecraftForge.EVENT_BUS.register(this);

		MinecraftForge.EVENT_BUS.register(new DataGeneration());

		// Server
		MinecraftForge.EVENT_BUS.register(new EntityEvents());
		MinecraftForge.EVENT_BUS.register(new ModCapabilities());
	}

	private void setup(final FMLCommonSetupEvent event) {
		// Run setup on proxies
		proxy.setup(event);
		ModCapabilities.register();
		//ModBiomes.init();
		//ModDimensions.init();
		event.enqueueWork(PacketHandler::register);
		event.enqueueWork(ModEntities::registerAttributes);
		event.enqueueWork(ModEntities::registerPlacements);
		event.enqueueWork(ModDimensions::setupDimension);
		addMoogleHouse();

	}

	public void addMoogleHouse() {
		addPieceToPattern(new ResourceLocation("village/plains/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_plains"), 2);
		addPieceToPattern(new ResourceLocation("village/desert/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_desert"), 2);
		addPieceToPattern(new ResourceLocation("village/savanna/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_savanna"), 2);
		addPieceToPattern(new ResourceLocation("village/snowy/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_snowy"), 2);
		addPieceToPattern(new ResourceLocation("village/taiga/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_taiga"), 2);
	}

	public void addPieceToPattern(ResourceLocation pattern, ResourceLocation structure, int weight) {
		ResourceKey<StructureTemplatePool> key = ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, pattern);
		StructureTemplatePool pat = BuiltinRegistries.TEMPLATE_POOL.get(key);
	//	pat.rawTemplates.add(Pair.of(StructurePoolElement.legacy(structure.toString()).apply(Projection.RIGID), weight));
		Pools.register(pat);
	}

	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();

		KKMunnyCommand.register(dispatcher);
		KKRecipeCommand.register(dispatcher);
		KKMaterialCommand.register(dispatcher);
		KKLevelCommand.register(dispatcher);
		KKDriveLevelCommand.register(dispatcher);
		KKExpCommand.register(dispatcher);
		KKDimensionCommand.register(dispatcher);
		KKHeartsCommand.register(dispatcher);
		KKDrivePointsCommand.register(dispatcher);
		KKPayMunnyCommand.register(dispatcher);
	}

	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void addToBiome(BiomeLoadingEvent event) {
		MobSpawnings.registerSpawns(event);
		OreGeneration.generateOre(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void removeFromBiome(BiomeLoadingEvent event) {
		MobSpawnings.removeSpawns(event);
	}

	@SubscribeEvent
	public void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(new KeybladeDataLoader());
		event.addListener(new OrganizationDataLoader());
		event.addListener(new RecipeDataLoader());
		event.addListener(new DriveFormDataLoader());
		event.addListener(new MagicDataLoader());
		event.addListener(new LevelingDataLoader());
	}

	public void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
		ModFeatures.register();
		ModFeatures.registerConfiguredFeatures();
	}
}
