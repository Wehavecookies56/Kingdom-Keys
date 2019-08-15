package online.kingdomkeys.kingdomkeys.handler;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.packets.PacketHandler;
import online.kingdomkeys.kingdomkeys.packets.PacketSyncCapability;

public class EntityEvents {

	public static boolean isBoss = false;
	public static boolean isHostiles = false;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
       // ILevelCapabilities props = ModCapabilities.get(event.player);
        //System.out.println(props.getExperience());

		// event.player.setHealth(120);
		// System.out.println(event.player.getHealth());
		List<Entity> entities = event.player.world.getEntitiesWithinAABBExcludingEntity(event.player,
				event.player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
		List<Entity> bossEntities = event.player.world.getEntitiesWithinAABBExcludingEntity(event.player,
				event.player.getBoundingBox().grow(150.0D, 100.0D, 150.0D).offset(-75.0D, -50.0D, -75.0D));
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
	public void onLivingDeathEvent(LivingDeathEvent event) {
		/*if (event.getEntity() instanceof EntityDragon) {
			WorldSavedDataKingdomKeys.get(DimensionManager.getWorld(DimensionType.OVERWORLD.getId())).setSpawnHeartless(true);
		}*/
		
		if (!event.getEntity().world.isRemote) {
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

				if (event.getEntity() instanceof MobEntity) {
					ILevelCapabilities props = ModCapabilities.get(player);

					MobEntity mob = (MobEntity) event.getEntity();

					//if (!player.getCapability(ModCapabilities.ABILITIES, null).getEquippedAbility(ModAbilities.zeroEXP)) {
						//Old way
							//player.getCapability(ModCapabilities.PLAYER_STATS, null).addExperience(player, (int) ((mob.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() / 2) * MainConfig.entities.xpMultiplier));
						//New way
						props.addExperience(player,(int) ((mob.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue() / 2) /* * MainConfig.entities.xpMultiplier */));
						if (event.getEntity() instanceof WitherEntity) {
							props.addExperience(player,1500);
						}
				//	}
						
					PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) player);
				}
			}
		}
	}

}
