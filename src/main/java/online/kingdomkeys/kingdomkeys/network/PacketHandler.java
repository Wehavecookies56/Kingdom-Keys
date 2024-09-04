package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.network.stc.*;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class PacketHandler {

	private static PayloadRegistrar registrar;

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		registrar = event.registrar(KingdomKeys.MODID);
		client(SCAeroSoundPacket.TYPE, SCAeroSoundPacket.STREAM_CODEC);
		client(SCDeleteSavePointScreenshot.TYPE, SCDeleteSavePointScreenshot.STREAM_CODEC);
		client(SCOpenAlignmentScreen.TYPE, SCOpenAlignmentScreen.STREAM_CODEC);
		client(SCOpenChoiceScreen.TYPE, SCOpenChoiceScreen.STREAM_CODEC);
		client(SCOpenCODoorGui.TYPE, SCOpenCODoorGui.STREAM_CODEC);
		client(SCOpenEquipmentScreen.TYPE, SCOpenEquipmentScreen.STREAM_CODEC);
		client(SCOpenMagicCustomize.TYPE, SCOpenMagicCustomize.STREAM_CODEC);
		client(SCOpenMaterialsScreen.TYPE, SCOpenMaterialsScreen.STREAM_CODEC);
		client(SCOpenSavePointScreen.TYPE, SCOpenSavePointScreen.STREAM_CODEC);
		client(SCOpenShortcutsCustomize.TYPE, SCOpenShortcutsCustomize.STREAM_CODEC);
		client(SCOpenSynthesisGui.TYPE, SCOpenSynthesisGui.STREAM_CODEC);
		client(SCRecalculateEyeHeight.TYPE, SCRecalculateEyeHeight.STREAM_CODEC);
		client(SCShowMessagesPacket.TYPE, SCShowMessagesPacket.STREAM_CODEC);
		client(SCShowOrgPortalGUI.TYPE, SCShowOrgPortalGUI.STREAM_CODEC);
		client(SCShowOverlayPacket.TYPE, SCShowOverlayPacket.STREAM_CODEC);
		client(SCSyncCastleOblivionInteriorData.TYPE, SCSyncCastleOblivionInteriorData.STREAM_CODEC);
		client(SCSyncDimensionLists.TYPE, SCSyncDimensionLists.STREAM_CODEC);
		client(SCSyncDriveFormData.TYPE, SCSyncDriveFormData.STREAM_CODEC);
		client(SCSyncGlobalData.TYPE, SCSyncGlobalData.STREAM_CODEC);
		client(SCSyncKeybladeData.TYPE, SCSyncKeybladeData.STREAM_CODEC);
		client(SCSyncLimitData.TYPE, SCSyncLimitData.STREAM_CODEC);
		client(SCSyncMagicData.TYPE, SCSyncMagicData.STREAM_CODEC);
		client(SCSyncMoogleNames.TYPE, SCSyncMoogleNames.STREAM_CODEC);
		client(SCSyncOrganizationData.TYPE, SCSyncOrganizationData.STREAM_CODEC);
		client(SCSyncOrgPortalPacket.TYPE, SCSyncOrgPortalPacket.STREAM_CODEC);
		client(SCSyncPlayerData.TYPE, SCSyncPlayerData.STREAM_CODEC);
		client(SCSyncShopData.TYPE, SCSyncShopData.STREAM_CODEC);
		client(SCSyncSynthesisData.TYPE, SCSyncSynthesisData.STREAM_CODEC);
		client(SCSyncWorldData.TYPE, SCSyncWorldData.STREAM_CODEC);
		client(SCUpdateCORooms.TYPE, SCUpdateCORooms.STREAM_CODEC);
		client(SCUpdateSavePoints.TYPE, SCUpdateSavePoints.STREAM_CODEC);

		server(CSAntiPointsPacket.TYPE, CSAntiPointsPacket.STREAM_CODEC);
		server(CSAttackOffhandPacket.TYPE, CSAttackOffhandPacket.STREAM_CODEC);
		server(CSChangeStyle.TYPE, CSChangeStyle.STREAM_CODEC);
		server(CSCloseMoogleGUI.TYPE, CSCloseMoogleGUI.STREAM_CODEC);
		server(CSCreateSavePoint.TYPE, CSCreateSavePoint.STREAM_CODEC);
		server(CSDepositMaterials.TYPE, CSDepositMaterials.STREAM_CODEC);
		server(CSEquipAccessories.TYPE, CSEquipAccessories.STREAM_CODEC);
		server(CSEquipArmor.TYPE, CSEquipArmor.STREAM_CODEC);
		server(CSEquipItems.TYPE, CSEquipItems.STREAM_CODEC);
		server(CSEquipKeychain.TYPE, CSEquipKeychain.STREAM_CODEC);
		server(CSEquipShotlock.TYPE, CSEquipShotlock.STREAM_CODEC);
		server(CSEquipShoulderArmor.TYPE, CSEquipShoulderArmor.STREAM_CODEC);
		server(CSExtendedReach.TYPE, CSExtendedReach.STREAM_CODEC);
		server(CSGenerateRoom.TYPE, CSGenerateRoom.STREAM_CODEC);
		server(CSGiveUpKO.TYPE, CSGiveUpKO.STREAM_CODEC);
		server(CSLevelUpKeybladePacket.TYPE, CSLevelUpKeybladePacket.STREAM_CODEC);
		server(CSOpenMagicCustomize.TYPE, CSOpenMagicCustomize.STREAM_CODEC);
		server(CSOpenShortcutsCustomize.TYPE, CSOpenShortcutsCustomize.STREAM_CODEC);
		server(CSOrgPortalTPPacket.TYPE, CSOrgPortalTPPacket.STREAM_CODEC);
		server(CSPartyAddMember.TYPE, CSPartyAddMember.STREAM_CODEC);
		server(CSPartyCreate.TYPE, CSPartyCreate.STREAM_CODEC);
		server(CSPartyDisband.TYPE, CSPartyDisband.STREAM_CODEC);
		server(CSPartyInvite.TYPE, CSPartyInvite.STREAM_CODEC);
		server(CSPartyLeave.TYPE, CSPartyLeave.STREAM_CODEC);
		server(CSPartyPromote.TYPE, CSPartyPromote.STREAM_CODEC);
		server(CSPartySettings.TYPE, CSPartySettings.STREAM_CODEC);
		server(CSPedestalConfig.TYPE, CSPedestalConfig.STREAM_CODEC);
		//server(CSPlayAnimation.TYPE, CSPlayAnimation.STREAM_CODEC);
		server(CSSavePointTP.TYPE, CSSavePointTP.STREAM_CODEC);
		server(CSSetAerialDodgeTicksPacket.TYPE, CSSetAerialDodgeTicksPacket.STREAM_CODEC);
		server(CSSetAirStepPacket.TYPE, CSSetAirStepPacket.STREAM_CODEC);
		server(CSSetAlignment.TYPE, CSSetAlignment.STREAM_CODEC);
		server(CSSetChoice.TYPE, CSSetChoice.STREAM_CODEC);
		server(CSSetEquippedAbilityPacket.TYPE, CSSetEquippedAbilityPacket.STREAM_CODEC);
		server(CSSetGlidingPacket.TYPE, CSSetGlidingPacket.STREAM_CODEC);
		server(CSSetNotifColor.TYPE, CSSetNotifColor.STREAM_CODEC);
		server(CSSetOrgPortalName.TYPE, CSSetOrgPortalName.STREAM_CODEC);
		server(CSSetShortcutPacket.TYPE, CSSetShortcutPacket.STREAM_CODEC);
		server(CSShopBuy.TYPE, CSShopBuy.STREAM_CODEC);
		server(CSShotlockShot.TYPE, CSShotlockShot.STREAM_CODEC);
		server(CSSpawnOrgPortalPacket.TYPE, CSSpawnOrgPortalPacket.STREAM_CODEC);
		server(CSStruggleSettings.TYPE, CSStruggleSettings.STREAM_CODEC);
		server(CSSummonArmor.TYPE, CSSummonArmor.STREAM_CODEC);
		server(CSSummonKeyblade.TYPE, CSSummonKeyblade.STREAM_CODEC);
		server(CSSyncAllClientDataPacket.TYPE, CSSyncAllClientDataPacket.STREAM_CODEC);
		server(CSSyncArmorColor.TYPE, CSSyncArmorColor.STREAM_CODEC);
		server(CSSynthesiseRecipe.TYPE, CSSynthesiseRecipe.STREAM_CODEC);
		server(CSTakeMaterials.TYPE, CSTakeMaterials.STREAM_CODEC);
		server(CSTravelToSoA.TYPE, CSTravelToSoA.STREAM_CODEC);
		server(CSUnlockEquipOrgWeapon.TYPE, CSUnlockEquipOrgWeapon.STREAM_CODEC);
		server(CSUpgradeSynthesisBagPacket.TYPE, CSUpgradeSynthesisBagPacket.STREAM_CODEC);
		server(CSUseDriveFormPacket.TYPE, CSUseDriveFormPacket.STREAM_CODEC);
		server(CSUseItemPacket.TYPE, CSUseItemPacket.STREAM_CODEC);
		server(CSUseLimitPacket.TYPE, CSUseLimitPacket.STREAM_CODEC);
		server(CSUseMagicPacket.TYPE, CSUseMagicPacket.STREAM_CODEC);
		server(CSUseReactionCommandPacket.TYPE, CSUseReactionCommandPacket.STREAM_CODEC);
		server(CSUseShortcutPacket.TYPE, CSUseShortcutPacket.STREAM_CODEC);
	}

	private static <T extends Packet> void client(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader) {
		registrar.playToClient(type, reader, PacketHandler::handlePacket);
	}

	private static <T extends Packet> void server(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader) {
		registrar.playToServer(type, reader, PacketHandler::handlePacket);
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

	public static void syncToAllAround(Player player, PlayerData playerData) {
		if (!player.level().isClientSide) {
			for (Player playerFromList : player.level().players()) {
				sendTo(new SCSyncPlayerData(player), (ServerPlayer) playerFromList);
			}
		}
	}

	public static void syncToAllAround(LivingEntity entity, GlobalData globalData) {
		if (!entity.level().isClientSide) {
			for (Player playerFromList : entity.level().players()) {
				sendTo(new SCSyncGlobalData(entity.getId(), globalData.serializeNBT(entity.level().registryAccess())), (ServerPlayer) playerFromList);
			}
		}
	}
}