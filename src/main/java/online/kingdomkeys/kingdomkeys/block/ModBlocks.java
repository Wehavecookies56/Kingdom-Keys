package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.Objects;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, KingdomKeys.MODID);

    public static final RegistryObject<Block>
            normalBlox = createNewBlock("normal_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            hardBlox = createNewBlock("hard_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 20.0F)),
            metalBlox = createNewBlock("metal_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(10.0F, 60.0F)),
            dangerBlox = createNewBlock("danger_blox", () -> new DangerBloxBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F))),
            bounceBlox = createNewBlock("bounce_blox", () -> new BounceBloxBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F))),
            blastBlox = createNewBlock("blast_blox", () -> new BlastBloxBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F))),
            ghostBlox = createNewBlock("ghost_blox", () -> new GhostBloxBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F))),
            prizeBlox = createNewBlock("prize_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            rarePrizeBlox = createNewBlock("rare_prize_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            magnetBlox = createNewBlock("magnet_blox", () -> new MagnetBloxBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 1.0F))),
            pairBlox = createNewBlock("pair_blox", () -> new PairBloxBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F))),

            blazingOre = createNewBlock("blazing_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            blazingOreN = createNewBlock("blazing_ore_n", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            soothingOre = createNewBlock("soothing_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            writhingOre = createNewBlock("writhing_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            writhingOreN = createNewBlock("writhing_ore_n", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            writhingOreE = createNewBlock("writhing_ore_e", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            betwixtOre = createNewBlock("betwixt_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            wellspringOre = createNewBlock("wellspring_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            wellspringOreN = createNewBlock("wellspring_ore_n", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            frostOre = createNewBlock("frost_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            lucidOre = createNewBlock("lucid_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            lightningOre = createNewBlock("lightning_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            pulsingOre = createNewBlock("pulsing_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            pulsingOreE = createNewBlock("pulsing_ore_e", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            remembranceOre = createNewBlock("remembrance_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            hungryOre = createNewBlock("hungry_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            stormyOre = createNewBlock("stormy_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            tranquilityOre = createNewBlock("tranquility_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            twilightOre = createNewBlock("twilight_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            twilightOreN = createNewBlock("twilight_ore_n", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),

            savepoint = createNewBlock("savepoint", () -> new SavePointBlock(Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F))),
    		kkChest = createNewBlock("kk_chest", () -> new KKChestBlock(Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F))),
    		soADoor = createNewBlock("soa_door", () -> new SoADoorBlock(Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F))),
    		pedestal = createNewBlock("pedestal", () -> new PedestalBlock(Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)))
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
    };

    private static <T extends Block> RegistryObject<T> createNewBlock(String name, Supplier<? extends T> block) {
        RegistryObject<T> newBlock = BLOCKS.register(name, block);
        createNewBlockItem(name, newBlock);
        return newBlock;
    }

    private static <T extends Block> void createNewBlockItem(String name, Supplier<? extends T> block) {
        Supplier<BlockItem> item = () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties().group(KingdomKeys.miscGroup));
        ModItems.ITEMS.register(name, item);
    }
}
