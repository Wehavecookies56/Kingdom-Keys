package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.container.MagicalChestContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MagicalChestTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 36;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

	//Used for opening
	private UUID keyblade;
	//Used to prevent others from breaking
	private UUID owner;

	public MagicalChestTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_MAGICAL_CHEST.get(), pos, state);
	}

	private IItemHandler createInventory() {
		return new ItemStackHandler(NUMBER_OF_SLOTS);
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
	protected void saveAdditional(CompoundTag compound) {
		inventory.ifPresent(iih -> {
			CompoundTag invCompound = ((INBTSerializable<CompoundTag>) iih).serializeNBT();
			compound.put("inv", invCompound);
		});
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
	public void load(CompoundTag compound) {
		super.load(compound);
		CompoundTag invCompound = compound.getCompound("inv");
		inventory.ifPresent(iih -> ((INBTSerializable<CompoundTag>) iih).deserializeNBT(invCompound));

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
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		if (tag != null) {
			load(tag);
		}
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.magical_chest");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
		return new MagicalChestContainer(windowId, playerInventory, this);
	}
}