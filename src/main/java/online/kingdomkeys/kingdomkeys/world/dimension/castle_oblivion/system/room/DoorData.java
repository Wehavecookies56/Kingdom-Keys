package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.KeycardType;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Arrays;
import java.util.EnumMap;

public class DoorData {

    RoomData parent;
    Type type;
    RoomDirection direction;
    private final EnumMap<CardCategory, CardCriteria> cardCriteria;
    boolean lockedByDefault;

    public DoorData(RoomData parent, Type type, RoomDirection direction) {
        this.type = type;
        this.parent = parent;
        this.direction = direction;
        this.cardCriteria = new EnumMap<>(CardCategory.class);
    }

    public DoorData(RoomData parent, Type type, RoomDirection direction, KeycardType requiredKeycard) {
        this(parent, type, direction);
        cardCriteria.put(CardCategory.YELLOW, new CardCriteria(requiredKeycard.ordinal(), CriteriaType.EQUAL));
    }

    //Used when generating the door TE as the value is based on the card used to generate the current room
    public void generateCardCriteria(int currentRoomValue) {
        if (type == Type.KEY) {
            //Loop 3 times for ENEMY, STATUS, BOUNTY card criteria
            //TODO maybe define this in the floor type json? rather than randomly assigning them
            for (int i = 0; i < 3; i++) {
                int value;
                //Random choice of criteria type (excluding greater no zero)
                CriteriaType type = CriteriaType.values()[Utils.randomWithRange(0, 3)];
                if (type == CriteriaType.TOTAL) {
                    value = Utils.randomWithRange(9, 30);
                } else {
                    value = Utils.randomWithRange(0, 9);
                }
                //If 0> criteria randomly chosen make it 0= instead as it's the same
                if (type == CriteriaType.LESSER && value == 0) {
                    type = CriteriaType.EQUAL;
                }

                //Replace greater with greater no zero so 0 cards can't be used
                if (type == CriteriaType.GREATER) {
                    type = CriteriaType.GREATER_NO_ZERO;
                }
                cardCriteria.put(CardCategory.values()[i], new CardCriteria(value, type));
            }
        } else if (type == Type.NORMAL) {
            int value = currentRoomValue + 1;
            if (value > 9) {
                value = 0;
            }
            cardCriteria.put(CardCategory.RGB, new CardCriteria(value, value == 0 ? CriteriaType.EQUAL : CriteriaType.GREATER));
        }
    }

    public void setLockedByDefault() {
        this.lockedByDefault = true;
    }

    public EnumMap<CardCategory, CardCriteria> getCardCriteria() {
        //create new instance to prevent changing the original map that should be immutable
        return new EnumMap<>(cardCriteria);
    }

    public DoorData(CompoundTag tag) {
        this.cardCriteria = new EnumMap<>(CardCategory.class);
        this.deserializeNBT(tag);
    }

    public Type getType() {
        return type;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("type", this.type.ordinal());
        tag.putInt("direction", this.direction.ordinal());
        CompoundTag criteria = new CompoundTag();
        cardCriteria.forEach((roomCategory, cardCriteria) -> {
            CompoundTag criteriaEntry = new CompoundTag();
            criteriaEntry.putInt("value", cardCriteria.value);
            criteriaEntry.putInt("type", cardCriteria.criteriaType.ordinal());
            criteria.put(roomCategory.name(), criteriaEntry);
        });
        tag.put("criteria", criteria);
        tag.putBoolean("locked", lockedByDefault);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.type = Type.values()[tag.getInt("type")];
        this.direction = RoomDirection.values()[tag.getInt("direction")];
        CompoundTag criteria = tag.getCompound("criteria");
        Arrays.stream(CardCategory.values()).forEach(cardCategory -> {
            if (criteria.contains(cardCategory.name())) {
                CompoundTag criteriaEntry = criteria.getCompound(cardCategory.name());
                this.cardCriteria.put(cardCategory, new CardCriteria(criteriaEntry.getInt("value"), CriteriaType.values()[criteriaEntry.getInt("type")]));
            }
        });
        this.lockedByDefault = tag.getBoolean("locked");
    }

    /**
     * NORMAL: door within a generated room that can be used to set the card for the room
     * ENTRANCE: door to go to previous floor or exit dimension on floor 1
     * EXIT: door to go to the next floor
     * FIXED: like NORMAL but cards cannot be used to generate a room
     * HALL: the door used to select a world card
     * KEY: needs key card to open
     * NONE: not a door but rather to stop adjacent rooms connecting on the side of this "door"
     */
    public enum Type {
        NORMAL, ENTRANCE, EXIT, FIXED, HALL, KEY, NONE
    }

    /** Criteria types
        GREATER card needs to be greater or equal to the value or 0
        LESSER card needs to be lesser or equal to the value
        EQUAL card needs to be the exact value
        TOTAL multiple cards can be used to add up to the total value
        GREATER_NO_ZERO card needs to be greater or equal to the value
     */
    public enum CriteriaType implements StringRepresentable {
        GREATER("↑"), LESSER("↓"), EQUAL("="), TOTAL(""), GREATER_NO_ZERO("↑");

        String name;

        @Override
        public String getSerializedName() {
            return name;
        }

        CriteriaType(String name) {
            this.name = name;
        }
    }

    public record CardCriteria(int value, CriteriaType criteriaType) {
        @Override
        public String toString() {
            return value + criteriaType.name;
        }

        public Component toDescriptiveString(boolean keycard) {
            if (keycard) {
                return Component.translatable("Criteria: %s", new ItemStack(KeycardType.values()[value].getCardForType()).getDisplayName());
            }
            return switch (criteriaType) {
                case GREATER -> value == 0 ? Component.translatable("co.criteria_greater_no_zero", value) : Component.translatable("co.criteria_greater", value);
                case LESSER -> Component.translatable("co.criteria_lesser", value);
                case EQUAL -> Component.translatable("co.criteria_equal", value);
                case TOTAL -> Component.translatable("co.criteria_total", value);
                case GREATER_NO_ZERO -> Component.translatable("co.criteria_greater_no_zero", value);
            };
        }
    }
}
