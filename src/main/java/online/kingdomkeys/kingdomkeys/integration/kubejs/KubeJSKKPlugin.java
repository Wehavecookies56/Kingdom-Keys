package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import net.minecraft.core.registries.Registries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class KubeJSKKPlugin implements KubeJSPlugin {

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        KubeJSPlugin.super.registerTypeWrappers(registry);
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.ITEM, itemCallback -> {
            itemCallback.add(KingdomKeys.rl("keyblade"), KeybladeItemBuilder.class, KeybladeItemBuilder::new);
            itemCallback.add(KingdomKeys.rl("keychain"), KeychainItemBuilder.class, KeychainItemBuilder::new);
            itemCallback.add(KingdomKeys.rl("world_card"), WorldCardItemBuilder.class, WorldCardItemBuilder::new);
            itemCallback.add(KingdomKeys.rl("map_card"), MapCardItemBuilder.class, MapCardItemBuilder::new);
        });
    }
}