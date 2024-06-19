package online.kingdomkeys.kingdomkeys.network.stc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public record SCOpenSavePointScreen(BlockPos tileEntity, Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints, boolean create) {
    public SCOpenSavePointScreen(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), readSavePoints(buf), buf.readBoolean());
    }

    public static Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> readSavePoints(FriendlyByteBuf buf) {
        Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> points = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            SavePointStorage.SavePoint savePoint = new SavePointStorage.SavePoint(buf.readNbt());
            points.put(savePoint.id(), Pair.of(savePoint, Instant.ofEpochSecond(buf.readLong(), buf.readInt())));
        }
        return points;
    }

    private static Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> getAndAddSavePoints(SavepointTileEntity tileEntity, Player player) {
        SavePointStorage storage = SavePointStorage.getStorage(player.getServer());
        Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints = storage.getDiscoveredSavePoints(player);
        if (storage.savePointRegistered(tileEntity.getID()) && !storage.getSavePoint(tileEntity.getID()).global()) {
            if (!savePoints.containsKey(tileEntity.getID())) {
                Instant instant = Instant.now();
                savePoints.put(tileEntity.getID(), Pair.of(storage.getSavePoint(tileEntity.getID()), instant));
                ModCapabilities.getPlayer(player).addDiscoveredSavePoint(tileEntity.getID(), instant);
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
        savePoints.values().forEach(savePoint -> {
            buf.writeNbt(savePoint.getFirst().serializeNBT());
            buf.writeLong(savePoint.getSecond().getEpochSecond());
            buf.writeInt(savePoint.getSecond().getNano());
        });
        buf.writeBoolean(create);
    }

    public static void handle(SCOpenSavePointScreen message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openSavePointScreen(message)));
        ctx.get().setPacketHandled(true);
    }
}
