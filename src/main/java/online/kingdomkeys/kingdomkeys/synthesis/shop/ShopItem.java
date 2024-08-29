package online.kingdomkeys.kingdomkeys.synthesis.shop;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class ShopItem {
    @Nullable Item result;
    @Nullable int amount;
   // @Nullable String type;
    @Nullable int cost;
    @Nullable int tier;
   

    public ShopItem() {

    }

    public ShopItem(int cost, Item result, int amount) {
		this.result = result;
		this.amount = amount;
		//this.type = type;
		this.cost = cost;
	}
  /*  
    public String getType() {
    	return type;
    }

    public void setType(String type) {
    	this.type = type;
    }
    */
   

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

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		nbt.putString("result", Utils.getItemRegistryName(result).toString());
		nbt.putInt("amount", amount);
		nbt.putInt("cost", cost);
		nbt.putInt("tier", tier);
		//nbt.putString("type", getType());
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString("result"))), nbt.getInt("amount"));
		//this.setType(nbt.getString("type"));
		this.setCost(nbt.getInt("cost"));
		this.setTier(nbt.getInt("tier"));
	}
}
