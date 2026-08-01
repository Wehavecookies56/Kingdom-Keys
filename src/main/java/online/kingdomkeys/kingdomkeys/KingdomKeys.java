package online.kingdomkeys.kingdomkeys;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.advancements.ModAdvancements;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.ModEnergy;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.HUDElement;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.ConvertOldForgeDataCommand;
import online.kingdomkeys.kingdomkeys.command.ModCommands;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.ClientEpicFightIntegration;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.EpicFightIntegration;
import online.kingdomkeys.kingdomkeys.integration.wildfire_gender.KKWildFireGender;
import online.kingdomkeys.kingdomkeys.item.ModArmorMaterials;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.LevelingDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.limit.LimitDataLoader;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.loot.ModLootModifier;
import online.kingdomkeys.kingdomkeys.magic.MagicDataLoader;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.menu.ModMenus;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.savepoint.SavePointDataLoader;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.ShotlockDataLoader;
import online.kingdomkeys.kingdomkeys.shotlock.minigame.ShotlockMinigameHandler;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.melding.MeldingDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListLoader;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellListDataLoader;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.MiniCO;
import online.kingdomkeys.kingdomkeys.world.StruggleHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModEncounterTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;
import online.kingdomkeys.kingdomkeys.world.structure.ModStructures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "kingdomkeys";
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	public static final Supplier<List<ItemStack>> kkItems = Suppliers.memoize(() -> ModItems.ITEMS.getEntries().stream().map(Supplier::get).map(ItemStack::new).toList());
	public static final Supplier<List<ItemStack>> kkBlocks = Suppliers.memoize(() -> ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).map(ItemStack::new).toList());
	public static final Supplier<CreativeModeTab> kingdomKeysTab =
			TABS.register(MODID, () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.kingdomkeys"))
					.icon(() -> new ItemStack(ModItems.kingdomKey.get()))
					.displayItems((params, output) -> Utils.getCurrentItems().forEach(output::accept))
					.build());
	public static boolean efmLoaded = false;
	public static boolean patchouliLoaded = false;
	public static boolean shoulderSurfingLoaded = false;

	public static ResourceLocation rl(String namespace, String path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}

	public static ResourceLocation rl(String path) {
		if (path.contains(":")) {
			return ResourceLocation.parse(path);
		}
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, path);
	}

	public KingdomKeys(IEventBus modEventBus, ModContainer modContainer) {
		ModMagic.MAGIC.register(modEventBus);
		ModDriveForms.DRIVE_FORMS.register(modEventBus);
		ModAbilities.ABILITIES.register(modEventBus);
		ModLevels.LEVELS.register(modEventBus);
		ModLimits.LIMITS.register(modEventBus);
		ModShotlocks.SHOTLOCKS.register(modEventBus);
		ModReactionCommands.REACTION_COMMANDS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModSounds.SOUNDS.register(modEventBus);
		ModEntities.TILE_ENTITIES.register(modEventBus);
		ModMenus.MENUS.register(modEventBus);
		ModLootModifier.LOOT_MODIFIERS.register(modEventBus);
		ModMobEffects.MOB_EFFECTS.register(modEventBus);
		TABS.register(modEventBus);

		ModEntities.ENTITIES.register(modEventBus);

		ModFeatures.RULE_TESTS.register(modEventBus);
		ModFeatures.FEATURES.register(modEventBus);
		ModDimensions.CHUNK_GENERATORS.register(modEventBus);
		ModStructures.STRUCTURES.register(modEventBus);

		ModJsonRegistries.JSON_REGISTRIES.register(modEventBus);
		ModRoomModifiers.ROOM_MODIFIERS.register(modEventBus);
		ModEncounterTypes.ENCOUNTER_TYPES.register(modEventBus);
		ModData.ATTACHMENT_TYPES.register(modEventBus);
		ModComponents.COMPONENTS.register(modEventBus);
		ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);

		ModAdvancements.TRIGGERS.register(modEventBus);

		modEventBus.addListener(this::modLoaded);
		modEventBus.addListener(ModMenus::registerCapabilities);
		modEventBus.addListener(ModEnergy::registerCapabilities);
		modEventBus.addListener(this::findPacks);

		if (FMLEnvironment.dist.isClient()) {
			modEventBus.addListener(ModMenus::registerGUIFactories);
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}

		if (ModList.get().isLoaded("epicfight")) {
			efmLoaded = true;
			EpicFightIntegration.initIntegration(modEventBus);
			// NeoForge.EVENT_BUS.register(new EpicFightEvents());
		}

		if (ModList.get().isLoaded("wildfire_gender")) {
			modEventBus.addListener(KKWildFireGender::registerCapabilities);
		}

		if (ModList.get().isLoaded("patchouli")) {
			patchouliLoaded = true;
		}

		if (ModList.get().isLoaded("shouldersurfing")) {
			shoulderSurfingLoaded = true;
		}

		if (ModList.get().isLoaded("supplementaries")) {
			KingdomKeys.LOGGER.warn("Supplementaries found, by default if you die while typing it sends the message with a - at the end.");
			KingdomKeys.LOGGER.warn("We recommend to disable it if you play with the KO System enabled.");
			KingdomKeys.LOGGER.warn("Change \"send_chat_on_death = true\" to false in supplementaries-client.toml.");
		}

		NeoForge.EVENT_BUS.register(this);
		NeoForge.EVENT_BUS.register(new CastleOblivionHandler());
		//MinecraftForge.EVENT_BUS.register(new APITests());

		modContainer.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);
		modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);
		modContainer.registerConfig(ModConfig.Type.SERVER, ModConfigs.SERVER_SPEC);

		// Server
		NeoForge.EVENT_BUS.register(new EntityEvents());
		NeoForge.EVENT_BUS.register(new MiniCO());
		NeoForge.EVENT_BUS.register(new StruggleHandler());
		NeoForge.EVENT_BUS.register(new ShotlockMinigameHandler());
	}

	private void modLoaded(final FMLLoadCompleteEvent event) {
		if (FMLEnvironment.dist.isClient()) {
			if (ModList.get().isLoaded("epicfight")) {
				ClientEpicFightIntegration.init();
				//ModList.get().getModContainerById(KingdomKeys.MODID).get().getEventBus().addListener(EpicFightRendering::patchedRenderersEventModify);
			}
			NeoForge.EVENT_BUS.post(new CommandMenuEvent.Construct(CommandMenuGui.INSTANCE));
			HUDElement.REGISTRY.forEach(HUDElement::loadFromConfig);
		}
	}

	@SubscribeEvent
	public void addMoogleHouse(ServerAboutToStartEvent event) {
		ConvertOldForgeDataCommand.run = false;
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/plains/houses"), KingdomKeys.rl("village/moogle_house_plains"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/desert/houses"), KingdomKeys.rl("village/moogle_house_desert"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/savanna/houses"), KingdomKeys.rl("village/moogle_house_savanna"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/snowy/houses"), KingdomKeys.rl("village/moogle_house_snowy"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/taiga/houses"), KingdomKeys.rl("village/moogle_house_taiga"), 2);
	}

	public void addPieceToPattern(RegistryAccess registryAccess, ResourceLocation pattern, ResourceLocation structure, int weight) {
		Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL);
		StructureTemplatePool pat = Objects.requireNonNull(registry.get(pattern));
		SinglePoolElement piece = StructurePoolElement.legacy(structure.toString()).apply(StructureTemplatePool.Projection.RIGID);
		for (int i = 0; i < weight; i++) {
			pat.templates.add(piece);
		}
		pat.rawTemplates = List.of(Pair.of(piece, weight));
	}


	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		ModCommands.register(dispatcher);
	}

	@SubscribeEvent
	public void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(new KeybladeDataLoader());
		event.addListener(new OrganizationDataLoader());
		event.addListener(new RecipeDataLoader());
		event.addListener(new MeldingDataLoader());
		event.addListener(new DriveFormDataLoader());
		event.addListener(new MagicDataLoader());
		event.addListener(new LevelingDataLoader());
		event.addListener(new NamesListLoader.Loader());
		event.addListener(new ShopListDataLoader());
		event.addListener(new SellListDataLoader());
		event.addListener(new LimitDataLoader());
		event.addListener(new ShotlockDataLoader());
		event.addListener(new SavePointDataLoader());
		ModJsonRegistries.registry.forEach(jsonRegistry -> {
			jsonRegistry.setRegistries(event.getRegistryAccess());
			event.addListener(jsonRegistry);
		});
	}

	public void findPacks(AddPackFindersEvent event) {
		event.addPackFinders(KingdomKeys.rl("datapacks/disable_blox_gen"), PackType.SERVER_DATA, Component.literal("KK: Disable Blox Gen (Overworld)"), PackSource.FEATURE, false, Pack.Position.TOP);
		event.addPackFinders(KingdomKeys.rl("datapacks/disable_blox_gen_end"), PackType.SERVER_DATA, Component.literal("KK: Disable Blox Gen (End)"), PackSource.FEATURE, false, Pack.Position.TOP);
		event.addPackFinders(KingdomKeys.rl("datapacks/recipe_example"), PackType.SERVER_DATA, Component.literal("KK: Custom Synthesis Recipe Example"), PackSource.FEATURE, false, Pack.Position.TOP);
		event.addPackFinders(KingdomKeys.rl("datapacks/co_floor_example"), PackType.SERVER_DATA, Component.literal("KK: Custom CO Floor Example"), PackSource.FEATURE, false, Pack.Position.TOP);
	}
}
