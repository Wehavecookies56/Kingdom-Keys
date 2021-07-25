package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ObjectHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class SynthesisBagContainer extends AbstractContainerMenu {

	@ObjectHolder(KingdomKeys.MODID + ":synthesis_bag")
	public static MenuType<SynthesisBagContainer> TYPE;

	public static SynthesisBagContainer fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
		InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new SynthesisBagContainer(windowId, inv, inv.player.getItemInHand(hand));
	}
	
	private final ItemStack bag;

	public SynthesisBagContainer(int windowId, Inventory playerInv, ItemStack bag) {
		super(TYPE, windowId);
		this.bag = bag;
		int i;
		int j;

		IItemHandlerModifiable bagInv = (IItemHandlerModifiable) bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

		CompoundTag nbt = playerInv.getSelected().getOrCreateTag();
		int bagLevel = nbt.getInt("level");
		
		int invStart = 0;
				
		if(bagLevel == 1) {
			invStart = 2; 
		} else if(bagLevel == 2) {
			invStart = 4;
		}
		
		//Bag inventory slots
		for (i = 0; i < 2*(bagLevel+1); ++i) {
			for (j = 0; j < 9; ++j) {
				int k = j + i * 9;
				addSlot(new SynthesisBagSlot(bagInv, k, 8 + j * 18,18+i * 18));
			}
		}
		
		//Player Inventory slots	
		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 41 + 17 + (i+invStart) * 18));
			}
		}
		
		//Player hotbar slots
		for (i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInv, i, 8 + i * 18, 45 + 17 + (3+invStart) * 18));
		}

     }
    
    @Override
    public boolean stillValid (Player player) {
        return true;
    }
    
    @Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		
		CompoundTag nbt = bag.getOrCreateTag();
		int bagLevel = nbt.getInt("level");
		int maxSlots = 0;
		switch(bagLevel) {
		case 0:
			maxSlots = 18;
			break;
		case 1:
			maxSlots = 36;
			break;
		case 2:
			maxSlots = 54;
			break;
		}
		
		Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

			if (index < maxSlots) {
				if (!this.moveItemStackTo(itemstack1, maxSlots, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, maxSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}

    @Override
    public void clicked(int slot, int dragType, ClickType clickTypeIn, Player player) {
        if (slot >= 0 && getSlot(slot).getItem() == player.getItemInHand(InteractionHand.MAIN_HAND))
        	return;
        super.clicked(slot, dragType, clickTypeIn, player);
    }
    
  /*  @Override
    public void onContainerClosed(PlayerEntity playerIn) {
		IItemHandlerModifiable bagInv = (IItemHandlerModifiable) playerIn.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if(!playerIn.world.isRemote) {
			PacketHandler.sendTo(new SCSyncSynthBagToClientPacket(bagInv), (ServerPlayerEntity)playerIn);
		}
    	super.onContainerClosed(playerIn);
    }*/

   /* @Override
    protected boolean mergeItemStack (ItemStack stack, int start, int end, boolean backwards) {
        boolean flag1 = false;
        int k = (backwards ? end - 1 : start);
        Slot slot;
        ItemStack itemstack1;

        if (stack.isStackable()) while (stack.getCount() > 0 && (!backwards && k < end || backwards && k >= start)) {
            slot = inventorySlots.get(k);
            itemstack1 = slot.getStack();

            if (!slot.isItemValid(stack)) {
                k += (backwards ? -1 : 1);
                continue;
            }

            if (!ItemStack.areItemStacksEqual(itemstack1, ItemStack.EMPTY) && itemstack1.getItem() == stack.getItem()) {
                int l = itemstack1.getCount() + stack.getCount();

                if (l <= stack.getMaxStackSize() && l <= slot.getSlotStackLimit()) {
                    stack.setCount(0);
                    itemstack1.setCount(1);
                    flag1 = true;
                } else if (itemstack1.getCount() < stack.getMaxStackSize() && l < slot.getSlotStackLimit()) {
                    stack.setCount(stack.getCount() - (stack.getMaxStackSize() - itemstack1.getCount()));
                    itemstack1.setCount(stack.getMaxStackSize());
                    flag1 = true;
                }
            }

            k += (backwards ? -1 : 1);
        }

        if (stack.getCount() > 0) {
            k = (backwards ? end - 1 : start);

            while (!backwards && k < end || backwards && k >= start) {
                slot = inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (!slot.isItemValid(stack)) {
                    k += (backwards ? -1 : 1);
                    continue;
                }

                if (ItemStack.areItemStacksEqual(itemstack1, ItemStack.EMPTY)) {
                    int l = stack.getCount();

                    if (l <= slot.getSlotStackLimit()) {
                        slot.putStack(stack.copy());
                        stack.setCount(0);
                        flag1 = true;
                        break;
                    } else {
                        putStackInSlot(k, new ItemStack(stack.getItem(), slot.getSlotStackLimit()));
                        stack.setCount(stack.getCount() - slot.getSlotStackLimit());
                        flag1 = true;
                    }
                }

                k += (backwards ? -1 : 1);
            }
        }

        return flag1;
    }*/
}