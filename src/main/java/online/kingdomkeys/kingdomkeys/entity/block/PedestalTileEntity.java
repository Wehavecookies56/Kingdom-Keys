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
import online.kingdomkeys.kingdomkeys.container.PedestalContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class PedestalTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
	public static final int NUMBER_OF_SLOTS = 1;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

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

	//only changed on the client so it will not hide for other players
	public boolean hide = false;

	public PedestalTileEntity() {
		super(ModEntities.TYPE_PEDESTAL.get());
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
		CompoundNBT transformations = compound.getCompound("transforms");
		rotationSpeed = transformations.getFloat("rotspeed");
		bobSpeed = transformations.getFloat("bobspeed");
		savedRotation = transformations.getFloat("savedrot");
		savedHeight = transformations.getFloat("savedheight");
		scale = transformations.getFloat("scale");
		baseHeight = transformations.getFloat("baseheight");
		pause = transformations.getBoolean("pause");
		stationOfAwakeningMarker = compound.getBoolean("soa_marker");
		displayStack = ItemStack.read(compound.getCompound("display_stack"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		inventory.ifPresent(iih -> {
			CompoundNBT invCompound = ((INBTSerializable<CompoundNBT>) iih).serializeNBT();
			compound.put("inv", invCompound);
		});
		CompoundNBT transformations = new CompoundNBT();
		transformations.putFloat("rotspeed", rotationSpeed);
		transformations.putFloat("bobspeed", bobSpeed);
		transformations.putFloat("savedrot", savedRotation);
		transformations.putFloat("savedheight", savedHeight);
		transformations.putFloat("scale", scale);
		transformations.putFloat("baseheight", baseHeight);
		transformations.putBoolean("pause", pause);
		compound.put("transforms", transformations);
		compound.putBoolean("soa_marker", stationOfAwakeningMarker);
		compound.put("display_stack", displayStack.serializeNBT());
		return compound;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.pedestal");
	}

	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new PedestalContainer(windowID, playerInventory, this);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	public void setStationOfAwakeningMarker(boolean marker) {
		this.stationOfAwakeningMarker = marker;
		markDirty();
	}

	public boolean isStationOfAwakeningMarker() {
		return stationOfAwakeningMarker;
	}

	public ItemStack getDisplayStack() {
		return displayStack;
	}

	public void setDisplayStack(ItemStack displayStack) {
		this.displayStack = displayStack;
		markDirty();
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

	public void setSpeed(float rotationSpeed, float bobSpeed) {
		this.rotationSpeed = rotationSpeed;
		this.bobSpeed = bobSpeed;
		markDirty();
	}

	public void saveTransforms(float savedRotation, float savedHeight) {
		this.savedRotation = savedRotation;
		this.savedHeight = savedHeight;
		markDirty();
	}

	public void setPause(boolean pause) {
		this.pause = pause;
		markDirty();
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

	private int ticksExisted;
	public int previousTicks;

	public int ticksExisted() {
		return ticksExisted;
	}

	@Override
	public void tick() {
		if (!pause) {
			previousTicks = ticksExisted;
			ticksExisted++;
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
	
}