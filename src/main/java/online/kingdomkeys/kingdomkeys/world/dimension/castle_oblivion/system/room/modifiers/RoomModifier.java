package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

public abstract class RoomModifier {

    //Do something when the player enters the room
    public abstract void onEnter(Room room, Player player);

    //Do something when the room is generated
    public abstract void onGenerate(Room room);

    //Do something when the player exits the room
    public abstract void onExit(Room room, Player player);

    //Do something while the room ticks
    public abstract void tick(Room room, List<Player> players);

    public ResourceLocation modifierName;

    public RoomModifier(ResourceLocation modifierName) {
        this.modifierName = modifierName;
    }

}
