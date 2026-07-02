package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

public class LevelModifier implements RoomModifier {

    public enum Operator implements StringRepresentable {
        ADD("+"), SUBTRACT("-"), MULTIPLY("*"), SET("="), RAND("rand");

        final String name;

        Operator(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public record Operation(int amount, Operator operator) {
        int apply(int input) {
            return switch (operator) {
                case ADD -> input + amount;
                case SUBTRACT -> input - amount;
                case MULTIPLY -> input * amount;
                case SET -> amount;
                case RAND -> amount + Utils.randomWithRange(-3, 3);
            };
        }

        public static Codec<Operation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.INT.fieldOf("amount").forGetter(Operation::amount),
                StringRepresentable.fromEnum(Operator::values).fieldOf("operator").forGetter(Operation::operator)
            ).apply(instance, Operation::new)
        );
    }

    List<Operation> operations;

    public static final MapCodec<LevelModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Operation.CODEC.listOf().fieldOf("operations").forGetter(LevelModifier::getOperations)
            ).apply(instance, LevelModifier::new)
    );

    private LevelModifier(List<Operation> operations) {
        this.operations = operations;
    }

    private List<Operation> getOperations() {
        return operations;
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

    @Override
    public MapCodec<? extends RoomModifier> codec() {
        return CODEC;
    }

    @Override
    public RoomModifierType<? extends RoomModifier> type() {
        return ModRoomModifiers.LEVEL.get();
    }
}
