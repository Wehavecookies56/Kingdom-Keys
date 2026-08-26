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
            itemCallback.add(KingdomKeys.rl("arrowgun"), OrganizationWeaponItemBuilder.Arrowgun.class, OrganizationWeaponItemBuilder.Arrowgun::new);
            itemCallback.add(KingdomKeys.rl("axe_sword"), OrganizationWeaponItemBuilder.AxeSword.class, OrganizationWeaponItemBuilder.AxeSword::new);
            itemCallback.add(KingdomKeys.rl("card"), OrganizationWeaponItemBuilder.Card.class, OrganizationWeaponItemBuilder.Card::new);
            itemCallback.add(KingdomKeys.rl("chakram"), OrganizationWeaponItemBuilder.Chakram.class, OrganizationWeaponItemBuilder.Chakram::new);
            itemCallback.add(KingdomKeys.rl("claymore"), OrganizationWeaponItemBuilder.Claymore.class, OrganizationWeaponItemBuilder.Claymore::new);
            itemCallback.add(KingdomKeys.rl("ethereal_blade"), OrganizationWeaponItemBuilder.EtherealBlade.class, OrganizationWeaponItemBuilder.EtherealBlade::new);
            itemCallback.add(KingdomKeys.rl("knife"), OrganizationWeaponItemBuilder.Knife.class, OrganizationWeaponItemBuilder.Knife::new);
            itemCallback.add(KingdomKeys.rl("lance"), OrganizationWeaponItemBuilder.Lance.class, OrganizationWeaponItemBuilder.Lance::new);
            itemCallback.add(KingdomKeys.rl("lexicon"), OrganizationWeaponItemBuilder.Lexicon.class, OrganizationWeaponItemBuilder.Lexicon::new);
            itemCallback.add(KingdomKeys.rl("shield"), OrganizationWeaponItemBuilder.Shield.class, OrganizationWeaponItemBuilder.Shield::new);
            itemCallback.add(KingdomKeys.rl("scythe"), OrganizationWeaponItemBuilder.Scythe.class, OrganizationWeaponItemBuilder.Scythe::new);
            itemCallback.add(KingdomKeys.rl("sitar"), OrganizationWeaponItemBuilder.Sitar.class, OrganizationWeaponItemBuilder.Sitar::new);
        });
    }
}