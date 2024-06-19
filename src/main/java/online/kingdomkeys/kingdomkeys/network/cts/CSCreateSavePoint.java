package online.kingdomkeys.kingdomkeys.network.cts;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateSavePoints;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

public record CSCreateSavePoint(BlockPos tileEntity, String name, UUID owner, String ownerName, boolean global) {
    public CSCreateSavePoint(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readUtf(), buf.readUUID(), buf.readUtf(), buf.readBoolean());
    }

    public CSCreateSavePoint(SavepointTileEntity tileEntity, String name, Player player, boolean global) {
        this(tileEntity.getBlockPos(), name, player.getGameProfile().getId(), player.getGameProfile().getName(), global);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(tileEntity);
        buf.writeUtf(name);
        buf.writeUUID(owner);
        buf.writeUtf(ownerName);
        buf.writeBoolean(global);
    }

   public static void handle(CSCreateSavePoint message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();;
            Level level = player.level();
            SavePointStorage storage = SavePointStorage.getStorage(player.server);
            SavepointTileEntity te = (SavepointTileEntity) level.getBlockEntity(message.tileEntity);
            storage.addSavePoint(new SavePointStorage.SavePoint(te.getID(), te.getBlockState().getValue(SavePointBlock.TIER), message.name, te.getBlockPos(), Pair.of(message.owner, message.ownerName), level.dimension(), message.global, Instant.now()));
            if (!message.global) {
                ModCapabilities.getPlayer(player).addDiscoveredSavePoint(te.getID(), Instant.now());
            }
            MinecraftServer server = level.getServer();
            Iterable<ServerLevel> levels = server.getAllLevels();
            for (Level level1 : levels) {
                for (Player playerFromList : level1.players()) {
                    PacketHandler.sendTo(new SCUpdateSavePoints(SavePointStorage.getStorage(server).getDiscoveredSavePoints(playerFromList)), (ServerPlayer) playerFromList);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
