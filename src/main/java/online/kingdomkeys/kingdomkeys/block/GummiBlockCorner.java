package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class GummiBlockCorner extends GummiBlockRotable {

    public GummiBlockCorner(Properties properties, int weight, int armour, DyeColor color, List<Supplier<Block>> blocks) {
        super(properties, weight, armour, color, blocks);
    }


    @Override
    protected Quarter getQuarter(double x, double y) {
        double leftDiag = y - x;        // línea que va de top-left a bottom-right
        double rightDiag = y + x - 1;   // línea que va de top-right a bottom-left

        if (Math.abs(leftDiag) > Math.abs(rightDiag)) {
            return y > 0.5 ? Quarter.TOP : Quarter.BOTTOM;
        } else {
            return x > 0.5 ? Quarter.RIGHT : Quarter.LEFT;
        }
    }
}
