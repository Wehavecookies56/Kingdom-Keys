package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.DialogueHandler;

public record CSDialogueAnswer(int index) implements Packet {

	public static final Type<CSDialogueAnswer> TYPE = new Type<>(KingdomKeys.rl("cs_dialogue_answer"));

	public static final StreamCodec<FriendlyByteBuf, CSDialogueAnswer> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, CSDialogueAnswer::index,
			CSDialogueAnswer::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player) {
			DialogueHandler.answer(player, index);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
