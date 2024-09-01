package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSPartyLeave(Party party, UUID playerUUID) implements Packet {

	public static final Type<CSPartyLeave> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_party_leave"));

	public static final StreamCodec<FriendlyByteBuf, CSPartyLeave> STREAM_CODEC = StreamCodec.composite(
			Party.STREAM_CODEC,
			CSPartyLeave::party,
			UUIDUtil.STREAM_CODEC,
			CSPartyLeave::playerUUID,
			CSPartyLeave::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		Party p = worldData.getPartyFromName(party().getName());
		p.removeMember(playerUUID);

		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
