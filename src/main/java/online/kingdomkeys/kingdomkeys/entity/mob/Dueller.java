package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.TrainingHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public interface Dueller extends KeybladeWielder {
    int MAGIC_FROM = 20;

    int SECOND_GRADE_FROM = 40;
    int THIRD_GRADE_FROM = 60;

    int BEAT_TICKS = 20;
    int COUNTDOWN_BEATS = 3;
    int READY_TICKS = BEAT_TICKS * COUNTDOWN_BEATS;

    double LEAVE_RANGE = 48.0D;
    int LEAVE_GRACE = 60;

    int CURE_AT = 25;
    int ENDING_TICKS = 60;

    int DOWN_TICKS = 70;
    int RISE_TICKS = 25;

    int LOWEST = 1;
    int HIGHEST = 100;

    record Scale(Holder<Attribute> attribute, double base, double perLevel, double cap) {
        public Scale(Holder<Attribute> attribute, double base, double perLevel) {
            this(attribute, base, perLevel, Double.MAX_VALUE);
        }

        public double at(int level) {
            return Math.min(cap, base + perLevel * (Mth.clamp(level, LOWEST, HIGHEST) - LOWEST));
        }
    }

    List<Scale> WORTH = List.of(
            new Scale(Attributes.MAX_HEALTH, 80.0D, 12.0D),
            new Scale(Attributes.ATTACK_DAMAGE, 3.0D, 0.18D),
            new Scale(Attributes.MOVEMENT_SPEED, 0.25D, 0.0013D),
            new Scale(Attributes.ARMOR, 0.0D, 0.1D),
            new Scale(Attributes.KNOCKBACK_RESISTANCE, 0.0D, 0.01D, 0.9D)
    );

    static void setWorth(LivingEntity fighter, int level) {
        each(fighter, (instance, scale) -> instance.setBaseValue(scale.at(level)));
    }

    static void lendWorth(LivingEntity fighter, int level, ResourceLocation id) {
        takeBack(fighter, id);

        each(fighter, (instance, scale) -> {
            double missing = scale.at(level) - instance.getBaseValue();

            if (missing != 0.0D) {
                instance.addTransientModifier(new AttributeModifier(id, missing, AttributeModifier.Operation.ADD_VALUE));
            }
        });
    }

    static void takeBack(LivingEntity fighter, ResourceLocation id) {
        each(fighter, (instance, scale) -> instance.removeModifier(id));
    }

    private static void each(LivingEntity fighter, BiConsumer<AttributeInstance, Scale> what) {
        for (Scale scale : WORTH) {
            AttributeInstance instance = fighter.getAttribute(scale.attribute());

            if (instance != null) {
                what.accept(instance, scale);
            }
        }
    }

    static float yieldingBlow(LivingEntity fighter, float damage) {
        if (!(fighter instanceof Dueller dueller) || !dueller.isDuelling()) {
            return damage;
        }

        if (damage < fighter.getHealth() + fighter.getAbsorptionAmount()) {
            return damage;
        }

        dueller.concedeDuel();
        TrainingHandler.beaten(fighter);
        return 0F;
    }

    Duel duel();

    int getDuelLevel();

    void beginDuel(Player pupil, int duelLevel);

    default void onBeaten() {
    }

    default boolean fallsWhenBeaten() {
        return true;
    }

    void bowOut();

    default boolean isSettled() {
        return duel().isSettled();
    }

    default boolean isDuelling() {
        return !isSettled() && getDuelist() != null;
    }

    default boolean isPreparing() {
        return duel().isPreparing();
    }

    @Nullable
    default ServerPlayer getDuelist() {
        return duel().getDuelist();
    }

    default void setDuelist(Player pupil) {
        duel().setDuelist(pupil);
    }

    default boolean isDuelist(Player pupil) {
        return duel().isDuelist(pupil);
    }

    default void loseDuel() {
        duel().end(true);
    }

    default void concedeDuel() {
        duel().end(false);
    }
}
