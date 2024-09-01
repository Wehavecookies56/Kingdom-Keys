package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;

import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSPartyInvite(Party party, UUID playerUUID) implements Packet {

	public static final Type<CSPartyInvite> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_party_invite"));

	public static final StreamCodec<FriendlyByteBuf, CSPartyInvite> STREAM_CODEC = StreamCodec.composite(
			Party.STREAM_CODEC,
			CSPartyInvite::party,
			UUIDUtil.STREAM_CODEC,
			CSPartyInvite::playerUUID,
			CSPartyInvite::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();

		Player target = player.level().getPlayerByUUID(playerUUID);
		PlayerData targetPlayerData = PlayerData.get(target);
		if(!targetPlayerData.getPartiesInvited().contains(party.getName())) {
			targetPlayerData.addPartiesInvited(party.getName());

			target.sendSystemMessage(Component.translatable(ChatFormatting.YELLOW+"You got an invitation to "+party.getName()));
		}


		PacketHandler.sendTo(new SCSyncPlayerData(target), (ServerPlayer)target);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
