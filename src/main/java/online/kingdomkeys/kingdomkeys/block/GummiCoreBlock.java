package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;

import javax.annotation.Nullable;

public class GummiCoreBlock extends BaseBlock implements EntityBlock, ICreativeTab {
    public GummiCoreBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModEntities.TYPE_GUMMI_CORE_TE.get().create(pPos, pState);
    }

    @Override
    public Tab getTab() {
        return Tab.GUMMI;
    }
}
