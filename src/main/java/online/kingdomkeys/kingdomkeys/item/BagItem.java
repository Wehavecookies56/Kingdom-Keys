package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.menu.BagMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.function.Predicate;

public class BagItem extends Item implements IItemCategory {
	Type type;

	public BagItem(Properties properties, Type type) {
		super(properties);
		this.type = type;
	}

	public Predicate<ItemStack> getValidator() {
		return switch (type) {
			case SYNTHESIS_BAG -> stack -> stack.getItem() instanceof SynthesisItem;
			case MAGICS_BAG -> stack -> stack.getItem() instanceof MagicSpellItem;
			case CARDS_BAG -> stack -> stack.getItem() instanceof MapCardItem;
			case SHOTLOCKS_BAG -> stack -> stack.getItem() instanceof ShotlockItem;
		};
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack bagStack = player.getItemInHand(hand);
		if (!bagStack.has(ModComponents.BAG_LEVEL)) {
			if(bagStack.getItem() == ModItems.cardsBag.get()) //Cards bag start on lvl 1 since it's too small and either way it's a placeholder
				bagStack.set(ModComponents.BAG_LEVEL, 1);
			else
				bagStack.set(ModComponents.BAG_LEVEL, 0);
		}

		if (!level.isClientSide) {
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new BagMenu(w, p, bagStack, getValidator()), bagStack.getHoverName());
			player.openMenu(container, buf -> {
				buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
				buf.writeEnum(type);
			});
		}
		return InteractionResultHolder.consume(bagStack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		Integer level = stack.get(ModComponents.BAG_LEVEL);
		if (level != null) {
			int bagLevel = level;
			tooltip.add(Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Status_Level) + " " + (bagLevel + 1)));
		}
		if (type == Type.MAGICS_BAG) {
			if (!Utils.hasOnlyOneBag(Minecraft.getInstance().player, Type.MAGICS_BAG)) {
				tooltip.add(Component.translatable("gui.spellsbag.complain").withStyle(ChatFormatting.RED));
			}
		}
		if (type == Type.CARDS_BAG) {
			if (!Utils.hasOnlyOneBag(Minecraft.getInstance().player, Type.CARDS_BAG)) {
				tooltip.add(Component.translatable("gui.cardsbag.complain").withStyle(ChatFormatting.RED));
			}
		}
		if (type == Type.SHOTLOCKS_BAG) {
			if (!Utils.hasOnlyOneBag(Minecraft.getInstance().player, Type.SHOTLOCKS_BAG)) {
				tooltip.add(Component.translatable("gui.shotlocksbag.complain").withStyle(ChatFormatting.RED));
			}
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.TOOL;
	}

	public enum Type {
		SYNTHESIS_BAG, MAGICS_BAG, CARDS_BAG, SHOTLOCKS_BAG
	}
}
