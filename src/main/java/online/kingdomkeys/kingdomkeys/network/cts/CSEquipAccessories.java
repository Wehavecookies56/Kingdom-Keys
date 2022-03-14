package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSEquipAccessories {

    int slotToEquipTo;
    int slotToEquipFrom;

    public CSEquipAccessories() {}

    public CSEquipAccessories(int slotToEquipTo, int slotToEquipFrom) {
        this.slotToEquipTo = slotToEquipTo;
        this.slotToEquipFrom = slotToEquipFrom;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(slotToEquipTo);
        buffer.writeInt(slotToEquipFrom);
    }

    public static CSEquipAccessories decode(PacketBuffer buffer) {
        CSEquipAccessories msg = new CSEquipAccessories();
        msg.slotToEquipTo = buffer.readInt();
        msg.slotToEquipFrom = buffer.readInt();
        return msg;
    }

    public static void handle(CSEquipAccessories message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
            int oldItemAP = 0;
            int newItemAP = 0;
            if(!ItemStack.areItemStacksEqual(playerData.getEquippedAccessory(message.slotToEquipTo),ItemStack.EMPTY)){
              	oldItemAP = ((KKAccessoryItem)playerData.getEquippedAccessory(message.slotToEquipTo).getItem()).getAp();
            }
            
            if(!ItemStack.areItemStacksEqual(player.inventory.getStackInSlot(message.slotToEquipFrom),ItemStack.EMPTY)){
            	newItemAP = ((KKAccessoryItem)player.inventory.getStackInSlot(message.slotToEquipFrom).getItem()).getAp();
            }

            //System.out.println(Utils.getConsumedAP(playerData)+" "+playerData.getMaxAP(true)+" "+itemAP);
            if(playerData.getMaxAP(true) - oldItemAP + newItemAP >= Utils.getConsumedAP(playerData)) { 
	            ItemStack stackToEquip = player.inventory.getStackInSlot(message.slotToEquipFrom);
	            ItemStack stackPreviouslyEquipped = playerData.equipAccessory(message.slotToEquipTo, stackToEquip);
	            player.inventory.setInventorySlotContents(message.slotToEquipFrom, stackPreviouslyEquipped);
            }
            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayerEntity) player);

            Utils.RefreshAbilityAttributes(player, playerData);
        });
        ctx.get().setPacketHandled(true);
    }

}
