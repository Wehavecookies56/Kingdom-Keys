package online.kingdomkeys.kingdomkeys.world.utils;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by Toby on 01/08/2016.
 */
public class TeleporterOrgPortal extends Teleporter {

    public TeleporterOrgPortal(ServerWorld worldIn) {
        super(worldIn);
    }

    public void teleport(PlayerEntity player, BlockPos pos, RegistryKey<World> dimension) {
        ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
        playerMP.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        playerMP.setMotion(0, 0, 0);
        if (player.world.getDimensionKey() != dimension) {
            ServerWorld destinationWorld = playerMP.getServer().getWorld(dimension);
            playerMP.changeDimension(destinationWorld);
        }
        playerMP.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
    }

    @Override
    public Optional<TeleportationRepositioner.Result> getExistingPortal(BlockPos pos, boolean isNether) {
        return Optional.empty();
    }

    @Override
    public Optional<TeleportationRepositioner.Result> makePortal(BlockPos pos, Direction.Axis axis) {
        return Optional.empty();
    }
}
