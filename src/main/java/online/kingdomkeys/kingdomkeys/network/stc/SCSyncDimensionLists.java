package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class SCSyncDimensionLists {

    final Set<ResourceKey<Level>> addedDims;
    final Set<ResourceKey<Level>> removedDims;

    public SCSyncDimensionLists(Set<ResourceKey<Level>> addedDims, Set<ResourceKey<Level>> removedDims) {
        this.addedDims = addedDims;
        this.removedDims = removedDims;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.addedDims.size());
        this.addedDims.forEach(key -> buf.writeResourceLocation(key.location()));
        buf.writeVarInt(this.removedDims.size());
        this.removedDims.forEach(key -> buf.writeResourceLocation(key.location()));
    }

    public static SCSyncDimensionLists decode(FriendlyByteBuf buf) {
        Set<ResourceKey<Level>> addedDims = new HashSet<>();
        Set<ResourceKey<Level>> removedDims = new HashSet<>();
        final int addedSize = buf.readVarInt();
        for (int i = 0; i < addedSize; i++) {
            final ResourceLocation dim = buf.readResourceLocation();
            addedDims.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, dim));
        }
        final int removedSize = buf.readVarInt();
        for (int i = 0; i < removedSize; i++) {
            final ResourceLocation dim = buf.readResourceLocation();
            removedDims.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, dim));
        }
        return new SCSyncDimensionLists(addedDims, removedDims);
    }

    public static void handle(SCSyncDimensionLists msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                final Set<ResourceKey<Level>> levels = player.connection.levels();
                levels.addAll(msg.addedDims);
                msg.removedDims.forEach(levels::remove);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void syncClients(Set<ResourceKey<Level>> addedDims, Set<ResourceKey<Level>> removedDims) {
        PacketHandler.sendToAllPlayers(new SCSyncDimensionLists(addedDims, removedDims));
    }

}
