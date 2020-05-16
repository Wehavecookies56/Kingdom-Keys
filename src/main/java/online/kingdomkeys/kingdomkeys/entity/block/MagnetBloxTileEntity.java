package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import online.kingdomkeys.kingdomkeys.block.MagnetBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.List;

public class MagnetBloxTileEntity extends TileEntity implements ITickableTileEntity {
    public MagnetBloxTileEntity() {
        super(ModEntities.TYPE_MAGNET_BLOX);
    }

    //int ticks = 0;

    //Loop through each block in the direction facing for a given range and returns the nunmber of blocks it goes without hitting one
    //Returns the original range if nothing is hit
    int calculateActualRange(Direction facing, int range) {
        int actualRange = range;
        for (int i = 0; i < range; i++) {
            BlockState current = world.getBlockState(pos.offset(facing, i+1));
            if (current.isSolid() && current.getBlock() != Blocks.AIR) {
                actualRange = i;
                break;
            }
        }
        return actualRange;
    }

    @Override
    public void tick() {
        //ticks++;
        //if (ticks % 20 == 0) {
            //Don't do anything unless it's active
            if (getBlockState().get(MagnetBloxBlock.ACTIVE)) {
                Direction facing = getBlockState().get(MagnetBloxBlock.FACING);
                int range = calculateActualRange(facing, getBlockState().get(MagnetBloxBlock.RANGE));
                //Not very useful if it's 0
                if (range > 0) {
                    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(0, 0, 0, 1, 1, 1).expand(range * facing.getDirectionVec().getX(), range * facing.getDirectionVec().getY(), range * facing.getDirectionVec().getZ()).offset(pos));

                    //No reason to do anymore if there are no entities in range
                    if (!entities.isEmpty()) {
                        boolean attract = getBlockState().get(MagnetBloxBlock.ATTRACT);
                        double strength = 0.8;
                        for (Entity e : entities) {
                            Vec3d ePos = e.getPositionVec();
                            Vec3d blockPos = new Vec3d(getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5);
                            //Attract
                            if (attract) {
                                Vec3d blockDir = blockPos.subtract(ePos);
                                e.setMotion(blockDir.normalize().mul(strength, strength, strength));
                            //Repel
                            } else {
                                Vec3d pushDir = new Vec3d(facing.getDirectionVec());
                                e.setMotion(pushDir.normalize().mul(strength, strength, strength));
                            }
                        }
                    }
                }
            }
        //}
    }
}
