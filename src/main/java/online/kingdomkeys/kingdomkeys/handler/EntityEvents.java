package online.kingdomkeys.kingdomkeys.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.CommonConfig;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.KeybladeDamageSource;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.*;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetGlidingPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenAlignmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncKeybladeData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSynthesisData;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateSoA;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

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
				player.setHealth(playerData.getMaxHP());
				player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
				
				if (!playerData.getDriveFormMap().containsKey(DriveForm.NONE)) { //One time event here :D
					playerData.setDriveFormLevel(DriveForm.NONE.toString(), 1);
					
					playerData.addKnownRecipe(ModItems.mythril_shard.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.mythril_stone.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.mythril_gem.get().getRegistryName());
					playerData.addKnownRecipe(ModItems.mythril_crystal.get().getRegistryName());
					
					playerData.addAbility(Strings.zeroExp, false);
				}
				
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
				
				List<UUID> portalUUID = playerData.getPortalUUIDList();
				for(byte i = 0; i < portalUUID.size(); i++) {
					if(worldData.getPortalFromUUID(portalUUID.get(i)) == null) {
						playerData.setPortalCoordsUUID(i, new UUID(0,0));
					}
				}
				
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				PacketHandler.sendTo(new SCSyncWorldCapability(worldData), (ServerPlayerEntity) player);

				PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), (ServerPlayerEntity) player);
				PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), (ServerPlayerEntity)player);
				PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), (ServerPlayerEntity)player);
				if (player.dimension.getId() == ModDimensions.DIVE_TO_THE_HEART_TYPE.getId()) {
					PacketHandler.sendTo(new SCUpdateSoA(playerData), (ServerPlayerEntity) player);
				}
			}
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	Map<UUID, Boolean> openedAlignment = new HashMap<>();

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(ticks >= Integer.MAX_VALUE) {
			ticks = Integer.MIN_VALUE;
		}
		ticks++;
		
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);

		if (playerData != null) {
			//System.out.println(event.player.world.isRemote+" "+playerData.getPartiesInvited());
			if(!event.player.world.isRemote && event.player.ticksExisted == 5) {
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
		
			// MP Recharge system
			if (playerData.getRecharge()) {
				if (playerData.getMP() >= playerData.getMaxMP()) { //Has recharged fully
					playerData.setRecharge(false);
					playerData.setMP(playerData.getMaxMP());
				} else { //Still recharging
					// if (event.player.ticksExisted % 1 == 0)
					//System.out.println((Utils.getMPHasteValue(playerData)/10) + 1);
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
					if (entity instanceof MobEntity) {
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
			playerData = ModCapabilities.getPlayer((PlayerEntity) event.getEntityLiving());
		}

		//MinecraftForge.EVENT_BUS.post(new EntityEvent.EyeHeight(player, player.getPose(), player.getSize(player.getPose()), player.getHeight()));

		if (globalData != null) {
			// Stop
			if (globalData.getStoppedTicks() > 0) {
				globalData.subStoppedTicks(1);

				event.getEntityLiving().setMotion(0, 0, 0);
				event.getEntityLiving().velocityChanged = true;

				if (globalData.getStoppedTicks() <= 0) {
					globalData.setStoppedTicks(0); // Just in case it goes below (shouldn't happen)
					if (globalData.getDamage() > 0 && globalData.getStopCaster() != null) {
						event.getEntityLiving().attackEntityFrom(DamageSource.causePlayerDamage(Utils.getPlayerByName(event.getEntity().world, globalData.getStopCaster())), globalData.getDamage()/2);
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
			}
		}

		if (playerData != null) {
			// Drive Form abilities
				if(shouldHandleHighJump(player, playerData)) {
					handleHighJump(player, playerData);
				}
				if(playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
					//handleQuickRun(player, playerData);
				}
				if(playerData.getActiveDriveForm().equals(Strings.Form_Limit)) {
					//handleDodgeRoll(player, playerData);
				}
				if(playerData.getActiveDriveForm().equals(Strings.Form_Master) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Master) && playerData.getDriveFormLevel(Strings.Form_Master) >= 3 && playerData.getEquippedAbilityLevel(Strings.aerialDodge) != null && playerData.getEquippedAbilityLevel(Strings.aerialDodge)[1] > 0)) {
					handleAerialDodge(player, playerData);
				}
				if(playerData.getActiveDriveForm().equals(Strings.Form_Final) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Final) && playerData.getDriveFormLevel(Strings.Form_Final) >= 3 && playerData.getEquippedAbilityLevel(Strings.glide) != null && playerData.getEquippedAbilityLevel(Strings.glide)[1] > 0)) {
					handleGlide(player, playerData);
				}

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
				double freq = 0.6;
				double X = event.getEntityLiving().getPosX();
				double Y = event.getEntityLiving().getPosY();
				double Z = event.getEntityLiving().getPosZ();

				for (double x = X - radius; x <= X + radius; x += freq) {
					for (double y = Y - radius; y <= Y + radius; y += freq) {
						for (double z = Z - radius; z <= Z + radius; z += freq) {
							if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radius * radius) {
								event.getEntityLiving().world.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
							}
						}
					}
				}

			} else { // When it finishes
				if (playerData.getReflectActive()) {// If has been hit
					// SPAWN ENTITY and apply damage
					List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(8.0D, 4.0D, 8.0D).offset(-4.0D, -1.0D, -4.0D));
					Party casterParty = ModCapabilities.getWorld(player.world).getPartyFromMember(player.getUniqueID());

					if(casterParty != null) {
						for(Party.Member m : casterParty.getMembers()) {
							list.remove(player.world.getPlayerByUuid(m.getUUID()));
						}
					}

					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							Entity e = (Entity) list.get(i);
							if (e instanceof LivingEntity) {
								e.attackEntityFrom(DamageSource.causePlayerDamage(player), 10);
							}
						}
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
					double freq = 0.5;
					double X = event.getEntityLiving().getPosX();
					double Y = event.getEntityLiving().getPosY();
					double Z = event.getEntityLiving().getPosZ();
	
					for (double x = X - radius; x <= X + radius; x += freq) {
						for (double y = Y - radius; y <= Y + radius; y += freq) {
							for (double z = Z - radius; z <= Z + radius; z += freq) {
								if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radius * radius) {
									event.getEntityLiving().world.addParticle(ParticleTypes.ENCHANTED_HIT, x, y + 1, z, 0, 0, 0);
								}
							}
						}
					}
				}
			} 
		}
	}
	
	private void handleHighJump(PlayerEntity player, IPlayerCapabilities playerData) {
		boolean j = false;
        if(player.world.isRemote) {
            j = Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown();
        }

        if (j) {
            if(player.getMotion().y > 0) {
            	if(playerData.getActiveDriveForm().equals(Strings.Form_Final)) {
	            	player.setMotion(player.getMotion().add(0,DriveForm.FINAL_JUMP_BOOST[playerData.getDriveFormLevel(Strings.Form_Final)],0));
            	} else {
            		//System.out.println(playerData.getDriveFormMap() != null);
            		if(playerData.getActiveDriveForm() != null) {
            			//System.out.println(playerData.getDriveFormLevel(Strings.Form_Valor));
            			int jumpLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Valor)-2 : playerData.getDriveFormLevel(Strings.Form_Valor);//TODO eventually replace it with the skill
	            		player.setMotion(player.getMotion().add(0,DriveForm.VALOR_JUMP_BOOST[jumpLevel],0));
            		}
	            }
            }
        }
	}
	
	private boolean shouldHandleHighJump(PlayerEntity player, IPlayerCapabilities playerData) {
		if(playerData.getDriveFormMap() == null)
			return false;
		if(playerData.getActiveDriveForm().equals(Strings.Form_Valor) || playerData.getActiveDriveForm().equals(Strings.Form_Final)) {
			return true;
		}

		if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())
				&& (playerData.getDriveFormMap().containsKey(Strings.Form_Valor)
				&& playerData.getDriveFormLevel(Strings.Form_Valor) >= 3
				&& playerData.getEquippedAbilityLevel(Strings.highJump) != null
				&& playerData.getEquippedAbilityLevel(Strings.highJump)[1]>0)){
			return true;
		}
		return false;
	}

	private void handleAerialDodge(PlayerEntity player, IPlayerCapabilities playerData) {
		if (playerData.getAerialDodgeTicks() <= 0) {
			if (player.onGround) {
				playerData.setHasJumpedAerialDodge(false);
				playerData.setAerialDodgeTicks(0);
			} else {
				if (player.world.isRemote) {
					if (player.getMotion().y < 0 && Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown() && !player.isSneaking()) {
						if (!playerData.hasJumpedAerialDodge()) {
							playerData.setHasJumpedAerialDodge(true);
							player.jump();
							int jumpLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Master)-2 : playerData.getDriveFormLevel(Strings.Form_Master);//TODO eventually replace it with the skill
							float boost = DriveForm.MASTER_AERIAL_DODGE_BOOST[jumpLevel];
							player.setMotion(player.getMotion().mul(new Vec3d(boost, boost, boost)));
							PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 10));
						}
					}
				}
			}
		}
	}

	private void handleGlide(PlayerEntity player, IPlayerCapabilities playerData) {
		if (player.world.isRemote) {// Need to check if it's clientside for the keyboard key detection
			if (Minecraft.getInstance().player == player) { // Only the local player will send the packets
				if (!player.onGround && player.fallDistance > 0) {
					if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
						if (!playerData.getIsGliding()) {
							playerData.setIsGliding(true);// Set playerData clientside
							playerData.setAerialDodgeTicks(0);
							PacketHandler.sendToServer(new CSSetGlidingPacket(true)); // Set playerData serverside
							PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 0)); // In case the player is still rotating stop it
						}
					} else { // If is no longer pressing space
						if (playerData.getIsGliding()) {
							playerData.setIsGliding(false);
							PacketHandler.sendToServer(new CSSetGlidingPacket(false));
						}
					}
				} else { // If touches the ground
					if (playerData.getIsGliding()) {
						playerData.setIsGliding(false);
						PacketHandler.sendToServer(new CSSetGlidingPacket(false));
						PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(false, 0)); // In case the player is still rotating stop it
					}
				}
			}
		}

		if (playerData.getIsGliding()) {
			int glideLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Final)-2 : playerData.getDriveFormLevel(Strings.Form_Final);//TODO eventually replace it with the skill
			float glide = DriveForm.FINAL_GLIDE[glideLevel];
			Vec3d motion = player.getMotion();
			player.setMotion(motion.x, glide, motion.z);
		}
	}
	
	@SubscribeEvent
	public void entityPickup(EntityItemPickupEvent event) {
		if(event.getPlayer().inventory.hasItemStack(new ItemStack(ModItems.synthesisBag.get()))) {
			if(event.getItem().getItem() != null && event.getItem().getItem().getItem() instanceof SynthesisItem) {
				//ItemStack stack = event.getItem().getItem();
				//System.out.println("Pickup: "+stack.getDisplayName().getFormattedText()+" x"+stack.getCount());
				//event.setCanceled(true);
				
				for (int i = 0; i < event.getPlayer().inventory.getSizeInventory(); i++) {
					ItemStack bag = event.getPlayer().inventory.getStackInSlot(i);
					if (!ItemStack.areItemStacksEqual(bag, ItemStack.EMPTY)) {
						if (bag.getItem() == ModItems.synthesisBag.get()) {
						//	System.out.println("Found bag");
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
			
			ItemStack heldOrgWeapon = null;
			ItemStack stack = null;
			
			stack = Utils.getKeybladeDamageStack(event.getSource(), player);
			
			if(stack != null) {
				float dmg = DamageCalculation.getKBStrengthDamage(player, stack);
				event.setAmount(dmg);

			} else {
				if (player.getHeldItemMainhand().getItem() instanceof IOrgWeapon) {
					heldOrgWeapon = player.getHeldItemMainhand();
				} else if(player.getHeldItemOffhand().getItem() instanceof IOrgWeapon) {
					heldOrgWeapon = player.getHeldItemOffhand();
				}
			}

			
			//if(ModCapabilities.getPlayer(player).getAlignment() != OrgMember.NONE && ModCapabilities.getPlayer(player).getAlignment() != OrgMember.ROXAS) {
			if(heldOrgWeapon != null && event.getSource().getImmediateSource() instanceof PlayerEntity) {
				float dmg = DamageCalculation.getOrgStrengthDamage(player, heldOrgWeapon);
				event.setAmount(dmg);
			}
			//}
			
			if(ModCapabilities.getPlayer(player).getActiveDriveForm().equals(Strings.Form_Anti)) {
				event.setAmount(ModCapabilities.getPlayer(player).getStrength());
			}
		}
		
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			float damage = (float) Math.round((event.getAmount() * 100 / ((100 + (playerData.getLevel() * 2)) + playerData.getDefense())));
			if(playerData.getAeroTicks() > 0) {
				playerData.remAeroTicks((int) damage * 2);
				damage -= (damage * 30 / 100);
				System.out.println(playerData.getAeroTicks());
			}
			event.setAmount(damage <= 0 ? 1 : damage);
		}
	}

	// Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if (!event.getEntityLiving().world.isRemote) {
			if (event.getSource().getTrueSource() instanceof LivingEntity) { // If attacker is a LivingEntity
				LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
				LivingEntity target = event.getEntityLiving();

				if (attacker instanceof PlayerEntity && target instanceof PlayerEntity) {
					Party p = ModCapabilities.getWorld(attacker.world).getPartyFromMember(attacker.getUniqueID());
					if (p != null && p.getMember(event.getEntityLiving().getUniqueID()) != null) {
						event.setCanceled(true);
					}
				}
			
			/*if(event.getSource().damageType.equals("player") && attacker.getHeldItemMainhand() != null && attacker.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
				event.setCanceled(true);
				event.getEntityLiving().attackEntityFrom(KeybladeDamageSource.causeKeybladeDamage(Hand.MAIN_HAND, (PlayerEntity) attacker), event.getAmount());
			}*/

				IGlobalCapabilities globalData = ModCapabilities.getGlobal(target);
				if (target instanceof PlayerEntity) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer((PlayerEntity) target);

					if (playerData.getReflectTicks() > 0) { // If is casting reflect
						if (!playerData.getReflectActive()) // If has been hit while casting reflect
							playerData.setReflectActive(true);
						event.setCanceled(true);
					}

					if (playerData.isAbilityEquipped(Strings.mpRage)) {
						playerData.addMP(event.getAmount());
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) target);
					}

					if (playerData.isAbilityEquipped(Strings.damageDrive)) {
						playerData.addDP(event.getAmount());
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) target);
					}

				}

				if (globalData != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
					PlayerEntity source = (PlayerEntity) event.getSource().getTrueSource();
					if (globalData.getStoppedTicks() > 0) {
						float dmg = event.getAmount();
						if (event.getSource().getTrueSource() instanceof PlayerEntity) {
							ItemStack stack = Utils.getKeybladeDamageStack(event.getSource(), source);
							if (stack != null) {
								dmg = DamageCalculation.getKBStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), stack);
							}
						/*if(source.getHeldItemMainhand() != null && source.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
							dmg = DamageCalculation.getKBStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), source.getHeldItemMainhand());
						} else if(source.getHeldItemOffhand() != null && source.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
							dmg = DamageCalculation.getKBStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), source.getHeldItemOffhand());
						}*/
							if (dmg == 0) {
								dmg = event.getAmount();
							}
						}
						globalData.addDamage((int) dmg);
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
		if (event.getEntity() instanceof EnderDragonEntity && worldData.getHeartlessSpawnLevel() == 0 && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_DRAGON) {
			worldData.setHeartlessSpawnLevel(1);
		}

		if (event.getEntityLiving() instanceof EnderDragonEntity) {
			LivingEntity entity = event.getEntityLiving();
			entity.world.addEntity(new ItemEntity(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), new ItemStack(ModItems.proofOfHeart.get(), 1)));
		}

		if (!event.getEntity().world.isRemote) {
			if (event.getSource().getImmediateSource() instanceof PlayerEntity || event.getSource().getTrueSource() instanceof PlayerEntity) {
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
							playerData.addHearts(20 * multiplier);
						}
					} else if (event.getEntity() instanceof EnderDragonEntity || event.getEntity() instanceof WitherEntity) {
						playerData.addHearts(1000 * multiplier);
					} else if (event.getEntity() instanceof VillagerEntity) {
						playerData.addHearts(5 * multiplier);
					} else if (event.getEntity() instanceof MonsterEntity) {
						playerData.addHearts(2 * multiplier);
					} else {
						playerData.addHearts(1 * multiplier);
					}
				}
				if(event.getEntityLiving() instanceof IKHMob) {
					IKHMob heartless = (IKHMob) event.getEntityLiving();
					if(heartless.getMobType() == MobType.HEARTLESS_EMBLEM && Utils.getKeybladeDamageStack(event.getSource(), player) != null && Utils.getKeybladeDamageStack(event.getSource(), player).getItem() instanceof KeybladeItem) {
						HeartEntity heart = new HeartEntity(event.getEntityLiving().world);
						heart.setPosition(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 1, event.getEntityLiving().getPosZ());
						event.getEntityLiving().world.addEntity(heart);
					}
				}
				
				if (event.getEntity().getClassification(false) == EntityClassification.MONSTER) {
					if(!playerData.isAbilityEquipped(Strings.zeroExp)) {
						LivingEntity mob = (LivingEntity) event.getEntity();
						
						double value = mob.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue() / 2;
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
					
					entity.world.addEntity(new MunnyEntity(event.getEntity().world, x, y, z, Utils.randomWithRange(5, 15)));
					entity.world.addEntity(new HPOrbEntity(event.getEntity().world, x, y, z, (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5)));
					entity.world.addEntity(new MPOrbEntity(event.getEntity().world, x, y, z, (int) Utils.randomWithRange(entity.getMaxHealth() / 10, entity.getMaxHealth() / 5)));
					entity.world.addEntity(new DriveOrbEntity(event.getEntity().world, x, y, z, (int) Utils.randomWithRange(entity.getMaxHealth() / 2, entity.getMaxHealth() / 1.2)));
					
					int num = Utils.randomWithRange(0, 99);

					if(num <= ModConfigs.recipeDropChance) {
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
		//newPlayerData.setMP(oldPlayerData.getMP());
		newPlayerData.setMaxMP(oldPlayerData.getMaxMP());
		newPlayerData.setDP(oldPlayerData.getDP());
		//newPlayerData.setFP(oldPlayerData.getFP());
		newPlayerData.setMaxDP(oldPlayerData.getMaxDP());
		newPlayerData.setMaxAP(oldPlayerData.getMaxAP());

		newPlayerData.setMunny(oldPlayerData.getMunny());

		newPlayerData.setMagicList(oldPlayerData.getMagicList());
		newPlayerData.setAbilityMap(oldPlayerData.getAbilityMap());
		newPlayerData.setPortalUUIDList(oldPlayerData.getPortalUUIDList());

		newPlayerData.setDriveFormMap(oldPlayerData.getDriveFormMap());

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

		nPlayer.setHealth(oldPlayerData.getMaxHP());
		nPlayer.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(oldPlayerData.getMaxHP());
		
		PacketHandler.sendTo(new SCSyncWorldCapability(ModCapabilities.getWorld(nPlayer.world)), (ServerPlayerEntity)nPlayer);
		
	}
	
	@SubscribeEvent
	public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent e) { //Carries over the world data to the new dimension
		PlayerEntity player = e.getPlayer();
		if(!player.world.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			IWorldCapabilities fromWorldData = ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getFrom()));
			IWorldCapabilities toWorldData = ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getTo()));

			toWorldData.setParties(fromWorldData.getParties());
			toWorldData.setHeartlessSpawnLevel(fromWorldData.getHeartlessSpawnLevel());
			toWorldData.setPortals(fromWorldData.getPortals());
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
			PacketHandler.sendTo(new SCSyncWorldCapability(toWorldData), (ServerPlayerEntity)player);
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
