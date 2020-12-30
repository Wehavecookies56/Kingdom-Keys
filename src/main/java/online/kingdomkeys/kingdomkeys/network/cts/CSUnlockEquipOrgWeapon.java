package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.function.Supplier;

public class CSUnlockEquipOrgWeapon {

    int cost;
    ItemStack weapon;
    boolean unlock;

    public CSUnlockEquipOrgWeapon() {}

    //Unlock
    public CSUnlockEquipOrgWeapon(ItemStack weapon, int cost) {
        this.cost = cost;
        this.weapon = weapon;
        this.unlock = true;
    }

    //Equip
    public CSUnlockEquipOrgWeapon(ItemStack weapon) {
        this.cost = 0;
        this.weapon = weapon;
        this.unlock = false;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(weapon);
        buffer.writeInt(cost);
        buffer.writeBoolean(unlock);
    }

    public static CSUnlockEquipOrgWeapon decode(PacketBuffer buffer) {
        CSUnlockEquipOrgWeapon msg = new CSUnlockEquipOrgWeapon();
        msg.weapon = buffer.readItemStack();
        msg.cost = buffer.readInt();
        msg.unlock = buffer.readBoolean();
        return msg;
    }

    public static void handle(CSUnlockEquipOrgWeapon message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if (message.unlock) {
                if (playerData.getHearts() >= message.cost) {
                    playerData.unlockWeapon(message.weapon);
                    playerData.removeHearts(message.cost);
                }
            } else {
                if (playerData.isWeaponUnlocked(message.weapon.getItem())) {
                    playerData.equipWeapon(message.weapon);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
