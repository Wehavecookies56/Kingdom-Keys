package online.kingdomkeys.kingdomkeys.handler;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.DriveOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.HPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.MPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.MunnyEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSyncCapability;
import online.kingdomkeys.kingdomkeys.network.PacketSyncGlobalCapability;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;

	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent e) {
		PlayerEntity player = e.getPlayer();
		if(!e.getPlayer().world.isRemote) { //Sync from server to client
			PacketHandler.sendTo(new PacketSyncCapability(ModCapabilities.get(player)), (ServerPlayerEntity) player);
		}
		PacketHandler.syncToAllAround(player, ModCapabilities.get(player));
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		IPlayerCapabilities props = ModCapabilities.get(event.player);
		if (props != null) {
			//props.setMaxDP(900);
			if(props.getActiveDriveForm().equals(Strings.Form_Anti)) {
				if (props.getFP() > 0) {
					props.setFP(props.getFP() - 1);
				} else {
					props.setActiveDriveForm("");
					event.player.world.playSound(event.player, event.player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					PacketHandler.syncToAllAround(event.player, props);
				}
			} else if(!props.getActiveDriveForm().equals("")) {
				ModDriveForms.registry.getValue(new ResourceLocation(props.getActiveDriveForm())).updateDrive(event.player);
			}

			// MP Recharge system
			if (props.getRecharge()) {
				if (props.getMP() >= props.getMaxMP()) {
					props.setRecharge(false);
					props.setMP(props.getMaxMP());
					// System.out.println(props.getMP());
					if (!event.player.world.isRemote) {
						PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) event.player);
					}
				} else {
					if (event.player.ticksExisted % 5 == 0)
						props.addMP(1);
				}
			} else { // Not on recharge
				if (props.getMP() <= 0) {
					props.setRecharge(true);
					if (!event.player.world.isRemote) {
						PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) event.player);
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
		IGlobalCapabilities gProps = ModCapabilities.getGlobal(event.getEntityLiving());
		IPlayerCapabilities props = null;
		PlayerEntity player = null;
		if (event.getEntityLiving() instanceof PlayerEntity) {
			player = (PlayerEntity) event.getEntityLiving();
			props = ModCapabilities.get((PlayerEntity) event.getEntityLiving());
		}
			
		
		if (gProps != null) {
			//Stop
			if (gProps.getStoppedTicks() > 0) {
				gProps.subStoppedTicks(1);

				event.getEntityLiving().setMotion(0, 0, 0);
				event.getEntityLiving().velocityChanged = true;

				if (gProps.getStoppedTicks() <= 0) {
					gProps.setStoppedTicks(0); // Just in case it goes below (shouldn't happen)
					if (gProps.getDamage() > 0)
						event.getEntityLiving().attackEntityFrom(DamageSource.MAGIC, gProps.getDamage());
					
					if (event.getEntityLiving() instanceof ServerPlayerEntity) // Packet to unfreeze client
						PacketHandler.sendTo(new PacketSyncGlobalCapability(gProps), (ServerPlayerEntity) event.getEntityLiving());
					gProps.setDamage(0);
				}
			}
			
			//Gravity
			if (gProps.getFlatTicks() > 0) {
				gProps.subFlatTicks(1);

				event.getEntityLiving().setMotion(0, -4, 0);
				event.getEntityLiving().velocityChanged = true;

				if (gProps.getFlatTicks() <= 0) {
					gProps.setFlatTicks(0); // Just in case it goes below (shouldn't happen)
					
					if (event.getEntityLiving() instanceof LivingEntity) //This should sync the state of this entity (player or mob) to all the clients around to stop render it flat
						PacketHandler.syncToAllAround(event.getEntityLiving(), gProps);
				}
			}
		}

		if (props != null) {
			if (props.getReflectTicks() > 0) {
				props.remReflectTicks(1);

				event.getEntityLiving().setMotion(0, 0, 0);
				event.getEntityLiving().velocityChanged = true;

				//Spawn particles
				float radius = 1.5F;
				double freq = 0.4;
				double X = event.getEntityLiving().getPosX();
				double Y = event.getEntityLiving().getPosY();
				double Z = event.getEntityLiving().getPosZ();
				
				 for (double x = X - radius; x <= X + radius; x+=freq) {
	                for (double y = Y - radius; y <= Y + radius; y+=freq) {
	                    for (double z = Z - radius; z <= Z + radius; z+=freq) {
	                        if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radius * radius) {
	                        	event.getEntityLiving().world.addParticle(ParticleTypes.BUBBLE_POP, x, y+1, z, 0, 0, 0);
	                        }
	                    }
	                }
	            }
				
			} else { //When it finishes
				if(props.getReflectActive()) {//If has been hit
					//SPAWN ENTITY and apply damage
					List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(8.0D, 4.0D, 8.0D).offset(-4.0D, -1.0D, -4.0D));
			        if (!list.isEmpty()) {
			            for (int i = 0; i < list.size(); i++) {
			                Entity e = (Entity) list.get(i);
			                if (e instanceof LivingEntity) {
			                	e.attackEntityFrom(DamageSource.MAGIC, 10);
			                }
			            }
			        }
					props.setReflectActive(false); //Restart reflect
				}
			}
		}

	}

	// Prevent attack when stopped
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if (event.getSource().getTrueSource() instanceof LivingEntity) { // If attacker is a LivingEntity
			// LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
			LivingEntity target = event.getEntityLiving();
			IGlobalCapabilities gProps = ModCapabilities.getGlobal(target);
			if (target instanceof PlayerEntity) {
				IPlayerCapabilities props = ModCapabilities.get((PlayerEntity) target);

				if (props.getReflectTicks() > 0) { // If is casting reflect
					if(!props.getReflectActive()) // If has been hit while casting reflect
						props.setReflectActive(true);
					event.setCanceled(true);
				}
			}

			if (gProps != null) {
				if (gProps.getStoppedTicks() > 0) {
					float dmg = event.getAmount();
					if (event.getSource().getTrueSource() instanceof PlayerEntity) {
						dmg = DamageCalculation.getStrengthDamage((PlayerEntity) event.getSource().getTrueSource());
					}
					gProps.addDamage((int) dmg);
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
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

				if (event.getEntity() instanceof MobEntity) {
					IPlayerCapabilities props = ModCapabilities.get(player);

					MobEntity mob = (MobEntity) event.getEntity();

					props.addExperience(player, (int) ((mob.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue() / 2) /* * MainConfig.entities.xpMultiplier */));
					if (event.getEntity() instanceof WitherEntity) {
						props.addExperience(player, 1500);
					}

					PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) player);
				}
			}
			
		}
		
		Entity entity = event.getEntity();
		double x = entity.getPosX();
		double y = entity.getPosY();
		double z = entity.getPosZ();
		event.getEntity().world.addEntity(new MunnyEntity(event.getEntity().world,x,y,z,1000));
		event.getEntity().world.addEntity(new HPOrbEntity(event.getEntity().world,x,y,z,10));
		event.getEntity().world.addEntity(new MPOrbEntity(event.getEntity().world, x, y, z, 10));
		event.getEntity().world.addEntity(new DriveOrbEntity(event.getEntity().world, x, y, z, 10));
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if(event.isWasDeath()) {
			PlayerEntity oPlayer = event.getOriginal();
			PlayerEntity nPlayer = event.getPlayer();
			IPlayerCapabilities oProps = ModCapabilities.get(oPlayer);
			IPlayerCapabilities nProps = ModCapabilities.get(nPlayer);
			//TODO sync stuff
		}
	}

	// Sync drive form on Start Tracking
	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking e) {
		if (e.getTarget() instanceof PlayerEntity) {
			System.out.println(e.getTarget());
			PlayerEntity targetPlayer = (PlayerEntity) e.getTarget();
			IPlayerCapabilities props = ModCapabilities.get(targetPlayer);
			PacketHandler.syncToAllAround(targetPlayer, props);
		}
	}
	
	
}
