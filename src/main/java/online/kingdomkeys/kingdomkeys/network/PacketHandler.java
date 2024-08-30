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
		registrar.playToClient(SCOpenShortcutsCustomize.TYPE, SCOpenShortcutsCustomize.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCOpenSynthesisGui.TYPE, SCOpenSynthesisGui.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCRecalculateEyeHeight.TYPE, SCRecalculateEyeHeight.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCShowMessagesPacket.TYPE, SCShowMessagesPacket.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCShowOrgPortalGUI.TYPE, SCShowOrgPortalGUI.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCShowOverlayPacket.TYPE, SCShowOverlayPacket.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncCastleOblivionInteriorData.TYPE, SCSyncCastleOblivionInteriorData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncDimensionLists.TYPE, SCSyncDimensionLists.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncGlobalData.TYPE, SCSyncGlobalData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncKeybladeData.TYPE, SCSyncKeybladeData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncLimitData.TYPE, SCSyncLimitData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncMagicData.TYPE, SCSyncMagicData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncMoogleNames.TYPE, SCSyncMoogleNames.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncOrganizationData.TYPE, SCSyncOrganizationData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncOrgPortalPacket.TYPE, SCSyncOrgPortalPacket.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncPlayerData.TYPE, SCSyncPlayerData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncShopData.TYPE, SCSyncShopData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncSynthesisData.TYPE, SCSyncSynthesisData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCSyncWorldData.TYPE, SCSyncWorldData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCUpdateCORooms.TYPE, SCUpdateCORooms.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SCUpdateSavePoints.TYPE, SCUpdateSavePoints.STREAM_CODEC, PacketHandler::handlePacket);



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