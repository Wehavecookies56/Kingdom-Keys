package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;


public record CSSetAirStepPacket(BlockPos pos, float focusCost) implements Packet {

	public static final Type<CSSetAirStepPacket> TYPE = new Type<>(KingdomKeys.rl("cs_set_air_step"));

	public static final StreamCodec<FriendlyByteBuf, CSSetAirStepPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			CSSetAirStepPacket::pos,
			ByteBufCodecs.FLOAT,
			CSSetAirStepPacket::focusCost,
			CSSetAirStepPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		playerData.setAirStep(pos);
		playerData.remFocus(focusCost);
		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
