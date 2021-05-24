package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSEquipShotlock {

	String shotlock;

    public CSEquipShotlock() {}

    public CSEquipShotlock(String shotlock) {
        this.shotlock = shotlock;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeString(shotlock,100);
    }

    public static CSEquipShotlock decode(PacketBuffer buffer) {
        CSEquipShotlock msg = new CSEquipShotlock();
        msg.shotlock = buffer.readString(100);
        return msg;
    }

    public static void handle(CSEquipShotlock message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if(playerData.getShotlockList().contains(message.shotlock) || message.shotlock.equals("")) {
            	playerData.setEquippedShotlock(message.shotlock);
            }
           /* ItemStack stackToEquip = player.inventory.getStackInSlot(message.slotToEquipFrom);
            ItemStack stackPreviouslyEquipped = playerData.equipKeychain(message.shotlock, stackToEquip);
            player.inventory.setInventorySlotContents(message.slotToEquipFrom, stackPreviouslyEquipped);*/
            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayerEntity) player);
        });
        ctx.get().setPacketHandled(true);
    }

}
