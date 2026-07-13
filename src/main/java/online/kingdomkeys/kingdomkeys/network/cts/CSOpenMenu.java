package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMenu;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

public record CSOpenMenu() implements Packet {

    public static final Type<CSOpenMenu> TYPE = new Type<>(KingdomKeys.rl("cs_open_menu"));
    public static final StreamCodec<FriendlyByteBuf, CSOpenMenu> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSOpenMenu());


    @Override
    public void handle(IPayloadContext context) {
        PlayerData playerData = PlayerData.get(context.player());
        if (playerData.getSoAState() != SoAState.COMPLETE) {
            if (context.player().level().dimension() != ModDimensions.DIVE_TO_THE_HEART) {
                PacketHandler.sendTo(new SCOpenMenu(playerData.serializeNBT(context.player().level().registryAccess()), false), (ServerPlayer) context.player());
            }
        } else {
            PacketHandler.sendTo(new SCOpenMenu(playerData.serializeNBT(context.player().level().registryAccess()), true), (ServerPlayer) context.player());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
