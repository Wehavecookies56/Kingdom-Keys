package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisMaterialScreen;

import java.util.function.Supplier;

public class SCOpenMaterialsScreen {

    public SCOpenMaterialsScreen() {}

    public void encode(PacketBuffer buffer) {}

    public static SCOpenMaterialsScreen decode(PacketBuffer buffer) { return new SCOpenMaterialsScreen(); }

    public static void handle(final SCOpenMaterialsScreen msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            ctx.get().enqueueWork(() -> SCOpenMaterialsScreen.Client.handle(msg));
        ctx.get().setPacketHandled(true);
    }


    public static class Client {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SCOpenMaterialsScreen msg) {
            Minecraft.getInstance().displayGuiScreen(new SynthesisMaterialScreen());
        }
    }

}
