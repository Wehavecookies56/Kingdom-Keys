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
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellList;

import java.util.ArrayList;
import java.util.List;

public record SCSyncSellData(List<SellList> list) implements Packet {

	public static final Type<SCSyncSellData> TYPE = new Type<>(KingdomKeys.rl("sc_sync_sell_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncSellData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, SellList.STREAM_CODEC),
			SCSyncSellData::list,
			SCSyncSellData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncSellData(this);
		}
		KingdomKeys.LOGGER.info("Sell data sync complete");
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
