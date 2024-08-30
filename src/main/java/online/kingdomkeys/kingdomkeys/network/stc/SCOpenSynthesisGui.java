package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenSynthesisGui(String inv, String name, int moogle) implements Packet {

	public static final Type<SCOpenSynthesisGui> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_synthesis_gui"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenSynthesisGui> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			SCOpenSynthesisGui::inv,
			ByteBufCodecs.STRING_UTF8,
			SCOpenSynthesisGui::name,
			ByteBufCodecs.INT,
			SCOpenSynthesisGui::moogle,
			SCOpenSynthesisGui::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.openSynthesisGui(inv, name, moogle);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

}
