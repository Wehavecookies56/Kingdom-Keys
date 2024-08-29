package online.kingdomkeys.kingdomkeys.item;

import com.google.common.base.Supplier;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {

	public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, KingdomKeys.MODID);

	public static final Holder<ArmorMaterial> ORGANIZATION = ARMOR_MATERIALS.register("organization", () -> new ArmorMaterial(
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 3);
				map.put(ArmorItem.Type.LEGGINGS, 6);
				map.put(ArmorItem.Type.CHESTPLATE, 7);
				map.put(ArmorItem.Type.HELMET, 4);
				map.put(ArmorItem.Type.BODY, 6);
			}),
			25,
			SoundEvents.ARMOR_EQUIP_LEATHER,
			() -> Ingredient.of(Tags.Items.LEATHERS),
			List.of(
					new ArmorMaterial.Layer(
							ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "organization")
					)
			),
			1,
			0
	));


	XEMNAS(KingdomKeys.MODID + ":xemnas", 5, new int[] { 4, 7, 6, 3 }, 420, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 1F, 0, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ANTICOAT(KingdomKeys.MODID + ":anticoat", 5, new int[] { 4, 7, 6, 3 }, 420, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 1F,0,  () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	KEYBLADE(KingdomKeys.MODID + ":keyblade", 5, new int[] { 6, 10, 8, 6 }, 420, () -> ModSounds.keyblade_armor.get(), 3F, 0.15F, () -> {
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
	VANITAS(KingdomKeys.MODID + ":vanitas", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	VANITASREMNANT(KingdomKeys.MODID + ":vanitas_remnant", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	DARKRIKU(KingdomKeys.MODID + ":dark_riku", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	ACED(KingdomKeys.MODID + ":aced", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0.05F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	AVA(KingdomKeys.MODID + ":ava", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0.05F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	GULA(KingdomKeys.MODID + ":gula", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0.05F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	INVI(KingdomKeys.MODID + ":invi", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0.05F, () -> {
		return Ingredient.of(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future.get());
	}),
	IRA(KingdomKeys.MODID + ":ira", 5, new int[] { 3, 7, 5, 2 }, 420, () -> SoundEvents.ARMOR_EQUIP_DIAMOND, 3F, 0.05F, () -> {
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
	private final float knockbackResistance;

	ModArmorMaterials(String name, int maxDamageFactor, int[] damageReduction, int enchantability, Supplier<SoundEvent> sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterialIn) {
		this.name = name;
		//this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReduction;
		this.enchantability = enchantability;
		this.soundEvent = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
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
		return this.knockbackResistance;
	}

}
