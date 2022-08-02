package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.Map;
import java.util.function.Function;

public class EpicKKWeapons {
    public EpicKKWeapons() {
    }

    //public static final Function<Item, CapabilityItem.Builder> CHAKRAM = (item) -> WeaponCapability.builder().category((CapabilityItem.WeaponCategories) CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()))
    //        .hitSound(EpicFightSounds.BLADE_HIT).newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.CHAKRAM_AUTO_1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
    //        .weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()));
    private static final Map<String, Function<Item, CapabilityItem.Builder>> PRESETS = Maps.newHashMap();

    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        //event.getTypeEntry().put("chakram", CHAKRAM);
    }
}
