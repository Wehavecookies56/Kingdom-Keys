package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.List;

public class UnionApprenticeArmorItem extends BaseArmorItem {
	public static final int DEFAULT_PRIMARY_COLOR = 0xFFFFFF;
	public static final int DEFAULT_SECONDARY_COLOR = 0xFFFFFF;
	public static final int MIN_DESIGN = 1;
	public static final int MAX_DESIGN = 2;

	public UnionApprenticeArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type slot) {
		super(material, slot, "apprentices/apprentice_" + MIN_DESIGN + "/");
	}

	public static int clampDesign(int design) {
		if (design < MIN_DESIGN || design > MAX_DESIGN) {
			return MIN_DESIGN;
		}
		return design;
	}

	public int getDesign(ItemStack stack) {
		Integer design = stack.get(ModComponents.APPRENTICE_DESIGN);
		return design == null ? MIN_DESIGN : clampDesign(design);
	}

	public int getPrimaryColor(ItemStack stack) {
		Integer color = stack.get(ModComponents.APPRENTICE_PRIMARY_COLOR);
		return color == null ? DEFAULT_PRIMARY_COLOR : (color & 0xFFFFFF);
	}

	public int getSecondaryColor(ItemStack stack) {
		Integer color = stack.get(ModComponents.APPRENTICE_SECONDARY_COLOR);
		return color == null ? DEFAULT_SECONDARY_COLOR : (color & 0xFFFFFF);
	}

	public boolean isDyed(ItemStack stack) {
		return stack.has(ModComponents.APPRENTICE_PRIMARY_COLOR) || stack.has(ModComponents.APPRENTICE_SECONDARY_COLOR);
	}

	public void clearColors(ItemStack stack) {
		stack.remove(ModComponents.APPRENTICE_PRIMARY_COLOR);
		stack.remove(ModComponents.APPRENTICE_SECONDARY_COLOR);
	}

	public void setDesign(ItemStack stack, int design) {
		stack.set(ModComponents.APPRENTICE_DESIGN, clampDesign(design));
	}

	public void setPrimaryColor(ItemStack stack, int color) {
		stack.set(ModComponents.APPRENTICE_PRIMARY_COLOR, color & 0xFFFFFF);
	}

	public void setSecondaryColor(ItemStack stack, int color) {
		stack.set(ModComponents.APPRENTICE_SECONDARY_COLOR, color & 0xFFFFFF);
	}

	@Override
	public String getTextureName() {
		return folder(MIN_DESIGN);
	}

	public String getTextureName(ItemStack stack) {
		return folder(getDesign(stack));
	}

	private static String folder(int design) {
		return "apprentices/apprentice_" + design + "/";
	}

	/**
	 * Transparent sheet so vanilla draws nothing; all visible layers come from ClothArmorOverlayRenderer
	 * (fixed base + overlay1 primary + overlay2 secondary).
	 */
	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return KingdomKeys.rl("textures/models/armor/empty_armor.png");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		int design = getDesign(stack);
		int primary = getPrimaryColor(stack);
		int secondary = getSecondaryColor(stack);

		tooltip.add(Component.translatable("tooltip.kingdomkeys.apprentice.design", design).withStyle(ChatFormatting.GRAY));

		tooltip.add(Component.translatable("tooltip.kingdomkeys.apprentice.primary_color").withStyle(ChatFormatting.GRAY).append(Component.literal(": ")).append(Component.literal(String.format("#%06X", primary)).withStyle(style -> style.withColor(primary))));
		tooltip.add(Component.translatable("tooltip.kingdomkeys.apprentice.secondary_color").withStyle(ChatFormatting.GRAY).append(Component.literal(": ")).append(Component.literal(String.format("#%06X", secondary)).withStyle(style -> style.withColor(secondary))));

		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

}

