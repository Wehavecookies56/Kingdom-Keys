package online.kingdomkeys.kingdomkeys.item.organization;

import online.kingdomkeys.kingdomkeys.util.Utils;

public class ClaymoreItem extends OrgWeaponItem implements IOrgWeapon {
    
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.SAIX;
    }

}
