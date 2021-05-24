package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSEquipItems {

    int slotToEquipTo;
    int slotToEquipFrom;

    public CSEquipItems() {}

    public CSEquipItems(int slotToEquipTo, int slotToEquipFrom) {
        this.slotToEquipTo = slotToEquipTo;
        this.slotToEquipFrom = slotToEquipFrom;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(slotToEquipTo);
        buffer.writeInt(slotToEquipFrom);
    }

    public static CSEquipItems decode(PacketBuffer buffer) {
        CSEquipItems msg = new CSEquipItems();
        msg.slotToEquipTo = buffer.readInt();
        msg.slotToEquipFrom = buffer.readInt();
        return msg;
    }

    public static void handle(CSEquipItems message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            ItemStack stackToEquip = player.inventory.getStackInSlot(message.slotToEquipFrom);
            ItemStack stackPreviouslyEquipped = playerData.equipItem(message.slotToEquipTo, stackToEquip);
            player.inventory.setInventorySlotContents(message.slotToEquipFrom, stackPreviouslyEquipped);
            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayerEntity) player);
        });
        ctx.get().setPacketHandled(true);
    }

}
