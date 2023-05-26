package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSEquipArmor {

    int slotToEquipTo;
    int slotToEquipFrom;

    public CSEquipArmor() {}

    public CSEquipArmor(int slotToEquipTo, int slotToEquipFrom) {
        this.slotToEquipTo = slotToEquipTo;
        this.slotToEquipFrom = slotToEquipFrom;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(slotToEquipTo);
        buffer.writeInt(slotToEquipFrom);
    }

    public static CSEquipArmor decode(FriendlyByteBuf buffer) {
        CSEquipArmor msg = new CSEquipArmor();
        msg.slotToEquipTo = buffer.readInt();
        msg.slotToEquipFrom = buffer.readInt();
        return msg;
    }

    public static void handle(CSEquipArmor message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
            ItemStack stackToEquip = player.getInventory().getItem(message.slotToEquipFrom);
            ItemStack stackPreviouslyEquipped = playerData.equipArmor(message.slotToEquipTo, stackToEquip);
            player.getInventory().setItem(message.slotToEquipFrom, stackPreviouslyEquipped);
            
            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);

            Utils.RefreshAbilityAttributes(player, playerData);
        });
        ctx.get().setPacketHandled(true);
    }

}
