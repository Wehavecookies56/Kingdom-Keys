package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public class CSUseShortcutPacket {

	int index;

	public CSUseShortcutPacket() {
	}

	public CSUseShortcutPacket(int index) {
		this.index = index;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.index);
	}

	public static CSUseShortcutPacket decode(PacketBuffer buffer) {
		CSUseShortcutPacket msg = new CSUseShortcutPacket();
		msg.index = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseShortcutPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge()) {
				if (playerData.getShortcutsMap().containsKey(message.index)) {
					String[] data = playerData.getShortcutsMap().get(message.index).split(",");
					String magicName = data[0];
					int level = Integer.parseInt(data[1]);
	
					System.out.println(magicName);
					Magic magic = ModMagic.registry.getValue(new ResourceLocation(magicName));
					magic.onUse(player, player, level);
	
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				}
			}

		});
		ctx.get().setPacketHandled(true);
	}

}