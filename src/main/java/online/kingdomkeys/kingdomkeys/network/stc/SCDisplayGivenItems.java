package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

import java.util.List;

public record SCDisplayGivenItems(List<ItemStack> items, boolean showBig) implements Packet {

    public static final Type<SCDisplayGivenItems> TYPE = new Type<>(KingdomKeys.rl("sc_display_given_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SCDisplayGivenItems> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), SCDisplayGivenItems::items,
        ByteBufCodecs.BOOL, SCDisplayGivenItems::showBig,
        SCDisplayGivenItems::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.displayItems(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
