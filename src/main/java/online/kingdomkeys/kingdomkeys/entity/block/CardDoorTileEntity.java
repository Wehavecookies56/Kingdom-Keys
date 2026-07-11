package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateCORooms;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomGenerator;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;

public class CardDoorTileEntity extends BlockEntity {

    public CardDoorTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModEntities.TYPE_CARD_DOOR.get(), pWorldPosition, pBlockState);
    }

    boolean updateCriteria = false;

    boolean open = false;
    boolean locked = false;
    BlockPos destination;
    RoomData parent;
    RoomData destinationRoom;
    RoomDirection direction;
    DoorData data;
    //copy criteria from data to save the cards that have been used
    EnumMap<CardCategory, DoorData.CardCriteria> currentCriteria;

    int disableTicks;

    public void openDoor(boolean setBlock) {
        open = true;
        if (setBlock) {
            level.setBlock(this.getBlockPos(), getBlockState().setValue(CardDoorBlock.OPEN, true), 2);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isLocked() {
        return locked;
    }

    public void toggleDoorLock() {
        if (locked && destinationRoom.getFixedType().isPresent() && destinationRoom.getGenerated().isEmpty() && parent.getGenerated().isPresent()) {
            openDoor(false);
            CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get((ServerLevel) level).orElseThrow();
            destinationRoom = interiorData.getRoomByData(destinationRoom);
            destinationRoom.setGenerated(RoomGenerator.INSTANCE.generateRoom((ServerLevel) level, destinationRoom, destinationRoom.getFixedType().get(), parent.getGenerated().get(), direction, 0));
            destinationRoom.getGenerated().ifPresent(room -> {
                PacketHandler.sendToAll(new SCSyncCastleOblivionInteriorData(interiorData, level));
                PacketHandler.sendToAll(new SCUpdateCORooms(interiorData.getFloorByID(parent.getParentID()).getRooms()));
                CardDoorTileEntity te = (CardDoorTileEntity) level.getBlockEntity(room.doors.get(direction.opposite()).pos());
                if (te != null) {
                    te.setDestinationRoom(parent);
                    te.openDoor(true);
                }
                interiorData.setDirty();
            });
        }
        locked = !locked;
        if (level.getBlockState(getBlockPos()).getValue(CardDoorBlock.OPEN) == locked) {
            level.setBlock(this.getBlockPos(), getBlockState().setValue(CardDoorBlock.OPEN, !locked), 2);
        }
    }

    public void setParent(RoomData room) {
        parent = room;
        setChanged();
    }

    public RoomData getParentRoom() {
        return parent;
    }

    public RoomData getDestinationRoom() {
        return destinationRoom;
    }

    public void setDestinationRoom(RoomData destinationRoom) {
        this.destinationRoom = destinationRoom;
        setChanged();
    }

    public void setDirection(RoomDirection direction) {
        this.direction = direction;
        setChanged();
    }

    public RoomDirection getDirection() {
        return direction;
    }

    public DoorData getData() {
        return data;
    }

    public EnumMap<CardCategory, DoorData.CardCriteria> getCurrentCriteria() {
        return currentCriteria;
    }

    public void setCurrentCriteria(EnumMap<CardCategory, DoorData.CardCriteria> criteria) {
        this.currentCriteria = criteria;
        setChanged();
    }

    public boolean cardMatchesCriteria(ItemStack card) {
        if (card.getItem() instanceof MapCardItem mapCardItem) {
            boolean matchFound = false;
            //find first match from all criteria
            for (CardCategory category : currentCriteria.keySet()) {
                matchFound = cardMatchesCriterion(card, category);
                if (matchFound) {
                    break;
                }
            }
            return matchFound;
        } else {
            KingdomKeys.LOGGER.error("Tried to use non card item to open a door something has gone wrong");
            return false;
        }
    }

    public boolean cardMatchesCriterion(ItemStack card, CardCategory category) {
        if (card.getItem() instanceof MapCardItem mapCardItem) {
            boolean matchFound = false;
            boolean categoryMatch;
            //ANY is RGB
            if (category == CardCategory.RGB && mapCardItem.getCategory() != CardCategory.YELLOW) {
                categoryMatch = true;
            } else {
                categoryMatch = category == mapCardItem.getCategory() || mapCardItem.getCategory() == CardCategory.RGB;
            }
            //don't bother checking the card value if the category doesn't match
            if (categoryMatch) {
                int value = MapCardItem.getCardValue(card);
                DoorData.CardCriteria criteria = currentCriteria.get(category);
                matchFound = switch (criteria.criteriaType()) {
                    case EQUAL -> criteria.value() == value;
                    case TOTAL -> value != 0;
                    case GREATER -> value >= criteria.value() || value == 0;
                    case LESSER -> value <= criteria.value();
                    case GREATER_NO_ZERO -> value >= criteria.value();
                };
            }
            return matchFound;
        }
        return false;
    }

    public CardCategory getCardCriterionCategory(ItemStack card) {
        if (card.getItem() instanceof MapCardItem mapCardItem) {
            for (CardCategory category : currentCriteria.keySet()) {
                //ANY is RGB
                if (category == CardCategory.RGB && mapCardItem.getCategory() != CardCategory.YELLOW) {
                    return category;
                } else if (category == mapCardItem.getCategory()) {
                    return category;
                }
            }
        }
        return null;
    }

    public boolean consumeCard(ItemStack cardStack) {
        if (cardStack.getItem() instanceof MapCardItem card) {
            CardCategory category = card.getCategory();
            int value = MapCardItem.getCardValue(cardStack);
            if (cardMatchesCriteria(cardStack)) {
                CardCategory criteriaCategory = getCardCriterionCategory(cardStack);
                DoorData.CardCriteria criteria = currentCriteria.get(criteriaCategory);
                if (criteria.criteriaType() == DoorData.CriteriaType.TOTAL) {
                    if (criteria.value() - value <= 0)  {
                        currentCriteria.remove(criteriaCategory);
                    } else {
                        currentCriteria.put(criteriaCategory, new DoorData.CardCriteria(criteria.value() - value, DoorData.CriteriaType.TOTAL));
                    }
                } else {
                    currentCriteria.remove(criteriaCategory);
                }
                setChanged();
                cardStack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public void setData(DoorData data) {
        this.data = data;
    }

    public void setDisableTicks(int ticks) {
        this.disableTicks = ticks;
    }

    public int getDisableTicks() {
        return disableTicks;
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);
        if (pTag.contains("parent")) {
            parent = new RoomData(pTag.getCompound("parent"));
            direction = RoomDirection.values()[pTag.getInt("direction")];
        }
        if (pTag.contains("destination_room")) {
            destinationRoom = new RoomData(pTag.getCompound("destination_room"));
        }
        open = pTag.getBoolean("open");
        if (open && pTag.contains("destination")) {
            destination = NbtUtils.readBlockPos(pTag.getCompound("destination"), "destination").get();
        } else {
            destination = null;
        }
        if (pTag.contains("door_data")) {
            data = new DoorData(pTag.getCompound("door_data"));
        }
        currentCriteria = new EnumMap<>(CardCategory.class);
        CompoundTag criteria = pTag.getCompound("criteria");
        Arrays.stream(CardCategory.values()).forEach(cardCategory -> {
            if (criteria.contains(cardCategory.name())) {
                CompoundTag criteriaEntry = criteria.getCompound(cardCategory.name());
                this.currentCriteria.put(cardCategory, new DoorData.CardCriteria(criteriaEntry.getInt("value"), DoorData.CriteriaType.values()[criteriaEntry.getInt("type")]));
            }
        });
        locked = pTag.getBoolean("locked");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);
        if (parent != null) {
            pTag.put("parent", parent.serializeNBT());
            pTag.putInt("direction", direction.ordinal());
        }
        if (destinationRoom != null) {
            pTag.put("destination_room", destinationRoom.serializeNBT());
        }
        pTag.putBoolean("open", open);
        if (open && destination != null) {
            pTag.put("destination", NbtUtils.writeBlockPos(destination));
        }
        if (data != null) {
            pTag.put("door_data", data.serializeNBT());
            if (currentCriteria == null) {
                currentCriteria = getData().getCardCriteria();
            }
        }

        if (currentCriteria != null) {
            CompoundTag criteria = new CompoundTag();
            currentCriteria.forEach((roomCategory, cardCriteria) -> {
                CompoundTag criteriaEntry = new CompoundTag();
                criteriaEntry.putInt("value", cardCriteria.value());
                criteriaEntry.putInt("type", cardCriteria.criteriaType().ordinal());
                criteria.put(roomCategory.name(), criteriaEntry);
            });
            pTag.put("criteria", criteria);
        }
        pTag.putBoolean("locked", locked);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveCustomOnly(pRegistries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof CardDoorTileEntity cardDoorTileEntity) {
            if (cardDoorTileEntity.isOpen() && !cardDoorTileEntity.isLocked()) {
                if (cardDoorTileEntity.disableTicks > 0) {
                    cardDoorTileEntity.disableTicks--;
                }
            }
        }
    }
}