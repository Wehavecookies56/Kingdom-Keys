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
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
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

            gummiFire = createNewBlock("gummi_fire", ()-> new GummiWeaponBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), 1, 1, 25)),
            gummiBlizzard = createNewBlock("gummi_blizzard", ()-> new GummiWeaponBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), 1, 1, 50)),
            gummiGravity = createNewBlock("gummi_gravity", ()-> new GummiWeaponBlock(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), 1, 1, 100));


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
            gummiDispelDomes = new ArrayList<>()
    ;

    public static Supplier<List<Supplier<Block>>> gummiBlocks = () -> Stream.of(
            gummiCubes, gummiWedges, gummiPyramids, gummiCylinders, gummiPies, gummiRoundCorners, gummiCones, gummiDomes,
            gummiShellCubes, gummiShellWedges, gummiShellPyramids, gummiShellCylinders, gummiShellPies, gummiShellRoundCorners, gummiShellCones, gummiShellDomes,
            gummiDispelCubes, gummiDispelWedges, gummiDispelPyramids, gummiDispelCylinders, gummiDispelPies, gummiDispelRoundCorners, gummiDispelCones, gummiDispelDomes
    ).flatMap(Collection::stream).toList();

    static {
        createNewGummiBlock("gummi_cube", 1,5, gummiCubes);
        createNewEdgeGummiBlock("gummi_wedge",1, 3, gummiWedges);
        createNewCornerGummiBlock("gummi_pyramid",1, 3, gummiPyramids);
        createNewPillarGummiBlock("gummi_cylinder", 1,4, gummiCylinders);
        createNewEdgeGummiBlock("gummi_pie", 1,4, gummiPies);
        createNewCornerGummiBlock("gummi_round_corner", 1,3, gummiRoundCorners);
        createNewEndGummiBlock("gummi_cone", 1,3, gummiCones);
        createNewEndGummiBlock("gummi_dome", 1,3, gummiDomes);

        createNewGummiBlock("shell_gummi_cube", 1,10, gummiShellCubes);
        createNewEdgeGummiBlock("shell_gummi_wedge",1, 5, gummiShellWedges);
        createNewCornerGummiBlock("shell_gummi_pyramid",1, 5, gummiShellPyramids);
        createNewPillarGummiBlock("shell_gummi_cylinder", 1,7, gummiShellCylinders);
        createNewEdgeGummiBlock("shell_gummi_pie", 1,7, gummiShellPies);
        createNewCornerGummiBlock("shell_gummi_round_corner", 1,5, gummiShellRoundCorners);
        createNewEndGummiBlock("shell_gummi_cone", 1,5, gummiShellCones);
        createNewEndGummiBlock("shell_gummi_dome", 1,5, gummiShellDomes);

        createNewGummiBlock("dispel_gummi_cube", 1,15, gummiDispelCubes);
        createNewEdgeGummiBlock("dispel_gummi_wedge",1, 7, gummiDispelWedges);
        createNewCornerGummiBlock("dispel_gummi_pyramid",1, 7, gummiDispelPyramids);
        createNewPillarGummiBlock("dispel_gummi_cylinder", 1,12, gummiDispelCylinders);
        createNewEdgeGummiBlock("dispel_gummi_pie", 1,12, gummiDispelPies);
        createNewCornerGummiBlock("dispel_gummi_round_corner", 1,7, gummiDispelRoundCorners);
        createNewEndGummiBlock("dispel_gummi_cone", 1,7, gummiDispelCones);
        createNewEndGummiBlock("dispel_gummi_dome", 1,7, gummiDispelDomes);
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

    private static void createNewGummiBlock(String name, int weight, int armour, List<Supplier<Block>> list) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiBlockBase(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), weight, armour, dye, list));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            list.add(newBlock);
        }
    }

    private static void createNewEdgeGummiBlock(String name, int weight, int armour, List<Supplier<Block>> list) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiBlockEdge(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), weight, armour, dye, list));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            list.add(newBlock);
        }
    }

    private static void createNewCornerGummiBlock(String name, int weight, int armour, List<Supplier<Block>> list) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiBlockCorner(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), weight, armour, dye, list));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            list.add(newBlock);
        }
    }

    private static void createNewPillarGummiBlock(String name, int weight, int armour, List<Supplier<Block>> list) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiBlockPillar(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), weight, armour, dye, list));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            list.add(newBlock);
        }
    }

    private static void createNewEndGummiBlock(String name, int weight, int armour, List<Supplier<Block>> list) {
        for(DyeColor dye : DyeColor.values()) {
            Supplier<Block> newBlock = BLOCKS.register(name+"_"+dye.getName(), () -> new GummiBlockEnd(Block.Properties.of().noOcclusion().strength(0.1F, 10.0F), weight, armour, dye, list));
            createNewBlockItem(name+"_"+dye.getName(), newBlock);
            list.add(newBlock);
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
