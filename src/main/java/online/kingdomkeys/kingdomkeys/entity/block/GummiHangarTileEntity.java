package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

	private ItemStack displayStack = ItemStack.EMPTY;

	public GummiHangarTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_GUMMI_HANGAR.get(), pos, state);
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
				super.onContentsChanged(slot);
			}
		};
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		/*CompoundTag invCompound = compound.getCompound("inv");
		itemStackHandler.deserializeNBT(provider, invCompound);
		//CompoundNBT transformations = compound.getCompound("transforms");
		displayStack = ItemStack.parse(provider, compound.getCompound("display_stack")).get();*/
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		//compound.put("inv", itemStackHandler.serializeNBT(provider));
		//CompoundNBT transformations = new CompoundNBT();
		//compound.put("display_stack", displayStack.save(provider));
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.gummi_editor");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
		return new GummiHangarMenu(windowID, playerInventory, this);
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

	int ticks = 0;

	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		GummiHangarTileEntity TE = (GummiHangarTileEntity) blockEntity;
		Direction facing = state.getValue(GummiHangarBlock.FACING);
		TE.ticks++;

		/*
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 7; y++) {
				for (int z = 0; z < 7; z++) {
					boolean borderX = (x == 0 || x == 6);
					boolean borderY = (y == 0 || y == 6);
					boolean borderZ = (z == 0 || z == 6);

					if ((borderX && borderY) || (borderX && borderZ) || (borderY && borderZ)) {
						switch (facing) {
							case NORTH -> level.addParticle(new DustParticleOptions(new Vector3f(1, 1, 1), 1),pos.getX() + 0.5 - 3 + x,pos.getY() + y + 0.5,pos.getZ() + 1.5 + z,0, 0, 0);
							case SOUTH -> level.addParticle(new DustParticleOptions(new Vector3f(1, 1, 1), 1),pos.getX() + 0.5 - 3 + x,pos.getY() + y + 0.5,pos.getZ() - 0.5 - z,0, 0, 0);
							case EAST -> level.addParticle(new DustParticleOptions(new Vector3f(1, 1, 1), 1),pos.getX() - 0.5 - 6 + x,pos.getY() + y + 0.5,pos.getZ() + 0.5 + 3 - z,0, 0, 0);
							case WEST -> level.addParticle(new DustParticleOptions(new Vector3f(1, 1, 1), 1),pos.getX() - 0.5 + 2 + x,pos.getY() + y + 0.5,pos.getZ() + 0.5 + 3 - z,0, 0, 0);
						}
					}
				}
			}
		}
		 */


	}
	
}