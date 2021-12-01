package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;

public class CSPedestalConfig {

    private BlockPos tileEntityPos;
    private float rotationSpeed, bobSpeed, savedRotation, savedHeight, baseHeight, scale;
    private boolean pause;

    public CSPedestalConfig() {}

    public CSPedestalConfig(BlockPos tileEntityPos, float rotationSpeed, float bobSpeed, float savedRotation, float savedHeight, float baseHeight, float scale, boolean pause) {
        this.tileEntityPos = tileEntityPos;
        this.rotationSpeed = rotationSpeed;
        this.bobSpeed = bobSpeed;
        this.savedRotation = savedRotation;
        this.savedHeight = savedHeight;
        this.baseHeight = baseHeight;
        this.scale = scale;
        this.pause = pause;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.tileEntityPos);
        buffer.writeFloat(this.rotationSpeed);
        buffer.writeFloat(this.bobSpeed);
        buffer.writeFloat(this.savedRotation);
        buffer.writeFloat(this.savedHeight);
        buffer.writeFloat(this.baseHeight);
        buffer.writeFloat(this.scale);
        buffer.writeBoolean(this.pause);
    }

    public static CSPedestalConfig decode(FriendlyByteBuf buffer) {
        CSPedestalConfig msg = new CSPedestalConfig();
        msg.tileEntityPos = buffer.readBlockPos();
        msg.rotationSpeed = buffer.readFloat();
        msg.bobSpeed = buffer.readFloat();
        msg.savedRotation = buffer.readFloat();
        msg.savedHeight = buffer.readFloat();
        msg.baseHeight = buffer.readFloat();
        msg.scale = buffer.readFloat();
        msg.pause = buffer.readBoolean();
        return msg;
    }

    public static void handle(CSPedestalConfig message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = ctx.get().getSender().level;
            PedestalTileEntity tileEntity = (PedestalTileEntity) world.getBlockEntity(message.tileEntityPos);
            tileEntity.setSpeed(message.rotationSpeed, message.bobSpeed);
            tileEntity.saveTransforms(message.savedRotation, message.savedHeight);
            tileEntity.setScale(message.scale);
            tileEntity.setPause(message.pause);
            tileEntity.setBaseHeight(message.baseHeight);
            world.sendBlockUpdated(message.tileEntityPos, world.getBlockState(message.tileEntityPos), world.getBlockState(message.tileEntityPos), 2);
        });
        ctx.get().setPacketHandled(true);
    }

}
