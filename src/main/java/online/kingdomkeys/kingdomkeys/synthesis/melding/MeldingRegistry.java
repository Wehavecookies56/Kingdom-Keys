package online.kingdomkeys.kingdomkeys.synthesis.melding;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MeldingRegistry {

	private static MeldingRegistry INSTANCE;
	private Map<ResourceLocation, Melding> registry;

	private MeldingRegistry() {
		registry = new HashMap<>();
	}

	public static MeldingRegistry getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MeldingRegistry();
		return INSTANCE;
	}

	public void register(Melding recipe) {
		if (recipe.getRegistryName() != null) {
			registry.put(recipe.getRegistryName(), recipe);
			KingdomKeys.LOGGER.debug("Successfully registered synthesis recipe {}", recipe.getRegistryName());
		} else {
			KingdomKeys.LOGGER.error("Cannot register Synthesis Recipe with no registry name");
		}
	}

	public List<ResourceLocation> getRecipesOfType(String type) {
		return registry.entrySet().stream().filter(resourceLocationRecipeEntry -> resourceLocationRecipeEntry.getValue().getType().equals(type)).map(Map.Entry::getKey).toList();
	}

	public Melding getValue(ResourceLocation key) {
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
		KingdomKeys.LOGGER.debug("Melding Recipe registry cleared");
	}

	public Map<ResourceLocation, Melding> getRegistry() {
		return registry;
	}

	public List<Melding> getValues() {
		return new LinkedList<>(registry.values());
	}

	public void setRegistry(Map<ResourceLocation, Melding> registry) {
		this.registry = registry;
	}
}
