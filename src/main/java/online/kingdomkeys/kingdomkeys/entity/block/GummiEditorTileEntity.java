package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.container.GummiEditorContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class GummiEditorTileEntity extends BlockEntity implements MenuProvider, TickingBlockEntity {
	public static final int NUMBER_OF_SLOTS = 1;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

	private ItemStack displayStack = ItemStack.EMPTY;

	public GummiEditorTileEntity(BlockPos blockPos, BlockState blockState) {
		super(ModEntities.TYPE_GUMMI_EDITOR.get(), blockPos, blockState);
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
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expandTowards(0, 5, 0);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load( compound);
		CompoundTag invCompound = compound.getCompound("inv");
		inventory.ifPresent(iih -> ((INBTSerializable<CompoundTag>) iih).deserializeNBT(invCompound));
		//CompoundNBT transformations = compound.getCompound("transforms");
		displayStack = ItemStack.of(compound.getCompound("display_stack"));
	}
	
	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		inventory.ifPresent(iih -> {
			CompoundTag invCompound = ((INBTSerializable<CompoundTag>) iih).serializeNBT();
			compound.put("inv", invCompound);
		});
		//CompoundNBT transformations = new CompoundNBT();
		compound.put("display_stack", displayStack.serializeNBT());
		return compound;
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("container.gummi_editor");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
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
		setChanged();
	}

	private int ticksExisted;
	public int previousTicks;

	public int ticksExisted() {
		return ticksExisted;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		this.save(nbt);
		return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.save(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load( tag);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public BlockPos getPos() {
		return this.getBlockPos();
	}

}