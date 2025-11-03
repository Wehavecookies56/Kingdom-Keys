package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;

import java.util.List;

public class GummiPhoneItem extends Item implements IItemCategory {
	public GummiPhoneItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		if (!world.isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			GummiStructure gummiStruct = null;
			if(stack.has(ModComponents.GUMMI_STRUCTURE)){
				gummiStruct = stack.get(ModComponents.GUMMI_STRUCTURE);
			}
			if (gummiStruct != null || gummiStruct.getBlocks().length > 0) {
				GummiShipEntity gummi = new GummiShipEntity(world, gummiStruct);
				gummiStruct.serializeNBT(world.registryAccess());
				gummi.setPos(context.getClickedPos().getX(), context.getClickedPos().getY()+1, context.getClickedPos().getZ());
				world.addFreshEntity(gummi);
				stack.remove(ModComponents.GUMMI_STRUCTURE);
			}
		}
		return InteractionResult.SUCCESS;
	}

	private void takeItem(Player player) {
		if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
			player.getMainHandItem().shrink(1);
		} else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
			player.getOffhandItem().shrink(1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> tooltip, TooltipFlag pTooltipFlag) {
		if (stack.has(ModComponents.GUMMI_STRUCTURE)) {
			GummiStructure structure = stack.get(ModComponents.GUMMI_STRUCTURE);
			tooltip.add(Component.translatable(ChatFormatting.GRAY+"Call Gummi Ship: ").append(ChatFormatting.RED+structure.getName()));
		} else {
			tooltip.add(Component.translatable(ChatFormatting.GRAY+"No Gummi Ship stored"));
			tooltip.add(Component.translatable(ChatFormatting.GRAY+"Sneak + left click your Gummi Ship to store it"));
		}
		super.appendHoverText(stack, pContext, tooltip, pTooltipFlag);
	}
	
	@Override
	public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int itemSlot, boolean isSelected) {

	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
