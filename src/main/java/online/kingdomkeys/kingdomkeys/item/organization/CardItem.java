
package online.kingdomkeys.kingdomkeys.item.organization;

import online.kingdomkeys.kingdomkeys.util.Utils;

public class CardItem extends OrgSwordItem implements IOrgWeapon {
   
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.LUXORD;
    }

}
