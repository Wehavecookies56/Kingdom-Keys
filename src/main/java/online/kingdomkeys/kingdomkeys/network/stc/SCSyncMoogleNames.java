package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.List;
import java.util.Map;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;

public record SCSyncMoogleNames(Map<ResourceLocation, List<String>> names) implements Packet {

    public static final Type<SCSyncMoogleNames> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_moogle_names"));

    public static final StreamCodec<FriendlyByteBuf, SCSyncMoogleNames> STREAM_CODEC = StreamCodec.composite(
            StreamCodecs.MOOGLE_NAMES,
            SCSyncMoogleNames::names,
            SCSyncMoogleNames::new
    );

    public SCSyncMoogleNames(NamesListRegistry names) {
        this(names.getRegistry());
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.syncMoogleNames(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
