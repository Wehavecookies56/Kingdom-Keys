package online.kingdomkeys.kingdomkeys;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
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
import net.minecraftforge.registries.ForgeRegistries;
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
import online.kingdomkeys.kingdomkeys.world.biome.ModBiomes;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Supplier;

@Mod("kingdomkeys")
public class KingdomKeys {

	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "kingdomkeys";
	public static final String MODNAME = "Kingdom Keys";
	public static final String MODVER = "2.3.1.0";
	public static final String MCVER = "1.19.2";

	public static CreativeModeTab orgWeaponsGroup = new CreativeModeTab(Strings.organizationGroup) {
		private static final Supplier<List<ItemStack>> orgWeapons = Suppliers.memoize(() -> ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof IOrgWeapon).map(ItemStack::new).toList());
		@Override
		public ItemStack makeIcon() {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack getIconItem() {
			List<ItemStack> orgWeaponsList = orgWeapons.get();
			return orgWeaponsList.get((int)(System.currentTimeMillis() / 1500) % orgWeaponsList.size());
		}
	};
	public static CreativeModeTab keybladesGroup = new CreativeModeTab(Strings.keybladesGroup) {
		private static final Supplier<List<ItemStack>> keyblades = Suppliers.memoize(() -> ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof KeybladeItem).map(ItemStack::new).toList());
		@Override
		public ItemStack makeIcon() {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack getIconItem() {
			List<ItemStack> keybladesList = keyblades.get();
			return keybladesList.get((int)(System.currentTimeMillis() / 1500) % keybladesList.size());
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
		ModBiomes.BIOMES.register(modEventBus);

		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::modLoaded);

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
		event.enqueueWork(ModDimensions::setupDimension);
		addMoogleHouse();
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
		pat.rawTemplates.add(Pair.of(StructurePoolElement.legacy(structure.toString()).apply(StructureTemplatePool.Projection.RIGID), weight));
		Pools.register(pat);
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
