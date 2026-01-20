package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public record CSPlayAnimation(int animation) implements Packet {

    public static final Type<CSPlayAnimation> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_play_animation"));

    public static final StreamCodec<FriendlyByteBuf, CSPlayAnimation> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CSPlayAnimation::animation,
            CSPlayAnimation::new
    );

    public CSPlayAnimation(AnimationManager.AnimationAccessor<?> animation) {
        this(animation.id());
    }

    @Override
    public void handle(IPayloadContext context) {
        ServerPlayerPatch spp = EpicFightCapabilities.getEntityPatch(context.player(), ServerPlayerPatch.class);
        spp.playAnimationSynchronized(AnimationManager.byId(animation), 0);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
