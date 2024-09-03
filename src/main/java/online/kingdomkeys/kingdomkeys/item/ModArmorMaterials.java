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
					new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "organization_layer_1")),
					new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "organization_layer_2"))
			),
			1,
			0
	));

	public static final Holder<ArmorMaterial> KEYBLADE = ARMOR_MATERIALS.register("keyblade", () -> new ArmorMaterial(
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 6);
				map.put(ArmorItem.Type.LEGGINGS, 8);
				map.put(ArmorItem.Type.CHESTPLATE, 10);
				map.put(ArmorItem.Type.HELMET, 6);
				map.put(ArmorItem.Type.BODY, 10);
			}),
			25,
			ModSounds.keyblade_armor,
			() -> Ingredient.of(Tags.Items.INGOTS_IRON),
			List.of(
					new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade"))
			),
			3,
			0.15F
	));

	public static final Holder<ArmorMaterial> VANITY = ARMOR_MATERIALS.register("vanity", () -> new ArmorMaterial(
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 2);
				map.put(ArmorItem.Type.LEGGINGS, 5);
				map.put(ArmorItem.Type.CHESTPLATE, 7);
				map.put(ArmorItem.Type.HELMET, 3);
				map.put(ArmorItem.Type.BODY, 7);
			}),
			25,
			SoundEvents.ARMOR_EQUIP_LEATHER,
			() -> Ingredient.of(Tags.Items.LEATHERS),
			List.of(
					new ArmorMaterial.Layer(
							ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "vanity")
					)
			),
			1,
			0
	));
}
