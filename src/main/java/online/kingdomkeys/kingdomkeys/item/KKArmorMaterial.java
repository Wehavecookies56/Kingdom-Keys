package online.kingdomkeys.kingdomkeys.item;

import com.google.common.base.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public enum KKArmorMaterial implements IArmorMaterial {
	ORGANIZATION(KingdomKeys.MODID + ":organization", 5, new int[] { 3, 6, 8, 4 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	TERRA(KingdomKeys.MODID + ":terra", 5, new int[] { 7, 9, 11, 7 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	AQUA(KingdomKeys.MODID + ":aqua", 5, new int[] { 7, 9, 11, 7 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	VENTUS(KingdomKeys.MODID + ":ventus", 5, new int[] { 7, 9, 11, 7 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ERAQUS(KingdomKeys.MODID + ":eraqus", 5, new int[] { 7, 9, 11, 7 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	VANITAS(KingdomKeys.MODID + ":vanitas", 5, new int[] { 4, 7, 9, 5 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	NIGHTMAREVEN(KingdomKeys.MODID + ":nightmareventus", 5, new int[] { 7, 9, 11, 7 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ANTICOAT(KingdomKeys.MODID + ":anticoat", 5, new int[] { 3, 6, 8, 4 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	XEMNAS(KingdomKeys.MODID + ":xemnas", 5, new int[] { 3, 6, 8, 4 }, 420, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F, () -> {
		return Ingredient.fromItems(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	});
	//private static final int[] MAX_DAMAGE_ARRAY = new int[] { 16, 16, 16, 16 };

	String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final LazyValue<Ingredient> repairMaterial;

	KKArmorMaterial(String name, int maxDamageFactor, int[] damageReduction, int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairMaterialIn) {
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReduction;
		this.enchantability = enchantability;
		this.soundEvent = sound;
		this.toughness = toughness;
		this.repairMaterial = new LazyValue<>(repairMaterialIn);
	}

	@Override
	public int getDurability(EquipmentSlotType slotIn) {
		return -1;//MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return this.repairMaterial.getValue();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

}
