package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSGiveMapCard(ItemStack item) implements Packet {

    public static final Type<CSGiveMapCard> TYPE = new Type<>(KingdomKeys.rl("cs_give_map_card"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CSGiveMapCard> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            CSGiveMapCard::item,
            CSGiveMapCard::new
    );

    @Override
    public void handle(IPayloadContext context) {
        //at least enforce it has to be a map card to limit how exploitable this is
        if (item.getItem() instanceof MapCardItem) {
            Utils.giveItems((ServerPlayer) context.player(), true, item);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
