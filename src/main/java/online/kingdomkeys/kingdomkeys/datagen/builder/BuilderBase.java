package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public abstract class BuilderBase {

    private final ResourceLocation location;
    protected final JsonObject root = new JsonObject();

    public BuilderBase(ResourceLocation location) {
        this.location = location;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public JsonObject build() {
        return root;
    }
}
