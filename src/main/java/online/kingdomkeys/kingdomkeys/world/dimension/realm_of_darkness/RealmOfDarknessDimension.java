package online.kingdomkeys.kingdomkeys.world.dimension.realm_of_darkness;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@EventBusSubscriber
public class RealmOfDarknessDimension {
	// Event Listeners//
	@SubscribeEvent
	public static void playerTick(PlayerTickEvent event) {
		if (!event.getEntity().isCreative() && !event.getEntity().level().isClientSide()) {
			if (event.getEntity().level().dimension().location().getPath().equals("realm_of_darkness")) {
				if (event.getEntity().getY() < 0) {
					IPlayerData playerData = ModData.getPlayer(event.getEntity());
					playerData.setRespawnROD(false);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)event.getEntity());
					
					ResourceKey<Level> resourcekey = Level.OVERWORLD;
					ServerLevel serverlevel = ((ServerLevel) event.getEntity().level()).getServer().getLevel(resourcekey);
					if (serverlevel != null) {
						ServerPlayer sPlayer = (ServerPlayer) event.getEntity();
						BlockPos pos = sPlayer.getRespawnPosition() != null ? sPlayer.getRespawnPosition() : serverlevel.getSharedSpawnPos();
						event.getEntity().changeDimension(new DimensionTransition(serverlevel, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, event.getEntity().getYRot(), event.getEntity().getXRot(), entity -> {}));
					}
				}
			}
		}
	}
}
