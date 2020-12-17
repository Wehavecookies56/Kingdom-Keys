package online.kingdomkeys.kingdomkeys.block;

import javafx.beans.property.DoubleProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class MosaicStainedGlassBlock extends StainedGlassBlock {

    public static final BooleanProperty STRUCTURE = BooleanProperty.create("structure");

    public MosaicStainedGlassBlock(Properties properties) {
        super(DyeColor.LIME, properties);
        this.setDefaultState(this.getDefaultState().with(STRUCTURE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(STRUCTURE);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(STRUCTURE) ? BlockRenderType.INVISIBLE : super.getRenderType(state);
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == ModBlocks.mosaic_stained_glass.get()) {
            if (event.getState().get(STRUCTURE)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        List<BlockPos> toRemove = new ArrayList<>();
        event.getAffectedBlocks().forEach(block -> {
            BlockState state = event.getWorld().getBlockState(block);
            if (state.getBlock() == ModBlocks.mosaic_stained_glass.get()) {
                if (state.get(STRUCTURE)) {
                    toRemove.add(block);
                }
            }
        });
        if (!toRemove.isEmpty()) {
            event.getAffectedBlocks().removeAll(toRemove);
        }
    }

}
