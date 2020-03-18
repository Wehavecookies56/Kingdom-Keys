package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GhostBloxBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, KingdomKeys.MODID,  exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        

        simpleBlock(ModBlocks.blastBlox);
        simpleBlock(ModBlocks.blazingOre);
        simpleBlock(ModBlocks.blazingOreN);
        simpleBlock(ModBlocks.bounceBlox);
        simpleBlock(ModBlocks.brightOre);
        simpleBlock(ModBlocks.dangerBlox);
        simpleBlock(ModBlocks.darkOre);
        simpleBlock(ModBlocks.darkOreE);
        simpleBlock(ModBlocks.darkOreN);
        simpleBlock(ModBlocks.denseOre);
        simpleBlock(ModBlocks.energyOre);
        simpleBlock(ModBlocks.energyOreN);
        simpleBlock(ModBlocks.frostOre);
        //TODO ghostblox
        //getVariantBuilder(ModBlocks.ghostBlox).forAllStates(blockState -> ConfiguredModel.builder().modelFile(blockState.get(GhostBloxBlock.VISIBLE) ? ));
        simpleBlock(ModBlocks.hardBlox);
        simpleBlock(ModBlocks.lightningOre);
        simpleBlock(ModBlocks.lucidOre);
        simpleBlock(ModBlocks.metalBlox);
        simpleBlock(ModBlocks.normalBlox);
        simpleBlock(ModBlocks.powerOre);
        simpleBlock(ModBlocks.powerOreE);
        simpleBlock(ModBlocks.prizeBlox);
        simpleBlock(ModBlocks.rarePrizeBlox);
        simpleBlock(ModBlocks.remembranceOre);
        simpleBlock(ModBlocks.serenityOre);
        simpleBlock(ModBlocks.stormyOre);
        simpleBlock(ModBlocks.tranquilOre);
        simpleBlock(ModBlocks.twilightOre);
        simpleBlock(ModBlocks.twilightOreN);

    }
}
