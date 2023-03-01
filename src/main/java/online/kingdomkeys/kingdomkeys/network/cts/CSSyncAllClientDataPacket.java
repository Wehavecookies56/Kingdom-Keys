package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSSyncAllClientDataPacket {

	public CSSyncAllClientDataPacket() {
	}

	public void encode(FriendlyByteBuf buffer) {

	}

	public static CSSyncAllClientDataPacket decode(FriendlyByteBuf buffer) {
		CSSyncAllClientDataPacket msg = new CSSyncAllClientDataPacket();

		return msg;
	}

	public static void handle(final CSSyncAllClientDataPacket message, Supplier<NetworkEvent.Context> ctx) {
		Player player = ctx.get().getSender();

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);

		ctx.get().setPacketHandled(true);
	}

}
