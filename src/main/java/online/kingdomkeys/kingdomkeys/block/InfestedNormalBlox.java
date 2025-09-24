package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.mob.BloxBugEntity;
import online.kingdomkeys.kingdomkeys.util.SetBlockStateFlags;

public class InfestedNormalBlox extends BaseBlock {

    public InfestedNormalBlox(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
        BloxBugEntity entity = new BloxBugEntity(pLevel, true);
        entity.setPos(new Vec3(pPos.getX() + 0.5F, pPos.getY(), pPos.getZ() + 0.5F));
        pLevel.addFreshEntity(entity);
    }
}
