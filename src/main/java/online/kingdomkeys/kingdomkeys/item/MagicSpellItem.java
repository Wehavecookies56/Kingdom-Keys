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
import online.kingdomkeys.kingdomkeys.KingdomKeys;
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
		/*if(player.getItemInHand(hand).getItem() instanceof MagicSpellItem spell){
			spell.setExp(player.getItemInHand(hand), 1800);
		}
		System.out.println("Level: " + getLocalLevel(player.getItemInHand(hand)));
		System.out.println("Local Exp: " + getLocalExp(player.getItemInHand(hand)));
		System.out.println("Percentage: " + getLocalPercent(player.getItemInHand(hand)));
		System.out.println();
*/
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
		if(Minecraft.getInstance().player != null) {
			tooltip.add(Component.translatable("gui.magicspell.lvl_short", getLocalLevel(stack)));
			tooltip.add(Component.translatable("gui.magicspell.exp", getLocalExp(stack), getLocalMaxExp()));
			tooltip.add(Component.translatable("gui.magicspell.equip").withStyle(ChatFormatting.GRAY));
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	public int getExp(ItemStack stack) {
		return stack.getOrDefault(ModComponents.MAGIC_EXP.get(), 0);
	}

	public int getMaxExp() {
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		int exp = magicInstance.getMaxExp(getLevel());
		return exp == 0 ? 1 : exp;
	}

	/**
	 * Total of levels the magic can level up
	 * @return
	 */
	public int getMaxExpLevel() {
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		if(magicInstance == null){
			KingdomKeys.LOGGER.error(magic+": magic not found");
			return 1;
		}
		int lvl = magicInstance.getMaxLocalLevel(getLevel());
		return lvl == 0 ? 1 : lvl;
	}

	/**
	 * Global experience percentage
	 * @param stack
	 * @return
	 */
	public float getExpPercent(ItemStack stack) {
		int exp = getExp(stack);
		return (float) exp / getMaxExp();
	}

	// Fully maxed
	public boolean isMaxed(ItemStack stack) {
		return getExpPercent(stack) == 1;
	}

	//Local level based on the section
	public int getLocalLevel(ItemStack stack) {
		int maxLevel = getMaxExpLevel();
		if(maxLevel <= 1) {
			return 1;
		}

		float percent = (float)getExp(stack) / (float)getMaxExp();
		int level = (int)(percent * (maxLevel - 1)) + 1;

		return Math.min(level, maxLevel);
	}

	public int getLocalExp(ItemStack stack) {
		if(getMaxExpLevel() <= 1) {
			return getMaxExp();
		}
		int expPerLevel = getMaxExp() / (getMaxExpLevel() - 1);
		int exp = getExp(stack) % expPerLevel;
		if(getExp(stack) >= getMaxExp()) {
			exp = getLocalMaxExp();
		}
		return exp;
	}

	public int getLocalMaxExp() {
		int maxLevel = getMaxExpLevel();
		if(maxLevel <= 1) {
			return getMaxExp();
		}

		return getMaxExp() / (maxLevel - 1);
	}

	public float getLocalPercent(ItemStack stack) {
		if(getMaxExpLevel() <= 1) {
			return 1;
		}
		int expPerLevel = getMaxExp() / (getMaxExpLevel() - 1);
		float perc =  (float) getLocalExp(stack) / (float)expPerLevel;
		if(getExp(stack) >= getMaxExp()) {
			perc = 1;
		}
		return perc;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}

	public void setExp(ItemStack stack, int amount) {
		stack.set(ModComponents.MAGIC_EXP.get(), amount);
	}
	public void addExp(ItemStack stack, int amount) {
		int newExp = Math.min(getExp(stack) + amount, getMaxExp());
		setExp(stack, newExp);
	}

	public boolean canMeld(ItemStack stack) {
		return getExp(stack) >= getMaxExp();
	}
}
