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
import online.kingdomkeys.kingdomkeys.world.HeartlessForm;

public record CSSetNobodyName(String name) implements Packet {

    public static final Type<CSSetNobodyName> TYPE = new Type<>(KingdomKeys.rl("cs_set_nobody_name"));

    public static final StreamCodec<FriendlyByteBuf, CSSetNobodyName> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(32), CSSetNobodyName::name,
            CSSetNobodyName::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (playerData == null || (!name.isEmpty() && !HeartlessForm.isValidNobodyName(player.getGameProfile().getName(), name))) {
            return;
        }
        playerData.setNobodyName(name);
        PacketHandler.syncToAllAround(player, playerData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
