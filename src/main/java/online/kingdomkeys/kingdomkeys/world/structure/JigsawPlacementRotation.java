package online.kingdomkeys.kingdomkeys.world.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SequencedPriorityIterator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//Copy of JigsawPlacement to set the rotation instead of it being random, if there's a better solution that'd be ideal
public class JigsawPlacementRotation {
	static final Logger LOGGER = LogUtils.getLogger();

	public static Optional<Structure.GenerationStub> addPieces(
			Structure.GenerationContext context,
			Holder<StructureTemplatePool> startPool,
			Optional<ResourceLocation> startJigsawName,
			int maxDepth,
			BlockPos pos,
			boolean useExpansionHack,
			Optional<Heightmap.Types> projectStartToHeightmap,
			int maxDistanceFromCenter,
			PoolAliasLookup aliasLookup,
			DimensionPadding dimensionPadding,
			LiquidSettings liquidSettings,
			Rotation rotation
	) {
		RegistryAccess registryaccess = context.registryAccess();
		ChunkGenerator chunkgenerator = context.chunkGenerator();
		StructureTemplateManager structuretemplatemanager = context.structureTemplateManager();
		LevelHeightAccessor levelheightaccessor = context.heightAccessor();
		WorldgenRandom worldgenrandom = context.random();
		Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
		StructureTemplatePool structuretemplatepool = startPool.unwrapKey()
				.flatMap(p_314915_ -> registry.getOptional(aliasLookup.lookup((ResourceKey<StructureTemplatePool>)p_314915_)))
				.orElse(startPool.value());
		StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
		if (structurepoolelement == EmptyPoolElement.INSTANCE) {
			return Optional.empty();
		} else {
			BlockPos blockpos;
			if (startJigsawName.isPresent()) {
				ResourceLocation resourcelocation = startJigsawName.get();
				Optional<BlockPos> optional = getRandomNamedJigsaw(
						structurepoolelement, resourcelocation, pos, rotation, structuretemplatemanager, worldgenrandom
				);
				if (optional.isEmpty()) {
					LOGGER.error(
							"No starting jigsaw {} found in start pool {}",
							resourcelocation,
							startPool.unwrapKey().map(p_248484_ -> p_248484_.location().toString()).orElse("<unregistered>")
					);
					return Optional.empty();
				}

				blockpos = optional.get();
			} else {
				blockpos = pos;
			}

			Vec3i vec3i = blockpos.subtract(pos);
			BlockPos blockpos1 = pos.subtract(vec3i);
			PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(
					structuretemplatemanager,
					structurepoolelement,
					blockpos1,
					structurepoolelement.getGroundLevelDelta(),
					rotation,
					structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation),
					liquidSettings
			);
			BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
			int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
			int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
			int k;
			if (projectStartToHeightmap.isPresent()) {
				k = pos.getY() + chunkgenerator.getFirstFreeHeight(i, j, projectStartToHeightmap.get(), levelheightaccessor, context.randomState());
			} else {
				k = blockpos1.getY();
			}

			int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
			poolelementstructurepiece.move(0, k - l, 0);
			int i1 = k + vec3i.getY();
			return Optional.of(
					new Structure.GenerationStub(
							new BlockPos(i, i1, j),
							p_352014_ -> {
								List<PoolElementStructurePiece> list = Lists.newArrayList();
								list.add(poolelementstructurepiece);
								if (maxDepth > 0) {
									AABB aabb = new AABB(
											(double)(i - maxDistanceFromCenter),
											(double)Math.max(i1 - maxDistanceFromCenter, levelheightaccessor.getMinBuildHeight() + dimensionPadding.bottom()),
											(double)(j - maxDistanceFromCenter),
											(double)(i + maxDistanceFromCenter + 1),
											(double)Math.min(i1 + maxDistanceFromCenter + 1, levelheightaccessor.getMaxBuildHeight() - dimensionPadding.top()),
											(double)(j + maxDistanceFromCenter + 1)
									);
									VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
									addPieces(
											context.randomState(),
											maxDepth,
											useExpansionHack,
											chunkgenerator,
											structuretemplatemanager,
											levelheightaccessor,
											worldgenrandom,
											registry,
											poolelementstructurepiece,
											list,
											voxelshape,
											aliasLookup,
											liquidSettings
									);
									list.forEach(p_352014_::addPiece);
								}
							}
					)
			);
		}
	}

	private static Optional<BlockPos> getRandomNamedJigsaw(
			StructurePoolElement element,
			ResourceLocation startJigsawName,
			BlockPos pos,
			Rotation rotation,
			StructureTemplateManager structureTemplateManager,
			WorldgenRandom random
	) {
		List<StructureTemplate.StructureBlockInfo> list = element.getShuffledJigsawBlocks(structureTemplateManager, pos, rotation, random);
		Optional<BlockPos> optional = Optional.empty();

		for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : list) {
			ResourceLocation resourcelocation = ResourceLocation.tryParse(
					Objects.requireNonNull(structuretemplate$structureblockinfo.nbt(), () -> structuretemplate$structureblockinfo + " nbt was null")
							.getString("name")
			);
			if (startJigsawName.equals(resourcelocation)) {
				optional = Optional.of(structuretemplate$structureblockinfo.pos());
				break;
			}
		}

		return optional;
	}

	private static void addPieces(
			RandomState randomState,
			int maxDepth,
			boolean useExpansionHack,
			ChunkGenerator chunkGenerator,
			StructureTemplateManager structureTemplateManager,
			LevelHeightAccessor level,
			RandomSource random,
			Registry<StructureTemplatePool> pools,
			PoolElementStructurePiece startPiece,
			List<PoolElementStructurePiece> pieces,
			VoxelShape free,
			PoolAliasLookup aliasLookup,
			LiquidSettings liquidSettings
	) {
		Placer jigsawplacement$placer = new Placer(pools, maxDepth, chunkGenerator, structureTemplateManager, pieces, random);
		jigsawplacement$placer.tryPlacingChildren(startPiece, new MutableObject<>(free), 0, useExpansionHack, level, randomState, aliasLookup, liquidSettings);

		while (jigsawplacement$placer.placing.hasNext()) {
			PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.next();
			jigsawplacement$placer.tryPlacingChildren(
					jigsawplacement$piecestate.piece,
					jigsawplacement$piecestate.free,
					jigsawplacement$piecestate.depth,
					useExpansionHack,
					level,
					randomState,
					aliasLookup,
					liquidSettings
			);
		}
	}

	public static boolean generateJigsaw(
			ServerLevel level, Holder<StructureTemplatePool> startPool, ResourceLocation startJigsawName, int maxDepth, BlockPos pos, boolean keepJigsaws, Rotation rotation
	) {
		ChunkGenerator chunkgenerator = level.getChunkSource().getGenerator();
		StructureTemplateManager structuretemplatemanager = level.getStructureManager();
		StructureManager structuremanager = level.structureManager();
		RandomSource randomsource = level.getRandom();
		Structure.GenerationContext structure$generationcontext = new Structure.GenerationContext(
				level.registryAccess(),
				chunkgenerator,
				chunkgenerator.getBiomeSource(),
				level.getChunkSource().randomState(),
				structuretemplatemanager,
				level.getSeed(),
				new ChunkPos(pos),
				level,
				p_227255_ -> true
		);
		Optional<Structure.GenerationStub> optional = addPieces(
				structure$generationcontext,
				startPool,
				Optional.of(startJigsawName),
				maxDepth,
				pos,
				false,
				Optional.empty(),
				128,
				PoolAliasLookup.EMPTY,
				JigsawStructure.DEFAULT_DIMENSION_PADDING,
				JigsawStructure.DEFAULT_LIQUID_SETTINGS,
				rotation
		);
		if (optional.isPresent()) {
			StructurePiecesBuilder structurepiecesbuilder = optional.get().getPiecesBuilder();

			for (StructurePiece structurepiece : structurepiecesbuilder.build().pieces()) {
				if (structurepiece instanceof PoolElementStructurePiece poolelementstructurepiece) {
					poolelementstructurepiece.place(level, structuremanager, chunkgenerator, randomsource, BoundingBox.infinite(), pos, keepJigsaws);
				}
			}

			return true;
		} else {
			return false;
		}
	}

	static record PieceState(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth) {
	}

	static final class Placer {
		private final Registry<StructureTemplatePool> pools;
		private final int maxDepth;
		private final ChunkGenerator chunkGenerator;
		private final StructureTemplateManager structureTemplateManager;
		private final List<? super PoolElementStructurePiece> pieces;
		private final RandomSource random;
		final SequencedPriorityIterator<PieceState> placing = new SequencedPriorityIterator<>();

		Placer(
				Registry<StructureTemplatePool> pools,
				int maxDepth,
				ChunkGenerator chunkGenerator,
				StructureTemplateManager structureTemplateManager,
				List<? super PoolElementStructurePiece> pieces,
				RandomSource random
		) {
			this.pools = pools;
			this.maxDepth = maxDepth;
			this.chunkGenerator = chunkGenerator;
			this.structureTemplateManager = structureTemplateManager;
			this.pieces = pieces;
			this.random = random;
		}

		void tryPlacingChildren(
				PoolElementStructurePiece piece,
				MutableObject<VoxelShape> free,
				int depth,
				boolean useExpansionHack,
				LevelHeightAccessor level,
				RandomState random,
				PoolAliasLookup poolAliasLookup,
				LiquidSettings liquidSettings
		) {
			StructurePoolElement structurepoolelement = piece.getElement();
			BlockPos blockpos = piece.getPosition();
			Rotation rotation = piece.getRotation();
			StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
			boolean flag = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
			MutableObject<VoxelShape> mutableobject = new MutableObject<>();
			BoundingBox boundingbox = piece.getBoundingBox();
			int i = boundingbox.minY();

			label134:
			for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : structurepoolelement.getShuffledJigsawBlocks(
					this.structureTemplateManager, blockpos, rotation, this.random
			)) {
				Direction direction = JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state());
				BlockPos blockpos1 = structuretemplate$structureblockinfo.pos();
				BlockPos blockpos2 = blockpos1.relative(direction);
				int j = blockpos1.getY() - i;
				int k = -1;
				ResourceKey<StructureTemplatePool> resourcekey = readPoolKey(structuretemplate$structureblockinfo, poolAliasLookup);
				Optional<? extends Holder<StructureTemplatePool>> optional = this.pools.getHolder(resourcekey);
				if (optional.isEmpty()) {
					LOGGER.warn("Empty or non-existent pool: {}", resourcekey.location());
				} else {
					Holder<StructureTemplatePool> holder = (Holder<StructureTemplatePool>)optional.get();
					if (holder.value().size() == 0 && !holder.is(Pools.EMPTY)) {
						LOGGER.warn("Empty or non-existent pool: {}", resourcekey.location());
					} else {
						Holder<StructureTemplatePool> holder1 = holder.value().getFallback();
						if (holder1.value().size() == 0 && !holder1.is(Pools.EMPTY)) {
							LOGGER
									.warn(
											"Empty or non-existent fallback pool: {}",
											holder1.unwrapKey().map(p_255599_ -> p_255599_.location().toString()).orElse("<unregistered>")
									);
						} else {
							boolean flag1 = boundingbox.isInside(blockpos2);
							MutableObject<VoxelShape> mutableobject1;
							if (flag1) {
								mutableobject1 = mutableobject;
								if (mutableobject.getValue() == null) {
									mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
								}
							} else {
								mutableobject1 = free;
							}

							List<StructurePoolElement> list = Lists.newArrayList();
							if (depth != this.maxDepth) {
								list.addAll(holder.value().getShuffledTemplates(this.random));
							}

							list.addAll(holder1.value().getShuffledTemplates(this.random));
							int l = structuretemplate$structureblockinfo.nbt() != null
									? structuretemplate$structureblockinfo.nbt().getInt("placement_priority")
									: 0;

							for (StructurePoolElement structurepoolelement1 : list) {
								if (structurepoolelement1 == EmptyPoolElement.INSTANCE) {
									break;
								}

								for (Rotation rotation1 : Rotation.getShuffled(this.random)) {
									List<StructureTemplate.StructureBlockInfo> list1 = structurepoolelement1.getShuffledJigsawBlocks(
											this.structureTemplateManager, BlockPos.ZERO, rotation1, this.random
									);
									BoundingBox boundingbox1 = structurepoolelement1.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, rotation1);
									int i1;
									if (useExpansionHack && boundingbox1.getYSpan() <= 16) {
										i1 = list1.stream()
												.mapToInt(
														p_255598_ -> {
															if (!boundingbox1.isInside(p_255598_.pos().relative(JigsawBlock.getFrontFacing(p_255598_.state())))) {
																return 0;
															} else {
																ResourceKey<StructureTemplatePool> resourcekey1 = readPoolKey(p_255598_, poolAliasLookup);
																Optional<? extends Holder<StructureTemplatePool>> optional1 = this.pools.getHolder(resourcekey1);
																Optional<Holder<StructureTemplatePool>> optional2 = optional1.map(
																		p_255600_ -> p_255600_.value().getFallback()
																);
																int k3 = optional1.<Integer>map(
																				p_255596_ -> p_255596_.value().getMaxSize(this.structureTemplateManager)
																		)
																		.orElse(0);
																int l3 = optional2.<Integer>map(
																				p_255601_ -> p_255601_.value().getMaxSize(this.structureTemplateManager)
																		)
																		.orElse(0);
																return Math.max(k3, l3);
															}
														}
												)
												.max()
												.orElse(0);
									} else {
										i1 = 0;
									}

									for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 : list1) {
										if (JigsawBlock.canAttach(structuretemplate$structureblockinfo, structuretemplate$structureblockinfo1)) {
											BlockPos blockpos3 = structuretemplate$structureblockinfo1.pos();
											BlockPos blockpos4 = blockpos2.subtract(blockpos3);
											BoundingBox boundingbox2 = structurepoolelement1.getBoundingBox(this.structureTemplateManager, blockpos4, rotation1);
											int j1 = boundingbox2.minY();
											StructureTemplatePool.Projection structuretemplatepool$projection1 = structurepoolelement1.getProjection();
											boolean flag2 = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
											int k1 = blockpos3.getY();
											int l1 = j - k1 + JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state()).getStepY();
											int i2;
											if (flag && flag2) {
												i2 = i + l1;
											} else {
												if (k == -1) {
													k = this.chunkGenerator
															.getFirstFreeHeight(
																	blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, random
															);
												}

												i2 = k - k1;
											}

											int j2 = i2 - j1;
											BoundingBox boundingbox3 = boundingbox2.moved(0, j2, 0);
											BlockPos blockpos5 = blockpos4.offset(0, j2, 0);
											if (i1 > 0) {
												int k2 = Math.max(i1 + 1, boundingbox3.maxY() - boundingbox3.minY());
												boundingbox3.encapsulate(new BlockPos(boundingbox3.minX(), boundingbox3.minY() + k2, boundingbox3.minZ()));
											}

											if (!Shapes.joinIsNotEmpty(
													mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3).deflate(0.25)), BooleanOp.ONLY_SECOND
											)) {
												mutableobject1.setValue(
														Shapes.joinUnoptimized(
																mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3)), BooleanOp.ONLY_FIRST
														)
												);
												int j3 = piece.getGroundLevelDelta();
												int l2;
												if (flag2) {
													l2 = j3 - l1;
												} else {
													l2 = structurepoolelement1.getGroundLevelDelta();
												}

												PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(
														this.structureTemplateManager, structurepoolelement1, blockpos5, l2, rotation1, boundingbox3, liquidSettings
												);
												int i3;
												if (flag) {
													i3 = i + j;
												} else if (flag2) {
													i3 = i2 + k1;
												} else {
													if (k == -1) {
														k = this.chunkGenerator
																.getFirstFreeHeight(
																		blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, random
																);
													}

													i3 = k + l1 / 2;
												}

												piece.addJunction(
														new JigsawJunction(blockpos2.getX(), i3 - j + j3, blockpos2.getZ(), l1, structuretemplatepool$projection1)
												);
												poolelementstructurepiece.addJunction(
														new JigsawJunction(blockpos1.getX(), i3 - k1 + l2, blockpos1.getZ(), -l1, structuretemplatepool$projection)
												);
												this.pieces.add(poolelementstructurepiece);
												if (depth + 1 <= this.maxDepth) {
													PieceState jigsawplacement$piecestate = new PieceState(
															poolelementstructurepiece, mutableobject1, depth + 1
													);
													this.placing.add(jigsawplacement$piecestate, l);
												}
												continue label134;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		private static ResourceKey<StructureTemplatePool> readPoolKey(StructureTemplate.StructureBlockInfo blockInfo, PoolAliasLookup aliasLookup) {
			CompoundTag compoundtag = Objects.requireNonNull(blockInfo.nbt(), () -> blockInfo + " nbt was null");
			ResourceKey<StructureTemplatePool> resourcekey = Pools.parseKey(compoundtag.getString("pool"));
			return aliasLookup.lookup(resourcekey);
		}
	}
}