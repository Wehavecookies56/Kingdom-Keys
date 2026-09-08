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

import java.util.List;

public record SCOpenDialogue(int speaker, List<String> lines, List<String> answers) implements Packet {

	public static final Type<SCOpenDialogue> TYPE = new Type<>(KingdomKeys.rl("sc_open_dialogue"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenDialogue> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SCOpenDialogue::speaker,
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SCOpenDialogue::lines,
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SCOpenDialogue::answers,
			SCOpenDialogue::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.openDialogue(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
