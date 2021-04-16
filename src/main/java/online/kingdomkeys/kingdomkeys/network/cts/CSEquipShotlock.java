package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class CSEquipShotlock {

    ResourceLocation shotlock;

    public CSEquipShotlock() {}

    public CSEquipShotlock(ResourceLocation shotlock) {
        this.shotlock = shotlock;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(shotlock);
    }

    public static CSEquipShotlock decode(PacketBuffer buffer) {
        CSEquipShotlock msg = new CSEquipShotlock();
        msg.shotlock = buffer.readResourceLocation();
        return msg;
    }

    public static void handle(CSEquipShotlock message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if(playerData.getShotlockList().contains(message.shotlock.toString())) {
            	playerData.setEquippedShotlock(message.shotlock.toString());
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
