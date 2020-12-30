package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.function.Supplier;

public class CSSetAlignment {

    public CSSetAlignment() {}

    Utils.OrgMember alignment;

    public CSSetAlignment(Utils.OrgMember alignment) {
        this.alignment = alignment;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(alignment.ordinal());
    }

    public static CSSetAlignment decode(PacketBuffer buffer) {
        CSSetAlignment msg = new CSSetAlignment();
        msg.alignment = Utils.OrgMember.values()[buffer.readInt()];
        return msg;
    }

    public static void handle(CSSetAlignment message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            playerData.setAlignment(message.alignment);
            Item weapon = null;
            switch(message.alignment) {
                case XEMNAS:
                    weapon = ModItems.malice.get();
                    break;
                case XIGBAR:
                    weapon = ModItems.standalone.get();
                    break;
                case XALDIN:
                    weapon = ModItems.zephyr.get();
                    break;
                case VEXEN:
                    weapon = ModItems.testerZero.get();
                    break;
                case LEXAEUS:
                    weapon = ModItems.reticence.get();
                    break;
                case ZEXION:
                    weapon = ModItems.blackPrimer.get();
                    break;
                case SAIX:
                    weapon = ModItems.newMoon.get();
                    break;
                case AXEL:
                    weapon = ModItems.ashes.get();
                    break;
                case DEMYX:
                    weapon = ModItems.basicModel.get();
                    break;
                case LUXORD:
                    weapon = ModItems.theFool.get();
                    break;
                case MARLUXIA:
                    weapon = ModItems.fickleErica.get();
                    break;
                case LARXENE:
                    weapon = ModItems.trancheuse.get();
                    break;
                case ROXAS:
                    weapon = ModItems.kingdomKey.get();
                    break;
            }
            if (weapon != null) {
                playerData.unlockWeapon(new ItemStack(weapon));
                playerData.equipWeapon(new ItemStack(weapon));
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
