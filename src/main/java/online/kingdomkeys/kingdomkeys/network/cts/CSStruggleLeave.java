package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

public record CSStruggleLeave(String struggleName) implements Packet {
	public static final Type<CSStruggleLeave> TYPE = new Type<>(KingdomKeys.rl("cs_struggle_leave"));

	public static final StreamCodec<FriendlyByteBuf, CSStruggleLeave> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, CSStruggleLeave::struggleName,
			CSStruggleLeave::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		Struggle struggle = worldData.getStruggleFromName(struggleName);
		if (struggle == null)
			return;

		worldData.removeStruggleParticipant(struggle, player.getUUID());
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
