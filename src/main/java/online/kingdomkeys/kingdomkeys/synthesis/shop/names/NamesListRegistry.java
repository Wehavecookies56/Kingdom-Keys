package online.kingdomkeys.kingdomkeys.synthesis.shop.names;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NamesListRegistry {

    private static NamesListRegistry INSTANCE;
    private Map<ResourceLocation, List<String>> registry;

    private NamesListRegistry() {
        registry = new HashMap<>();
    }

    public static NamesListRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NamesListRegistry();
        return INSTANCE;
    }

    public void register(ResourceLocation registryName, List<String> names) {
        if (registryName != null) {
            registry.put(registryName, names);
            if (ModConfigs.debugConsoleOutput)
                KingdomKeys.LOGGER.info("Successfully registered Moogle names {}", registryName);
        } else {
            KingdomKeys.LOGGER.error("Cannot register Moogle names with no registry name");
        }
    }

    public List<String> getValue(ResourceLocation key) {
        if (containsKey(key)) {
            return registry.get(key);
        } else {
            KingdomKeys.LOGGER.error("Moogle names registry does not contain location: {}", key);
            return null;
        }
    }

    public boolean containsKey(ResourceLocation key) {
        return registry.containsKey(key);
    }

    public void clearRegistry() {
        registry.clear();
        KingdomKeys.LOGGER.info("Moogle names registry cleared");
    }

    public Map<ResourceLocation, List<String>> getRegistry() {
        return registry;
    }

    public List<List<String>> getValues() {
        return new LinkedList<>(registry.values());
    }

    public List<ResourceLocation> getKeys() {
        return new LinkedList<>(registry.keySet());
    }

    public void setRegistry(Map<ResourceLocation, List<String>> registry) {
        this.registry = registry;
    }
    
}
