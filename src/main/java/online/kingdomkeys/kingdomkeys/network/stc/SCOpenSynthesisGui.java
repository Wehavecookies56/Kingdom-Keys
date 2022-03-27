package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class SCOpenSynthesisGui {

	public SCOpenSynthesisGui() {
	}

	public void encode(FriendlyByteBuf buffer) {

	}

	public static SCOpenSynthesisGui decode(FriendlyByteBuf buffer) {
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
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openSynthesisGui());
		}
	}

}
