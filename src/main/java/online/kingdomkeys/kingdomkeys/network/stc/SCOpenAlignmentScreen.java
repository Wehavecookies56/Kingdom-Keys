package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.organization.AlignmentSelectionScreen;

public class SCOpenAlignmentScreen {

	public SCOpenAlignmentScreen() { }

	public void encode(FriendlyByteBuf buffer) {
	}

	public static SCOpenAlignmentScreen decode(FriendlyByteBuf buffer) {
		SCOpenAlignmentScreen msg = new SCOpenAlignmentScreen();
		return msg;
	}

	public static void handle(final SCOpenAlignmentScreen message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCOpenAlignmentScreen message) {
			KingdomKeys.proxy.getClientMCInstance().setScreen(new AlignmentSelectionScreen());
		}
	}

}
