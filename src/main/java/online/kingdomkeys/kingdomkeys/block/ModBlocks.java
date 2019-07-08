package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ItemBlockWrapper;

public class ModBlocks {

    public static Block normalBlox, hardBlox, metalBlox, dangerBlox, bounceBlox, blastBlox, ghostBlox, prizeBlox, rarePrizeBlox;

    //Array of all blocks to reduce registry code
    private static final Block[] BLOCKS = {
            normalBlox = createNewBlock("normal_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            hardBlox = createNewBlock("hard_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 20.0F)),
            metalBlox = createNewBlock("metal_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(10.0F, 60.0F)),
            dangerBlox = new BlockDangerBlox("danger_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F)),
            bounceBlox = new BlockBounceBlox("bounce_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            blastBlox = new BlockBlastBlox("blast_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            ghostBlox = new BlockGhostBlox("ghost_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            prizeBlox = createNewBlock("prize_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F)),
            rarePrizeBlox = createNewBlock("rare_prize_blox", Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F))
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
                event.getRegistry().register(new ItemBlockWrapper(b, KingdomKeys.miscGroup));
            }
        }

    }

}
