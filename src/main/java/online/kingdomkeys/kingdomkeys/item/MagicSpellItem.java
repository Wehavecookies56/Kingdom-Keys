package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class MagicSpellItem extends Item implements IItemCategory {
	String magic;
	int level;

	public MagicSpellItem(Properties properties, String name, int level) {
		super(properties.stacksTo(1));
		this.magic = name;
		this.level = level;
	}

	public int getLevel(){
		return level;
	}

	public String getMagic() {
		return magic;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		player.displayClientMessage(Component.translatable("gui.magicspell.equip", Utils.translateToLocal(magicInstance.getTranslationKey(getLevel()))), true);
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if(!stack.has(ModComponents.MAGIC_EXP)){
			stack.set(ModComponents.MAGIC_EXP, 0);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		if(Minecraft.getInstance().player != null) {
			int maxExp = magicInstance.getMaxExp(getLevel());
			tooltip.add(Component.translatable("gui.magicspell.exp", getExp(stack), maxExp));

			tooltip.add(Component.translatable("gui.magicspell.equip").withStyle(ChatFormatting.GRAY));
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	public static int getExp(ItemStack stack) {
		return stack.getOrDefault(ModComponents.MAGIC_EXP.get(), 0);
	}

	public float getExpPercent(ItemStack stack) {
		int exp = getExp(stack);
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		int maxExp = magicInstance.getMaxExp(getLevel());
		return (float) exp / maxExp;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}

	public void setExp(ItemStack stack, int amount) {
		stack.set(ModComponents.MAGIC_EXP.get(), amount);
	}
	public void addExp(ItemStack stack, int amount) {
		stack.set(ModComponents.MAGIC_EXP.get(), stack.getOrDefault(ModComponents.MAGIC_EXP.get(), 0) + amount);
	}
}
