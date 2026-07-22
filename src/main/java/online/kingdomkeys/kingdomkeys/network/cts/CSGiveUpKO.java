package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSGiveUpKO() implements Packet {

    public static final Type<CSGiveUpKO> TYPE = new Type<>(KingdomKeys.rl("cs_give_up_ko"));

    public static final StreamCodec<FriendlyByteBuf, CSGiveUpKO> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSGiveUpKO());

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        player.kill();
        player.removeEffect(ModMobEffects.KO);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
