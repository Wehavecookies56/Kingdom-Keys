package online.kingdomkeys.kingdomkeys.network.cts;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateSavePoints;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

public record CSCreateSavePoint(BlockPos tileEntity, String name, UUID owner, String ownerName, boolean global) implements Packet {

    public static final Type<CSCreateSavePoint> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_create_save_point"));

    public static final StreamCodec<FriendlyByteBuf, CSCreateSavePoint> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CSCreateSavePoint::tileEntity,
            ByteBufCodecs.STRING_UTF8,
            CSCreateSavePoint::name,
            UUIDUtil.STREAM_CODEC,
            CSCreateSavePoint::owner,
            ByteBufCodecs.STRING_UTF8,
            CSCreateSavePoint::ownerName,
            ByteBufCodecs.BOOL,
            CSCreateSavePoint::global,
            CSCreateSavePoint::new
    );

    public CSCreateSavePoint(SavepointTileEntity tileEntity, String name, Player player, boolean global) {
        this(tileEntity.getBlockPos(), name, player.getGameProfile().getId(), player.getGameProfile().getName(), global);
    }

    @Override
    public void handle(IPayloadContext context) {
        ServerPlayer player = context.player()
        Level level = player.level();
        SavePointStorage storage = SavePointStorage.getStorage(player.server);
        SavepointTileEntity te = (SavepointTileEntity) level.getBlockEntity(tileEntity);
        storage.addSavePoint(new SavePointStorage.SavePoint(te.getID(), te.getBlockState().getValue(SavePointBlock.TIER), name, te.getBlockPos(), Pair.of(owner, ownerName), level.dimension(), global, Instant.now()));
        if (!global) {
            PlayerData.get(player).addDiscoveredSavePoint(te.getID(), Instant.now());
        }
        MinecraftServer server = level.getServer();
        Iterable<ServerLevel> levels = server.getAllLevels();
        for (Level level1 : levels) {
            for (Player playerFromList : level1.players()) {
                PacketHandler.sendTo(new SCUpdateSavePoints(SavePointStorage.getStorage(server).getDiscoveredSavePoints(playerFromList)), (ServerPlayer) playerFromList);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
