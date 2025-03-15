package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

public abstract class JsonRegistryObject {
    protected ResourceLocation registryName;

    public JsonRegistryObject(CompoundTag tag) {
        deserializeNBT(tag);
    }
    public JsonRegistryObject(JsonElement element) {
        deserializeJson(element);
    }

    public abstract CompoundTag serializeNBT();

    public abstract void deserializeNBT(CompoundTag tag);

    public abstract void deserializeJson(JsonElement element) throws JsonParseException;

    public ResourceLocation getRegistryName() {
        return registryName;
    }
}
