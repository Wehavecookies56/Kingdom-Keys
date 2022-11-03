package online.kingdomkeys.kingdomkeys.synthesis.shop;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShopListRegistry {

    private static ShopListRegistry INSTANCE;
    private Map<ResourceLocation, ShopList> registry;

    private ShopListRegistry() {
        registry = new HashMap<>();
    }

    public static ShopListRegistry getInstance() {
        if (INSTANCE == null) 
        	INSTANCE = new ShopListRegistry();
        return INSTANCE;
    }

    public void register(ShopList shopList) {
        if (shopList.getRegistryName() != null) {
            registry.put(shopList.getRegistryName(), shopList);
            if (ModConfigs.debugConsoleOutput)
                KingdomKeys.LOGGER.info("Successfully registered synthesis recipe {}", shopList.getRegistryName());
        } else {
            KingdomKeys.LOGGER.error("Cannot register Synthesis Recipe with no registry name");
        }
    }

    public ShopList getValue(ResourceLocation key) {
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

    public Map<ResourceLocation, ShopList> getRegistry() {
        return registry;
    }

    public List<ShopList> getValues() {
        return new LinkedList<ShopList>(registry.values());
    }

    public void setRegistry(Map<ResourceLocation, ShopList> registry) {
        this.registry = registry;
    }
}
