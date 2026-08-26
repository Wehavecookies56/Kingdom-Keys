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

public record CSSetCrownOffset(float x, float y, float z, float rotX, float rotY, float rotZ) implements Packet {

    public static final Type<CSSetCrownOffset> TYPE = new Type<>(KingdomKeys.rl("cs_set_crown_offset"));

    public static final StreamCodec<FriendlyByteBuf, CSSetCrownOffset> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, CSSetCrownOffset::x,
            ByteBufCodecs.FLOAT, CSSetCrownOffset::y,
            ByteBufCodecs.FLOAT, CSSetCrownOffset::z,
            ByteBufCodecs.FLOAT, CSSetCrownOffset::rotX,
            ByteBufCodecs.FLOAT, CSSetCrownOffset::rotY,
            ByteBufCodecs.FLOAT, CSSetCrownOffset::rotZ,
            CSSetCrownOffset::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        playerData.setCrownOffset(x, y, z);
        playerData.setCrownRotation(rotX, rotY, rotZ);
        PacketHandler.syncToAllAround(player, playerData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
