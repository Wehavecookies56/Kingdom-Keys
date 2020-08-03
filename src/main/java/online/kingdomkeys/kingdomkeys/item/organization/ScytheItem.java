package online.kingdomkeys.kingdomkeys.item.organization;

import online.kingdomkeys.kingdomkeys.lib.Utils;

public class ScytheItem extends OrgWeaponItem implements IOrgWeapon {
    private OrganizationData data;

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.MARLUXIA;
    }

}
