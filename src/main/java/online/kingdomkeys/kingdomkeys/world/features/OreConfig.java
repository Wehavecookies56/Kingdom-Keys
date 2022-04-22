package online.kingdomkeys.kingdomkeys.world.features;

import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.List;

public class OreConfig {

    public String oreName;
    public Values values;
    public Values defaults;

    public OreConfig(String oreName, Values defaults) throws NumberFormatException {
        this.oreName = oreName;
        this.defaults = defaults;
    }

    public String toConfigString() {
        return defaults.enabled() + "," + defaults.veinSize() + "," + defaults.minHeight() + "," + defaults.maxHeight() + "," + defaults.count();
    }

    public void setFromConfig(String rawConfig) {
        List<String> config = List.of(rawConfig.split(","));
        if (config.size() != 5) {
            KingdomKeys.LOGGER.warn("Ore config for {} has too many or too few values, using default values", oreName);
            values = defaults;
        } else {
            try {
                boolean enabled = Boolean.parseBoolean(config.get(0));
                int veinSize = Integer.parseInt(config.get(1));
                int minHeight = Integer.parseInt(config.get(2));
                int maxHeight = Integer.parseInt(config.get(3));
                int count = Integer.parseInt(config.get(4));
                values = new Values(enabled, veinSize, minHeight, maxHeight, count);
            } catch (NumberFormatException e) {
                KingdomKeys.LOGGER.warn("Ore config for {} has invalid values, using default values", oreName);
                values = defaults;
            }
        }
    }

    public record Values (boolean enabled, int veinSize, int minHeight, int maxHeight, int count){}

}
