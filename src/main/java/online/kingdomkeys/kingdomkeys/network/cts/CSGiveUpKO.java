package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record CSGiveUpKO() implements Packet {

    public static final Type<CSGiveUpKO> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_give_up_ko"));

    public static final StreamCodec<FriendlyByteBuf, CSGiveUpKO> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSGiveUpKO());

    public static void killPlayer(Player player){
        player.kill();
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        killPlayer(player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
