package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

//TODO Should be an API thing

/**
 * Material for synthesis and keyblade forge or anything else that could use materials
 * Uses the forge registry so registry name follows the same format as every other registry item
 * Very simple class right now as the
 */
public class Material extends ForgeRegistryEntry<Material> {

    Item material;

    public Material(Item material) {
        this.material = material;
    }

    public Material(String registryName, Item material) {
        this(material);
        setRegistryName(registryName);
    }
    
    public String getMaterialName() {
    	return material.getRegistryName().toString();
    }

}