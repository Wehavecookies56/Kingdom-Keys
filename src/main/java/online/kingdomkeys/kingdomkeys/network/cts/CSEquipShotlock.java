package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

/**
 * Equips (or unequips) the single Shotlock slot, swapping with an inventory slot - same idea as
 * CSEquipMagic, just without a "to" slot (there's only ever one Shotlock slot) and without a bag, since
 * Shotlocks don't have their own storage bag the way Magics do.
 */
public record CSEquipShotlock(int slotToEquipFrom) implements Packet {

	public static final Type<CSEquipShotlock> TYPE = new Type<>(KingdomKeys.rl("cs_equip_shotlock"));
	public static final StreamCodec<FriendlyByteBuf, CSEquipShotlock> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, CSEquipShotlock::slotToEquipFrom, CSEquipShotlock::new);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		if (slotToEquipFrom < 0 || slotToEquipFrom >= player.getInventory().getContainerSize()) {
			return;
		}

		ItemStack stackToEquip = player.getInventory().getItem(slotToEquipFrom);

		if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Shotlock(player, playerData.getEquippedShotlock(), stackToEquip, slotToEquipFrom, 0)).isCanceled()) {
			ItemStack stackPreviouslyEquipped = playerData.equipShotlock(stackToEquip);
			if (stackPreviouslyEquipped != null) {
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
