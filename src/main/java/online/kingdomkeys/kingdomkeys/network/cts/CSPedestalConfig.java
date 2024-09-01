package online.kingdomkeys.kingdomkeys.network.cts;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;

public record CSPedestalConfig(BlockPos tileEntityPos, float rotationSpeed, float bobSpeed, float savedRotation, float savedHeight, float baseHeight, float scale, boolean pause, boolean flipped) implements Packet {

    public static final Type<CSPedestalConfig> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_pedestal_config"));

    public static final StreamCodec<FriendlyByteBuf, CSPedestalConfig> STREAM_CODEC = StreamCodecs.composite(
            BlockPos.STREAM_CODEC,
            CSPedestalConfig::tileEntityPos,
            ByteBufCodecs.FLOAT,
            CSPedestalConfig::rotationSpeed,
            ByteBufCodecs.FLOAT,
            CSPedestalConfig::bobSpeed,
            ByteBufCodecs.FLOAT,
            CSPedestalConfig::savedRotation,
            ByteBufCodecs.FLOAT,
            CSPedestalConfig::savedHeight,
            ByteBufCodecs.FLOAT,
            CSPedestalConfig::baseHeight,
            ByteBufCodecs.FLOAT,
            CSPedestalConfig::scale,
            ByteBufCodecs.BOOL,
            CSPedestalConfig::pause,
            ByteBufCodecs.BOOL,
            CSPedestalConfig::flipped,
            CSPedestalConfig::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Level world = context.player().level();
        PedestalTileEntity tileEntity = (PedestalTileEntity) world.getBlockEntity(tileEntityPos);
        tileEntity.setSpeed(rotationSpeed, bobSpeed);
        tileEntity.saveTransforms(savedRotation, savedHeight);
        tileEntity.setScale(scale);
        tileEntity.setPause(pause);
        tileEntity.setFlipped(flipped);
        tileEntity.setBaseHeight(baseHeight);
        world.sendBlockUpdated(tileEntityPos, world.getBlockState(tileEntityPos), world.getBlockState(tileEntityPos), 2);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
