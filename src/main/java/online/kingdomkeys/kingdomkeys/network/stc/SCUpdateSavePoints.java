package online.kingdomkeys.kingdomkeys.network.stc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public record SCUpdateSavePoints(Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints) {

    public SCUpdateSavePoints(FriendlyByteBuf buf) {
        this(SCOpenSavePointScreen.readSavePoints(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(savePoints.size());
        savePoints.values().forEach(savePoint -> {
            buf.writeNbt(savePoint.getFirst().serializeNBT());
            buf.writeLong(savePoint.getSecond().getEpochSecond());
            buf.writeInt(savePoint.getSecond().getNano());
        });
    }

    public static void handle(SCUpdateSavePoints message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.updateSavePoints(message)));
        ctx.get().setPacketHandled(true);
    }
}
