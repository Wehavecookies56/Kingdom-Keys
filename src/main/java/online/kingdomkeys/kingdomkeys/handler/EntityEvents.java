package online.kingdomkeys.kingdomkeys.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.advancements.KKAllAdvancementsTrigger;
import online.kingdomkeys.kingdomkeys.advancements.ModAdvancements;
import online.kingdomkeys.kingdomkeys.api.event.CastleOblivionEvent;
import online.kingdomkeys.kingdomkeys.block.FlowmotionRailBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.DimensionCommand;
import online.kingdomkeys.kingdomkeys.command.ExpCommand;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;
import online.kingdomkeys.kingdomkeys.entity.drops.*;
import online.kingdomkeys.kingdomkeys.entity.mob.*;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.MarluxiaGoal;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightUtils;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.LevelingDataLoader;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.lib.*;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.limit.LimitDataLoader;
import online.kingdomkeys.kingdomkeys.magic.MagicDataLoader;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.menu.PauldronInventory;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAirStepPacket;
import online.kingdomkeys.kingdomkeys.network.stc.*;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.savepoint.SavePointDataLoader;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorldLoader;
import online.kingdomkeys.kingdomkeys.world.worldmap.WorldMap;
import online.kingdomkeys.kingdomkeys.shotlock.ShotlockDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.melding.MeldingRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellListRegistry;
import online.kingdomkeys.kingdomkeys.util.CombatAbilities;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomPos;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.DropModifier;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityEvents {

	public static ThreatLevel threatLevel = ThreatLevel.NONE;
	Map<UUID, Boolean> openedAlignment = new HashMap<>();
	int airstepTicks = -1;

	private static int getMultiplier(LivingDeathEvent event, Player player, PlayerData playerData) {
		int multiplier = 1;
		if (player.getMainHandItem().getItem() instanceof IOrgWeapon weapon) {
			if (weapon.getMember() == playerData.getAlignment() || (event.getSource().getDirectEntity() instanceof KKThrowableEntity && playerData.getAlignment() == OrgMember.AXEL)) { // If the item used to kill is for the correct alignment OR if it's been a
				// throwable entity and the player is Axel (probably the only case so far which could be true)
				multiplier = 2;
			}
		}
		multiplier += playerData.getNumberOfAbilitiesEquipped(ModAbilities.LUCKY_STRIKE);
		return multiplier;
	}

	@SubscribeEvent
	public void soundPlayed(PlayLevelSoundEvent.AtEntity event) {
		if (event.getEntity() instanceof Player player && event.getSound().value().getLocation().getPath().contains("step")) {
			ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
			ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
			ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
			if (Utils.isKBArmor(chest) || Utils.isKBArmor(legs) || Utils.isKBArmor(feet)) {
				event.getEntity().playSound(ModSounds.keyblade_armor.get());
			}
		}
	}

	@SubscribeEvent
	public void onXPPickup(PlayerXpEvent.XpChange e) {
		if (e.getEntity() instanceof Player player && player.getHealth() <= player.getMaxHealth() / 2) {
			PlayerData playerData = PlayerData.get(player);
			e.setAmount(e.getAmount() * (playerData.getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST) + 1));
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinLevelEvent e) {
		if(e.getEntity() instanceof ItemEntity itemEntity) {
			if (!(itemEntity.getItem().getItem() instanceof MapCardItem))
				return;

			if (itemEntity instanceof CardItemEntity)
				return;

			if (e.getLevel().isClientSide())
				return;

			CardItemEntity card = new CardItemEntity(e.getLevel(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem().copy());
			card.setDeltaMovement(itemEntity.getDeltaMovement());
			card.setPickUpDelay(40);
			card.setTarget(itemEntity.getTarget());

			e.setCanceled(true);
			e.getLevel().addFreshEntity(card);
		}
		if (e.getEntity() instanceof LivingEntity mob) {
			GlobalData mobData = GlobalData.get(mob);
			if (mobData == null) return;

			Player player = Utils.getClosestPlayer(mob, mob.level());
			if (player == null) return;

			//ROD
			if (e.getLevel().dimension().location().getPath().equals("realm_of_darkness") && mob instanceof IKHMob ikhmob) {
				if (ikhmob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD) {
					double dist = e.getEntity().position().distanceTo(new Vec3(0, 62, 0));
					int level = (int) Math.min(dist / ModConfigs.rodHeartlessLevelScale, ModConfigs.rodHeartlessMaxLevel);
					mobData.setLevel(level);
				}
			}

			//Set level based on config
			if (mobData.getLevel() <= 0 && mob instanceof Monster && ModConfigs.SERVER.hostileMobsLevel.get()) {
				mobData.setLevel(Utils.getRandomMobLevel(player));
			}

			if (mob instanceof EnderDragon && ModConfigs.SERVER.dragonLevel.get()) {
				mobData.setLevel(Utils.getRandomMobLevel(player));
			}

			//Tamed mobs
			if (mob instanceof OwnableEntity ownableEntity) {
				if (mobData.getLevel() == 0) {
					if (ownableEntity.getOwner() instanceof Player owner) {
						PlayerData ownerData = PlayerData.get(owner);
						mobData.setLevel(ownerData.getLevel());
					}
				}
			}

			//Apply stored level attributes
			if (mobData.getLevel() > 0) {
				int lvl = mobData.getLevel();
				Utils.applyMobLevel(mob, lvl);

				mob.setHealth(mob.getMaxHealth());
				if (!mob.hasCustomName() && !(mob instanceof OwnableEntity)) {
					if (ModConfigs.mobLevelName) {
						mob.setCustomName(Component.translatable(mob.getDisplayName().getString() + " Lv." + Utils.getLevelColor(player, lvl) + lvl + ChatFormatting.RESET));
					}
					PacketHandler.sendTo(new SCSyncGlobalData(mob), (ServerPlayer) player);
				}
			}
		}
	}

	public void checkRecipeMaterials(Player player) {
		if (player.level().registryAccess().lookupOrThrow(Registries.ITEM).get(ModTags.MATERIALS).isPresent()) {
			RecipeRegistry.getInstance().getValues().forEach(recipe -> recipe.getMaterials().keySet().forEach(item -> {
				if (!item.builtInRegistryHolder().is(ModTags.MATERIALS)) {
					player.sendSystemMessage(Component.translatable("kingdomkeys.error.recipe_missing_material", recipe.getRegistryName().toString()).withStyle(ChatFormatting.RED));
				}
			}));
			BuiltInRegistries.ITEM.entrySet().stream().filter(itemRegistryObject -> itemRegistryObject.getValue() instanceof KeybladeItem).map(itemRegistryObject -> (KeybladeItem) itemRegistryObject.getValue()).toList().forEach(keybladeItem -> {
				if (keybladeItem.data != null) {
					for (int i = 0; i < keybladeItem.data.getMaxLevel(); i++) {
						keybladeItem.data.getLevelData(i).getMaterialList().keySet().forEach(item -> {
							if (!item.builtInRegistryHolder().is(ModTags.MATERIALS)) {
								player.sendSystemMessage(Component.translatable("kingdomkeys.error.keyblade_missing_material", BuiltInRegistries.ITEM.getKey(keybladeItem)).withStyle(ChatFormatting.RED));
							}
						});
					}
				}
			});
		} else {
			player.sendSystemMessage(Component.translatable("kingdomkeys.error.synthesis_tag_failed").withStyle(ChatFormatting.RED));
		}
	}

	@SubscribeEvent
	public void animalTame(AnimalTameEvent event) {
		GlobalData mobData = GlobalData.get(event.getAnimal());
		PlayerData ownerData = PlayerData.get(event.getTamer());
		mobData.setLevel(ownerData.getLevel());
		int lvl = mobData.getLevel();
		Utils.applyMobLevel(event.getAnimal(), lvl);
		event.getAnimal().heal(event.getAnimal().getMaxHealth());
	}

	@SubscribeEvent
	public void dataPackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		if (player != null) {
			// Sync all registries, important
			PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncLevelingData(LevelingDataLoader.names, LevelingDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), player);
			PacketHandler.sendTo(new SCSyncMeldingData(MeldingRegistry.getInstance().getValues()), player);
			PacketHandler.sendTo(new SCSyncMoogleNames(NamesListRegistry.getInstance()), player);
			PacketHandler.sendTo(new SCSyncShopData(ShopListRegistry.getInstance().getValues()), player);
			PacketHandler.sendTo(new SCSyncSellData(SellListRegistry.getInstance().getValues()), player);
			PacketHandler.sendTo(new SCSyncMagicData(MagicDataLoader.names, MagicDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncDriveFormData(DriveFormDataLoader.names, DriveFormDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncLimitData(LimitDataLoader.names, LimitDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncShotlockData(ShotlockDataLoader.names, ShotlockDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncSavePointData(SavePointDataLoader.names, SavePointDataLoader.dataList), player);
			PacketHandler.sendTo(new SCSyncGummiWorlds(GummiWorldLoader.names, GummiWorldLoader.dataList), player);
			ModJsonRegistries.registry.forEach(jsonRegistry -> {
				PacketHandler.sendTo(new SCSyncJsonRegistry<>(jsonRegistry), player);
			});
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
		checkRecipeMaterials(e.getEntity());
		Player player = e.getEntity();
		PlayerData playerData = PlayerData.get(player);
		WorldData worldData = WorldData.get(e.getEntity().getServer());
		GlobalData.clearClientCache();
		CastleOblivionData.InteriorData.clearClientCache();
		if (playerData != null) {
			// Heartless Spawn reset
			if (worldData != null) {
				if (worldData.getHeartlessSpawnLevel() > 0 && ModConfigs.heartlessSpawningMode == SpawningMode.NEVER) {
					worldData.setHeartlessSpawnLevel(0);
				} else if (worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.ALWAYS) {
					worldData.setHeartlessSpawnLevel(1);
				}
			}

			if (!player.level().isClientSide) { // Sync from server to client
				Utils.updateOrgRobesTeam((ServerPlayer) player);

				if (!playerData.getDriveFormMap().containsKey(DriveForm.NONE)) { // One time event here :D
					Utils.getFakeForms().forEach(form -> {
						playerData.setDriveFormLevel(form, 1);
					});
					playerData.setDriveFormLevel(ModDriveForms.ANTI.location(), 1);

					if (playerData.getEquippedItems().isEmpty()) {
						HashMap<Integer, ItemStack> map = new HashMap<>();
						for (int i = 0; i < 4; i++) {
							map.put(i, ItemStack.EMPTY);
						}
						playerData.equipAllItems(map, true);
					}
				}

				if(!playerData.getMaterialMap().isEmpty() && playerData.getTotalMaterialMap().isEmpty()){ //Shop req
					playerData.setTotalMaterialMap(playerData.getMaterialMap());
				}

				if (!playerData.getDriveFormMap().containsKey(DriveForm.KB2)) {
					playerData.setDriveFormLevel(DriveForm.KB2, 1);
					playerData.setDriveFormLevel(DriveForm.KB3, 1);
				}

				ModConfigs.startingRecipes.forEach(resourceLocation -> {
					if (RecipeRegistry.getInstance().containsKey(resourceLocation)) {
						playerData.addKnownRecipe(resourceLocation);
					} else {
						KingdomKeys.LOGGER.error("Recipe[{}] in startingRecipes config doesn't exist", resourceLocation);
					}
				});

				if (!playerData.getDriveFormMap().containsKey(ModDriveForms.ANTI.location())) {
					playerData.setDriveFormLevel(ModDriveForms.ANTI.location(), 1);
				}

				// Old worlds stat conversion
				if (playerData.getSoAState() == SoAState.COMPLETE) {
					switch (playerData.getChosen()) {
						case WARRIOR -> {
							if (!playerData.getStrengthStat().hasModifier("choice") && !playerData.getStrengthStat().hasModifier("sacrifice")) {
								playerData.setStrength(playerData.getStrength(false) - 1);
								playerData.getStrengthStat().addModifier("choice", 1, false, false);
							}
						}
						case GUARDIAN -> {
							if (!playerData.getDefenseStat().hasModifier("choice") && !playerData.getDefenseStat().hasModifier("sacrifice")) {
								playerData.setDefense(playerData.getDefense(false) - 1);
								playerData.getDefenseStat().addModifier("choice", 1, false, false);
							}
						}
						case MYSTIC -> {
							if (!playerData.getMagicStat().hasModifier("choice") && !playerData.getMagicStat().hasModifier("sacrifice")) {
								playerData.setMagic(playerData.getMagic(false) - 1);
								playerData.getMagicStat().addModifier("choice", 1, false, false);
							}
						}
					}
					switch (playerData.getSacrificed()) {
						case WARRIOR -> {
							if (!playerData.getStrengthStat().hasModifier("choice") && !playerData.getStrengthStat().hasModifier("sacrifice")) {
								playerData.setStrength(playerData.getStrength(false) + 1);
								playerData.getStrengthStat().addModifier("sacrifice", -1, false, false);
							}
						}
						case GUARDIAN -> {
							if (!playerData.getDefenseStat().hasModifier("choice") && !playerData.getDefenseStat().hasModifier("sacrifice")) {
								playerData.setDefense(playerData.getDefense(false) + 1);
								playerData.getDefenseStat().addModifier("sacrifice", -1, false, false);
							}
						}
						case MYSTIC -> {
							if (!playerData.getMagicStat().hasModifier("choice") && !playerData.getMagicStat().hasModifier("sacrifice")) {
								playerData.setMagic(playerData.getMagic(false) + 1);
								playerData.getMagicStat().addModifier("sacrifice", -1, false, false);
							}
						}
					}
				}

				//Set org weapon keybladeIDs if they don't exist
				if (!playerData.getEquippedWeapon().is(Items.AIR)) {
					Utils.createKeybladeID(playerData.getEquippedWeapon());
				}

				playerData.getWeaponsUnlocked().forEach(itemStack -> {
					if (itemStack.is(playerData.getEquippedWeapon().getItem())) {
						Utils.copyKeybladeID(playerData.getEquippedWeapon(), itemStack);
					} else {
						Utils.createKeybladeID(itemStack);
					}
				});

				// Added for old world retrocompatibility
				if (!playerData.getDriveFormMap().containsKey(DriveForm.SYNCH_BLADE)) {
					playerData.setDriveFormLevel(DriveForm.SYNCH_BLADE, 1);
				}

				// TODO (done) Fix for retrocompatibility, move above in a few versions
				if (playerData.getEquippedKBArmors().isEmpty()) {
					HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
					for (int i = 0; i < 1; i++) {
						map.put(i, ItemStack.EMPTY);
					}
					playerData.equipAllKBArmor(map, true);
				}

				HashMap<Integer, ItemStack> accessoriesMap = (HashMap<Integer, ItemStack>) playerData.getEquippedAccessories();
				if (accessoriesMap.isEmpty()) {
					for (int i = 0; i < 4; i++) { //Initial for to initialize slots
						accessoriesMap.put(i, ItemStack.EMPTY);
					}
				}
				for (int i = accessoriesMap.size(); i < 4; i++) { //For needed to expand slots post update
					accessoriesMap.put(i, ItemStack.EMPTY);
				}
				playerData.equipAllAccessories(accessoriesMap, true);

				HashMap<Integer, ItemStack> armorsMap = (HashMap<Integer, ItemStack>) playerData.getEquippedArmors();
				if (armorsMap.isEmpty()) {
					for (int i = 0; i < 4; i++) {
						armorsMap.put(i, ItemStack.EMPTY);
					}
				}
				for (int i = armorsMap.size(); i < 4; i++) {
					armorsMap.put(i, ItemStack.EMPTY);
				}
				playerData.equipAllArmors(armorsMap, true);

				HashMap<Integer, ItemStack> magicsMap = (HashMap<Integer, ItemStack>) playerData.getEquippedMagics();
				if (magicsMap.isEmpty()) {
					for (int i = 0; i < 20; i++) {
						magicsMap.put(i, ItemStack.EMPTY);
					}
				}
				for (int i = magicsMap.size(); i < 20; i++) {
					magicsMap.put(i, ItemStack.EMPTY);
				}
				playerData.equipAllMagics(magicsMap, true);


				// Fills the map with empty stacks for every form that requires one.
				playerData.getDriveFormMap().keySet().forEach(key -> {
					// Make sure the form exists
					if (ModDriveForms.registry.containsKey(key)) {
						// Check if it requires a slot
						if (ModDriveForms.registry.get(key).hasKeychain()) {
							// Check if the player has form
							if (playerData.getDriveFormMap().containsKey(key)) {
								if (!playerData.getEquippedKeychains().containsKey(key)) {
									playerData.setNewKeychain(key, ItemStack.EMPTY);
								}
							}
						}
					}
				});

				//Data check, might be able to reduce the above code:
				online.kingdomkeys.kingdomkeys.leveling.Level levelData = ModLevels.registry.get(KingdomKeys.rl(playerData.getChosen().getSerializedName()));
				if (levelData != null) {// Only run if the player has made a choice
					//If stored is -1 (default value in the capability) set it directly to the real version without fixing
					KingdomKeys.LOGGER.debug("Player version: " + playerData.getVer() + " leveldata version: " + levelData.getVersion());
					if (playerData.getVer() == -1) playerData.setVer(levelData.getVersion());

					//If the stored player version is different from the one in the file of it's choice (for example file got updated)
					if (playerData.getVer() != levelData.getVersion()) {
						//Run kkexp fix command
						ExpCommand.fix(playerData, player);
						player.level().playSound(null, player.blockPosition(), ModSounds.levelup.get(), SoundSource.MASTER, 1f, 1.0f);
						KingdomKeys.LOGGER.info("Auto adjusted " + player.getDisplayName().getString() + " data from version " + playerData.getVer() + " to version " + levelData.getVersion());
						player.sendSystemMessage(Component.translatable("kingdomkeys.data.version_adjusted", playerData.getVer(), levelData.getVersion()));
						playerData.setVer(levelData.getVersion());
					}
				}

				StaffCrowns.refresh(player.getUUID(), playerData);

				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncWorldData(player.getServer()), (ServerPlayer) player);
				PacketHandler.syncToAllAround(player, playerData);

				Utils.RefreshAbilityAttributes(player, playerData);
				if (CastleOblivionHandler.isInterior(player.level().dimension())) {
					SCSyncCastleOblivionInteriorData.syncClients((ServerLevel) player.level());
					ServerLevel level = player.level().getServer().getLevel(player.level().dimension());
					Floor startFloor = Floor.getOrCreateFirstFloor(level);
					NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(null, startFloor, null, startFloor.getRoom(RoomPos.ZERO).getGenerated().orElse(null), player));
					PacketHandler.sendTo(new SCUpdateCORooms(CastleOblivionHandler.getCurrentFloor(player).getRooms()), (ServerPlayer) player);
				}
			}
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	@SubscribeEvent
	public void onServerStarted(ServerStartedEvent event) {
		StaffCrowns.fetch(event.getServer());
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		PlayerData playerData = PlayerData.get(player);
		//playerData.setCrown("silver");
		//playerData.clearRecipes("all");
		/*System.out.println(playerData.getMaterialMap());
		System.out.println(playerData.getTotalMaterialMap());
		System.out.println("---");*/

		if (playerData != null) {
			CombatAbilities.tick(player, playerData);

			// Check if rc conditions match
			//Tick RCs in list
			playerData.tickReactionCommands(player);

			// Check commands from registry that need active check (can turn off based on conditions like drive forms when you are healed)
			// Those will be available when joining the world too if the conditions are met
			for (ReactionCommand rc : ModReactionCommands.CONSTANT_CHECK_COMMANDS.get()) {
				if (rc.conditionsToAppear(player, player)) {
					// The conditions were just checked and the command is already resolved, so this skips
					// the two registry lookups and the second conditionsToAppear the name-based overload
					// would do - per command, per player, per tick.
					playerData.addCheckedReactionCommand(rc);
				}
			}

			if (!player.level().isClientSide && player.tickCount == 5) { // TODO Check if it's necessary, I thought it was to set the max hp value but now it seems to work fine without it
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}

			// Anti form FP code done here
			if (playerData.isFormActive(ModDriveForms.ANTI)) {
				if (playerData.getFP() > 0) {
					playerData.setFP(playerData.getFP() - 0.3);
				} else {
					playerData.setActiveDriveForm(DriveForm.NONE);
					player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					if (!player.level().isClientSide) {
						PacketHandler.syncToAllAround(player, playerData);
					}
				}
			} else if (!playerData.noFormActive()) {
				ModDriveForms.registry.get(playerData.getActiveDriveForm()).updateDrive(player);
			}
			// Limit recharge system
			if (playerData.getLimitCooldownTicks() > 0 && !player.level().isClientSide) {
				playerData.setLimitCooldownTicks(playerData.getLimitCooldownTicks() - 1);
				if (playerData.getLimitCooldownTicks() <= 0) {
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}

			// Magic Casttime
			if (playerData.getMagicCasttimeTicks() > 0 && !player.level().isClientSide)
				playerData.remMagicCasttimeTicks(1);
			if (playerData.getCastedMagic() != null) {
				if (playerData.getMagicCasttimeTicks() <= 0) {
					Utils.castMagic castedMagic = playerData.getCastedMagic();
					castedMagic.magic().magicUse(castedMagic.player(), castedMagic.caster(), castedMagic.fullMPBlastMult(), castedMagic.lockOnEntity());
					player.swing(InteractionHand.MAIN_HAND, true);
					playerData.setCastedMagic(null);
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}

			// Magic CD recharge system
			if (playerData.getMagicCooldownTicks() > 0 && !player.level().isClientSide) {
				playerData.setMagicCooldownTicks(playerData.getMagicCooldownTicks() - 1);
				if (playerData.getMagicCooldownTicks() <= 0) {
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}

			boolean magicCooldownExpired = playerData.tickMagicCooldowns();
			if (magicCooldownExpired && !player.level().isClientSide) {
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}

			// MP Recharge system
			if (playerData.getRecharge()) {
				if (playerData.getMP() >= playerData.getMaxMP()) { // Has recharged fully
					playerData.setRecharge(false);
					playerData.setMP(playerData.getMaxMP());
				} else { // Still recharging
					if (playerData.getMP() < 0) // Somehow people was getting negative MP so this should hopefully fix it
						playerData.setMP(0);
					playerData.addMP(playerData.getMaxMP() / 500 * ((Utils.getMPHasteValue(playerData) / 10) + 2));
				}

			} else { // Not on recharge
				if (playerData.getMP() <= 0 && playerData.getMaxMP() > 0) {
					playerData.setRecharge(true);
					if (!player.level().isClientSide) {
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}
			}

			if (!player.level().isClientSide) {
				if (playerData.getAlignment() == OrgMember.NONE) {
					if (!openedAlignment.containsKey(player.getUUID())) {
						openedAlignment.put(player.getUUID(), false);
					}
					boolean wearingOrgCloak = Utils.isWearingOrgRobes(player);

					if (wearingOrgCloak) {
						if (!openedAlignment.get(player.getUUID())) {
							PacketHandler.sendTo(new SCOpenAlignmentScreen(), (ServerPlayer) player);
							openedAlignment.put(player.getUUID(), true);
						}
					} else {
						openedAlignment.put(player.getUUID(), false);
					}
				}

				// Treasure Magnet
				if (playerData.isAbilityEquipped(ModAbilities.TREASURE_MAGNET) && !player.isCrouching() && player.getInventory().getFreeSlot() > -1) {
					double x = player.getX();
					double y = player.getY() + 0.75;
					double z = player.getZ();

					float range = 1 + playerData.getNumberOfAbilitiesEquipped(ModAbilities.TREASURE_MAGNET);

					List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, new AABB(x - range, y - range, z - range, x + range, y + range, z + range));
					int pulled = 0;
					for (ItemEntity item : items) {
						if (item.tickCount < 20) {
							break;
						}
						if (pulled > 200) {
							break;
						}

						Vec3 entityVector = new Vec3(item.getX(), item.getY() - item.getBbHeight() / 2, item.getZ());
						Vec3 finalVector = new Vec3(x, y, z).subtract(entityVector);

						if (finalVector.length() > 1) {
							finalVector = finalVector.normalize();
						}

						item.setDeltaMovement(finalVector.multiply(0.45F, 0.45F, 0.45F));
						pulled++;
					}
				}
			}
		}

		// The threat level used to be worked out here, which ran for every player the client was ticking
		// and left whichever went last holding the answer. ClientEvents does it now, for the one player
		// whose command menu and music it belongs to

		//Flowmotion
		if (!player.onGround() && Utils.isTouchingWall(player) && !playerData.getIsGliding()) {
			if (playerData.hasAirDashed() && playerData.isAbilityEquipped(ModAbilities.WALL_KICK)) {
				int grabs = playerData.getWallGrabs();
				if (playerData.getHangingInWallTicks() == 0 && grabs < playerData.getNumberOfAbilitiesEquipped(ModAbilities.WALL_KICK)) {
					playerData.setBounced(false);
					playerData.setHangingWallTicks(20);
					playerData.setWallGrabs(grabs + 1);
					playerData.setFlowmotion(true);//TODO packet?
					if (!player.level().isClientSide) {
						//PacketHandler.syncToAllAround(player, playerData);
						float radius = 0.5F;
						for (int i = 0; i < 10; i++) {
							((ServerLevel) player.level()).sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX() - Math.random() * (radius * 2) + radius, player.getY(), player.getZ() - Math.random() * (radius * 2) + radius, 100, 0, 0, 0, 0);
						}
					}

					player.level().playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.wall_grab.get(), SoundSource.PLAYERS);
					player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1, false, false, false));
					if (player.level().isClientSide) {
						InputHandler.jumpRayTrace = InputHandler.getMouseOverExtendedStraight(20);
					}
				}
			}
			if (playerData.getHangingInWallTicks() > 0) {
				if (!playerData.hasBounced()) player.setDeltaMovement(0, 0, 0);
			}

		} else {
			//If no longer touching wall reset
			if (playerData.getHangingInWallTicks() > 0) playerData.setHangingWallTicks(0);
		}

		//Ticker
		if (playerData.getHangingInWallTicks() > 0) {
			playerData.setHangingWallTicks(playerData.getHangingInWallTicks() - 1);
		}

		if (playerData.inFlowmotion()) {
			float radius = 0.3F;

			if (!player.level().isClientSide()) {
				((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(1F, 0.5F, 1), 1F), player.getX() - Math.random() * (radius * 2) + radius, player.getY() + Math.random() * player.getBbHeight(), player.getZ() - Math.random() * (radius * 2) + radius, 1, 0, 0, 0, 0);
				((ServerLevel) player.level()).sendParticles(new DustParticleOptions(new Vector3f(0.2F, 0.8F, 1), 1F), player.getX() - Math.random() * (radius * 2) + radius, player.getY() + Math.random() * player.getBbHeight(), player.getZ() - Math.random() * (radius * 2) + radius, 1, 0, 0, 0, 0);
			}
		}

		//If in ground reset
		if (player.onGround()) {
			if (player.hasEffect(MobEffects.GLOWING)) player.removeEffect(MobEffects.GLOWING);
			playerData.setAirDashed(false);
			playerData.setBounced(false);
			playerData.setHangingWallTicks(0);
			playerData.setWallGrabs(0);

			// Only remove flowmotion if player is touching ground AND NOT on a flowmotion rail
			if (!FlowmotionRailBlock.isOn(player)) {
				playerData.setFlowmotion(false);
			}
		}

		boolean canHitMini = player.fallDistance > 0.1F || player.isSprinting();

		if (canHitMini && !player.level().isClientSide() && !player.hasEffect(ModMobEffects.MINI)) {
			List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(0.1D));

			for (LivingEntity target : entities) {
				if (target == player)
					continue;

				if (!target.hasEffect(ModMobEffects.MINI))
					continue;

				if (!player.getBoundingBox().intersects(target.getBoundingBox()))
					continue;

				if (target.invulnerableTime > 0)
					continue;

				if (player.fallDistance > 0.1F && player.getY() > target.getY()) { //Step on mini enemy
					float damage = player.fallDistance;
					target.hurt(player.damageSources().playerAttack(player), damage);
					target.knockback(3F, target.getX() - player.getX(), target.getZ() - player.getZ());
					player.setDeltaMovement(player.getDeltaMovement().x, 0.4, player.getDeltaMovement().z);
				} else if(player.isSprinting()) { //Run into it
					target.knockback(2F, target.getX() - player.getX(), target.getZ() - player.getZ());
					target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.OFFHAND, player, player), 2.0F);
				}
			}
		}

	}

	@SubscribeEvent
	public void onLivingUpdate(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof LivingEntity entity) {
			GlobalData globalData = GlobalData.get(entity);
			PlayerData playerData = null;
			if (event.getEntity() instanceof Player player) {
				playerData = PlayerData.get(player);
				if (playerData != null) {
					// Drive form speed
					if (!playerData.noFormActive()) {
						DriveForm form = ModDriveForms.registry.get(playerData.getActiveDriveForm());
						if (player.onGround() && player.getBlockStateOn().getFriction(player.level(), player.blockPosition(), player) <= 0.6F) {
							player.setDeltaMovement(player.getDeltaMovement().multiply(new Vec3(form.getSpeedMult(), 1, form.getSpeedMult())));
						}
					}

					if (!playerData.getAirStep().equals(new BlockPos(0, 0, 0))) {
						airstepTicks++;

						BlockPos pos = playerData.getAirStep();
						float speedFactor = 0.3F;

						if (pos.distToCenterSqr(player.position()) < 2 || (airstepTicks > 5 && player.getDeltaMovement().x() == 0 && player.getDeltaMovement().z() == 0)) {
							player.setDeltaMovement(0, 0, 0);
							player.setPos(pos.getCenter().subtract(0, 0.4, 0));
							//player.addEffect(new MobEffectInstance(MobEffects.LEVITATION,0,0));
							if (player.level().isClientSide) {
								PacketHandler.sendToServer(new CSSetAirStepPacket(new BlockPos(0, 0, 0), 0));
								airstepTicks = -1;
							}
						}
						if (airstepTicks > -1) player.setDeltaMovement((pos.getX() - player.getX()) * speedFactor, (pos.getY() - player.getY()) * speedFactor, (pos.getZ() - player.getZ()) * speedFactor);
					}
				}
			}

			if (globalData != null) {
				if (globalData.getStopModelTicks() > 0) {
					globalData.setStopModelTicks(globalData.getStopModelTicks() - 1);
					if (globalData.getStopModelTicks() <= 0) {
						PacketHandler.syncToAllAround(entity, globalData);
					}
				}

				//Stop effect hitting the entities with the real damage every 5 ticks once it expires
				if (!entity.hasEffect(ModMobEffects.STOP) && !globalData.getStopDamage().isEmpty()) {
					if (globalData.getStopCaster() != null && !entity.hasEffect(ModMobEffects.KO) && entity.tickCount % 4 == 0) {
						Player player = Utils.getPlayerByName(entity.level(), globalData.getStopCaster().toLowerCase());
						if (player != null) {
							entity.hurt(StopDamageSource.getStopDamage(player), globalData.getStopDamage().getFirst() / 3F);
							globalData.getStopDamage().removeFirst();
							entity.invulnerableTime = 4;
							if (globalData.getStopDamage().isEmpty()) {
								globalData.setStopCaster(null);
							}
						}
					}
				}
			}

			if (entity instanceof Player player && playerData != null) {
				// Rotation ticks should always be lost, this way we prevent the spinning animation on other players (hopefully)
				if (playerData.getAerialDodgeTicks() > 0) {
					playerData.setAerialDodgeTicks(playerData.getAerialDodgeTicks() - 1);
				}

				// Reflect
				if (playerData.getReflectTicks() > 0) {
					playerData.remReflectTicks(1);

					entity.setDeltaMovement(0, 0, 0);
					entity.hurtMarked = true;

					// Spawn particles
					float radius = 1.5F;
					double X = entity.getX();
					double Y = entity.getY();
					double Z = entity.getZ();

					for (int t = 1; t < 360; t += 20) {
						double radT = Math.toRadians(t);
						double sinT = Math.sin(radT);
						double y = Y + (radius * Math.cos(radT));
						for (int s = 1; s < 360; s += 20) {
							double radS = Math.toRadians(s);
							double x = X + (radius * Math.cos(radS) * sinT);
							double z = Z + (radius * Math.sin(radS) * sinT);
							entity.level().addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
						}
					}

				} else { // When it finishes
					if (playerData.getReflectActive() && !player.level().isClientSide()) {// If has been hit
						// SPAWN ENTITY and apply damage
						float radius = 2F + playerData.getReflectLevel() * 0.5F;
						float dmgMult = ModMagic.registry.get(ModMagic.REFLECT.location()).getDamageMult(); //TODO reflect level?

						List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
						Utils.removeAllies(player, list);

						double X = entity.getX();
						double Y = entity.getY();
						double Z = entity.getZ();

						for (int t = 1; t < 360; t += 20) {
							double radT = Math.toRadians(t);
							double x = X + (radius * Math.cos(radT));
							double z = Z + (radius * Math.sin(radT));
							((ServerLevel) entity.level()).sendParticles(ParticleTypes.BUBBLE.getType(), x, Y + 1, z, 5, 0, 0, 0, 1);
						}
						if (!list.isEmpty()) {
							DamageSource damageSource = KKDamageTypes.getElementalDamage(KKDamageTypes.AIR, player, player);
							float dmg = DamageCalculation.getMagicDamage(player) * dmgMult;

							for (Entity e : list) {
								e.hurt(damageSource, dmg);
							}
							player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.reflect2.get(), SoundSource.PLAYERS, 1F, 1F);

						}
						playerData.setReflectActive(false); // Restart reflect
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void entityPickup(ItemEntityPickupEvent.Pre event) {
		if (event.getItemEntity().getItem() != null) {
			if (event.getItemEntity().getItem().getItem() instanceof SynthesisItem) {
				for (int i = 0; i < event.getPlayer().getInventory().getContainerSize(); i++) {
					ItemStack bag = event.getPlayer().getInventory().getItem(i);
					if (!ItemStack.matches(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.synthesisBag.get()) {
							IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM, null);
							if (addToBag(inv, event, bag)) {
								Player picker = event.getPlayer();
								picker.level().playSound(null, picker.blockPosition(), ModSounds.synthesisPickup.get(), SoundSource.PLAYERS, 0.6F, 1F);
							}
						}
					}
				}
			} else if (event.getItemEntity().getItem().getItem() instanceof MagicSpellItem spell) {
				PlayerData playerData = PlayerData.get(event.getPlayer());
				if(playerData.getTotalMaterialAmount(spell) == 0){
					playerData.setTotalMaterial(spell,1);
				}
				for (int i = 0; i < event.getPlayer().getInventory().getContainerSize(); i++) {
					ItemStack bag = event.getPlayer().getInventory().getItem(i);
					if (!ItemStack.matches(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.magicsBag.get()) {
							IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM, null);
							addToBag(inv, event, bag);
						}
					}
				}
			} else if (event.getItemEntity().getItem().getItem() instanceof MapCardItem) {
				for (int i = 0; i < event.getPlayer().getInventory().getContainerSize(); i++) {
					ItemStack bag = event.getPlayer().getInventory().getItem(i);
					if (!ItemStack.matches(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.cardsBag.get()) {
							IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM, null);
							addToBag(inv, event, bag);
						}
					}
				}
			} else if (event.getItemEntity().getItem().getItem() instanceof ShotlockItem) {
				for (int i = 0; i < event.getPlayer().getInventory().getContainerSize(); i++) {
					ItemStack bag = event.getPlayer().getInventory().getItem(i);
					if (!ItemStack.matches(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.shotlocksBag.get()) {
							IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM, null);
							addToBag(inv, event, bag);
						}
					}
				}
			}
		}
	}

	public boolean addToBag(IItemHandler inv, ItemEntityPickupEvent.Pre event, ItemStack bag) {
		int bagLevel = bag.getOrDefault(ModComponents.BAG_LEVEL, 0);
		int maxSlots = switch (bagLevel) {
			case 0 -> 18;
			case 1 -> 36;
			case 2 -> 54;
			case 3 -> 72;
			default -> 0;
		};

		ItemStack onGround = event.getItemEntity().getItem();
		if (onGround.isEmpty()) {
			return false;
		}

		ItemStack remaining = onGround.copy();

		for (int j = 0; j < maxSlots && !remaining.isEmpty(); j++) {
			remaining = inv.insertItem(j, remaining, false);
		}

		if (remaining.isEmpty()) {
			event.setCanPickup(TriState.FALSE);
			event.getItemEntity().discard();
			return true;
		}

		onGround.setCount(remaining.getCount());
		return false;
	}

	@SubscribeEvent
	public void hitEntity(LivingDamageEvent.Pre event) {
		if (event.getEntity() instanceof Player hurt && !hurt.level().isClientSide) {
			event.setNewDamage(CombatAbilities.survive(hurt, PlayerData.get(hurt), event.getNewDamage()));
		}
		/*if(event.getEntity() instanceof LivingEntity khmob){
			if(event.getSource().getDirectEntity() instanceof Player player){
				WorldData worldData = WorldData.get(player.getServer());
				Party p = worldData.getPartyFromMember(player.getUUID());
				worldData.addPartyMember(p,khmob);
				PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
			}
		}*/
		if (event.getSource().getEntity() instanceof Player attacker && event.getEntity() instanceof Player victim) {
			WorldData worldData = WorldData.get(attacker.getServer());
			Struggle struggle = worldData.getStruggleFromActiveCombatant(attacker.getUUID());
			boolean isStruggleHit = struggle != null && struggle.isInProgress() && struggle.getActiveCombatantIds().contains(attacker.getUUID()) && struggle.getActiveCombatantIds().contains(victim.getUUID());

			KingdomKeys.LOGGER.debug("Struggle hit check: attacker={} victim={} struggleFound={} inProgress={} activeCombatants={} result={}", attacker.getGameProfile().getName(), victim.getGameProfile().getName(), struggle != null, struggle != null && struggle.isInProgress(), struggle != null ? struggle.getActiveCombatantIds() : "n/a", isStruggleHit);
			if (isStruggleHit) {
				float originalDamage = event.getNewDamage();
				event.setNewDamage(0); // never actually hurt each other during a Struggle match

				if (Struggle.weapons().contains(attacker.getMainHandItem().getItem())) {
					int rawPoints = Mth.clamp(Math.round(originalDamage * struggle.getDamageMult() / 100F), 1, 50);

					Struggle.Participant victimParticipant = struggle.getParticipant(victim.getUUID());
					int points = Math.min(rawPoints, victimParticipant.getScore());
					if (points > 0) {
						victimParticipant.setScore(victimParticipant.getScore() - points);
						worldData.setDirty();
						PacketHandler.sendToAll(new SCSyncWorldData(attacker.getServer()));

						PlayerData victimData = PlayerData.get(victim);
						int orbColor = victimData != null ? victimData.getNotifColor() : 0xFFFFFF;
						for (int i = 0; i < points; i++) {
							StruggleOrbEntity orb = new StruggleOrbEntity(victim.level(), victim.getX(), victim.getY() + victim.getBbHeight() / 2, victim.getZ(), orbColor, struggle.getName());
							victim.level().addFreshEntity(orb);
						}
					}
				}
				return; // this hit was fully handled as struggle combat, skip the normal damage pipeline below
			}
		}
		if (event.getSource().getEntity() instanceof Player player) {
			//First we calculate the weapon damage
			ItemStack weapon = Utils.getWeaponDamageStack(event.getSource(), player);
			if (weapon != null && !(event.getSource() instanceof StopDamageSource)) {
				float dmg = 0;
				if (weapon.getItem() instanceof KeybladeItem) {
					dmg = DamageCalculation.getKBStrengthDamage(player, weapon);
				} else if (weapon.getItem() instanceof IOrgWeapon) {
					dmg = DamageCalculation.getOrgStrengthDamage(player, weapon);
				}

				dmg *= EpicFightUtils.getCritMulti(player);
				float newDMG = (event.getNewDamage() - 1) + dmg * player.getAttackStrengthScale(0);
				event.setNewDamage(newDMG);
			}

			PlayerData playerData = PlayerData.get(player);
			if (playerData != null && playerData.isFormActive(ModDriveForms.ANTI)) {
				event.setNewDamage(playerData.getStrength(true));
			}

			if (event.getEntity() instanceof TrainingDummyEntity dummy) {
				dummy.recordDamage(player, event.getNewDamage(), event.getSource());

				float maxDamage = 120f;
				float damage = event.getNewDamage();
				float normalized = Mth.clamp(damage / maxDamage, 0f, 1f);
				float strength = (float) Math.pow(normalized, 0.7f);
				strength = Math.max(strength, 0.08f);

				dummy.getEntityData().set(TrainingDummyEntity.HIT_STRENGTH, strength);

				int duration = (int) (8 + strength * 10);
				dummy.getEntityData().set(TrainingDummyEntity.HIT_TICKS, duration);

				if (dummy.getIgnoreCD()) dummy.invulnerableTime = 0;
			}
		}

		if (event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);

			if (playerData == null) return;

			if (playerData.getReflectTicks() <= 0) { // If is casting reflect
				if (playerData.isAbilityEquipped(ModAbilities.MP_RAGE)) {
					playerData.addMP((event.getNewDamage() * 0.2F) * playerData.getNumberOfAbilitiesEquipped(ModAbilities.MP_RAGE));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}

				if (playerData.isAbilityEquipped(ModAbilities.DAMAGE_DRIVE)) {
					playerData.addDP(player, (event.getNewDamage() * 0.2F) * playerData.getNumberOfAbilitiesEquipped(ModAbilities.DAMAGE_DRIVE));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}

			//KO Method
			WorldData worldData = WorldData.get(player.getServer());
			if (worldData != null && worldData.getPartyFromMember(player.getUUID()) != null) { //If the player gets hit and data is not null
				Party p = worldData.getPartyFromMember(player.getUUID());
				if (Utils.anyPartyMemberOnExcept(player, p, (ServerLevel) player.level())) { //If there's a party member on at this point
					if (ModConfigs.SERVER.allowPartyKO.get()) { //If KO is allowed
						if (player.getHealth() - event.getNewDamage() <= 0) { //If gets hit by a mortal attack
							if (!player.hasEffect(ModMobEffects.KO)) { // We only set KO if player gets hit enough to kill them (but doesn't kill them yet) while not KO already
								event.setNewDamage(0);
								player.removeAllEffects();
								player.setHealth(player.getMaxHealth());
								player.invulnerableTime = 40;
								player.getFoodData().setFoodLevel(10);
								player.getFoodData().setExhaustion(0);
								player.getFoodData().setSaturation(0);
								MobEffectInstance koInstance = new MobEffectInstance(ModMobEffects.KO, MobEffectInstance.INFINITE_DURATION, 0, false, false, false);
								player.addEffect(koInstance);
								player.level().playSound(null, player.blockPosition(), ModSounds.playerDeathHardcore.get(), SoundSource.PLAYERS);
							}
						}
						return;
					} else { //If config does not allow prevent KO from being applied
						player.removeEffect(ModMobEffects.KO);
					}
				}
			}
		}

		// This is outside as it should apply the formula if you have been hit by non player too
		if (event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);

			float damage = event.getNewDamage() * 100 / (100 + playerData.getDefense(true));
			if (player.hasEffect(ModMobEffects.AERO)) {
				MobEffectInstance aero = player.getEffect(ModMobEffects.AERO);

				float resistMultiplier = aero.getAmplifier() == 0 ? 0.3F : aero.getAmplifier() == 1 ? 0.35F : aero.getAmplifier() == 2 ? 0.4F : 0;
				damage -= (damage * resistMultiplier);
			}

			if (event.getSource().getMsgId().equals(KKResistanceType.fire.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.fire.toString())) / 100F;
			} else if (event.getSource().getMsgId().equals(KKResistanceType.ice.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.ice.toString())) / 100F;
			} else if (event.getSource().getMsgId().equals(KKResistanceType.lightning.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.lightning.toString())) / 100F;
			} else if (event.getSource().getMsgId().equals(KKResistanceType.darkness.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.darkness.toString())) / 100F;
			}

			// Damage Control
			if (Utils.isPlayerLowHP(player) && playerData.isAbilityEquipped(ModAbilities.DAMAGE_CONTROL)) {
				damage /= (1 + playerData.getNumberOfAbilitiesEquipped(ModAbilities.DAMAGE_CONTROL));
			}

			// Protect Abilities
			float protectReduction;
			if (playerData.isAbilityEquipped(ModAbilities.PROTECT)) {
				protectReduction = damage * 0.1F;
				damage -= protectReduction;
			}
			if (playerData.isAbilityEquipped(ModAbilities.PROTECTRA)) {
				protectReduction = damage * 0.2F;
				damage -= protectReduction;
			}
			if (playerData.isAbilityEquipped(ModAbilities.PROTECTGA)) {
				protectReduction = damage * 0.4F;
				damage -= protectReduction;
			}

			// Has to evaluate last
			// Second chance (will save the player from a damage that would've killed him as long as he had 2 hp or more)
			if (playerData.isAbilityEquipped(ModAbilities.SECOND_CHANCE)) {
				if (damage >= player.getHealth() && player.getHealth() > 1) {
					if (player.hasEffect(MobEffects.REGENERATION)) {
						player.removeEffect(MobEffects.REGENERATION);
						player.effectsDirty = true;
					}
					damage = player.getHealth() - 1;
				}
			}

			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			PacketHandler.sendTo(new SCSyncGlobalData(player), (ServerPlayer) player);

			if (!player.isDamageSourceBlocked(event.getSource())) event.setNewDamage(damage <= 0 ? 1 : damage);
		}

		// Mobs defense formula
		if (event.getEntity() instanceof BaseKHEntity) {
			float damage = event.getNewDamage();
			int defense = ((BaseKHEntity) event.getEntity()).getDefense();
			if (defense > 0) damage = (float) Math.round((damage * 100 / (300 + defense)));

			if (event.getEntity().hasEffect(ModMobEffects.AERO)) {
				MobEffectInstance aero = event.getEntity().getEffect(ModMobEffects.AERO);

				float resistMultiplier = aero.getAmplifier() == 0 ? 0.3F : aero.getAmplifier() == 1 ? 0.35F : aero.getAmplifier() == 2 ? 0.4F : 0;
				damage -= (damage * resistMultiplier);
			}

			// Marluxia's last stand: the blow that would have killed him leaves him on 1 HP and starts the finisher instead. beginFinisher handles the invulnerability, so it can also end it.
			if (event.getEntity() instanceof MarluxiaEntity mar) {
				if (mar.getState() != MarluxiaGoal.STATE_FINISHER && !mar.marluxiaGoal.hasUsedFinisher() && mar.getHealth() - damage <= 0) {
					mar.marluxiaGoal.beginFinisher();
					event.setNewDamage(mar.getHealth() - 1);
					return;
				}

				if (mar.isArmored()) {
					damage = event.getNewDamage() * 0.1F;
					// Burning the petals off is the intended answer to the armor phase.
					if (event.getSource().is(KKDamageTypes.FIRE)) {
						mar.marluxiaGoal.breakArmor();
					}
				}
			}
			event.setNewDamage(damage < 1 ? 1 : damage);
		}

	}

	@SubscribeEvent
	public void onLivingEquipArmor(LivingEquipmentChangeEvent event) {
		if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
			if (event.getSlot().isArmor()) {
				Utils.updateOrgRobesTeam(player);
			}
			if (event.getSlot() == EquipmentSlot.MAINHAND || event.getSlot() == EquipmentSlot.OFFHAND) {
				ModAdvancements.triggerDualWield(player, player.getMainHandItem().getItem(), player.getOffhandItem().getItem());
			}
		}
	}

	// Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingIncomingDamageEvent event) {
		if (!event.getEntity().level().isClientSide) {
			if (event.getEntity() instanceof Player guardingPlayer) {
				PlayerData playerData = PlayerData.get(guardingPlayer);

				if (CombatAbilities.blocks(guardingPlayer, playerData, event.getSource())) {
					CombatAbilities.onBlocked(guardingPlayer, playerData);
					event.setCanceled(true);
					return;
				}

				// Anything that gets through is a hit to remember, both for the combo Once More watches and for the moment of flight Aerial Recovery can be caught in
				if (playerData != null) {
					CombatAbilities.noteHit(guardingPlayer, playerData);
					CombatAbilities.noteKnockback(guardingPlayer, playerData);
				}
			}

			if (event.getSource().getEntity() instanceof LivingEntity attacker) { // If attacker is a LivingEntity
				LivingEntity target = event.getEntity();

				if (!Utils.canHarm(attacker, target)) {
					event.setCanceled(true);
				}

				if (target instanceof Player) {
					PlayerData playerData = PlayerData.get((Player) target);
					if (playerData != null) {
						if (playerData.getReflectTicks() > 0) { // If is casting reflect
							if (!playerData.getReflectActive()) // If has been hit while casting reflect
								playerData.setReflectActive(true);
							event.setCanceled(true);
						}
					}
				}

				// Stop
				GlobalData globalData = GlobalData.get(target);
				if (globalData != null && event.getSource().getEntity() instanceof Player source) {
					if (target.hasEffect(ModMobEffects.STOP)) {
						if (target.getEffect(ModMobEffects.STOP).getDuration() > 0) {
							float dmg = event.getAmount();
							ItemStack stack = Utils.getWeaponDamageStack(event.getSource(), source);
							if (stack != null) {
								if (stack.getItem() instanceof KeybladeItem) {
									dmg = DamageCalculation.getKBStrengthDamage((Player) event.getSource().getEntity(), stack);
								} else if (stack.getItem() instanceof IOrgWeapon) {
									dmg = DamageCalculation.getOrgStrengthDamage((Player) event.getSource().getEntity(), stack);
								}
							}

							if (dmg == 0) {
								dmg = event.getAmount();
							}

							globalData.addDamage(dmg);
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if (!event.getEntity().level().isClientSide) {
			LivingEntity entity = event.getEntity();
			Level level = event.getEntity().level();

			//Castle oblivion
			if (CastleOblivionHandler.isInterior(level.dimension())) {
				if (!(entity instanceof Player)) {
					if (GlobalData.get(entity).getCastleOblivionMarker()) {
						CastleOblivionData.InteriorData.get((ServerLevel) level).ifPresent(interiorData -> {
							Room room = interiorData.getRoomAtPos(entity.blockPosition());

							boolean replaced = false;
							List<DropModifier> modifiers =  room.getModifiers(ModRoomModifiers.DROP.get());
							for (DropModifier dropModifier : modifiers) {
								boolean shouldDrop = true;
								if (dropModifier.getChance() < 100) {
									shouldDrop = Utils.randomWithRange(1, 100) < dropModifier.getChance();
								}
								if (shouldDrop) {
									level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), dropModifier.getItem()));
									//if any of the modifiers replace the drops then the rest can be ignored
									if (!replaced) {
										replaced = dropModifier.replaceCard();
									}
								}
							}

							if (!replaced) {
								List<Item> cardDrops = ModTags.getItemsInTag(level, ModTags.MAP_CARD);
								Item toDrop = cardDrops.get(Utils.randomWithRange(0, cardDrops.size() - 1));
								ItemStack dropStack = new ItemStack(toDrop);
								MapCardItem.initialize(dropStack);
								level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), dropStack));
							}

							room.removeCurrentSpawn();
							room.removeEntityFromCache(entity);
							KingdomKeys.LOGGER.debug("CO spawned mob died {} remaining", room.getCurrentlySpawned());
						});
					}
				}
			}

			// EnderDragon killed makes heartless spawn if mode is 3
			WorldData worldData = WorldData.get(event.getEntity().getServer());
			if (event.getEntity() instanceof EnderDragon) {
				if (worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_DRAGON) {
					worldData.setHeartlessSpawnLevel(1);
				}

				for (Player p : entity.level().players()) {
					entity.level().addFreshEntity(new ItemEntity(entity.level(), p.getX(), p.getY(), p.getZ(), new ItemStack(ModItems.proofOfHeart.get(), 1)));
				}
			}

			if (event.getEntity() instanceof Player player) {
				if (player.level().getLevelData().isHardcore()) player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.playerDeathHardcore.get(), SoundSource.PLAYERS, 1F, 1F);
				else player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.playerDeath.get(), SoundSource.PLAYERS, 1F, 1F);
			}

			Player player = null; // we store if the player is either the direct hit or the indirect
			if (event.getSource().getDirectEntity() instanceof Player pl) { // If the player kills
				player = pl;
			} else if (event.getSource().getEntity() instanceof Player pl) {
				player = pl;
			}

			//Kill sound
			if (player != null && entity instanceof IKHMob killed) {
				SoundEvent death = switch (killed.getKHMobType()) {
					case HEARTLESS_PUREBLOOD, HEARTLESS_EMBLEM -> ModSounds.heartlessKill.get();
					case NOBODY -> ModSounds.nobodyKill.get();
					default -> null;
				};

				if (death != null) {
					level.playSound(null, entity.blockPosition(), death, SoundSource.HOSTILE, 1F, 1F);
				}
			}

			if (player != null) {
				PlayerData playerData = PlayerData.get(player);

				// TODO more sophisticated and dynamic way to do this
				// Give hearts
				if (player.getMainHandItem().getItem() instanceof IOrgWeapon || player.getMainHandItem().getItem() instanceof KeybladeItem || event.getSource().getDirectEntity() instanceof KKThrowableEntity) {
					int multiplier = getMultiplier(event, player, playerData);
					if (event.getEntity() instanceof IKHMob mob) {
						if (mob.getKHMobType() == MobType.HEARTLESS_EMBLEM) {
							playerData.addHearts((int) ((20 * multiplier) * ModConfigs.SERVER.heartMultiplier.get()));
						}
					} else if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
						playerData.addHearts((int) ((1000 * multiplier) * ModConfigs.SERVER.heartMultiplier.get()));
					} else if (event.getEntity() instanceof Villager) {
						playerData.addHearts((int) ((5 * multiplier) * ModConfigs.SERVER.heartMultiplier.get()));
					} else if (event.getEntity() instanceof Monster) {
						playerData.addHearts((int) ((2 * multiplier) * ModConfigs.SERVER.heartMultiplier.get()));
					} else {
						playerData.addHearts((int) ((multiplier) * ModConfigs.SERVER.heartMultiplier.get()));
					}
				}
				if (event.getEntity() instanceof IKHMob heartless) {
					if (heartless.getKHMobType() == MobType.HEARTLESS_EMBLEM && Utils.getWeaponDamageStack(event.getSource(), player) != null && Utils.getWeaponDamageStack(event.getSource(), player).getItem() instanceof KeybladeItem) {
						HeartEntity heart = new HeartEntity(event.getEntity().level());
						heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
						event.getEntity().level().addFreshEntity(heart);
					}
					if (heartless.getKHMobType() == MobType.HEARTLESS_PUREBLOOD && event.getEntity().level() instanceof ServerLevel) {
						for (int i = 0; i < 2; i++) {
							((ServerLevel) entity.level()).sendParticles(ParticleTypes.SMOKE, entity.getX() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getY() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getZ() + entity.level().random.nextDouble() / 2 - 0.25D, 50, 0, -1, 0, 0.3);
							((ServerLevel) entity.level()).sendParticles(ParticleTypes.SQUID_INK, entity.getX() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getY() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getZ() + entity.level().random.nextDouble() / 2 - 0.25D, 30, 0, -1, 0, 0.1);
						}
					}
				}

				if (event.getEntity().getClassification(false) == MobCategory.MONSTER) {
					if (!playerData.isAbilityEquipped(ModAbilities.ZERO_EXP)) {
						LivingEntity mob = event.getEntity();

						double health = mob.getAttributeValue(Attributes.MAX_HEALTH);
						double baseExp = Utils.randomWithRange(health * 0.4D, health * 0.9D);

						int clampedXP = (int) Math.max(baseExp * ModConfigs.SERVER.xpMultiplier.get(), 1);
						int clampedMagicXP = (int) Math.max(baseExp * ModConfigs.SERVER.magicXPMultiplier.get(), 1);

						if (mob instanceof WitherBoss) {
							clampedXP += 1500;
						}

						if (playerData.getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST) > 0 && player.getHealth() <= player.getMaxHealth() / 2) {
							clampedXP *= (1 + playerData.getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST));
							clampedMagicXP *= (1 + playerData.getNumberOfAbilitiesEquipped(ModAbilities.EXPERIENCE_BOOST));
						}

						Utils.addMagicExperience(player, clampedMagicXP);
						Utils.addShotlockExperience(player, clampedMagicXP);
						playerData.addExperience(player, clampedXP, true, true);
						player.level().addFreshEntity(new XPEntity(mob.level(), player, mob, clampedXP));
					}

					double x = entity.getX();
					double y = entity.getY();
					double z = entity.getZ();

					if (entity.level().random.nextInt(100) <= ModConfigs.munnyDropProbability) {
						int num = (int) Utils.randomWithRange(5, entity.getMaxHealth() / 5);
						num += playerData.getNumberOfAbilitiesEquipped(ModAbilities.JACKPOT) * 1.2;
						// reduce munny value by 2 for each level of drive converter
						num /= (1 + playerData.getNumberOfAbilitiesEquipped(ModAbilities.DRIVE_CONVERTER));
						entity.level().addFreshEntity(new MunnyEntity(event.getEntity().level(), x, y, z, num));
					}

					if (entity.level().random.nextInt(100) <= ModConfigs.hpDropProbability) {
						int num = (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5);
						num += playerData.getNumberOfAbilitiesEquipped(ModAbilities.JACKPOT) * 1.2;
						entity.level().addFreshEntity(new HPOrbEntity(event.getEntity().level(), x, y, z, num));
					}

					if (entity.level().random.nextInt(100) <= ModConfigs.mpDropProbability) {
						int num = (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5);
						num += playerData.getNumberOfAbilitiesEquipped(ModAbilities.JACKPOT) * 1.2;
						entity.level().addFreshEntity(new MPOrbEntity(event.getEntity().level(), x, y, z, num));
					}

					if (entity.level().random.nextInt(100) <= ModConfigs.driveDropProbability) {
						int num = (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.drivePointsMultiplier);
						num += num * playerData.getNumberOfAbilitiesEquipped(ModAbilities.JACKPOT) * 0.5;
						entity.level().addFreshEntity(new DriveOrbEntity(event.getEntity().level(), x, y, z, num));
					}

					if (entity.level().random.nextInt(100) <= ModConfigs.focusDropProbability) {
						int num = (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.focusPointsMultiplier);
						num += num * playerData.getNumberOfAbilitiesEquipped(ModAbilities.JACKPOT) * 0.25;
						entity.level().addFreshEntity(new FocusOrbEntity(event.getEntity().level(), x, y, z, num));
					}

					int num = Utils.randomWithRange(0, 99);
					if (num < ModConfigs.biomeMemoryDropChance + Utils.getLootingLevel(player)) {
						Item biomeMemoryItem = Utils.getMemoryFromBiome(level.getBiomeManager().getBiome(new BlockPos((int)x,(int)y,(int)z)));
						if(biomeMemoryItem != null) {
							ItemEntity ie = new ItemEntity(player.level(), x, y, z, new ItemStack(biomeMemoryItem));
							player.level().addFreshEntity(ie);
						}
					}

					num = Utils.randomWithRange(0, 99);
					if (num < ModConfigs.recipeDropChance + Utils.getLootingLevel(player)) {
						Item recipeTier = ModItems.recipeD.get();
						GlobalData mobData = GlobalData.get(entity);
						if (mobData != null) {
							int num2 = Utils.randomWithRange(0, mobData.getLevel() + 1);
							if (num2 < 15) {
								recipeTier = ModItems.recipeD.get();
							} else if (num2 < 30) {
								recipeTier = ModItems.recipeC.get();
							} else if (num2 < 60) {
								recipeTier = ModItems.recipeB.get();
							} else if (num2 < 90) {
								recipeTier = ModItems.recipeA.get();
							} else if (num2 < 150) {
								recipeTier = ModItems.recipeS.get();
							} else if (num2 < 200) {
								recipeTier = ModItems.recipeSS.get();
							} else if (num2 == 200) {
								recipeTier = ModItems.recipeSSS.get();
							}
						}
						ItemEntity ie = new ItemEntity(player.level(), x, y, z, new ItemStack(recipeTier));
						player.level().addFreshEntity(ie);

					}

					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}

			if (event.getEntity() instanceof MoogleEntity moogle) {
				ItemStack weapon = player != null ? player.getMainHandItem() : ItemStack.EMPTY;

				boolean killedByAnvil = event.getSource().getMsgId().equals("anvil");
				boolean killedByMace = !weapon.isEmpty() && weapon.getItem() instanceof MaceItem;

				if (killedByAnvil || killedByMace) {
					ItemEntity ie = new ItemEntity(event.getEntity().level(), moogle.getX(), moogle.getY(), moogle.getZ(), new ItemStack(ModBlocks.moogleProjector.get()));
					event.getEntity().level().addFreshEntity(ie);
				}
			}

			if (event.getSource().getEntity() instanceof IKHMob killerMob && ModConfigs.playerSpawnHeartless) {
				if (!event.getSource().getEntity().hasCustomName() && (killerMob.getKHMobType() == MobType.HEARTLESS_EMBLEM || killerMob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD)) {
					if (event.getEntity() instanceof Player) { // If a player gets killed by a heartless
						PlayerData playerData = PlayerData.get((Player) event.getEntity());

						String[] heartless = ModConfigs.playerSpawnHeartlessData.get(0).split(",");
						String[] nobody = ModConfigs.playerSpawnHeartlessData.get(1).split(",");

						DuskEntity newDusk = new DuskEntity(ModEntities.TYPE_DUSK.get(), event.getSource().getEntity().level());
						newDusk.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						newDusk.setCustomName(Component.translatable("kingdomkeys.entity.nobody_of", event.getEntity().getDisplayName().getString()));
						newDusk.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntity().getMaxHealth() * Double.parseDouble(nobody[1]) / 100, newDusk.getMaxHealth()));
						newDusk.heal(newDusk.getMaxHealth());
						newDusk.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength(true) * Double.parseDouble(nobody[2]) / 100, newDusk.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getEntity().level().addFreshEntity(newDusk);

						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getEntity().level());
						newShadow.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						newShadow.setCustomName(Component.translatable("kingdomkeys.entity.heartless_of", event.getEntity().getDisplayName().getString()));
						newShadow.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntity().getMaxHealth() * Double.parseDouble(heartless[1]) / 100, newShadow.getMaxHealth()));
						newShadow.heal(newShadow.getMaxHealth());

						newShadow.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength(true) * Double.parseDouble(heartless[2]) / 100, newShadow.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getEntity().level().addFreshEntity(newShadow);

						HeartEntity heart = new HeartEntity(event.getEntity().level());
						heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
						event.getEntity().level().addFreshEntity(heart);

					} else if (event.getEntity() instanceof Villager) {
						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getEntity().level());
						newShadow.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						event.getSource().getEntity().level().addFreshEntity(newShadow);

						HeartEntity heart = new HeartEntity(event.getEntity().level());
						heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
						event.getEntity().level().addFreshEntity(heart);

					}
				}
			}
			if (event.getEntity() instanceof MarluxiaEntity && event.getSource().getEntity() instanceof Player && event.getSource().getEntity().level().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
				ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace("overworld"));
				BlockPos coords = DimensionCommand.getWorldCoords(player, dimension);
				player.changeDimension(new DimensionTransition(player.getServer().getLevel(dimension), new Vec3(coords.getX(), coords.getY(), coords.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), e -> {
				}));
			}
		}
	}

	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if (event.getEntity() instanceof LivingEntity entity) {
			if (entity.getVehicle() instanceof GummiShipEntity) {
				event.setDistance(0);
			}
		}
		if (event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			// Check to prevent edge case crash
			if (playerData != null && playerData.getActiveDriveForm() != null) {
				if (!playerData.getActiveDriveForm().equals(DriveForm.NONE)) {
					event.setDistance(0);
				} else {
					if (playerData.isAbilityEquipped(ModAbilities.HIGH_JUMP) || playerData.isAbilityEquipped(ModAbilities.AERIAL_DODGE) || playerData.isAbilityEquipped(ModAbilities.GLIDE) || playerData.isAbilityEquipped(ModAbilities.WALL_KICK)) {
						event.setDistance(0);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if (!event.getLevel().isClientSide()) {
			if (!event.getPlayer().isCreative() && event.getPlayer().getMainHandItem().getEnchantmentLevel(event.getLevel().holderOrThrow(Enchantments.SILK_TOUCH)) == 0) {
				if (event.getState().getBlock() == ModBlocks.prizeBlox.get()) {
					event.getLevel().addFreshEntity(new MunnyEntity((Level) event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Utils.randomWithRange(50, 200)));
				} else if (event.getState().getBlock() == ModBlocks.rarePrizeBlox.get()) {
					event.getLevel().addFreshEntity(new MunnyEntity((Level) event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Utils.randomWithRange(300, 500)));
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (!event.getEntity().level().isClientSide) {
			Player oPlayer = event.getOriginal();
			Player nPlayer = event.getEntity();

			nPlayer.setHealth(PlayerData.get(oPlayer).getMaxHP());
			nPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(PlayerData.get(oPlayer).getMaxHP());
			Utils.RefreshAbilityAttributes(nPlayer, PlayerData.get(nPlayer));

			PacketHandler.sendTo(new SCSyncWorldData(nPlayer.getServer()), (ServerPlayer) nPlayer);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!event.getEntity().level().isClientSide) {
			Player nPlayer = event.getEntity();

			WorldData newWorldData = WorldData.get(nPlayer.getServer());
			final PlayerData playerData = PlayerData.get(nPlayer);
			nPlayer.setHealth(playerData.getMaxHP());
			Utils.RefreshAbilityAttributes(nPlayer, playerData);


			PacketHandler.sendTo(new SCSyncWorldData(nPlayer.getServer()), (ServerPlayer) nPlayer);

			if (!event.isEndConquered() && !nPlayer.level().isClientSide()) {
				if (playerData.getRespawnROD() && ModConfigs.respawnROD) {
					ServerPlayer sPlayer = (ServerPlayer) nPlayer;
					ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, KingdomKeys.rl("realm_of_darkness"));
					ServerLevel serverlevel = ((ServerLevel) sPlayer.level()).getServer().getLevel(dimension);
					BlockPos pos = serverlevel.getSharedSpawnPos();
					sPlayer.changeDimension(new DimensionTransition(serverlevel, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, sPlayer.getYRot(), sPlayer.getXRot(), entity -> {
					}));
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
			ItemStack crafted = event.getCrafting();
			if (crafted.getItem() == Items.PLAYER_HEAD && crafted.has(DataComponents.PROFILE)) {
				ResolvableProfile profile = crafted.get(DataComponents.PROFILE);
				profile.name().ifPresent(name -> ModAdvancements.triggerCraftProfileHead(player, name));
			}
		}
	}

	@SubscribeEvent
	public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent e) {
		Player player = e.getEntity();
		if (!player.level().isClientSide) {
			PlayerData playerData = PlayerData.get(player);
			ServerLevel world = player.getServer().getLevel(e.getTo());
			// The star map's markers aren't saved to disk, so they're put back whenever anyone arrives -
			// including by command, not just by taking off.
			if (e.getTo() == ModDimensions.WORLDMAP && world != null) {
				WorldMap.ensureMarkers(world);
			}
			if (e.getTo() == ModDimensions.STATION_OF_SORROW) {
				BlockPos blockPos = player.blockPosition().below(2);
				world.setBlock(blockPos, ModBlocks.sorCore.get().defaultBlockState(), 2);
				if (world.getBlockEntity(blockPos) instanceof SoRCoreTileEntity te) {
					te.setUUID(player.getUUID());
				}
			}

			Utils.RefreshAbilityAttributes(player, playerData);
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			PacketHandler.sendTo(new SCSyncWorldData(world.getServer()), (ServerPlayer) player);
		}
	}

	// Sync drive form on Start Tracking
	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		Player localPlayer = e.getEntity();
		PlayerData playerData = PlayerData.get(localPlayer);
		GlobalData globalData = GlobalData.get(localPlayer);
		if (localPlayer == null || playerData == null || globalData == null) return;

		if (e.getTarget() instanceof Player targetPlayer) {
			PacketHandler.syncToAllAround(localPlayer, playerData);
			PacketHandler.syncToAllAround(localPlayer, globalData);
			PlayerData targetPlayerData = PlayerData.get(targetPlayer);
			GlobalData globalData2 = GlobalData.get(targetPlayer);
			if (targetPlayerData != null && globalData2 != null) {
				PacketHandler.syncToAllAround(targetPlayer, targetPlayerData);
				PacketHandler.syncToAllAround(targetPlayer, globalData2);
			}
		}

		if (!localPlayer.level().isClientSide) {
			localPlayer.level().getServer().getPlayerList().getPlayers().forEach(sPlayer -> {
				localPlayer.getActiveEffects().forEach(mobEffectInstance -> {
					sPlayer.connection.send(new ClientboundUpdateMobEffectPacket(localPlayer.getId(), mobEffectInstance, false));
				});
			});
		}
	}

	@SubscribeEvent
	public void playerStoppedTracking(PlayerEvent.StopTracking e) {
		if (!e.getEntity().level().isClientSide()) {
			Player player = e.getEntity();
			PlayerData playerData = PlayerData.get(player);
			WorldData worldData = WorldData.get(player.getServer());
			if (playerData == null || worldData == null) return;

			Party p = worldData.getPartyFromMember(player.getUUID());
			if (p == null) return;

			Member m = p.getMember(player.getUUID());
			m.setLevel(playerData.getLevel());
			m.setHP((int) player.getMaxHealth());
			m.setMP((int) playerData.getMaxMP());
			PacketHandler.sendTo(new SCSyncWorldData(player.getServer()), (ServerPlayer) player);
		}
	}

	//Prevent using Anvil with items that aren't enchanted books or empty for renaming. Mainly to stop infinite enchantments with Apotheosis
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void anvilUpdate(AnvilUpdateEvent event) {
		Item item = event.getLeft().getItem();
		if (item instanceof KeybladeItem || item instanceof IOrgWeapon || item instanceof KeybladeArmorItem) {
			if (!event.getRight().isEmpty() && !(event.getRight().getItem() instanceof EnchantedBookItem)) {
				event.setOutput(ItemStack.EMPTY);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void armourHurt(ArmorHurtEvent event) {
		if (!event.getEntity().level().isClientSide()) {
			if (event.getEntity() instanceof Player player) {
				for (int i = 5; i > 2; i--) {
					ItemStack stack = event.getArmorItemStack(EquipmentSlot.values()[i]);
					float damage = event.getNewDamage(EquipmentSlot.values()[i]);
					if (!stack.isEmpty() && !stack.has(DataComponents.UNBREAKABLE) && stack.getMaxDamage() > 0) {
						if (stack.getDamageValue() + damage >= stack.getMaxDamage()) {
							if (Utils.hasArmorID(stack)) {
								ItemStack pauldron = PlayerData.get(player).getEquippedKBArmor(0);
								PauldronInventory pauldronInventory = (PauldronInventory) pauldron.getCapability(Capabilities.ItemHandler.ITEM);
								if (pauldronInventory != null) {
									pauldronInventory.setStackInSlot(-(i - 5), ItemStack.EMPTY);
									player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void effectAdded(MobEffectEvent.Added event) {
		if (event.getEntity().level().isClientSide) return;

		event.getEntity().level().getServer().getPlayerList().getPlayers().forEach(sPlayer -> {
			sPlayer.connection.send(new ClientboundUpdateMobEffectPacket(event.getEntity().getId(), event.getEffectInstance(), false));
		});

	}

	@SubscribeEvent
	public void MobEffectRemove(MobEffectEvent.Remove event) {
		if (event.getEffectInstance() != null) {
			Utils.removeEffects(event.getEffect(), event.getEntity());
		}
	}

	@SubscribeEvent
	public void MobEffectExpire(MobEffectEvent.Expired event) {
		if (event.getEffectInstance() != null) {
			Utils.removeEffects(event.getEffectInstance().getEffect(), event.getEntity());
		}

	}

	//This way we can give items with the Utils#giveItems method which is better for this kind of stuff.
	private static final Map<ResourceLocation, Supplier<Item>> ADVANCEMENT_ITEM_REWARDS = Map.of(
			KingdomKeys.rl("munny_hoarder"), ModItems.proofOfPeace,
			KingdomKeys.rl("max_keyblade_level"), ModItems.proofOfNonexistence,
			KKAllAdvancementsTrigger.ADVANCEMENT_ID, ModItems.proofOfConnection
	);

	@SubscribeEvent
	public void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
			return;
		}

		ResourceLocation id = event.getAdvancement().id();
		if (!id.getNamespace().equals(KingdomKeys.MODID)) {
			return;
		}

		Supplier<Item> reward = ADVANCEMENT_ITEM_REWARDS.get(id);
		if (reward != null) {
			Utils.giveItems(serverPlayer, true, new ItemStack(reward.get()));
		}

		//Last advancement
		KKAllAdvancementsTrigger.checkCompletion(serverPlayer);
	}

	public enum ThreatLevel {
		NONE, HOSTILES, BOSS
	}
}