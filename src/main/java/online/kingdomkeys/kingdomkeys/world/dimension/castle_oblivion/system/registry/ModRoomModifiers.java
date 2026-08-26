package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.*;

public class ModRoomModifiers {

    public static DeferredRegister<RoomModifierType<?>> ROOM_MODIFIERS = DeferredRegister.create(KingdomKeys.rl("room_modifiers"), KingdomKeys.MODID);
    public static Registry<RoomModifierType<?>> registry = ROOM_MODIFIERS.makeRegistry(builder -> builder.sync(true));

    public static final DeferredHolder<RoomModifierType<?>, RoomModifierType<EffectRoomModifier>> EFFECT = ROOM_MODIFIERS.register("effect", () -> new RoomModifierType<>(EffectRoomModifier.CODEC));
    public static final DeferredHolder<RoomModifierType<?>, RoomModifierType<LevelModifier>> LEVEL = ROOM_MODIFIERS.register("level", () -> new RoomModifierType<>(LevelModifier.CODEC));
    public static final DeferredHolder<RoomModifierType<?>, RoomModifierType<SpawnMobModifier>> SPAWN = ROOM_MODIFIERS.register("spawn", () -> new RoomModifierType<>(SpawnMobModifier.CODEC));
    public static final DeferredHolder<RoomModifierType<?>, RoomModifierType<DropModifier>> DROP = ROOM_MODIFIERS.register("drop", () -> new RoomModifierType<>(DropModifier.CODEC));

}
