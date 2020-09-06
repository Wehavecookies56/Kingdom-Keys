package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;

import java.util.function.Supplier;

public class SCOpenEquipmentScreen {

    public SCOpenEquipmentScreen() {}

    public void encode(PacketBuffer buffer) {}

    public static SCOpenEquipmentScreen decode(PacketBuffer buffer) { return new SCOpenEquipmentScreen(); }

    public static void handle(final SCOpenEquipmentScreen msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            ctx.get().enqueueWork(() -> SCOpenEquipmentScreen.Client.handle(msg));
        ctx.get().setPacketHandled(true);
    }


    public static class Client {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SCOpenEquipmentScreen msg) {
            Minecraft.getInstance().displayGuiScreen(new MenuEquipmentScreen());
        }
    }

}
