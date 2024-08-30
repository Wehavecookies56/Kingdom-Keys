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
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;

import java.util.ArrayList;
import java.util.List;

public record SCUpdateCORooms(List<RoomData> rooms) implements Packet {

    public static final Type<SCUpdateCORooms> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_update_co_rooms"));

    public static final StreamCodec<FriendlyByteBuf, SCUpdateCORooms> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, RoomData.STREAM_CODEC),
            SCUpdateCORooms::rooms,
            SCUpdateCORooms::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.updateCORooms(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
