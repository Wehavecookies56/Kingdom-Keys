package online.kingdomkeys.kingdomkeys.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;
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

    record HasFlag(ResourceLocation flag, boolean present) implements DialogueCondition {
        public static final MapCodec<HasFlag> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("flag").forGetter(HasFlag::flag),
                        Codec.BOOL.optionalFieldOf("present", true).forGetter(HasFlag::present)
                ).apply(instance, HasFlag::new)
        );

        @Override
        public boolean test(ServerPlayer player, LivingEntity speaker) {
            PlayerData data = PlayerData.get(player);
            return data != null && data.hasFlag(flag) == present;
        }

        @Override
        public MapCodec<HasFlag> codec() {
            return CODEC;
        }

        @Override
        public Type<HasFlag> type() {
            return ModDialogue.HAS_FLAG.get();
        }
    }

    /**
     * The player is allowed to start this fight.
     *
     * <p>Asks the encounter itself rather than repeating its prerequisites here, so the chain is
     * written once, in the file that describes the fight.</p>
     */
    record CanStart(boolean duel, ResourceLocation encounter) implements DialogueCondition {
        public static final MapCodec<CanStart> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.BOOL.optionalFieldOf("duel", false).forGetter(CanStart::duel),
                        ResourceLocation.CODEC.fieldOf("encounter").forGetter(CanStart::encounter)
                ).apply(instance, CanStart::new)
        );

        @Override
        public boolean test(ServerPlayer player, LivingEntity speaker) {
            RoomEncounter found = (duel ? ModJsonRegistries.DUEL_ENCOUNTER : ModJsonRegistries.TRAINING_ENCOUNTER).get().getValue(encounter);
            return found != null && found.canStart(PlayerData.get(player));
        }

        @Override
        public MapCodec<CanStart> codec() {
            return CODEC;
        }

        @Override
        public Type<CanStart> type() {
            return ModDialogue.CAN_START.get();
        }
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
