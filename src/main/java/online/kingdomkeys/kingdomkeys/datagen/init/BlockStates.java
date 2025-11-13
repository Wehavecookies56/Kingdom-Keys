package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.*;
import online.kingdomkeys.kingdomkeys.lib.Corner;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Objects;
import java.util.function.Supplier;

public class BlockStates extends BlockStateProvider {

	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen.getPackOutput(), KingdomKeys.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (DeferredHolder<Block, ? extends Block> itemRegistryObject : ModBlocks.BLOCKS.getEntries()) {
			final Block block = itemRegistryObject.get();
			String name = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath();

			String blockName = Utils.getBlockRegistryName(block).getPath();
			String tier;
			if(blockName.contains("shell")){
				tier = "shell_";
			} else if (blockName.contains("dispel")) {
				tier = "dispel_";
			} else {
                tier = "";
            }
            if (block instanceof GummiBlockEdge) {
				getVariantBuilder(block).forAllStates(blockState -> {
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
					Quarter quarter = blockState.getValue(GummiBlockEdge.QUARTER);
					Direction facing = blockState.getValue(GummiBlockEdge.FACING);

					if (blockName.contains("gummi_wedge")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/"+tier+"gummi_wedge"), models().existingFileHelper));
                    } else if (blockName.contains("gummi_pie")) {
                        builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_pie"), models().existingFileHelper));
                    } else if (blockName.contains("gummi_fire")) {
                        builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_fire"), models().existingFileHelper));
					} else if (blockName.contains("gummi_fira")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_fira"), models().existingFileHelper));
					} else if (blockName.contains("gummi_blizzard")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_blizzard"), models().existingFileHelper));
					} else if (blockName.contains("gummi_gravity")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_gravity"), models().existingFileHelper));
					} else if (blockName.contains("gummi_water")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_water"), models().existingFileHelper));
					}
					int x = switch (quarter) {
						case TOP -> 180;
						case BOTTOM -> 0;
						case RIGHT -> 90;
						case LEFT -> 270;
					};
					int y = switch (facing) {
                        case DOWN, UP, SOUTH -> 270;
                        case NORTH -> 90;
						case EAST -> 180;
                        case WEST -> 0;
					};
					builder.rotationX(x);
					builder.rotationY(y);

					return builder.build();
				});
			} else if (block instanceof GummiBlockCorner) {
				getVariantBuilder(block).forAllStates(blockState -> {
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
					Half half = blockState.getValue(GummiBlockCorner.HALF);
					Corner corner = blockState.getValue(GummiBlockCorner.CORNER);
					int x = half == Half.TOP ? 180 : 0;
					int y = 0;
					if (blockName.contains("gummi_pyramid")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/"+tier+"gummi_pyramid"), models().existingFileHelper));
						y = half == Half.TOP ?
								switch (corner) {
									case CORNER1 -> 0;
									case CORNER2 -> 90;
									case CORNER3 -> 180;
									case CORNER4 -> 270;
								} :
								switch (corner) {
									case CORNER1 -> 90;
									case CORNER2 -> 180;
									case CORNER3 -> 270;
									case CORNER4 -> 0;
								};
					} else if (blockName.contains("gummi_round_corner")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_round_corner"), models().existingFileHelper));
						y = half == Half.TOP ?
								switch (corner) {
									case CORNER1 -> 180;
									case CORNER2 -> 270;
									case CORNER3 -> 0;
									case CORNER4 -> 90;
								} :
								switch (corner) {
									case CORNER1 -> 270;
									case CORNER2 -> 0;
									case CORNER3 -> 90;
									case CORNER4 -> 180;
								};
					}
					builder.rotationX(x);
					builder.rotationY(y);

					return builder.build();
				});
			} else if(block instanceof GummiBlockPillar) {
				getVariantBuilder(block).forAllStates(blockState -> {
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
					Direction.Axis facing = blockState.getValue(GummiBlockPillar.AXIS);

					if (blockName.contains("gummi_cylinder")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_cylinder"), models().existingFileHelper));
					}

					int x = switch (facing) {
                        case X -> 90;
                        case Y -> 0;
                        case Z -> 90;
                    };
					int y = switch (facing) {
                        case X -> 90;
                        case Y -> 0;
                        case Z -> 0;
                    };
					builder.rotationX(x);
					builder.rotationY(y);

					return builder.build();
				});

			} else if(block instanceof GummiBlockEnd) {
				getVariantBuilder(block).forAllStates(blockState -> {
					ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
					Direction facing = blockState.getValue(GummiBlockEnd.FACING);

					if (blockName.contains("gummi_cone")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/" + tier + "gummi_cone"), models().existingFileHelper));
					} else if (blockName.contains("gummi_dome")) {
						builder.modelFile(new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/gummi/gummi_dome"), models().existingFileHelper));
					}
					int x = switch (facing) {
                        case DOWN -> 180;
                        case UP -> 0;
                        case NORTH -> 90;
                        case SOUTH -> 90;
                        case WEST -> 90;
                        case EAST -> 90;
                    };
					int y = switch (facing) {
                        case DOWN -> 0;
                        case UP -> 0;
                        case NORTH -> 0;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        case EAST -> 90;
                    };
					builder.rotationX(x);
					builder.rotationY(y);

					return builder.build();
				});

			} else if (block instanceof GhostBloxBlock) {
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
			} else if (block instanceof GummiBlockBase || name.contains("gummi_meteor")) {
				simpleBlock(block, new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/"+name)));
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
