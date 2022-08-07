package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Skills;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.Function;

public class EpicKKWeapons {
    public EpicKKWeapons() {
        EpicKKWeaponEnum e = EpicKKWeaponEnum.CHAKRAM;
    }

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

    public static final Function<Item, CapabilityItem.Builder> CHAKRAM = (item) ->
            WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()))
                .styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()) ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                .hitSound(EpicFightSounds.BLADE_HIT)
                .collider(ColliderPreset.DAGGER)
                .weaponCombinationPredicator((entityPatch) -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.ENUM_MANAGER.get(EpicKKWeaponEnum.CHAKRAM.universalOrdinal()))
                .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.CHAKRAM_AUTO_1, Animations.DAGGER_AUTO_2, Animations.DAGGER_AUTO_3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.CHAKRAM_AUTO_1, Animations.DAGGER_DUAL_AUTO_2, Animations.DAGGER_DUAL_AUTO_3, Animations.DAGGER_DUAL_AUTO_4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).specialAttack(CapabilityItem.Styles.ONE_HAND, Skills.EVISCERATE).specialAttack(CapabilityItem.Styles.TWO_HAND, Skills.BLADE_RUSH);

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put("chakram", CHAKRAM);
    }
}
