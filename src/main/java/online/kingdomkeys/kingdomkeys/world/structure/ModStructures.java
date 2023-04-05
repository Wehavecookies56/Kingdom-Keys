package online.kingdomkeys.kingdomkeys.world.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.CastleOblivionLobbyStructure;
import online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion.CastleOblivionStructure;

public class ModStructures {

    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, KingdomKeys.MODID);


    public static final RegistryObject<StructureType<CastleOblivionStructure>> CASTLE_OBLIVION = STRUCTURES.register("castle_oblivion", () -> () -> CastleOblivionStructure.CODEC);

    public static final RegistryObject<StructureType<CastleOblivionLobbyStructure>> CASTLE_OBLIVION_LOBBY = STRUCTURES.register("castle_oblivion_lobby", () -> () -> CastleOblivionLobbyStructure.CODEC);

}
