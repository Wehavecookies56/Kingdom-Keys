package online.kingdomkeys.kingdomkeys.integration.epicfight;

import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum EpicKKWeaponEnum implements WeaponCategory {
    ETHEREAL_BLADE, ARROW_GUNS, LANCE, KK_SHIELD, AXE_SWORD, LEXICON, CLAYMORE, CHAKRAM, SITAR, CARD, SCYTHE, KNIVES, KEYBLADE_ROXAS;
    private final int id;
    EpicKKWeaponEnum() {
        System.out.println("this a test and the enums were added");
        this.id =  CapabilityItem.WeaponCategories.ENUM_MANAGER.assign(this);
    }
    @Override
    public int universalOrdinal() {
        return id;
    }
}
