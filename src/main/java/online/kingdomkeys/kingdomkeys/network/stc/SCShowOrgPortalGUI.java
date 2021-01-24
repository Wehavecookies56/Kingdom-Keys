package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.OrgPortalGui;

public class SCShowOrgPortalGUI {

	BlockPos pos;
	
	public SCShowOrgPortalGUI() { }
	
	public SCShowOrgPortalGUI(BlockPos pos) {
		this.pos = pos;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static SCShowOrgPortalGUI decode(PacketBuffer buffer) {
		SCShowOrgPortalGUI msg = new SCShowOrgPortalGUI();
		msg.pos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(final SCShowOrgPortalGUI message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCShowOrgPortalGUI message) {
			KingdomKeys.proxy.getClientMCInstance().displayGuiScreen(new OrgPortalGui(message.pos));
		}
	}

}
