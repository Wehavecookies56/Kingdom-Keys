package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.*;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KingdomKeys.MODID);

    public static final Supplier<Block>
            normalBlox = createNewBlock("normal_blox", Block.Properties.of().mapColor(MapColor.COLOR_RED).strength(1.0F, 10.0F)),
            hardBlox = createNewBlock("hard_blox", Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(5.0F, 20.0F).requiresCorrectToolForDrops()),
            metalBlox = createNewBlock("metal_blox", Block.Properties.of().mapColor(MapColor.METAL).strength(10.0F, 60.0F).requiresCorrectToolForDrops()),
            dangerBlox = createNewBlock("danger_blox", () -> new DangerBloxBlock(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(1.0F))),
            bounceBlox = createNewBlock("bounce_blox", () -> new BounceBloxBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).strength(1.0F, 10.0F))),
            blastBlox = createNewBlock("blast_blox", () -> new BlastBloxBlock(Block.Properties.of().mapColor(MapColor.TERRACOTTA_RED).strength(1.0F, 10.0F))),
            ghostBlox = createNewBlock("ghost_blox", () -> new GhostBloxBlock(Block.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(1.0F, 10.0F))),
            prizeBlox = createNewBlock("prize_blox", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.COLOR_PINK).strength(1.0F, 10.0F))),
            rarePrizeBlox = createNewBlock("rare_prize_blox", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.GOLD).strength(1.0F, 10.0F))),
            magnetBlox = createNewBlock("magnet_blox", () -> new MagnetBloxBlock(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(1.0F, 1.0F))),
            pairBlox = createNewBlock("pair_blox", () -> new PairBloxBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(1.0F, 10.0F))),
            infestedNormalBlox = createNewBlock("infested_normal_blox", () -> new InfestedNormalBlox(Block.Properties.of().mapColor(MapColor.COLOR_RED).strength(1, 10))),

            //TODO HARVEST LEVEL REPLACED BY TAGS
            blazingOre = createNewBlock("blazing_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            blazingOreN = createNewBlock("blazing_ore_n", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            blazingOreD = createNewBlock("blazing_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            soothingOre = createNewBlock("soothing_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            soothingOreD = createNewBlock("soothing_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            writhingOre = createNewBlock("writhing_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            writhingOreN = createNewBlock("writhing_ore_n", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            writhingOreE = createNewBlock("writhing_ore_e", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            writhingOreD = createNewBlock("writhing_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            betwixtOre = createNewBlock("betwixt_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            betwixtOreD = createNewBlock("betwixt_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            betwixtOreE = createNewBlock("betwixt_ore_e", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            wellspringOre = createNewBlock("wellspring_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            wellspringOreN = createNewBlock("wellspring_ore_n", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            frostOre = createNewBlock("frost_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            frostOreD = createNewBlock("frost_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            lucidOre = createNewBlock("lucid_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            lightningOre = createNewBlock("lightning_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            pulsingOre = createNewBlock("pulsing_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            pulsingOreE = createNewBlock("pulsing_ore_e", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            pulsingOreD = createNewBlock("pulsing_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            remembranceOre = createNewBlock("remembrance_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            hungryOre = createNewBlock("hungry_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            sinisterOre = createNewBlock("sinister_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            sinisterOreD = createNewBlock("sinister_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            stormyOre = createNewBlock("stormy_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            stormyOreD = createNewBlock("stormy_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            tranquilityOre = createNewBlock("tranquility_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 1
            twilightOre = createNewBlock("twilight_ore", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            twilightOreN = createNewBlock("twilight_ore_n", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2
            twilightOreD = createNewBlock("twilight_ore_d", () -> new KKOreBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 1.0F))), //HL 2

            savepoint = createNewBlock("savepoint", () -> new SavePointBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2
            magicalChest = createNewBlock("magical_chest", () -> new MagicalChestBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2
    		soADoor = createNewBlock("soa_door", () -> new SoADoorBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2
    		pedestal = createNewBlock("pedestal", () -> new PedestalBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2
    		mosaic_stained_glass = createNewBlock("mosaic_stained_glass", () -> new MosaicStainedGlassBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).lightLevel(state -> state.getValue(MosaicStainedGlassBlock.STRUCTURE) ? 15 : 0).noOcclusion().sound(SoundType.GLASS).strength(1.0F, 10.0F))),

            station_of_awakening_core = createNewBlock("station_of_awakening_core", () -> new SoAPlatformCoreBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).noOcclusion().sound(SoundType.GLASS).strength(1.0F, 10.0F))),
            orgPortal = createNewBlock("org_portal", () -> new OrgPortalBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2
            moogleProjector = createNewBlock("moogle_projector", () -> new MoogleProjectorBlock(Block.Properties.of().mapColor(MapColor.METAL).lightLevel((state) -> 6).noOcclusion().strength(2F,1F))), //HL 0
            sorCore = createNewBlock("sor_core", () -> new SoRCore(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2
            dataPortal = createNewBlock("data_portal", () -> new DataPortalBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))),

            gummiHangar = createNewBlock("gummi_hangar", () -> new GummiHangarBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2.0F, 1.0F))), //HL 2

            cardDoor = createNewBlock("card_door", () -> new CardDoorBlock(Block.Properties.of().mapColor(MapColor.WOOD).strength(-1.0F, 3600000.0F))),
            structureWall = createNewBlock("structure_wall", () -> new StructureWallBlock(Block.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).lootFrom(() -> Blocks.AIR).isValidSpawn((p1, p2, p3, p4) -> false))),
            castleOblivionWall = createNewBlock("castle_oblivion_wall", () -> new CastleOblivionWallBlock(Block.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F))),
            castleOblivionWallChiseled = createNewBlock("castle_oblivion_wall_chiseled", () -> new CastleOblivionWallBlock(Block.Properties.of().mapColor(MapColor.STONE).noOcclusion().strength(-1.0F, 3600000.0F))),
            castleOblivionWall2 = createNewBlock("castle_oblivion_wall2", () -> new CastleOblivionWallBlock(Block.Properties.of().mapColor(MapColor.STONE).noOcclusion().strength(-1.0F, 3600000.0F))),
            castleOblivionWall3 = createNewBlock("castle_oblivion_wall3", () -> new CastleOblivionWallBlock(Block.Properties.of().mapColor(MapColor.STONE).noOcclusion().strength(-1.0F, 3600000.0F))),
            castleOblivionPillar = createNewBlock("castle_oblivion_pillar", () -> new CastleOblivionPillarBlock(Block.Properties.of().mapColor(MapColor.STONE).noOcclusion().strength(-1.0F, 3600000.0F))),
            castleOblivionStairs = createNewBlock("castle_oblivion_stairs", () -> new CastleOblivionStairBlock(Block.Properties.of().mapColor(MapColor.STONE).noOcclusion().strength(-1.0F, 3600000.0F))),
            castleOblivionSlab = createNewBlock("castle_oblivion_slab", () -> new CastleOblivionSlabBlock(Block.Properties.of().mapColor(MapColor.STONE).noOcclusion().strength(-1.0F, 3600000.0F))),
    
            rodSand = createNewBlock("rod_sand", Block.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.SAND).strength(1.0F, 10.0F)),
            rodStone = createNewBlock("rod_stone", Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.0F, 10.0F)),
            rodCrackedStone = createNewBlock("rod_cracked_stone", Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.0F, 10.0F).lightLevel(state -> 14)),

            airstepTarget = createNewBlock("airstep_target",()-> new AirStepBlock(Block.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.CHIME).strength(1.0F, 10.0F).lightLevel(state -> 10))),
            gummiMeteor = createNewBlock("gummi_meteor", Block.Properties.of().mapColor(MapColor.DIRT).strength(2.0F, 3600000.0F)),
            gummiCore = createNewBlock("gummi_core", ()-> new GummiCoreBlock(Block.Properties.of().mapColor(MapColor.DIAMOND).instrument(NoteBlockInstrument.CHIME).strength(1.0F, 3600000.0F))),

            gummiFire = createNewGummiWeaponBlock("gummi_fire", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.EDGE), GummiWeaponBlock.ShotType.FIRE, 2, 35),
            gummiFira = createNewGummiWeaponBlock("gummi_fira", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.MULTIBLOCK2D), GummiWeaponBlock.ShotType.FIRA, 3, 41),
            //gummiFiragaVertical = createNewGummiWeaponBlock("gummi_firaga_vertical", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.FIRAGA, 1, 1, 53, 53)),
            //gummiFiragaHorizontal = createNewGummiWeaponBlock("gummi_firaga_horizontal", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.FIRAGA, 1, 1, 53, 53)),
            gummiBlizzard = createNewGummiWeaponBlock("gummi_blizzard", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.EDGE), GummiWeaponBlock.ShotType.BLIZZARD, 2,71),
            gummiBlizzara = createNewGummiWeaponBlock("gummi_blizzara", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.MULTIBLOCK2D), GummiWeaponBlock.ShotType.BLIZZARA, 3,108),
            //gummiBlizzagaVertical = createNewGummiWeaponBlock("gummi_blizzaga_vertical", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.BLIZZAGA, 1, 1, 35,138)),
            //gummiBlizzagaHorizontal = createNewGummiWeaponBlock("gummi_blizzaga_horizontal", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.BLIZZAGA, 1, 1, 35,138)),
            gummiGravity = createNewGummiWeaponBlock("gummi_gravity", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.EDGE),  GummiWeaponBlock.ShotType.GRAVITY, 10, 145),
            gummiGravira = createNewGummiWeaponBlock("gummi_gravira", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.MULTIBLOCK2D), GummiWeaponBlock.ShotType.GRAVIRA, 15,155),
            //gummiGravigaVertical = createNewGummiWeaponBlock("gummi_graviga_vertical", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.GRAVIGA, 1, 1, 130,184)),
            //gummiGravigaHorizontal = createNewGummiWeaponBlock("gummi_graviga_horizontal", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.GRAVIGA, 1, 1, 130,184)),

            gummiWater = createNewGummiWeaponBlock("gummi_water", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.EDGE), GummiWeaponBlock.ShotType.WATER, 2, 0),
            gummiWatera = createNewGummiWeaponBlock("gummi_watera", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.MULTIBLOCK2D), GummiWeaponBlock.ShotType.WATERA, 3,0),
            //gummiWatergaVertical = createNewGummiWeaponBlock("gummi_waterga_vertical", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.WATERGA, 1, 1, 130,184)),
            //gummiWatergaHorizontal = createNewGummiWeaponBlock("gummi_waterga_horizontal", ()-> new GummiWeaponMultiBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), GummiWeaponBlock.ShotType.WATERGA, 1, 1, 130,184));

            gummiVernier = createNewGummiEngineBlock("gummi_vernier", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.END), 30),
            gummiThruster = createNewGummiEngineBlock("gummi_thruster", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.END), 40);
            //gummiBooster = createNewGummiEngineBlock("gummi_booster", GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.MULTIBLOCK3D), 50);



    public static List<Supplier<Block>>
            gummiCubes = new ArrayList<>(),
            gummiWedges = new ArrayList<>(),
            gummiPyramids = new ArrayList<>(),
            gummiCylinders = new ArrayList<>(),
            gummiPies = new ArrayList<>(),
            gummiRoundCorners = new ArrayList<>(),
            gummiCones = new ArrayList<>(),
            gummiDomes = new ArrayList<>(),

            gummiShellCubes = new ArrayList<>(),
            gummiShellWedges = new ArrayList<>(),
            gummiShellPyramids = new ArrayList<>(),
            gummiShellCylinders = new ArrayList<>(),
            gummiShellPies = new ArrayList<>(),
            gummiShellRoundCorners = new ArrayList<>(),
            gummiShellCones = new ArrayList<>(),
            gummiShellDomes = new ArrayList<>(),

            gummiDispelCubes = new ArrayList<>(),
            gummiDispelWedges = new ArrayList<>(),
            gummiDispelPyramids = new ArrayList<>(),
            gummiDispelCylinders = new ArrayList<>(),
            gummiDispelPies = new ArrayList<>(),
            gummiDispelRoundCorners = new ArrayList<>(),
            gummiDispelCones = new ArrayList<>(),
            gummiDispelDomes = new ArrayList<>(),

            gummiBubbleHelms = new ArrayList<>(),
            gummiAeroTriangles = new ArrayList<>(),
            gummiAeroSquares = new ArrayList<>()
        ;

    public static Supplier<List<Supplier<Block>>> gummiBlocks = () -> Stream.of(
            gummiCubes, gummiWedges, gummiPyramids, gummiCylinders, gummiPies, gummiRoundCorners, gummiCones, gummiDomes,
            gummiShellCubes, gummiShellWedges, gummiShellPyramids, gummiShellCylinders, gummiShellPies, gummiShellRoundCorners, gummiShellCones, gummiShellDomes,
            gummiDispelCubes, gummiDispelWedges, gummiDispelPyramids, gummiDispelCylinders, gummiDispelPies, gummiDispelRoundCorners, gummiDispelCones, gummiDispelDomes
    ).flatMap(Collection::stream).toList();

    static {
        createNewGummiBlock("gummi_cube", gummiCubes, GummiBlockProperties.of(1,5, 0));
        createNewGummiBlock("gummi_wedge", gummiWedges, GummiBlockProperties.of(1, 3, 0).withPlacement(GummiPlacementType.EDGE));
        createNewGummiBlock("gummi_pyramid", gummiPyramids, GummiBlockProperties.of(1, 3, 0).withPlacement(GummiPlacementType.CORNER));
        createNewGummiBlock("gummi_cylinder", gummiCylinders, GummiBlockProperties.of(1,4, 0).withPlacement(GummiPlacementType.PILLAR));
        createNewGummiBlock("gummi_pie", gummiPies, GummiBlockProperties.of(1,4, 0).withPlacement(GummiPlacementType.EDGE));
        createNewGummiBlock("gummi_round_corner", gummiRoundCorners, GummiBlockProperties.of(1,3, 0).withPlacement(GummiPlacementType.CORNER));
        createNewGummiBlock("gummi_cone", gummiCones, GummiBlockProperties.of(1,3, 0).withPlacement(GummiPlacementType.END));
        createNewGummiBlock("gummi_dome", gummiDomes, GummiBlockProperties.of(1,3, 0).withPlacement(GummiPlacementType.END));

        createNewGummiBlock("shell_gummi_cube", gummiShellCubes, GummiBlockProperties.of(1,10, 0));
        createNewGummiBlock("shell_gummi_wedge", gummiShellWedges, GummiBlockProperties.of(1, 5, 0).withPlacement(GummiPlacementType.EDGE));
        createNewGummiBlock("shell_gummi_pyramid", gummiShellPyramids, GummiBlockProperties.of(1, 5, 0).withPlacement(GummiPlacementType.CORNER));
        createNewGummiBlock("shell_gummi_cylinder", gummiShellCylinders, GummiBlockProperties.of(1, 7, 0).withPlacement(GummiPlacementType.PILLAR));
        createNewGummiBlock("shell_gummi_pie", gummiShellPies, GummiBlockProperties.of(1,7, 0).withPlacement(GummiPlacementType.EDGE));
        createNewGummiBlock("shell_gummi_round_corner", gummiShellRoundCorners, GummiBlockProperties.of(1,5, 0).withPlacement(GummiPlacementType.CORNER));
        createNewGummiBlock("shell_gummi_cone", gummiShellCones, GummiBlockProperties.of(1,5, 0).withPlacement(GummiPlacementType.END));
        createNewGummiBlock("shell_gummi_dome", gummiShellDomes, GummiBlockProperties.of(1,5, 0).withPlacement(GummiPlacementType.END));

        createNewGummiBlock("dispel_gummi_cube", gummiDispelCubes, GummiBlockProperties.of(1,15, 0));
        createNewGummiBlock("dispel_gummi_wedge", gummiDispelWedges, GummiBlockProperties.of(1, 7, 0).withPlacement(GummiPlacementType.EDGE));
        createNewGummiBlock("dispel_gummi_pyramid", gummiDispelPyramids, GummiBlockProperties.of(1, 7, 0).withPlacement(GummiPlacementType.CORNER));
        createNewGummiBlock("dispel_gummi_cylinder", gummiDispelCylinders, GummiBlockProperties.of(1,12, 0).withPlacement(GummiPlacementType.PILLAR));
        createNewGummiBlock("dispel_gummi_pie", gummiDispelPies, GummiBlockProperties.of(1,12, 0).withPlacement(GummiPlacementType.EDGE));
        createNewGummiBlock("dispel_gummi_round_corner", gummiDispelRoundCorners, GummiBlockProperties.of(1,7, 0).withPlacement(GummiPlacementType.CORNER));
        createNewGummiBlock("dispel_gummi_cone", gummiDispelCones, GummiBlockProperties.of(1,7, 0).withPlacement(GummiPlacementType.END));
        createNewGummiBlock("dispel_gummi_dome", gummiDispelDomes, GummiBlockProperties.of(1,7, 0).withPlacement(GummiPlacementType.END));

        Vec3 seat1 = new Vec3(0.5F, 0F, 0F); //first is 0.5F positive (to the center, first row)
        Vec3 seat2 = new Vec3(0F, 0F, 1F);//second is right behind the 0,0 block
        Vec3 seat3 = new Vec3(1F, 0F, 1F);//third is right next to the second, opposite to 0,0
        createNewGummiCockpitBlock("gummi_bubble_helm", gummiBubbleHelms, GummiBlockProperties.of(2, 40, 0).withPlacement(GummiPlacementType.MULTIBLOCK3D), seat1,seat2,seat3);

        createNewGummiAeroBlock("gummi_aero_square", gummiAeroSquares, GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.EDGE), 10);
        createNewGummiAeroBlock("gummi_aero_triangle", gummiAeroTriangles, GummiBlockProperties.of(1, 1, 0).withPlacement(GummiPlacementType.EDGE), 10);
    }

    /**
     * Helper method to create basic blocks
     * @param name The registry name
     * @param properties The properties
     * @return The created block
     */
    private static Supplier<Block> createNewBlock(String name, Block.Properties properties) {
        Supplier<Block> newBlock = BLOCKS.register(name, () -> new Block(properties));
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static void createNewGummiBlock(String name, List<Supplier<Block>> blocks, GummiBlockProperties gummiBlockProperties) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiBlockBase(gummiBlockProperties.withColour(dye, blocks)));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            blocks.add(newBlock);
        }
    }

    private static Supplier<Block> createNewGummiWeaponBlock(String name, GummiBlockProperties gummiBlockProperties, GummiWeaponBlock.ShotType shotType, int firepower, int fuelPerShot) {
        Supplier<Block> newBlock = BLOCKS.register(name, () -> new GummiWeaponBlock(gummiBlockProperties, shotType, firepower, fuelPerShot));
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static Supplier<Block> createNewGummiEngineBlock(String name, GummiBlockProperties gummiBlockProperties, int speed) {
        Supplier<Block> newBlock = BLOCKS.register(name, () -> new GummiEngineBlock(gummiBlockProperties, speed));
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static void createNewGummiAeroBlock(String name, List<Supplier<Block>> blocks, GummiBlockProperties gummiBlockProperties, int mobility) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiAeroBlock(gummiBlockProperties.withColour(dye, blocks), mobility));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            blocks.add(newBlock);
        }
    }

    private static void createNewGummiCockpitBlock(String name, List<Supplier<Block>> blocks, GummiBlockProperties gummiBlockProperties, Vec3... seats) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiCockpitBlock(gummiBlockProperties.withColour(dye, blocks), List.of(seats)));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            blocks.add(newBlock);
        }
    }

    private static Supplier<Block> createNewBlock(String name, Block.Properties properties, CreativeModeTab tab) {
        Supplier<Block> newBlock = BLOCKS.register(name, () -> new Block(properties));
        createNewBlockItem(name, newBlock, tab);
        return newBlock;
    }

    private static <T extends Block> Supplier<T> createNewBlock(String name, Supplier<? extends T> block) {
        Supplier<T> newBlock = BLOCKS.register(name, block);
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static <T extends Block> void createNewBlockItem(String name, Supplier<? extends T> block) {
        Supplier<BlockItem> item = () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        ModItems.ITEMS.register(name, item);
    }

    private static <T extends Block> void createNewBlockItem(String name, Supplier<? extends T> block, CreativeModeTab tab) {
        Supplier<BlockItem> item = () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        ModItems.ITEMS.register(name, item);
    }
}
