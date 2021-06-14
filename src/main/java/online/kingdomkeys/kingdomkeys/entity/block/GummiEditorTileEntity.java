package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.container.GummiEditorContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class GummiEditorTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
	public static final int NUMBER_OF_SLOTS = 1;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

	private ItemStack displayStack = ItemStack.EMPTY;

	public GummiEditorTileEntity() {
		super(ModEntities.TYPE_GUMMI_EDITOR.get());
	}

	private IItemHandler createInventory() {
		return new ItemStackHandler(NUMBER_OF_SLOTS) {
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return true; //stack.getItem() instanceof KeybladeItem;
			}
		};
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expand(0, 5, 0);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		CompoundNBT invCompound = compound.getCompound("inv");
		inventory.ifPresent(iih -> ((INBTSerializable<CompoundNBT>) iih).deserializeNBT(invCompound));
		//CompoundNBT transformations = compound.getCompound("transforms");
		displayStack = ItemStack.read(compound.getCompound("display_stack"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		inventory.ifPresent(iih -> {
			CompoundNBT invCompound = ((INBTSerializable<CompoundNBT>) iih).serializeNBT();
			compound.put("inv", invCompound);
		});
		//CompoundNBT transformations = new CompoundNBT();
		compound.put("display_stack", displayStack.serializeNBT());
		return compound;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.gummi_editor");
	}

	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new GummiEditorContainer(windowID, playerInventory, this);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	public ItemStack getDisplayStack() {
		return displayStack;
	}

	public void setDisplayStack(ItemStack displayStack) {
		this.displayStack = displayStack;
		markDirty();
	}

	private int ticksExisted;
	public int previousTicks;

	public int ticksExisted() {
		return ticksExisted;
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

	@Override
	public void tick() {
		
	}
	
}