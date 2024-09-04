package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.menu.PedestalMenu;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class PedestalTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 1;
	private ItemStackHandler itemStackHandler = createInventory();
	public Lazy<IItemHandler> inventory = Lazy.of(() -> itemStackHandler);

	public static final float DEFAULT_HEIGHT = 1.25F;
	public static final float DEFAULT_ROTATION = 0.0F;
	public static final float DEFAULT_ROTATION_SPEED = 0.6F;
	public static final float DEFAULT_BOB_SPEED = 0.02F;
	public static final float DEFAULT_SCALE = 1.0F;

	private boolean stationOfAwakeningMarker = false;

	private ItemStack displayStack = ItemStack.EMPTY;

	private float rotationSpeed = 0.6F;
	private float bobSpeed = 0.02F;
	private float scale = 1.0F;

	//only storing values from the TESR here so values won't be correct on the server
	private float currentRotation = 0;
	private float currentHeight = 0;

	private float savedRotation = 0.0F;
	private float savedHeight = 1.25F;

	private float baseHeight = 1.25F;

	private boolean pause = false;
	private boolean flipped = false;

	//only changed on the client so it will not hide for other players
	public boolean hide = false;

	public PedestalTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_PEDESTAL.get(), pos, state);
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

			}
		};
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		CompoundTag invCompound = compound.getCompound("inv");
		itemStackHandler.deserializeNBT(registries, invCompound);
		CompoundTag transformations = compound.getCompound("transforms");
		rotationSpeed = transformations.getFloat("rotspeed");
		bobSpeed = transformations.getFloat("bobspeed");
		savedRotation = transformations.getFloat("savedrot");
		savedHeight = transformations.getFloat("savedheight");
		scale = transformations.getFloat("scale");
		baseHeight = transformations.getFloat("baseheight");
		pause = transformations.getBoolean("pause");
		flipped = transformations.getBoolean("flipped");
		stationOfAwakeningMarker = compound.getBoolean("soa_marker");
		displayStack = ItemStack.parseOptional(registries, compound.getCompound("display_stack"));
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.put("inv", itemStackHandler.serializeNBT(registries));
		CompoundTag transformations = new CompoundTag();
		transformations.putFloat("rotspeed", rotationSpeed);
		transformations.putFloat("bobspeed", bobSpeed);
		transformations.putFloat("savedrot", savedRotation);
		transformations.putFloat("savedheight", savedHeight);
		transformations.putFloat("scale", scale);
		transformations.putFloat("baseheight", baseHeight);
		transformations.putBoolean("pause", pause);
		transformations.putBoolean("flipped", flipped);
		compound.put("transforms", transformations);
		compound.putBoolean("soa_marker", stationOfAwakeningMarker);
		compound.put("display_stack", displayStack.save(registries));
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.pedestal");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
		return new PedestalMenu(windowID, playerInventory, this);
	}

	public void setStationOfAwakeningMarker(boolean marker) {
		this.stationOfAwakeningMarker = marker;
		setChanged();
	}

	public boolean isStationOfAwakeningMarker() {
		return stationOfAwakeningMarker;
	}

	public ItemStack getDisplayStack() {
		return displayStack;
	}

	public void setDisplayStack(ItemStack displayStack) {
		this.displayStack = displayStack;
		setChanged();
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public float getBobSpeed() {
		return bobSpeed;
	}

	public float getSavedRotation() {
		return savedRotation;
	}

	public float getSavedHeight() {
		return savedHeight;
	}

	public boolean isPaused() {
		return pause;
	}
	
	public boolean isFlipped() {
		return flipped;
	}

	public void setSpeed(float rotationSpeed, float bobSpeed) {
		this.rotationSpeed = rotationSpeed;
		this.bobSpeed = bobSpeed;
		setChanged();
	}

	public void saveTransforms(float savedRotation, float savedHeight) {
		this.savedRotation = savedRotation;
		this.savedHeight = savedHeight;
		setChanged();
	}

	public void setPause(boolean pause) {
		this.pause = pause;
		setChanged();
	}
	
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
		setChanged();
	}

	public void setCurrentTransforms(float currentRotation, float currentHeight) {
		this.currentHeight = currentHeight;
		this.currentRotation = currentRotation;
	}

	public float getBaseHeight() {
		return baseHeight;
	}

	public void setBaseHeight(float baseHeight) {
		this.baseHeight = baseHeight;
	}

	public float getCurrentRotation() {
		return currentRotation;
	}

	public float getCurrentHeight() {
		return currentHeight;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	private static int ticksExisted;
	public static int previousTicks;

	public int ticksExisted() {
		return ticksExisted;
	}

	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		if (level.getBlockEntity(pos) instanceof PedestalTileEntity ped) {
			if(!ped.isPaused()) {
				previousTicks = ticksExisted;
				ticksExisted++;
			}
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		this.loadAdditional(tag, registries);
	}
	
}