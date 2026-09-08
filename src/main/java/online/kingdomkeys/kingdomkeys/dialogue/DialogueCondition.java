package online.kingdomkeys.kingdomkeys.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.lib.Union;

import java.util.List;
import java.util.Optional;

public interface DialogueCondition {
    boolean test(ServerPlayer player, LivingEntity speaker);

    MapCodec<? extends DialogueCondition> codec();

    Type<? extends DialogueCondition> type();

    Codec<DialogueCondition> CODEC = ModDialogue.CONDITIONS.byNameCodec().dispatch(DialogueCondition::type, Type::codec);

    record Type<T extends DialogueCondition>(MapCodec<T> codec) { }

    /** True only when every one of them is. An empty list is always true. */
    static boolean all(List<DialogueCondition> conditions, ServerPlayer player, LivingEntity speaker) {
        return conditions.stream().allMatch(condition -> condition.test(player, speaker));
    }

    /** The player is far enough along. Either bound can be left out. */
    record AtLevel(int min, int max) implements DialogueCondition {

        public static final MapCodec<AtLevel> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.INT.optionalFieldOf("min", 0).forGetter(AtLevel::min),
                        Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(AtLevel::max)
                ).apply(instance, AtLevel::new)
        );

        @Override
        public boolean test(ServerPlayer player, LivingEntity speaker) {
            PlayerData data = PlayerData.get(player);
            return data != null && data.getLevel() >= min && data.getLevel() <= max;
        }

        @Override
        public MapCodec<AtLevel> codec() {
            return CODEC;
        }

        @Override
        public Type<AtLevel> type() {
            return ModDialogue.AT_LEVEL.get();
        }
    }

    record InUnion(Optional<Union> union) implements DialogueCondition {
        public static final MapCodec<InUnion> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Union.CODEC.optionalFieldOf("union").forGetter(InUnion::union)
            ).apply(instance, InUnion::new)
        );

        @Override
        public boolean test(ServerPlayer player, LivingEntity speaker) {
            PlayerData data = PlayerData.get(player);

            if (data == null || !data.hasUnion()) {
                return false;
            }

            Union wanted = union.orElseGet(() -> speaker instanceof ForetellerEntity master ? master.getUnion() : null);
            return wanted != null && data.getUnion() == wanted;
        }

        @Override
        public MapCodec<InUnion> codec() {
            return CODEC;
        }

        @Override
        public Type<InUnion> type() {
            return ModDialogue.IN_UNION.get();
        }
    }
}
