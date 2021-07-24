package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;

public class SCOpenEquipmentScreen {

    public SCOpenEquipmentScreen() {}

    public void encode(FriendlyByteBuf buffer) {}

    public static SCOpenEquipmentScreen decode(FriendlyByteBuf buffer) { return new SCOpenEquipmentScreen(); }

    public static void handle(final SCOpenEquipmentScreen msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            ctx.get().enqueueWork(() -> SCOpenEquipmentScreen.Client.handle(msg));
        ctx.get().setPacketHandled(true);
    }


    public static class Client {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SCOpenEquipmentScreen msg) {
            Minecraft.getInstance().setScreen(new MenuEquipmentScreen());
        }
    }

}
