package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

public interface RoomModifier {

    //Do something when the player enters the room
    default void onEnter(Room room, Player player) {}

    //Do something when the room is generated
    default void onGenerate(Room room, ServerLevel level) {}

    //Do something when the player exits the room
    default void onExit(Room room, Player player) {}

    //Do something while the room ticks
    default void tick(Room room, List<Player> players) {}

    //Do something when a mob is spawned
    default void onSpawn(Room room, LivingEntity spawned) {}

    MapCodec<? extends RoomModifier> codec();
    RoomModifierType<? extends RoomModifier> type();
    Codec<RoomModifier> CODEC = ModRoomModifiers.registry.byNameCodec().dispatch(RoomModifier::type, RoomModifierType::codec);
}
