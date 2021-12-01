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

    public SCOpenMaterialsScreen() {}

    public void encode(FriendlyByteBuf buffer) {}

    public static SCOpenMaterialsScreen decode(FriendlyByteBuf buffer) { return new SCOpenMaterialsScreen(); }

    public static void handle(final SCOpenMaterialsScreen msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            ctx.get().enqueueWork(() -> SCOpenMaterialsScreen.Client.handle(msg));
        ctx.get().setPacketHandled(true);
    }


    public static class Client {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SCOpenMaterialsScreen msg) {
            Minecraft.getInstance().setScreen(new SynthesisMaterialScreen());
        }
    }

}
