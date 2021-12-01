package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSUseShortcutPacket {

	int index;

	public CSUseShortcutPacket() {
	}

	public CSUseShortcutPacket(int index) {
		this.index = index;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.index);
	}

	public static CSUseShortcutPacket decode(FriendlyByteBuf buffer) {
		CSUseShortcutPacket msg = new CSUseShortcutPacket();
		msg.index = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseShortcutPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && !playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
				if (playerData.getShortcutsMap().containsKey(message.index)) {
					String[] data = playerData.getShortcutsMap().get(message.index).split(",");
					String magicName = data[0];
					int level = Integer.parseInt(data[1]);
					Magic magic = ModMagic.registry.getValue(new ResourceLocation(magicName));
					magic.onUse(player, player, level);
	
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
				}
			}

		});
		ctx.get().setPacketHandled(true);
	}

}