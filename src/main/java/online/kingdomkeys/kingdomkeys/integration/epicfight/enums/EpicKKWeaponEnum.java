package online.kingdomkeys.kingdomkeys.integration.epicfight.enums;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum EpicKKWeaponEnum implements WeaponCategory {
    KK_ETHEREAL_BLADE, KK_ARROW_GUNS, KK_LANCE, KK_SHIELD, KK_AXE_SWORD, KK_LEXICON, KK_CLAYMORE, KK_CHAKRAM, KK_SITAR, KK_CARD, KK_SCYTHE,
    KK_KNIVES, KK_KEYBLADE;
    private final int id;

    EpicKKWeaponEnum() {
        this.id = WeaponCategory.ENUM_MANAGER.assign(this);
    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}
