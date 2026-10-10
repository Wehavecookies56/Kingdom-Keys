package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiCostLevel;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiPieceEntity;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.GummiCostChipItem;
import online.kingdomkeys.kingdomkeys.item.GummiShipBlueprintItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;

import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class GummiHangarTileEntity extends BlockEntity implements MenuProvider {
	public static final int NUMBER_OF_SLOTS = 3;
	public static final int COST_SLOT = 2;
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
                return switch (slot) {
                    case 0 -> stack.getItem() instanceof GummiShipBlueprintItem;
                    case COST_SLOT -> stack.getItem() instanceof GummiCostChipItem;
                    default -> stack.getBurnTime(RecipeType.SMELTING) > 0;
                };
			}

			@Override
			public int getSlotLimit(int slot) {
				return slot == COST_SLOT ? GummiCostLevel.maxChips(getBlockState().getValue(GummiHangarBlock.LEVEL)) : super.getSlotLimit(slot);
			}

			@Override
			protected void onContentsChanged(int slot) {
                if(slot == 0) {
                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                    // Remove display if blueprint is removed
                    getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(GummiHangarBlock.DISPLAY_BLUEPRINT, false));
                } else if (slot == COST_SLOT && level != null && !level.isClientSide) {
                    // Increasing the limit updates the hologram too
                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
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
                    ItemStack remainder = fuelStack.getCraftingRemainingItem();
                    fuelStack.shrink(1);
                    if (!remainder.isEmpty() && fuelStack.isEmpty()) {
                        hangar.itemStackHandler.setStackInSlot(1, remainder);
                    }
                    hangar.setChanged();
                }
            }

            if (hangar.building && ModConfigs.SERVER.gummiHangarAutoBuild.get()) {
                hangar.buildFromBlueprint(level, pos, state);
            }

            //Refuel ships, unless a redstone signal is holding it back
            if (state.getValue(GummiHangarBlock.ACTIVE)) {
                int size = GummiHangarBlock.getSize(state.getValue(GummiHangarBlock.LEVEL));
                List <GummiShipEntity> ships = Utils.getAllGummiShipsInBuildPlate(level, pos, state.getValue(GummiHangarBlock.FACING), size);
                //Refuel all ships found in the area
                if (!ships.isEmpty() && hangar.energyStorage.getEnergyStored() > 0) {
                    for (GummiShipEntity ship: ships) {
                        int transfer = (state.getValue(GummiHangarBlock.LEVEL) + 1) * 10;
                        boolean serviced = false;
                        //Heal first, refuel later
                        if(ship.getDamage() > 0){
                            ship.setDamage(ship.getDamage() - hangar.energyStorage.extractEnergy((int)(transfer*0.1F), false));
                            serviced = true;
                        } else {
                            if(ModConfigs.SERVER.gummiShipFuelSystem.get()) { //Only refuel (and lose energy) if the fuel system is enabled
                                if (ship.getFuel() < ship.getMaxFuel()) { // Extract the energy from the block and insert it to the ship
                                    ship.addFuel(hangar.energyStorage.extractEnergy(transfer, false));
                                    serviced = true;
                                }
                            }
                        }

                        if (serviced && level instanceof ServerLevel server) {
                            serviceTrails(server, pos, state.getValue(GummiHangarBlock.LEVEL), ship.getId(), random -> hullPoint(ship, random), ship.getDamage() > 0, ship.getBoundingBox());
                        }
                    }
                }

                GummiCoreTileEntity core = hangar.editedCore(level, pos, state, size);
                if (core != null && hangar.energyStorage.getEnergyStored() > 0) {
                    int transfer = (state.getValue(GummiHangarBlock.LEVEL) + 1) * 10;
                    boolean serviced = false;

                    if (core.getDamage() > 0) {
                        core.setDamage(core.getDamage() - hangar.energyStorage.extractEnergy((int) (transfer * 0.1F), false));
                        serviced = true;
                    } else if (ModConfigs.SERVER.gummiShipFuelSystem.get() && core.getFuel() < GummiShipEntity.getMaxFuelForSize(size)) {
                        int room = GummiShipEntity.getMaxFuelForSize(size) - core.getFuel();
                        core.setFuel(core.getFuel() + hangar.energyStorage.extractEnergy(Math.min(transfer, room), false));
                        serviced = true;
                    }

                    if (serviced && level instanceof ServerLevel server) {
                        AABB plate = buildPlate(pos, state, size);
                        serviceTrails(server, pos, state.getValue(GummiHangarBlock.LEVEL), core.getBlockPos().asLong(), random -> platePoint(level, plate, core.getBlockPos(), random), core.getDamage() > 0, plate);
                    }
                }
            }
        }
    }

    private static final DustParticleOptions SERVICE_DUST = new DustParticleOptions(new Vector3f(0.35F, 1F, 0.45F), 1.1F);
    // A bigger hangar repairs faster, so it sends more streams and they travel quicker
    private static final int TRAILS_BASE = 2, TRAILS_MAX = 12;
    private static final int TRIP_TICKS_BASE = 44, TRIP_TICKS_PER_LEVEL = 6, TRIP_TICKS_MIN = 12;
    private static final int TRAIL_LENGTH = 4;
    private static final double TRAIL_STEP = 0.025D, TRAIL_ARC = 2.5D;

    // Green streams arcing from the hangar into the ship, so you can see it being charged and patched up
    private static void serviceTrails(ServerLevel level, BlockPos pos, int hangarLevel, long seed, Function<RandomSource, Vec3> landingPoint, boolean healing, AABB hull) {
        int trails = Math.min(TRAILS_MAX, TRAILS_BASE + hangarLevel);
        int tripTicks = Math.max(TRIP_TICKS_MIN, TRIP_TICKS_BASE - hangarLevel * TRIP_TICKS_PER_LEVEL);

        if (level.getGameTime() % 2 != 0) {
            return;
        }

        Vec3 from = Vec3.atCenterOf(pos).add(0D, 0.6D, 0D);
        Vec3 to = hull.getCenter();

        for (int trail = 0; trail < trails; trail++) {
            long time = level.getGameTime() + (long) trail * tripTicks / trails;
            double head = (time % tripTicks) / (double) tripTicks;

            // Each trip lands somewhere new on the hull, held for the whole trip so the stream does not jitter
            RandomSource landing = RandomSource.create(seed * 31L + trail * 7919L + time / tripTicks);
            Vec3 end = landingPoint.apply(landing);
            Vec3 peak = from.add(end).scale(0.5D).add(0D, TRAIL_ARC, 0D);

            for (int tail = 0; tail < TRAIL_LENGTH; tail++) {
                double t = head - tail * TRAIL_STEP;
                if (t < 0D) {
                    continue;
                }

                double u = 1D - t;
                Vec3 at = from.scale(u * u).add(peak.scale(2D * u * t)).add(end.scale(t * t));
                level.sendParticles(SERVICE_DUST, at.x, at.y, at.z, 1, 0D, 0D, 0D, 0D);
            }
        }

        if (healing && level.getGameTime() % 10 == 0) {
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, to.x, to.y, to.z, 6, hull.getXsize() * 0.3D, hull.getYsize() * 0.3D, hull.getZsize() * 0.3D, 0D);
        }
    }

    private static final int HULL_TRIES = 24;
    private static final int CORE_SEARCH_INTERVAL = 20;

    @Nullable
    private BlockPos editedCorePos;

    // The plate is recounted every so often: with more than one core on it there is no telling which ship is being edited, so none is serviced
    @Nullable
    private GummiCoreTileEntity editedCore(Level level, BlockPos pos, BlockState state, int size) {
        if (level.getGameTime() % CORE_SEARCH_INTERVAL == 0) {
            editedCorePos = null;
            AABB plate = buildPlate(pos, state, size);
            int cores = 0;

            for (BlockPos at : BlockPos.betweenClosed((int) plate.minX, (int) plate.minY, (int) plate.minZ, (int) plate.maxX - 1, (int) plate.maxY - 1, (int) plate.maxZ - 1)) {
                if (level.getBlockEntity(at) instanceof GummiCoreTileEntity) {
                    if (++cores > 1) {
                        editedCorePos = null;
                        break;
                    }
                    editedCorePos = at.immutable();
                }
            }
        }

        if (editedCorePos != null && level.getBlockEntity(editedCorePos) instanceof GummiCoreTileEntity core) {
            return core;
        }

        editedCorePos = null;
        return null;
    }

    private static AABB buildPlate(BlockPos pos, BlockState state, int size) {
        int[] offsets = Utils.getShipOffset(state.getValue(GummiHangarBlock.FACING), size);
        int x = pos.getX() + (offsets == null ? 0 : offsets[0]);
        int z = pos.getZ() + (offsets == null ? 0 : offsets[1]);
        return new AABB(x, pos.getY(), z, x + size, pos.getY() + size, z + size);
    }

    // A random placed block of the ship being edited; air is skipped
    private static Vec3 platePoint(Level level, AABB plate, BlockPos core, RandomSource random) {
        for (int i = 0; i < HULL_TRIES; i++) {
            BlockPos at = BlockPos.containing(Mth.lerp(random.nextDouble(), plate.minX, plate.maxX), Mth.lerp(random.nextDouble(), plate.minY, plate.maxY), Mth.lerp(random.nextDouble(), plate.minZ, plate.maxZ));

            if (!level.getBlockState(at).isAir()) {
                return Vec3.atCenterOf(at);
            }
        }

        return Vec3.atCenterOf(core);
    }

    // A random block of the ship itself, placed the same way the renderer places it; air is skipped
    private static Vec3 hullPoint(GummiShipEntity ship, RandomSource random) {
        GummiStructure structure = ship.structure;

        if (structure != null && structure.getWidth() > 0 && structure.getHeight() > 0 && structure.getDepth() > 0) {
            int w = structure.getWidth(), h = structure.getHeight(), d = structure.getDepth();
            boolean[] even = Utils.isStructureEven(structure);

            for (int i = 0; i < HULL_TRIES; i++) {
                int x = random.nextInt(w), y = random.nextInt(h), z = random.nextInt(d);
                BlockState state = structure.getBlocks()[x][y][z];

                if (state == null || state.isAir()) {
                    continue;
                }

                double bx = (even[0] ? x + 0.5D : x) + 0.5D - w / 2.0D;
                double bz = (even[1] ? z - 0.5D : z) + 0.5D - d / 2.0D;
                Vec3 local = new Vec3(bx, y + 0.5D, bz)
                        .xRot((float) Math.toRadians(ship.getXRot()))
                        .yRot((float) Math.toRadians(180.0F - ship.getYRot()));

                return ship.position().add(local);
            }
        }

        AABB hull = ship.getBoundingBox();
        return new Vec3(Mth.lerp(random.nextDouble(), hull.minX, hull.maxX), Mth.lerp(random.nextDouble(), hull.minY, hull.maxY), Mth.lerp(random.nextDouble(), hull.minZ, hull.maxZ));
    }

    private GummiStructure fitted;
    private GummiStructure fittedSource;
    private int fittedSize;

    public List<?> ghosts = List.of();
    public long ghostsAt = Long.MIN_VALUE;
    public GummiStructure ghostsSource;
    public int ghostsSize;
    public Direction ghostsFacing;

    // Cache the structure so it doesn't have to rebuild it for every block
    public GummiStructure fitted(GummiStructure blueprint, int size) {
        if (fittedSource != blueprint || fittedSize != size) {
            fittedSource = blueprint;
            fittedSize = size;
            fitted = Utils.resizeStructure(blueprint, size);
        }

        return fitted;
    }

    public int hologramCost = -1;
    public long hologramTime = Long.MIN_VALUE;

    public int getCostLimitLevel() {
        return GummiCostLevel.fromChips(inventory.get().getStackInSlot(COST_SLOT).getCount(), getBlockState().getValue(GummiHangarBlock.LEVEL));
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

        if (blueprint == null) {
            return;
        }

        if (GummiCostLevel.overLimit(Utils.getShipStats(blueprint), getCostLimitLevel()) != null) {
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
        GummiStructure struct = fitted(blueprint, size);

        // Null means the blueprint's blocks do not fit this plate, whatever size it says it is
        if (struct == null) {
            return;
        }

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
					level.playSound(null, getBlockPos(), ModSounds.gummiPlace.get(), SoundSource.MASTER, 1F, 1F);
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