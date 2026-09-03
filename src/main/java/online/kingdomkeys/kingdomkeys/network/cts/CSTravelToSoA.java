package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartDimension;

public record CSTravelToSoA() implements Packet {

    public static final Type<CSTravelToSoA> TYPE = new Type<>(KingdomKeys.rl("cs_travel_to_soa"));

    public static final StreamCodec<FriendlyByteBuf, CSTravelToSoA> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSTravelToSoA());

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (playerData.getSoAState() != SoAState.COMPLETE) {
            playerData.setReturnDimension(player);
            playerData.setReturnLocation(player);

            // Without a union the dive starts in a new platform which then takes you to the pedestals one
            boolean hasUnion = playerData.hasUnion();
            playerData.setSoAState(hasUnion ? SoAState.CHOICE : SoAState.UNION);

            BlockPos arrival = DiveToTheHeartChunkGenerator.spawnFor(hasUnion);
            ServerLevel dimension = player.level().getServer().getLevel(ModDimensions.DIVE_TO_THE_HEART);
            player.changeDimension(new DimensionTransition(dimension, new Vec3(arrival.getX() + 0.5D, arrival.getY() + 3, arrival.getZ() + 0.5D), Vec3.ZERO, player.getYRot(), player.getXRot(), pEntity -> {}));

            if (!hasUnion && dimension != null) {
                DiveToTheHeartDimension.ensureForetellers(dimension);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
