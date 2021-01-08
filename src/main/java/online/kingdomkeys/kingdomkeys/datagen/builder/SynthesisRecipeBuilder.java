package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;

import java.util.HashMap;
import java.util.Map;

public class SynthesisRecipeBuilder<T extends SynthesisRecipeBuilder<T>> extends ModelFile {

    protected final ExistingFileHelper existingFileHelper;
    private ResourceLocation output;
    private String type;
    private Map recipe = new HashMap();
    private int quantity;
    public SynthesisRecipeBuilder(Object o, Object o1) {
        super((ResourceLocation) o);
        this.existingFileHelper = (ExistingFileHelper) o1;
    }

    private T self() {
        return (T) this;
    }

    public T output(String output, int quantity) {
        Preconditions.checkNotNull(output, "Texture must not be null");
        ResourceLocation asLoc;
        if (output.contains(":")) {
            asLoc = new ResourceLocation(output);
        } else {
            asLoc = new ResourceLocation(getLocation().getNamespace(), output);
        }
        return output(asLoc, quantity);
    }

    public T output(ResourceLocation output, int quantity) {
        Preconditions.checkNotNull(output, "Keychain must not be null");
        this.output = output;
        this.quantity = quantity;
        return self();
    }
    
    public T addType(String type) {
        this.type = type;
        return self();
    }
    
    public T addMaterial(String mat, int quantity) {
        recipe.put(KingdomKeys.MODID + ":" + Strings.SM_Prefix + mat, quantity);
        return self();
    }

    @Override
    protected boolean exists() {
        return true;
    }

    @VisibleForTesting
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonObject obj1 = new JsonObject();
        JsonArray recipes = new JsonArray();

        if (this.output != null) {
            obj1.addProperty("item", this.output.toString());
            obj1.addProperty("quantity", quantity);
            obj1.addProperty("type", type);
        }

        if (recipe != null) {
            recipe.forEach((key, value) -> {
                JsonObject matObj = new JsonObject();
                matObj.addProperty("material", key.toString());
                matObj.addProperty("quantity", value.toString());
                recipes.add(matObj);
            });
        }

        root.add("output", obj1);
        //root.add("type", type);
        root.add("ingredients", recipes);

        return root;
    }
}
