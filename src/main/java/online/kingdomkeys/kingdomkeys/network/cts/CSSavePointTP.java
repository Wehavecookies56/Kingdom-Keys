package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.util.UUID;
import java.util.function.Function;

public record CSSavePointTP(UUID currentSavePoint, UUID destinationSavePoint) implements Packet {

    public static final Type<CSSavePointTP> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_save_point_tp"));

    public static final StreamCodec<FriendlyByteBuf, CSSavePointTP> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            CSSavePointTP::currentSavePoint,
            UUIDUtil.STREAM_CODEC,
            CSSavePointTP::destinationSavePoint,
            CSSavePointTP::new
    );

    @Override
    public void handle(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        SavePointStorage storage = SavePointStorage.getStorage(player.getServer());
        SavepointTileEntity te = (SavepointTileEntity) player.level().getBlockEntity(storage.getSavePoint(currentSavePoint).pos());
        SavePointStorage.SavePoint destination = storage.getSavePoint(destinationSavePoint);
        if (te != null) {
            if (te.getBlockState().getValue(SavePointBlock.TIER) == SavePointStorage.SavePointType.WARP) {
                player.changeDimension(new DimensionTransition(player.getServer().getLevel(destination.dimension()), new Vec3(destination.pos().getX() + 0.5D, destination.pos().getY()+0.07D, destination.pos().getZ() + 0.5D), Vec3.ZERO, player.getYRot(), player.getXRot(), pEntity -> {}));
            } else if (!storage.getSavePoint(currentSavePoint).dimension().equals(destination.dimension())) {
                return;
            }
            player.connection.teleport(destination.pos().getX() + 0.5D, destination.pos().getY()+0.07D, destination.pos().getZ() + 0.5D, player.getYRot(), player.getXRot());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
