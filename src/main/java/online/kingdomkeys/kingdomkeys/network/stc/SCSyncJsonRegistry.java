package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistry;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import java.util.function.Supplier;

public class SCSyncJsonRegistry<T extends JsonRegistryObject> {

    JsonRegistry<T> registry;

    public SCSyncJsonRegistry() {}

    public SCSyncJsonRegistry(JsonRegistry<T> registry) {
        this.registry = registry;
    }

    public SCSyncJsonRegistry(FriendlyByteBuf buffer) {
        ResourceLocation rl = buffer.readResourceLocation();
        CompoundTag tag = buffer.readNbt();
        JsonRegistry<T> registry = (JsonRegistry<T>) ModJsonRegistries.registry.get().getValue(rl);
        if (tag != null) {
           registry.deserializeNBT(tag);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(registry.getRegistryName());
        buffer.writeNbt(registry.serializeNBT());
    }

    public static <T extends JsonRegistryObject> void handle(final SCSyncJsonRegistry<T> message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
    }

}
