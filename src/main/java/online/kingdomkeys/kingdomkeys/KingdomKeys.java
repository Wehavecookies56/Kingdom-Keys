package online.kingdomkeys.kingdomkeys;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.ModCommands;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.datagen.DataGeneration;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
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
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "kingdomkeys";
	public static final String MODNAME = "Kingdom Keys";
	
	public static final String MODVER = "2.3.0.2";
	public static final String MCVER = "1.19.4";

	@SubscribeEvent
	public void creativeTabRegistry(CreativeModeTabEvent.Register event) {
		final List<ItemStack> kkItems = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).map(ItemStack::new).toList();
		final Supplier<List<ItemStack>> orgWeapons = Suppliers.memoize(() -> kkItems.stream().filter(item -> item.getItem() instanceof IOrgWeapon).toList());
		final Supplier<List<ItemStack>> keyblades = Suppliers.memoize(() -> kkItems.stream().filter(item -> item.getItem() instanceof KeybladeItem).toList());
		final Supplier<List<ItemStack>> keychains = Suppliers.memoize(() -> kkItems.stream().filter(item -> item.getItem() instanceof KeychainItem).toList());
		final Supplier<List<ItemStack>> misc = Suppliers.memoize(() -> kkItems.stream().filter(item -> !(item.getItem() instanceof KeybladeItem) && !(item.getItem() instanceof IOrgWeapon) && !(item.getItem() instanceof KeychainItem)).toList());

		//Keyblades
		event.registerCreativeModeTab(new ResourceLocation(MODID, Strings.keybladesGroup), builder -> {
			builder
					.title(Component.translatable("itemGroup." + Strings.keybladesGroup))
					.icon(() -> {
						List<ItemStack> keybladesList = keyblades.get();
						return keybladesList.get((int)(System.currentTimeMillis() / 1500) % keybladesList.size());
					})
					.displayItems(((params, output) -> {
						keyblades.get().forEach(output::accept);
						keychains.get().forEach(output::accept);
					}));
		});

		//Org weapons
		event.registerCreativeModeTab(new ResourceLocation(MODID, Strings.organizationGroup), builder -> {
			builder
					.title(Component.translatable("itemGroup." + Strings.organizationGroup))
					.icon(() -> {
						List<ItemStack> orgWeaponsList = orgWeapons.get();
						return orgWeaponsList.get((int)(System.currentTimeMillis() / 1500) % orgWeaponsList.size());
					})
					.displayItems(((params, output) -> {
						orgWeapons.get().forEach(output::accept);
					}));
		});

		//Misc
		event.registerCreativeModeTab(new ResourceLocation(MODID, Strings.miscGroup), builder -> {
			builder
					.title(Component.translatable("itemGroup." + Strings.miscGroup))
					.icon(() -> new ItemStack(ModBlocks.normalBlox.get()))
					.displayItems(((params, output) -> {
						misc.get().forEach(output::accept);
					}));
		});
	}

	public KingdomKeys() {
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		//KKLivingMotionsEnum spell = KKLivingMotionsEnum.SPELL; // initialization
		ModMagic.MAGIC.register(modEventBus);
		//ModMagic.MAGIC2.register(modEventBus);
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
        ModContainers.CONTAINERS.register(modEventBus);
		ModLootModifier.LOOT_MODIFIERS.register(modEventBus);

        ModEntities.ENTITIES.register(modEventBus);

		ModFeatures.RULE_TESTS.register(modEventBus);
		ModFeatures.FEATURES.register(modEventBus);
		ModDimensions.CHUNK_GENERATORS.register(modEventBus);

		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::modLoaded);
		modEventBus.addListener(this::creativeTabRegistry);

		if (ModList.get().isLoaded("epicfight")) {
			//modEventBus.addListener(KKAnimations::register);
			//modEventBus.addListener(EpicKKWeapons::register);
		}
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new DataGeneration());

		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ModConfigs.CLIENT_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.SERVER, ModConfigs.SERVER_SPEC);

		// Server
		MinecraftForge.EVENT_BUS.register(new EntityEvents());
		MinecraftForge.EVENT_BUS.register(new ModCapabilities());
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(PacketHandler::register);
		event.enqueueWork(ModEntities::registerPlacements);
	}

	private void modLoaded(final FMLLoadCompleteEvent event) {
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
			@Override
			public void run() {
				if (ModList.get().isLoaded("epicfight")) {
					//FMLJavaModLoadingContext.get().getModEventBus().addListener(EpicFightRendering::patchedRenderersEventModify);
				}
			}
		});
	}

	@SubscribeEvent
	public void addMoogleHouse(ServerAboutToStartEvent event) {
		addPieceToPattern(event.getServer().registryAccess(), new ResourceLocation("village/plains/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_plains"), 2);
		addPieceToPattern(event.getServer().registryAccess(), new ResourceLocation("village/desert/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_desert"), 2);
		addPieceToPattern(event.getServer().registryAccess(), new ResourceLocation("village/savanna/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_savanna"), 2);
		addPieceToPattern(event.getServer().registryAccess(), new ResourceLocation("village/snowy/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_snowy"), 2);
		addPieceToPattern(event.getServer().registryAccess(), new ResourceLocation("village/taiga/houses"), new ResourceLocation(KingdomKeys.MODID, "village/moogle_house_taiga"), 2);
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
		event.addListener(new ShopListDataLoader());
		event.addListener(new LimitDataLoader());
	}
}
