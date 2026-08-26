package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenMenu(CompoundTag playerData, boolean open) implements Packet {

    public static final Type<SCOpenMenu> TYPE = new Type<>(KingdomKeys.rl("sc_open_menu"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenMenu> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SCOpenMenu::playerData,
            ByteBufCodecs.BOOL,
            SCOpenMenu::open,
            SCOpenMenu::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openMenu(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
