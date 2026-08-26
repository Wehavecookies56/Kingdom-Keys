package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSToggleFlightModePacket() implements Packet {

	public static final Type<CSToggleFlightModePacket> TYPE = new Type<>(KingdomKeys.rl("cs_toggle_flight_mode"));

	public static final StreamCodec<FriendlyByteBuf, CSToggleFlightModePacket> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSToggleFlightModePacket());

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.getVehicle() instanceof GummiShipEntity ship && ship.getControllingPassenger() == player) {
			ship.setFlightType3D(!ship.isFlightType3D());
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}