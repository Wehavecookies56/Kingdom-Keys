package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import com.mojang.serialization.MapCodec;

public record RoomModifierType<T extends RoomModifier>(MapCodec<T> codec) { }
