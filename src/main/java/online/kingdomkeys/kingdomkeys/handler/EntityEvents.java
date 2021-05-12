package online.kingdomkeys.kingdomkeys.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.KeybladeDamageSource;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.DriveOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.FocusOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.HPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.HeartEntity;
import online.kingdomkeys.kingdomkeys.entity.MPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.MunnyEntity;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.RagnarokShotEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.VolleyShotEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetGlidingPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenAlignmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncKeybladeData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSynthesisData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;
	public int ticks;
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent e) {
		PlayerEntity player = e.getPlayer();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
		if(playerData != null) {
			//Heartless Spawn reset
			if(worldData != null) {
				if(worldData.getHeartlessSpawnLevel() > 0 && ModConfigs.heartlessSpawningMode == SpawningMode.NEVER) {
					worldData.setHeartlessSpawnLevel(0);
				} else if(worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.ALWAYS) {
					worldData.setHeartlessSpawnLevel(1);
				}
			
			}
			
			if (!player.world.isRemote) { // Sync from server to client				
				if (!playerData.getDriveFormMap().containsKey(DriveForm.NONE)) { //One time event here :D
					playerData.setDriveFormLevel(DriveForm.NONE.toString(), 1);
					
					playerData.addKnownRecipe(ModItems.mythril_shard.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.mythril_stone.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.mythril_gem.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.mythril_crystal.get().getRegistryName());
					
					playerData.addAbility(Strings.zeroExp, false);
					
					playerData.addKnownRecipe(ModItems.potion.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.hiPotion.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.megaPotion.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.ether.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.hiEther.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.megaEther.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.elixir.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.megaLixir.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.driveRecovery.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.hiDriveRecovery.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.refocuser.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.hiRefocuser.get().getRegistryName());
					
				}
				
				/*if(playerData.getMaxFocus()<100) { //TODO merge it with above in a few versions.
					playerData.setMaxFocus(100);
					playerData.setFocus(100);
				}*/
				
				
				// TODO (done) Fix for retrocompatibility, remove in a few versions
			/*	if(playerData.getEquippedItems().size() == 0) {
					HashMap<Integer,ItemStack> map = new HashMap<Integer,ItemStack>();
					for(int i = 0 ; i < 4; i++) {
						map.put(i,ItemStack.EMPTY);
					}
					playerData.equipAllItems(map, true);
				}*/
				
				//Fills the map with empty stacks for every form that requires one.
				playerData.getDriveFormMap().keySet().forEach(key -> {
					//Make sure the form exists
					if (ModDriveForms.registry.containsKey(new ResourceLocation(key))) {
						//Check if it requires a slot
						if (ModDriveForms.registry.getValue(new ResourceLocation(key)).hasKeychain()) {
							//Check if the player has form
							if (playerData.getDriveFormMap().containsKey(key)) {
								if (!playerData.getEquippedKeychains().containsKey(new ResourceLocation(key))) {
									playerData.setNewKeychain(new ResourceLocation(key), ItemStack.EMPTY);
								}
							}
						}
					}
				});
				
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				PacketHandler.sendTo(new SCSyncWorldCapability(worldData), (ServerPlayerEntity) player);
	    		PacketHandler.syncToAllAround(player, playerData);


				PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), (ServerPlayerEntity) player);
				PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), (ServerPlayerEntity)player);
				PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), (ServerPlayerEntity)player);
				//TODO dimension
				/*
				if (player.dimension.getId() == ModDimensions.DIVE_TO_THE_HEART_TYPE.getId()) {
					PacketHandler.sendTo(new SCUpdateSoA(playerData), (ServerPlayerEntity) player);
				}
				 */
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
				if(!event.player.world.isRemote && event.player.ticksExisted == 5) { //TODO Check if it's necessary, I thought it was to set the max hp value but now it seems to work fine without it
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)event.player);
				}
				
				if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
					if (playerData.getFP() > 0) {
						playerData.setFP(playerData.getFP() - 0.3);
					} else {
						playerData.setActiveDriveForm(DriveForm.NONE.toString());
						event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						if(!event.player.world.isRemote) {
							PacketHandler.syncToAllAround(event.player, playerData);
						}
					}
				} else if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
					ModDriveForms.registry.getValue(new ResourceLocation(playerData.getActiveDriveForm())).updateDrive(event.player);
				}
				//Limit recharge system
				if(playerData.getLimitCooldownTicks() > 0 && !event.player.world.isRemote) {
					playerData.setLimitCooldownTicks(playerData.getLimitCooldownTicks() - 1);
					if(playerData.getLimitCooldownTicks() <= 0) {
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);
					}
				}
				
				//Magic CD recharge system
				if(playerData.getMagicCooldownTicks() > 0 && !event.player.world.isRemote) {
					playerData.setMagicCooldownTicks(playerData.getMagicCooldownTicks() - 1);
					if(playerData.getMagicCooldownTicks() <= 0) {
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);
					}
				}
				
				// MP Recharge system
				if (playerData.getRecharge()) {
					if (playerData.getMP() >= playerData.getMaxMP()) { //Has recharged fully
						playerData.setRecharge(false);
						playerData.setMP(playerData.getMaxMP());
					} else { //Still recharging
						playerData.addMP(playerData.getMaxMP()/500 * ((Utils.getMPHasteValue(playerData)/10) + 2));
					}
					
					//PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);
	
				} else { // Not on recharge
					if (playerData.getMP() <= 0 && playerData.getMaxMP() > 0) {
						playerData.setRecharge(true);
						if(!event.player.world.isRemote) {
							PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);
						}
					}
				}
	
				if(!event.player.world.isRemote) {
					if (playerData.getAlignment() == Utils.OrgMember.NONE) {
						if (!openedAlignment.containsKey(event.player.getUniqueID())) {
							openedAlignment.put(event.player.getUniqueID(), false);
						}
						boolean wearingOrgCloak = Utils.isWearingOrgRobes(event.player);
	
						if (wearingOrgCloak) {
							if (!openedAlignment.get(event.player.getUniqueID())) {
								PacketHandler.sendTo(new SCOpenAlignmentScreen(), (ServerPlayerEntity) event.player);
								openedAlignment.put(event.player.getUniqueID(), true);
							}
						} else {
							openedAlignment.put(event.player.getUniqueID(), false);
						}
					}
				}
			}
		}

		if(ticks % 5 == 0) {
			// Combat mode
			List<Entity> entities = event.player.world.getEntitiesWithinAABBExcludingEntity(event.player, event.player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
			List<Entity> bossEntities = event.player.world.getEntitiesWithinAABBExcludingEntity(event.player, event.player.getBoundingBox().grow(150.0D, 100.0D, 150.0D).offset(-75.0D, -50.0D, -75.0D));
			if (!bossEntities.isEmpty()) {
				for (int i = 0; i < bossEntities.size(); i++) {
					if (bossEntities.get(i) instanceof EnderDragonEntity || bossEntities.get(i) instanceof WitherEntity) {
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
					if (entity instanceof MonsterEntity) {
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
	public void onLivingUpdate(LivingUpdateEvent event) {
		IGlobalCapabilities globalData = ModCapabilities.getGlobal(event.getEntityLiving());
		IPlayerCapabilities playerData = null;
		PlayerEntity player = null;
		if (event.getEntityLiving() instanceof PlayerEntity) {
			player = (PlayerEntity) event.getEntityLiving();
			playerData = ModCapabilities.getPlayer(player);
		}

		//MinecraftForge.EVENT_BUS.post(new EntityEvent.EyeHeight(player, player.getPose(), player.getSize(player.getPose()), player.getHeight()));

		if (globalData != null) {
			// Stop
			if (globalData.getStoppedTicks() > 0) {
				globalData.subStoppedTicks(1);

				event.getEntityLiving().setMotion(0, 0, 0);
				event.getEntityLiving().velocityChanged = true;

				if (event.getEntityLiving() instanceof MobEntity) {
					((MobEntity) event.getEntityLiving()).setAttackTarget(null);
				}

				if (globalData.getStoppedTicks() <= 0) {
					if(event.getEntityLiving() instanceof MobEntity) {
                		((MobEntity) event.getEntityLiving()).setNoAI(false);
                	}
					
					globalData.setStoppedTicks(0); // Just in case it goes below (shouldn't happen)
					if (globalData.getDamage() > 0 && globalData.getStopCaster() != null) {
						event.getEntityLiving().attackEntityFrom(StopDamageSource.getStopDamage(Utils.getPlayerByName(event.getEntity().world, globalData.getStopCaster())), globalData.getDamage()/2);
					}
					
					if (event.getEntityLiving() instanceof ServerPlayerEntity) // Packet to unfreeze client
						PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayerEntity) event.getEntityLiving());
					globalData.setDamage(0);
					globalData.setStopCaster(null);
				}
			}

			// Gravity
			if (globalData.getFlatTicks() > 0) {
				globalData.subFlatTicks(1);
				
				if(event.getEntityLiving() instanceof PlayerEntity) {
					if(((PlayerEntity)event.getEntityLiving()).getForcedPose() != Pose.SWIMMING){
						((PlayerEntity)event.getEntityLiving()).setForcedPose(Pose.SWIMMING);
					}					
					//System.out.println(((PlayerEntity)event.getEntityLiving()).getPose());
				}
			
				event.getEntityLiving().setMotion(0, -4, 0);
				event.getEntityLiving().velocityChanged = true;

				if (globalData.getFlatTicks() <= 0) {
					globalData.setFlatTicks(0); // Just in case it goes below (shouldn't happen)
					
					if (event.getEntityLiving() instanceof LivingEntity) {// This should sync the state of this entity (player or mob) to all the clients around to stop render it flat
						PacketHandler.syncToAllAround(event.getEntityLiving(), globalData);
						
						if (event.getEntityLiving() instanceof ServerPlayerEntity) {
							PacketHandler.sendTo(new SCRecalculateEyeHeight(), (ServerPlayerEntity) event.getEntityLiving());
						}
					}
					
				}
			} else {
				if(event.getEntityLiving() instanceof PlayerEntity) {
					PlayerEntity pl = (PlayerEntity) event.getEntityLiving();
					if(pl.getForcedPose() != null && !ModCapabilities.getPlayer(pl).getIsGliding()){
						pl.setForcedPose(null);
					}					
				}
			}
		}

		if (playerData != null) {
			// Drive Form abilities
			/*if(shouldHandleHighJump(player, playerData)) {
				handleHighJump(player, playerData);
			}
			if(playerData.getActiveDriveForm().equals(Strings.Form_Master) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Master) && playerData.getDriveFormLevel(Strings.Form_Master) >= 3 && playerData.getEquippedAbilityLevel(Strings.aerialDodge) != null && playerData.getEquippedAbilityLevel(Strings.aerialDodge)[1] > 0)) {
				handleAerialDodge(player, playerData);
			}
			if(playerData.getActiveDriveForm().equals(Strings.Form_Final) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Final) && playerData.getDriveFormLevel(Strings.Form_Final) >= 3 && playerData.getEquippedAbilityLevel(Strings.glide) != null && playerData.getEquippedAbilityLevel(Strings.glide)[1] > 0)) {
				handleGlide(player, playerData);
			}*/

			//Rotation ticks should always be lost, this way we prevent the spinning animation on other players (hopefully)
			if (playerData.getAerialDodgeTicks() > 0) {
				playerData.setAerialDodgeTicks(playerData.getAerialDodgeTicks() - 1);
			}
				
			//Reflect
			if (playerData.getReflectTicks() > 0) {
				playerData.remReflectTicks(1);

				event.getEntityLiving().setMotion(0, 0, 0);
				event.getEntityLiving().velocityChanged = true;

				// Spawn particles
				float radius = 1.5F;
				double X = event.getEntityLiving().getPosX();
				double Y = event.getEntityLiving().getPosY();
				double Z = event.getEntityLiving().getPosZ();

				for (int t = 1; t < 360; t += 20) {
					for (int s = 1; s < 360; s += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
						double y = Y + (radius * Math.cos(Math.toRadians(t)));
						event.getEntityLiving().world.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
					}
				}

			} else { // When it finishes
				if (playerData.getReflectActive()) {// If has been hit
					// SPAWN ENTITY and apply damage
					List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(4, 4, 4));
					Party casterParty = ModCapabilities.getWorld(player.world).getPartyFromMember(player.getUniqueID());

					if(casterParty != null && !casterParty.getFriendlyFire()) {
						for(Member m : casterParty.getMembers()) {
							list.remove(player.world.getPlayerByUuid(m.getUUID()));
						}
					}
					
					double X = event.getEntityLiving().getPosX();
					double Y = event.getEntityLiving().getPosY();
					double Z = event.getEntityLiving().getPosZ();

					float radius = 3.5F;
					//System.out.println(event.getEntityLiving().world);

					for (int t = 1; t < 360; t += 20) {
						double x = X + (radius * Math.cos(Math.toRadians(t)));
						double z = Z + (radius * Math.sin(Math.toRadians(t)));
						((ServerWorld)event.getEntityLiving().world).spawnParticle(ParticleTypes.BUBBLE.getType(), x, Y + 1, z, 5, 0, 0, 0, 1);
					}
					
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							Entity e = (Entity) list.get(i);
							if (e instanceof LivingEntity) {
								e.attackEntityFrom(DamageSource.causePlayerDamage(player), DamageCalculation.getMagicDamage(player));
							}
						}
						player.world.playSound(null, player.getPosition(), ModSounds.reflect2.get(), SoundCategory.PLAYERS, 1F, 1F);

					}
					playerData.setReflectActive(false); // Restart reflect
				}
			}
			
			//Aero
			if (playerData.getAeroTicks() > 0) {
				playerData.remAeroTicks(1);

				if(player.ticksExisted % 5 == 0) {
					// Spawn particles
					float radius = 1F;
					double X = event.getEntityLiving().getPosX();
					double Y = event.getEntityLiving().getPosY();
					double Z = event.getEntityLiving().getPosZ();

					for (int t = 1; t < 360; t += 30) {
						for (int s = 1; s < 360; s += 30) {
							double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t))/2);
							double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t))/2);
							double y = Y + (radius * Math.cos(Math.toRadians(t)));
							event.getEntityLiving().world.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
						}
					}
				}
			} 
			
		}
	}
		
	@SubscribeEvent
	public void entityPickup(EntityItemPickupEvent event) {
		if(event.getPlayer().inventory.hasItemStack(new ItemStack(ModItems.synthesisBag.get()))) {
			if(event.getItem().getItem() != null && event.getItem().getItem().getItem() instanceof SynthesisItem) {
				for (int i = 0; i < event.getPlayer().inventory.getSizeInventory(); i++) {
					ItemStack bag = event.getPlayer().inventory.getStackInSlot(i);
					if (!ItemStack.areItemStacksEqual(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.synthesisBag.get()) {
							IItemHandler inv = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
							addSynthesisMaterialToBag(inv, event, bag);
						}
					}
				}
			}
		}
	}
	
	public void addSynthesisMaterialToBag(IItemHandler inv, EntityItemPickupEvent event, ItemStack bag) {
		CompoundNBT nbt = bag.getOrCreateTag();
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
			if (!ItemStack.areItemStacksEqual(bagItem, ItemStack.EMPTY)) {
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
			} else if (ItemStack.areItemStacksEqual(bagItem, ItemStack.EMPTY)) {
				inv.insertItem(j, pickUp.copy(), false);
				pickUp.setCount(0);
				return;
			}
		}
	}
	
	@SubscribeEvent
	public void hitEntity(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			
			ItemStack weapon = null;
			weapon = Utils.getWeaponDamageStack(event.getSource(), player);
			if(weapon != null && !(event.getSource() instanceof StopDamageSource)) {
				float dmg = 0;
				if(weapon.getItem() instanceof KeybladeItem) {
					dmg = DamageCalculation.getKBStrengthDamage(player, weapon);
				} else if(weapon.getItem() instanceof IOrgWeapon) {
					dmg = DamageCalculation.getOrgStrengthDamage(player, weapon);
				}
				event.setAmount(dmg);
			}
			
			if(ModCapabilities.getPlayer(player).getActiveDriveForm().equals(Strings.Form_Anti)) {
				event.setAmount(ModCapabilities.getPlayer(player).getStrength());
			}
			
			LivingEntity target = event.getEntityLiving();
			
			if(event.getSource().getImmediateSource() instanceof VolleyShotEntity || event.getSource().getImmediateSource() instanceof RagnarokShotEntity || event.getSource().getImmediateSource() instanceof ThunderBoltEntity) {
				target.hurtResistantTime = 0;
			}

			if (target instanceof PlayerEntity) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer((PlayerEntity) target);

				if (playerData.getReflectTicks() <= 0) { // If is casting reflect
					if (playerData.isAbilityEquipped(Strings.mpRage)) {
						playerData.addMP(event.getAmount() * 0.05F);
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) target);
					}

					if (playerData.isAbilityEquipped(Strings.damageDrive)) {
						playerData.addDP(event.getAmount() * 0.05F);
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) target);
					}
				}

			}
			
		}
		
		//This is outside as it should apply the formula if you have been hit by non player too		
		if(event.getEntityLiving() instanceof PlayerEntity) { 
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			float damage = (float) Math.round((event.getAmount() * 100 / ((100 + (playerData.getLevel() * 2)) + playerData.getDefense())));
			if(playerData.getAeroTicks() > 0) {
				playerData.remAeroTicks((int) damage * 2);
				damage -= (damage * 0.3);
			}
			
			playerData = Utils.chooseAutoForm(player);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);

			//TODO 
			event.setAmount(damage <= 0 ? 1 : damage);
		}
	}

	// Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if(!event.getEntityLiving().world.isRemote) {
			if (event.getSource().getTrueSource() instanceof LivingEntity) { // If attacker is a LivingEntity
				LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
				LivingEntity target = event.getEntityLiving();
				
				if(attacker instanceof PlayerEntity && target instanceof PlayerEntity) {
					Party p = ModCapabilities.getWorld(attacker.world).getPartyFromMember(attacker.getUniqueID());
					if(p != null && p.getMember(event.getEntityLiving().getUniqueID()) != null && !p.getFriendlyFire()) {
						event.setCanceled(true);
					}
				}
								
				if (target instanceof PlayerEntity) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer((PlayerEntity) target);
	
					if (playerData.getReflectTicks() > 0) { // If is casting reflect
						if (!playerData.getReflectActive()) // If has been hit while casting reflect
							playerData.setReflectActive(true);
						event.setCanceled(true);
					}
	
				}
	
				IGlobalCapabilities globalData = ModCapabilities.getGlobal(target);
				if (globalData != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
					PlayerEntity source = (PlayerEntity) event.getSource().getTrueSource();
					if (globalData.getStoppedTicks() > 0) {
						float dmg = event.getAmount();
						if (event.getSource().getTrueSource() instanceof PlayerEntity) {
							ItemStack stack = Utils.getWeaponDamageStack(event.getSource(), source);
							if(stack != null) {
								if(stack.getItem() instanceof KeybladeItem) {
									dmg = DamageCalculation.getKBStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), stack);
								} else if(stack.getItem() instanceof IOrgWeapon){
									dmg = DamageCalculation.getOrgStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), stack);
								}
							}
							
							if(dmg == 0) {
								dmg = event.getAmount();
							}
						}
					//	System.out.println(dmg);
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
		IWorldCapabilities worldData = ModCapabilities.getWorld(event.getEntityLiving().world);
		if (event.getEntityLiving() instanceof EnderDragonEntity) {
			LivingEntity entity = event.getEntityLiving();
			if (worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_DRAGON) {
				worldData.setHeartlessSpawnLevel(1);
			}

			for(PlayerEntity p : entity.world.getPlayers()) {
				entity.world.addEntity(new ItemEntity(entity.world, p.getPosX(), p.getPosY(), p.getPosZ(), new ItemStack(ModItems.proofOfHeart.get(), 1)));
			}
		}

		if (!event.getEntity().world.isRemote) {
			if (event.getSource().getImmediateSource() instanceof PlayerEntity || event.getSource().getTrueSource() instanceof PlayerEntity) { //If the player kills
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

				//TODO more sophisticated and dynamic way to do this
				//Give hearts
				if (player.getHeldItemMainhand().getItem() instanceof IOrgWeapon || player.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
					int multiplier = 1;
					if (player.getHeldItemMainhand().getItem() instanceof IOrgWeapon) {
						IOrgWeapon weapon = (IOrgWeapon) player.getHeldItemMainhand().getItem();
						if (weapon.getMember() == playerData.getAlignment()) {
							multiplier = 2;
						}
					}
					if (event.getEntity() instanceof IKHMob) {
						IKHMob mob = (IKHMob) event.getEntity();
						if (mob.getMobType() == MobType.HEARTLESS_EMBLEM) {
							playerData.addHearts((int)((20 * multiplier) * ModConfigs.heartMultiplier));
						}
					} else if (event.getEntity() instanceof EnderDragonEntity || event.getEntity() instanceof WitherEntity) {
						playerData.addHearts((int)((1000 * multiplier) * ModConfigs.heartMultiplier));
					} else if (event.getEntity() instanceof VillagerEntity) {
						playerData.addHearts((int)((5 * multiplier) * ModConfigs.heartMultiplier));
					} else if (event.getEntity() instanceof MonsterEntity) {
						playerData.addHearts((int)((2 * multiplier) * ModConfigs.heartMultiplier));
					} else {
						playerData.addHearts((int)((1 * multiplier) * ModConfigs.heartMultiplier));
					}
				}
				if(event.getEntityLiving() instanceof IKHMob) {
					IKHMob heartless = (IKHMob) event.getEntityLiving();
					if(heartless.getMobType() == MobType.HEARTLESS_EMBLEM && Utils.getWeaponDamageStack(event.getSource(), player) != null && Utils.getWeaponDamageStack(event.getSource(), player).getItem() instanceof KeybladeItem) {
						HeartEntity heart = new HeartEntity(event.getEntityLiving().world);
						heart.setPosition(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 1, event.getEntityLiving().getPosZ());
						event.getEntityLiving().world.addEntity(heart);
					}
				}
				
				if (event.getEntity().getClassification(false) == EntityClassification.MONSTER) {
					if(!playerData.isAbilityEquipped(Strings.zeroExp)) {
						LivingEntity mob = (LivingEntity) event.getEntity();
						
						double value = mob.getAttribute(Attributes.MAX_HEALTH).getValue() / 2;
						double exp = Utils.randomWithRange(value * 0.8, value * 1.8);
						playerData.addExperience(player, (int) ((int)exp * ModConfigs.xpMultiplier), true);
											
						if (event.getEntity() instanceof WitherEntity) {
							playerData.addExperience(player, 1500, true);
						}
						
					}
					LivingEntity entity = event.getEntityLiving();
					double x = entity.getPosX();
					double y = entity.getPosY();
					double z = entity.getPosZ();
					
					if(entity.world.rand.nextInt(100) <= ModConfigs.munnyDropProbability)
						entity.world.addEntity(new MunnyEntity(event.getEntity().world, x, y, z, Utils.randomWithRange(5, 15)));
					if(entity.world.rand.nextInt(100) <= ModConfigs.hpDropProbability)
						entity.world.addEntity(new HPOrbEntity(event.getEntity().world, x, y, z, (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5)));
					if(entity.world.rand.nextInt(100) <= ModConfigs.mpDropProbability)
						entity.world.addEntity(new MPOrbEntity(event.getEntity().world, x, y, z, (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5)));
					if(entity.world.rand.nextInt(100) <= ModConfigs.driveDropProbability)
						entity.world.addEntity(new DriveOrbEntity(event.getEntity().world, x, y, z, (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.drivePointsMultiplier)));
					if(entity.world.rand.nextInt(100) <= ModConfigs.focusDropProbability)
					entity.world.addEntity(new FocusOrbEntity(event.getEntity().world, x, y, z, (int) (Utils.randomWithRange(entity.getMaxHealth() * 0.1F, entity.getMaxHealth() * 0.25F) * ModConfigs.focusPointsMultiplier)));
					
					int num = Utils.randomWithRange(0,99);

					if(num < ModConfigs.recipeDropChance) {
						ItemEntity ie = new ItemEntity(player.world, x, y, z, new ItemStack(ModItems.recipe.get()));
						player.world.addEntity(ie);
					}

					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				}
			}
			
			if(event.getEntity() instanceof MoogleEntity && event.getSource() == DamageSource.ANVIL) {
				ItemEntity ie = new ItemEntity(event.getEntity().world, event.getEntity().getPosX(), event.getEntity().getPosY(), event.getEntity().getPosZ(), new ItemStack(ModBlocks.moogleProjector.get()));
				event.getEntity().world.addEntity(ie);
			}

			if (event.getSource().getTrueSource() instanceof IKHMob && ModConfigs.playerSpawnHeartless) {
				IKHMob killerMob = (IKHMob) event.getSource().getTrueSource();
				if (!event.getSource().getTrueSource().hasCustomName() && (killerMob.getMobType() == MobType.HEARTLESS_EMBLEM || killerMob.getMobType() == MobType.HEARTLESS_PUREBLOOD)) {
					if (event.getEntityLiving() instanceof PlayerEntity) { // If a player gets killed by a heartless
						IPlayerCapabilities playerData = ModCapabilities.getPlayer((PlayerEntity) event.getEntityLiving());

						String[] heartless = ModConfigs.playerSpawnHeartlessData.get(0).split(",");
						String[] nobody = ModConfigs.playerSpawnHeartlessData.get(1).split(",");
						
						DuskEntity newDusk = new DuskEntity(ModEntities.TYPE_DUSK.get(), event.getSource().getTrueSource().world);
						newDusk.setPosition(event.getEntityLiving().getPosition().getX(), event.getEntityLiving().getPosition().getY(), event.getEntityLiving().getPosition().getZ());
						newDusk.setCustomName(new TranslationTextComponent(event.getEntityLiving().getDisplayName().getString()+"'s Nobody"));
						newDusk.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntityLiving().getMaxHealth() * Double.parseDouble(nobody[1]) / 100, newDusk.getMaxHealth()));
						newDusk.heal(newDusk.getMaxHealth());
						newDusk.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength() * Double.parseDouble(nobody[2]) / 100, newDusk.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getTrueSource().world.addEntity(newDusk);
						
						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getTrueSource().world);
						newShadow.setPosition(event.getEntityLiving().getPosition().getX(), event.getEntityLiving().getPosition().getY(), event.getEntityLiving().getPosition().getZ());
						newShadow.setCustomName(new TranslationTextComponent(event.getEntityLiving().getDisplayName().getString()+"'s Heartless"));
						newShadow.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(event.getEntityLiving().getMaxHealth() * Double.parseDouble(heartless[1]) / 100, newShadow.getMaxHealth()));
						newShadow.heal(newShadow.getMaxHealth());
						//System.out.println(Math.max(playerData.getStrength(), newShadow.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						newShadow.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(playerData.getStrength() * Double.parseDouble(heartless[2]) / 100, newShadow.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
						event.getSource().getTrueSource().world.addEntity(newShadow);
						
						HeartEntity heart = new HeartEntity(event.getEntityLiving().world);
						heart.setPosition(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 1, event.getEntityLiving().getPosZ());
						event.getEntityLiving().world.addEntity(heart);

					} else if (event.getEntityLiving() instanceof VillagerEntity) {
						ShadowEntity newShadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), event.getSource().getTrueSource().world);
						newShadow.setPosition(event.getEntityLiving().getPosition().getX(), event.getEntityLiving().getPosition().getY(), event.getEntityLiving().getPosition().getZ());
						event.getSource().getTrueSource().world.addEntity(newShadow);
						
						HeartEntity heart = new HeartEntity(event.getEntityLiving().world);
						heart.setPosition(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 1, event.getEntityLiving().getPosZ());
						event.getEntityLiving().world.addEntity(heart);

					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
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
		if(!event.getWorld().isRemote()) {
			if(!event.getPlayer().isCreative()) {
				if(event.getState().getBlock() == ModBlocks.prizeBlox.get()) {
					event.getWorld().addEntity(new MunnyEntity((World) event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Utils.randomWithRange(50, 200)));
				} else if(event.getState().getBlock() == ModBlocks.rarePrizeBlox.get()) {
					event.getWorld().addEntity(new MunnyEntity((World) event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Utils.randomWithRange(300, 500)));
				}
			}
		}
	}


	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		PlayerEntity oPlayer = event.getOriginal();
		PlayerEntity nPlayer = event.getPlayer();
		IPlayerCapabilities oldPlayerData = ModCapabilities.getPlayer(oPlayer);
		IPlayerCapabilities newPlayerData = ModCapabilities.getPlayer(nPlayer);
		newPlayerData.setLevel(oldPlayerData.getLevel());
		newPlayerData.setExperience(oldPlayerData.getExperience());
		newPlayerData.setExperienceGiven(oldPlayerData.getExperienceGiven());
		newPlayerData.setStrength(oldPlayerData.getStrength());
		newPlayerData.setMagic(oldPlayerData.getMagic());
		newPlayerData.setDefense(oldPlayerData.getDefense());
		newPlayerData.setMaxHP(oldPlayerData.getMaxHP());
		newPlayerData.setMP(oldPlayerData.getMP());
		newPlayerData.setMaxMP(oldPlayerData.getMaxMP());
		newPlayerData.setDP(oldPlayerData.getDP());
		newPlayerData.setFP(oldPlayerData.getFP());
		newPlayerData.setMaxDP(oldPlayerData.getMaxDP());
		newPlayerData.setMaxAP(oldPlayerData.getMaxAP());
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

		nPlayer.setHealth(oldPlayerData.getMaxHP());
		nPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(oldPlayerData.getMaxHP());
		

		PacketHandler.sendTo(new SCSyncWorldCapability(ModCapabilities.getWorld(nPlayer.world)), (ServerPlayerEntity)nPlayer);

	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		PlayerEntity nPlayer = event.getPlayer();
		IWorldCapabilities newWorldData = ModCapabilities.getWorld(nPlayer.world);
		nPlayer.setHealth(ModCapabilities.getPlayer(nPlayer).getMaxHP());

		if(!nPlayer.world.isRemote)		
			PacketHandler.sendTo(new SCSyncWorldCapability(newWorldData), (ServerPlayerEntity)nPlayer);
	}
	
	@SubscribeEvent
	public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent e) {
		PlayerEntity player = e.getPlayer();
		if(!player.world.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			/*IWorldCapabilities fromWorldData = ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getFrom()));
			IWorldCapabilities toWorldData = ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getTo()));

			toWorldData.setParties(fromWorldData.getParties());
			toWorldData.setHeartlessSpawnLevel(fromWorldData.getHeartlessSpawnLevel());
			toWorldData.setPortals(fromWorldData.getPortals());*/
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
			PacketHandler.sendTo(new SCSyncWorldCapability(ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getTo()))), (ServerPlayerEntity)player);
		}
	}
	
	// Sync drive form on Start Tracking
	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof PlayerEntity) {
			//System.out.println(e.getTarget());
			PlayerEntity targetPlayer = (PlayerEntity) e.getTarget();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(targetPlayer);
			PacketHandler.syncToAllAround(targetPlayer, playerData);
		}
	}
}
