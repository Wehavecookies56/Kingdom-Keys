package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSEquipKeychain {

    ResourceLocation slotToEquipTo;
    int slotToEquipFrom;

    public CSEquipKeychain() {}

    public CSEquipKeychain(ResourceLocation slotToEquipTo, int slotToEquipFrom) {
        this.slotToEquipTo = slotToEquipTo;
        this.slotToEquipFrom = slotToEquipFrom;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(slotToEquipTo);
        buffer.writeInt(slotToEquipFrom);
    }

    public static CSEquipKeychain decode(FriendlyByteBuf buffer) {
        CSEquipKeychain msg = new CSEquipKeychain();
        msg.slotToEquipTo = buffer.readResourceLocation();
        msg.slotToEquipFrom = buffer.readInt();
        return msg;
    }

    public static void handle(CSEquipKeychain message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerData playerData = ModData.getPlayer(player);
            if (!MinecraftForge.EVENT_BUS.post(new EquipmentEvent.Keychain(player, playerData.getEquippedKeychain(message.slotToEquipTo), player.getInventory().getItem(message.slotToEquipFrom), message.slotToEquipFrom, message.slotToEquipTo))) {
                ItemStack stackToEquip = player.getInventory().getItem(message.slotToEquipFrom);
                ItemStack stackPreviouslyEquipped = playerData.equipKeychain(message.slotToEquipTo, stackToEquip);
                player.getInventory().setItem(message.slotToEquipFrom, stackPreviouslyEquipped);
                PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);

                Utils.RefreshAbilityAttributes(player, playerData);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
