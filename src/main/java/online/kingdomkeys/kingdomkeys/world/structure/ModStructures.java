package online.kingdomkeys.kingdomkeys.world.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.CastleOblivionEntranceHallStructure;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.CastleOblivionStructure;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.MiniCOStructure;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.MiniCOStructurePlacement;

import java.util.function.Supplier;

public class ModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, KingdomKeys.MODID);
    public static final DeferredRegister<StructurePlacementType<?>> PLACEMENTS = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, KingdomKeys.MODID);


    public static final Supplier<StructureType<CastleOblivionStructure>> CASTLE_OBLIVION = STRUCTURES.register("castle_oblivion", () -> () -> CastleOblivionStructure.CODEC);

    public static final Supplier<StructureType<CastleOblivionEntranceHallStructure>> CASTLE_OBLIVION_ENTRANCE_HALL = STRUCTURES.register("castle_oblivion_entrance_hall", () -> () -> CastleOblivionEntranceHallStructure.CODEC);

    public static final Supplier<StructureType<MiniCOStructure>> MINI_CO = STRUCTURES.register("mini_co", () -> () -> MiniCOStructure.CODEC);

    public static final Supplier<StructurePlacementType<MiniCOStructurePlacement>> MINI_CO_PLACEMENT = PLACEMENTS.register("mini_co", () -> () -> MiniCOStructurePlacement.CODEC);

}
