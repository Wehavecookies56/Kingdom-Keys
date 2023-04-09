package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

/**
 * Stores the data loaded from the organization datapack
 */
public class OrganizationData {

    int baseStrength, baseMagic;
    float reach;
    String description;
    String[] abilities;
    
    public OrganizationData() {

    }

    public OrganizationData(String description, int baseStrength, int baseMagic, float reach, List<String> abilities) {
        this.description = description;
        this.baseStrength = baseStrength;
        this.baseMagic = baseMagic;
        this.abilities = (String[]) abilities.toArray();
    }

    //Returns the base strength if level is 0
    public int getStrength() {
        return baseStrength;
    }

    //Returns the base magic if level is 0
    public int getMagic() {
        return baseMagic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBaseMagic(int baseMagic) {
        this.baseMagic = baseMagic;
    }

    public void setBaseStrength(int baseStrength) {
        this.baseStrength = baseStrength;
    }
    
    public float getReach() {
        return reach;
    }
    
    public void setReach(float reach) {
        this.reach = reach;
    }
    
    public String[] getAbilities() {
        return abilities;
    }
    
    public void setAbilities(String[] strings) {
        this.abilities = strings;
    }
    
}
