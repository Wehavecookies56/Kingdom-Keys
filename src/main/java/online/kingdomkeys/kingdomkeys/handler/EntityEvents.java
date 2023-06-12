package online.kingdomkeys.kingdomkeys.handler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.command.DimensionCommand;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.DarknessDamageSource;
import online.kingdomkeys.kingdomkeys.damagesource.FireDamageSource;
import online.kingdomkeys.kingdomkeys.damagesource.IceDamageSource;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.damagesource.LightningDamageSource;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataLoader;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.DriveOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.FocusOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.HPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.HeartEntity;
import online.kingdomkeys.kingdomkeys.entity.MPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.MunnyEntity;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.entity.XPEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.RagnarokShotEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.VolleyShotEntity;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;
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
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenAlignmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncDriveFormData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncKeybladeData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncLimitData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncMagicData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncShopData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSynthesisData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;
	public int ticks;
	
	@SubscribeEvent
	public void soundPlayed(PlayLevelSoundEvent.AtEntity event) {
		if(event.getEntity() instanceof Player player && event.getSound().get().getLocation().getPath().contains("step")) {
			boolean kbArmor = false;
			byte index = 0;
			Iterator<ItemStack> it = player.getArmorSlots().iterator();
			while(it.hasNext()) {
				ItemStack a = it.next();
				if(a.getItem() instanceof ArmorItem armor) {
					if(index < 3 && armor.getMaterial().getEquipSound() == ModSounds.keyblade_armor.get()){ //If the armor has a kb sound we assume it's a keyblade armor part, if it's index is < 3 it means it's boots, pants or chest.
						kbArmor = true;
					}
				}
				index++;
			}
			if(kbArmor) {
				event.getEntity().playSound(ModSounds.keyblade_armor.get());
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinLevelEvent e) {
		if(e.getEntity() instanceof LivingEntity mob) {
			IGlobalCapabilities mobData = ModCapabilities.getGlobal(mob);
			if(mobData.getLevel() > 0) {
				int level = mobData.getLevel();

				if(!mob.hasCustomName()) {
					mob.setCustomName(Component.translatable(mob.getDisplayName().getString()+" Lv."+level));
					mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * (level * ModConfigs.mobLevelStats / 100), mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
					mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(mob.getMaxHealth() * (level * ModConfigs.mobLevelStats / 100), mob.getMaxHealth()));	
					mob.heal(mob.getMaxHealth());
					return;
				}
			}
			
			if(e.getLevel().dimension().location().getPath().equals("realm_of_darkness") && mob instanceof IKHMob ikhmob) {
				if(ikhmob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD) {
					double dist = e.getEntity().position().distanceTo(new Vec3(0, 62, 0));
					int level = (int)Math.min(dist / ModConfigs.rodHeartlessLevelScale, ModConfigs.rodHeartlessMaxLevel);
					mobData.setLevel(level);

					if(level > 0) {
						if(!mob.hasCustomName()) {
							mob.setCustomName(Component.translatable(mob.getDisplayName().getString()+" Lv."+level));
							mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * (level * ModConfigs.mobLevelStats / 100), mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
							mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(mob.getMaxHealth() * (level * ModConfigs.mobLevelStats / 100), mob.getMaxHealth()));	
							mob.heal(mob.getMaxHealth());
							return;
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent e) {
		Player player = e.getEntity();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
		if(playerData != null) {
			//Heartless Spawn reset
			if(worldData != null) {
				if(worldData.getHeartlessSpawnLevel() > 0 && ModConfigs.heartlessSpawningMode == SpawningMode.NEVER) {
					worldData.setHeartlessSpawnLevel(0);
				} else if(worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.ALWAYS) {
					worldData.setHeartlessSpawnLevel(1);
				}
			}
			
			if (!player.level.isClientSide) { // Sync from server to client				
				if (!playerData.getDriveFormMap().containsKey(DriveForm.NONE.toString())) { //One time event here :D
					playerData.setDriveFormLevel(DriveForm.NONE.toString(), 1);
					playerData.setDriveFormLevel(DriveForm.SYNCH_BLADE.toString(), 1);

					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.mythril_shard.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.mythril_stone.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.mythril_gem.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.mythril_crystal.get()));
					
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.potion.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.hiPotion.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.megaPotion.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.ether.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.hiEther.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.megaEther.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.elixir.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.megaLixir.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.driveRecovery.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.hiDriveRecovery.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.refocuser.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.hiRefocuser.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.powerBoost.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.magicBoost.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.defenseBoost.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.apBoost.get()));
					
					if(playerData.getEquippedItems().size() == 0) {
						HashMap<Integer,ItemStack> map = new HashMap<Integer,ItemStack>();
						for(int i = 0 ; i < 4; i++) {
							map.put(i,ItemStack.EMPTY);
						}
						playerData.equipAllItems(map, true);
					}
				}
				
				if(!playerData.getKnownRecipeList().contains(ForgeRegistries.ITEMS.getKey(ModItems.powerBoost.get()))){
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.powerBoost.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.magicBoost.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.defenseBoost.get()));
					playerData.addKnownRecipe(ForgeRegistries.ITEMS.getKey(ModItems.apBoost.get()));
				}

				//Old worlds stat conversion
				if (playerData.getSoAState() == SoAState.COMPLETE) {
					switch(playerData.getChosen()) {
						case WARRIOR -> {
							if (!playerData.getStrengthStat().hasModifier("choice") && !playerData.getStrengthStat().hasModifier("sacrifice")) {
								playerData.setStrength(playerData.getStrength(false) - 1);
								playerData.getStrengthStat().addModifier("choice", 1, false);
							}
						}
						case GUARDIAN -> {
							if (!playerData.getDefenseStat().hasModifier("choice") && !playerData.getDefenseStat().hasModifier("sacrifice")) {
								playerData.setDefense(playerData.getDefense(false) - 1);
								playerData.getDefenseStat().addModifier("choice", 1, false);
							}
						}
						case MYSTIC -> {
							if (!playerData.getMagicStat().hasModifier("choice") && !playerData.getMagicStat().hasModifier("sacrifice")) {
								playerData.setMagic(playerData.getMagic(false) - 1);
								playerData.getMagicStat().addModifier("choice", 1, false);
							}
						}
					}
					switch(playerData.getSacrificed()) {
						case WARRIOR -> {
							if (!playerData.getStrengthStat().hasModifier("choice") && !playerData.getStrengthStat().hasModifier("sacrifice")) {
								playerData.setStrength(playerData.getStrength(false) + 1);
								playerData.getStrengthStat().addModifier("sacrifice", -1, false);
							}
						}
						case GUARDIAN -> {
							if (!playerData.getDefenseStat().hasModifier("choice") && !playerData.getDefenseStat().hasModifier("sacrifice")) {
								playerData.setDefense(playerData.getDefense(false) + 1);
								playerData.getDefenseStat().addModifier("sacrifice", -1, false);
							}
						}
						case MYSTIC -> {
							if (!playerData.getMagicStat().hasModifier("choice") && !playerData.getMagicStat().hasModifier("sacrifice")) {
								playerData.setMagic(playerData.getMagic(false) + 1);
								playerData.getMagicStat().addModifier("sacrifice", -1, false);
							}
						}
					}
					
				}

				//Added for old world retrocompatibility
				if (!playerData.getDriveFormMap().containsKey(DriveForm.SYNCH_BLADE.toString())) { 
					playerData.setDriveFormLevel(DriveForm.SYNCH_BLADE.toString(), 1);
				}
							
				// TODO (done) Fix for retrocompatibility, move above in a few versions
				if(playerData.getEquippedAccessories().size() == 0) {
					HashMap<Integer,ItemStack> map = new HashMap<Integer,ItemStack>();
					for(int i = 0 ; i < 3; i++) {
						map.put(i,ItemStack.EMPTY);
					}
					playerData.equipAllAccessories(map, true);
				}
				
				if(playerData.getEquippedKBArmors().size() == 0) {
					HashMap<Integer,ItemStack> map = new HashMap<Integer,ItemStack>();
					for(int i = 0 ; i < 1; i++) {
						map.put(i,ItemStack.EMPTY);
					}
					playerData.equipAllKBArmor(map, true);
				}
				
				//System.out.println(playerData.getEquippedArmors());
				if(playerData.getEquippedArmors().size() == 0) {
					HashMap<Integer,ItemStack> map = new HashMap<Integer,ItemStack>();
					for(int i = 0 ; i < 3; i++) {
						map.put(i,ItemStack.EMPTY);
					}
					playerData.equipAllArmors(map, true);
				}

				//Fills the map with empty stacks for every form that requires one.
				playerData.getDriveFormMap().keySet().forEach(key -> {
					//Make sure the form exists
					if (ModDriveForms.registry.get().containsKey(new ResourceLocation(key))) {
						//Check if it requires a slot
						if (ModDriveForms.registry.get().getValue(new ResourceLocation(key)).hasKeychain()) {
							//Check if the player has form
							if (playerData.getDriveFormMap().containsKey(key)) {
								if (!playerData.getEquippedKeychains().containsKey(new ResourceLocation(key))) {
									playerData.setNewKeychain(new ResourceLocation(key), ItemStack.EMPTY);
								}
							}
						}
					}
				});
								
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncWorldCapability(worldData), (ServerPlayer) player);
	    		PacketHandler.syncToAllAround(player, playerData);

	    		//Sync all registries, important
				PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), (ServerPlayer)player);
				PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), (ServerPlayer)player);
				PacketHandler.sendTo(new SCSyncShopData(ShopListRegistry.getInstance().getValues()), (ServerPlayer)player);
				PacketHandler.sendTo(new SCSyncMagicData(MagicDataLoader.names, MagicDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncDriveFormData(DriveFormDataLoader.names, DriveFormDataLoader.dataList), (ServerPlayer) player);
				PacketHandler.sendTo(new SCSyncLimitData(LimitDataLoader.names, LimitDataLoader.dataList), (ServerPlayer) player);

				Utils.RefreshAbilityAttributes(player, playerData);
			}

			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	Map<UUID, Boolean> openedAlignment = new HashMap<>();

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(event.phase == Phase.START) {
			if(ticks >= Integer.MAX_VALUE) {
				ticks = Integer.MIN_VALUE;
			}

			IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);

			if (playerData != null) {
				//System.out.println(playerData.getNumberOfAbilitiesEquipped(Strings.criticalBoost));
				//playerData.setArmorColor(Color.decode("#FF00FF").getRGB());
				/*if(!event.player.level.isClientSide) {
					PacketHandler.syncToAllAround(event.player, playerData);
				}*/
				
				
				//Check if rc conditions match
				List<ReactionCommand> rcList = new ArrayList<ReactionCommand>();
				
				//Check commands from registry that need active check (can turn off based on conditions like drive forms when you are healed)
				//Those will be available when joining the world too if the conditions are met
				for(ReactionCommand rc : ModReactionCommands.registry.get().getValues()) {
					if(rc.needsConstantCheck() && rc.conditionsToAppear(event.player, event.player)) {
						rcList.add(rc);
					}
				}

				//Check commands in player list
				for(String rcName : playerData.getReactionCommands()) {
					ReactionCommand rc = ModReactionCommands.registry.get().getValue(new ResourceLocation(rcName));
					if(rc.conditionsToAppear(event.player, event.player)) {
						rcList.add(rc);
					}
				}
								
				playerData.setReactionCommands(new ArrayList<String>());
				for(ReactionCommand rc : rcList) {
					playerData.addReactionCommand(rc.getName(), event.player);
				}		
				
				if(!event.player.level.isClientSide && event.player.tickCount == 5) { //TODO Check if it's necessary, I thought it was to set the max hp value but now it seems to work fine without it
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)event.player);
				}
				
				//Anti form FP code done here
				if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
					if (playerData.getFP() > 0) {
						playerData.setFP(playerData.getFP() - 0.3);
					} else {
						playerData.setActiveDriveForm(DriveForm.NONE.toString());
						event.player.level.playSound(event.player, event.player.position().x(),event.player.position().y(),event.player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						if(!event.player.level.isClientSide) {
							PacketHandler.syncToAllAround(event.player, playerData);
						}
					}
				} else if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
					ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm())).updateDrive(event.player);
				}
				//Limit recharge system
				if(playerData.getLimitCooldownTicks() > 0 && !event.player.level.isClientSide) {
					playerData.setLimitCooldownTicks(playerData.getLimitCooldownTicks() - 1);
					if(playerData.getLimitCooldownTicks() <= 0) {
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) event.player);
					}
				}
				
				//Magic CD recharge system
				if(playerData.getMagicCooldownTicks() > 0 && !event.player.level.isClientSide) {
					playerData.setMagicCooldownTicks(playerData.getMagicCooldownTicks() - 1);
					if(playerData.getMagicCooldownTicks() <= 0) {
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) event.player);
					}
				}
				
				// MP Recharge system
				if (playerData.getRecharge()) {
					if (playerData.getMP() >= playerData.getMaxMP()) { //Has recharged fully
						playerData.setRecharge(false);
						playerData.setMP(playerData.getMaxMP());
					} else { //Still recharging
						if(playerData.getMP() < 0) //Somehow people was getting negative MP so this should hopefully fix it
							playerData.setMP(0);
						playerData.addMP(playerData.getMaxMP()/500 * ((Utils.getMPHasteValue(playerData)/10) + 2));
					}
					
					//PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);
	
				} else { // Not on recharge
					if (playerData.getMP() <= 0 && playerData.getMaxMP() > 0) {
						playerData.setRecharge(true);
						if(!event.player.level.isClientSide) {
							PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) event.player);
						}
					}
				}
	
				if(!event.player.level.isClientSide) {
					if (playerData.getAlignment() == Utils.OrgMember.NONE) {
						if (!openedAlignment.containsKey(event.player.getUUID())) {
							openedAlignment.put(event.player.getUUID(), false);
						}
						boolean wearingOrgCloak = Utils.isWearingOrgRobes(event.player);
	
						if (wearingOrgCloak) {
							if (!openedAlignment.get(event.player.getUUID())) {
								PacketHandler.sendTo(new SCOpenAlignmentScreen(), (ServerPlayer) event.player);
								openedAlignment.put(event.player.getUUID(), true);
							}
						} else {
							openedAlignment.put(event.player.getUUID(), false);
						}
					}
					
					//Treasure Magnet
					if(playerData.isAbilityEquipped(Strings.treasureMagnet) && !event.player.isCrouching() && event.player.getInventory().getFreeSlot() > -1) {
						double x = event.player.getX();
						double y = event.player.getY() + 0.75;
						double z = event.player.getZ();
					
						float range = 1 + playerData.getNumberOfAbilitiesEquipped(Strings.treasureMagnet);
						
						List<ItemEntity> items = event.player.level.getEntitiesOfClass(ItemEntity.class, new AABB(x - range, y - range, z - range, x + range, y + range, z + range));
						int pulled = 0;
						for (ItemEntity item : items) {
							if(item.tickCount < 20) {
								break;
							}
							if (pulled > 200) {
								break;
							}

							Vec3 entityVector = new Vec3(item.getX(), item.getY() - item.getMyRidingOffset() + item.getBbHeight() / 2, item.getZ());
							Vec3 finalVector = new Vec3(x, y, z).subtract(entityVector);

							if (Math.sqrt(x * x + y * y + z * z) > 1) {
								finalVector = finalVector.normalize();
							}

							item.setDeltaMovement(finalVector.multiply(0.45F,0.45F,0.45F));
							pulled++;
						}
					}
					
				}
				
				/*if(ModConfigs.magicUsesTimer > 1) {
					if(event.player.tickCount % ModConfigs.magicUsesTimer == 0) {
						for (Entry<String, int[]> entry : playerData.getMagicsMap().entrySet()) {
							int uses = playerData.getMagicUses(entry.getKey());
							if(uses > 0) {
								playerData.remMagicUses(entry.getKey(), 1);
							}
						}
					}
				}*/
			}
		}

		if(ticks % 5 == 0) {
			// Combat mode
			List<LivingEntity> entities = Utils.getLivingEntitiesInRadius(event.player, 16);
			List<LivingEntity> bossEntities = Utils.getLivingEntitiesInRadius(event.player, 150);
			if (!bossEntities.isEmpty()) {
				for (int i = 0; i < bossEntities.size(); i++) {
					if (bossEntities.get(i) instanceof EnderDragon || bossEntities.get(i) instanceof WitherBoss) {
						isBoss = true;
						break;
					} else {
						isBoss = false;
					}
				}
			} else {
				isBoss = false;
			}
			if (!entities.isEmpty()) {
				for (Entity entity : entities) {
					if (entity instanceof Monster) {
						isHostiles = true;
						break;
					} else {
						isHostiles = false;
					}
				}
			} else {
				isHostiles = false;
			}
		}
				
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingTickEvent event) {
		IGlobalCapabilities globalData = ModCapabilities.getGlobal(event.getEntity());
		IPlayerCapabilities playerData = null;
		Player player = null;
		if (event.getEntity() instanceof Player) {
			player = (Player) event.getEntity();
			playerData = ModCapabilities.getPlayer(player);
			if(playerData != null) {
				//Drive form speed
				if(!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
	            	DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
					if(player.isOnGround()) {
						player.setDeltaMovement(player.getDeltaMovement().multiply(new Vec3(form.getSpeedMult(), 1, form.getSpeedMult())));
					}
				}
			}
		}

		if (globalData != null) {
			// Stop
			
			if (globalData.getStopModelTicks() > 0) {
				globalData.setStopModelTicks(globalData.getStopModelTicks()-1);
				if (globalData.getStopModelTicks() <= 0) {
					PacketHandler.syncToAllAround(event.getEntity(), globalData);
				}

			}

			if (globalData.getStoppedTicks() > 0) {
				globalData.subStoppedTicks(1);

				event.getEntity().setDeltaMovement(0, 0, 0);
				event.getEntity().hurtMarked = true;

				if (event.getEntity() instanceof Mob) {
					((Mob) event.getEntity()).setTarget(null);
				}

				if (globalData.getStoppedTicks() <= 0) {
					if(event.getEntity() instanceof Mob) {
                		((Mob) event.getEntity()).setNoAi(false);
                	}
					
					globalData.setStoppedTicks(0); // Just in case it goes below (shouldn't happen)
					if (globalData.getStopDamage() > 0 && globalData.getStopCaster() != null) {
						event.getEntity().hurt(StopDamageSource.getStopDamage(Utils.getPlayerByName(event.getEntity().level, globalData.getStopCaster())), globalData.getStopDamage()/2);
					}
					
					if (event.getEntity() instanceof ServerPlayer) // Packet to unfreeze client
						PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayer) event.getEntity());
					globalData.setStopDamage(0);
					globalData.setStopCaster(null);
				}
			}

			// Gravity
			if (globalData.getFlatTicks() > 0) {
				globalData.subFlatTicks(1);
				
				if(event.getEntity() instanceof Player) {
					if(((Player)event.getEntity()).getForcedPose() != Pose.SWIMMING){
						((Player)event.getEntity()).setForcedPose(Pose.SWIMMING);
					}					
					
				}
			
				event.getEntity().setDeltaMovement(0, -4, 0);
				event.getEntity().hurtMarked = true;

				if (globalData.getFlatTicks() <= 0) {
					globalData.setFlatTicks(0); // Just in case it goes below (shouldn't happen)
					
					if (event.getEntity() instanceof LivingEntity) {// This should sync the state of this entity (player or mob) to all the clients around to stop render it flat
						PacketHandler.syncToAllAround(event.getEntity(), globalData);
						
						if (event.getEntity() instanceof ServerPlayer) {
							PacketHandler.sendTo(new SCRecalculateEyeHeight(), (ServerPlayer) event.getEntity());
						}
					}
					
				}
			} else {
				if(event.getEntity() instanceof Player pl) {
					if(pl.getForcedPose() != null && !ModCapabilities.getPlayer(pl).getIsGliding()){
						pl.setForcedPose(null);
					}					
				}
			}
			
			//Aero
			if (globalData.getAeroTicks() > 0) {
				globalData.remAeroTicks(1);

				if(globalData.getAeroLevel() == 1) {
					if(event.getEntity().tickCount % 20 == 0) {
						float radius = 0.4F;
						List<LivingEntity> list = Utils.getLivingEntitiesInRadius(event.getEntity(), radius);
						if(!list.isEmpty()) {
							for(Entity e : list) {
								if(event.getEntity() instanceof Player)
									e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player)* 0.033F);
							}
						}
					}
				} else if(globalData.getAeroLevel() == 2) {
					if(event.getEntity().tickCount % 10 == 0) {
						float radius = 0.6F;
						List<LivingEntity> list = Utils.getLivingEntitiesInRadius(event.getEntity(), radius);
						if(!list.isEmpty()) {
							for(Entity e : list) {
								if(event.getEntity() instanceof Player)
									e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player)* 0.066F);
							}
						}
					}
				}

			} 
		}

		if (playerData != null) {
			//Rotation ticks should always be lost, this way we prevent the spinning animation on other players (hopefully)
			if (playerData.getAerialDodgeTicks() > 0) {
				playerData.setAerialDodgeTicks(playerData.getAerialDodgeTicks() - 1);
			}
				
			//Reflect
			if (playerData.getReflectTicks() > 0) {
				playerData.remReflectTicks(1);

				event.getEntity().setDeltaMovement(0, 0, 0);
				event.getEntity().hurtMarked = true;

				// Spawn particles
				float radius = 1.5F;
				double X = event.getEntity().getX();
				double Y = event.getEntity().getY();
				double Z = event.getEntity().getZ();

				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t)));
						event.getEntity().level.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
					}
				}

			} else { // When it finishes
				if (playerData.getReflectActive()) {// If has been hit
					// SPAWN ENTITY and apply damage
					float dmgMult = 1;
					float radius = 1;
					switch(playerData.getReflectLevel()) {
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
					List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
					Party casterParty = ModCapabilities.getWorld(player.level).getPartyFromMember(player.getUUID());

					if(casterParty != null && !casterParty.getFriendlyFire()) {
						for(Member m : casterParty.getMembers()) {
							list.remove(player.level.getPlayerByUUID(m.getUUID()));
						}
					}
					
					double X = event.getEntity().getX();
					double Y = event.getEntity().getY();
					double Z = event.getEntity().getZ();

					for (int t = 1; t < 360; t += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(t)));
						((ServerLevel)event.getEntity().level).sendParticles(ParticleTypes.BUBBLE.getType(), x, Y + 1, z, 5, 0, 0, 0, 1);
					}
					
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							Entity e = (Entity) list.get(i);
							if (e instanceof LivingEntity) {
								e.hurt(e.damageSources().playerAttack(player), DamageCalculation.getMagicDamage(player) * dmgMult * ModMagic.registry.get().getValue(new ResourceLocation(Strings.Magic_Reflect)).getDamageMult(playerData.getReflectLevel()));
							}
						}
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.reflect2.get(), SoundSource.PLAYERS, 1F, 1F);

					}
					playerData.setReflectActive(false); // Restart reflect
				}
			}				
		}
	}
		
	@SubscribeEvent
	public void entityPickup(EntityItemPickupEvent event) {
		if(event.getEntity().getInventory().contains(new ItemStack(ModItems.synthesisBag.get()))) {
			if(event.getItem().getItem() != null && event.getItem().getItem().getItem() instanceof SynthesisItem) {
				for (int i = 0; i < event.getEntity().getInventory().getContainerSize(); i++) {
					ItemStack bag = event.getEntity().getInventory().getItem(i);
					if (!ItemStack.matches(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.synthesisBag.get()) {
							IItemHandler inv = bag.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
							addSynthesisMaterialToBag(inv, event, bag);
						}
					}
				}
			}
		}
	}
	
	public void addSynthesisMaterialToBag(IItemHandler inv, EntityItemPickupEvent event, ItemStack bag) {
		CompoundTag nbt = bag.getOrCreateTag();
		int bagLevel = nbt.getInt("level");
		int maxSlots = 0;
		switch(bagLevel) {
		case 0:
			maxSlots = 18;
			break;
		case 1:
			maxSlots = 36;
			break;
		case 2:
			maxSlots = 54;
			break;
		}
		
		for (int j = 0; j < maxSlots; j++) {
			ItemStack bagItem = inv.getStackInSlot(j);
			ItemStack pickUp = event.getItem().getItem();
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
	public void hitEntity(LivingHurtEvent event) {
		//System.out.println(event.getSource());
		if (event.getSource().getEntity() instanceof Player) {
			Player player = (Player) event.getSource().getEntity();
			
			ItemStack weapon = Utils.getWeaponDamageStack(event.getSource(), player);
			if(weapon != null && !(event.getSource() instanceof StopDamageSource)) {
				float dmg = 0;
				if(weapon.getItem() instanceof KeybladeItem) {
					dmg = DamageCalculation.getKBStrengthDamage(player, weapon);
				} else if(weapon.getItem() instanceof IOrgWeapon) {
					dmg = DamageCalculation.getOrgStrengthDamage(player, weapon);
				}
				
				if(player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger()) { //Crit attack formula
					dmg *= ModConfigs.critMult;
					dmg += dmg * ModCapabilities.getPlayer(player).getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.1F;
				}
				
				//System.out.println("event dmg: "+dmg);
				event.setAmount(dmg);
			}
			
			if(ModCapabilities.getPlayer(player).getActiveDriveForm().equals(Strings.Form_Anti)) {
				event.setAmount(ModCapabilities.getPlayer(player).getStrength(true));
			}
			
		}
		
		LivingEntity target = event.getEntity();
		
		if(event.getSource().getDirectEntity() instanceof VolleyShotEntity || event.getSource().getDirectEntity() instanceof RagnarokShotEntity || event.getSource().getDirectEntity() instanceof ThunderBoltEntity || event.getSource().getDirectEntity() instanceof ArrowgunShotEntity || event.getSource().getDirectEntity() instanceof BlizzardEntity || event.getSource().getDirectEntity() instanceof KKThrowableEntity) {
			target.invulnerableTime = 0;
		}

		if (target instanceof Player) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer((Player) target);

			if (playerData.getReflectTicks() <= 0) { // If is casting reflect
				if (playerData.isAbilityEquipped(Strings.mpRage)) {
					playerData.addMP((event.getAmount() * 0.2F) * playerData.getNumberOfAbilitiesEquipped(Strings.mpRage));
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) target);
				}

				if (playerData.isAbilityEquipped(Strings.damageDrive)) {
					playerData.addDP((event.getAmount() * 0.2F) * playerData.getNumberOfAbilitiesEquipped(Strings.damageDrive));
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) target);
				}
			}
		}
		
		
		//This is outside as it should apply the formula if you have been hit by non player too		
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);

			float damage = (float) Math.round((event.getAmount() * 100 / (200 + playerData.getDefense(true))));
			if(globalData.getAeroTicks() > 0) {
				float resistMultiplier = globalData.getAeroLevel() == 0 ? 0.3F : globalData.getAeroLevel() == 1 ? 0.35F : globalData.getAeroLevel() == 2 ? 0.4F : 0;
				
				globalData.remAeroTicks((int) damage * 2);
				damage -= (damage * resistMultiplier);
			}
						
			if(event.getSource().getMsgId().equals(KKResistanceType.fire.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.fire.toString())) / 100F;
			} else if (event.getSource().getMsgId().equals(KKResistanceType.ice.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.ice.toString())) / 100F;
			} else if (event.getSource().getMsgId().equals(KKResistanceType.lightning.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.lightning.toString())) / 100F;
			} else if (event.getSource().getMsgId().equals(KKResistanceType.darkness.toString())) {
				damage *= (100 - Utils.getArmorsStat(playerData, KKResistanceType.darkness.toString())) / 100F;	
			}
			//System.out.println(damage);
			
			//Damage Control
			if(Utils.isPlayerLowHP(player) && playerData.isAbilityEquipped(Strings.damageControl)) {
				damage /= (1+playerData.getNumberOfAbilitiesEquipped(Strings.damageControl));
			}
			
			//Has to evaluate last
			//Second chance (will save the player from a damage that would've killed him as long as he had 2 hp or more
			if(playerData.isAbilityEquipped(Strings.secondChance)) {
				if(damage >= player.getHealth() && player.getHealth() > 1) {
					if(player.hasEffect(MobEffects.REGENERATION)) {
						player.removeEffect(MobEffects.REGENERATION);
						player.effectsDirty = true;
					}
					damage = player.getHealth()-1;
				}
			}
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
			PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayer) player);
			
			event.setAmount(damage <= 0 ? 1 : damage);
		}

		if (event.getEntity() instanceof BaseKHEntity) {
			float damage = event.getAmount();
			int defense = ((BaseKHEntity)event.getEntity()).getDefense();
			if(defense > 0)
				damage = (float) Math.round((damage * 100 / (300 + defense)));
			
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(event.getEntity());
			if(globalData.getAeroTicks() > 0) {
				float resistMultiplier = globalData.getAeroLevel() == 0 ? 0.3F : globalData.getAeroLevel() == 1 ? 0.35F : globalData.getAeroLevel() == 2 ? 0.4F : 0;
				globalData.remAeroTicks((int) damage * 2);
				damage -= (damage * resistMultiplier);
			}
			
			//Marluxia's final attack
			if (event.getEntity() instanceof MarluxiaEntity) {
				MarluxiaEntity mar = (MarluxiaEntity) event.getEntity();
				if(EntityHelper.getState(event.getEntity()) != 3) {
					if(mar.marluxiaGoal.chasedTimes == 0) {
						if(mar.getHealth() - damage <= 0) {
							mar.marluxiaGoal.chasedTimes++;
							EntityHelper.setState(mar, 3);
							event.setAmount(mar.getHealth()-1);
							mar.setInvulnerable(true);
							return;
						}
					}
				}
				
				if (EntityHelper.getState(event.getEntity()) == 1) { // If marly is armored
					damage = event.getAmount() * 0.1F;
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
			event.setAmount(damage < 1 ? 1 : damage);
		}
	}
	

	//Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if(!event.getEntity().level.isClientSide) {
			if (event.getSource().getEntity() instanceof LivingEntity) { // If attacker is a LivingEntity
				LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
				LivingEntity target = event.getEntity();
				
				if(attacker instanceof Player && target instanceof Player) {
					Party p = ModCapabilities.getWorld(attacker.level).getPartyFromMember(attacker.getUUID());
					if(p != null && p.getMember(event.getEntity().getUUID()) != null && !p.getFriendlyFire()) {
						event.setCanceled(true);
					}
				}
								
				if (target instanceof Player) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer((Player) target);
	
					if (playerData.getReflectTicks() > 0) { // If is casting reflect
						if (!playerData.getReflectActive()) // If has been hit while casting reflect
							playerData.setReflectActive(true);
						event.setCanceled(true);
					}
				}
	
				IGlobalCapabilities globalData = ModCapabilities.getGlobal(target);
				if (globalData != null && event.getSource().getEntity() instanceof Player) {
					Player source = (Player) event.getSource().getEntity();
					if (globalData.getStoppedTicks() > 0) {
						float dmg = event.getAmount();
						if (event.getSource().getEntity() instanceof Player) {
							ItemStack stack = Utils.getWeaponDamageStack(event.getSource(), source);
							if(stack != null) {
								if(stack.getItem() instanceof KeybladeItem) {
									dmg = DamageCalculation.getKBStrengthDamage((Player) event.getSource().getEntity(), stack);
								} else if(stack.getItem() instanceof IOrgWeapon){
									dmg = DamageCalculation.getOrgStrengthDamage((Player) event.getSource().getEntity(), stack);
								}
							}
							
							if(dmg == 0) {
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
		IWorldCapabilities worldData = ModCapabilities.getWorld(event.getEntity().level);
		if (event.getEntity() instanceof EnderDragon) {
			LivingEntity entity = event.getEntity();
			if (worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_DRAGON) {
				worldData.setHeartlessSpawnLevel(1);
			}

			for(Player p : entity.level.players()) {
				entity.level.addFreshEntity(new ItemEntity(entity.level, p.getX(), p.getY(), p.getZ(), new ItemStack(ModItems.proofOfHeart.get(), 1)));
			}
		}
		
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.level.getLevelData().isHardcore())
				player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(),ModSounds.playerDeathHardcore.get(), SoundSource.PLAYERS, 1F, 1F);
			else
				player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(),ModSounds.playerDeath.get(), SoundSource.PLAYERS, 1F, 1F);
		}

		if (!event.getEntity().level.isClientSide) {
			if (event.getSource().getDirectEntity() instanceof Player || event.getSource().getEntity() instanceof Player) { //If the player kills
				Player player = (Player) event.getSource().getEntity();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

				//TODO more sophisticated and dynamic way to do this
				//Give hearts
				if (player.getMainHandItem().getItem() instanceof IOrgWeapon || player.getMainHandItem().getItem() instanceof KeybladeItem) {
					int multiplier = 1;
					if (player.getMainHandItem().getItem() instanceof IOrgWeapon) {
						IOrgWeapon weapon = (IOrgWeapon) player.getMainHandItem().getItem();
						if (weapon.getMember() == playerData.getAlignment()) {
							multiplier = 2;
						}
					}
					if (event.getEntity() instanceof IKHMob) {
						IKHMob mob = (IKHMob) event.getEntity();
						if (mob.getKHMobType() == MobType.HEARTLESS_EMBLEM) {
							playerData.addHearts((int)((20 * multiplier) * ModConfigs.heartMultiplier));
						}
					} else if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
						playerData.addHearts((int)((1000 * multiplier) * ModConfigs.heartMultiplier));
					} else if (event.getEntity() instanceof Villager) {
						playerData.addHearts((int)((5 * multiplier) * ModConfigs.heartMultiplier));
					} else if (event.getEntity() instanceof Monster) {
						playerData.addHearts((int)((2 * multiplier) * ModConfigs.heartMultiplier));
					} else {
						playerData.addHearts((int)((1 * multiplier) * ModConfigs.heartMultiplier));
					}
				}
				if(event.getEntity() instanceof IKHMob) {
					IKHMob heartless = (IKHMob) event.getEntity();
					if(heartless.getKHMobType() == MobType.HEARTLESS_EMBLEM && Utils.getWeaponDamageStack(event.getSource(), player) != null && Utils.getWeaponDamageStack(event.getSource(), player).getItem() instanceof KeybladeItem) {
						HeartEntity heart = new HeartEntity(event.getEntity().level);
						heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
						event.getEntity().level.addFreshEntity(heart);
					}
				}
				
				if (event.getEntity().getClassification(false) == MobCategory.MONSTER) {
					if(!playerData.isAbilityEquipped(Strings.zeroExp)) {
						LivingEntity mob = (LivingEntity) event.getEntity();
						
						double value = mob.getAttribute(Attributes.MAX_HEALTH).getValue() / 2;
						double exp = Utils.randomWithRange(value * 0.8, value * 1.8);
						playerData.addExperience(player, (int) (exp * ModConfigs.xpMultiplier), true, true);
											
						if (event.getEntity() instanceof WitherBoss) {
							exp += 1500;
							playerData.addExperience(player, (int) (exp * ModConfigs.xpMultiplier), true, true);
						}
						
						if(!playerData.isAbilityEquipped(Strings.zeroExp)) {
							if(playerData.getNumberOfAbilitiesEquipped(Strings.experienceBoost) > 0 && player.getHealth() <= player.getMaxHealth() / 2) {
								exp *= (1 + playerData.getNumberOfAbilitiesEquipped(Strings.experienceBoost));
							}

							XPEntity xp = new XPEntity(mob.level, player, mob, exp);
							player.level.addFreshEntity(xp);
						}
					}
					
					LivingEntity entity = event.getEntity();
					double x = entity.getX();
					double y = entity.getY();
					double z = entity.getZ();
					
					if(entity.level.random.nextInt(100) <= ModConfigs.munnyDropProbability) {
						int num = Utils.randomWithRange(5, 15);
						num += playerData.getNumberOfAbilitiesEquipped(Strings.jackpot) * 1.2;
						entity.level.addFreshEntity(new MunnyEntity(event.getEntity().level, x, y, z, num));
					}
					
					if(entity.level.random.nextInt(100) <= ModConfigs.hpDropProbability) {
						int num = (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5);
						num += playerData.getNumberOfAbilitiesEquipped(Strings.jackpot) * 1.2;
						entity.level.addFreshEntity(new HPOrbEntity(event.getEntity().level, x, y, z, num));
					}
					
					if(entity.level.random.nextInt(100) <= ModConfigs.mpDropProbability) {
						int num = (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5);
						num += playerData.getNumberOfAbilitiesEquipped(Strings.jackpot) * 1.2;
						entity.level.addFreshEntity(new MPOrbEntity(event.getEntity().level, x, y, z,num));
					}
					
					if(entity.level.random.nextInt(100) <= ModConfigs.driveDropProbability) {
						int num = (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.drivePointsMultiplier);
						num += num * playerData.getNumberOfAbilitiesEquipped(Strings.driveConverter)*0.5;
						entity.level.addFreshEntity(new DriveOrbEntity(event.getEntity().level, x, y, z, num));
					}
				
					if(entity.level.random.nextInt(100) <= ModConfigs.focusDropProbability) {
						int num = (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.focusPointsMultiplier);
						num += num * playerData.getNumberOfAbilitiesEquipped(Strings.focusConverter)*0.25;
						entity.level.addFreshEntity(new FocusOrbEntity(event.getEntity().level, x, y, z, num));
					}
					
					int num = Utils.randomWithRange(0,99);
					if(num < ModConfigs.recipeDropChance + Utils.getLootingLevel(player)) {
						ItemEntity ie = new ItemEntity(player.level, x, y, z, new ItemStack(ModItems.recipeD.get()));
						player.level.addFreshEntity(ie);
					}

					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
				}
			}
			//TODO check if works
			if(event.getEntity() instanceof MoogleEntity && event.getSource().getMsgId().equals("anvil")) {
				ItemEntity ie = new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModBlocks.moogleProjector.get()));
				event.getEntity().level.addFreshEntity(ie);
			}

			if (event.getSource().getEntity() instanceof IKHMob && ModConfigs.playerSpawnHeartless) {
				IKHMob killerMob = (IKHMob) event.getSource().getEntity();
				if (!event.getSource().getEntity().hasCustomName() && (killerMob.getKHMobType() == MobType.HEARTLESS_EMBLEM || killerMob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD)) {
					if (event.getEntity() instanceof Player) { // If a player gets killed by a heartless
						IPlayerCapabilities playerData = ModCapabilities.getPlayer((Player) event.getEntity());

						String[] heartless = ModConfigs.playerSpawnHeartlessData.get(0).split(",");
						String[] nobody = ModConfigs.playerSpawnHeartlessData.get(1).split(",");
						
						DuskEntity newDusk = new DuskEntity(ModEntities.TYPE_DUSK.get(), event.getSource().getEntity().level);
						newDusk.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						newDusk.setCustomName(Component.translatable(event.getEntity().getDisplayName().getString()+"'s Nobody"));
						newDusk.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntity().getMaxHealth() * Double.parseDouble(nobody[1]) / 100, newDusk.getMaxHealth()));
						newDusk.heal(newDusk.getMaxHealth());
						newDusk.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength(true) * Double.parseDouble(nobody[2]) / 100, newDusk.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getEntity().level.addFreshEntity(newDusk);
						
						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getEntity().level);
						newShadow.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						newShadow.setCustomName(Component.translatable(event.getEntity().getDisplayName().getString()+"'s Heartless"));
						newShadow.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntity().getMaxHealth() * Double.parseDouble(heartless[1]) / 100, newShadow.getMaxHealth()));
						newShadow.heal(newShadow.getMaxHealth());
						
						newShadow.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength(true) * Double.parseDouble(heartless[2]) / 100, newShadow.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getEntity().level.addFreshEntity(newShadow);
						
						HeartEntity heart = new HeartEntity(event.getEntity().level);
						heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
						event.getEntity().level.addFreshEntity(heart);

					} else if (event.getEntity() instanceof Villager) {
						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getEntity().level);
						newShadow.setPos(event.getEntity().blockPosition().getX(), event.getEntity().blockPosition().getY(), event.getEntity().blockPosition().getZ());
						event.getSource().getEntity().level.addFreshEntity(newShadow);
						
						HeartEntity heart = new HeartEntity(event.getEntity().level);
						heart.setPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
						event.getEntity().level.addFreshEntity(heart);

					}
				}
			}
			if(event.getEntity() instanceof MarluxiaEntity && event.getSource().getEntity() instanceof Player && event.getSource().getEntity().getLevel().dimension().equals(ModDimensions.STATION_OF_SORROW)) {
				Player player = (Player) event.getSource().getEntity();
				System.out.println(player.getDisplayName().getString()+" killed "+event.getEntity().getDisplayName().getString());
				ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld"));
				BlockPos coords = DimensionCommand.getWorldCoords(player, dimension);
				player.changeDimension(player.getServer().getLevel(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			}
		}
	}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			//Check to prevent edge case crash
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
		if(!event.getLevel().isClientSide()) {
			if(!event.getPlayer().isCreative()) {
				if(event.getState().getBlock() == ModBlocks.prizeBlox.get()) {
					event.getLevel().addFreshEntity(new MunnyEntity((Level) event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Utils.randomWithRange(50, 200)));
				} else if(event.getState().getBlock() == ModBlocks.rarePrizeBlox.get()) {
					event.getLevel().addFreshEntity(new MunnyEntity((Level) event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Utils.randomWithRange(300, 500)));
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		Player oPlayer = event.getOriginal();
		Player nPlayer = event.getEntity();
				
		oPlayer.reviveCaps();
		IPlayerCapabilities oldPlayerData = ModCapabilities.getPlayer(oPlayer);
		IPlayerCapabilities newPlayerData = ModCapabilities.getPlayer(nPlayer);
		
		newPlayerData.setLevel(oldPlayerData.getLevel());
		newPlayerData.setExperience(oldPlayerData.getExperience());
		newPlayerData.setExperienceGiven(oldPlayerData.getExperienceGiven());
		newPlayerData.setStrengthStat(oldPlayerData.getStrengthStat());
		newPlayerData.setMagicStat(oldPlayerData.getMagicStat());
		newPlayerData.setDefenseStat(oldPlayerData.getDefenseStat());
		newPlayerData.setMaxHP(oldPlayerData.getMaxHP());
		newPlayerData.setMP(oldPlayerData.getMP());
		newPlayerData.setMaxMP(oldPlayerData.getMaxMP());
		newPlayerData.setDP(oldPlayerData.getDP());
		newPlayerData.setFP(oldPlayerData.getFP());
		newPlayerData.setMaxDP(oldPlayerData.getMaxDP());
		newPlayerData.setMaxAPStat(oldPlayerData.getMaxAPStat());
		newPlayerData.setFocus(oldPlayerData.getFocus());
		newPlayerData.setMaxFocus(oldPlayerData.getMaxFocus());
		
		newPlayerData.setMunny(oldPlayerData.getMunny());

		newPlayerData.setMagicsMap(oldPlayerData.getMagicsMap());
		newPlayerData.setAbilityMap(oldPlayerData.getAbilityMap());

		newPlayerData.setDriveFormMap(oldPlayerData.getDriveFormMap());
		newPlayerData.setAntiPoints(oldPlayerData.getAntiPoints());
		newPlayerData.setActiveDriveForm(oldPlayerData.getActiveDriveForm());

		newPlayerData.setPartiesInvited(oldPlayerData.getPartiesInvited());
		
		newPlayerData.setKnownRecipeList(oldPlayerData.getKnownRecipeList());
		newPlayerData.setMaterialMap(oldPlayerData.getMaterialMap());

		newPlayerData.equipAllKeychains(oldPlayerData.getEquippedKeychains(), true);

		newPlayerData.setSoAState(oldPlayerData.getSoAState());
		newPlayerData.setReturnDimension(oldPlayerData.getReturnDimension());
		newPlayerData.setReturnLocation(oldPlayerData.getReturnLocation());
		newPlayerData.setChoice(oldPlayerData.getChosen());
		newPlayerData.setChoicePedestal(oldPlayerData.getChoicePedestal());
		newPlayerData.setSacrifice(oldPlayerData.getSacrificed());
		newPlayerData.setSacrificePedestal(oldPlayerData.getSacrificePedestal());

		newPlayerData.setHearts(oldPlayerData.getHearts());
		newPlayerData.setAlignment(oldPlayerData.getAlignment());
		newPlayerData.equipWeapon(oldPlayerData.getEquippedWeapon());
		newPlayerData.setWeaponsUnlocked(oldPlayerData.getWeaponsUnlocked());
		newPlayerData.setLimitCooldownTicks(oldPlayerData.getLimitCooldownTicks());
		
		newPlayerData.setEquippedShotlock(oldPlayerData.getEquippedShotlock());
		newPlayerData.setShotlockList(oldPlayerData.getShotlockList());
		newPlayerData.equipAllItems(oldPlayerData.getEquippedItems(), true);
		newPlayerData.equipAllAccessories(oldPlayerData.getEquippedAccessories(), true);
		newPlayerData.equipAllArmors(oldPlayerData.getEquippedArmors(), true);
		newPlayerData.equipAllKBArmor(oldPlayerData.getEquippedKBArmors(), true);
		newPlayerData.setArmorColor(oldPlayerData.getArmorColor());
		newPlayerData.setArmorGlint(oldPlayerData.getArmorGlint());
		newPlayerData.setRespawnROD(oldPlayerData.getRespawnROD());
		
		nPlayer.setHealth(oldPlayerData.getMaxHP());
		nPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(oldPlayerData.getMaxHP());
		
		newPlayerData.setShortcutsMap(oldPlayerData.getShortcutsMap());
		
		newPlayerData.setSynthLevel(oldPlayerData.getSynthLevel());
		newPlayerData.setSynthExperience(oldPlayerData.getSynthExperience());
		Utils.RefreshAbilityAttributes(nPlayer, newPlayerData);

		PacketHandler.sendTo(new SCSyncWorldCapability(ModCapabilities.getWorld(nPlayer.level)), (ServerPlayer)nPlayer);
		oPlayer.invalidateCaps();
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Player nPlayer = event.getEntity();
		
		IWorldCapabilities newWorldData = ModCapabilities.getWorld(nPlayer.level);
		final IPlayerCapabilities playerData = ModCapabilities.getPlayer(nPlayer);
		nPlayer.setHealth(playerData.getMaxHP());
		Utils.RefreshAbilityAttributes(nPlayer, playerData);

		if(!nPlayer.level.isClientSide)		
			PacketHandler.sendTo(new SCSyncWorldCapability(newWorldData), (ServerPlayer)nPlayer);

		if(!event.isEndConquered() && !nPlayer.level.isClientSide()) {
			if(playerData.getRespawnROD() && ModConfigs.respawnROD) {
				System.out.println(nPlayer.getName().getString()+ " died in ROD, back to it you go");
				ServerPlayer sPlayer = (ServerPlayer) nPlayer;
				ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID,"realm_of_darkness"));
				ServerLevel serverlevel = ((ServerLevel) sPlayer.level).getServer().getLevel(dimension);
				BlockPos pos = serverlevel.getSharedSpawnPos();
				sPlayer.changeDimension(serverlevel, new BaseTeleporter(pos.getX(), pos.getY(), pos.getZ()));							
			}
		}
	}
	
	@SubscribeEvent
	public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent e) {
		Player player = e.getEntity();
		if(!player.level.isClientSide) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			ServerLevel world = player.getServer().getLevel(e.getTo());
			if(e.getTo() == ModDimensions.STATION_OF_SORROW) {
				BlockPos blockPos = player.blockPosition().below(2);
				world.setBlock(blockPos, ModBlocks.sorCore.get().defaultBlockState(), 2);
				if(world.getBlockEntity(blockPos) instanceof SoRCoreTileEntity) {
					SoRCoreTileEntity te = (SoRCoreTileEntity) world.getBlockEntity(blockPos);
					te.setUUID(player.getUUID());
				}
			}
			
			IWorldCapabilities fromWorldData = ModCapabilities.getWorld(player.getServer().getLevel(e.getFrom()));
			IWorldCapabilities toWorldData = ModCapabilities.getWorld(world);
			
			toWorldData.deserializeNBT(fromWorldData.serializeNBT());

			Utils.RefreshAbilityAttributes(player, playerData);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
			PacketHandler.sendTo(new SCSyncWorldCapability(ModCapabilities.getWorld(world)), (ServerPlayer)player);
		}
	}
	
	// Sync drive form on Start Tracking
	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		if (e.getEntity() instanceof Player) {
			Player localPlayer = (Player) e.getEntity();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(localPlayer);
			PacketHandler.syncToAllAround(localPlayer, playerData);
		}
		if (e.getTarget() instanceof Player) {
			Player targetPlayer = (Player) e.getTarget();
			IPlayerCapabilities targetPlayerData = ModCapabilities.getPlayer(targetPlayer);
			PacketHandler.syncToAllAround(targetPlayer, targetPlayerData);
		}
	}


	@SubscribeEvent
	public void looting(LootingLevelEvent event) {
		if (event.getDamageSource() != null) {
			if (event.getDamageSource().getEntity() instanceof Player) {
				Player player = (Player) event.getDamageSource().getEntity();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				event.setLootingLevel(event.getLootingLevel() + playerData.getNumberOfAbilitiesEquipped(Strings.luckyLucky));
			}
		}
	}
}
