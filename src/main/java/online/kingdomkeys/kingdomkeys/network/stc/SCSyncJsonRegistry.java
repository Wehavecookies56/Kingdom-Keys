package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistry;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

public class SCSyncJsonRegistry<T extends JsonRegistryObject> implements Packet {

    public static final Type<SCSyncJsonRegistry<?>> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_json_registry"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SCSyncJsonRegistry<?>> STREAM_CODEC = StreamCodec.of((buffer, scSyncJsonRegistry) -> scSyncJsonRegistry.encode(buffer), SCSyncJsonRegistry::new);

    JsonRegistry<T> registry;

    public SCSyncJsonRegistry(FriendlyByteBuf buf) {
        ResourceLocation rl = buf.readResourceLocation();
        CompoundTag tag = buf.readNbt();
        JsonRegistry<T> registry = (JsonRegistry<T>) ModJsonRegistries.registry.get(rl);
        if (tag != null) {
            registry.deserializeNBT(tag);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(registry.getRegistryName());
        buffer.writeNbt(registry.serializeNBT());
    }

    public SCSyncJsonRegistry(JsonRegistry<T> registry) {
        this.registry = registry;
    }


    @Override
    public void handle(IPayloadContext context) {}

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
