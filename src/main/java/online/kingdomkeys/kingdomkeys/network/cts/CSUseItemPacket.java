package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSUseItemPacket {

	int slot;
	String target;

	public CSUseItemPacket() {
	}

	public CSUseItemPacket(int slot) {
		this.slot = slot;
		this.target = "";
	}

	public CSUseItemPacket(int slot, String target) {
		this.slot = slot;
		this.target = target;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.slot);
		buffer.writeInt(this.target.length());
		buffer.writeUtf(this.target);
	}

	public static CSUseItemPacket decode(FriendlyByteBuf buffer) {
		CSUseItemPacket msg = new CSUseItemPacket();
		msg.slot = buffer.readInt();
		int length = buffer.readInt();
		msg.target = buffer.readUtf(length);
		return msg;
	}

	public static void handle(CSUseItemPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			KKPotionItem potion = (KKPotionItem) playerData.getEquippedItem(message.slot).getItem();

			if (message.target.equals("")) {
				potion.potionEffect(player);
			} else {
				Player targetEntity = Utils.getPlayerByName(player.level(), message.target);
				potion.potionEffect(targetEntity);
			}
			playerData.equipItem(message.slot, ItemStack.EMPTY);

			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);

		});
		ctx.get().setPacketHandled(true);
	}

}
