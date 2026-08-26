package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCShotlockMinigameState(int minigame, int round, int totalRounds, int roundDuration, int payload, int lastResult) implements Packet {

	public static final Type<SCShotlockMinigameState> TYPE = new Type<>(KingdomKeys.rl("sc_shotlock_minigame_state"));

	public static final StreamCodec<FriendlyByteBuf, SCShotlockMinigameState> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SCShotlockMinigameState::minigame,
			ByteBufCodecs.VAR_INT, SCShotlockMinigameState::round,
			ByteBufCodecs.VAR_INT, SCShotlockMinigameState::totalRounds,
			ByteBufCodecs.VAR_INT, SCShotlockMinigameState::roundDuration,
			ByteBufCodecs.VAR_INT, SCShotlockMinigameState::payload,
			ByteBufCodecs.VAR_INT, SCShotlockMinigameState::lastResult,
			SCShotlockMinigameState::new
	);

	// True when this packet is telling the client to remove the HUD and release the player.
	public boolean isEnd() {
		return round == 0;
	}

	// True while the Shotlock barrage is still running: hold the player still, draw nothing.
	public boolean isBarrage() {
		return round < 0;
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.shotlockMinigameState(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
