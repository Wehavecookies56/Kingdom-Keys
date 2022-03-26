package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

public class BlockTagsGen extends BlockTagsProvider
{
    public BlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, KingdomKeys.MODID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        add(BlockTags.DRAGON_IMMUNE, ModBlocks.metalBlox.get());
        add(BlockTags.WITHER_IMMUNE, ModBlocks.metalBlox.get());
        //add(BlockTags.BEACON_BASE_BLOCKS, ModBlocks.metalBlox.get());
    }

    public void add(TagKey<Block> branch, Block block)
    {
        this.tag(branch).add(block);
    }

    public void add(TagKey<Block> branch, Block... block)
    {
        this.tag(branch).add(block);
    }
}
