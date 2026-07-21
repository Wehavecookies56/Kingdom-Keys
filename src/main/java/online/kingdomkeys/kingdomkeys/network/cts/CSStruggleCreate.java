package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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

public record CSStruggleCreate(Struggle struggle) implements Packet {
	public static final Type<CSStruggleCreate> TYPE = new Type<>(KingdomKeys.rl("cs_struggle_create"));

	public static final StreamCodec<FriendlyByteBuf, CSStruggleCreate> STREAM_CODEC = StreamCodec.composite(
			Struggle.STREAM_CODEC, CSStruggleCreate::struggle,
			CSStruggleCreate::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());

		BlockPos boardPos = struggle.getPos();
		if (worldData.getStruggleFromBlockPos(boardPos) != null)
			return; // board already has a match
		if (worldData.getStruggleFromName(struggle.getName()) != null)
			return; // name taken

		worldData.addStruggle(struggle);
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
