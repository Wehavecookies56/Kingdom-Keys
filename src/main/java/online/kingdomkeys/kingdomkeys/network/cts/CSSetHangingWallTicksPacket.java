package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;


public record CSSetHangingWallTicksPacket(int wallGrabs, int ticks) implements Packet {

	public static final Type<CSSetHangingWallTicksPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_hanging_wall_ticks"));

	public static final StreamCodec<FriendlyByteBuf, CSSetHangingWallTicksPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSSetHangingWallTicksPacket::wallGrabs,
			ByteBufCodecs.INT,
			CSSetHangingWallTicksPacket::ticks,
			CSSetHangingWallTicksPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		if(wallGrabs > -1)
			playerData.setWallGrabs(wallGrabs);
		if(ticks > -1)
			playerData.setHangingWallTicks(ticks);
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.wall_grab.get(), SoundSource.PLAYERS);
		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
