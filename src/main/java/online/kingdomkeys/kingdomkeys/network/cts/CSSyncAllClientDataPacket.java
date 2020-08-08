package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSSyncAllClientDataPacket {

	public CSSyncAllClientDataPacket() {
	}

	public void encode(PacketBuffer buffer) {

	}

	public static CSSyncAllClientDataPacket decode(PacketBuffer buffer) {
		CSSyncAllClientDataPacket msg = new CSSyncAllClientDataPacket();

		return msg;
	}

	public static void handle(final CSSyncAllClientDataPacket message, Supplier<NetworkEvent.Context> ctx) {
		PlayerEntity player = ctx.get().getSender();

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);

		ctx.get().setPacketHandled(true);
	}

}
