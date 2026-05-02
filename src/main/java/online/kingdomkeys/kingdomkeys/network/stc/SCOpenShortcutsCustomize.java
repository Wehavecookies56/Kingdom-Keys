package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;

import java.util.LinkedHashMap;

public record SCOpenShortcutsCustomize() implements Packet {

    public static final Type<SCOpenShortcutsCustomize> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_shortcuts_customize"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenShortcutsCustomize> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new SCOpenShortcutsCustomize());

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openShortcutsCustomize();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
