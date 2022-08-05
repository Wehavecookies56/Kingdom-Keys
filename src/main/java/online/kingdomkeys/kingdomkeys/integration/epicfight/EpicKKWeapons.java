package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.Function;

public class EpicKKWeapons {
    public EpicKKWeapons() {
        EpicKKWeaponEnum e = EpicKKWeaponEnum.CHAKRAM;
    }


    //private static final Map<String, Function<Item, CapabilityItem.Builder>> PRESETS = Maps.newHashMap();

    public enum EpicKKWeaponEnum implements WeaponCategory {
        ETHEREAL_BLADE, ARROW_GUNS, LANCE, KK_SHIELD, AXE_SWORD, LEXICON, CLAYMORE, CHAKRAM, SITAR, CARD, SCYTHE, KNIVES, KEYBLADE_ROXAS;
        private final int id;
        EpicKKWeaponEnum() {
            this.id =  CapabilityItem.WeaponCategories.ENUM_MANAGER.assign(this);
        }
        @Override
        public int universalOrdinal() {
            return id;
        }
    }

    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {

        //Map<String, Function<Item, CapabilityItem.Builder>> typeEntry = Maps.newHashMap();
        final Function<Item, CapabilityItem.Builder> CHAKRAM = (item) -> WeaponCapability.builder().category((CapabilityItem.WeaponCategories)
                        CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()))
                .hitSound(EpicFightSounds.BLADE_HIT).newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.CHAKRAM_AUTO_1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()));

        event.getTypeEntry().put("chakram", CHAKRAM);
        //WeaponCapabilityPresetRegistryEvent weaponCapabilityPresetRegistryEvent = new WeaponCapabilityPresetRegistryEvent(typeEntry);
        //ModLoader.get().postEvent(weaponCapabilityPresetRegistryEvent);
        //weaponCapabilityPresetRegistryEvent.getTypeEntry().forEach(PRESETS::put);
    }
}
