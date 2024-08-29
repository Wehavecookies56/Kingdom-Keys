package online.kingdomkeys.kingdomkeys;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.advancements.KKLevelUpTrigger;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.ModCommands;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.menu.ModMenus;
import online.kingdomkeys.kingdomkeys.datagen.DataGeneration;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.EpicKKWeapons;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.LevelingDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.LimitDataLoader;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.loot.ModLootModifier;
import online.kingdomkeys.kingdomkeys.magic.MagicDataLoader;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListLoader;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;
import online.kingdomkeys.kingdomkeys.world.structure.ModStructures;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "kingdomkeys";
	public static final String MODNAME = "Kingdom Keys";

	public static boolean efmLoaded = false;

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

	private static final Supplier<List<ItemStack>> kkItems = Suppliers.memoize(() -> ModItems.ITEMS.getEntries().stream().map(Supplier::get).map(ItemStack::new).toList());
	private static final Supplier<List<ItemStack>> orgWeapons = Suppliers.memoize(() -> kkItems.get().stream().filter(item -> item.getItem() instanceof IOrgWeapon).toList());
	private static final Supplier<List<ItemStack>> keyblades = Suppliers.memoize(() -> kkItems.get().stream().filter(item -> item.getItem() instanceof KeybladeItem).toList());
	private static final Supplier<List<ItemStack>> keychains = Suppliers.memoize(() -> kkItems.get().stream().filter(item -> item.getItem() instanceof KeychainItem).toList());
	private static final Supplier<List<ItemStack>> equipables = Suppliers.memoize(() -> kkItems.get().stream().filter(item -> (item.getItem() instanceof KKPotionItem || item.getItem() instanceof KKArmorItem || item.getItem() instanceof KKAccessoryItem)).toList());
	private static final Supplier<List<ItemStack>> misc = Suppliers.memoize(() -> kkItems.get().stream().filter(item -> !(item.getItem() instanceof KeybladeItem) && !(item.getItem() instanceof KeychainItem) && !(item.getItem() instanceof IOrgWeapon) && !(item.getItem() instanceof KKPotionItem) && !(item.getItem() instanceof KKArmorItem) && !(item.getItem() instanceof KKAccessoryItem)).toList());

	public static final Supplier<CreativeModeTab>
			keyblades_tab = TABS.register(Strings.keybladesGroup, () -> CreativeModeTab.builder()
				.title(Component.translatable("itemGroup." + Strings.keybladesGroup))
				.icon(() -> {
					List<ItemStack> keybladesList = keyblades.get();
					return keybladesList.get((int)(System.currentTimeMillis() / 1500) % keybladesList.size());
				})
				.displayItems(((params, output) -> {
					keyblades.get().forEach(output::accept);
					keychains.get().forEach(output::accept);
				}))
				.withSearchBar(71)
			.backgroundTexture(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"textures/gui/container/tab_kk.png"))
				.hideTitle()
				.build()),
			organization_tab = TABS.register(Strings.organizationGroup, () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup." + Strings.organizationGroup))
					.icon(() -> {
						List<ItemStack> orgWeaponsList = orgWeapons.get();
						return orgWeaponsList.get((int)(System.currentTimeMillis() / 1500) % orgWeaponsList.size());
					})
					.displayItems(((params, output) -> {
						orgWeapons.get().forEach(output::accept);
					}))
					.withSearchBar(71)
					.backgroundTexture(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"textures/gui/container/tab_kk.png"))
					.hideTitle()
					.build()),
			
			equipables_tab = TABS.register(Strings.equipablesGroup, () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup." + Strings.equipablesGroup))
					.icon(() -> {
						List<ItemStack> equipablesList = equipables.get();
						return equipablesList.get((int)(System.currentTimeMillis() / 1500) % equipablesList.size());
					})
					.displayItems(((params, output) -> {
						equipables.get().forEach(output::accept);
					}))
					.withSearchBar(71)
					.backgroundTexture(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"textures/gui/container/tab_kk.png"))
					.hideTitle()
					.build()),

			misc_tab = TABS.register(Strings.miscGroup, () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup." + Strings.miscGroup))
					.icon(() -> new ItemStack(ModBlocks.normalBlox.get()))
					.displayItems(((params, output) -> {
						misc.get().forEach(output::accept);
						ItemStack linkedSavePoint = new ItemStack(ModBlocks.savepoint.get());
						linkedSavePoint.setTag(new CompoundTag());
						linkedSavePoint.getTag().putString("tier", SavePointStorage.SavePointType.LINKED.getSerializedName().toUpperCase());
						ItemStack warpPoint = new ItemStack(ModBlocks.savepoint.get());
						warpPoint.setTag(new CompoundTag());
						warpPoint.getTag().putString("tier", SavePointStorage.SavePointType.WARP.getSerializedName().toUpperCase());
						output.accept(linkedSavePoint);
						output.accept(warpPoint);
					}))
					.withSearchBar(71)
					.withBackgroundLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"textures/gui/container/tab_kk.png"))
					.hideTitle()
					.build());

	public KingdomKeys(IEventBus modEventBus, ModContainer modContainer) {
		ModMagic.MAGIC.register(modEventBus);
		ModDriveForms.DRIVE_FORMS.register(modEventBus);
		ModAbilities.ABILITIES.register(modEventBus);
		ModLevels.LEVELS.register(modEventBus);
		ModLimits.LIMITS.register(modEventBus);
		ModShotlocks.SHOTLOCKS.register(modEventBus);
		ModReactionCommands.REACTION_COMMANDS.register(modEventBus);
		ModMaterials.MATERIALS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModSounds.SOUNDS.register(modEventBus);
		ModEntities.TILE_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
		ModLootModifier.LOOT_MODIFIERS.register(modEventBus);
		TABS.register(modEventBus);

        ModEntities.ENTITIES.register(modEventBus);

		ModFeatures.RULE_TESTS.register(modEventBus);
		ModFeatures.FEATURES.register(modEventBus);
		ModDimensions.CHUNK_GENERATORS.register(modEventBus);
		ModStructures.STRUCTURES.register(modEventBus);

		ModRoomStructures.ROOM_STRUCTURES.register(modEventBus);
		ModRoomTypes.ROOM_TYPES.register(modEventBus);
		ModFloorTypes.FLOOR_TYPES.register(modEventBus);
		ModData.ATTACHMENT_TYPES.register(modEventBus);

		modEventBus.addListener(this::setup);

		if (ModList.get().isLoaded("epicfight")) {
			efmLoaded = true;
			modEventBus.addListener(KKAnimations::register);
			modEventBus.addListener(EpicKKWeapons::register);
			NeoForge.EVENT_BUS.register(new KKSkills());
			//KKSkills.SKILLS.register(modEventBus);
			//ComboExtender.DATA_KEYS.register(modEventBus);
		}

		NeoForge.EVENT_BUS.register(this);
		NeoForge.EVENT_BUS.register(new DataGeneration());
		NeoForge.EVENT_BUS.register(new CastleOblivionHandler());
		//MinecraftForge.EVENT_BUS.register(new APITests());

		modContainer.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);
		modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);
		modContainer.registerConfig(ModConfig.Type.SERVER, ModConfigs.SERVER_SPEC);

		// Server
		NeoForge.EVENT_BUS.register(new EntityEvents());
		NeoForge.EVENT_BUS.register(new ModData());
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(PacketHandler::register);
        KKLevelUpTrigger.TRIGGER_LEVELUP = CriteriaTriggers.register(new KKLevelUpTrigger());
	}

	@SubscribeEvent
	public void addMoogleHouse(ServerAboutToStartEvent event) {
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/plains/houses"), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "village/moogle_house_plains"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/desert/houses"), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "village/moogle_house_desert"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/savanna/houses"), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "village/moogle_house_savanna"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/snowy/houses"), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "village/moogle_house_snowy"), 2);
		addPieceToPattern(event.getServer().registryAccess(), ResourceLocation.withDefaultNamespace("village/taiga/houses"), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "village/moogle_house_taiga"), 2);
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
		event.addListener(new DriveFormDataLoader());
		event.addListener(new MagicDataLoader());
		event.addListener(new LevelingDataLoader());
		event.addListener(new NamesListLoader.Loader());
		event.addListener(new ShopListDataLoader());
		event.addListener(new LimitDataLoader());
	}
}
