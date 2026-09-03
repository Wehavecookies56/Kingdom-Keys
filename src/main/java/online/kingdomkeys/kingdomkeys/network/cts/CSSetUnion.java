package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record CSSetUnion(Union union) implements Packet {
    public static final Type<CSSetUnion> TYPE = new Type<>(KingdomKeys.rl("cs_set_union"));

    public static final StreamCodec<FriendlyByteBuf, CSSetUnion> STREAM_CODEC = StreamCodec.composite(
            Union.STREAM_CODEC, CSSetUnion::union,
            CSSetUnion::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (playerData == null)
            return;

        if (union == Union.NONE)
            return;

        // Has to be at the union stage, and cannot already have one
        if (playerData.getSoAState() != SoAState.UNION || playerData.hasUnion())
            return;

        playerData.setUnion(union);
        playerData.setSoAState(SoAState.CHOICE);
        PacketHandler.syncToAllAround(player, playerData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
