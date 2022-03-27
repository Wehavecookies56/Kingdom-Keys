package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
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
			ctx.get().enqueueWork(() -> ClientHandler.handle(message, ctx));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCOpenAlignmentScreen message, Supplier<NetworkEvent.Context> ctx) {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openAlignment());
		}
	}

}
