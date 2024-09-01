package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMagicCustomize;

public record CSOpenMagicCustomize() implements Packet {

    public static final Type<CSOpenMagicCustomize> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_open_magic_customize"));

    public static final StreamCodec<FriendlyByteBuf, CSOpenMagicCustomize> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSOpenMagicCustomize());

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData cap = PlayerData.get(player);
        PacketHandler.sendTo(new SCOpenMagicCustomize(cap.getMagicsMap()), (ServerPlayer) player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
