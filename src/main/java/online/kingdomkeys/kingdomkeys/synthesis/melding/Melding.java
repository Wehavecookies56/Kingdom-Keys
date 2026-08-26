package online.kingdomkeys.kingdomkeys.synthesis.melding;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.jetbrains.annotations.Nullable;

public class Melding {
	public static final StreamCodec<FriendlyByteBuf, Melding> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.COMPOUND_TAG, Melding::serializeNBT, Melding::new);
	@Nullable Item ingredient1, ingredient2;
	@Nullable Item result;
	int amount;
	@Nullable Item bonusResult;
	int bonusAmount;
	@Nullable String type;
	int cost;
	int tier;
	int bonusChance;
	ResourceLocation registryName;

	public Melding() {
	}

	public Melding(CompoundTag tag) {
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

	public void setIngredient1(Item ingredient1) {
		this.ingredient1 = ingredient1;
	}

	public Item getIngredient2() {
		return ingredient2;
	}

	public void setIngredient2(Item ingredient2) {
		this.ingredient2 = ingredient2;
	}

	public Item getResult() {
		return result;
	}

	public Item getBonusResult() {
		return bonusResult;
	}

	public void setResult(Item result, int amount) {
		this.result = result;
		this.amount = amount;
	}

	public void setBonusResult(Item result, int amount, int chance) {
		this.bonusResult = result;
		this.bonusAmount = amount;
		this.bonusChance = chance;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(int amount) {
		this.bonusAmount = amount;
	}

	public int getBonusChance() {
		return bonusChance;
	}

	public void setBonusChance(int bonusChance) {
		this.bonusChance = bonusChance;
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

		if (bonusResult != null) {
			nbt.putString("bonus_result", BuiltInRegistries.ITEM.getKey(bonusResult).toString());
			nbt.putInt("bonus_amount", bonusAmount);
			nbt.putInt("bonus_chance", bonusChance);
		}

		nbt.putInt("cost", cost);

		nbt.putInt("tier", tier);
		nbt.putString("type", getType());

		nbt.putString("ingredient_material_1", BuiltInRegistries.ITEM.getKey(ingredient1).toString());
		nbt.putString("ingredient_material_2", BuiltInRegistries.ITEM.getKey(ingredient2).toString());

		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.setResult(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("result"))), nbt.getInt("amount"));

		if (nbt.contains("bonus_result")) {
			this.setBonusResult(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("bonus_result"))), nbt.getInt("bonus_amount"), nbt.getInt("bonus_chance"));
		}

		this.setType(nbt.getString("type"));
		this.setCost(nbt.getInt("cost"));

		this.setTier(nbt.getInt("tier"));
		this.setIngredient1(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("ingredient_material_1"))));
		this.setIngredient2(BuiltInRegistries.ITEM.get(KingdomKeys.rl(nbt.getString("ingredient_material_2"))));
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

	public boolean hasBonus() {
		return bonusResult != null;
	}
}
