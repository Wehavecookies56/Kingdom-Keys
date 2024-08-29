package online.kingdomkeys.kingdomkeys.datagen.builder;

import java.util.HashMap;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class SynthesisRecipeBuilder<T extends SynthesisRecipeBuilder<T>> extends ModelFile {

    private ResourceLocation output;
    private String type;
    private Map<String, Integer> recipe = new HashMap<>();
    private int quantity;
    private int cost;
    private int tier;
    
    public SynthesisRecipeBuilder(Object o, Object o1) {
        super((ResourceLocation) o);
    }

    private T self() {
        return (T) this;
    }

    public T output(String output, int quantity) {
        Preconditions.checkNotNull(output, "Texture must not be null");
        ResourceLocation asLoc;
        if (output.contains(":")) {
            asLoc = ResourceLocation.parse(output);
        } else {
            asLoc = ResourceLocation.fromNamespaceAndPath(getLocation().getNamespace(), output);
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
    
    public T addCost(int cost) {
        this.cost = cost;
        return self();
    }
    
    public T addTier(int tier) {
        this.tier = tier;
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

        root.addProperty("cost", cost);
        root.addProperty("tier", tier);

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
