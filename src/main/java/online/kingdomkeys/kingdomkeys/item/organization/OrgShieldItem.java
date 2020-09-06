package online.kingdomkeys.kingdomkeys.item.organization;

import online.kingdomkeys.kingdomkeys.util.Utils;

public class OrgShieldItem extends OrgWeaponItem implements IOrgWeapon {

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.VEXEN;
    }

}
