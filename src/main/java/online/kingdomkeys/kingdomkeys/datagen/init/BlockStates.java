package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.*;

public class BlockStates extends BlockStateProvider {

	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen.getPackOutput(), KingdomKeys.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (DeferredHolder<Block, ? extends Block> itemRegistryObject : ModBlocks.BLOCKS.getEntries()) {
			final Block block = itemRegistryObject.get();
			String name = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath();

			if (block instanceof GhostBloxBlock) {
				getVariantBuilder(block).forAllStates(state -> {
					boolean active = state.getValue(GhostBloxBlock.VISIBLE);
					String modelName = active ? name + "_visible" : name + "_invisible";
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();

					ModelFile blockModel = models().withExistingParent(modelName, ResourceLocation.withDefaultNamespace("block/cube_all")).texture("all", ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/" + modelName));

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

					ModelFile blockModel = models().withExistingParent(modelName, ResourceLocation.withDefaultNamespace("block/cube_all")).texture("all", ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/" + modelName));

					builder.modelFile(blockModel);

					if (pairState == 0) {
						this.simpleBlockItem(block, blockModel);
					}

					return builder.build();
				});
			} else if (block instanceof INoDataGen) {
				// Skip
			} else if (block instanceof KKOreBlock && name.endsWith("_n")) {
				simpleNetherOre(itemRegistryObject);
			} else {
				simpleBlock(itemRegistryObject);
			}
		}

	}

	public void simpleBlock(Supplier<? extends Block> blockSupplier) {
		simpleBlock(blockSupplier.get());
	}

	@Override
	public void simpleBlock(Block block, ModelFile model) {
		super.simpleBlock(block, model);
		// create item model for block
		this.simpleBlockItem(block, model);
	}

	public ModelFile netherCubeAll(Block block) {
		ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
		return models().cubeAll(name.getPath(), ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + "ether"));
	}

	public void simpleNetherOre(Supplier<? extends Block> blockSupplier) {
		simpleBlock(blockSupplier.get(), netherCubeAll(blockSupplier.get()));
	}
}
