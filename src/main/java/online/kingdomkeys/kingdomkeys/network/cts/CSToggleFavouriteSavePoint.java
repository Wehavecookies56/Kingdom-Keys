package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.UUID;

public record CSToggleFavouriteSavePoint(UUID savePoint) implements Packet {
	public static final Type<CSToggleFavouriteSavePoint> TYPE = new Type<>(KingdomKeys.rl("cs_toggle_favourite_save_point"));

	public static final StreamCodec<FriendlyByteBuf, CSToggleFavouriteSavePoint> STREAM_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, CSToggleFavouriteSavePoint::savePoint,
			CSToggleFavouriteSavePoint::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		if (playerData == null || savePoint == null) {
			return;
		}

		playerData.toggleFavouriteSavePoint(savePoint);
		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
