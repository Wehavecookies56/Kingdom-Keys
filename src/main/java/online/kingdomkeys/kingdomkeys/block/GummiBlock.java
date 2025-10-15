package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GummiBlock extends BaseBlock {

    DyeColor color;
    public GummiBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return false;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            System.out.println("Set color to "+dyeColor);
            Block b = Utils.getGummiBlockColorFromDye(dyeColor);
            level.setBlockAndUpdate(pos, b.defaultBlockState());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    /* @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(COLOR, Color.RED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            ItemStack stack = new ItemStack(this);
            stack.set(ModComponents.HANGAR_LEVEL, state.getValue(COLOR));
            popResource(world, pos, stack);
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!worldIn.isClientSide){
            if(worldIn.getBlockEntity(pos) != null) {
                if (stack.get(ModComponents.GUMMI_COLOR) != null) {
                    worldIn.setBlockAndUpdate(pos, state.setValue(COLOR, stack.get(ModComponents.GUMMI_COLOR)));
                }
            }
        }
    }*/
}
