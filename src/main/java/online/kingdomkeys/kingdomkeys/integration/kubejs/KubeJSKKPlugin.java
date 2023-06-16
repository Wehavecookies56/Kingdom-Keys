package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.custom.SwordItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class KubeJSKKPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        //super.init();
        RegistryInfo.ITEM.addType("keyblade", KeybladeItemBuilder.class, KeybladeItemBuilder::new);
        RegistryInfo.ITEM.addType("keychain", KeychainItemBuilder.class, KeychainItemBuilder::new);
    }
}

