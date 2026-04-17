package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;


public record CSSetFlowmotionPacket(boolean flowmotion) implements Packet {

	public static final Type<CSSetFlowmotionPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_flowmotion"));

	public static final StreamCodec<FriendlyByteBuf, CSSetFlowmotionPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, CSSetFlowmotionPacket::flowmotion,
			CSSetFlowmotionPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
			playerData.setFlowmotion(flowmotion);
		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
