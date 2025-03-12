package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class Recipe implements INBTSerializable<CompoundTag> {
    @Nullable Map<Item, Integer> materials;
    @Nullable Item result;
    @Nullable int amount;
    @Nullable String type;
    @Nullable int cost;
    @Nullable int tier;
   
    ResourceLocation registryName;

    public Recipe() {

    }

    public Recipe(Map<Item, Integer> materials, int cost, Item result, int amount, String type) {
		this.materials = materials;
		this.result = result;
		this.amount = amount;
		this.type = type;
		this.cost = cost;
	}
    
    public String getType() {
    	return type;
    }

    public void setType(String type) {
    	this.type = type;
    }
    
    public Map<Item, Integer> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<Item, Integer> materials) {
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
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		nbt.putString("regname", getRegistryName().toString());
		nbt.putString("result", ForgeRegistries.ITEMS.getKey(result).toString());
		nbt.putInt("amount", amount);
		nbt.putInt("cost", cost);
		nbt.putInt("tier", tier);
		nbt.putString("type", getType());
		nbt.putInt("ingredients_size", materials.entrySet().size());
		AtomicInteger i = new AtomicInteger();
		materials.entrySet().forEach((entry)-> {
			nbt.putString("ingredient_material_" + i, ForgeRegistries.ITEMS.getKey(entry.getKey()).toString());
			nbt.putInt("ingredient_amount_" + i, entry.getValue());
			i.getAndIncrement();
		});
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("result"))), nbt.getInt("amount"));
		this.setType(nbt.getString("type"));
		this.setCost(nbt.getInt("cost"));
		this.setTier(nbt.getInt("tier"));
		Map<Item, Integer> ingredients = new HashMap<>();
		for (int i = 0; i < nbt.getInt("ingredients_size"); i++) {
			ingredients.put(ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("ingredient_material_" + i))), nbt.getInt("ingredient_amount_" + i));
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
