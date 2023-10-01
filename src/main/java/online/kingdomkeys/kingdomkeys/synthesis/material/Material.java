package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

//TODO Should be an API thing

/**
 * Material for synthesis and keyblade forge or anything else that could use materials
 * Uses the forge registry so registry name follows the same format as every other registry item
 * Very simple class right now as the
 */
public class Material {

    Supplier<Item> material;
    private ResourceLocation registryName;

    public Material(Supplier<Item> material) {
        this.material = material;
    }

    public Material(ResourceLocation registryName, Supplier<Item> material) {
        this(material);
        this.registryName = registryName;
    }
    
    public String getMaterialName() {
    	return ForgeRegistries.ITEMS.getKey(material.get()).toString();
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

}