package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuPotionSelectorScreen;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSEquipItems(int slotToEquipTo, int slotToEquipFrom) implements Packet {

    public static final Type<CSEquipItems> TYPE = new CustomPacketPayload.Type<>(KingdomKeys.rl("cs_equip_items"));

    public static final StreamCodec<FriendlyByteBuf, CSEquipItems> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CSEquipItems::slotToEquipTo,
            ByteBufCodecs.INT, CSEquipItems::slotToEquipFrom,
            CSEquipItems::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);

        boolean fromBag = slotToEquipFrom <= MenuPotionSelectorScreen.BAG_OFFSET;
        BagInventory bagInv = null;
        int bagSlot = -1;
        ItemStack stackToEquip;

        if (fromBag) {
            bagSlot = Math.abs(slotToEquipFrom - MenuPotionSelectorScreen.BAG_OFFSET);
            ItemStack bag = Utils.getItemInInventory(player, ModItems.consumablesBag.get());
            if (bag.isEmpty())
                return;

            if (!(bag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory inv))
                return;

            bagInv = inv;
            if (bagSlot < 0 || bagSlot >= bagInv.getSlots())
                return;

            stackToEquip = bagInv.getStackInSlot(bagSlot);
        } else {
            if (slotToEquipFrom < 0 || slotToEquipFrom >= player.getInventory().getContainerSize())
                return;

            stackToEquip = player.getInventory().getItem(slotToEquipFrom);
        }

        if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Item(player, playerData.getEquippedItem(slotToEquipTo), stackToEquip, slotToEquipFrom, slotToEquipTo)).isCanceled()) {
            ItemStack stackPreviouslyEquipped = playerData.equipItem(slotToEquipTo, stackToEquip);

            if (fromBag) {
                bagInv.setStackInSlot(bagSlot, stackPreviouslyEquipped);
            } else {
                player.getInventory().setItem(slotToEquipFrom, stackPreviouslyEquipped);
            }

            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
