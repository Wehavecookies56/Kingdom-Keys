package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

import java.util.Map;

public class KeybladeRecipeBuilder<T extends KeybladeRecipeBuilder<T>> extends ModelFile {

    protected final ExistingFileHelper existingFileHelper;
    private Map<Material, Integer> materials;
    private ResourceLocation keychain;

    public KeybladeRecipeBuilder(Object o, Object o1) {
        super((ResourceLocation) o);
        this.existingFileHelper = (ExistingFileHelper) o1;
    }

    private T self() {
        return (T) this;
    }

    public T keychain(String keyChain) {
        Preconditions.checkNotNull(keyChain, "Texture must not be null");
        ResourceLocation asLoc;
        if (keyChain.contains(":")) {
            asLoc = new ResourceLocation(keyChain);
        } else {
            asLoc = new ResourceLocation(getLocation().getNamespace(), keyChain);
        }
        return keychain(asLoc);
    }

    public T keychain(ResourceLocation keychain) {
        Preconditions.checkNotNull(keychain, "Keychain must not be null");
        this.keychain = keychain;
        return self();
    }

    @Override
    protected boolean exists() {
        return true;
    }

    @VisibleForTesting
    public JsonObject toJson() {
        JsonObject root = new JsonObject();

        if (this.keychain != null) {
            root.addProperty("keychain", this.keychain.toString());
        }

            JsonObject obj1 = new JsonObject();
            JsonArray recipe = new JsonArray();
            materials.forEach((key, value) -> {
                JsonObject matObj = new JsonObject();
                matObj.addProperty("material", key.getRegistryName().toString());
                matObj.addProperty("quantity", value);
                recipe.add(matObj); });
            obj1.add("ingredients", recipe);

        return root;
    }
}
