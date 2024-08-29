package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.UUIDUtil;
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

import java.util.UUID;

public record SCDeleteSavePointScreenshot(String name, UUID uuid) implements Packet {

    public static final Type<SCDeleteSavePointScreenshot> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_delete_save_point_screenshot"));

    public static final StreamCodec<FriendlyByteBuf, SCDeleteSavePointScreenshot> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SCDeleteSavePointScreenshot::name,
            UUIDUtil.STREAM_CODEC,
            SCDeleteSavePointScreenshot::uuid,
            SCDeleteSavePointScreenshot::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.deleteScreenshot(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
