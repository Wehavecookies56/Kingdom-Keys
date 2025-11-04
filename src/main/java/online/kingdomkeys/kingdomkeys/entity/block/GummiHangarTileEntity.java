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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.block.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GummiHangarTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 1;
	private final ItemStackHandler itemStackHandler = createInventory();
	public final Lazy<IItemHandler> inventory = Lazy.of(() -> itemStackHandler);
	private String lastShipName = "";

	public GummiHangarTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_GUMMI_HANGAR.get(), pos, state);
	}

	public void setLastShipName(String name) {
		this.lastShipName = name;
		setChanged();
	}

	public String getLastShipName() {
		return lastShipName;
	}

	private ItemStackHandler createInventory() {
		return new ItemStackHandler(NUMBER_OF_SLOTS) {
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return true; //stack.getItem() instanceof KeybladeItem;
			}

			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                // Remove display if blueprint is removed
                getLevel().setBlockAndUpdate(getBlockPos(),getBlockState().setValue(GummiHangarBlock.DISPLAY_BLUEPRINT,false));
				super.onContentsChanged(slot);
			}
		};
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		CompoundTag invCompound = compound.getCompound("inv");
		itemStackHandler.deserializeNBT(provider, invCompound);
		if (compound.contains("LastShipName"))
			lastShipName = compound.getString("LastShipName");
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put("inv", itemStackHandler.serializeNBT(provider));
		compound.putString("LastShipName", lastShipName);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.gummi_hangar");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
		return new GummiHangarMenu(windowID, playerInventory, this);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		loadAdditional(tag, registries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, pRegistries);
		return tag;
	}
	
}