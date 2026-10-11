package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;

// Whether the pilot is pressing any movement key, only the pilot's client knows, and only then does the ship burn fuel
public record CSGummiThrottlePacket(int entityID, boolean thrusting) implements Packet {
	public static final Type<CSGummiThrottlePacket> TYPE = new Type<>(KingdomKeys.rl("cs_gummi_throttle"));

	public static final StreamCodec<FriendlyByteBuf, CSGummiThrottlePacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, CSGummiThrottlePacket::entityID,
			ByteBufCodecs.BOOL, CSGummiThrottlePacket::thrusting,
			CSGummiThrottlePacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Entity entity = player.level().getEntity(entityID);
		if (entity instanceof GummiShipEntity ship && ship.getControllingPassenger() == player) {
			ship.setPilotThrusting(thrusting);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
