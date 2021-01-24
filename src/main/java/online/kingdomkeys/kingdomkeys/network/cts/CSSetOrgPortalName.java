package online.kingdomkeys.kingdomkeys.network.cts;


import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;

public class CSSetOrgPortalName {

	BlockPos pos;
    String name;
	
    public CSSetOrgPortalName() {}

    public CSSetOrgPortalName(BlockPos pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(name.length());
        buffer.writeString(name, name.length());
    }

    public static CSSetOrgPortalName decode(PacketBuffer buffer) {
        CSSetOrgPortalName msg = new CSSetOrgPortalName();
        msg.pos = buffer.readBlockPos();
        int len = buffer.readInt();
        msg.name = buffer.readString(len);
        return msg;
    }

    public static void handle(CSSetOrgPortalName message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if(player.world.getTileEntity(message.pos) != null && player.world.getTileEntity(message.pos) instanceof OrgPortalTileEntity) {
            	OrgPortalTileEntity te = (OrgPortalTileEntity) player.world.getTileEntity(message.pos);
            	UUID portalUUID = te.getUUID();
            	ModCapabilities.getWorld(player.world).getPortalFromUUID(portalUUID).setName(message.name);
				PacketHandler.sendTo(new SCSyncWorldCapability(ModCapabilities.getWorld(player.world)), (ServerPlayerEntity) player);            	
            }
        });
        ctx.get().setPacketHandled(true);
    }

}