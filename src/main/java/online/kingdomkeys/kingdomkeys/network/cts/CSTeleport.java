package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSTeleport(Vec3 pos) implements Packet {

    public static final Type<CSTeleport> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_teleport"));

    public static final StreamCodec<FriendlyByteBuf, CSTeleport> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            CSTeleport::pos,
            CSTeleport::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (context.player().isCreative()) {
            context.player().teleportTo(pos.x, pos.y, pos.z);
            context.player().level().playSound(null, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
