package online.kingdomkeys.kingdomkeys.world.utils;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.PortalForcer;

import java.util.Optional;

public class TeleporterOrgPortal extends PortalForcer {

    public TeleporterOrgPortal(ServerLevel worldIn) {
        super(worldIn);
    }

    public void teleport(Player player, BlockPos pos, ResourceKey<Level> dimension) {
        ServerPlayer playerMP = (ServerPlayer) player;
        playerMP.teleportTo(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
        playerMP.setDeltaMovement(0, 0, 0);
        if (player.level().dimension() != dimension) {
            ServerLevel destinationWorld = playerMP.getServer().getLevel(dimension);
            playerMP.changeDimension(destinationWorld);
        }
        playerMP.teleportTo(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
    }

    @Override
    public Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos pPos, boolean pIsNether, WorldBorder pWorldBorder) {
        return Optional.empty();
    }

    @Override
    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        return Optional.empty();
    }
}
