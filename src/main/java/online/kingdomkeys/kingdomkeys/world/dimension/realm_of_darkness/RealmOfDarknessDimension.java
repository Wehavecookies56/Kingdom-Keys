package online.kingdomkeys.kingdomkeys.world.dimension.realm_of_darkness;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

@Mod.EventBusSubscriber
public class RealmOfDarknessDimension {
	// Event Listeners//
	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		if (!event.player.isCreative() && !event.player.level().isClientSide()) {
			if (event.player.level().dimension().location().getPath().equals("realm_of_darkness")) {
				if (event.player.getY() < 0) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(event.player);
					playerData.setRespawnROD(false);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)event.player);
					
					ResourceKey<Level> resourcekey = Level.OVERWORLD;
					ServerLevel serverlevel = ((ServerLevel) event.player.level()).getServer().getLevel(resourcekey);
					if (serverlevel != null) {
						ServerPlayer sPlayer = (ServerPlayer) event.player;
						BlockPos pos = sPlayer.getRespawnPosition() != null ? sPlayer.getRespawnPosition() : serverlevel.getSharedSpawnPos();
						event.player.changeDimension(serverlevel, new BaseTeleporter(pos.getX(), pos.getY(), pos.getZ()));
					}
				}
			}
		}
	}
}
