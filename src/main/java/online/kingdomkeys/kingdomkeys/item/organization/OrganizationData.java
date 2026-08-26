package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Stores the data loaded from the organization datapack
 */
public class OrganizationData {

    int baseStrength, baseMagic;
    float reach;
    String description;
    ResourceLocation[] abilities;
    
    public OrganizationData() {

    }

    public OrganizationData(String description, int baseStrength, int baseMagic, float reach, List<ResourceLocation> abilities) {
        this.description = description;
        this.baseStrength = baseStrength;
        this.baseMagic = baseMagic;
        this.abilities = (ResourceLocation[]) abilities.toArray();
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
    
    public ResourceLocation[] getAbilities() {
        return abilities;
    }
    
    public void setAbilities(ResourceLocation[] abilities) {
        this.abilities = abilities;
    }
    
}
