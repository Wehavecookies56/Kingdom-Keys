package online.kingdomkeys.kingdomkeys.worldgen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by Toby on 01/08/2016.
 */
public class TeleporterOrgPortal extends Teleporter {

    public TeleporterOrgPortal(ServerWorld worldIn) {
        super(worldIn);
    }

    public void teleport(PlayerEntity player, BlockPos pos, int dimension) {
        ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
        playerMP.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        playerMP.setMotion(0, 0, 0);
        playerMP.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        if (player.dimension.getId() != dimension)
          //  playerMP.server.getPlayerList().transferPlayerToDimension(playerMP, dimension, this);
        	playerMP.changeDimension(DimensionType.getById(dimension));
        playerMP.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
       // playerMP.connection.sendPacket(new SPacketEntityTeleport(playerMP));
       // playerMP.connection.sendPacket(new SPacketEntity(playerMP.getEntityId()));
    }

    @Override
    public boolean placeInPortal(Entity entityIn, float rotationYaw) {
		return false;
    }

   /* @Override
    public BlockPattern.PortalInfo placeInExistingPortal(BlockPos p_222272_1_, Vec3d p_222272_2_, Direction directionIn, double p_222272_4_, double p_222272_6_, boolean p_222272_8_) {
        return blockpattern$patternhelper.getPortalInfo(directionIn, blockpos, p_222272_6_, p_222272_2_, p_222272_4_);
     }).orElse((BlockPattern.PortalInfo)null);
    }*/

    @Override
    public boolean makePortal(Entity entityIn) {
        return false;
    }

}
