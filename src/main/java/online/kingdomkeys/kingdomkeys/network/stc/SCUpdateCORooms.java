package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record SCUpdateCORooms(List<RoomData> rooms) {

    public SCUpdateCORooms(FriendlyByteBuf buf) {
        this(readRooms(buf));
    }

    public static List<RoomData> readRooms(FriendlyByteBuf buf) {
        List<RoomData> rooms = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            rooms.add(RoomData.deserialize(buf.readNbt()));
        }
        return rooms;
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(rooms.size());
        rooms.forEach(roomData -> buf.writeNbt(roomData.serializeNBT()));
    }
    
    public static void handle(SCUpdateCORooms message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.updateCORooms(message)));
        ctx.get().setPacketHandled(true);
    }
}
