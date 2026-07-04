package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

//tired of putting the modid everytime
public class KKResourceLocation {
    public static ResourceLocation of(String path) {
        if (path.contains(":")) {
            return ResourceLocation.parse(path);
        }
        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, path);
    }
}
