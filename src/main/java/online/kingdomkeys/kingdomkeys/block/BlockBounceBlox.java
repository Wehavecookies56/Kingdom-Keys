package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockBounceBlox extends BlockBase {

    public BlockBounceBlox(String name, Properties properties) {
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
        double d0 = 0.4D + Math.abs(entity.motionY) * 0.2D;
        entity.motionX *= d0;
        entity.motionZ *= d0;
        entity.motionY++;
        entity.fallDistance = 0;
    }

    //Bounce when landed on if the entity is not sneaking
    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        if (Math.abs(entityIn.motionY) < 0.1D && !entityIn.isSneaking()) {
            bounce(entityIn);
        } else {
            super.onLanded(worldIn, entityIn);
        }
    }

    //Bounce when walked on if the entity is not sneaking
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (Math.abs(entityIn.motionY) < 0.1D && !entityIn.isSneaking()) {
            bounce(entityIn);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
