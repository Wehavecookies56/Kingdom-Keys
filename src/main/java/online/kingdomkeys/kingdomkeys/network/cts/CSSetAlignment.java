package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSSetAlignment {

    public CSSetAlignment() {}

    Utils.OrgMember alignment;

    public CSSetAlignment(Utils.OrgMember alignment) {
        this.alignment = alignment;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(alignment.ordinal());
    }

    public static CSSetAlignment decode(FriendlyByteBuf buffer) {
        CSSetAlignment msg = new CSSetAlignment();
        msg.alignment = Utils.OrgMember.values()[buffer.readInt()];
        return msg;
    }

    public static void handle(CSSetAlignment message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerData playerData = ModData.getPlayer(player);
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
                ItemStack stack = new ItemStack(weapon);
                Utils.createKeybladeID(stack);
                playerData.unlockWeapon(stack);
                playerData.equipWeapon(stack);
                playerData.setActiveDriveForm(DriveForm.NONE.toString());
                PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
            }
            
        });
        ctx.get().setPacketHandled(true);
    }

}
