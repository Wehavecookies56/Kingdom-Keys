package online.kingdomkeys.kingdomkeys.synthesis.shop.sell;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SellListRegistry {

    private static SellListRegistry INSTANCE;
    private Map<ResourceLocation, SellList> registry;

    private SellListRegistry() {
        registry = new HashMap<>();
    }

    public static SellListRegistry getInstance() {
        if (INSTANCE == null) 
        	INSTANCE = new SellListRegistry();
        return INSTANCE;
    }

    public void register(SellList SellList) {
        if (SellList.getRegistryName() != null) {
            registry.put(SellList.getRegistryName(), SellList);
            KingdomKeys.LOGGER.debug("Successfully registered Sell list {}", SellList.getRegistryName());
        } else {
            KingdomKeys.LOGGER.error("Cannot register Sell list with no registry name");
        }
    }

    public SellList getValue(ResourceLocation key) {
        if (containsKey(key)) {
            return registry.get(key);
        } else {
            if (!key.getPath().isEmpty()) {
                KingdomKeys.LOGGER.error("Sell list registry does not contain location: {}", key);
            }
            return null;
        }
    }

    public boolean containsKey(ResourceLocation key) {
        return registry.containsKey(key);
    }

    public void clearRegistry() {
        registry.clear();
        KingdomKeys.LOGGER.debug("Sell list registry cleared");
    }

    public Map<ResourceLocation, SellList> getRegistry() {
        return registry;
    }

    public List<SellList> getValues() {
        return new LinkedList<>(registry.values());
    }

    public void setRegistry(Map<ResourceLocation, SellList> registry) {
        this.registry = registry;
    }
}
