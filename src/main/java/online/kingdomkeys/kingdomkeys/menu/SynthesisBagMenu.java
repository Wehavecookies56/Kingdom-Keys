package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import online.kingdomkeys.kingdomkeys.item.ModComponents;

public class SynthesisBagMenu extends AbstractContainerMenu {

	public static SynthesisBagMenu fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new SynthesisBagMenu(windowId, inv, inv.player.getItemInHand(hand));
	}
	
	public final ItemStack bag;

	public SynthesisBagMenu(int windowId, Inventory playerInv, ItemStack bag) {
		super(ModMenus.SYNTHESIS_BAG.get(), windowId);
		this.bag = bag;
		int i;
		int j;

		SynthesisBagInventory bagInv = (SynthesisBagInventory) bag.getCapability(Capabilities.ItemHandler.ITEM);
		if (bagInv != null) {
			int bagLevel = bag.get(ModComponents.SYNTH_BAG_LEVEL);
			int invStart = bagLevel * 2;

			//Bag inventory slots
			for (i = 0; i < 2 * (bagLevel + 1); ++i) {
				for (j = 0; j < 9; ++j) {
					int k = j + i * 9;
					addSlot(new SynthesisBagSlot(bagInv, k, 8 + j * 18, 18 + i * 18));
				}
			}

			//Player Inventory slots
			for (i = 0; i < 3; ++i) {
				for (j = 0; j < 9; ++j) {
					addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 41 + 17 + (i + invStart) * 18));
				}
			}

			//Player hotbar slots
			for (i = 0; i < 9; ++i) {
				addSlot(new Slot(playerInv, i, 8 + i * 18, 45 + 17 + (3 + invStart) * 18));
			}

		}
	}
    
    @Override
    public boolean stillValid (Player player) {
        return true;
    }

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = slots.get(index);

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();
		ItemStack copy = stack.copy();

		int bagLevel = bag.get(ModComponents.SYNTH_BAG_LEVEL);
		int maxSlots = switch (bagLevel) {
			case 0 -> 18;
			case 1 -> 36;
			case 2 -> 54;
			case 3 -> 72;
			default -> 0;
		};

		// From player to bag
		if (index >= maxSlots) {
			SynthesisBagInventory inv = (SynthesisBagInventory) bag.getCapability(Capabilities.ItemHandler.ITEM);

			if (inv != null) {
				int oldCount = stack.getCount();
				for (int i = 0; i < maxSlots; i++) {
					stack = inv.insertItem(i, stack, false);

					if (stack.isEmpty())
						break;
				}

				if (stack.getCount() == oldCount) {
					return ItemStack.EMPTY;
				}
			}

			slot.set(stack);
			return copy;
		}

		// From bag to player
		if (!moveItemStackTo(stack, maxSlots, slots.size(), true))
			return ItemStack.EMPTY;

		if (stack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		return copy;
	}

	@Override
    public void clicked(int slot, int dragType, ClickType clickTypeIn, Player player) {
        if (!(slot >= 0 && ItemStack.isSameItemSameComponents(getSlot(slot).getItem(), bag))) {
			super.clicked(slot, dragType, clickTypeIn, player);
		}

    }

}