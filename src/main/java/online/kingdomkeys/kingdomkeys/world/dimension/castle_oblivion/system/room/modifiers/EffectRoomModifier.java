package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

public class EffectRoomModifier extends RoomModifierBase {

    Holder<MobEffect> effect;

    EffectType effectType;

    public enum EffectType {
        PLAYER, MOBS, BOTH
    }

    public EffectRoomModifier(ResourceLocation registryName, Holder<MobEffect> effect, EffectType effectType) {
        super(registryName);
        this.effect = effect;
        this.effectType = effectType;
    }

    @Override
    public void onEnter(Room room, Player player) {
        if (effectType != EffectType.MOBS) {
            player.addEffect(new MobEffectInstance(effect, -1, 0, false, true, true));
        }
    }

    @Override
    public void onExit(Room room, Player player) {
        if (effectType != EffectType.MOBS) {
            player.removeEffect(effect);
        }
    }

    @Override
    public void onSpawn(Room room, LivingEntity spawned) {
        if (effectType != EffectType.PLAYER) {
            spawned.addEffect(new MobEffectInstance(effect, -1, 0, false, true, true));
        }
    }
}
