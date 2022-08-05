package online.kingdomkeys.kingdomkeys.block;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KingdomKeys.MODID);

    public static final RegistryObject<Block>
            normalBlox = createNewBlock("normal_blox", Block.Properties.of(Material.METAL).strength(1.0F, 10.0F)),
            hardBlox = createNewBlock("hard_blox", Block.Properties.of(Material.METAL).strength(5.0F, 20.0F)),
            metalBlox = createNewBlock("metal_blox", Block.Properties.of(Material.METAL).strength(10.0F, 60.0F)),
            dangerBlox = createNewBlock("danger_blox", () -> new DangerBloxBlock(Block.Properties.of(Material.METAL).strength(1.0F))),
            bounceBlox = createNewBlock("bounce_blox", () -> new BounceBloxBlock(Block.Properties.of(Material.METAL).strength(1.0F, 10.0F))),
            blastBlox = createNewBlock("blast_blox", () -> new BlastBloxBlock(Block.Properties.of(Material.METAL).strength(1.0F, 10.0F))),
            ghostBlox = createNewBlock("ghost_blox", () -> new GhostBloxBlock(Block.Properties.of(Material.METAL).strength(1.0F, 10.0F))),
            prizeBlox = createNewBlock("prize_blox", () -> new KKOreBlock(Block.Properties.of(Material.METAL).strength(1.0F, 10.0F))),
            rarePrizeBlox = createNewBlock("rare_prize_blox", () -> new KKOreBlock(Block.Properties.of(Material.METAL).strength(1.0F, 10.0F))),
            magnetBlox = createNewBlock("magnet_blox", () -> new MagnetBloxBlock(Block.Properties.of(Material.METAL).strength(1.0F, 1.0F))),
            pairBlox = createNewBlock("pair_blox", () -> new PairBloxBlock(Block.Properties.of(Material.METAL).strength(1.0F, 10.0F))),

            //TODO HARVEST LEVEL REPLACED BY TAGS
            blazingOre = createNewBlock("blazing_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            blazingOreN = createNewBlock("blazing_ore_n", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            blazingOreD = createNewBlock("blazing_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            soothingOre = createNewBlock("soothing_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            soothingOreD = createNewBlock("soothing_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            writhingOre = createNewBlock("writhing_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            writhingOreN = createNewBlock("writhing_ore_n", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            writhingOreE = createNewBlock("writhing_ore_e", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            writhingOreD = createNewBlock("writhing_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            betwixtOre = createNewBlock("betwixt_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            betwixtOreD = createNewBlock("betwixt_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            wellspringOre = createNewBlock("wellspring_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            wellspringOreN = createNewBlock("wellspring_ore_n", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            frostOre = createNewBlock("frost_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            frostOreD = createNewBlock("frost_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            lucidOre = createNewBlock("lucid_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            lightningOre = createNewBlock("lightning_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F).lightLevel((state) -> 4))), //HL 1
            pulsingOre = createNewBlock("pulsing_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            pulsingOreE = createNewBlock("pulsing_ore_e", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            pulsingOreD = createNewBlock("pulsing_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            remembranceOre = createNewBlock("remembrance_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            hungryOre = createNewBlock("hungry_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            sinisterOre = createNewBlock("sinister_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            sinisterOreD = createNewBlock("sinister_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            stormyOre = createNewBlock("stormy_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            stormyOreD = createNewBlock("stormy_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            tranquilityOre = createNewBlock("tranquility_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 1
            twilightOre = createNewBlock("twilight_ore", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            twilightOreN = createNewBlock("twilight_ore_n", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2
            twilightOreD = createNewBlock("twilight_ore_d", () -> new KKOreBlock(Block.Properties.of(Material.STONE).strength(2.0F, 1.0F))), //HL 2

            savepoint = createNewBlock("savepoint", () -> new SavePointBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
    		magicalChest = createNewBlock("magical_chest", () -> new MagicalChestBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
    		soADoor = createNewBlock("soa_door", () -> new SoADoorBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
    		pedestal = createNewBlock("pedestal", () -> new PedestalBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
    		mosaic_stained_glass = createNewBlock("mosaic_stained_glass", () -> new MosaicStainedGlassBlock(Block.Properties.of(Material.GLASS).noOcclusion().sound(SoundType.GLASS).strength(1.0F, 10.0F))),
       		//mosaic_stained_glass = createNewBlock("mosaic_stained_glass", Block.Properties.create(Material.GLASS).notSolid().hardnessAndResistance(1.0F, 10.0F)),
       		//mosaic_stained_glass = createNewBlock("mosaircfdxc_stained_glass", () -> new KKGlassBlock(Block.Properties.create(Material.GLASS).notSolid().hardnessAndResistance(1.0F, 10.0F))),

            station_of_awakening_core = createNewBlock("station_of_awakening_core", () -> new SoAPlatformCoreBlock(Block.Properties.of(Material.GLASS).noOcclusion().sound(SoundType.GLASS).strength(1.0F, 10.0F))),
            orgPortal = createNewBlock("org_portal", () -> new OrgPortalBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
            moogleProjector = createNewBlock("moogle_projector", () -> new MoogleProjectorBlock(Block.Properties.of(Material.METAL).lightLevel((state) -> 6).noOcclusion().strength(2F,1F))), //HL 0
            gummiEditor = createNewBlock("gummi_editor", () -> new GummiEditorBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
            sorCore = createNewBlock("sor_core", () -> new SoRCore(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F))), //HL 2
            dataPortal = createNewBlock("data_portal", () -> new DataPortalBlock(Block.Properties.of(Material.METAL).strength(2.0F, 1.0F)))
    ;

    /**
     * Helper method to create basic blocks
     * @param name The registry name
     * @param properties The properties
     * @return The created block
     */
    private static RegistryObject<Block> createNewBlock(String name, Block.Properties properties) {
        RegistryObject<Block> newBlock = BLOCKS.register(name, () -> new Block(properties));
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static RegistryObject<Block> createNewBlock(String name, Block.Properties properties, CreativeModeTab tab) {
        RegistryObject<Block> newBlock = BLOCKS.register(name, () -> new Block(properties));
        createNewBlockItem(name, newBlock, tab);
        return newBlock;
    }

    private static <T extends Block> RegistryObject<T> createNewBlock(String name, Supplier<? extends T> block) {
        RegistryObject<T> newBlock = BLOCKS.register(name, block);
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static <T extends Block> void createNewBlockItem(String name, Supplier<? extends T> block) {
        Supplier<BlockItem> item = () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties().tab(KingdomKeys.miscGroup));
        ModItems.ITEMS.register(name, item);
    }

    private static <T extends Block> void createNewBlockItem(String name, Supplier<? extends T> block, CreativeModeTab tab) {
        Supplier<BlockItem> item = () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties().tab(tab));
        ModItems.ITEMS.register(name, item);
    }
}
