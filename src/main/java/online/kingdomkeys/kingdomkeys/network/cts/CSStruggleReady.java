package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.StruggleHandler;

import java.util.List;

public record CSStruggleReady(String struggleName) implements Packet {

	public static final Type<CSStruggleReady> TYPE = new Type<>(KingdomKeys.rl("cs_struggle_ready"));

	public static final StreamCodec<FriendlyByteBuf, CSStruggleReady> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, CSStruggleReady::struggleName,
			CSStruggleReady::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		Struggle struggle = worldData.getStruggleFromName(struggleName);
		if (struggle == null || struggle.isInProgress())
			return;

		Struggle.Participant participant = struggle.getParticipant(player.getUUID());
		if (participant == null)
			return;

		if (!participant.isReady()) {
			if (StruggleHandler.findAnyWeaponSlot(player.getInventory()) == null) {
				PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title("kingdomkeys.struggle.no_weapon", ""))), (ServerPlayer) player);
				return;
			}
		}

		participant.setReady(!participant.isReady());
		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
