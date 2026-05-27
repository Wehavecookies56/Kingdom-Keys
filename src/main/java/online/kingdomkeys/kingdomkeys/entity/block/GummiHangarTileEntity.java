package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.GummiShipBlueprintItem;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GummiHangarTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 2;
	private final ItemStackHandler itemStackHandler = createInventory();
	public final Lazy<IItemHandler> inventory = Lazy.of(() -> itemStackHandler);
	private String lastShipName = "";

    public int burnTime;
    public int maxBurnTime;

    public HangarEnergyStorage energyStorage = Utils.getEnergyStoragePerLevel(0);

	public GummiHangarTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_GUMMI_HANGAR.get(), pos, state);
	}

	public void setLastShipName(String name) {
        this.lastShipName = name;
        setChanged();
        this.getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

	public String getLastShipName() {
		return lastShipName;
	}

    public int getMaxEnergy() {
        return energyStorage.getMaxEnergyStored();
    }

    private ItemStackHandler createInventory() {
		return new ItemStackHandler(NUMBER_OF_SLOTS) {
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                boolean isFuel = stack.getBurnTime(RecipeType.SMELTING) > 0;
                return slot == 0 ? stack.getItem() instanceof GummiShipBlueprintItem : isFuel;
			}

			@Override
			protected void onContentsChanged(int slot) {
                if(slot == 0) {
                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                    // Remove display if blueprint is removed
                    getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(GummiHangarBlock.DISPLAY_BLUEPRINT, false));
                }
				super.onContentsChanged(slot);
			}
		};
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		CompoundTag invCompound = compound.getCompound("inv");
		itemStackHandler.deserializeNBT(provider, invCompound);

        if (itemStackHandler.getSlots() < NUMBER_OF_SLOTS) {
            ItemStackHandler newHandler = new ItemStackHandler(NUMBER_OF_SLOTS);
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                newHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
            }
            itemStackHandler.setSize(NUMBER_OF_SLOTS);
        }

		if (compound.contains("LastShipName"))
			lastShipName = compound.getString("LastShipName");
        burnTime = compound.getInt("BurnTime");
        maxBurnTime = compound.getInt("MaxBurnTime");
        if(compound.contains("EnergyFE"))
            energyStorage.deserializeNBT(provider,compound.getCompound("EnergyFE"));
    }

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put("inv", itemStackHandler.serializeNBT(provider));
		compound.putString("LastShipName", lastShipName);

        compound.putInt("BurnTime", burnTime);
        compound.putInt("MaxBurnTime", maxBurnTime);
        compound.put("EnergyFE", energyStorage.serializeNBT(provider));
    }

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.gummi_hangar");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
		return new GummiHangarMenu(windowID, playerInventory, this);
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

    public static < T > void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof GummiHangarTileEntity hangar) {
            if (level == null || level.isClientSide)
                return;

            //If has some combustible store it
	        if (hangar.burnTime > 0 && hangar.energyStorage.getEnergyStored() < hangar.getMaxEnergy()) {
		        int speed = state.getValue(GummiHangarBlock.LEVEL) + 1;
		        hangar.burnTime -= speed;
		        if (hangar.burnTime < 0) {
			        hangar.burnTime = 0;
		        }
		        hangar.energyStorage.receiveEnergy(speed, false);
	        }
            //If has finished consuming find a new combustible
            if (hangar.burnTime <= 0 && hangar.energyStorage.getEnergyStored() < hangar.getMaxEnergy()) {
                hangar.maxBurnTime = 0;
                ItemStack fuelStack = hangar.inventory.get().getStackInSlot(1);
                int fuel = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (fuel > 0) {
                    hangar.burnTime = fuel;
                    hangar.maxBurnTime = fuel;
                    fuelStack.shrink(1);
                }
            }

            //Refuel ships
            if (level.hasNeighborSignal(pos)) {
                int size = GummiHangarBlock.getSize(state.getValue(GummiHangarBlock.LEVEL));
                List <GummiShipEntity> ships = Utils.getAllGummiShipsInBuildPlate(level, pos, state.getValue(GummiHangarBlock.FACING), size);
                //Refuel all ships found in the area
                if (!ships.isEmpty() && hangar.energyStorage.getEnergyStored() > 0) {
                    for (GummiShipEntity ship: ships) {
                        int transfer = (state.getValue(GummiHangarBlock.LEVEL) + 1) * 10;
                        //Heal first, refuel later
                        if(ship.getDamage() > 0){
                            ship.setDamage(ship.getDamage() - hangar.energyStorage.extractEnergy((int)(transfer*0.1F), false));
                        } else {
                            if(ModConfigs.SERVER.gummiShipFuelSystem.get()) { //Only refuel (and lose energy) if the fuel system is enabled
                                if (ship.getFuel() < ship.getMaxFuel()) { // Extract the energy from the block and insert it to the ship
                                    ship.addFuel(hangar.energyStorage.extractEnergy(transfer, false));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static class HangarEnergyStorage extends EnergyStorage {
        public HangarEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergy(int energy) {
            this.energy = Math.min(energy, capacity);
        }

        public int setCapacity(int capacity) {
            this.capacity = capacity;
            return capacity;
        }

        @Override
        public Tag serializeNBT(HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Energy", this.energy);
            tag.putInt("Capacity", this.capacity);
            tag.putInt("MaxReceive", this.maxReceive);
            tag.putInt("MaxExtract", this.maxExtract);
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
            if (!(nbt instanceof CompoundTag tag))
                throw new IllegalArgumentException("Expected CompoundTag for HangarEnergyStorage");

            this.energy = tag.getInt("Energy");
            this.capacity = tag.getInt("Capacity");
            this.maxReceive = tag.getInt("MaxReceive");
            this.maxExtract = tag.getInt("MaxExtract");
            if (energy > capacity)
                energy = capacity;
        }
    }

}