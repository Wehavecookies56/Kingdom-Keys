package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSSetShortcutPacket(int position, int level, String magic) implements Packet {
	
	public static final Type<CSSetShortcutPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_shortcut"));

	public static final StreamCodec<FriendlyByteBuf, CSSetShortcutPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSSetShortcutPacket::position,
			ByteBufCodecs.INT,
			CSSetShortcutPacket::level,
			ByteBufCodecs.STRING_UTF8,
			CSSetShortcutPacket::magic,
			CSSetShortcutPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		if(magic.equals("")) {
			playerData.removeShortcut(position);
		} else {
			playerData.changeShortcut(position, magic, level);
		}

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}