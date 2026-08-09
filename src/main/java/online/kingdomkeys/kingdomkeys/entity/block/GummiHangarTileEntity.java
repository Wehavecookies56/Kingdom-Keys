package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import online.kingdomkeys.kingdomkeys.entity.GummiPieceEntity;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.core.Direction;
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
    private int buildCooldown;
    private boolean building;
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

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
        building = compound.getBoolean("Building");
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

        compound.putBoolean("Building", building);
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

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
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

            if (hangar.building && ModConfigs.SERVER.gummiHangarAutoBuild.get()) {
                hangar.buildFromBlueprint(level, pos, state);
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

    private void buildFromBlueprint(Level level, BlockPos pos, BlockState state) {
        if (--buildCooldown > 0) {
            return;
        }

        buildCooldown = Math.max(1, ModConfigs.SERVER.gummiHangarBuildDelay.get() / (state.getValue(GummiHangarBlock.LEVEL) + 1));

        ItemStack blueprintStack = inventory.get().getStackInSlot(0);

        if (!GummiShipBlueprintItem.isBlueprint(blueprintStack)) {
            return;
        }

        GummiStructure blueprint = blueprintStack.get(ModComponents.GUMMI_STRUCTURE);
        int size = GummiHangarBlock.getSize(state.getValue(GummiHangarBlock.LEVEL));

        if (blueprint == null || blueprint.getWidth() > size) {
            return;
        }

        Direction facing = state.getValue(GummiHangarBlock.FACING);
        int[] offsets = Utils.getShipOffset(facing, size);

        if (offsets == null) {
            return;
        }

        List<IItemHandler> containers = new ArrayList<>();

        for (Direction side : Direction.values()) {
            IItemHandler container = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(side), side.getOpposite());

            if (container != null) {
                containers.add(container);
            }
        }

        if (containers.isEmpty()) {
            return;
        }

        // The same mapping the finished ship is built with, so what is laid out here lands exactly where importing the blueprint in one go would have put it
        GummiStructure struct = Utils.resizeStructure(blueprint, size);
        Rotation rotation = switch (facing) {
            case NORTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.CLOCKWISE_90;
            case EAST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        int cost = ModConfigs.SERVER.gummiHangarBuildCost.get();
        int max = size - 1;

        // Pieces traveling
        Set<BlockPos> pending = new HashSet<>();

        for (GummiPieceEntity piece : level.getEntitiesOfClass(GummiPieceEntity.class, new AABB(pos).inflate(size + 2))) {
            pending.add(piece.getTarget());
        }

        // from bottom to top
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    BlockState wanted = struct.getBlocks()[x][y][z];

                    if (wanted == null || wanted.isAir()) {
                        continue;
                    }

                    wanted = Utils.rotateBlock(wanted, rotation);

                    int rx = x, rz = z;
                    switch (facing) {
                        case NORTH -> { rx = max - x; rz = max - z; }
                        case EAST -> { rx = z; rz = max - x; }
                        case WEST -> { rx = max - z; rz = x; }
                    }

                    BlockPos target = pos.offset(offsets[0] + rx, y, offsets[1] + rz);
                    BlockState current = level.getBlockState(target);

                    if (GummiBlockBase.sameAppearance(current, wanted) || !current.canBeReplaced() || pending.contains(target)) {
                        continue;
                    }

                    if (energyStorage.getEnergyStored() < cost) {
                        return;
                    }

                    if (!takePiece(containers, wanted)) {
                        continue;
                    }

                    energyStorage.extractEnergy(cost, false);
                    setChanged();
                    // The piece flies out and puts itself down when it gets there
                    level.addFreshEntity(GummiPieceEntity.create(level, pos.getCenter(), target, wanted));
                    return;
                }
            }
        }
    }

    private static boolean takePiece(List<IItemHandler> containers, BlockState wanted) {
        Item piece = wanted.getBlock().asItem();

        if (piece == Items.AIR) {
            return false;
        }

        for (IItemHandler container : containers) {
            for (int slot = 0; slot < container.getSlots(); slot++) {
                if (container.getStackInSlot(slot).is(piece) && !container.extractItem(slot, 1, false).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
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