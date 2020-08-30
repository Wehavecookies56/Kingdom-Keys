package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class Recipe implements INBTSerializable<CompoundNBT> {
    @Nullable Map<Material, Integer> materials;
    @Nullable Item result;
    @Nullable int amount;
    @Nullable String type;
   
    ResourceLocation registryName;

    public Recipe() {

    }

    public Recipe(Map<Material, Integer> materials, Item result, int amount, String type) {
		this.materials = materials;
		this.result = result;
		this.amount = amount;
		this.type = type;
	}
    
    public String getType() {
    	return type;
    }

    public void setType(String type) {
    	this.type = type;
    }
    
    public Map<Material, Integer> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<Material, Integer> materials) {
        this.materials = materials;
    }

	public Item getResult() {
		return result;
	}

	public void setResult(Item result, int amount) {
		this.result = result;
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();

		nbt.putString("regname", getRegistryName().toString());
		nbt.putString("result", result.getRegistryName().toString());
		nbt.putInt("amount", amount);
		nbt.putString("type", getType());
		nbt.putInt("ingredients_size", materials.entrySet().size());
		AtomicInteger i = new AtomicInteger();
		materials.entrySet().forEach((entry)-> {
			nbt.putString("ingredient_material_" + i, entry.getKey().getRegistryName().toString());
			nbt.putInt("ingredient_amount_" + i, entry.getValue());
			i.getAndIncrement();
		});
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.setResult(ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("result"))), nbt.getInt("amount"));
		this.setType(nbt.getString("type"));
		Map<Material, Integer> ingredients = new HashMap<>();
		for (int i = 0; i < nbt.getInt("ingredients_size"); i++) {
			ingredients.put(ModMaterials.registry.getValue(new ResourceLocation(nbt.getString("ingredient_material_" + i))), nbt.getInt("ingredient_amount_" + i));
		}
		this.setMaterials(ingredients);
		this.setRegistryName(nbt.getString("regname"));
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}
	public void setRegistryName(String registryName) {
		this.registryName = new ResourceLocation(registryName);
	}

	public void setRegistryName(String namespace, String path) {
		this.registryName = new ResourceLocation(namespace, path);
	}

	public void setRegistryName(ResourceLocation registryName) {
		this.registryName = registryName;
	}
}
