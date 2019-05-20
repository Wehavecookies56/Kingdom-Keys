package online.kingdomkeys.kingdomkeys.api;

import net.minecraftforge.registries.IForgeRegistryEntry;


public class Ability extends IForgeRegistryEntry.Impl<Ability> {

    int apCost;
    String name;
    Category category;
    
    public Ability (int apCost, String name, Category cat) {
        this.apCost = apCost;
        this.name = name;
        this.category = cat;
        
        setRegistryName(Reference.MODID+":"+name);
    }

    public int getApCost() {
        return apCost;
    }

    public String getName() {
        return name;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public enum Category{
    	ACTION,
    	GROWTH,
    	SUPPORT,
    	WEAPONS
    }
}
