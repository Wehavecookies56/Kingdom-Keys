package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

public record CSPartyCreate(Party party) implements Packet {

	public static final Type<CSPartyCreate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_party_create"));

	public static final StreamCodec<FriendlyByteBuf, CSPartyCreate> STREAM_CODEC = StreamCodec.composite(
			Party.STREAM_CODEC,
			CSPartyCreate::party,
			CSPartyCreate::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		worldData.addParty(party);
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
