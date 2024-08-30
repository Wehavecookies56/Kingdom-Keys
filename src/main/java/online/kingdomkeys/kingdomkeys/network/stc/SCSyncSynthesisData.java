package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;

public record SCSyncSynthesisData(List<Recipe> recipes) implements Packet {

	public static final Type<SCSyncSynthesisData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_synthesis_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncSynthesisData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, Recipe.STREAM_CODEC),
			SCSyncSynthesisData::recipes,
			SCSyncSynthesisData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncSynthesisData(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
