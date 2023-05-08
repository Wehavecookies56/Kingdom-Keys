package online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities;

import online.kingdomkeys.kingdomkeys.integration.epicfight.init.EpicKKWeapons;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.HashMap;
import java.util.Map;

public class KKWeaponCapabilities extends WeaponCapability {

    private final Map<String, GuardObject> guardMap = new HashMap<>();

    public KKWeaponCapabilities(CapabilityItem.Builder builder) {
        super(builder);
        guardMap.put(EpicKKWeapons.EpicKKWeaponEnum.KEYBLADE.toString()+ Styles.TWO_HAND, new GuardObject(Animations.SWORD_DUAL_GUARD_HIT, Animations.COMMON_GUARD_BREAK, Animations.SWORD_DUAL_GUARD_HIT));
        guardMap.put(EpicKKWeapons.EpicKKWeaponEnum.KEYBLADE.toString()+ KKStyles.VALOR, new GuardObject(Animations.SWORD_DUAL_GUARD_HIT, Animations.COMMON_GUARD_BREAK, Animations.SWORD_DUAL_GUARD_HIT));
        guardMap.put(EpicKKWeapons.EpicKKWeaponEnum.KEYBLADE.toString()+ Styles.ONE_HAND, new GuardObject(Animations.SWORD_GUARD_HIT, Animations.COMMON_GUARD_BREAK, Animations.SWORD_GUARD_HIT));
        guardMap.put(EpicKKWeapons.EpicKKWeaponEnum.CHAKRAM.toString()+ Styles.TWO_HAND, new GuardObject(Animations.SWORD_DUAL_GUARD_HIT, Animations.COMMON_GUARD_BREAK, Animations.SWORD_DUAL_GUARD_HIT));
        guardMap.put(EpicKKWeapons.EpicKKWeaponEnum.CHAKRAM.toString()+ Styles.ONE_HAND, new GuardObject(Animations.SWORD_GUARD_HIT, Animations.COMMON_GUARD_BREAK, Animations.SWORD_GUARD_HIT));


    }

    @Override
    public StaticAnimation getGuardMotion(GuardSkill skill, GuardSkill.BlockType blockType, PlayerPatch<?> playerpatch) {
        return guardMap.get(this.getWeaponCategory().toString()+this.getStyle(playerpatch)).getGuardAnimation(blockType);
    }

    public Map<String, GuardObject> getGuardMap() {
        return guardMap;
    }

}
