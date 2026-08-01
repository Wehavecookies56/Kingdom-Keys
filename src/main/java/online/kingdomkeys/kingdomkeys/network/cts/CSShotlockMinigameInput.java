package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.shotlock.minigame.ShotlockMinigameHandler;

public record CSShotlockMinigameInput(int round, int value) implements Packet {

	public static final Type<CSShotlockMinigameInput> TYPE = new Type<>(KingdomKeys.rl("cs_shotlock_minigame_input"));

	public static final StreamCodec<FriendlyByteBuf, CSShotlockMinigameInput> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, CSShotlockMinigameInput::round,
			ByteBufCodecs.VAR_INT, CSShotlockMinigameInput::value,
			CSShotlockMinigameInput::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player) {
			ShotlockMinigameHandler.onInput(player, round, value);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
