package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class Recipe {
    @Nullable Map<Item, Integer> materials;
    @Nullable Item result;
    int amount;
    @Nullable String type;
    int cost;
    int tier;
	int exp;
   
    ResourceLocation registryName;

    public Recipe() {
		exp = -1;
    }

	public Recipe(CompoundTag tag) {
		exp = -1;
		deserializeNBT(tag);
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

	public int getExp() {
		return this.exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
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

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		nbt.putString("regname", getRegistryName().toString());
		nbt.putString("result", BuiltInRegistries.ITEM.getKey(result).toString());
		nbt.putInt("amount", amount);
		nbt.putInt("cost", cost);
		if (exp >= 0) {
			nbt.putInt("exp", exp);
		}
		nbt.putInt("tier", tier);
		nbt.putString("type", getType());
		nbt.putInt("ingredients_size", materials.entrySet().size());
		AtomicInteger i = new AtomicInteger();
		materials.entrySet().forEach((entry)-> {
			nbt.putString("ingredient_material_" + i, BuiltInRegistries.ITEM.getKey(entry.getKey()).toString());
			nbt.putInt("ingredient_amount_" + i, entry.getValue());
			i.getAndIncrement();
		});
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("result"))), nbt.getInt("amount"));
		this.setType(nbt.getString("type"));
		this.setCost(nbt.getInt("cost"));
		if (nbt.contains("exp")) {
			this.setExp(nbt.getInt("exp"));
		}
		this.setTier(nbt.getInt("tier"));
		Map<Item, Integer> ingredients = new HashMap<>();
		for (int i = 0; i < nbt.getInt("ingredients_size"); i++) {
			ingredients.put(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("ingredient_material_" + i))), nbt.getInt("ingredient_amount_" + i));
		}
		this.setMaterials(ingredients);
		this.setRegistryName(nbt.getString("regname"));
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}
	public void setRegistryName(String registryName) {
		this.registryName = KingdomKeys.rl(registryName);
	}

	public void setRegistryName(ResourceLocation registryName) {
		this.registryName = registryName;
	}

	public static final StreamCodec<FriendlyByteBuf, Recipe> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			Recipe::serializeNBT,
			Recipe::new
	);
}
