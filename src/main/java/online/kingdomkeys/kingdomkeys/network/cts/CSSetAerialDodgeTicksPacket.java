package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record CSSetAerialDodgeTicksPacket(boolean hasJumped, int ticks) implements Packet {

	public static final Type<CSSetAerialDodgeTicksPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_aerial_dodge_ticks"));

	public static final StreamCodec<FriendlyByteBuf, CSSetAerialDodgeTicksPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			CSSetAerialDodgeTicksPacket::hasJumped,
			ByteBufCodecs.INT,
			CSSetAerialDodgeTicksPacket::ticks,
			CSSetAerialDodgeTicksPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		playerData.setHasJumpedAerialDodge(hasJumped);
		playerData.setAerialDodgeTicks(ticks);

		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
