package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;

public class KubeJSKKPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        //super.init();
        RegistryInfo.ITEM.addType("keyblade", KeybladeItemBuilder.class, KeybladeItemBuilder::new);
        RegistryInfo.ITEM.addType("keychain", KeychainItemBuilder.class, KeychainItemBuilder::new);
    }
}