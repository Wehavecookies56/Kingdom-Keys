package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSEquipShotlock {

	String shotlock;

    public CSEquipShotlock() {}

    public CSEquipShotlock(String shotlock) {
        this.shotlock = shotlock;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(shotlock,100);
    }

    public static CSEquipShotlock decode(FriendlyByteBuf buffer) {
        CSEquipShotlock msg = new CSEquipShotlock();
        msg.shotlock = buffer.readUtf(100);
        return msg;
    }

    public static void handle(CSEquipShotlock message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if (!MinecraftForge.EVENT_BUS.post(new EquipmentEvent.Shotlock(player, new ResourceLocation(playerData.getEquippedShotlock()), new ResourceLocation(message.shotlock)))) {
                if (playerData.getShotlockList().contains(message.shotlock) || message.shotlock.equals("")) {
                    playerData.setEquippedShotlock(message.shotlock);
                }
                PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
                PacketHandler.sendTo(new SCOpenEquipmentScreen(), (ServerPlayer) player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
