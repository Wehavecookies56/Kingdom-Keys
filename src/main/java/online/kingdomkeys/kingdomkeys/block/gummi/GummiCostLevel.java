package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;

import javax.annotation.Nullable;

public final class GummiCostLevel {
    public static int maxChips(int hangarLevel) {
        return Math.max(hangarLevel, 0) + 1;
    }

    private GummiCostLevel() {
    }

    public static int fromChips(int chips, int hangarLevel) {
        return 1 + Math.clamp(chips, 0, maxChips(hangarLevel));
    }

    // By default 400 with no chips, then 300 more per chip: 1900 at best in an XL hangar, and on for the sizes only commands give
    public static int maxCost(int level) {
        return ModConfigs.SERVER.gummiHangarBaseCost.getAsInt() + ModConfigs.SERVER.gummiHangarLevelCost.getAsInt() * (Math.max(level, 1) - 1);
    }

    @Nullable
    public static Component overLimit(GummiShipEntity.ShipStats stats, int level) {
        if (stats.cost() > maxCost(level)) {
            return Component.translatable("container.gummi_hangar.com.over_cost", stats.cost(), maxCost(level));
        }

        return null;
    }
}
