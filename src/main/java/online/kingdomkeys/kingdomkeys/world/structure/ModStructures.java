package online.kingdomkeys.kingdomkeys.world.structure;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.CastleOblivionLobbyStructure;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.CastleOblivionStructure;

public class ModStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, KingdomKeys.MODID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_0 = STRUCTURES.register("castle_oblivion_0", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_1 = STRUCTURES.register("castle_oblivion_1", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_2 = STRUCTURES.register("castle_oblivion_2", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_3 = STRUCTURES.register("castle_oblivion_3", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_4 = STRUCTURES.register("castle_oblivion_4", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_5 = STRUCTURES.register("castle_oblivion_5", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_6 = STRUCTURES.register("castle_oblivion_6", CastleOblivionStructure::new);
    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CASTLE_OBLIVION_LOBBY = STRUCTURES.register("castle_oblivion_lobby", CastleOblivionLobbyStructure::new);

}
