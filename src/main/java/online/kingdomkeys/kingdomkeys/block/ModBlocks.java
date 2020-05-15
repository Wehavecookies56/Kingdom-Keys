package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.BlockItemWrapper;

public class ModBlocks {

    public static Block normalBlox, hardBlox, metalBlox, dangerBlox, bounceBlox, blastBlox, ghostBlox, prizeBlox, rarePrizeBlox, magnetBlox;
	public static Block blazingOre, blazingOreN, brightOre, darkOre, darkOreN, darkOreE, denseOre, energyOre, energyOreN, frostOre, lucidOre, lightningOre, powerOre, powerOreE, remembranceOre, serenityOre, stormyOre, tranquilOre, twilightOre, twilightOreN;
    public static Block kkChest, orgPortal, pedestal;

    //Array of all blocks to reduce registry code
    private static final Block[] BLOCKS = {
            normalBlox = createNewBlock("normal_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            hardBlox = createNewBlock("hard_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 20.0F)),
            metalBlox = createNewBlock("metal_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(10.0F, 60.0F)),
            dangerBlox = new DangerBloxBlock("danger_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F)),
            bounceBlox = new BounceBloxBlock("bounce_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            blastBlox = new BlastBloxBlock("blast_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            ghostBlox = new GhostBloxBlock("ghost_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            prizeBlox = createNewBlock("prize_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            rarePrizeBlox = createNewBlock("rare_prize_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            magnetBlox = new MagnetBloxBlock("magnet_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 1.0F)),

            blazingOre = createNewBlock("blazing_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            blazingOreN = createNewBlock("blazing_ore_n", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            brightOre = createNewBlock("bright_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            darkOre = createNewBlock("dark_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            darkOreN = createNewBlock("dark_ore_n", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            darkOreE = createNewBlock("dark_ore_e", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            denseOre = createNewBlock("dense_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            energyOre = createNewBlock("energy_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            energyOreN = createNewBlock("energy_ore_n", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            frostOre = createNewBlock("frost_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            lucidOre = createNewBlock("lucid_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            lightningOre = createNewBlock("lightning_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            powerOre = createNewBlock("power_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            powerOreE = createNewBlock("power_ore_e", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            remembranceOre = createNewBlock("remembrance_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            serenityOre = createNewBlock("serenity_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            stormyOre = createNewBlock("stormy_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            tranquilOre = createNewBlock("tranquil_ore", Block.Properties.create(Material.IRON).harvestLevel(1).hardnessAndResistance(2.0F, 1.0F)),
            twilightOre = createNewBlock("twilight_ore", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F)),
            twilightOreN = createNewBlock("twilight_ore_n", Block.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(2.0F, 1.0F))
    };

    /**
     * Helper method to create basic blocks
     * @param name The registry name
     * @param properties The properties
     * @return The created block
     */
    private static Block createNewBlock(String name, Block.Properties properties) {
        return new Block(properties).setRegistryName(KingdomKeys.MODID, name);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {
        //Register every block from the blocks array
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(BLOCKS);
        }

        //Create and register the ItemBlock for each block in the blocks array
        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
            for (Block b : BLOCKS) {
                event.getRegistry().register(new BlockItemWrapper(b, KingdomKeys.miscGroup));
            }
        }

    }

}
