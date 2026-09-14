package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;
import online.kingdomkeys.kingdomkeys.menu.ApprenticeClothStationMenu;
import online.kingdomkeys.kingdomkeys.util.ApprenticeDyeHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ApprenticeClothStationTileEntity extends BlockEntity implements MenuProvider {
	public static final int INPUT_SLOT = 0;
	public static final int DYE_COUNT = 8;

	public static final int PRIMARY_DYE_START = 1;
	public static final int PRIMARY_DYE_COUNT = DYE_COUNT;
	public static final int SECONDARY_DYE_START = PRIMARY_DYE_START + PRIMARY_DYE_COUNT;
	public static final int SECONDARY_DYE_COUNT = DYE_COUNT;
	public static final int OUTPUT_SLOT = SECONDARY_DYE_START + SECONDARY_DYE_COUNT;
	public static final int SLOT_COUNT = OUTPUT_SLOT + 1;

	private final ItemStackHandler itemHandler = createInventory();
	public final Lazy<IItemHandler> inventory = Lazy.of(() -> itemHandler);

	private int selectedDesign = UnionApprenticeArmorItem.MIN_DESIGN;

	public ApprenticeClothStationTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_APPRENTICE_CLOTH_STATION.get(), pos, state);
	}

	private ItemStackHandler createInventory() {
		return new ItemStackHandler(SLOT_COUNT) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if (slot == INPUT_SLOT) {
					return stack.getItem() instanceof UnionApprenticeArmorItem;
				}
				if (slot >= PRIMARY_DYE_START && slot < PRIMARY_DYE_START + PRIMARY_DYE_COUNT) {
					return stack.getItem() instanceof DyeItem;
				}
				if (slot >= SECONDARY_DYE_START && slot < SECONDARY_DYE_START + SECONDARY_DYE_COUNT) {
					return stack.getItem() instanceof DyeItem;
				}
				// Output is computed only
				return false;
			}

			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				if (slot != OUTPUT_SLOT) {
					updateResult();
				}
				if (level != null && !level.isClientSide) {
					level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
				}
			}

			@Override
			public int getSlotLimit(int slot) {
				if (slot == INPUT_SLOT || slot == OUTPUT_SLOT) {
					return 1;
				}
				return 64;
			}
		};
	}

	public int getSelectedDesign() {
		return UnionApprenticeArmorItem.clampDesign(selectedDesign);
	}

	public void setSelectedDesign(int design) {
		int clamped = UnionApprenticeArmorItem.clampDesign(design);
		if (this.selectedDesign != clamped) {
			this.selectedDesign = clamped;
			setChanged();
			updateResult();
			if (level != null && !level.isClientSide) {
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
		}
	}

	public ItemStackHandler getItemHandler() {
		return itemHandler;
	}

	public void updateResult() {
		ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);
		if (input.isEmpty() || !(input.getItem() instanceof UnionApprenticeArmorItem armor)) {
			itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
			return;
		}

		List<ItemStack> primaryDyes = collectDyes(PRIMARY_DYE_START, PRIMARY_DYE_COUNT);
		List<ItemStack> secondaryDyes = collectDyes(SECONDARY_DYE_START, SECONDARY_DYE_COUNT);

		boolean hasPrimary = ApprenticeDyeHelper.hasAnyDye(primaryDyes);
		boolean hasSecondary = ApprenticeDyeHelper.hasAnyDye(secondaryDyes);
		boolean designChanged = getSelectedDesign() != armor.getDesign(input);

		// Need at least a design change or some dyes to produce a result
		if (!hasPrimary && !hasSecondary && !designChanged) {
			itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
			return;
		}

		ItemStack result = input.copy();
		result.setCount(1);
		armor.setDesign(result, getSelectedDesign());

		int primary = armor.getPrimaryColor(input);
		int secondary = armor.getSecondaryColor(input);

		if (hasPrimary) {
			primary = ApprenticeDyeHelper.mixColors(primaryDyes, primary, true);
			armor.setPrimaryColor(result, primary);
		}
		if (hasSecondary) {
			secondary = ApprenticeDyeHelper.mixColors(secondaryDyes, secondary, true);
			armor.setSecondaryColor(result, secondary);
		}

		itemHandler.setStackInSlot(OUTPUT_SLOT, result);
	}

	private List<ItemStack> collectDyes(int start, int count) {
		List<ItemStack> list = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			list.add(itemHandler.getStackInSlot(start + i));
		}
		return list;
	}

	/**
	 * Called when the player takes the output. Consumes one of each used dye and the input.
	 */
	public void onTakeResult() {
		ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);
		if (!input.isEmpty()) {
			itemHandler.setStackInSlot(INPUT_SLOT, ItemStack.EMPTY);
		}
		consumeOneFromRange(PRIMARY_DYE_START, PRIMARY_DYE_COUNT);
		consumeOneFromRange(SECONDARY_DYE_START, SECONDARY_DYE_COUNT);
		updateResult();
	}

	private void consumeOneFromRange(int start, int count) {
		for (int i = 0; i < count; i++) {
			ItemStack stack = itemHandler.getStackInSlot(start + i);
			if (!stack.isEmpty() && stack.getItem() instanceof DyeItem) {
				stack.shrink(1);
				itemHandler.setStackInSlot(start + i, stack);
			}
		}
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.kingdomkeys.apprentice_cloth_station");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		return new ApprenticeClothStationMenu(windowId, playerInv, this);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("Inventory", itemHandler.serializeNBT(registries));
		tag.putInt("SelectedDesign", selectedDesign);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("Inventory")) {
			itemHandler.deserializeNBT(registries, tag.getCompound("Inventory"));

			// A station placed before the dye slots grew saved its old Size, and reading it back
			// shrinks the handler to match, which leaves the output slot off the end of the array
			if (itemHandler.getSlots() != SLOT_COUNT) {
				itemHandler.setSize(SLOT_COUNT);
			}
		}
		selectedDesign = UnionApprenticeArmorItem.clampDesign(tag.getInt("SelectedDesign"));
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
