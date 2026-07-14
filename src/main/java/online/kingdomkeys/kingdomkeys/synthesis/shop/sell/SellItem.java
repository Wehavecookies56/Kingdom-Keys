package online.kingdomkeys.kingdomkeys.synthesis.shop.sell;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class SellItem {
    @Nullable Item result;
    @Nullable int price;

    public SellItem() {

    }

    public SellItem(int price, Item result) {
		this.result = result;
		this.price = price;
	}

	public Item getResult() {
		return result;
	}

	public void setResult(Item result) {
		this.result = result;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putString("result", Utils.getItemRegistryName(result).toString());
		nbt.putInt("price", price);
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("result"))));
		this.setPrice(nbt.getInt("price"));
	}
}
