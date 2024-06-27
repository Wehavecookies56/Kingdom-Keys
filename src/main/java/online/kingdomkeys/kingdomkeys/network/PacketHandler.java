package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.network.stc.*;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);

	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(KingdomKeys.MODID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register() {
		int packetID = 0;
		
		//ServerToClient
		HANDLER.registerMessage(packetID++, SCShowOverlayPacket.class, SCShowOverlayPacket::encode, SCShowOverlayPacket::decode, SCShowOverlayPacket::handle);
		HANDLER.registerMessage(packetID++, SCSyncCapabilityPacket.class, SCSyncCapabilityPacket::encode, SCSyncCapabilityPacket::decode, SCSyncCapabilityPacket::handle);
		HANDLER.registerMessage(packetID++, SCSyncCapabilityToAllPacket.class, SCSyncCapabilityToAllPacket::encode, SCSyncCapabilityToAllPacket::decode, SCSyncCapabilityToAllPacket::handle);
		HANDLER.registerMessage(packetID++, SCSyncGlobalCapabilityPacket.class, SCSyncGlobalCapabilityPacket::encode, SCSyncGlobalCapabilityPacket::decode, SCSyncGlobalCapabilityPacket::handle);
		HANDLER.registerMessage(packetID++, SCSyncGlobalCapabilityToAllPacket.class, SCSyncGlobalCapabilityToAllPacket::encode, SCSyncGlobalCapabilityToAllPacket::decode, SCSyncGlobalCapabilityToAllPacket::handle);
		HANDLER.registerMessage(packetID++, SCSyncOrgPortalPacket.class, SCSyncOrgPortalPacket::encode, SCSyncOrgPortalPacket::decode, SCSyncOrgPortalPacket::handle);
		HANDLER.registerMessage(packetID++, SCRecalculateEyeHeight.class, SCRecalculateEyeHeight::encode, SCRecalculateEyeHeight::decode, SCRecalculateEyeHeight::handle);
		HANDLER.registerMessage(packetID++, SCSyncWorldCapability.class, SCSyncWorldCapability::encode, SCSyncWorldCapability::decode, SCSyncWorldCapability::handle);
		HANDLER.registerMessage(packetID++, SCSyncKeybladeData.class, SCSyncKeybladeData::encode, SCSyncKeybladeData::decode, SCSyncKeybladeData::handle);
		HANDLER.registerMessage(packetID++, SCSyncOrganizationData.class, SCSyncOrganizationData::encode, SCSyncOrganizationData::decode, SCSyncOrganizationData::handle);
		HANDLER.registerMessage(packetID++, SCSyncSynthesisData.class, SCSyncSynthesisData::encode, SCSyncSynthesisData::decode, SCSyncSynthesisData::handle);
		HANDLER.registerMessage(packetID++, SCOpenSynthesisGui.class, SCOpenSynthesisGui::encode, SCOpenSynthesisGui::decode, SCOpenSynthesisGui::handle);
		HANDLER.registerMessage(packetID++, SCOpenEquipmentScreen.class, SCOpenEquipmentScreen::encode, SCOpenEquipmentScreen::decode, SCOpenEquipmentScreen::handle);
		HANDLER.registerMessage(packetID++, SCOpenMaterialsScreen.class, SCOpenMaterialsScreen::encode, SCOpenMaterialsScreen::decode, SCOpenMaterialsScreen::handle);
		HANDLER.registerMessage(packetID++, SCOpenChoiceScreen.class, SCOpenChoiceScreen::encode, SCOpenChoiceScreen::decode, SCOpenChoiceScreen::handle);
		HANDLER.registerMessage(packetID++, SCOpenAlignmentScreen.class, SCOpenAlignmentScreen::encode, SCOpenAlignmentScreen::decode, SCOpenAlignmentScreen::handle);
		HANDLER.registerMessage(packetID++, SCShowOrgPortalGUI.class, SCShowOrgPortalGUI::encode, SCShowOrgPortalGUI::decode, SCShowOrgPortalGUI::handle);
		HANDLER.registerMessage(packetID++, SCSyncMagicData.class, SCSyncMagicData::encode, SCSyncMagicData::decode, SCSyncMagicData::handle);
		HANDLER.registerMessage(packetID++, SCSyncDriveFormData.class, SCSyncDriveFormData::encode, SCSyncDriveFormData::decode, SCSyncDriveFormData::handle);
		HANDLER.registerMessage(packetID++, SCSyncShopData.class, SCSyncShopData::encode, SCSyncShopData::decode, SCSyncShopData::handle);
		HANDLER.registerMessage(packetID++, SCSyncLimitData.class, SCSyncLimitData::encode, SCSyncLimitData::decode, SCSyncLimitData::handle);
		HANDLER.registerMessage(packetID++, SCOpenMagicCustomize.class, SCOpenMagicCustomize::encode, SCOpenMagicCustomize::decode, SCOpenMagicCustomize::handle);
		HANDLER.registerMessage(packetID++, SCOpenShortcutsCustomize.class, SCOpenShortcutsCustomize::encode, SCOpenShortcutsCustomize::decode, SCOpenShortcutsCustomize::handle);
		HANDLER.registerMessage(packetID++, SCSyncDimensionLists.class, SCSyncDimensionLists::encode, SCSyncDimensionLists::decode, SCSyncDimensionLists::handle);
		HANDLER.registerMessage(packetID++, SCSyncCastleOblivionInteriorCapability.class, SCSyncCastleOblivionInteriorCapability::encode, SCSyncCastleOblivionInteriorCapability::decode, SCSyncCastleOblivionInteriorCapability::handle);
		HANDLER.registerMessage(packetID++, SCOpenCODoorGui.class, SCOpenCODoorGui::encode, SCOpenCODoorGui::decode, SCOpenCODoorGui::handle);
		HANDLER.registerMessage(packetID++, SCAeroSoundPacket.class, SCAeroSoundPacket::encode, SCAeroSoundPacket::decode, SCAeroSoundPacket::handle);
		HANDLER.registerMessage(packetID++, SCShowMessagesPacket.class, SCShowMessagesPacket::encode, SCShowMessagesPacket::decode, SCShowMessagesPacket::handle);
		HANDLER.registerMessage(packetID++, SCSyncMoogleNames.class, SCSyncMoogleNames::encode, SCSyncMoogleNames::decode, SCSyncMoogleNames::handle);
		HANDLER.registerMessage(packetID++, SCOpenSavePointScreen.class, SCOpenSavePointScreen::encode, SCOpenSavePointScreen::new, SCOpenSavePointScreen::handle);
		HANDLER.registerMessage(packetID++, SCUpdateSavePoints.class, SCUpdateSavePoints::encode, SCUpdateSavePoints::new, SCUpdateSavePoints::handle);
		HANDLER.registerMessage(packetID++, SCDeleteSavePointScreenshot.class, SCDeleteSavePointScreenshot::encode, SCDeleteSavePointScreenshot::new, SCDeleteSavePointScreenshot::handle);
		HANDLER.registerMessage(packetID++, SCUpdateCORooms.class, SCUpdateCORooms::encode, SCUpdateCORooms::new, SCUpdateCORooms::handle);

		//ClientToServer
		HANDLER.registerMessage(packetID++, CSSyncAllClientDataPacket.class, CSSyncAllClientDataPacket::encode, CSSyncAllClientDataPacket::decode, CSSyncAllClientDataPacket::handle);
		HANDLER.registerMessage(packetID++, CSUseMagicPacket.class, CSUseMagicPacket::encode, CSUseMagicPacket::decode, CSUseMagicPacket::handle);
		HANDLER.registerMessage(packetID++, CSUseDriveFormPacket.class, CSUseDriveFormPacket::encode, CSUseDriveFormPacket::decode, CSUseDriveFormPacket::handle);
		HANDLER.registerMessage(packetID++, CSUpgradeSynthesisBagPacket.class, CSUpgradeSynthesisBagPacket::encode, CSUpgradeSynthesisBagPacket::decode, CSUpgradeSynthesisBagPacket::handle);
		HANDLER.registerMessage(packetID++, CSAttackOffhandPacket.class, CSAttackOffhandPacket::encode, CSAttackOffhandPacket::decode, CSAttackOffhandPacket::handle);
		HANDLER.registerMessage(packetID++, CSAntiPointsPacket.class, CSAntiPointsPacket::encode, CSAntiPointsPacket::decode, CSAntiPointsPacket::handle);
		HANDLER.registerMessage(packetID++, CSSetGlidingPacket.class, CSSetGlidingPacket::encode, CSSetGlidingPacket::decode, CSSetGlidingPacket::handle);
		HANDLER.registerMessage(packetID++, CSSetAerialDodgeTicksPacket.class, CSSetAerialDodgeTicksPacket::encode, CSSetAerialDodgeTicksPacket::decode, CSSetAerialDodgeTicksPacket::handle);
		HANDLER.registerMessage(packetID++, CSSpawnOrgPortalPacket.class, CSSpawnOrgPortalPacket::encode, CSSpawnOrgPortalPacket::decode, CSSpawnOrgPortalPacket::handle);
		HANDLER.registerMessage(packetID++, CSOrgPortalTPPacket.class, CSOrgPortalTPPacket::encode, CSOrgPortalTPPacket::decode, CSOrgPortalTPPacket::handle);
		HANDLER.registerMessage(packetID++, CSSetEquippedAbilityPacket.class, CSSetEquippedAbilityPacket::encode, CSSetEquippedAbilityPacket::decode, CSSetEquippedAbilityPacket::handle);
		HANDLER.registerMessage(packetID++, CSPartyCreate.class, CSPartyCreate::encode, CSPartyCreate::decode, CSPartyCreate::handle);
		HANDLER.registerMessage(packetID++, CSPartyDisband.class, CSPartyDisband::encode, CSPartyDisband::decode, CSPartyDisband::handle);
		HANDLER.registerMessage(packetID++, CSPartySettings.class, CSPartySettings::encode, CSPartySettings::decode, CSPartySettings::handle);
		HANDLER.registerMessage(packetID++, CSPartyAddMember.class, CSPartyAddMember::encode, CSPartyAddMember::decode, CSPartyAddMember::handle);
		HANDLER.registerMessage(packetID++, CSPartyLeave.class, CSPartyLeave::encode, CSPartyLeave::decode, CSPartyLeave::handle);
		HANDLER.registerMessage(packetID++, CSPartyPromote.class, CSPartyPromote::encode, CSPartyPromote::decode, CSPartyPromote::handle);
		HANDLER.registerMessage(packetID++, CSPartyInvite.class, CSPartyInvite::encode, CSPartyInvite::decode, CSPartyInvite::handle);
		HANDLER.registerMessage(packetID++, CSDepositMaterials.class, CSDepositMaterials::encode, CSDepositMaterials::decode, CSDepositMaterials::handle);
		HANDLER.registerMessage(packetID++, CSTakeMaterials.class, CSTakeMaterials::encode, CSTakeMaterials::decode, CSTakeMaterials::handle);
		HANDLER.registerMessage(packetID++, CSSynthesiseRecipe.class, CSSynthesiseRecipe::encode, CSSynthesiseRecipe::decode, CSSynthesiseRecipe::handle);
		HANDLER.registerMessage(packetID++, CSLevelUpKeybladePacket.class, CSLevelUpKeybladePacket::encode, CSLevelUpKeybladePacket::decode, CSLevelUpKeybladePacket::handle);
		HANDLER.registerMessage(packetID++, CSSummonKeyblade.class, CSSummonKeyblade::encode, CSSummonKeyblade::decode, CSSummonKeyblade::handle);
		HANDLER.registerMessage(packetID++, CSSummonArmor.class, CSSummonArmor::encode, CSSummonArmor::decode, CSSummonArmor::handle);
		HANDLER.registerMessage(packetID++, CSEquipKeychain.class, CSEquipKeychain::encode, CSEquipKeychain::decode, CSEquipKeychain::handle);
		HANDLER.registerMessage(packetID++, CSPedestalConfig.class, CSPedestalConfig::encode, CSPedestalConfig::decode, CSPedestalConfig::handle);
		HANDLER.registerMessage(packetID++, CSTravelToSoA.class, CSTravelToSoA::encode, CSTravelToSoA::decode, CSTravelToSoA::handle);
		HANDLER.registerMessage(packetID++, CSSetChoice.class, CSSetChoice::encode, CSSetChoice::decode, CSSetChoice::handle);
		HANDLER.registerMessage(packetID++, CSSetAlignment.class, CSSetAlignment::encode, CSSetAlignment::decode, CSSetAlignment::handle);
		HANDLER.registerMessage(packetID++, CSUnlockEquipOrgWeapon.class, CSUnlockEquipOrgWeapon::encode, CSUnlockEquipOrgWeapon::decode, CSUnlockEquipOrgWeapon::handle);
		HANDLER.registerMessage(packetID++, CSSetOrgPortalName.class, CSSetOrgPortalName::encode, CSSetOrgPortalName::decode, CSSetOrgPortalName::handle);
		HANDLER.registerMessage(packetID++, CSUseLimitPacket.class, CSUseLimitPacket::encode, CSUseLimitPacket::decode, CSUseLimitPacket::handle);
		HANDLER.registerMessage(packetID++, CSShotlockShot.class, CSShotlockShot::encode, CSShotlockShot::decode, CSShotlockShot::handle);
		HANDLER.registerMessage(packetID++, CSEquipShotlock.class, CSEquipShotlock::encode, CSEquipShotlock::decode, CSEquipShotlock::handle);
		HANDLER.registerMessage(packetID++, CSEquipItems.class, CSEquipItems::encode, CSEquipItems::decode, CSEquipItems::handle);
		HANDLER.registerMessage(packetID++, CSEquipAccessories.class, CSEquipAccessories::encode, CSEquipAccessories::decode, CSEquipAccessories::handle);
		HANDLER.registerMessage(packetID++, CSEquipShoulderArmor.class, CSEquipShoulderArmor::encode, CSEquipShoulderArmor::decode, CSEquipShoulderArmor::handle);
		HANDLER.registerMessage(packetID++, CSEquipArmor.class, CSEquipArmor::encode, CSEquipArmor::decode, CSEquipArmor::handle);
		HANDLER.registerMessage(packetID++, CSUseItemPacket.class, CSUseItemPacket::encode, CSUseItemPacket::decode, CSUseItemPacket::handle);
		HANDLER.registerMessage(packetID++, CSUseReactionCommandPacket.class, CSUseReactionCommandPacket::encode, CSUseReactionCommandPacket::decode, CSUseReactionCommandPacket::handle);
		HANDLER.registerMessage(packetID++, CSSetShortcutPacket.class, CSSetShortcutPacket::encode, CSSetShortcutPacket::decode, CSSetShortcutPacket::handle);
		HANDLER.registerMessage(packetID++, CSUseShortcutPacket.class, CSUseShortcutPacket::encode, CSUseShortcutPacket::decode, CSUseShortcutPacket::handle);
		HANDLER.registerMessage(packetID++, CSExtendedReach.class, CSExtendedReach::encode, CSExtendedReach::decode, CSExtendedReach::handle);
		HANDLER.registerMessage(packetID++, CSShopBuy.class, CSShopBuy::encode, CSShopBuy::decode, CSShopBuy::handle);
		HANDLER.registerMessage(packetID++, CSChangeStyle.class, CSChangeStyle::encode, CSChangeStyle::decode, CSChangeStyle::handle);
		HANDLER.registerMessage(packetID++, CSSyncArmorColor.class, CSSyncArmorColor::encode, CSSyncArmorColor::decode, CSSyncArmorColor::handle);
		HANDLER.registerMessage(packetID++, CSOpenMagicCustomize.class, CSOpenMagicCustomize::encode, CSOpenMagicCustomize::decode, CSOpenMagicCustomize::handle);
		HANDLER.registerMessage(packetID++, CSOpenShortcutsCustomize.class, CSOpenShortcutsCustomize::encode, CSOpenShortcutsCustomize::decode, CSOpenShortcutsCustomize::handle);
		HANDLER.registerMessage(packetID++, CSPlayAnimation.class, CSPlayAnimation::encode, CSPlayAnimation::decode, CSPlayAnimation::handle);
		HANDLER.registerMessage(packetID++, CSSetNotifColor.class, CSSetNotifColor::encode, CSSetNotifColor::decode, CSSetNotifColor::handle);
		HANDLER.registerMessage(packetID++, CSGenerateRoom.class, CSGenerateRoom::encode, CSGenerateRoom::decode, CSGenerateRoom::handle);
		HANDLER.registerMessage(packetID++, CSStruggleSettings.class, CSStruggleSettings::encode, CSStruggleSettings::decode, CSStruggleSettings::handle);
		HANDLER.registerMessage(packetID++, CSCloseMoogleGUI.class, CSCloseMoogleGUI::encode, CSCloseMoogleGUI::decode, CSCloseMoogleGUI::handle);
		HANDLER.registerMessage(packetID++, CSSetAirStepPacket.class, CSSetAirStepPacket::encode, CSSetAirStepPacket::decode, CSSetAirStepPacket::handle);
		HANDLER.registerMessage(packetID++, CSGiveUpKO.class, CSGiveUpKO::encode, CSGiveUpKO::decode, CSGiveUpKO::handle);
		HANDLER.registerMessage(packetID++, CSCreateSavePoint.class, CSCreateSavePoint::encode, CSCreateSavePoint::new, CSCreateSavePoint::handle);
		HANDLER.registerMessage(packetID++, CSSavePointTP.class, CSSavePointTP::encode, CSSavePointTP::new, CSSavePointTP::handle);
	}

	public static <MSG> void sendToServer(MSG msg) {
		HANDLER.sendToServer(msg);
	}

	public static <MSG> void sendTo(MSG msg, ServerPlayer player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	/*public static <MSG> void sendToAll(MSG msg, PlayerEntity player) {
		MinecraftServer ms = player.getServer();
		java.util.Iterator<ServerWorld> it = ms.getWorlds().iterator();
		while(it.hasNext()) {
			ServerWorld world = it.next();
			for(PlayerEntity p : world.getPlayers()) {
				HANDLER.sendTo(msg, ((ServerPlayerEntity)p).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
			}
		}
		
	}*/

	public static <MSG> void sendToAllPlayers(MSG msg) {
		HANDLER.send(PacketDistributor.ALL.noArg(), msg);
	}

	public static void syncToAllAround(Player player, IPlayerCapabilities playerData) {
		if (!player.level().isClientSide) {
			for (Player playerFromList : player.level().players()) {
				sendTo(new SCSyncCapabilityToAllPacket(player.getDisplayName().getString(), playerData), (ServerPlayer) playerFromList);
			}
		}
	}
	
	public static void syncToAllAround(LivingEntity entity, IGlobalCapabilities globalData) {
		if (!entity.level().isClientSide) {
			for (Player playerFromList : entity.level().players()) {
				sendTo(new SCSyncGlobalCapabilityToAllPacket(entity.getId(), globalData), (ServerPlayer) playerFromList);
			}
		}
	}

	
}