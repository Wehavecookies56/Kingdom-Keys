package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EffectRoomModifier implements RoomModifier {

    Holder<MobEffect> effect;

    EffectType effectType;

    public enum EffectType implements StringRepresentable {
        PLAYER("PLAYER"), MOB("MOB"), BOTH("BOTH");

        final String name;

        EffectType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static final MapCodec<EffectRoomModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(EffectRoomModifier::getEffect),
                    StringRepresentable.fromEnum(EffectType::values).fieldOf("target").forGetter(EffectRoomModifier::getEffectType)
            ).apply(instance, EffectRoomModifier::new)
    );

    public EffectRoomModifier(Holder<MobEffect> effect, EffectType effectType) {
        this.effect = effect;
        this.effectType = effectType;
    }

    private Holder<MobEffect> getEffect() {
        return effect;
    }

    private EffectType getEffectType() {
        return effectType;
    }

    @Override
    public void onEnter(Room room, Player player) {
        if (effectType != EffectType.MOB) {
            player.addEffect(new MobEffectInstance(effect, -1, 0, false, true, true));
        }
    }

    @Override
    public void onExit(Room room, Player player) {
        if (effectType != EffectType.MOB) {
            player.removeEffect(effect);
        }
    }

    @Override
    public void onSpawn(Room room, LivingEntity spawned) {
        if (effectType != EffectType.PLAYER) {
            spawned.addEffect(new MobEffectInstance(effect, -1, 0, false, true, true));
        }
    }

    @Override
    public MapCodec<? extends RoomModifier> codec() {
        return CODEC;
    }

    @Override
    public RoomModifierType<? extends RoomModifier> type() {
        return ModRoomModifiers.EFFECT.get();
    }
}
