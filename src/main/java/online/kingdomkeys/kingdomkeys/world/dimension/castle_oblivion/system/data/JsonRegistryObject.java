package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class JsonRegistryObject implements INBTSerializable<CompoundTag> {
    protected ResourceLocation registryName;

    public JsonRegistryObject(CompoundTag tag) {
        deserializeNBT(tag);
    }
    public JsonRegistryObject(JsonElement element) {
        deserializeJson(element);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", registryName.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        registryName = new ResourceLocation(nbt.getString("name"));
    }

    public void deserializeJson(JsonElement element) throws JsonParseException {
        if (element.getAsJsonObject().has("name")) {
            String s = element.getAsJsonObject().get("name").getAsString();
            if (s.isEmpty()) {
                throw new JsonParseException("Name must not be empty");
            }
            registryName = new ResourceLocation(s);
        } else {
            throw new JsonParseException("Missing required element \"name\"");
        }
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }
}
