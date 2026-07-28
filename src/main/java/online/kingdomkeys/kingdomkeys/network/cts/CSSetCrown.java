package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

/**
 * Picks which of the player's unlocked crowns to wear. An empty string means none.
 *
 * <p>The server checks that they actually own it: the menu only offers unlocked crowns, but the menu
 * is not where this decision gets to be final.</p>
 */
public record CSSetCrown(String crown) implements Packet {

    public static final Type<CSSetCrown> TYPE = new Type<>(KingdomKeys.rl("cs_set_crown"));

    public static final StreamCodec<FriendlyByteBuf, CSSetCrown> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CSSetCrown::crown,
            CSSetCrown::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return;
        }

        if (!crown.isEmpty() && !playerData.hasUnlockedCrown(crown)) {
            return;
        }

        playerData.setCrown(crown);
        PacketHandler.syncToAllAround(player, playerData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
