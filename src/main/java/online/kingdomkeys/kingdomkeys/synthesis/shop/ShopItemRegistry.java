package online.kingdomkeys.kingdomkeys.synthesis.shop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Lists;

public class ShopItemRegistry {

    private static ShopItemRegistry INSTANCE;
    private Map<ResourceLocation, ShopItem> registry;

    private ShopItemRegistry() {
        registry = new HashMap<>();
    }

    public static ShopItemRegistry getInstance() {
        if (INSTANCE == null) 
        	INSTANCE = new ShopItemRegistry();
        return INSTANCE;
    }

    public void register(ShopItem shopItem) {
        if (shopItem.getRegistryName() != null) {
            registry.put(shopItem.getRegistryName(), shopItem);
            if (ModConfigs.debugConsoleOutput)
                KingdomKeys.LOGGER.info("Successfully registered synthesis recipe {}", shopItem.getRegistryName());
            switch(shopItem.type) {
            case "keyblade":
            	Lists.keybladeRecipes.add(shopItem.getRegistryName());
            	break;
            case "item":
            	Lists.itemRecipes.add(shopItem.getRegistryName());
            	break;
            }
        } else {
            KingdomKeys.LOGGER.error("Cannot register Synthesis Recipe with no registry name");
        }
    }

    public ShopItem getValue(ResourceLocation key) {
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

    public Map<ResourceLocation, ShopItem> getRegistry() {
        return registry;
    }

    public List<ShopItem> getValues() {
        return new LinkedList<ShopItem>(registry.values());
    }

    public void setRegistry(Map<ResourceLocation, ShopItem> registry) {
        this.registry = registry;
    }
}
