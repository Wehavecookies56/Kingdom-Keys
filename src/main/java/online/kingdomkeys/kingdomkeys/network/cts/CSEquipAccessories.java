package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSEquipAccessories {

    int slotToEquipTo;
    int slotToEquipFrom;

    public CSEquipAccessories() {}

    public CSEquipAccessories(int slotToEquipTo, int slotToEquipFrom) {
        this.slotToEquipTo = slotToEquipTo;
        this.slotToEquipFrom = slotToEquipFrom;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(slotToEquipTo);
        buffer.writeInt(slotToEquipFrom);
    }

    public static CSEquipAccessories decode(FriendlyByteBuf buffer) {
        CSEquipAccessories msg = new CSEquipAccessories();
        msg.slotToEquipTo = buffer.readInt();
        msg.slotToEquipFrom = buffer.readInt();
        return msg;
    }

    public static void handle(CSEquipAccessories message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerData playerData = ModData.getPlayer(player);

            if (!MinecraftForge.EVENT_BUS.post(new EquipmentEvent.Accessory(player, playerData.getEquippedAccessory(message.slotToEquipTo), player.getInventory().getItem(message.slotToEquipFrom), message.slotToEquipFrom, message.slotToEquipTo))) {
                int oldItemAP = 0;
                int newItemAP = 0;
                if (!ItemStack.matches(playerData.getEquippedAccessory(message.slotToEquipTo), ItemStack.EMPTY)) {
                    oldItemAP = ((KKAccessoryItem) playerData.getEquippedAccessory(message.slotToEquipTo).getItem()).getAp();
                }

                if (!ItemStack.matches(player.getInventory().getItem(message.slotToEquipFrom), ItemStack.EMPTY)) {
                    newItemAP = ((KKAccessoryItem) player.getInventory().getItem(message.slotToEquipFrom).getItem()).getAp();
                }

                //System.out.println(Utils.getConsumedAP(playerData)+" "+playerData.getMaxAP(true)+" "+itemAP);
                if (playerData.getMaxAP(true) - oldItemAP + newItemAP >= Utils.getConsumedAP(playerData)) {
                    ItemStack stackToEquip = player.getInventory().getItem(message.slotToEquipFrom);
                    ItemStack stackPreviouslyEquipped = playerData.equipAccessory(message.slotToEquipTo, stackToEquip);
                    player.getInventory().setItem(message.slotToEquipFrom, stackPreviouslyEquipped);
                }
                PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
                PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);

                Utils.RefreshAbilityAttributes(player, playerData);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
