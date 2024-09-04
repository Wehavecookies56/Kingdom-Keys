package online.kingdomkeys.kingdomkeys.handler;

import java.util.*;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.data.*;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.DimensionCommand;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.*;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.*;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.LimitDataLoader;
import online.kingdomkeys.kingdomkeys.magic.MagicDataLoader;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAirStepPacket;
import online.kingdomkeys.kingdomkeys.network.stc.*;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;
	public int ticks;

	@SubscribeEvent
	public void soundPlayed(PlayLevelSoundEvent.AtEntity event) {
		if (event.getEntity() instanceof Player player && event.getSound().value().getLocation().getPath().contains("step")) {
			boolean kbArmor = false;
			byte index = 0;
			Iterator<ItemStack> it = player.getArmorSlots().iterator();
			while (it.hasNext()) {
				ItemStack a = it.next();
				if (a.getItem() instanceof ArmorItem armor) {
					if (index < 3 && armor.getMaterial().value().equipSound().value() == ModSounds.keyblade_armor.get()) { // If the armor has a kb sound we assume it's a keyblade armor part, if it's index is < 3 it means it's boots, pants or chest.
						kbArmor = true;
					}
				}
				index++;
			}
			if (kbArmor) {
				event.getEntity().playSound(ModSounds.keyblade_armor.get());
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinLevelEvent e) {
		if (e.getEntity() instanceof LivingEntity mob) {
			
			GlobalData mobData = GlobalData.get(mob);
			if(mobData == null)
				return;
						
			Player player = Utils.getClosestPlayer(mob, mob.level());
			if(player == null)
				return;

			if (e.getLevel().dimension().location().getPath().equals("realm_of_darkness") && mob instanceof IKHMob ikhmob) {
				if (ikhmob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD) {
					double dist = e.getEntity().position().distanceTo(new Vec3(0, 62, 0));
					int level = (int) Math.min(dist / ModConfigs.rodHeartlessLevelScale, ModConfigs.rodHeartlessMaxLevel);
					mobData.setLevel(level);
				}
			}
			
			if(mobData.getLevel() <= 0 && mob instanceof Monster && ModConfigs.hostileMobsLevel) { //TODO config
				mobData.setLevel(Utils.getRandomMobLevel(player));
			}	

			if (mobData.getLevel() > 0) {
				if (!mob.hasCustomName()) {
					int lvl = mobData.getLevel();
					mob.setCustomName(Component.translatable(mob.getDisplayName().getString() + " Lv."+ Utils.getLevelColor(player,lvl) + lvl+ ChatFormatting.RESET));
					if(mob.getAttribute(Attributes.ATTACK_DAMAGE) != null)
						mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * (lvl * ModConfigs.mobLevelStats / 100), mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
					if(mob.getAttribute(Attributes.MAX_HEALTH) != null)
						mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(mob.getMaxHealth() * (lvl * ModConfigs.mobLevelStats / 100), mob.getMaxHealth()));
					mob.heal(mob.getMaxHealth());
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
		Player player = e.getEntity();
		PlayerData playerData = PlayerData.get(player);
		WorldData worldData = WorldData.get(e.getEntity().getServer());
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
				if (!playerData.getDriveFormMap().containsKey(DriveForm.NONE.toString())) { // One time event here :D
					playerData.setDriveFormLevel(DriveForm.NONE.toString(), 1);
					playerData.setDriveFormLevel(DriveForm.SYNCH_BLADE.toString(), 1);
					playerData.setDriveFormLevel(Strings.Form_Anti, 1);
					playerData.addVisibleDriveForm(Strings.Form_Anti);

					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.mythril_shard.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.mythril_stone.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.mythril_gem.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.mythril_crystal.get()));

					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.potion.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.hiPotion.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.megaPotion.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.ether.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.hiEther.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.megaEther.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.elixir.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.megaLixir.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.driveRecovery.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.hiDriveRecovery.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.refocuser.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.hiRefocuser.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.powerBoost.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.magicBoost.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.defenseBoost.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.apBoost.get()));

					if (playerData.getEquippedItems().size() == 0) {
						HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
						for (int i = 0; i < 4; i++) {
							map.put(i, ItemStack.EMPTY);
						}
						playerData.equipAllItems(map, true);
					}
				}

				if(!playerData.getVisibleDriveForms().contains(Strings.Form_Anti)) {
					playerData.addVisibleDriveForm(Strings.Form_Anti);
				}

				if (!playerData.getKnownRecipeList().contains(BuiltInRegistries.ITEM.getKey(ModItems.powerBoost.get()))) {
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.powerBoost.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.magicBoost.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.defenseBoost.get()));
					playerData.addKnownRecipe(BuiltInRegistries.ITEM.getKey(ModItems.apBoost.get()));
				}

				if (!playerData.getDriveFormMap().containsKey(Strings.Form_Anti)) {
					playerData.setDriveFormLevel(Strings.Form_Anti, 1);
				}

				if (!playerData.getDriveFormMap().containsKey(Strings.Form_Anti)) {
					playerData.setDriveFormLevel(Strings.Form_Anti, 1);
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
				if (!playerData.getDriveFormMap().containsKey(DriveForm.SYNCH_BLADE.toString())) {
					playerData.setDriveFormLevel(DriveForm.SYNCH_BLADE.toString(), 1);
				}

				// TODO (done) Fix for retrocompatibility, move above in a few versions

				if (playerData.getEquippedKBArmors().size() == 0) {
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

				// Fills the map with empty stacks for every form that requires one.
				playerData.getDriveFormMap().keySet().forEach(key -> {
					// Make sure the form exists
					if (ModDriveForms.registry.containsKey(ResourceLocation.parse(key))) {
						// Check if it requires a slot
						if (ModDriveForms.registry.get(ResourceLocation.parse(key)).hasKeychain()) {
							// Check if the player has form
							if (playerData.getDriveFormMap().containsKey(key)) {
								if (!playerData.getEquippedKeychains().containsKey(ResourceLocation.parse(key))) {
									playerData.setNewKeychain(ResourceLocation.parse(key), ItemStack.EMPTY);
								}
							}
						}
					}
				});

				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncWorldData(player.getServer()), (ServerPlayer) player);
				PacketHandler.syncToAllAround(player, playerData);

				// Sync all registries, important
				PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncMoogleNames(NamesListRegistry.getInstance()), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncShopData(ShopListRegistry.getInstance().getValues()), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncMagicData(MagicDataLoader.names, MagicDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncDriveFormData(DriveFormDataLoader.names, DriveFormDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncLimitData(LimitDataLoader.names, LimitDataLoader.dataList), (ServerPlayer) player);

				Utils.RefreshAbilityAttributes(player, playerData);
				if (player.level().dimension().location().getPath().contains("castle_oblivion_interior")) {
					SCSyncCastleOblivionInteriorData.syncClients((ServerLevel) player.level());
					PacketHandler.sendTo(new SCUpdateCORooms(CastleOblivionHandler.getCurrentFloor(player).getRooms()), (ServerPlayer) player);
				} else {
					PacketHandler.sendTo(new SCUpdateCORooms(List.of()), (ServerPlayer) player);
				}
			}

			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	Map<UUID, Boolean> openedAlignment = new HashMap<>();

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();

		if (ticks >= Integer.MAX_VALUE) {
			ticks = Integer.MIN_VALUE;
		}

		PlayerData playerData = PlayerData.get(player);

		if (playerData != null) {
			// Check if rc conditions match
			List<ReactionCommand> rcList = new ArrayList<ReactionCommand>();

			// Check commands from registry that need active check (can turn off based on
			// conditions like drive forms when you are healed)
			// Those will be available when joining the world too if the conditions are met
			for (ReactionCommand rc : ModReactionCommands.registry) {
				if (rc.needsConstantCheck() && rc.conditionsToAppear(player, player)) {
					rcList.add(rc);
				}
			}

			// Check commands in player list
			for (String rcName : playerData.getReactionCommands()) {
				ReactionCommand rc = ModReactionCommands.registry.get(ResourceLocation.parse(rcName));
				if (rc.conditionsToAppear(player, player)) {
					rcList.add(rc);
				}
			}

			playerData.setReactionCommands(new ArrayList<String>());
			for (ReactionCommand rc : rcList) {
				playerData.addReactionCommand(rc.getName(), player);
			}

			if (!player.level().isClientSide && player.tickCount == 5) { // TODO Check if it's necessary, I thought it was to set the max hp value but
																						// now it seems to work fine without it
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}

			// Anti form FP code done here
			if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
				if (playerData.getFP() > 0) {
					playerData.setFP(playerData.getFP() - 0.3);
				} else {
					playerData.setActiveDriveForm(DriveForm.NONE.toString());
					player.level().playSound(player, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					if (!player.level().isClientSide) {
						PacketHandler.syncToAllAround(player, playerData);
					}
				}
			} else if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm())).updateDrive(player);
			}
			// Limit recharge system
			if (playerData.getLimitCooldownTicks() > 0 && !player.level().isClientSide) {
				playerData.setLimitCooldownTicks(playerData.getLimitCooldownTicks() - 1);
				if (playerData.getLimitCooldownTicks() <= 0) {
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}
			//System.out.println(playerData.getMagicCasttimeTicks());

			// Magic Casttime
			if (playerData.getMagicCasttimeTicks() > 0 && !player.level().isClientSide)
				playerData.remMagicCasttimeTicks(1);
			if (playerData.getCastedMagic() != null) {
				if (playerData.getMagicCasttimeTicks() <= 0) {
					Utils.castMagic castedMagic = playerData.getCastedMagic();
					castedMagic.magic().magicUse(castedMagic.player(), castedMagic.caster(), castedMagic.level(), castedMagic.fullMPBlastMult(), castedMagic.lockOnEntity());
					player.swing(InteractionHand.MAIN_HAND,true);
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

				// PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData),
				// (ServerPlayerEntity) player);

			} else { // Not on recharge
				if (playerData.getMP() <= 0 && playerData.getMaxMP() > 0) {
					playerData.setRecharge(true);
					if (!player.level().isClientSide) {
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}
			}

			if (!player.level().isClientSide) {
				if (playerData.getAlignment() == Utils.OrgMember.NONE) {
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
				if (playerData.isAbilityEquipped(Strings.treasureMagnet) && !player.isCrouching() && player.getInventory().getFreeSlot() > -1) {
					double x = player.getX();
					double y = player.getY() + 0.75;
					double z = player.getZ();

					float range = 1 + playerData.getNumberOfAbilitiesEquipped(Strings.treasureMagnet);

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

						if (Math.sqrt(x * x + y * y + z * z) > 1) {
							finalVector = finalVector.normalize();
						}

						item.setDeltaMovement(finalVector.multiply(0.45F, 0.45F, 0.45F));
						pulled++;
					}
				}

			}

			/*
			 * if(ModConfigs.magicUsesTimer > 1) { if(player.tickCount %
			 * ModConfigs.magicUsesTimer == 0) { for (Entry<String, int[]> entry :
			 * playerData.getMagicsMap().entrySet()) { int uses =
			 * playerData.getMagicUses(entry.getKey()); if(uses > 0) {
			 * playerData.remMagicUses(entry.getKey(), 1); } } } }
			 */
		}

		if (ticks % 5 == 0) {
			updateCommandMenu(player);
			// Combat mode
		}

	}

	/**
	 * This method returns once a boss is been found, therefore isHostiles will not be updated
	 * @param player
	 */
	private void updateCommandMenu(Player player) {
		List<LivingEntity> entities = Utils.getLivingEntitiesInRadius(player, 16);
		List<LivingEntity> bossEntities = Utils.getLivingEntitiesInRadius(player, 150);
		if (!bossEntities.isEmpty()) {
			for (int i = 0; i < bossEntities.size(); i++) {
				if (bossEntities.get(i) instanceof EnderDragon || bossEntities.get(i) instanceof WitherBoss || bossEntities.get(i) instanceof MarluxiaEntity) {
					isBoss = true;
					return;
				} else {
					isBoss = false;
				}
			}
		} else {
			isBoss = false;
		}
		if (!entities.isEmpty()) {
			for (Entity entity : entities) {
				if (entity instanceof Monster || entity instanceof Slime) {
					isHostiles = true;
					return;
				} else {
					isHostiles = false;
				}
			}
		} else {
			isHostiles = false;
		}

	}

	int airstepTicks = -1;
	@SubscribeEvent
	public void onLivingUpdate(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof LivingEntity entity) {
			GlobalData globalData = GlobalData.get(entity);
			PlayerData playerData = null;
			if (event.getEntity() instanceof Player player) {
				playerData = PlayerData.get(player);
				if (playerData != null) {
					// Drive form speed
					if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
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
								PacketHandler.sendToServer(new CSSetAirStepPacket(new BlockPos(0, 0, 0)));
								airstepTicks = -1;
							}
						}
						if (airstepTicks > -1)
							player.setDeltaMovement((pos.getX() - player.getX()) * speedFactor, (pos.getY() - player.getY()) * speedFactor, (pos.getZ() - player.getZ()) * speedFactor);

					}
				}
			}

			if (globalData != null) {
				//t's time for the challenge
				// globalData.setKO(true);
				if (globalData.isKO()) {
					if (entity.tickCount % 20 == 0) {
						if (entity.getHealth() - 1 <= 0) {
							entity.kill();
							globalData.setKO(false);
							PacketHandler.syncToAllAround(entity, globalData);
						} else {
							entity.setHealth(entity.getHealth() - 1);
						}
					}
					entity.setYRot(0);
					entity.setYBodyRot(0);
					entity.setXRot(0);
				}

				if (globalData.getStopModelTicks() > 0) {
					globalData.setStopModelTicks(globalData.getStopModelTicks() - 1);
					if (globalData.getStopModelTicks() <= 0) {
						PacketHandler.syncToAllAround(entity, globalData);
					}
				}

				if (globalData.getStoppedTicks() > 0) {
					globalData.subStoppedTicks(1);

					entity.setDeltaMovement(0, 0, 0);
					entity.hurtMarked = true;

					if (entity instanceof Mob) {
						((Mob) entity).setTarget(null);
					}

					if (globalData.getStoppedTicks() <= 0) {
						if (entity instanceof Mob) {
							((Mob) entity).setNoAi(false);
						}

						globalData.setStoppedTicks(0); // Just in case it goes below (shouldn't happen)
						if (globalData.getStopDamage() > 0 && globalData.getStopCaster() != null) {
							entity.hurt(StopDamageSource.getStopDamage(Utils.getPlayerByName(entity.level(), globalData.getStopCaster().toLowerCase())), globalData.getStopDamage() / 2);
						}

						if (entity instanceof ServerPlayer) // Packet to unfreeze client
							PacketHandler.sendTo(new SCSyncGlobalData(entity), (ServerPlayer) entity);
						globalData.setStopDamage(0);
						globalData.setStopCaster(null);
					}
				}

				// Gravity
				if (globalData.getFlatTicks() > 0) {
					globalData.subFlatTicks(1);

					if (entity instanceof Player player) {
						if (player.getForcedPose() != Pose.SWIMMING) {
							player.setForcedPose(Pose.SWIMMING);
						}

					}

					entity.setDeltaMovement(0, -4, 0);
					entity.hurtMarked = true;

					if (globalData.getFlatTicks() <= 0) {
						globalData.setFlatTicks(0); // Just in case it goes below (shouldn't happen)
						// This should sync the state of this entity (player or mob) to all the clients
						// around to stop render it flat
						PacketHandler.syncToAllAround(entity, globalData);

						if (event.getEntity() instanceof ServerPlayer) {
							PacketHandler.sendTo(new SCRecalculateEyeHeight(), (ServerPlayer) event.getEntity());
						}
					}
				} else {
					if (event.getEntity() instanceof Player pl) {
						if (pl.getForcedPose() != null && !PlayerData.get(pl).getIsGliding()) {
							pl.setForcedPose(null);
						}
					}
				}

				// Aero
				if (globalData.getAeroTicks() > 0) {
					globalData.remAeroTicks(1);

					if (globalData.getAeroLevel() == 1) {
						if (entity.tickCount % 20 == 0) {
							float radius = 0.4F;
							List<LivingEntity> list = Utils.getLivingEntitiesInRadius(entity, radius);
							if (!list.isEmpty()) {
								for (Entity e : list) {
									if (entity instanceof Player player)
										e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player) * 0.033F);
								}
							}
						}
					} else if (globalData.getAeroLevel() == 2) {
						if (entity.tickCount % 10 == 0) {
							float radius = 0.6F;
							List<LivingEntity> list = Utils.getLivingEntitiesInRadius(entity, radius);
							if (!list.isEmpty()) {
								for (Entity e : list) {
									if (entity instanceof Player player)
										e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player) * 0.066F);
								}
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
						for (int s = 1; s < 360; s += 20) {
							double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
							double y = Y + (radius * Math.cos(Math.toRadians(t)));
							entity.level().addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
						}
					}

				} else { // When it finishes
					if (playerData.getReflectActive()) {// If has been hit
						// SPAWN ENTITY and apply damage
						float dmgMult = 1;
						float radius = 1;
						switch (playerData.getReflectLevel()) {
							case 0:
								radius = 2.5F;
								dmgMult = 0.3F;
								break;
							case 1:
								radius = 3F;
								dmgMult = 0.5F;
								break;
							case 2:
								radius = 3.5F;
								dmgMult = 0.7F;
								break;
						}
						List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
						Party casterParty = WorldData.get(player.level().getServer()).getPartyFromMember(player.getUUID());

						if (casterParty != null && !casterParty.getFriendlyFire()) {
							for (Member m : casterParty.getMembers()) {
								list.remove(player.level().getPlayerByUUID(m.getUUID()));
							}
						}

						double X = entity.getX();
						double Y = entity.getY();
						double Z = entity.getZ();

						for (int t = 1; t < 360; t += 20) {
							double x = X + (radius * Math.cos(Math.toRadians(t)));
							double z = Z + (radius * Math.sin(Math.toRadians(t)));
							((ServerLevel) entity.level()).sendParticles(ParticleTypes.BUBBLE.getType(), x, Y + 1, z, 5, 0, 0, 0, 1);
						}

						if (!list.isEmpty()) {
							for (int i = 0; i < list.size(); i++) {
								Entity e = (Entity) list.get(i);
								if (e instanceof LivingEntity) {
									e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player) * dmgMult * ModMagic.registry.get(ResourceLocation.parse(Strings.Magic_Reflect)).getDamageMult(playerData.getReflectLevel()));
								}
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
		if (event.getItemEntity().getItem() != null && event.getItemEntity().getItem().getItem() instanceof SynthesisItem) {
			for (int i = 0; i < event.getPlayer().getInventory().getContainerSize(); i++) {
				ItemStack bag = event.getPlayer().getInventory().getItem(i);
				if (!ItemStack.matches(bag, ItemStack.EMPTY)) {
					if (bag.getItem() == ModItems.synthesisBag.get()) {
						IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM, null);
						addSynthesisMaterialToBag(inv, event, bag);
					}
				}
			}
		}
	}

	public void addSynthesisMaterialToBag(IItemHandler inv, ItemEntityPickupEvent event, ItemStack bag) {
		int bagLevel = bag.getOrDefault(ModComponents.SYNTH_BAG_LEVEL, 0);
		int maxSlots = switch (bagLevel) {
            case 0 -> 18;
            case 1 -> 36;
            case 2 -> 54;
            case 3 -> 72;
            default -> 0;
        };

        for (int j = 0; j < maxSlots; j++) {
			ItemStack bagItem = inv.getStackInSlot(j);
			ItemStack pickUp = event.getItemEntity().getItem();
			if (!ItemStack.matches(bagItem, ItemStack.EMPTY)) {
				if (bagItem.getItem().equals(pickUp.getItem())) {
					if (bagItem.getCount() < 64) {
						if (bagItem.getCount() + pickUp.getCount() <= 64) {
							ItemStack stack = new ItemStack(pickUp.copy().getItem(), pickUp.copy().getCount());
							inv.insertItem(j, stack, false);
							pickUp.setCount(0);
							return;
						}
					}
				}
			} else if (ItemStack.matches(bagItem, ItemStack.EMPTY)) {
				inv.insertItem(j, pickUp.copy(), false);
				pickUp.setCount(0);
				return;
			}
		}
	}

	@SubscribeEvent
	public void hitEntity(LivingDamageEvent.Pre event) {
		if (event.getSource().getEntity() instanceof Player player) {
			
			ItemStack weapon = Utils.getWeaponDamageStack(event.getSource(), player);
			if (weapon != null && !(event.getSource() instanceof StopDamageSource)) {
				float dmg = 0;
				if (weapon.getItem() instanceof KeybladeItem) {
					dmg = DamageCalculation.getKBStrengthDamage(player, weapon);
				} else if (weapon.getItem() instanceof IOrgWeapon) {
					dmg = DamageCalculation.getOrgStrengthDamage(player, weapon);
				}

				if (player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger()) { // Crit attack formula
					dmg *= ModConfigs.critMult;
					dmg += dmg * PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.1F;
				}
				event.setNewDamage((event.getOriginalDamage()-1)+dmg * player.getAttackStrengthScale(0));
			}

			PlayerData playerData = PlayerData.get(player);
			if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
				event.setNewDamage(playerData.getStrength(true));
			}
		}

		/*if (event.getSource().getDirectEntity() instanceof VolleyShotEntity || event.getSource().getDirectEntity() instanceof RagnarokShotEntity || event.getSource().getDirectEntity() instanceof ThunderBoltEntity || event.getSource().getDirectEntity() instanceof ArrowgunShotEntity || event.getSource().getDirectEntity() instanceof BlizzardEntity || event.getSource().getDirectEntity() instanceof KKThrowableEntity) {
			target.invulnerableTime = 0;
		}*/

		if (event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);

			if (playerData.getReflectTicks() <= 0) { // If is casting reflect
				if (playerData.isAbilityEquipped(Strings.mpRage)) {
					playerData.addMP((event.getOriginalDamage() * 0.2F) * playerData.getNumberOfAbilitiesEquipped(Strings.mpRage));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}

				if (playerData.isAbilityEquipped(Strings.damageDrive)) {
					playerData.addDP((event.getOriginalDamage() * 0.2F) * playerData.getNumberOfAbilitiesEquipped(Strings.damageDrive));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}
		}

		// This is outside as it should apply the formula if you have been hit by non
		// player too
		if (event.getEntity() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			GlobalData globalData = GlobalData.get(player);

			float damage = event.getOriginalDamage() * 100 / (100 + playerData.getDefense(true));
			if (globalData.getAeroTicks() > 0) {
				float resistMultiplier = globalData.getAeroLevel() == 0 ? 0.3F : globalData.getAeroLevel() == 1 ? 0.35F : globalData.getAeroLevel() == 2 ? 0.4F : 0;

				globalData.remAeroTicks((int) damage * 2);
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
			 //System.out.println(damage);

			// Damage Control
			if (Utils.isPlayerLowHP(player) && playerData.isAbilityEquipped(Strings.damageControl)) {
				damage /= (1 + playerData.getNumberOfAbilitiesEquipped(Strings.damageControl));
			}
			
			// Protect Abilities
			float protectReduction;
			if (playerData.isAbilityEquipped(Strings.protect)){
				protectReduction = damage * 0.1F;
				damage -= protectReduction;
				//System.out.println(damage);
			}
			if (playerData.isAbilityEquipped(Strings.protectra)){
				protectReduction = damage *  0.2F;
				damage -= protectReduction;
				//System.out.println(damage);
			}
			if (playerData.isAbilityEquipped(Strings.protectga)){
				protectReduction = damage *  0.4F;
				damage -= protectReduction;
				//System.out.println(damage);
			}



			// Has to evaluate last
			// Second chance (will save the player from a damage that would've killed him as long as he had 2 hp or more)
			if (playerData.isAbilityEquipped(Strings.secondChance)) {
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

			event.setNewDamage(damage <= 0 ? 1 : damage);
		}

		// Mobs defense formula
		if (event.getEntity() instanceof BaseKHEntity) {
			float damage = event.getOriginalDamage();
			int defense = ((BaseKHEntity) event.getEntity()).getDefense();
			if (defense > 0)
				damage = (float) Math.round((damage * 100 / (300 + defense)));

			GlobalData globalData = GlobalData.get(event.getEntity());
			if (globalData.getAeroTicks() > 0) {
				float resistMultiplier = globalData.getAeroLevel() == 0 ? 0.3F : globalData.getAeroLevel() == 1 ? 0.35F : globalData.getAeroLevel() == 2 ? 0.4F : 0;
				globalData.remAeroTicks((int) damage * 2);
				damage -= (damage * resistMultiplier);
			}

			// Marluxia's final attack
			if (event.getEntity() instanceof MarluxiaEntity) {
				MarluxiaEntity mar = (MarluxiaEntity) event.getEntity();
				if (EntityHelper.getState(event.getEntity()) != 3) {
					if (mar.marluxiaGoal.chasedTimes == 0) {
						if (mar.getHealth() - damage <= 0) {
							mar.marluxiaGoal.chasedTimes++;
							EntityHelper.setState(mar, 3);
							event.setNewDamage(mar.getHealth() - 1);
							mar.setInvulnerable(true);
							return;
						}
					}
				}

				if (EntityHelper.getState(event.getEntity()) == 1) { // If marly is armored
					damage = event.getOriginalDamage() * 0.1F;
					if (event.getSource().getMsgId().equals(KKResistanceType.fire.toString())) {
						mar.marluxiaGoal.removeArmor(mar);
					}
				} else if (EntityHelper.getState(event.getEntity()) == 2) {
					if (event.getSource().getEntity() == mar.getKillCredit()) {
						EntityHelper.setState(mar, 0);
						mar.setNoGravity(false);
					}
				}
			}
			event.setNewDamage(damage < 1 ? 1 : damage);
		}
	}

	// Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingIncomingDamageEvent event) {
		if (!event.getEntity().level().isClientSide) {
			if (event.getEntity() instanceof Player player) {
				WorldData worldData = WorldData.get(player.getServer());
				GlobalData globalData = GlobalData.get(player);
				if (worldData != null && globalData != null && worldData.getPartyFromMember(player.getUUID()) != null) {
					if (!player.level().isClientSide()) {
						Party p = worldData.getPartyFromMember(player.getUUID());
						if (Utils.anyPartyMemberOnExcept(player, p, (ServerLevel) player.level())) {
							if (ModConfigs.allowPartyKO) {
								if (!globalData.isKO() && player.getHealth() - event.getAmount() <= 0) { // We only set KO if we die while not KO already
									event.setCanceled(true);
									player.removeAllEffects();
									player.setHealth(player.getMaxHealth());
									player.invulnerableTime = 40;
									player.getFoodData().setFoodLevel(10);
									player.getFoodData().setExhaustion(0);
									player.getFoodData().setSaturation(0);
									globalData.setKO(true);
									player.level().playSound(null, player.blockPosition(), ModSounds.playerDeathHardcore.get(), SoundSource.PLAYERS);
								}
								PacketHandler.syncToAllAround(player, globalData);
							} else { //If config does not allow prevent KO from being applied
								globalData.setKO(false);
							}
						}
					}
				}
			}
			if (event.getSource().getEntity() instanceof LivingEntity) { // If attacker is a LivingEntity
				LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
				LivingEntity target = event.getEntity();

				if (attacker instanceof Player && target instanceof Player) {
					Party p = WorldData.get(attacker.getServer()).getPartyFromMember(attacker.getUUID());
					if (p != null && p.getMember(event.getEntity().getUUID()) != null && !p.getFriendlyFire()) {
						event.setCanceled(true);
					}
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

				GlobalData globalData = GlobalData.get(target);
				if (globalData != null && event.getSource().getEntity() instanceof Player) {
					Player source = (Player) event.getSource().getEntity();
					if (globalData.getStoppedTicks() > 0) {
						float dmg = event.getAmount();
						if (event.getSource().getEntity() instanceof Player) {
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
						}

						globalData.addDamage(dmg);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		// EnderDragon killed makes heartless spawn if mode is 3
		WorldData worldData = WorldData.get(event.getEntity().getServer());
		if (event.getEntity() instanceof EnderDragon) {
			LivingEntity entity = event.getEntity();
			if (worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_DRAGON) {
				worldData.setHeartlessSpawnLevel(1);
			}

			for (Player p : entity.level().players()) {
				entity.level().addFreshEntity(new ItemEntity(entity.level(), p.getX(), p.getY(), p.getZ(), new ItemStack(ModItems.proofOfHeart.get(), 1)));
			}
		}

		if (event.getEntity() instanceof Player player) {
			if (player.level().getLevelData().isHardcore())
				player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.playerDeathHardcore.get(), SoundSource.PLAYERS, 1F, 1F);
			else
				player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.playerDeath.get(), SoundSource.PLAYERS, 1F, 1F);
		}

		if (!event.getEntity().level().isClientSide) {
			if (event.getSource().getDirectEntity() instanceof Player || event.getSource().getEntity() instanceof Player) { // If the player kills
				Player player = (Player) event.getSource().getEntity();
				if (player != null) {
					PlayerData playerData = PlayerData.get(player);

					// TODO more sophisticated and dynamic way to do this
					// Give hearts
					if (player.getMainHandItem().getItem() instanceof IOrgWeapon || player.getMainHandItem().getItem() instanceof KeybladeItem || event.getSource().getDirectEntity() instanceof KKThrowableEntity) {
						int multiplier = 1;
						if (player.getMainHandItem().getItem() instanceof IOrgWeapon) {
							IOrgWeapon weapon = (IOrgWeapon) player.getMainHandItem().getItem();
							if (weapon.getMember() == playerData.getAlignment() || (event.getSource().getDirectEntity() instanceof KKThrowableEntity && playerData.getAlignment() == OrgMember.AXEL)) { // If the item used to kill is for the correct alignment OR if it's been a
																																																		// throwable entity and the player is Axel (probably the only case so far which
																																																		// could be true)
								multiplier = 2;
							}
						}
						if (event.getEntity() instanceof IKHMob) {
							IKHMob mob = (IKHMob) event.getEntity();
							if (mob.getKHMobType() == MobType.HEARTLESS_EMBLEM) {
								playerData.addHearts((int) ((20 * multiplier) * ModConfigs.heartMultiplier));
							}
						} else if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
							playerData.addHearts((int) ((1000 * multiplier) * ModConfigs.heartMultiplier));
						} else if (event.getEntity() instanceof Villager) {
							playerData.addHearts((int) ((5 * multiplier) * ModConfigs.heartMultiplier));
						} else if (event.getEntity() instanceof Monster) {
							playerData.addHearts((int) ((2 * multiplier) * ModConfigs.heartMultiplier));
						} else {
							playerData.addHearts((int) ((1 * multiplier) * ModConfigs.heartMultiplier));
						}
					}
					if (event.getEntity() instanceof IKHMob) {
						IKHMob heartless = (IKHMob) event.getEntity();
						if (heartless.getKHMobType() == MobType.HEARTLESS_EMBLEM && Utils.getWeaponDamageStack(event.getSource(), player) != null && Utils.getWeaponDamageStack(event.getSource(), player).getItem() instanceof KeybladeItem) {
							HeartEntity heart = new HeartEntity(event.getEntity().level());
							heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
							event.getEntity().level().addFreshEntity(heart);
						}
						if (heartless.getKHMobType() == MobType.HEARTLESS_PUREBLOOD && event.getEntity().level() instanceof ServerLevel) {
							LivingEntity entity = event.getEntity();
							for (int i = 0; i < 2; i++) {
								((ServerLevel) entity.level()).sendParticles(ParticleTypes.SMOKE, entity.getX() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getY() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getZ() + entity.level().random.nextDouble() / 2 - 0.25D, 50, 0, -1, 0, 0.3);
								((ServerLevel) entity.level()).sendParticles(ParticleTypes.SQUID_INK, entity.getX() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getY() + entity.level().random.nextDouble() / 2 - 0.25D, entity.getZ() + entity.level().random.nextDouble() / 2 - 0.25D, 30, 0, -1, 0, 0.1);
							}
						}
					}

					if (event.getEntity().getClassification(false) == MobCategory.MONSTER) {
						if (!playerData.isAbilityEquipped(Strings.zeroExp)) {
							LivingEntity mob = (LivingEntity) event.getEntity();

							double value = mob.getAttribute(Attributes.MAX_HEALTH).getValue() / 2;
							double exp = Utils.randomWithRange(value * 0.8, value * 1.8);
							playerData.addExperience(player, (int) (exp * ModConfigs.xpMultiplier), true, true);

							if (event.getEntity() instanceof WitherBoss) {
								exp += 1500;
								playerData.addExperience(player, (int) (exp * ModConfigs.xpMultiplier), true, true);
							}

							if (!playerData.isAbilityEquipped(Strings.zeroExp)) {
								if (playerData.getNumberOfAbilitiesEquipped(Strings.experienceBoost) > 0 && player.getHealth() <= player.getMaxHealth() / 2) {
									exp *= (1 + playerData.getNumberOfAbilitiesEquipped(Strings.experienceBoost));
								}

								XPEntity xp = new XPEntity(mob.level(), player, mob, exp);
								player.level().addFreshEntity(xp);
							}
						}

						LivingEntity entity = event.getEntity();
						double x = entity.getX();
						double y = entity.getY();
						double z = entity.getZ();

						if (entity.level().random.nextInt(100) <= ModConfigs.munnyDropProbability) {
							int num = (int) Utils.randomWithRange(5, entity.getMaxHealth() / 5);
							num += playerData.getNumberOfAbilitiesEquipped(Strings.jackpot) * 1.2;
							// reduce munny value by 2 for each level of drive converter
							num /= (1 + playerData.getNumberOfAbilitiesEquipped(Strings.driveConverter));
							entity.level().addFreshEntity(new MunnyEntity(event.getEntity().level(), x, y, z, num));
						}

						if (entity.level().random.nextInt(100) <= ModConfigs.hpDropProbability) {
							int num = (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5);
							num += playerData.getNumberOfAbilitiesEquipped(Strings.jackpot) * 1.2;
							entity.level().addFreshEntity(new HPOrbEntity(event.getEntity().level(), x, y, z, num));
						}

						if (entity.level().random.nextInt(100) <= ModConfigs.mpDropProbability) {
							int num = (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5);
							num += playerData.getNumberOfAbilitiesEquipped(Strings.jackpot) * 1.2;
							entity.level().addFreshEntity(new MPOrbEntity(event.getEntity().level(), x, y, z, num));
						}

						if (entity.level().random.nextInt(100) <= ModConfigs.driveDropProbability) {
							int num = (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.drivePointsMultiplier);
							num += num * playerData.getNumberOfAbilitiesEquipped(Strings.driveConverter) * 0.5;
							entity.level().addFreshEntity(new DriveOrbEntity(event.getEntity().level(), x, y, z, num));
						}

						if (entity.level().random.nextInt(100) <= ModConfigs.focusDropProbability) {
							int num = (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.focusPointsMultiplier);
							num += num * playerData.getNumberOfAbilitiesEquipped(Strings.focusConverter) * 0.25;
							entity.level().addFreshEntity(new FocusOrbEntity(event.getEntity().level(), x, y, z, num));
						}

						int num = Utils.randomWithRange(0, 99);
						if (num < ModConfigs.recipeDropChance + Utils.getLootingLevel(player)) {
							Item recipeTier = ModItems.recipeD.get();
							GlobalData mobData = GlobalData.get(entity);
							if (mobData != null) {
								int num2 = Utils.randomWithRange(0, mobData.getLevel()+1);
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
								}  else if (num2 == 200) {
									recipeTier = ModItems.recipeSSS.get();
								}
							}
							ItemEntity ie = new ItemEntity(player.level(), x, y, z, new ItemStack(recipeTier));
							player.level().addFreshEntity(ie);

						}

						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}
			}
			// TODO check if works
			if (event.getEntity() instanceof MoogleEntity && event.getSource().getMsgId().equals("anvil")) {
				ItemEntity ie = new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModBlocks.moogleProjector.get()));
				event.getEntity().level().addFreshEntity(ie);
			}

			if (event.getSource().getEntity() instanceof IKHMob && ModConfigs.playerSpawnHeartless) {
				IKHMob killerMob = (IKHMob) event.getSource().getEntity();
				if (!event.getSource().getEntity().hasCustomName() && (killerMob.getKHMobType() == MobType.HEARTLESS_EMBLEM || killerMob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD)) {
					if (event.getEntity() instanceof Player) { // If a player gets killed by a heartless
						PlayerData playerData = PlayerData.get((Player) event.getEntity());

						String[] heartless = ModConfigs.playerSpawnHeartlessData.get(0).split(",");
						String[] nobody = ModConfigs.playerSpawnHeartlessData.get(1).split(",");

						DuskEntity newDusk = new DuskEntity(ModEntities.TYPE_DUSK.get(), event.getSource().getEntity().level());
						newDusk.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						newDusk.setCustomName(Component.translatable(event.getEntity().getDisplayName().getString() + "'s Nobody"));
						newDusk.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntity().getMaxHealth() * Double.parseDouble(nobody[1]) / 100, newDusk.getMaxHealth()));
						newDusk.heal(newDusk.getMaxHealth());
						newDusk.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength(true) * Double.parseDouble(nobody[2]) / 100, newDusk.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getEntity().level().addFreshEntity(newDusk);

						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getEntity().level());
						newShadow.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						newShadow.setCustomName(Component.translatable(event.getEntity().getDisplayName().getString() + "'s Heartless"));
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
				Player player = (Player) event.getSource().getEntity();
				ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace("overworld"));
				BlockPos coords = DimensionCommand.getWorldCoords(player, dimension);
				player.changeDimension(new DimensionTransition(player.getServer().getLevel(dimension), new Vec3(coords.getX(), coords.getY(), coords.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
			}
		}
	}

	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerData playerData = PlayerData.get(player);
			// Check to prevent edge case crash
			if (playerData != null && playerData.getActiveDriveForm() != null) {
				if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
					event.setDistance(0);
				} else {
					if (playerData.isAbilityEquipped(Strings.highJump) || playerData.isAbilityEquipped(Strings.aerialDodge) || playerData.isAbilityEquipped(Strings.glide)) {
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
					// System.out.println(nPlayer.getName().getString()+ " died in ROD, back to it
					// you go");
					ServerPlayer sPlayer = (ServerPlayer) nPlayer;
					ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "realm_of_darkness"));
					ServerLevel serverlevel = ((ServerLevel) sPlayer.level()).getServer().getLevel(dimension);
					BlockPos pos = serverlevel.getSharedSpawnPos();
					sPlayer.changeDimension(new DimensionTransition(serverlevel, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, sPlayer.getYRot(), sPlayer.getXRot(), entity -> {}));
				}
			}
		}
	}

	@SubscribeEvent
	public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent e) {
		Player player = e.getEntity();
		if (!player.level().isClientSide) {
			PlayerData playerData = PlayerData.get(player);
			ServerLevel world = player.getServer().getLevel(e.getTo());
			if (e.getTo() == ModDimensions.STATION_OF_SORROW) {
				BlockPos blockPos = player.blockPosition().below(2);
				world.setBlock(blockPos, ModBlocks.sorCore.get().defaultBlockState(), 2);
				if (world.getBlockEntity(blockPos) instanceof SoRCoreTileEntity) {
					SoRCoreTileEntity te = (SoRCoreTileEntity) world.getBlockEntity(blockPos);
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
		if(playerData == null || globalData == null)
			return;
		PacketHandler.syncToAllAround(localPlayer, playerData);
		PacketHandler.syncToAllAround(localPlayer, globalData);
		if (e.getTarget() instanceof Player targetPlayer) {
			PlayerData targetPlayerData = PlayerData.get(targetPlayer);
			GlobalData globalData2 = GlobalData.get(targetPlayer);
			PacketHandler.syncToAllAround(targetPlayer, targetPlayerData);
			PacketHandler.syncToAllAround(targetPlayer, globalData2);
		}
	}

	@SubscribeEvent
	public void playerStoppedTracking(PlayerEvent.StopTracking e) {
		if (!e.getEntity().level().isClientSide()) {
			Player player = e.getEntity();
			PlayerData playerData = PlayerData.get(player);
			WorldData worldData = WorldData.get(player.getServer());
			if (playerData == null || worldData == null)
				return;

			Party p = worldData.getPartyFromMember(player.getUUID());
			if (p == null)
				return;

			Member m = p.getMember(player.getUUID());
			m.setLevel(playerData.getLevel());
			m.setHP((int) player.getMaxHealth());
			m.setMP((int) playerData.getMaxMP());
			PacketHandler.sendTo(new SCSyncWorldData(player.getServer()), (ServerPlayer) player);
		}
	}

	/* TODO event doesn't exist anymore
	@SubscribeEvent
	public void looting(LootingLevelEvent event) {
		if (event.getDamageSource() != null) {
			if (event.getDamageSource().getEntity() instanceof Player) {
				Player player = (Player) event.getDamageSource().getEntity();
				PlayerData playerData = PlayerData.get(player);
				if (playerData != null) {
					event.setLootingLevel(event.getLootingLevel() + playerData.getNumberOfAbilitiesEquipped(Strings.luckyLucky));
				}
			}
		}
	}

	 */

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
	public void grindstoneEvent(GrindstoneEvent.OnPlaceItem event) {
		if(!event.getTopItem().isEmpty() && event.getTopItem().getItem() instanceof PauldronItem pauldron) {
			event.setOutput(new ItemStack(pauldron));
			int xp = 5 * event.getTopItem().get(ModComponents.PAULDRON_ENCHANTMENTS).size();
			event.setXp(xp);
		}
	}
}
