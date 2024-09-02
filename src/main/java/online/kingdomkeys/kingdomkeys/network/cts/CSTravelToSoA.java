package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

public record CSTravelToSoA() implements Packet {

    public static final Type<CSTravelToSoA> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_travel_to_soa"));

    public static final StreamCodec<FriendlyByteBuf, CSTravelToSoA> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSTravelToSoA());

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (playerData.getSoAState() != SoAState.COMPLETE) {
            playerData.setReturnDimension(player);
            playerData.setReturnLocation(player);
            playerData.setSoAState(SoAState.CHOICE);
            ServerLevel dimension = player.level().getServer().getLevel(ModDimensions.DIVE_TO_THE_HEART);
            player.changeDimension(new DimensionTransition(dimension, new Vec3(0, 28, 0), Vec3.ZERO, player.getYRot(), player.getXRot(), pEntity -> {}));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
