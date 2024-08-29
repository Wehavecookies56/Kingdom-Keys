package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.network.stc.*;

public class PacketHandler {

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(KingdomKeys.MODID);
		registrar.playToClient(SCAeroSoundPacket.TYPE, SCAeroSoundPacket.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCDeleteSavePointScreenshot.TYPE, SCDeleteSavePointScreenshot.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenAlignmentScreen.TYPE, SCOpenAlignmentScreen.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenChoiceScreen.TYPE, SCOpenChoiceScreen.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenCODoorGui.TYPE, SCOpenCODoorGui.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenEquipmentScreen.TYPE, SCOpenEquipmentScreen.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenMagicCustomize.TYPE, SCOpenMagicCustomize.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenMaterialsScreen.TYPE, SCOpenMaterialsScreen.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenSavePointScreen.TYPE, SCOpenSavePointScreen.STREAM_CODEC, PacketHandler::handlePacket);
	}

	public static void sendTo (Packet packet, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, packet);
	}

	public static void sendToAll (Packet packet) {
		PacketDistributor.sendToAllPlayers(packet);
	}

	public static void sendToServer(Packet packet) {
		PacketDistributor.sendToServer(packet);
	}

	public static <T extends Packet>void handlePacket(final T data, final IPayloadContext context) {
		context.enqueueWork(() -> data.handle(context)).exceptionally(e -> {
			KingdomKeys.LOGGER.warn("Packet \"{}\" handling failed, something is likely broken", data.type());
			return null;
		});
	}
}