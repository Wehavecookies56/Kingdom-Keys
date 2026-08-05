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

import java.util.ArrayList;
import java.util.List;

public record SCSyncGummiWorlds(List<String> names, List<String> data) implements Packet {

	public static final Type<SCSyncGummiWorlds> TYPE = new Type<>(KingdomKeys.rl("sc_sync_gummi_worlds"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncGummiWorlds> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), SCSyncGummiWorlds::names,
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), SCSyncGummiWorlds::data,
			SCSyncGummiWorlds::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncGummiWorlds(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
