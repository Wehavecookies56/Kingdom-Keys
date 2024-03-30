package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

public class BlockTagsGen extends BlockTagsProvider {
	
	public static final TagKey<Block> BLOX = create(KingdomKeys.MODID+":blox");

	public BlockTagsGen(DataGenerator generator, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), lookupProvider, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		add(BlockTags.DRAGON_IMMUNE, ModBlocks.metalBlox.get());
		add(BlockTags.WITHER_IMMUNE, ModBlocks.metalBlox.get());
		// add(BlockTags.BEACON_BASE_BLOCKS, ModBlocks.metalBlox.get());
        for (RegistryObject<Block> itemRegistryObject : ModBlocks.BLOCKS.getEntries()) {
            final Block block = itemRegistryObject.get();
            String name = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

            if (block instanceof Block) {
                if(block == ModBlocks.rodSand.get()) {
            		add(BlockTags.MINEABLE_WITH_SHOVEL, block);
                } else {
	        		add(BlockTags.MINEABLE_WITH_PICKAXE, block);
	
	            	if(name.contains("writhing_ore") || name.contains("betwixt_ore") || name.contains("pulsing_ore") || name.contains("sinister_ore") || name.contains("stormy_ore") || name.contains("twilight_ore")) {
	            		add(BlockTags.NEEDS_IRON_TOOL, block);
	            	} else {
	            		add(BlockTags.NEEDS_STONE_TOOL, block);
	            	}
                }
                if(block == ModBlocks.normalBlox.get() || block == ModBlocks.hardBlox.get() || block == ModBlocks.metalBlox.get() || block == ModBlocks.dangerBlox.get() || block == ModBlocks.blastBlox.get() || block == ModBlocks.prizeBlox.get() || block == ModBlocks.rarePrizeBlox.get() || block == ModBlocks.pairBlox.get() || block == ModBlocks.ghostBlox.get() || block == ModBlocks.bounceBlox.get() || block == ModBlocks.magnetBlox.get()) {
                	add(BLOX,block);
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

	 private static TagKey<Block> create(String pName) {
	      return TagKey.create(Registries.BLOCK, new ResourceLocation(pName));
	   }
}
