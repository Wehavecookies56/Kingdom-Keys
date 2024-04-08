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
    int moogle = -1;
    public SCOpenMaterialsScreen() {}

    public SCOpenMaterialsScreen(int moogle) {
    	this.inv = "";
        this.moogle = moogle;
    }

	public SCOpenMaterialsScreen(String inv, int moogle) {
		this.inv = inv;
        this.moogle = moogle;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(inv);
        buffer.writeInt(moogle);
	}

	public static SCOpenMaterialsScreen decode(FriendlyByteBuf buffer) {
		SCOpenMaterialsScreen msg = new SCOpenMaterialsScreen();
		msg.inv = buffer.readUtf();
        msg.moogle = buffer.readInt();
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
            Minecraft.getInstance().setScreen(new SynthesisMaterialScreen(message.inv, message.moogle));
        }
    }

}
