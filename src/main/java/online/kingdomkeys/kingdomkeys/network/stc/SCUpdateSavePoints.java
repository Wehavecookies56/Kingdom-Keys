package online.kingdomkeys.kingdomkeys.network.stc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record SCUpdateSavePoints(Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints) implements Packet {

    public static final Type<SCUpdateSavePoints> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_update_save_points"));

    public static final StreamCodec<FriendlyByteBuf, SCUpdateSavePoints> STREAM_CODEC = StreamCodec.composite(
            StreamCodecs.SAVE_POINTS,
            SCUpdateSavePoints::savePoints,
            SCUpdateSavePoints::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.updateSavePoints(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
