package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
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
import online.kingdomkeys.kingdomkeys.container.PedestalContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class PedestalTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
	public static final int NUMBER_OF_SLOTS = 1;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);


	public PedestalTileEntity() {
		super(ModEntities.TYPE_PEDESTAL.get());
	}

	private IItemHandler createInventory() {
		return new ItemStackHandler(1) {
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return true; //stack.getItem() instanceof KeybladeItem;
			}
		};
	}


	@Override
	public void read(CompoundNBT compound) {
		CompoundNBT invCompound = compound.getCompound("inv");
		inventory.ifPresent(iih -> ((INBTSerializable<CompoundNBT>) iih).deserializeNBT(invCompound));
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		inventory.ifPresent(iih -> {
			CompoundNBT invCompound = ((INBTSerializable<CompoundNBT>) iih).serializeNBT();
			compound.put("inv", invCompound);
		});
		return super.write(compound);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.pedestal");
	}

	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new PedestalContainer(windowID, playerInventory, (PedestalTileEntity)this);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	private int ticksExisted;

	public int ticksExisted() {
		return ticksExisted;
	}

	@Override
	public void tick() {
		ticksExisted++;
	}
}