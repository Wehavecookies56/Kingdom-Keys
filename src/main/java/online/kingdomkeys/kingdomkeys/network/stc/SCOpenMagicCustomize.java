package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.LinkedHashMap;

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

public record SCOpenMagicCustomize(LinkedHashMap<String, int[]> knownMagic) implements Packet {

    public static final Type<SCOpenMagicCustomize> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_magic_customize"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenMagicCustomize> STREAM_CODEC = StreamCodec.composite(
            StreamCodecs.KNOWN_MAGIC,
            SCOpenMagicCustomize::knownMagic,
            SCOpenMagicCustomize::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openMagicCustomize(knownMagic);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
