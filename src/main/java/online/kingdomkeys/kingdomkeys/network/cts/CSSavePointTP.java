package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.util.UUID;
import java.util.function.Supplier;

public record CSSavePointTP(UUID currentSavePoint, UUID destinationSavePoint) {
    public CSSavePointTP(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(currentSavePoint);
        buf.writeUUID(destinationSavePoint);
    }

    public static void handle(CSSavePointTP message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            SavePointStorage storage = SavePointStorage.getStorage(player.getServer());
            SavepointTileEntity te = (SavepointTileEntity) player.level().getBlockEntity(storage.getSavePoint(message.currentSavePoint).pos());
            SavePointStorage.SavePoint destination = storage.getSavePoint(message.destinationSavePoint);
            if (te != null) {
                if (((SavePointBlock)te.getBlockState().getBlock()).getType() == SavePointStorage.SavePointType.WARP) {
                    player.changeDimension(player.getServer().getLevel(destination.dimension()));
                } else if (!storage.getSavePoint(message.currentSavePoint).dimension().equals(destination.dimension())) {
                    return;
                }
                player.setDeltaMovement(0, 0, 0);
                player.connection.teleport(destination.pos().getX() + 0.5D, destination.pos().getY(), destination.pos().getZ() + 0.5D, player.getYRot(), player.getXRot());

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
