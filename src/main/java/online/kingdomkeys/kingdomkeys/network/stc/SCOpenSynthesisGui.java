package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;

public class SCOpenSynthesisGui {
	String inv;
	String name;
	int moogle = -1;
	
	public SCOpenSynthesisGui() {
	}
	
	public SCOpenSynthesisGui(String inv, String name, int moogle) {
		this.inv = inv;
		this.moogle = moogle;
		this.name = name;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(inv);
		buffer.writeInt(moogle);
		buffer.writeUtf(name);
	}

	public static SCOpenSynthesisGui decode(FriendlyByteBuf buffer) {
		SCOpenSynthesisGui msg = new SCOpenSynthesisGui();
		msg.inv = buffer.readUtf();
		msg.moogle = buffer.readInt();
		msg.name = buffer.readUtf();
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
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openSynthesisGui(message.inv, message.name, message.moogle));
		}
	}

}
