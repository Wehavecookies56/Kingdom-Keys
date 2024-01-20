package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSChangeStyle {

    String style;
    String handStyle;

    public CSChangeStyle() {}

    public CSChangeStyle(String style, String handStyle) {
        this.style = style;
        this.handStyle = handStyle;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(style.toString(),20);
        buffer.writeUtf(handStyle.toString(),20);
    }

    public static CSChangeStyle decode(FriendlyByteBuf buffer) {
        CSChangeStyle msg = new CSChangeStyle();
        msg.style = buffer.readUtf(20);
        msg.handStyle = buffer.readUtf(20);
        return msg;
    }

    public static void handle(CSChangeStyle message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if(message.handStyle.equals(HandStyle.DUAL.toString()))
                playerData.setDualStyle(DualChoices.valueOf(message.style));
            else
                playerData.setSingleStyle(SingleChoices.valueOf(message.style));
            PacketHandler.syncToAllAround(player, playerData);
        });
        ctx.get().setPacketHandled(true);
    }
    /*public static CSChangeStyle decode(FriendlyByteBuf buffer) {
        String choice = buffer.readUtf(20);
        HandStyle handStyle1 = HandStyle.valueOf(buffer.readUtf(20));
        for (DualChoices d: DualChoices.values()) {
            if(d.toString().equals(choice))
                return new CSChangeStyle(DualChoices.valueOf(choice), handStyle1);
        }
        return new CSChangeStyle(SingleChoices.valueOf(choice), handStyle1);

    }

    public static void handle(CSChangeStyle message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            System.out.println(message.handStyle);
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if(message.handStyle == HandStyle.DUAL)
                playerData.setDualStyle((DualChoices) message.style);
            else
                playerData.setSingleStyle((SingleChoices) message.style);

        });
        ctx.get().setPacketHandled(true);
    }*/
}
