package online.kingdomkeys.kingdomkeys.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MosaicStainedGlassBlock extends StainedGlassBlock {

    public static final BooleanProperty STRUCTURE = BooleanProperty.create("structure");

    public MosaicStainedGlassBlock(Properties properties) {
        super(DyeColor.LIME, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(STRUCTURE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STRUCTURE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(STRUCTURE) ? RenderShape.INVISIBLE : super.getRenderShape(state);
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == ModBlocks.mosaic_stained_glass.get()) {
            if (event.getState().getValue(STRUCTURE)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        List<BlockPos> toRemove = new ArrayList<>();
        event.getAffectedBlocks().forEach(block -> {
            BlockState state = event.getLevel().getBlockState(block);
            if (state.getBlock() == ModBlocks.mosaic_stained_glass.get()) {
                if (state.getValue(STRUCTURE)) {
                    toRemove.add(block);
                }
            }
        });
        if (!toRemove.isEmpty()) {
            event.getAffectedBlocks().removeAll(toRemove);
        }
    }
}
