package online.kingdomkeys.kingdomkeys.item;

import com.google.common.base.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public enum KKArmorMaterial implements ArmorMaterial {
	ORGANIZATION(KingdomKeys.MODID + ":organization", 5, new int[] { 4, 7, 6, 3 }, 420, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 1F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	XEMNAS(KingdomKeys.MODID + ":xemnas", 5, new int[] { 4, 7, 6, 3 }, 420, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 1F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ANTICOAT(KingdomKeys.MODID + ":anticoat", 5, new int[] { 4, 7, 6, 3 }, 420, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 1F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	KEYBLADE(KingdomKeys.MODID + ":keyblade", 5, new int[] { 6, 10, 8, 6 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	/*TERRA(KingdomKeys.MODID + ":terra", 5, new int[] { 7, 9, 11, 7 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	AQUA(KingdomKeys.MODID + ":aqua", 5, new int[] { 7, 9, 11, 7 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	VENTUS(KingdomKeys.MODID + ":ventus", 5, new int[] { 7, 9, 11, 7 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	NIGHTMAREVEN(KingdomKeys.MODID + ":nightmareventus", 5, new int[] { 7, 9, 11, 7 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ERAQUS(KingdomKeys.MODID + ":eraqus", 5, new int[] { 7, 9, 11, 7 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	XEHANORT(KingdomKeys.MODID + ":xehanort", 5, new int[] { 7, 9, 11, 7 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),*/
	VANITAS(KingdomKeys.MODID + ":vanitas", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	VANITASREMNANT(KingdomKeys.MODID + ":vanitas_remnant", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	DARKRIKU(KingdomKeys.MODID + ":dark_riku", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ACED(KingdomKeys.MODID + ":aced", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	AVA(KingdomKeys.MODID + ":ava", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	GULA(KingdomKeys.MODID + ":gula", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	INVI(KingdomKeys.MODID + ":invi", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	IRA(KingdomKeys.MODID + ":ira", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	});
	
	//private static final int[] MAX_DAMAGE_ARRAY = new int[] { 16, 16, 16, 16 };

	String name;
	//private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final Supplier<SoundEvent> soundEvent;
	private final float toughness;
	private final Supplier<Ingredient> repairMaterial;

	KKArmorMaterial(String name, int maxDamageFactor, int[] damageReduction, int enchantability, Supplier<SoundEvent> sound, float toughness, Supplier<Ingredient> repairMaterialIn) {
		this.name = name;
		//this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReduction;
		this.enchantability = enchantability;
		this.soundEvent = sound;
		this.toughness = toughness;
		this.repairMaterial = repairMaterialIn;
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type slotIn) {
		return -1;//MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type slotIn) {
		return this.damageReductionAmountArray[slotIn.ordinal()]; //TODO changed intex to ordinal for 1.19.4, might not be the best way
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.soundEvent.get();
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
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

	@Override
	public float getKnockbackResistance() {
		return 0;
	}

}
