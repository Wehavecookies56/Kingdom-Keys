package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;

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

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(weapon);
        buffer.writeInt(cost);
        buffer.writeBoolean(unlock);
    }

    public static CSUnlockEquipOrgWeapon decode(FriendlyByteBuf buffer) {
        CSUnlockEquipOrgWeapon msg = new CSUnlockEquipOrgWeapon();
        msg.weapon = buffer.readItem();
        msg.cost = buffer.readInt();
        msg.unlock = buffer.readBoolean();
        return msg;
    }

    public static void handle(CSUnlockEquipOrgWeapon message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerData playerData = ModData.getPlayer(player);
            if (message.unlock) {
                if (playerData.getHearts() >= message.cost) {
                    message.weapon.setTag(new CompoundTag());
                    message.weapon.getTag().putUUID("keybladeID", UUID.randomUUID());
                    playerData.unlockWeapon(message.weapon);
                    playerData.removeHearts(message.cost);
                }
            } else {
                if (playerData.isWeaponUnlocked(message.weapon.getItem())) {
                    playerData.getWeaponsUnlocked().forEach(itemStack -> {
                        if (itemStack.is(message.weapon.getItem())) {
                            if (!MinecraftForge.EVENT_BUS.post(new EquipmentEvent.OrgWeapon(player, playerData.getEquippedWeapon(), itemStack))) {
                                playerData.equipWeapon(itemStack);
                            }
                        }
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
