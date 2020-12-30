package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
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
		//HANDLER.registerMessage(packetID++, SCSyncSynthBagToClientPacket.class, SCSyncSynthBagToClientPacket::encode, SCSyncSynthBagToClientPacket::decode, SCSyncSynthBagToClientPacket::handle);
		HANDLER.registerMessage(packetID++, SCRecalculateEyeHeight.class, SCRecalculateEyeHeight::encode, SCRecalculateEyeHeight::decode, SCRecalculateEyeHeight::handle);
		HANDLER.registerMessage(packetID++, SCSyncExtendedWorld.class, SCSyncExtendedWorld::encode, SCSyncExtendedWorld::decode, SCSyncExtendedWorld::handle);
		HANDLER.registerMessage(packetID++, SCSyncKeybladeData.class, SCSyncKeybladeData::encode, SCSyncKeybladeData::decode, SCSyncKeybladeData::handle);
		HANDLER.registerMessage(packetID++, SCSyncOrganizationData.class, SCSyncOrganizationData::encode, SCSyncOrganizationData::decode, SCSyncOrganizationData::handle);
		HANDLER.registerMessage(packetID++, SCSyncSynthesisData.class, SCSyncSynthesisData::encode, SCSyncSynthesisData::decode, SCSyncSynthesisData::handle);
		HANDLER.registerMessage(packetID++, SCOpenSynthesisGui.class, SCOpenSynthesisGui::encode, SCOpenSynthesisGui::decode, SCOpenSynthesisGui::handle);
		HANDLER.registerMessage(packetID++, SCOpenEquipmentScreen.class, SCOpenEquipmentScreen::encode, SCOpenEquipmentScreen::decode, SCOpenEquipmentScreen::handle);
		HANDLER.registerMessage(packetID++, SCOpenMaterialsScreen.class, SCOpenMaterialsScreen::encode, SCOpenMaterialsScreen::decode, SCOpenMaterialsScreen::handle);
		HANDLER.registerMessage(packetID++, SCOpenChoiceScreen.class, SCOpenChoiceScreen::encode, SCOpenChoiceScreen::decode, SCOpenChoiceScreen::handle);
		HANDLER.registerMessage(packetID++, SCUpdateSoA.class, SCUpdateSoA::encode, SCUpdateSoA::decode, SCUpdateSoA::handle);
		HANDLER.registerMessage(packetID++, SCOpenAlignmentScreen.class, SCOpenAlignmentScreen::encode, SCOpenAlignmentScreen::decode, SCOpenAlignmentScreen::handle);

		//ClientToServer
		HANDLER.registerMessage(packetID++, CSSyncAllClientDataPacket.class, CSSyncAllClientDataPacket::encode, CSSyncAllClientDataPacket::decode, CSSyncAllClientDataPacket::handle);
		HANDLER.registerMessage(packetID++, CSUseMagicPacket.class, CSUseMagicPacket::encode, CSUseMagicPacket::decode, CSUseMagicPacket::handle);
		HANDLER.registerMessage(packetID++, CSSetDriveFormPacket.class, CSSetDriveFormPacket::encode, CSSetDriveFormPacket::decode, CSSetDriveFormPacket::handle);
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
		HANDLER.registerMessage(packetID++, CSPartyInvite.class, CSPartyInvite::encode, CSPartyInvite::decode, CSPartyInvite::handle);
		HANDLER.registerMessage(packetID++, CSDepositMaterials.class, CSDepositMaterials::encode, CSDepositMaterials::decode, CSDepositMaterials::handle);
		HANDLER.registerMessage(packetID++, CSTakeMaterials.class, CSTakeMaterials::encode, CSTakeMaterials::decode, CSTakeMaterials::handle);
		HANDLER.registerMessage(packetID++, CSSynthesiseKeyblade.class, CSSynthesiseKeyblade::encode, CSSynthesiseKeyblade::decode, CSSynthesiseKeyblade::handle);
		HANDLER.registerMessage(packetID++, CSLevelUpKeybladePacket.class, CSLevelUpKeybladePacket::encode, CSLevelUpKeybladePacket::decode, CSLevelUpKeybladePacket::handle);
		HANDLER.registerMessage(packetID++, CSSummonKeyblade.class, CSSummonKeyblade::encode, CSSummonKeyblade::decode, CSSummonKeyblade::handle);
		HANDLER.registerMessage(packetID++, CSEquipKeychain.class, CSEquipKeychain::encode, CSEquipKeychain::decode, CSEquipKeychain::handle);
		HANDLER.registerMessage(packetID++, CSPedestalConfig.class, CSPedestalConfig::encode, CSPedestalConfig::decode, CSPedestalConfig::handle);
		HANDLER.registerMessage(packetID++, CSTravelToSoA.class, CSTravelToSoA::encode, CSTravelToSoA::decode, CSTravelToSoA::handle);
		HANDLER.registerMessage(packetID++, CSSetChoice.class, CSSetChoice::encode, CSSetChoice::decode, CSSetChoice::handle);
		HANDLER.registerMessage(packetID++, CSSetAlignment.class, CSSetAlignment::encode, CSSetAlignment::decode, CSSetAlignment::handle);
		HANDLER.registerMessage(packetID++, CSUnlockEquipOrgWeapon.class, CSUnlockEquipOrgWeapon::encode, CSUnlockEquipOrgWeapon::decode, CSUnlockEquipOrgWeapon::handle);
	}

	public static <MSG> void sendToServer(MSG msg) {
		HANDLER.sendToServer(msg);
	}

	public static <MSG> void sendTo(MSG msg, ServerPlayerEntity player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	public static <MSG> void sendToAll(MSG msg, PlayerEntity player) {
		MinecraftServer ms = player.getServer();
		java.util.Iterator<ServerWorld> it = ms.getWorlds().iterator();
		while(it.hasNext()) {
			ServerWorld world = it.next();
			for(PlayerEntity p : world.getPlayers()) {
				HANDLER.sendTo(msg, ((ServerPlayerEntity)p).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
			}
		}
		
	}

	public static <MSG> void sendToAllPlayers(MSG msg) {
		HANDLER.send(PacketDistributor.ALL.noArg(), msg);
	}
	
	/*public static <MSG> void sendToAll(MSG msg, World world) {
		for(PlayerEntity player : world.getPlayers())
			HANDLER.sendTo(msg, ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}*/

	public static void syncToAllAround(PlayerEntity player, IPlayerCapabilities playerData) {
		if (!player.world.isRemote) {
			for (PlayerEntity playerFromList : player.world.getPlayers()) {
				sendTo(new SCSyncCapabilityToAllPacket(player.getDisplayName().getString(), playerData), (ServerPlayerEntity) playerFromList);
			}
		}
	}
	
	public static void syncToAllAround(LivingEntity entity, IGlobalCapabilities globalData) {
		if (!entity.world.isRemote) {
			for (PlayerEntity playerFromList : entity.world.getPlayers()) {
				sendTo(new SCSyncGlobalCapabilityToAllPacket(entity.getEntityId(), globalData), (ServerPlayerEntity) playerFromList);
			}
		}
	}

	
}