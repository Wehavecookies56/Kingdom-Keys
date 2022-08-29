package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.block.GummiEditorBlock;
import online.kingdomkeys.kingdomkeys.block.MagnetBloxBlock;
import online.kingdomkeys.kingdomkeys.container.GummiEditorContainer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class GummiEditorTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 1;
	private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

	private ItemStack displayStack = ItemStack.EMPTY;

	public GummiEditorTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_GUMMI_EDITOR.get(), pos, state);
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
		super.load(compound);
		CompoundTag invCompound = compound.getCompound("inv");
		inventory.ifPresent(iih -> ((INBTSerializable<CompoundTag>) iih).deserializeNBT(invCompound));
		//CompoundNBT transformations = compound.getCompound("transforms");
		displayStack = ItemStack.of(compound.getCompound("display_stack"));
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		inventory.ifPresent(iih -> {
			CompoundTag invCompound = ((INBTSerializable<CompoundTag>) iih).serializeNBT();
			compound.put("inv", invCompound);
		});
		//CompoundNBT transformations = new CompoundNBT();
		compound.put("display_stack", displayStack.serializeNBT());
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
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		this.load(tag);
	}
	
	int ticks = 0;

	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		GummiEditorTileEntity TE = (GummiEditorTileEntity) blockEntity;
		Direction facing = state.getValue(GummiEditorBlock.FACING);
		TE.ticks++;
		//int y = 0;
		for(int x=0;x<7;x++) {
			for(int y=0;y<7;y++) {
				for(int z=0;z<7;z++) {
					if(x == 0 || x == 6 || z == 0 || z == 6) {
						if(y == 0 || y == 6) {
							switch(facing) {
							case NORTH:
								level.addParticle(new DustParticleOptions(new Vector3f(1,1,1), 1), pos.getX()+0.5 - 3 + x, pos.getY() + y+0.5, pos.getZ()+1.5 + z, 0,0,0);
								break;
							case SOUTH:
								level.addParticle(new DustParticleOptions(new Vector3f(1,1,1), 1), pos.getX()+0.5 - 3 + x, pos.getY() + y+0.5, pos.getZ()-0.5 - z, 0,0,0);
								break;
							case EAST:
								level.addParticle(new DustParticleOptions(new Vector3f(1,1,1), 1), pos.getX()-0.5 - 6 + x, pos.getY() + y+0.5, pos.getZ()+0.5 + 3 - z, 0,0,0);
								break;
							case WEST:
								level.addParticle(new DustParticleOptions(new Vector3f(1,1,1), 1), pos.getX()-0.5 + 2 + x, pos.getY() + y+0.5, pos.getZ()+0.5 + 3 - z, 0,0,0);
								break;
							}
						}
					}
	
				}	
			}
		}

	}
	
}