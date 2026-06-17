package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.nbt.CompoundTag;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.KeycardType;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

public class DoorData {

    RoomData parent;
    Type type;
    RoomDirection direction;
    private final EnumMap<CardCategory, CardCriteria> cardCriteria;

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
                //Random choice of criteria type
                CriteriaType type = CriteriaType.values()[Utils.randomWithRange(0, 3)];
                if (type == CriteriaType.TOTAL) {
                    value = Utils.randomWithRange(9, 30);
                } else {
                    value = Utils.randomWithRange(0, 9);
                }
                cardCriteria.put(CardCategory.values()[i], new CardCriteria(value, type));
            }
        } else if (type == Type.NORMAL) {
            int minValue = currentRoomValue + 1;
            if (minValue > 9) {
                minValue = 0;
            }
            int value = 0;
            if (minValue != 0) {
                value = Utils.randomWithRange(minValue, 9);
            }
            cardCriteria.put(CardCategory.RGB, new CardCriteria(value, CriteriaType.GREATER));
        }
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
     */
    public enum CriteriaType {
        GREATER, LESSER, EQUAL, TOTAL
    }

    public record CardCriteria(int value, CriteriaType criteriaType) {
        @Override
        public String toString() {
            String[] types = new String[]{"<", ">", "=", ""};
            return value + types[criteriaType.ordinal()];
        }
    }
}
