package online.kingdomkeys.kingdomkeys.synthesis.recipes;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class RecipeData {
    @Nullable Map<Material, Integer> materials;
    @Nullable Item result;
    @Nullable int amount;

    public RecipeData() {
    }

    public RecipeData(Map<Material, Integer> materials, Item result, int amount) {
		this.materials = materials;
		this.result = result;
		this.amount = amount;
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

  
}
