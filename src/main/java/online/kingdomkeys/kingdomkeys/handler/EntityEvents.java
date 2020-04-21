package online.kingdomkeys.kingdomkeys.handler;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSyncCapability;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		IPlayerCapabilities props = ModCapabilities.get(event.player);
		// MP Recharge system
		if (props != null) {
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

		if (gProps != null && gProps.getStopped()) {
			gProps.subStoppedTicks(1);
			//event.getEntityLiving().setMotion(0, 0, 0);
			event.getEntityLiving().setPosition(event.getEntityLiving().prevPosX, event.getEntityLiving().prevPosY, event.getEntityLiving().prevPosZ);

			System.out.println(gProps.getStoppedTicks());
			if (gProps.getStoppedTicks() <= 0) {
				gProps.setStopped(false);
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
