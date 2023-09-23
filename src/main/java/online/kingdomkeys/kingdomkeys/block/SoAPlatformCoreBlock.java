package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import javax.annotation.Nullable;
import java.util.List;

public class SoAPlatformCoreBlock extends BaseBlock implements EntityBlock {

    public static final BooleanProperty STRUCTURE = BooleanProperty.create("structure");

    //x
    int width = 17;
    //y
    int height = 24;
    //z
    int depth = 17;

    String structureTop =
            "00000111111100000" +
            "00011111111111000" +
            "00111111111111100" +
            "01111111111111110" +
            "01111111111111110" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111211111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "01111111111111110" +
            "01111111111111110" +
            "00111111111111100" +
            "00011111111111000" +
            "00000111111100000";

    String structureMiddle =
            "00000111111100000" +
            "00011000000011000" +
            "00100000000000100" +
            "01000000000000010" +
            "01000000000000010" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "01000000000000010" +
            "01000000000000010" +
            "00100000000000100" +
            "00011000000011000" +
            "00000111111100000";

    String structureBottom =
            "00000111111100000" +
            "00011111111111000" +
            "00111111111111100" +
            "01111111111111110" +
            "01111111111111110" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "01111111111111110" +
            "01111111111111110" +
            "00111111111111100" +
            "00011111111111000" +
            "00000111111100000";

    //Returns true if every block is present
    public boolean checkStructure(BlockPos.MutableBlockPos corePos, Level world, SoAPlatformTileEntity tileEntity) {
        corePos.set(corePos.getX() - (width / 2), corePos.getY(), corePos.getZ() - (depth / 2));
        BlockPos startPos = corePos.immutable();

        BlockState structureBlock = ModBlocks.mosaic_stained_glass.get().defaultBlockState();
        BlockState coreBlock = defaultBlockState();
        for (int y = 0; y < height; ++y) {
            corePos.set(corePos.getX(), startPos.getY() - y, corePos.getZ());
            for (int z = 0; z < depth; ++z) {
                corePos.set(corePos.getX(), corePos.getY(), startPos.getZ() + z);
                for (int x = 0; x < width; ++x) {
                    corePos.set(startPos.getX() + x, corePos.getY(), corePos.getZ());
                    //System.out.println("Check pos: " + corePos.getX() + ", " + corePos.getY() + ", " + corePos.getZ());
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
                                    tileEntity.addPos(corePos.immutable());
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
                                    tileEntity.addPos(corePos.immutable());
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
                                    tileEntity.addPos(corePos.immutable());
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
        this.registerDefaultState(this.defaultBlockState().setValue(STRUCTURE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STRUCTURE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(STRUCTURE) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModEntities.TYPE_SOA_PLATFORM.get().create(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            final BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof SoAPlatformTileEntity) {
                if (!((SoAPlatformTileEntity) te).isMultiblockFormed()) {
                    if (player.getItemInHand(handIn).getItem() == ModItems.woodenStick.get()) {
                        tryToFormPlatform((SoAPlatformTileEntity) te, worldIn, pos);
                    }
                }
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    public void setBlockStates(Level world, List<BlockPos> positions, boolean form) {
        BlockState stateToSet = ModBlocks.mosaic_stained_glass.get().defaultBlockState().setValue(MosaicStainedGlassBlock.STRUCTURE, form);
        positions.forEach(pos -> {
            world.setBlockAndUpdate(pos, stateToSet);
        });
    }

    public void tryToFormPlatform(SoAPlatformTileEntity tileEntity, Level world, BlockPos pos) {
        //Check for shape here
        boolean shapeCorrect = checkStructure(new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ()), world, tileEntity);
        if (shapeCorrect) {
            setBlockStates(world, tileEntity.structureBlockPosCache,true);
            tileEntity.setMultiblockFormed(true);
            BlockState state = world.getBlockState(pos);
            world.setBlockAndUpdate(pos, state.setValue(STRUCTURE, true));
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (newState.getBlock() != ModBlocks.station_of_awakening_core.get()) {
            if (te != null) {
                if (te instanceof SoAPlatformTileEntity) {
                    SoAPlatformTileEntity soAPlatformTileEntity = (SoAPlatformTileEntity) te;
                    if (soAPlatformTileEntity.isMultiblockFormed()) {
                        setBlockStates(worldIn, soAPlatformTileEntity.structureBlockPosCache, false);
                        soAPlatformTileEntity.setMultiblockFormed(false);
                        worldIn.setBlockAndUpdate(pos, state.setValue(STRUCTURE, false));
                    }
                }
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
