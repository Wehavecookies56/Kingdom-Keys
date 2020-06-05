package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.network.packet.PacketAntiPoints;
import online.kingdomkeys.kingdomkeys.network.packet.PacketAttackOffhand;
import online.kingdomkeys.kingdomkeys.network.packet.PacketOrgPortalTP;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSetAerialDodgeTicks;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSetDriveForm;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSetEquippedAbility;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSetGliding;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSpawnOrgPortal;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncAllClientData;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncCapability;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncCapabilityToAll;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncCapabilityToAllFromClient;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncGlobalCapability;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncGlobalCapabilityToAll;
import online.kingdomkeys.kingdomkeys.network.packet.PacketUpgradeSynthesisBag;
import online.kingdomkeys.kingdomkeys.network.packet.PacketUseMagic;
import online.kingdomkeys.kingdomkeys.network.packet.ShowOverlayPacket;
import online.kingdomkeys.kingdomkeys.network.packet.SyncOrgPortal;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);

	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(KingdomKeys.MODID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register() {
		int packetID = 0;
		
		//ServerToClient
		HANDLER.registerMessage(packetID++, ShowOverlayPacket.class, ShowOverlayPacket::encode, ShowOverlayPacket::decode, ShowOverlayPacket::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapability.class, PacketSyncCapability::encode, PacketSyncCapability::decode, PacketSyncCapability::handle);
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAll.class, PacketSyncCapabilityToAll::encode, PacketSyncCapabilityToAll::decode, PacketSyncCapabilityToAll::handle);
		HANDLER.registerMessage(packetID++, PacketSyncGlobalCapability.class, PacketSyncGlobalCapability::encode, PacketSyncGlobalCapability::decode, PacketSyncGlobalCapability::handle);
		HANDLER.registerMessage(packetID++, PacketSyncGlobalCapabilityToAll.class, PacketSyncGlobalCapabilityToAll::encode, PacketSyncGlobalCapabilityToAll::decode, PacketSyncGlobalCapabilityToAll::handle);
		HANDLER.registerMessage(packetID++, SyncOrgPortal.class, SyncOrgPortal::encode, SyncOrgPortal::decode, SyncOrgPortal::handle);
		
		//ClientToServer
		HANDLER.registerMessage(packetID++, PacketSyncCapabilityToAllFromClient.class, PacketSyncCapabilityToAllFromClient::encode, PacketSyncCapabilityToAllFromClient::decode, PacketSyncCapabilityToAllFromClient::handle);
		HANDLER.registerMessage(packetID++, PacketSyncAllClientData.class, PacketSyncAllClientData::encode, PacketSyncAllClientData::decode, PacketSyncAllClientData::handle);
		HANDLER.registerMessage(packetID++, PacketUseMagic.class, PacketUseMagic::encode, PacketUseMagic::decode, PacketUseMagic::handle);
		HANDLER.registerMessage(packetID++, PacketSetDriveForm.class, PacketSetDriveForm::encode, PacketSetDriveForm::decode, PacketSetDriveForm::handle);
		HANDLER.registerMessage(packetID++, PacketUpgradeSynthesisBag.class, PacketUpgradeSynthesisBag::encode, PacketUpgradeSynthesisBag::decode, PacketUpgradeSynthesisBag::handle);
		HANDLER.registerMessage(packetID++, PacketAttackOffhand.class, PacketAttackOffhand::encode, PacketAttackOffhand::decode, PacketAttackOffhand::handle);
		HANDLER.registerMessage(packetID++, PacketAntiPoints.class, PacketAntiPoints::encode, PacketAntiPoints::decode, PacketAntiPoints::handle);
		HANDLER.registerMessage(packetID++, PacketSetGliding.class, PacketSetGliding::encode, PacketSetGliding::decode, PacketSetGliding::handle);
		HANDLER.registerMessage(packetID++, PacketSetAerialDodgeTicks.class, PacketSetAerialDodgeTicks::encode, PacketSetAerialDodgeTicks::decode, PacketSetAerialDodgeTicks::handle);
		HANDLER.registerMessage(packetID++, PacketSpawnOrgPortal.class, PacketSpawnOrgPortal::encode, PacketSpawnOrgPortal::decode, PacketSpawnOrgPortal::handle);
		HANDLER.registerMessage(packetID++, PacketOrgPortalTP.class, PacketOrgPortalTP::encode, PacketOrgPortalTP::decode, PacketOrgPortalTP::handle);
		HANDLER.registerMessage(packetID++, PacketSetEquippedAbility.class, PacketSetEquippedAbility::encode, PacketSetEquippedAbility::decode, PacketSetEquippedAbility::handle);
	}

	public static <MSG> void sendToServer(MSG msg) {
		HANDLER.sendToServer(msg);
	}

	public static <MSG> void sendTo(MSG msg, ServerPlayerEntity player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	public static <MSG> void sendToAll(MSG msg, World world) {
		for(PlayerEntity player : world.getPlayers())
			HANDLER.sendTo(msg, ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void syncToAllAround(PlayerEntity player, IPlayerCapabilities props) {
		if (!player.world.isRemote) {
			for (PlayerEntity playerFromList : player.world.getPlayers()) {
				sendTo(new PacketSyncCapabilityToAll(player.getDisplayName().getString(), props), (ServerPlayerEntity) playerFromList);
			}
		}
	}
	
	public static void syncToAllAround(LivingEntity entity, IGlobalCapabilities props) {
		if (!entity.world.isRemote) {
			for (PlayerEntity playerFromList : entity.world.getPlayers()) {
				sendTo(new PacketSyncGlobalCapabilityToAll(entity.getEntityId(), props), (ServerPlayerEntity) playerFromList);
			}
		}
	}

	
}