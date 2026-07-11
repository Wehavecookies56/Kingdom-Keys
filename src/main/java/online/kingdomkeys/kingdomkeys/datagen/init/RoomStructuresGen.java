package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.FloorTypeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomStructureBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.util.KKResourceLocation;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomCategory;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomSize;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;

import java.util.List;

public class RoomStructuresGen extends BaseProvider<RoomStructureBuilder> {

    public RoomStructuresGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "castle_oblivion/room_structure");
    }

    @Override
    protected void build() {
        createRoomStructure("bottomless_darkness", "bottomless_darkness", RoomSize.L, List.of(RoomCategory.ENEMY), RoomStructure.L).notFloorSpecific().roomWhitelist(KKResourceLocation.of("bottomless_darkness"));
        createRoomStructure("conquerors_respite", "conquerors_respite", RoomSize.SPECIAL, List.of(RoomCategory.SPECIAL), RoomStructure.S).roomWhitelist(KKResourceLocation.of("conquerors_respite"));
        createRoomStructure("encounter_room", "encounter", RoomSize.S, List.of(RoomCategory.ENCOUNTER), new RoomStructure.RoomDimensions(24, 24));
        createRoomStructure("entrance_hall", "entrance_hall", RoomSize.SPECIAL, List.of(RoomCategory.SPECIAL), new RoomStructure.RoomDimensions(33, 69)).notFloorSpecific().roomWhitelist(KKResourceLocation.of("entrance_hall"));
        createRoomStructure("entrance_hall_1f", "entrance_hall_1f", RoomSize.SPECIAL, List.of(RoomCategory.SPECIAL), new RoomStructure.RoomDimensions(33, 69)).notFloorSpecific().roomWhitelist(KKResourceLocation.of("entrance_hall"));
        createRoomStructure("fallback", "fallback_room", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.S).notFloorSpecific().fallback();
        createRoomStructure("large_1", "large_1", RoomSize.L, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.L);
        createRoomStructure("large_2", "large_2", RoomSize.L, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.L);
        createRoomStructure("large_3", "large_3", RoomSize.L, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.L);
        createRoomStructure("medium_1", "medium_1", RoomSize.M, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.M);
        createRoomStructure("medium_2", "medium_2", RoomSize.M, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.M);
        createRoomStructure("medium_3", "medium_3", RoomSize.M, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.M);
        createRoomStructure("moments_reprieve", "moments_reprieve", RoomSize.S, List.of(RoomCategory.BOUNTY), RoomStructure.S).roomWhitelist(KKResourceLocation.of("moments_reprieve"));
        createRoomStructure("moogle_room", "moogle_room", RoomSize.S, List.of(RoomCategory.BOUNTY), RoomStructure.S).roomWhitelist(KKResourceLocation.of("moogle_room"));
        createRoomStructure("small_1", "small_1", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.S);
        createRoomStructure("small_2", "small_2", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.S);
        createRoomStructure("small_3", "small_3", RoomSize.S, List.of(RoomCategory.ENEMY, RoomCategory.STATUS, RoomCategory.BOUNTY), RoomStructure.S);
    }

    @Override
    public String getName() {
        return "Kingdom Keys Castle Oblivion Room Structures";
    }

    public RoomStructureBuilder createRoomStructure(String path, String structure, RoomSize size, List<RoomCategory> categories, RoomStructure.RoomDimensions dimensions) {
        return addBuilder(new RoomStructureBuilder(getLocation(path), structure, size, categories, dimensions));
    }
}
