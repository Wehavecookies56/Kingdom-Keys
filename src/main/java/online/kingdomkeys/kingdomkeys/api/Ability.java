package online.kingdomkeys.kingdomkeys.api;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class Ability implements IForgeRegistryEntry<Ability> {

    int apCost;
    ResourceLocation name;
    Category category;
    
    public Ability (int apCost, String name, Category cat) {
        this.apCost = apCost;
        setRegistryName(new ResourceLocation(KingdomKeys.MODID+":"+name));
        this.category = cat;
    }

    public int getApCost() {
        return apCost;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public Ability setRegistryName(ResourceLocation resourceLocation) {
        name = resourceLocation;
        return this;
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

    @Override
    public Class<Ability> getRegistryType() {
        return Ability.class;
    }
}
