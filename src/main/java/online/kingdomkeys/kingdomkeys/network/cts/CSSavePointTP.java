package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.util.UUID;
import java.util.function.Function;
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
            ITeleporter tp = new ITeleporter() {
                @Override
                public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                    player.setDeltaMovement(0, 0, 0);
                    player.connection.teleport(destination.pos().getX() + 0.5D, destination.pos().getY(), destination.pos().getZ() + 0.5D, player.getYRot(), player.getXRot());
                    return repositionEntity.apply(false);
                }
            };
            if (te != null) {
                if (((SavePointBlock)te.getBlockState().getBlock()).getType() == SavePointStorage.SavePointType.WARP) {
                    player.changeDimension(player.getServer().getLevel(destination.dimension()), tp);
                } else if (!storage.getSavePoint(message.currentSavePoint).dimension().equals(destination.dimension())) {
                    return;
                }
                tp.placeEntity(player, (ServerLevel) player.level(), player.getServer().getLevel(destination.dimension()), 0, portal -> player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
