package online.kingdomkeys.kingdomkeys.item.organization;

import online.kingdomkeys.kingdomkeys.util.Utils;

public interface IOrgWeapon {
    Utils.OrgMember getMember();

    void setDescription(String description);
    String getDesc();

    void setOrganizationData(OrganizationData organizationData);
    
    OrganizationData getOrganizationData();

    void setStrength(int str);
    //Get strength from the data based on level
    int getStrength();

    void setMagic(int mag);
    //Get magic from the data based on level
    int getMagic();
    
    void setAbilities(String[] abilities);
    String[] getAbilities();
}
