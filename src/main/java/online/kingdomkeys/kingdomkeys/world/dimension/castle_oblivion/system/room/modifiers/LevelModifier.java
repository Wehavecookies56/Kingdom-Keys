package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

public class LevelModifier extends RoomModifierBase {

    List<Operation> operations;

    public enum Operator {
        ADD, SUBTRACT, MULTIPLY, SET
    }

    public LevelModifier(ResourceLocation registryName, List<Operation> operations) {
        super(registryName);
        this.operations = operations;
    }

    public record Operation(int amount, Operator operator) {
        int apply(int input) {
            return switch (operator) {
                case ADD -> input + amount;
                case SUBTRACT -> input - amount;
                case MULTIPLY -> input * amount;
                case SET -> amount;
            };
        }
    }

    @Override
    public void onSpawn(Room room, LivingEntity spawned) {
        if (!operations.isEmpty()) {
            GlobalData globalData = GlobalData.get(spawned);
            int level = globalData.getLevel();
            for (Operation operation : operations) {
                level = operation.apply(level);
            }
            globalData.setLevel(level);
        }
    }
}
