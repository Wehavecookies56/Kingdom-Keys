package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BounceBloxBlock extends BaseBlock {

    public BounceBloxBlock(String name, Properties properties) {
        super(name, properties);
    }

    //Negate fall damage when fallen on if the entity is not sneaking
    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn.isSneaking()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        } else {
            entityIn.fall(fallDistance, 0.0F);
        }
    }

    /**
     * Method to make entities bounce
     * @param entity The entity bouncing
     */
    private void bounce(Entity entity) {
        double bounceFactor = 1;
        entity.setMotion(new Vec3d(entity.getMotion().getX(), bounceFactor, entity.getMotion().getZ()));
        entity.move(MoverType.SELF, entity.getMotion());
        entity.fallDistance = 0;
    }

    //Bounce when landed on if the entity is not sneaking
    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        if (Math.abs(entityIn.getMotion().getY()) < 0.1D && !entityIn.isSneaking()) {
            bounce(entityIn);
        } else {
            super.onLanded(worldIn, entityIn);
        }
    }

    //Bounce when walked on if the entity is not sneaking
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (Math.abs(entityIn.getMotion().getY()) < 0.1D && !entityIn.isSneaking()) {
            bounce(entityIn);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
