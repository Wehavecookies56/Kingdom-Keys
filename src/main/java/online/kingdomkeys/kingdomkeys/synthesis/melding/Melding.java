package online.kingdomkeys.kingdomkeys.synthesis.melding;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class Melding {
	@Nullable Item ingredient1, ingredient2;
	@Nullable Item result;
	int amount;
	@Nullable String type;
	int cost;
	int tier;
	int exp;

	ResourceLocation registryName;

	public Melding() {
		exp = -1;
	}

	public Melding(CompoundTag tag) {
		exp = -1;
		deserializeNBT(tag);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Item getIngredient1() {
		return ingredient1;
	}
	public Item getIngredient2() {
		return ingredient2;
	}

	public void setIngredient1(Item ingredient1) {
		this.ingredient1 = ingredient1;
	}
	public void setIngredient2(Item ingredient2) {
		this.ingredient2 = ingredient2;
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

		nbt.putString("ingredient_material_1", BuiltInRegistries.ITEM.getKey(ingredient1).toString());
		nbt.putString("ingredient_material_2", BuiltInRegistries.ITEM.getKey(ingredient2).toString());
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString("result"))), nbt.getInt("amount"));
		this.setType(nbt.getString("type"));
		this.setCost(nbt.getInt("cost"));
		if (nbt.contains("exp")) {
			this.setExp(nbt.getInt("exp"));
		}
		this.setTier(nbt.getInt("tier"));

		this.setIngredient1(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString("ingredient_material_1"))));
		this.setIngredient2(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString("ingredient_material_2"))));

		this.setRegistryName(nbt.getString("regname"));
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}
	public void setRegistryName(String registryName) {
		this.registryName = ResourceLocation.parse(registryName);
	}

	public void setRegistryName(String namespace, String path) {
		this.registryName = ResourceLocation.fromNamespaceAndPath(namespace, path);
	}

	public void setRegistryName(ResourceLocation registryName) {
		this.registryName = registryName;
	}

	public static final StreamCodec<FriendlyByteBuf, Melding> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			Melding::serializeNBT,
			Melding::new
	);
}
