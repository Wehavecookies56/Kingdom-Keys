package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.mob.BloxBugEntity;

public class InfestedNormalBlox extends BaseBlock {
    public InfestedNormalBlox(Properties properties) {
        super(properties);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        BloxBugEntity entity = new BloxBugEntity(level, true);
        entity.setPos(new Vec3(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F));
        level.addFreshEntity(entity);
    }
}
