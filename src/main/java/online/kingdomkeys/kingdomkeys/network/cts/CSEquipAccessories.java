package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSEquipAccessories(int slotToEquipTo, int slotToEquipFrom) implements Packet {

    public static final Type<CSEquipAccessories> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_equip_accessories"));

    public static final StreamCodec<FriendlyByteBuf, CSEquipAccessories> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CSEquipAccessories::slotToEquipTo,
            ByteBufCodecs.INT,
            CSEquipAccessories::slotToEquipFrom,
            CSEquipAccessories::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);

        if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Accessory(player, playerData.getEquippedAccessory(slotToEquipTo), player.getInventory().getItem(slotToEquipFrom), slotToEquipFrom, slotToEquipTo)).isCanceled()) {
            int oldItemAP = 0;
            int newItemAP = 0;
            if (!ItemStack.matches(playerData.getEquippedAccessory(slotToEquipTo), ItemStack.EMPTY)) {
                oldItemAP = ((KKAccessoryItem) playerData.getEquippedAccessory(slotToEquipTo).getItem()).getAp();
            }

            if (!ItemStack.matches(player.getInventory().getItem(slotToEquipFrom), ItemStack.EMPTY)) {
                newItemAP = ((KKAccessoryItem) player.getInventory().getItem(slotToEquipFrom).getItem()).getAp();
            }

            //System.out.println(Utils.getConsumedAP(playerData)+" "+playerData.getMaxAP(true)+" "+itemAP);
            if (playerData.getMaxAP(true) - oldItemAP + newItemAP >= Utils.getConsumedAP(playerData)) {
                ItemStack stackToEquip = player.getInventory().getItem(slotToEquipFrom);
                ItemStack stackPreviouslyEquipped = playerData.equipAccessory(slotToEquipTo, stackToEquip);
                player.getInventory().setItem(slotToEquipFrom, stackPreviouslyEquipped);
            }
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);

            Utils.RefreshAbilityAttributes(player, playerData);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
