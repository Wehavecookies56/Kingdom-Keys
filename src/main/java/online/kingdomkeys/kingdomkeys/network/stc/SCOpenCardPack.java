package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion.CardPackScreen;
import online.kingdomkeys.kingdomkeys.network.Packet;

import java.util.List;

public record SCOpenCardPack(List<ResourceLocation> cards) implements Packet {

	public static final Type<SCOpenCardPack> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_card_pack"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenCardPack> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), SCOpenCardPack::cards,
			SCOpenCardPack::new);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			context.enqueueWork(() -> Minecraft.getInstance().setScreen(new CardPackScreen(cards)));
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}