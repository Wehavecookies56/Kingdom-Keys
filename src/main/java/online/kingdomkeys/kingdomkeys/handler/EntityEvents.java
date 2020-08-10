package online.kingdomkeys.kingdomkeys.handler;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.DriveOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.HPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.MPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.MunnyEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataLoader;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetGlidingPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncExtendedWorld;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncKeybladeData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSynthesisData;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataLoader;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.world.ModDimensions;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;

	@SubscribeEvent
	public void registerDimensions(final RegisterDimensionsEvent event) {
		if (DimensionType.byName(KingdomKeys.TTDimType) == null) {
			DimensionManager.registerDimension(KingdomKeys.TTDimType, ModDimensions.traverseTownDimension.get(), null, true);
		}
		KingdomKeys.LOGGER.info("Dimensions Registered!");
	}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent e) {
		PlayerEntity player = e.getPlayer();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
		if(playerData != null) {
			if (!player.world.isRemote) { // Sync from server to client
				//System.out.println(player.world.isRemote+" : "+ModCapabilities.get(player).getAbilitiesMap().get("kingdomkeys:scan")[0]);
				//LinkedHashMap<String, int[]> map = ModCapabilities.get(player).getAbilitiesMap();
				//System.out.println(map.values());
				player.setHealth(playerData.getMaxHP());
				player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
				
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				PacketHandler.sendTo(new SCSyncExtendedWorld(worldData), (ServerPlayerEntity) player);

				PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), (ServerPlayerEntity) player);
				PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), (ServerPlayerEntity)player);
				PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), (ServerPlayerEntity)player);
			}
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		//IWorldCapabilities worldData = ModCapabilities.getWorld(event.player.world);
		//System.out.println(event.player.world.isRemote);
		/*for(Party p : worldData.getParties()) {
			System.out.println(event.player.world.isRemote+ ": "+p.getName());
		}*/
		//ExtendedWorldData worldData = ExtendedWorldData.get(event.player.world);
		//System.out.println(event.player.getDisplayName().getFormattedText()+" "+worldData.getParties());
		/*if(!event.player.world.isRemote) {
			PacketHandler.sendTo(new SCSyncExtendedWorld(worldData), (ServerPlayerEntity)event.player);
		}*/
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);
		//playerData.setPartiesInvited(new ArrayList<String>());
	//	System.out.println(playerData);
		//System.out.println(event.player.world.isRemote);
		if (playerData != null) {
			//System.out.println(event.player.world.isRemote+" "+playerData.getPartiesInvited());
			if(!event.player.world.isRemote && event.player.ticksExisted == 5) {
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)event.player);
			}
			
			if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
				if (playerData.getFP() > 0) {
					playerData.setFP(playerData.getFP() - 0.3);
				} else {
					playerData.setActiveDriveForm("");
					event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					if(!event.player.world.isRemote) {
						PacketHandler.syncToAllAround(event.player, playerData);
					}
				}
			} else if (!playerData.getActiveDriveForm().equals("")) {
				ModDriveForms.registry.getValue(new ResourceLocation(playerData.getActiveDriveForm())).updateDrive(event.player);
			}
		
			// MP Recharge system
			if (playerData.getRecharge()) {
				if (playerData.getMP() >= playerData.getMaxMP()) { //Has recharged fully
					playerData.setRecharge(false);
					playerData.setMP(playerData.getMaxMP());
				} else { //Still recharging
					if (event.player.ticksExisted % 5 == 0)
						playerData.addMP(playerData.getMaxMP()/50);
				}
				
				//PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);

			} else { // Not on recharge
				if (playerData.getMP() <= 0) {
					playerData.setRecharge(true);
					if(!event.player.world.isRemote) {
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) event.player);
					}
				}
			}
		}

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
				if(playerData.getActiveDriveForm().equals(Strings.Form_Master) || playerData.getActiveDriveForm().equals("") && (playerData.getDriveFormMap().containsKey(Strings.Form_Master) && playerData.getDriveFormLevel(Strings.Form_Master) >= 3 && playerData.getEquippedAbilityLevel(Strings.aerialDodge) != null && playerData.getEquippedAbilityLevel(Strings.aerialDodge)[1] > 0)) {
					handleAerialDodge(player, playerData);
				}
				if(playerData.getActiveDriveForm().equals(Strings.Form_Final) || playerData.getActiveDriveForm().equals("") && (playerData.getDriveFormMap().containsKey(Strings.Form_Final) && playerData.getDriveFormLevel(Strings.Form_Final) >= 3 && playerData.getEquippedAbilityLevel(Strings.glide) != null && playerData.getEquippedAbilityLevel(Strings.glide)[1] > 0)) {
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
            			int jumpLevel = playerData.getActiveDriveForm().equals("") ? playerData.getDriveFormLevel(Strings.Form_Valor)-2 : playerData.getDriveFormLevel(Strings.Form_Valor);//TODO eventually replace it with the skill
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

		if(playerData.getActiveDriveForm().equals("")
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
							int jumpLevel = playerData.getActiveDriveForm().equals("") ? playerData.getDriveFormLevel(Strings.Form_Master)-2 : playerData.getDriveFormLevel(Strings.Form_Master);//TODO eventually replace it with the skill
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
			int glideLevel = playerData.getActiveDriveForm().equals("") ? playerData.getDriveFormLevel(Strings.Form_Final)-2 : playerData.getDriveFormLevel(Strings.Form_Final);//TODO eventually replace it with the skill
			float glide = DriveForm.FINAL_GLIDE[glideLevel];
			Vec3d motion = player.getMotion();
			player.setMotion(motion.x, glide, motion.z);
		}
	}
	
	@SubscribeEvent
	public void hitEntity(LivingHurtEvent event) {
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			KeybladeItem heldKeyblade = null;
			ItemStack heldOrgWeapon = null;
			ItemStack stack = null;
			
			if (player.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
				heldKeyblade = (KeybladeItem) player.getHeldItemMainhand().getItem();
				stack = player.getHeldItemMainhand();
			} else if(player.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
				heldKeyblade = (KeybladeItem) player.getHeldItemOffhand().getItem();
				stack = player.getHeldItemOffhand();
			}
			
			//System.out.println(event.getSource().getImmediateSource());
			if(heldKeyblade != null && event.getSource().getImmediateSource() instanceof PlayerEntity) {
				float dmg = DamageCalculation.getKBStrengthDamage(player, stack);
				event.setAmount(dmg);
			}
			
			if (player.getHeldItemMainhand().getItem() instanceof IOrgWeapon) {
				heldOrgWeapon = player.getHeldItemMainhand();
			} else if(player.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
				heldOrgWeapon = player.getHeldItemOffhand();
			}
			
			if(heldKeyblade == null && heldOrgWeapon != null && event.getSource().getImmediateSource() instanceof PlayerEntity) {
				float dmg = DamageCalculation.getOrgStrengthDamage(player, heldOrgWeapon);
				event.setAmount(dmg);
			}
		}
	}

	// Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if (event.getSource().getTrueSource() instanceof LivingEntity) { // If attacker is a LivingEntity
			//LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
			LivingEntity target = event.getEntityLiving();
			
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(target);
			if (target instanceof PlayerEntity) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer((PlayerEntity) target);

				if (playerData.getReflectTicks() > 0) { // If is casting reflect
					if (!playerData.getReflectActive()) // If has been hit while casting reflect
						playerData.setReflectActive(true);
					event.setCanceled(true);
				}
			}

			if (globalData != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity source = (PlayerEntity) event.getSource().getTrueSource();
				if (globalData.getStoppedTicks() > 0) {
					float dmg = event.getAmount();
					System.out.println(event.getSource());
					if (event.getSource().getTrueSource() instanceof PlayerEntity) {
						if(source.getHeldItemMainhand() != null && source.getHeldItemMainhand().getItem() instanceof KeybladeItem) {
							dmg = DamageCalculation.getKBStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), source.getHeldItemMainhand());
						} else if(source.getHeldItemOffhand() != null && source.getHeldItemOffhand().getItem() instanceof KeybladeItem) {
							dmg = DamageCalculation.getKBStrengthDamage((PlayerEntity) event.getSource().getTrueSource(), source.getHeldItemOffhand());
						}
						if(dmg == 0) {
							dmg = event.getAmount();
						}
						//System.out.println(dmg);
					}
					globalData.addDamage((int) dmg);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		/*
		 * if (event.getEntity() instanceof EntityDragon) {
		 * WorldSavedDataKingdomKeys.get(DimensionManager.getWorld(DimensionType.
		 * OVERWORLD.getId())).setSpawnHeartless(true); }
		 */

		if (!event.getEntity().world.isRemote) {
			if (event.getSource().getImmediateSource() instanceof PlayerEntity || event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

				if (event.getEntity() instanceof MonsterEntity) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

					MonsterEntity mob = (MonsterEntity) event.getEntity();

					double value = mob.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue() / 2;
					double exp = Utils.randomWithRange(value * 0.8, value * 1.8);
					playerData.addExperience(player, (int)exp /* * MainConfig.entities.xpMultiplier */);
										
					if (event.getEntity() instanceof WitherEntity) {
						playerData.addExperience(player, 1500);
					}
					
					Entity entity = event.getEntity();
					double x = entity.getPosX();
					double y = entity.getPosY();
					double z = entity.getPosZ();
					entity.world.addEntity(new MunnyEntity(event.getEntity().world, x, y, z, 1000));
					entity.world.addEntity(new HPOrbEntity(event.getEntity().world, x, y, z, 10));
					entity.world.addEntity(new MPOrbEntity(event.getEntity().world, x, y, z, 10));
					entity.world.addEntity(new DriveOrbEntity(event.getEntity().world, x, y, z, 10));
					
					int num = Utils.randomWithRange(1,100);

					if(num <= 1) {
						ItemEntity ie = new ItemEntity(player.world, x, y, z, new ItemStack(ModItems.recipe.get()));
						player.world.addEntity(ie);
					}

					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
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
			newPlayerData.setConsumedAP(oldPlayerData.getConsumedAP());
			newPlayerData.setMaxAP(oldPlayerData.getMaxAP());

			newPlayerData.setMunny(oldPlayerData.getMunny());

			newPlayerData.setMagicList(oldPlayerData.getMagicList());
			newPlayerData.setAbilityMap(oldPlayerData.getAbilityMap());
			newPlayerData.setPortalList(oldPlayerData.getPortalList());

			newPlayerData.setDriveFormMap(oldPlayerData.getDriveFormMap());
			
			nPlayer.setHealth(oldPlayerData.getMaxHP());
			nPlayer.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(oldPlayerData.getMaxHP());

			System.out.println(event.getPlayer().world.isRemote+": "+newPlayerData.getMP());
			// TODO sync stuff
			//if(!event.getPlayer().world.isRemote) {
			//	PacketHandler.sendTo(new SCSyncCapabilityPacket(newPlayerData), (ServerPlayerEntity) nPlayer);

				/*PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.get(event.getPlayer())), (ServerPlayerEntity)event.getPlayer());

				PacketHandler.syncToAllAround(nPlayer, ModCapabilities.get(nPlayer));*/
			//}

		}
	}
	
	@SubscribeEvent
	public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent e) {
		PlayerEntity player = e.getPlayer();
		if(!player.world.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			IWorldCapabilities fromWorldData = ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getFrom()));
			IWorldCapabilities toWorldData = ModCapabilities.getWorld(e.getPlayer().getServer().getWorld(e.getTo()));

			toWorldData.setParties(fromWorldData.getParties());
			toWorldData.setHeartlessSpawn(fromWorldData.getHeartlessSpawn());
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
			PacketHandler.sendTo(new SCSyncExtendedWorld(toWorldData), (ServerPlayerEntity)player);
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
