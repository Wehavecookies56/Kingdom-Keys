package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BlockTagsGen extends BlockTagsProvider {
	
	public static final TagKey<Block> BLOX = create(KingdomKeys.MODID+":blox");
	public static final TagKey<Block> BLAZING = create("c:ores/blazing");
	public static final TagKey<Block> SOOTHING = create("c:ores/soothing");
	public static final TagKey<Block> WRITHING = create("c:ores/writhing");
	public static final TagKey<Block> BETWIXT = create("c:ores/betwixt");
	public static final TagKey<Block> WELLSPRING = create("c:ores/wellspring");
	public static final TagKey<Block> FROST = create("c:ores/frost");
	public static final TagKey<Block> LUCID = create("c:ores/lucid");
	public static final TagKey<Block> LIGHTNING = create("c:ores/lightning");
	public static final TagKey<Block> PULSING = create("c:ores/pulsing");
	public static final TagKey<Block> REMEMBRANCE = create("c:ores/remembrance");
	public static final TagKey<Block> HUNGRY = create("c:ores/hungry");
	public static final TagKey<Block> SINISTER = create("c:ores/sinister");
	public static final TagKey<Block> STORMY = create("c:ores/stormy");
	public static final TagKey<Block> TRANQUILITY = create("c:ores/tranquility");
	public static final TagKey<Block> TWILIGHT = create("c:ores/twilight");
	public static final TagKey<Block> GUMMI_DROPS = create(KingdomKeys.MODID+":gummi_drops");
	public static final TagKey<Block> BANNED_GUMMI_BLOCKS = create(KingdomKeys.MODID+":banned_gummi");
	public static final TagKey<Block> GUMMI_BLOCK = create(KingdomKeys.MODID+":gummi_block");


	public BlockTagsGen(DataGenerator generator, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), lookupProvider, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		add(BlockTags.DRAGON_IMMUNE, ModBlocks.metalBlox.get());
		add(BlockTags.WITHER_IMMUNE, ModBlocks.metalBlox.get());

		add(GUMMI_DROPS,Blocks.PISTON);
		add(BANNED_GUMMI_BLOCKS, ModBlocks.savepoint.get());
		add(BANNED_GUMMI_BLOCKS, ModBlocks.gummiHangar.get());

		// add(BlockTags.BEACON_BASE_BLOCKS, ModBlocks.metalBlox.get());
        for (DeferredHolder<Block, ? extends Block> itemRegistryObject : ModBlocks.BLOCKS.getEntries()) {
            final Block block = itemRegistryObject.get();
            String name = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath();

            if (block instanceof Block) {
				if(block instanceof GummiBlockBase){
					add(GUMMI_BLOCK, block);
				}

                if(block == ModBlocks.rodSand.get()) {
            		add(BlockTags.MINEABLE_WITH_SHOVEL, block);
                } else {
					if (name.contains("blazing")) {
						add(BLAZING, block);
					}
					if (name.contains("soothing")) {
						add(SOOTHING, block);
					}
					if (name.contains("writhing")) {
						add(WRITHING, block);
					}
					if (name.contains("betwixt")) {
						add(BETWIXT, block);
					}
					if (name.contains("wellspring")) {
						add(WELLSPRING, block);
					}
					if (name.contains("frost")) {
						add(FROST, block);
					}
					if (name.contains("lucid")) {
						add(LUCID, block);
					}
					if (name.contains("lightning")) {
						add(LIGHTNING, block);
					}
					if (name.contains("pulsing")) {
						add(PULSING, block);
					}
					if (name.contains("remembrance")) {
						add(REMEMBRANCE, block);
					}
					if (name.contains("hungry")) {
						add(HUNGRY, block);
					}
					if (name.contains("sinister")) {
						add(SINISTER, block);
					}
					if (name.contains("stormy")) {
						add(STORMY, block);
					}
					if (name.contains("tranquility")) {
						add(TRANQUILITY, block);
					}
					if (name.contains("twilight")) {
						add(TWILIGHT, block);
					}

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
	      return TagKey.create(Registries.BLOCK, ResourceLocation.parse(pName));
	   }
}
