package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.container.MagicalChestContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MagicalChestTileEntity extends TileEntity implements INamedContainerProvider {
	public static final int NUMBER_OF_SLOTS = 36;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

	//Used for opening
	private UUID keyblade;
	//Used to prevent others from breaking
	private UUID owner;

	public MagicalChestTileEntity() {
		super(ModEntities.TYPE_MAGICAL_CHEST.get());
	}

	private IItemHandler createInventory() {
		return new ItemStackHandler(NUMBER_OF_SLOTS);
	}

	public UUID getKeyblade() {
		return keyblade;
	}

	public void setKeyblade(UUID keyblade) {
		this.keyblade = keyblade;
		markDirty();
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		markDirty();
	}

	public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        inventory.ifPresent(iih -> {
        	CompoundNBT invCompound = ((INBTSerializable<CompoundNBT>) iih).serializeNBT();
        	compound.put("inv", invCompound);
		});
        compound.putUniqueId("owner", owner);
        //Check that the UUID is not empty
        if (keyblade != null) {
			if (!keyblade.equals(new UUID(0L, 0L))) {
				compound.putUniqueId("keyblade", keyblade);
			}
		}
        return compound;
    }
	
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		CompoundNBT invCompound = compound.getCompound("inv");
	    inventory.ifPresent(iih -> ((INBTSerializable<CompoundNBT>) iih).deserializeNBT(invCompound));
	
	    owner = compound.getUniqueId("owner");
	
	    if (compound.hasUniqueId("keyblade")) {
			keyblade = compound.getUniqueId("keyblade");
		}
	}
    

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.read(state, tag);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.magical_chest");
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new MagicalChestContainer(windowId, playerInventory, this);
	}
}