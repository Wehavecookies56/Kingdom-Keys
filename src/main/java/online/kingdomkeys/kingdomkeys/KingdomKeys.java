package online.kingdomkeys.kingdomkeys;

import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;
import online.kingdomkeys.kingdomkeys.world.features.OreGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.KKDriveLevelCommand;
import online.kingdomkeys.kingdomkeys.command.KKDrivePointsCommand;
import online.kingdomkeys.kingdomkeys.command.KKExpCommand;
import online.kingdomkeys.kingdomkeys.command.KKHeartsCommand;
import online.kingdomkeys.kingdomkeys.command.KKLevelCommand;
import online.kingdomkeys.kingdomkeys.command.KKMaterialCommand;
import online.kingdomkeys.kingdomkeys.command.KKMunnyCommand;
import online.kingdomkeys.kingdomkeys.command.KKRecipeCommand;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.datagen.DataGeneration;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.proxy.IProxy;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;
import online.kingdomkeys.kingdomkeys.proxy.ProxyServer;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeDataLoader;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static KingdomKeys instance;

	public static final String MODID = "kingdomkeys";
	public static final String MODNAME = "Kingdom Keys";
	public static final String MODVER = "2.0.1.1";
	public static final String MCVER = "1.16.4";

	// The proxy instance created for the current dist double lambda prevents class being loaded on the other dist
	public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> ProxyServer::new);

	public static ItemGroup orgWeaponsGroup = new ItemGroup(Strings.organizationGroup) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.eternalFlames.get());
		}
	};
	public static ItemGroup keybladesGroup = new ItemGroup(Strings.keybladesGroup) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.kingdomKey.get());
		}
	};
	public static ItemGroup miscGroup = new ItemGroup(Strings.miscGroup) {
		@Override
		public ItemStack createIcon() {
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
		//ModBiomes.BIOMES.register(modEventBus);
		//ModDimensions.DIMENSIONS.register(modEventBus);
		//ModParticles.PARTICLES.register(modEventBus);

		modEventBus.addListener(this::setup);

		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);

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
		addMoogleHouse();

	}

	public void addMoogleHouse() {
		//TODO figure out for 1.16
		/*
		List v = new ArrayList();
		v.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_plains"),2)));
		List s = new ArrayList();
		s.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_snowy"),2)));
		List t = new ArrayList();
		t.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_taiga"),2)));
		List sa = new ArrayList();
		sa.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_savanna"),2)));
		List d = new ArrayList();
		d.add((new Pair<>(new SingleJigsawPiece("kingdomkeys:village/moogle_house_desert"),2)));
		
		JigsawJank.create().append(new ResourceLocation("minecraft", "village/plains/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return v; }
		});
		JigsawJank.create().append(new ResourceLocation("minecraft", "village/desert/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return d; }
		});
		JigsawJank.create().append(new ResourceLocation("minecraft", "village/savanna/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return sa; }
		});
		JigsawJank.create().append(new ResourceLocation("minecraft", "village/taiga/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return t; }
		});
		JigsawJank.create().append(new ResourceLocation("minecraft", "village/snowy/houses"), new Supplier<List<Pair<JigsawPiece, Integer>>>() {
			@Override
			public List<Pair<JigsawPiece, Integer>> get() { return s; }
		});

		 */
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommandManager().getDispatcher();

		KKMunnyCommand.register(dispatcher);
		KKRecipeCommand.register(dispatcher);
		KKMaterialCommand.register(dispatcher);
		KKLevelCommand.register(dispatcher);
		KKDriveLevelCommand.register(dispatcher);
		KKExpCommand.register(dispatcher);
		//DimensionCommand.register(dispatcher);
		KKHeartsCommand.register(dispatcher);
		KKDrivePointsCommand.register(dispatcher);
	}

	@SubscribeEvent
	public void biomeLoad(BiomeLoadingEvent event) {
		OreGeneration.generateOre(event);
		if(event.getCategory() != Category.OCEAN) {
			if(event.getClimate().temperature >= 0.3 && event.getClimate().temperature <= 1.0) {
				event.getSpawns().getSpawner(ModEntities.TYPE_MOOGLE.get().getClassification()).add(new MobSpawnInfo.Spawners(ModEntities.TYPE_MOOGLE.get(), 2, 0, 1));
			}

			for(EntityType<?> entityType : ModEntities.pureblood) {
				event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, 2, 0, 1));
			}
			for(EntityType<?> entityType : ModEntities.emblem) {
				event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, 2, 0, 1));
			}
			for(EntityType<?> entityType : ModEntities.nobody) {
				event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, 2, 0, 1));
			}
		}
		if (event.getName().equals(new ResourceLocation(MODID, Strings.diveToTheHeart + "_biome"))) {
			//Remove all entity spawns added to the Dive to the Heart biome
			for (EntityClassification entityClassification : EntityClassification.values()) {
				event.getSpawns().getSpawner(entityClassification).clear();
			}
			for (GenerationStage.Decoration i : GenerationStage.Decoration.values()) {
				event.getGeneration().getFeatures(i).clear();
			}
		}
	}

	@SubscribeEvent
	public void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(new KeybladeDataLoader());
		event.addListener(new OrganizationDataLoader());
		event.addListener(new RecipeDataLoader());
	}
	
}
