package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class SoAPlatformCoreBlock extends BaseBlock {

    public static final BooleanProperty STRUCTURE = BooleanProperty.create("structure");

    //x
    int width = 16;
    //y
    int height = 24;
    //z
    int depth = 16;

    String structureTop =
            "0000001111000000" +
            "0000111111110000" +
            "0001111111111000" +
            "0011111111111100" +
            "0111111111111110" +
            "0111111111111110" +
            "1111111111111111" +
            "1111111111111111" +
            "1111111121111111" +
            "1111111111111111" +
            "0111111111111110" +
            "0111111111111110" +
            "0011111111111100" +
            "0001111111111000" +
            "0000111111110000" +
            "0000001111000000";

    String structureMiddle =
            "0000001111000000" +
            "0000110000110000" +
            "0001000000001000" +
            "0010000000000100" +
            "0100000000000010" +
            "0100000000000010" +
            "1000000000000001" +
            "1000000000000001" +
            "1000000000000001" +
            "1000000000000001" +
            "0100000000000010" +
            "0100000000000010" +
            "0010000000000100" +
            "0001000000001000" +
            "0000110000110000" +
            "0000001111000000";

    String structureBottom =
            "0000001111000000" +
            "0000111111110000" +
            "0001111111111000" +
            "0011111111111100" +
            "0111111111111110" +
            "0111111111111110" +
            "1111111111111111" +
            "1111111111111111" +
            "1111111111111111" +
            "1111111111111111" +
            "0111111111111110" +
            "0111111111111110" +
            "0011111111111100" +
            "0001111111111000" +
            "0000111111110000" +
            "0000001111000000";

    //Returns true if every block is present
    public boolean checkStructure(BlockPos.Mutable corePos, World world, SoAPlatformTileEntity tileEntity) {
        corePos.setPos(corePos.getX() - (width / 2), corePos.getY(), corePos.getZ() - (depth / 2));
        BlockPos startPos = corePos.toImmutable();

        BlockState structureBlock = ModBlocks.mosaic_stained_glass.get().getDefaultState();
        BlockState coreBlock = getDefaultState();
        for (int y = 0; y < height; ++y) {
            corePos.setPos(corePos.getX(), startPos.getY() - y, corePos.getZ());
            for (int z = 0; z < depth; ++z) {
                corePos.setPos(corePos.getX(), corePos.getY(), startPos.getZ() + z);
                for (int x = 0; x < width; ++x) {
                    corePos.setPos(startPos.getX() + x, corePos.getY(), corePos.getZ());
                    if (corePos.getY() == startPos.getY()) {
                        switch (structureTop.charAt(x + z * width)) {
                            case '0':
                                //air ignore
                                continue;
                            case '1':
                                //mosaic stained glass
                                if (world.getBlockState(corePos).getBlock() != structureBlock.getBlock()) {
                                    tileEntity.clearPositions();
                                    return false;
                                } else {
                                    tileEntity.addPos(corePos.toImmutable());
                                }
                                break;
                            case '2':
                                //core
                                if (world.getBlockState(corePos).getBlock() != coreBlock.getBlock()) {
                                    tileEntity.clearPositions();
                                    return false;
                                }
                                break;
                        }
                    } else if (startPos.getY() - corePos.getY() == (height-1)) {
                        switch (structureBottom.charAt(x + z * width)) {
                            case '0':
                                //air ignore
                                continue;
                            case '1':
                                //mosaic stained glass
                                if (world.getBlockState(corePos).getBlock() != structureBlock.getBlock()) {
                                    tileEntity.clearPositions();
                                    return false;
                                } else {
                                    tileEntity.addPos(corePos.toImmutable());
                                }
                                break;
                        }
                    } else {
                        switch (structureMiddle.charAt(x + z * width)) {
                            case '0':
                                //air ignore
                                continue;
                            case '1':
                                //mosaic stained glass
                                if (world.getBlockState(corePos).getBlock() != structureBlock.getBlock()) {
                                    tileEntity.clearPositions();
                                    return false;
                                } else {
                                    tileEntity.addPos(corePos.toImmutable());
                                }
                                break;
                        }
                    }
                }
            }
        }
        return true;
    }

    public SoAPlatformCoreBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(STRUCTURE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(STRUCTURE);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(STRUCTURE) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SoAPlatformTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
        return false;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            final TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof SoAPlatformTileEntity) {
                if (!((SoAPlatformTileEntity) te).isMultiblockFormed()) {
                    if (player.getHeldItem(handIn).getItem() == ModItems.woodenStick.get()) {
                        tryToFormPlatform((SoAPlatformTileEntity) te, worldIn, pos);
                    }
                }
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public void setBlockStates(World world, List<BlockPos> positions, boolean form) {
        BlockState stateToSet = ModBlocks.mosaic_stained_glass.get().getDefaultState().with(MosaicStainedGlassBlock.STRUCTURE, form);
        positions.forEach(pos -> {
            world.setBlockState(pos, stateToSet);
        });
    }

    public void tryToFormPlatform(SoAPlatformTileEntity tileEntity, World world, BlockPos pos) {
        //Check for shape here
        boolean shapeCorrect = checkStructure(new BlockPos.Mutable(pos), world, tileEntity);
        if (shapeCorrect) {
            setBlockStates(world, tileEntity.structureBlockPosCache,true);
            tileEntity.setMultiblockFormed(true);
            BlockState state = world.getBlockState(pos);
            world.setBlockState(pos, state.with(STRUCTURE, true));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (newState.getBlock() != ModBlocks.station_of_awakening_core.get()) {
            if (te != null) {
                if (te instanceof SoAPlatformTileEntity) {
                    SoAPlatformTileEntity soAPlatformTileEntity = (SoAPlatformTileEntity) te;
                    if (soAPlatformTileEntity.isMultiblockFormed()) {
                        setBlockStates(worldIn, soAPlatformTileEntity.structureBlockPosCache, false);
                        soAPlatformTileEntity.setMultiblockFormed(false);
                        worldIn.setBlockState(pos, state.with(STRUCTURE, false));
                    }
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
