package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record SCSyncDimensionLists(Set<ResourceKey<Level>> addedDims, Set<ResourceKey<Level>> removedDims) implements Packet {

    public static final Type<SCSyncDimensionLists> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_dimension_lists"));

    public static final StreamCodec<FriendlyByteBuf, SCSyncDimensionLists> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(HashSet::new, ResourceKey.streamCodec(Registries.DIMENSION)),
            SCSyncDimensionLists::addedDims,
            ByteBufCodecs.collection(HashSet::new, ResourceKey.streamCodec(Registries.DIMENSION)),
            SCSyncDimensionLists::removedDims,
            SCSyncDimensionLists::new
    );

    public static void syncClients(Set<ResourceKey<Level>> addedDims, Set<ResourceKey<Level>> removedDims) {
        PacketHandler.sendToAll(new SCSyncDimensionLists(addedDims, removedDims));
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                final Set<ResourceKey<Level>> levels = player.connection.levels();
                levels.addAll(addedDims);
                removedDims.forEach(levels::remove);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
