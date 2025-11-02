package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.lighting.LightEngine;
import online.kingdomkeys.kingdomkeys.util.IKKLevelChunkExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin extends ChunkAccess implements IKKLevelChunkExtension {
    @Shadow @Final private Level level;

    @Shadow public abstract void addAndRegisterBlockEntity(BlockEntity blockEntity);

    @Shadow @javax.annotation.Nullable public abstract BlockEntity getBlockEntity(BlockPos pos, LevelChunk.EntityCreationType creationType);

    @Shadow protected abstract <T extends BlockEntity> void updateBlockEntityTicker(T blockEntity);

    private LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, Registry<Biome> biomeRegistry, long inhabitedTime, @Nullable LevelChunkSection[] sections, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, biomeRegistry, inhabitedTime, sections, blendingData);
    }

    @Override
    public BlockState kingdom_Keys$setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
        int i = pos.getY();
        LevelChunkSection levelchunksection = this.getSection(this.getSectionIndex(i));
        boolean flag = levelchunksection.hasOnlyAir();
        if (flag && state.isAir()) {
            return null;
        } else {
            int j = pos.getX() & 15;
            int k = i & 15;
            int l = pos.getZ() & 15;
            BlockState blockstate = levelchunksection.setBlockState(j, k, l, state);
            if (blockstate == state) {
                return null;
            } else {
                Block block = state.getBlock();
                this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(j, i, l, state);
                this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(j, i, l, state);
                this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(j, i, l, state);
                this.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(j, i, l, state);
                boolean flag1 = levelchunksection.hasOnlyAir();
                if (flag != flag1) {
                    this.level.getChunkSource().getLightEngine().updateSectionStatus(pos, flag1);
                }

                if (LightEngine.hasDifferentLightProperties(this, pos, blockstate, state)) {
                    ProfilerFiller profilerfiller = this.level.getProfiler();
                    profilerfiller.push("updateSkyLightSources");
                    this.skyLightSources.update(this, j, i, l);
                    profilerfiller.popPush("queueCheckLight");
                    this.level.getChunkSource().getLightEngine().checkBlock(pos);
                    profilerfiller.pop();
                }

                boolean flag2 = blockstate.hasBlockEntity();
                if (!this.level.isClientSide) {
                    //blockstate.onRemove(this.level, pos, state, isMoving);
                    if (blockstate.hasBlockEntity() && !blockstate.is(state.getBlock())) {
                        level.removeBlockEntity(pos);
                    }
                } else if (!blockstate.is(block) && flag2) {
                    this.removeBlockEntity(pos);
                }

                if (!levelchunksection.getBlockState(j, k, l).is(block)) {
                    return null;
                } else {
                    if (!this.level.isClientSide && !this.level.captureBlockSnapshots) {
                        state.onPlace(this.level, pos, blockstate, isMoving);
                    }

                    if (state.hasBlockEntity()) {
                        BlockEntity blockentity = this.getBlockEntity(pos, LevelChunk.EntityCreationType.CHECK);
                        if (blockentity != null && !blockentity.isValidBlockState(state)) {
                            this.removeBlockEntity(pos);
                            blockentity = null;
                        }

                        if (blockentity == null) {
                            blockentity = ((EntityBlock)block).newBlockEntity(pos, state);
                            if (blockentity != null) {
                                this.addAndRegisterBlockEntity(blockentity);
                            }
                        } else {
                            blockentity.setBlockState(state);
                            this.updateBlockEntityTicker(blockentity);
                        }
                    }

                    this.unsaved = true;
                    return blockstate;
                }
            }
        }
    }
}
