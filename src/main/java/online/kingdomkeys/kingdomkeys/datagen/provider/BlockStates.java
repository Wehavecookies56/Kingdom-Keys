package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, KingdomKeys.MODID,  exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.blastBlox.get());
        simpleBlock(ModBlocks.blazingOre.get());
        simpleBlock(ModBlocks.blazingOreN.get());
        simpleBlock(ModBlocks.bounceBlox.get());
        simpleBlock(ModBlocks.soothingOre.get());
        simpleBlock(ModBlocks.dangerBlox.get());
        simpleBlock(ModBlocks.writhingOre.get());
        simpleBlock(ModBlocks.writhingOreE.get());
        simpleBlock(ModBlocks.writhingOreN.get());
        simpleBlock(ModBlocks.betwixtOre.get());
        simpleBlock(ModBlocks.wellspringOre.get());
        simpleBlock(ModBlocks.wellspringOreN.get());
        simpleBlock(ModBlocks.frostOre.get());
        //TODO ghostblox
        //getVariantBuilder(ModBlocks.ghostBlox).forAllStates(blockState -> ConfiguredModel.builder().modelFile(blockState.get(GhostBloxBlock.VISIBLE) ? ));
        simpleBlock(ModBlocks.hardBlox.get());
        simpleBlock(ModBlocks.lightningOre.get());
        simpleBlock(ModBlocks.lucidOre.get());
        simpleBlock(ModBlocks.metalBlox.get());
        simpleBlock(ModBlocks.normalBlox.get());
        simpleBlock(ModBlocks.pulsingOre.get());
        simpleBlock(ModBlocks.pulsingOreE.get());
        simpleBlock(ModBlocks.prizeBlox.get());
        simpleBlock(ModBlocks.rarePrizeBlox.get());
        simpleBlock(ModBlocks.remembranceOre.get());
        simpleBlock(ModBlocks.hungryOre.get());
        simpleBlock(ModBlocks.stormyOre.get());
        simpleBlock(ModBlocks.tranquilityOre.get());
        simpleBlock(ModBlocks.twilightOre.get());
        simpleBlock(ModBlocks.twilightOreN.get());
        
        simpleBlock(ModBlocks.orgPortal.get());

        //simpleBlock(ModBlocks.mosaic_stained_glass.get());

    }
}
