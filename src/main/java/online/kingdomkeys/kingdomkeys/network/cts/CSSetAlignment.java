package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.HeartlessForm;

import java.util.List;

public record CSSetAlignment(Utils.OrgMember alignment) implements Packet {

    public static final Type<CSSetAlignment> TYPE = new Type<>(KingdomKeys.rl("cs_set_alignment"));

    public static final StreamCodec<FriendlyByteBuf, CSSetAlignment> STREAM_CODEC = StreamCodec.composite(
            Utils.OrgMember.STREAM_CODEC,
            CSSetAlignment::alignment,
            CSSetAlignment::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (alignment == Utils.OrgMember.NONE || !Utils.canJoinOrg(player)) {
            return;
        }
        // Joining by their Nobody's memory uses it up, giving back what they lost as it always does
        if (!Utils.orgJoinsByRobes()) {
            Utils.findOwnMemory(player).shrink(1);
            HeartlessForm.recoverStats(playerData);
            playerData.setNobody(true);
            PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title(HeartlessForm.MERGED, HeartlessForm.MERGED_SUB, 10, 70, 20))), (ServerPlayer) player);
        }
        playerData.setAlignment(alignment);
        Item weapon = switch (alignment) {
	        case XEMNAS -> ModItems.malice.get();
	        case XIGBAR -> ModItems.standalone.get();
	        case XALDIN -> ModItems.zephyr.get();
	        case VEXEN -> ModItems.testerZero.get();
	        case LEXAEUS -> ModItems.reticence.get();
	        case ZEXION -> ModItems.blackPrimer.get();
	        case SAIX -> ModItems.newMoon.get();
	        case AXEL -> ModItems.ashes.get();
	        case DEMYX -> ModItems.basicModel.get();
	        case LUXORD -> ModItems.theFool.get();
	        case MARLUXIA -> ModItems.fickleErica.get();
	        case LARXENE -> ModItems.trancheuse.get();
	        case ROXAS -> ModItems.kingdomKey.get();
	        default -> null;
        };
	    if (weapon != null) {
            ItemStack stack = new ItemStack(weapon);
            if (playerData.isWeaponUnlocked(weapon)) {
                playerData.getWeaponsUnlocked().forEach(stack1 -> {
                    if (stack1.is(stack.getItem())) {
                        stack.set(ModComponents.KEYBLADE_ID, stack1.get(ModComponents.KEYBLADE_ID));
                    }
                });
            } else {
                Utils.createKeybladeID(stack);
            }
            playerData.unlockWeapon(stack);
            playerData.equipWeapon(stack);
            playerData.setActiveDriveForm(DriveForm.NONE);
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
