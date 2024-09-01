package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSCloseMoogleGUI(int moogle) implements Packet {

    public static final Type<CSCloseMoogleGUI> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_close_moogle_gui"));

    public static final StreamCodec<FriendlyByteBuf, CSCloseMoogleGUI> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CSCloseMoogleGUI::moogle,
            CSCloseMoogleGUI::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Level level = context.player().level();
        MoogleEntity entity = (MoogleEntity) level.getEntity(moogle);
        if (entity != null && !entity.isDeadOrDying()) {
            entity.stopInteracting();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
