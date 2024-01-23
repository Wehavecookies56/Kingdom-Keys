package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.*;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), KingdomKeys.MODID,  exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<Block> itemRegistryObject : ModBlocks.BLOCKS.getEntries()) {
            final Block block = itemRegistryObject.get();
            String name = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

			if (block instanceof GhostBloxBlock) {
				getVariantBuilder(block).forAllStates(state -> {
					boolean active = state.getValue(GhostBloxBlock.VISIBLE);
					String modelName = active ? name + "_visible" : name + "_invisible";
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();

					ModelFile blockModel = models().withExistingParent(modelName, new ResourceLocation("block/cube_all")).texture("all", new ResourceLocation(KingdomKeys.MODID, "block/" + modelName));

					builder.modelFile(blockModel);

					if (active) {
						this.simpleBlockItem(block, blockModel);
					}

					return builder.build();
				});
			} else if (block instanceof PairBloxBlock) {
				getVariantBuilder(block).forAllStates(state -> {
					int pairState = state.getValue(PairBloxBlock.PAIR);
					String modelName = name + "_" + pairState;
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();

					ModelFile blockModel = models().withExistingParent(modelName, new ResourceLocation("block/cube_all")).texture("all", new ResourceLocation(KingdomKeys.MODID, "block/" + modelName));

					builder.modelFile(blockModel);

					if (pairState == 0) {
						this.simpleBlockItem(block, blockModel);
					}

					return builder.build();
				});
			} else if (block instanceof MagnetBloxBlock) {
				// skip
				// This one was manually generated
			} else if (block instanceof OrgPortalBlock) {
				// skip
			} else if (block instanceof SavePointBlock) {
				// skip
			} else if (block instanceof SoRCore) {
				// skip
			} else if (block instanceof SoAPlatformCoreBlock) {
				// skip
			} else if (block instanceof SoADoorBlock) {
				// skip
			} else if (block instanceof PedestalBlock) {
				// skip
			} else if (block instanceof MoogleProjectorBlock) {
				// skip
			} else if (block instanceof GummiEditorBlock) {
				// skip
			} else if (block instanceof MagicalChestBlock) {
				// skip
			} else if (block instanceof MosaicStainedGlassBlock) {
				// skip
			} else if (block instanceof DataPortalBlock) {

			} else if (block instanceof StructureWallBlock) {

			} else if (block instanceof CastleOblivionStairBlock) {

			} else if (block instanceof CastleOblivionSlabBlock) {

			} else if (block instanceof CastleOblivionPillarBlock) {

			} else if (block instanceof CardDoorBlock) {

			} else {
				simpleBlock(itemRegistryObject);
			}
		}

    }

    public void simpleBlock(Supplier<? extends Block> blockSupplier)
    {
        simpleBlock(blockSupplier.get());
    }

    @Override
    public void simpleBlock(Block block, ModelFile model)
    {
        super.simpleBlock(block, model);
        //create item model for block
        this.simpleBlockItem(block, model);
    }
}
