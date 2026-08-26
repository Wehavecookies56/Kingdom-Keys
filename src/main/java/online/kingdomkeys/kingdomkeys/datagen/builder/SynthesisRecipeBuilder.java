package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SynthesisRecipeBuilder extends ModelFile {

    private ResourceLocation output;
    private String type;
    private final Map<String, Integer> recipe = new HashMap<>();
    private int quantity;
    private int cost;
    private int tier;
    
    public SynthesisRecipeBuilder(Object o, Object o1) {
        super((ResourceLocation) o);
    }

    private SynthesisRecipeBuilder self() {
        return (SynthesisRecipeBuilder) this;
    }

    public SynthesisRecipeBuilder output(String output, int quantity) {
        Preconditions.checkNotNull(output, "Texture must not be null");
        ResourceLocation asLoc;
        if (output.contains(":")) {
            asLoc = KingdomKeys.rl(output);
        } else {
            asLoc = KingdomKeys.rl(getLocation().getNamespace(), output);
        }
        return output(asLoc, quantity);
    }

    public SynthesisRecipeBuilder output(ResourceLocation output, int quantity) {
        Preconditions.checkNotNull(output, "Keychain must not be null");
        this.output = output;
        this.quantity = quantity;
        return self();
    }
    
    public SynthesisRecipeBuilder addType(String type) {
        this.type = type;
        return self();
    }
    
    public SynthesisRecipeBuilder addCost(int cost) {
        this.cost = cost;
        return self();
    }
    
    public SynthesisRecipeBuilder addTier(int tier) {
        this.tier = tier;
        return self();
    }
    
    public SynthesisRecipeBuilder addMaterial(Supplier<Item> mat, int quantity) {
        recipe.put(BuiltInRegistries.ITEM.getKey(mat.get()).toString(), quantity);
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
                matObj.addProperty("material", key);
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
