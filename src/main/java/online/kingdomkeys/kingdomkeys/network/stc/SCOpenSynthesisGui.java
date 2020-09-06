package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class SCOpenSynthesisGui {

	public SCOpenSynthesisGui() {
	}

	public void encode(PacketBuffer buffer) {

	}

	public static SCOpenSynthesisGui decode(PacketBuffer buffer) {
		SCOpenSynthesisGui msg = new SCOpenSynthesisGui();
		return msg;
	}

	public static void handle(final SCOpenSynthesisGui message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCOpenSynthesisGui message) {
			KingdomKeys.proxy.getClientMCInstance().displayGuiScreen(new SynthesisScreen());
			KingdomKeys.proxy.getClientWorld().playSound(KingdomKeys.proxy.getClientPlayer(), KingdomKeys.proxy.getClientPlayer().getPosition(), ModSounds.kupo.get(), SoundCategory.MASTER, 1, 1);
		}
	}

}
