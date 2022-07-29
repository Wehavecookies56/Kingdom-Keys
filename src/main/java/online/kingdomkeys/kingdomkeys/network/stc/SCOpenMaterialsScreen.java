package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisMaterialScreen;

public class SCOpenMaterialsScreen {
	String inv;
	
    public SCOpenMaterialsScreen() {
    	this.inv = "default";
    }

	public SCOpenMaterialsScreen(String inv) {
		this.inv = inv;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(inv);
	}

	public static SCOpenMaterialsScreen decode(FriendlyByteBuf buffer) {
		SCOpenMaterialsScreen msg = new SCOpenMaterialsScreen();
		msg.inv = buffer.readUtf();
		return msg;
	}

    public static void handle(final SCOpenMaterialsScreen msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            ctx.get().enqueueWork(() -> SCOpenMaterialsScreen.Client.handle(msg));
        ctx.get().setPacketHandled(true);
    }


    public static class Client {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SCOpenMaterialsScreen message) {
            Minecraft.getInstance().setScreen(new SynthesisMaterialScreen(message.inv));
        }
    }

}
