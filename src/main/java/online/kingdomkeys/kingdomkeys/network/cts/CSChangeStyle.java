package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.IStyleChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.function.Supplier;

public class CSChangeStyle {

    IStyleChoices style;
    HandStyle handStyle;


    public CSChangeStyle(IStyleChoices style, HandStyle handStyle) {
        this.style = style;
        this.handStyle = handStyle;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(style.toString());
        buffer.writeUtf(handStyle.toString());
    }

    public static CSChangeStyle decode(FriendlyByteBuf buffer) {
        String choice = buffer.readUtf();
        HandStyle handStyle1 = HandStyle.valueOf(buffer.readUtf());
        for (DualChoices d: DualChoices.values()) {
            if(d.toString().equals(choice))
                return new CSChangeStyle(DualChoices.valueOf(choice), handStyle1);
        }
        return new CSChangeStyle(SingleChoices.valueOf(choice), handStyle1);

    }

    public static void handle(CSChangeStyle message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if(message.handStyle == HandStyle.DUAL)
                playerData.setDualStyle((DualChoices) message.style);
            else
                playerData.setSingleStyle((SingleChoices) message.style);

            PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), player);

            Utils.RefreshAbilityAttributes(player, playerData);
        });
        ctx.get().setPacketHandled(true);
    }
}
