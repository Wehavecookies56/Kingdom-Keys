package online.kingdomkeys.kingdomkeys.network.stc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SCOpenSavePointScreen(BlockPos tileEntity, Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints, boolean create) implements Packet {

    public static final Type<SCOpenSavePointScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_save_point_screen"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenSavePointScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SCOpenSavePointScreen::tileEntity,
            StreamCodecs.SAVE_POINTS,
            SCOpenSavePointScreen::savePoints,
            ByteBufCodecs.BOOL,
            SCOpenSavePointScreen::create,
            SCOpenSavePointScreen::new
    );

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
                PlayerData.get(player).addDiscoveredSavePoint(tileEntity.getID(), instant);
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

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openSavePointScreen(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
