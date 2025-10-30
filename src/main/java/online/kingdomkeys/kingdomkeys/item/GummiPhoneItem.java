package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			GummiStructure gummiStruct = null;
			if(stack.has(ModComponents.GUMMI_STRUCTURE)){
				gummiStruct = stack.get(ModComponents.GUMMI_STRUCTURE);
			}
			if (gummiStruct != null || gummiStruct.getBlocks().length > 0) {
				GummiShipEntity gummi = new GummiShipEntity(world, gummiStruct);
				KingdomKeys.LOGGER.debug(gummiStruct.serializeNBT(world.registryAccess()));
				gummi.setPos(player.getX(), player.getY(), player.getZ());
				world.addFreshEntity(gummi);
				stack.remove(ModComponents.GUMMI_STRUCTURE);
			}
		}
		return InteractionResultHolder.success(player.getItemInHand(hand));
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
			tooltip.add(Component.translatable(structure.getName()));
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
