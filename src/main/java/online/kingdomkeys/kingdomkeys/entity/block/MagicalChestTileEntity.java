package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.menu.MagicalChestMenu;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MagicalChestTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 36;
	private final ItemStackHandler itemStackHandler = createInventory();
	public final Lazy<IItemHandler> inventory = Lazy.of(() -> itemStackHandler);

	//Used for opening
	private UUID keyblade;
	//Used to prevent others from breaking
	private UUID owner;

	public MagicalChestTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_MAGICAL_CHEST.get(), pos, state);
	}

	private ItemStackHandler createInventory() {
		return new ItemStackHandler(NUMBER_OF_SLOTS) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
			}
		};
	}

	public UUID getKeyblade() {
		return keyblade;
	}

	public void setKeyblade(UUID keyblade) {
		this.keyblade = keyblade;
		setChanged();
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		compound.put("inv", itemStackHandler.serializeNBT(provider));
		//null check since BlockItem calls this method before loading the data from the stack
		if (owner != null) {
			compound.putUUID("owner", owner);
			//Check that the UUID is not empty
			if (keyblade != null) {
				if (!keyblade.equals(new UUID(0L, 0L))) {
					compound.putUUID("keyblade", keyblade);
				}
			}
		}
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		CompoundTag invCompound = compound.getCompound("inv");
		itemStackHandler.deserializeNBT(provider, invCompound);

		if (compound.hasUUID("owner")) {
			owner = compound.getUUID("owner");
		}
		if (compound.hasUUID("keyblade")) {
			keyblade = compound.getUUID("keyblade");
		}
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		loadAdditional(tag, registries);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.magical_chest");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
		return new MagicalChestMenu(windowId, playerInventory, this);
	}
}