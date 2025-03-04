package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data.JsonRegistry;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data.ModJsonRegistries;

import java.util.function.Supplier;

public class SCSyncJsonRegistry<T extends JsonRegistryObject> {

    JsonRegistry<T> registry;

    public SCSyncJsonRegistry() {}

    public SCSyncJsonRegistry(JsonRegistry<T> registry) {
        this.registry = registry;
    }

    public SCSyncJsonRegistry(FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        if (tag != null) {
            ModJsonRegistries.FLOOR_TYPE.get().deserializeNBT(tag);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(registry.serializeNBT());
    }

    public static <T extends JsonRegistryObject> void handle(final SCSyncJsonRegistry<T> message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
    }

}
