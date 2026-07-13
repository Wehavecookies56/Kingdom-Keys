package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSGummiBoostPacket(int entityID) implements Packet {

	public static final Type<CSGummiBoostPacket> TYPE = new Type<>(KingdomKeys.rl("cs_gummi_boost"));

	public static final StreamCodec<FriendlyByteBuf, CSGummiBoostPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSGummiBoostPacket::entityID,
			CSGummiBoostPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Entity entity = player.level().getEntity(entityID);
		if (entity instanceof GummiShipEntity ship) {
			ship.boost(player);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
