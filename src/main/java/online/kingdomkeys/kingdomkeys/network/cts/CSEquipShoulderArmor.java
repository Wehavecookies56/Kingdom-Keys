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
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSEquipShoulderArmor(int slotToEquipTo, int slotToEquipFrom) implements Packet {

    public static final Type<CSEquipShoulderArmor> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_equip_shoulder_armor"));

    public static final StreamCodec<FriendlyByteBuf, CSEquipShoulderArmor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CSEquipShoulderArmor::slotToEquipTo,
            ByteBufCodecs.INT,
            CSEquipShoulderArmor::slotToEquipFrom,
            CSEquipShoulderArmor::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Pauldron(player, playerData.getEquippedKBArmor(slotToEquipTo), player.getInventory().getItem(slotToEquipFrom), slotToEquipFrom, slotToEquipTo)).isCanceled()) {
            ItemStack stackToEquip = player.getInventory().getItem(slotToEquipFrom);
            ItemStack stackPreviouslyEquipped = playerData.equipKBArmor(slotToEquipTo, stackToEquip);
            player.getInventory().setItem(slotToEquipFrom, stackPreviouslyEquipped);
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandler.syncToAllAround(player, playerData);
            PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
