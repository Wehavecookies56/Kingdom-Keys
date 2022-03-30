package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Objects;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GhostBloxBlock;
import online.kingdomkeys.kingdomkeys.block.KKOreBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

public class BlockTagsGen extends BlockTagsProvider {
	public BlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		add(BlockTags.DRAGON_IMMUNE, ModBlocks.metalBlox.get());
		add(BlockTags.WITHER_IMMUNE, ModBlocks.metalBlox.get());
		// add(BlockTags.BEACON_BASE_BLOCKS, ModBlocks.metalBlox.get());
        for (RegistryObject<Block> itemRegistryObject : ModBlocks.BLOCKS.getEntries()) {
            final Block block = itemRegistryObject.get();
            String name = Objects.requireNonNull(block.getRegistryName()).getPath();

            if (block instanceof KKOreBlock) {
        		add(BlockTags.MINEABLE_WITH_PICKAXE, block);

            	if(name.contains("writhing_ore") || name.contains("betwixt_ore") || name.contains("pulsing_ore") || name.contains("sinister_ore") || name.contains("stormy_ore") || name.contains("twilight_ore")) {
            		add(BlockTags.NEEDS_IRON_TOOL, block);
            	} else {
            		add(BlockTags.NEEDS_STONE_TOOL, block);
            	}
            }
        }		
	}

	public void add(TagKey<Block> branch, Block block) {
		this.tag(branch).add(block);
	}

	public void add(TagKey<Block> branch, Block... block) {
		this.tag(branch).add(block);
	}
}
