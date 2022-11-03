package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Lists;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecipeRegistry {

    private static RecipeRegistry INSTANCE;
    private Map<ResourceLocation, Recipe> registry;

    private RecipeRegistry() {
        registry = new HashMap<>();
    }

    public static RecipeRegistry getInstance() {
        if (INSTANCE == null) 
        	INSTANCE = new RecipeRegistry();
        return INSTANCE;
    }

    public void register(Recipe recipe) {
        if (recipe.getRegistryName() != null) {
            registry.put(recipe.getRegistryName(), recipe);
            if (ModConfigs.debugConsoleOutput)
                KingdomKeys.LOGGER.info("Successfully registered synthesis recipe {}", recipe.getRegistryName());
            switch(recipe.type) {
            case "keyblade":
            	Lists.keybladeRecipes.add(recipe.getRegistryName());
            	break;
            case "item":
            	Lists.itemRecipes.add(recipe.getRegistryName());
            	break;
            }
        } else {
            KingdomKeys.LOGGER.error("Cannot register Synthesis Recipe with no registry name");
        }
    }

    public Recipe getValue(ResourceLocation key) {
        if (containsKey(key)) {
            return registry.get(key);
        } else {
            KingdomKeys.LOGGER.error("Synthesis Recipe registry does not contain location: {}", key);
            return null;
        }
    }

    public boolean containsKey(ResourceLocation key) {
        return registry.containsKey(key);
    }

    public void clearRegistry() {
        registry.clear();
        KingdomKeys.LOGGER.info("Synthesis Recipe registry cleared");
    }

    public Map<ResourceLocation, Recipe> getRegistry() {
        return registry;
    }

    public List<Recipe> getValues() {
        return new LinkedList<Recipe>(registry.values());
    }

    public void setRegistry(Map<ResourceLocation, Recipe> registry) {
        this.registry = registry;
    }
}
