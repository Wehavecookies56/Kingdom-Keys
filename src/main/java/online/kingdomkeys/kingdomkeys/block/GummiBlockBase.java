package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;

import java.util.List;
import java.util.function.Supplier;

public class GummiBlockBase extends BaseBlock implements ICreativeTab {

    DyeColor color;
    List<Supplier<Block>> blocks;
    int armour, weight;

    public GummiBlockBase(Properties properties, int weight, int armour, DyeColor color, List<Supplier<Block>> blocks) {
        super(properties);
        this.color = color;
        this.blocks = blocks;
        this.weight = weight;
        this.armour = armour;
    }

    public int getArmour() {
        return armour;
    }

    public int getWeight() {
        return weight;
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (blocks != null && stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            Block b = blocks.get(dyeColor.getId()).get();
            level.setBlockAndUpdate(pos, b.defaultBlockState());
            player.swing(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public Tab getTab() {
        return Tab.GUMMI;
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
