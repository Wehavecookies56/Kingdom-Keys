package online.kingdomkeys.kingdomkeys.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);

	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(KingdomKeys.MODID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register() {
		int packetID = 0;
		
		//ServerToClient
		HANDLER.registerMessage(packetID++, ShowOverlayPacket.class, ShowOverlayPacket::encode, ShowOverlayPacket::decode, ShowOverlayPacket::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapability.class, PacketSyncCapability::encode, PacketSyncCapability::decode, PacketSyncCapability::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAll.class, PacketSyncCapabilityToAll::encode, PacketSyncCapabilityToAll::decode, PacketSyncCapabilityToAll::handle);
		
		
		//ClientToServer
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAllFromClient.class, PacketSyncCapabilityToAllFromClient::encode, PacketSyncCapabilityToAllFromClient::decode, PacketSyncCapabilityToAllFromClient::handle);
		HANDLER.registerMessage(packetID++, PacketSyncAllClientData.class, PacketSyncAllClientData::encode, PacketSyncAllClientData::decode, PacketSyncAllClientData::handle);
	}

	public static <MSG> void sendToServer(MSG msg) {
		HANDLER.sendToServer(msg);
	}

	public static <MSG> void sendTo(MSG msg, ServerPlayerEntity player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	public static void syncToAllAround(PlayerEntity player, ILevelCapabilities props) {
		if (!player.world.isRemote) {
			for (PlayerEntity playerFromList : player.world.getPlayers()) {
				sendTo(new PacketSyncCapabilityToAll(player.getDisplayName().getString(), props), (ServerPlayerEntity) playerFromList);
			}
		}
	}
}