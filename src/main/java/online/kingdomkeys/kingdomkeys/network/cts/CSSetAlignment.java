package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSSetAlignment(Utils.OrgMember alignment) implements Packet {

    public static final Type<CSSetAlignment> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_alignment"));

    public static final StreamCodec<FriendlyByteBuf, CSSetAlignment> STREAM_CODEC = StreamCodec.composite(
            Utils.OrgMember.STREAM_CODEC,
            CSSetAlignment::alignment,
            CSSetAlignment::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        playerData.setAlignment(alignment);
        Item weapon = null;
        switch(alignment) {
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
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
