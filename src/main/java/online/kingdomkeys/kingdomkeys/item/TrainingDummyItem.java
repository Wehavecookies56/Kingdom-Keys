package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;

public class TrainingDummyItem extends Item {

    public TrainingDummyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos().above();

        TrainingDummyEntity dummy = new TrainingDummyEntity(ModEntities.TYPE_TRAINING_DUMMY.get(), level);
        dummy.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, context.getPlayer().getYRot(), 0.0F);
        float yaw = context.getPlayer().getYRot();

        dummy.setYRot(yaw);
        dummy.setYHeadRot(yaw);
        dummy.setYBodyRot(yaw);
        level.addFreshEntity(dummy);
        context.getItemInHand().shrink(1);
        return InteractionResult.CONSUME;
    }
}