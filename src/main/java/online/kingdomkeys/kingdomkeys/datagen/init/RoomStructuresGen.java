package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomStructureBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomCategory;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomSize;

import java.util.List;

public class RoomStructuresGen extends BaseProvider<RoomStructureBuilder> {

    public RoomStructuresGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "castle_oblivion/room_structure");
    }

    @Override
    protected void build() {
        createRoomStructure("bottomless_darkness", "bottomless_darkness", RoomSize.L, List.of(RoomCategory.ENEMY)).notFloorSpecific().roomWhitelist(KingdomKeys.rl("bottomless_darkness"));
        createRoomStructure("conquerors_respite", "conquerors_respite", RoomSize.SPECIAL, List.of(RoomCategory.SPECIAL)).roomWhitelist(KingdomKeys.rl("conquerors_respite"));
        createRoomStructure("encounter_room", "encounter", RoomSize.S, List.of(RoomCategory.ENCOUNTER));
        createRoomStructure("entrance_hall", "entrance_hall", RoomSize.SPECIAL, List.of(RoomCategory.SPECIAL)).notFloorSpecific().roomWhitelist(KingdomKeys.rl("entrance_hall"));
        createRoomStructure("entrance_hall_1f", "entrance_hall_1f", RoomSize.SPECIAL, List.of(RoomCategory.SPECIAL)).notFloorSpecific().roomWhitelist(KingdomKeys.rl("entrance_hall"));
        createRoomStructure("fallback", "fallback_room", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY)).notFloorSpecific().fallback();
        createRoomStructure("large_1", "large_1", RoomSize.L, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("large_2", "large_2", RoomSize.L, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("large_3", "large_3", RoomSize.L, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("medium_1", "medium_1", RoomSize.M, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("medium_2", "medium_2", RoomSize.M, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("medium_3", "medium_3", RoomSize.M, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("moments_reprieve", "moments_reprieve", RoomSize.S, List.of(RoomCategory.BOUNTY)).roomWhitelist(KingdomKeys.rl("moments_reprieve"));
        createRoomStructure("moogle_room", "moogle_room", RoomSize.S, List.of(RoomCategory.BOUNTY)).roomWhitelist(KingdomKeys.rl("moogle_room"));
        createRoomStructure("small_1", "small_1", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("small_2", "small_2", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("small_3", "small_3", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS));
        createRoomStructure("treasure_1", "treasure", RoomSize.S, List.of(RoomCategory.BOUNTY));
    }

    @Override
    public String getName() {
        return "Kingdom Keys Castle Oblivion Room Structures";
    }

    public RoomStructureBuilder createRoomStructure(String path, String structure, RoomSize size, List<RoomCategory> categories) {
        return addBuilder(new RoomStructureBuilder(getLocation(path), structure, size, categories));
    }
}
