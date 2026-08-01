package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.network.stc.*;

@EventBusSubscriber()
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
		client(SCOpenStruggleMenu.TYPE, SCOpenStruggleMenu.STREAM_CODEC);
		client(SCCloseScreen.TYPE, SCCloseScreen.STREAM_CODEC);
		client(SCOpenEquipmentScreen.TYPE, SCOpenEquipmentScreen.STREAM_CODEC);
		client(SCOpenMaterialsScreen.TYPE, SCOpenMaterialsScreen.STREAM_CODEC);
		client(SCOpenCheckScreen.TYPE, SCOpenCheckScreen.STREAM_CODEC);
        client(SCOpenSellScreen.TYPE, SCOpenSellScreen.STREAM_CODEC);
		client(SCOpenSavePointScreen.TYPE, SCOpenSavePointScreen.STREAM_CODEC);
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
		client(SCSyncLevelingData.TYPE, SCSyncLevelingData.STREAM_CODEC);
		client(SCSyncLimitData.TYPE, SCSyncLimitData.STREAM_CODEC);
		client(SCSyncShotlockData.TYPE, SCSyncShotlockData.STREAM_CODEC);
		client(SCSyncMagicData.TYPE, SCSyncMagicData.STREAM_CODEC);
		client(SCSyncMoogleNames.TYPE, SCSyncMoogleNames.STREAM_CODEC);
		client(SCSyncOrganizationData.TYPE, SCSyncOrganizationData.STREAM_CODEC);
		client(SCSyncOrgPortalPacket.TYPE, SCSyncOrgPortalPacket.STREAM_CODEC);
		client(SCSyncOrbStats.TYPE, SCSyncOrbStats.STREAM_CODEC);
		client(SCSyncPlayerData.TYPE, SCSyncPlayerData.STREAM_CODEC);
		client(SCSyncShopData.TYPE, SCSyncShopData.STREAM_CODEC);
        client(SCSyncSellData.TYPE, SCSyncSellData.STREAM_CODEC);
		client(SCSyncSavePointData.TYPE, SCSyncSavePointData.STREAM_CODEC);
		client(SCSyncMeldingData.TYPE, SCSyncMeldingData.STREAM_CODEC);
		client(SCSyncSynthesisData.TYPE, SCSyncSynthesisData.STREAM_CODEC);
		client(SCSyncWorldData.TYPE, SCSyncWorldData.STREAM_CODEC);
		client(SCUpdateCORooms.TYPE, SCUpdateCORooms.STREAM_CODEC);
		client(SCUpdateSavePoints.TYPE, SCUpdateSavePoints.STREAM_CODEC);
		client(SCSendPlayerDataToClient.TYPE, SCSendPlayerDataToClient.STREAM_CODEC);
		client(SCOpenMenu.TYPE, SCOpenMenu.STREAM_CODEC);
		client(SCSyncJsonRegistry.TYPE, SCSyncJsonRegistry.STREAM_CODEC);
		client(SCZeroGravityPacket.TYPE, SCZeroGravityPacket.STREAM_CODEC);
		client(SCShowRareMeld.TYPE, SCShowRareMeld.STREAM_CODEC);
		client(SCOpenCardPack.TYPE, SCOpenCardPack.STREAM_CODEC);
		client(SCDisplayGivenItems.TYPE, SCDisplayGivenItems.STREAM_CODEC);
		client(SCOpenCardRoulette.TYPE, SCOpenCardRoulette.STREAM_CODEC);
		client(SCShotlockMinigameState.TYPE, SCShotlockMinigameState.STREAM_CODEC);

		server(CSAntiPointsPacket.TYPE, CSAntiPointsPacket.STREAM_CODEC);
		server(CSAttackOffhandPacket.TYPE, CSAttackOffhandPacket.STREAM_CODEC);
		server(CSChangeStyle.TYPE, CSChangeStyle.STREAM_CODEC);
		server(CSCloseMoogleGUI.TYPE, CSCloseMoogleGUI.STREAM_CODEC);
		server(CSCreateSavePoint.TYPE, CSCreateSavePoint.STREAM_CODEC);
		server(CSDepositMaterials.TYPE, CSDepositMaterials.STREAM_CODEC);
		server(CSEquipAccessories.TYPE, CSEquipAccessories.STREAM_CODEC);
		server(CSEquipArmor.TYPE, CSEquipArmor.STREAM_CODEC);
		server(CSEquipMagic.TYPE, CSEquipMagic.STREAM_CODEC);
		server(CSEquipItems.TYPE, CSEquipItems.STREAM_CODEC);
		server(CSEquipKeychain.TYPE, CSEquipKeychain.STREAM_CODEC);
		server(CSEquipShotlock.TYPE, CSEquipShotlock.STREAM_CODEC);
		server(CSEquipShoulderArmor.TYPE, CSEquipShoulderArmor.STREAM_CODEC);
		server(CSGenerateRoom.TYPE, CSGenerateRoom.STREAM_CODEC);
		server(CSGiveUpKO.TYPE, CSGiveUpKO.STREAM_CODEC);
		server(CSToggleFlightModePacket.TYPE, CSToggleFlightModePacket.STREAM_CODEC);
		server(CSLevelUpKeybladePacket.TYPE, CSLevelUpKeybladePacket.STREAM_CODEC);
		server(CSOrgPortalTPPacket.TYPE, CSOrgPortalTPPacket.STREAM_CODEC);
		server(CSPartyAddMember.TYPE, CSPartyAddMember.STREAM_CODEC);
		server(CSPartyCreate.TYPE, CSPartyCreate.STREAM_CODEC);
		server(CSStruggleCreate.TYPE, CSStruggleCreate.STREAM_CODEC);
		server(CSStruggleJoin.TYPE, CSStruggleJoin.STREAM_CODEC);
		server(CSStruggleLeave.TYPE, CSStruggleLeave.STREAM_CODEC);
		server(CSStruggleReady.TYPE, CSStruggleReady.STREAM_CODEC);
		server(CSStruggleDelete.TYPE, CSStruggleDelete.STREAM_CODEC);
		server(CSPartyDisband.TYPE, CSPartyDisband.STREAM_CODEC);
		server(CSPartyInvite.TYPE, CSPartyInvite.STREAM_CODEC);
		server(CSPartyLeave.TYPE, CSPartyLeave.STREAM_CODEC);
		server(CSPartyPromote.TYPE, CSPartyPromote.STREAM_CODEC);
		server(CSPartySettings.TYPE, CSPartySettings.STREAM_CODEC);
		server(CSPedestalConfig.TYPE, CSPedestalConfig.STREAM_CODEC);
		if (KingdomKeys.efmLoaded) {
			server(CSPlayAnimation.TYPE, CSPlayAnimation.STREAM_CODEC);
		}
		server(CSSavePointTP.TYPE, CSSavePointTP.STREAM_CODEC);
		server(CSSetAerialDodgeTicksPacket.TYPE, CSSetAerialDodgeTicksPacket.STREAM_CODEC);
		server(CSSetAirStepPacket.TYPE, CSSetAirStepPacket.STREAM_CODEC);
		server(CSSetAlignment.TYPE, CSSetAlignment.STREAM_CODEC);
		server(CSSetChoice.TYPE, CSSetChoice.STREAM_CODEC);
		server(CSSetEquippedAbilityPacket.TYPE, CSSetEquippedAbilityPacket.STREAM_CODEC);
		server(CSSetGlidingPacket.TYPE, CSSetGlidingPacket.STREAM_CODEC);
		server(CSSetNotifColor.TYPE, CSSetNotifColor.STREAM_CODEC);
		server(CSSetCrown.TYPE, CSSetCrown.STREAM_CODEC);
		server(CSTakeOverflowItem.TYPE, CSTakeOverflowItem.STREAM_CODEC);
		server(CSSetCrownOffset.TYPE, CSSetCrownOffset.STREAM_CODEC);
		server(CSSetOrgPortalName.TYPE, CSSetOrgPortalName.STREAM_CODEC);
		server(CSSetShortcutPacket.TYPE, CSSetShortcutPacket.STREAM_CODEC);
		server(CSShopBuy.TYPE, CSShopBuy.STREAM_CODEC);
        server(CSShopSell.TYPE, CSShopSell.STREAM_CODEC);
		server(CSShotlockShot.TYPE, CSShotlockShot.STREAM_CODEC);
		server(CSShotlockMinigameInput.TYPE, CSShotlockMinigameInput.STREAM_CODEC);
		server(CSSpawnOrgPortalPacket.TYPE, CSSpawnOrgPortalPacket.STREAM_CODEC);
		server(CSStruggleSettings.TYPE, CSStruggleSettings.STREAM_CODEC);
		server(CSSummonArmor.TYPE, CSSummonArmor.STREAM_CODEC);
		server(CSSummonKeyblade.TYPE, CSSummonKeyblade.STREAM_CODEC);
		server(CSSyncAllClientDataPacket.TYPE, CSSyncAllClientDataPacket.STREAM_CODEC);
		server(CSSyncArmorColor.TYPE, CSSyncArmorColor.STREAM_CODEC);
		server(CSSynthesiseRecipe.TYPE, CSSynthesiseRecipe.STREAM_CODEC);
		server(CSMeldRecipe.TYPE, CSMeldRecipe.STREAM_CODEC);
		server(CSTakeMaterials.TYPE, CSTakeMaterials.STREAM_CODEC);
		server(CSTravelToSoA.TYPE, CSTravelToSoA.STREAM_CODEC);
		server(CSUnlockEquipOrgWeapon.TYPE, CSUnlockEquipOrgWeapon.STREAM_CODEC);
		server(CSUpgradeBagPacket.TYPE, CSUpgradeBagPacket.STREAM_CODEC);
		server(CSUseDriveFormPacket.TYPE, CSUseDriveFormPacket.STREAM_CODEC);
		server(CSUseItemPacket.TYPE, CSUseItemPacket.STREAM_CODEC);
		server(CSUseLimitPacket.TYPE, CSUseLimitPacket.STREAM_CODEC);
		server(CSUseMagicPacket.TYPE, CSUseMagicPacket.STREAM_CODEC);
		server(CSUseReactionCommandPacket.TYPE, CSUseReactionCommandPacket.STREAM_CODEC);
		server(CSUseShortcutPacket.TYPE, CSUseShortcutPacket.STREAM_CODEC);
		server(CSRequestPlayerDataFromServer.TYPE, CSRequestPlayerDataFromServer.STREAM_CODEC);
		server(CSOpenMenu.TYPE, CSOpenMenu.STREAM_CODEC);
		//server(CSOpenMeldingScreen.TYPE, CSOpenMeldingScreen.STREAM_CODEC);
		server(CSBuildGummiShip.TYPE, CSBuildGummiShip.STREAM_CODEC);
		server(CSEditGummiShip.TYPE, CSEditGummiShip.STREAM_CODEC);
		server(CSImportExportGummiShip.TYPE, CSImportExportGummiShip.STREAM_CODEC);
		server(CSUpgradeGummiHangarPacket.TYPE, CSUpgradeGummiHangarPacket.STREAM_CODEC);
		server(CSGummiFirePacket.TYPE, CSGummiFirePacket.STREAM_CODEC);
		server(CSMoveGummiShipPacket.TYPE, CSMoveGummiShipPacket.STREAM_CODEC);
        server(CSShowHangarLinesPacket.TYPE, CSShowHangarLinesPacket.STREAM_CODEC);
        server(CSGummiBoostPacket.TYPE, CSGummiBoostPacket.STREAM_CODEC);
        server(CSSetShotlockEnemyListPacket.TYPE, CSSetShotlockEnemyListPacket.STREAM_CODEC);
		server(CSSetHangingWallTicksPacket.TYPE, CSSetHangingWallTicksPacket.STREAM_CODEC);
		server(CSPlaySoundPacket.TYPE, CSPlaySoundPacket.STREAM_CODEC);
		server(CSSetFlowmotionPacket.TYPE, CSSetFlowmotionPacket.STREAM_CODEC);
		server(CSSetAirDashedPacket.TYPE, CSSetAirDashedPacket.STREAM_CODEC);
		server(CSSetBouncedPacket.TYPE, CSSetBouncedPacket.STREAM_CODEC);
		server(CSSwapKeyblade.TYPE, CSSwapKeyblade.STREAM_CODEC);
		server(CSConsumeCard.TYPE, CSConsumeCard.STREAM_CODEC);
		server(CSTeleport.TYPE, CSTeleport.STREAM_CODEC);
		server(CSGiveMapCard.TYPE, CSGiveMapCard.STREAM_CODEC);
	}

	private static <T extends Packet> void client(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader) {
		registrar.playToClient(type, reader, PacketHandler::handlePacket);
	}

	private static <T extends Packet> void server(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader) {
		registrar.playToServer(type, reader, PacketHandler::handlePacket);
	}

	private static <T extends Packet> void bidirectional(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> reader) {
		registrar.playBidirectional(type, reader, PacketHandler::handlePacket);
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
		Packet reply = data.reply(context);
		if (reply != null) {
			context.reply(reply);
		}
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