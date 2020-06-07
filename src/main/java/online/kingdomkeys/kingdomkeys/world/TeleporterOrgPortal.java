package online.kingdomkeys.kingdomkeys.world;

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
        if (player.dimension.getId() != dimension)
        	playerMP.changeDimension(DimensionType.getById(dimension));
        playerMP.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
    }

    @Override
    public boolean placeInPortal(Entity entityIn, float rotationYaw) {
		return false;
    }
    
    @Override
    public boolean makePortal(Entity entityIn) {
        return false;
    }

}
