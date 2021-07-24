package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.OrgPortalGui;

public class SCShowOrgPortalGUI {

	BlockPos pos;
	
	public SCShowOrgPortalGUI() { }
	
	public SCShowOrgPortalGUI(BlockPos pos) {
		this.pos = pos;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	public static SCShowOrgPortalGUI decode(FriendlyByteBuf buffer) {
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
			KingdomKeys.proxy.getClientMCInstance().setScreen(new OrgPortalGui(message.pos));
		}
	}

}
