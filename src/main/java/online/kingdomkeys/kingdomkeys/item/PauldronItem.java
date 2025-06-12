package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.menu.PauldronInventory;
import online.kingdomkeys.kingdomkeys.menu.PauldronMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.List;
import java.util.UUID;

public class PauldronItem extends Item implements IItemCategory {
	String textureName;

	Item[] items;

	public PauldronItem(Properties properties, String textureName, Item[] items) {
		super(properties);
		this.textureName = textureName;
		this.items = items;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide) {
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
			MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new PauldronMenu(w, p, stack), stack.getHoverName());
			player.openMenu(container, buf -> {
				buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.has(ModComponents.ARMOR_ID)) {
			stack.set(ModComponents.ARMOR_ID, UUID.randomUUID());
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return pStack.getRarity() == Rarity.EPIC;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.EQUIPMENT;
	}

	public String getTextureName() {
		return textureName;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		PauldronInventory pauldronInventory = (PauldronInventory) stack.getCapability(Capabilities.ItemHandler.ITEM);
		if (!pauldronInventory.getStackInSlot(0).isEmpty()) {
			tooltip.add(Component.literal(Component.translatable("kingdomkeys.helmet").getString() + ": " + pauldronInventory.getStackInSlot(0).getHoverName().getString()));
		}
		if (!pauldronInventory.getStackInSlot(1).isEmpty()) {
			tooltip.add(Component.literal(Component.translatable("kingdomkeys.chestplate").getString() + ": " + pauldronInventory.getStackInSlot(1).getHoverName().getString()));
		}
		if (!pauldronInventory.getStackInSlot(2).isEmpty()) {
			tooltip.add(Component.literal(Component.translatable("kingdomkeys.leggings").getString() + ": " + pauldronInventory.getStackInSlot(2).getHoverName().getString()));
		}
		if (!pauldronInventory.getStackInSlot(3).isEmpty()) {
			tooltip.add(Component.literal(Component.translatable("kingdomkeys.boots").getString() + ": " + pauldronInventory.getStackInSlot(3).getHoverName().getString()));
		}
		if (flagIn.isAdvanced()) {
			if (stack.has(ModComponents.ARMOR_ID)) {
				tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
				tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.get(ModComponents.ARMOR_ID).toString()));
			}
		}
	}

	public Item getArmor(int slot) {
		return items[slot];
	}

	@Override
	public boolean canGrindstoneRepair(ItemStack stack) {
		return isFoil(stack);
	}
}
