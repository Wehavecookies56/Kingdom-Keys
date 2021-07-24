package online.kingdomkeys.kingdomkeys.item.organization;

import online.kingdomkeys.kingdomkeys.util.Utils;

/**
 * Created by Toby on 08/02/2017.
 */
public interface IOrgWeapon {
    public Utils.OrgMember getMember();

    public void setWeaponDescription(String description);
    public String getWeaponDescription();

    public void setOrganizationData(OrganizationData data);
    public OrganizationData getOrganizationData();

    public void setStrength(int str);
    public int getStrength();

    public void setMagic(int mag);
    public int getMagic();


}
