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
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class ShotlockItem extends Item implements IItemCategory, ICreativeTab, ILevelableItem {
	ResourceLocation shotlock;

	public ShotlockItem(Properties properties, ResourceLocation name) {
		super(properties.stacksTo(1));
		this.shotlock = name;
	}

	public ResourceLocation getShotlock() {
		return shotlock;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		Shotlock shotlockInstance = ModShotlocks.registry.get(shotlock);
		player.displayClientMessage(Component.translatable("gui.shotlockitem.equip", Utils.translateToLocal(shotlockInstance.getTranslationKey())), true);
		//this.addExp(player.getMainHandItem(),100);
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (!stack.has(ModComponents.SHOTLOCK_EXP)) {
			stack.set(ModComponents.SHOTLOCK_EXP, 0);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		if (Minecraft.getInstance().player != null) {
			Shotlock shotlockInstance = ModShotlocks.registry.get(shotlock);
			if (shotlockInstance != null) {
				tooltip.add(Component.translatable("gui.shotlockitem.max_locks", shotlockInstance.getMaxLocks()));
				if (shotlockInstance.getMaxLevel() > 1) {
					tooltip.add(Component.translatable("gui.magicspell.lvl_short", getLocalLevel(stack)));
					tooltip.add(Component.translatable("gui.magicspell.exp", getLocalExp(stack), getLocalMaxExp()));
				}
			}
			tooltip.add(Component.translatable("gui.shotlockitem.equip").withStyle(ChatFormatting.GRAY));
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	public int getExp(ItemStack stack) {
		return stack.getOrDefault(ModComponents.SHOTLOCK_EXP.get(), 0);
	}

	public int getMaxExp() {
		Shotlock shotlockInstance = ModShotlocks.registry.get(shotlock);
		int exp = shotlockInstance.getMaxExp();
		return exp == 0 ? 1 : exp;
	}

	public int getMaxExpLevel() {
		Shotlock shotlockInstance = ModShotlocks.registry.get(shotlock);
		if (shotlockInstance == null) {
			KingdomKeys.LOGGER.error(shotlock + ": shotlock not found");
			return 1;
		}
		int lvl = shotlockInstance.getMaxLevel();
		return lvl == 0 ? 1 : lvl;
	}

	public float getExpPercent(ItemStack stack) {
		int exp = getExp(stack);
		return (float) exp / getMaxExp();
	}

	@Override
	public boolean isMaxed(ItemStack stack) {
		return getExpPercent(stack) == 1;
	}

	@Override
	public int getLocalLevel(ItemStack stack) {
		int maxLevel = getMaxExpLevel();
		if (maxLevel <= 1) {
			return 1;
		}

		float percent = (float) getExp(stack) / (float) getMaxExp();
		int level = (int) (percent * (maxLevel - 1)) + 1;

		return Math.min(level, maxLevel);
	}

	@Override
	public int getLocalExp(ItemStack stack) {
		if (getMaxExpLevel() <= 1) {
			return getMaxExp();
		}

		int expPerLevel = getMaxExp() / (getMaxExpLevel() - 1);
		int exp = getExp(stack) % expPerLevel;
		if (getExp(stack) >= getMaxExp()) {
			exp = getLocalMaxExp();
		}
		return exp;
	}

	@Override
	public int getLocalMaxExp() {
		int maxLevel = getMaxExpLevel();
		if (maxLevel <= 1) {
			return getMaxExp();
		}

		return getMaxExp() / (maxLevel - 1);
	}

	@Override
	public float getLocalPercent(ItemStack stack) {
		if (getMaxExpLevel() <= 1) {
			return 1;
		}
		int expPerLevel = getMaxExp() / (getMaxExpLevel() - 1);
		float perc = (float) getLocalExp(stack) / (float) expPerLevel;
		if (getExp(stack) >= getMaxExp()) {
			perc = 1;
		}
		return perc;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.SHOTLOCK;
	}

	public void setExp(ItemStack stack, int amount) {
		stack.set(ModComponents.SHOTLOCK_EXP.get(), amount);
	}

	public void addExp(ItemStack stack, int amount) {
		int newExp = Math.min(getExp(stack) + amount, getMaxExp());
		setExp(stack, newExp);
	}

	@Override
	public Tab getTab() {
		return Tab.EQUIPABLES;
	}
}
