package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuMagicSelectorScreen;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSEquipMagic(int slotToEquipTo, int slotToEquipFrom) implements Packet {
	public static final Type<CSEquipMagic> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_equip_magic"));
	public static final StreamCodec<FriendlyByteBuf, CSEquipMagic> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, CSEquipMagic::slotToEquipTo, ByteBufCodecs.INT, CSEquipMagic::slotToEquipFrom, CSEquipMagic::new);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		ItemStack stackToEquip;

		boolean fromBag = slotToEquipFrom <= MenuMagicSelectorScreen.BAG_OFFSET;
		BagInventory bagInv = null;
		int bagSlot = -1;

		if (fromBag) {
			bagSlot = Math.abs(slotToEquipFrom - MenuMagicSelectorScreen.BAG_OFFSET);
			ItemStack magicBag = Utils.getItemInInventory(player, ModItems.magicsBag.get());
			if (magicBag.isEmpty())
				return;

			if (!(magicBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory inv))
				return;

			bagInv = inv;
			stackToEquip = bagInv.getStackInSlot(bagSlot);
		} else {
			stackToEquip = player.getInventory().getItem(slotToEquipFrom);
		}

		if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Magic(player, playerData.getEquippedMagic(slotToEquipTo), stackToEquip, slotToEquipFrom, slotToEquipTo)).isCanceled()) {
			ItemStack stackPreviouslyEquipped = playerData.equipMagic(slotToEquipTo, stackToEquip);
			if (fromBag) {
				bagInv.setStackInSlot(bagSlot, stackPreviouslyEquipped);
			} else {
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
