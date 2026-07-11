package online.kingdomkeys.kingdomkeys.synthesis.shop;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class ShopItem {
    @Nullable Item result;
    @Nullable int amount;
   // @Nullable String type;
    @Nullable int cost;
    @Nullable int tier;
	@Nullable int matReq;
	@Nullable boolean requireAll;

    public ShopItem() {

    }

    public ShopItem(int cost, Item result, int amount) {
		this.result = result;
		this.amount = amount;
		this.cost = cost;
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

	public int getMatReq() {
		return matReq;
	}

	public void setMatReq(int matReq) {
		this.matReq = matReq;
	}

	public boolean requireAll() {
		return requireAll;
	}

	public void setRequireAll(boolean requireAll) {
		this.requireAll = requireAll;
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
		nbt.putInt("mat_req", matReq);
		nbt.putBoolean("require_all", requireAll);
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString("result"))), nbt.getInt("amount"));
		this.setCost(nbt.getInt("cost"));
		this.setTier(nbt.getInt("tier"));
		this.setMatReq(nbt.getInt("mat_req"));
		this.setRequireAll(nbt.getBoolean("require_all"));
	}
}
