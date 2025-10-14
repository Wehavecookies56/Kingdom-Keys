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

public record CSGummiFirePacket(int entityID, boolean rightClick) implements Packet {

	public static final Type<CSGummiFirePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_gummi_fire"));

	public static final StreamCodec<FriendlyByteBuf, CSGummiFirePacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSGummiFirePacket::entityID,
			ByteBufCodecs.BOOL,
			CSGummiFirePacket::rightClick,
			CSGummiFirePacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Entity entity = player.level().getEntity(entityID);
		if (entity instanceof GummiShipEntity ship) {
			ship.fire(player, rightClick);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
