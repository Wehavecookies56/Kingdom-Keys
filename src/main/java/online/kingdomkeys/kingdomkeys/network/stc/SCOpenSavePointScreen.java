package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public record SCOpenSavePointScreen(BlockPos tileEntity, Map<UUID, SavePointStorage.SavePoint> savePoints, boolean create) {
    public SCOpenSavePointScreen(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), readSavePoints(buf), buf.readBoolean());
    }

    public static Map<UUID, SavePointStorage.SavePoint> readSavePoints(FriendlyByteBuf buf) {
        Map<UUID, SavePointStorage.SavePoint> points = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            SavePointStorage.SavePoint savePoint = new SavePointStorage.SavePoint(buf.readNbt());
            points.put(savePoint.id(), savePoint);
        }
        return points;
    }

    private static Map<UUID, SavePointStorage.SavePoint> getAndAddSavePoints(SavepointTileEntity tileEntity, Player player) {
        SavePointStorage storage = SavePointStorage.getStorage(player.getServer());
        Map<UUID, SavePointStorage.SavePoint> savePoints = storage.getDiscoveredSavePoints(player);
        if (storage.savePointRegistered(tileEntity.getID())) {
            if (!savePoints.containsKey(tileEntity.getID())) {
                savePoints.put(tileEntity.getID(), SavePointStorage.getStorage(player.getServer()).getSavePoint(tileEntity.getID()));
            }
        }
        return savePoints;
    }

    private static boolean shouldCreate(SavepointTileEntity tileEntity, Player player) {
        SavePointStorage storage = SavePointStorage.getStorage(player.getServer());
        return !storage.savePointRegistered(tileEntity.getID());
    }

    public SCOpenSavePointScreen(SavepointTileEntity tileEntity, Player player) {
        this(tileEntity.getBlockPos(), getAndAddSavePoints(tileEntity, player), shouldCreate(tileEntity, player));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(tileEntity);
        buf.writeInt(savePoints.size());
        savePoints.values().forEach(savePoint -> buf.writeNbt(savePoint.serializeNBT()));
        buf.writeBoolean(create);
    }

    public static void handle(SCOpenSavePointScreen message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openSavePointScreen(message)));
        ctx.get().setPacketHandled(true);
    }
}
