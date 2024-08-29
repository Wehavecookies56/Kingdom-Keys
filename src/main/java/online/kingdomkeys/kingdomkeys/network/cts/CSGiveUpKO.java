package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.function.Supplier;

public class CSGiveUpKO {


    public CSGiveUpKO() {}


    public void encode(FriendlyByteBuf buffer) {
    }

    public static CSGiveUpKO decode(FriendlyByteBuf buffer) {
        CSGiveUpKO msg = new CSGiveUpKO();
        return msg;
    }

    public static void handle(CSGiveUpKO message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IGlobalCapabilities globalData = ModData.getGlobal(player);

            killPlayer(player);
            if(globalData != null){
                globalData.setKO(false);
                PacketHandler.syncToAllAround(player,globalData);
            }
            killPlayer(player);

        });
        ctx.get().setPacketHandled(true);
    }

    public static void killPlayer(Player player){
        player.kill();
        player.remove(Entity.RemovalReason.KILLED);
        player.gameEvent(GameEvent.ENTITY_DIE);
    }
}
