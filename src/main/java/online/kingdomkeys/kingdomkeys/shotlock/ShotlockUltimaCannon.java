package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.shotlock.PrismRainCoreEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

import java.util.List;

public class ShotlockUltimaCannon extends Shotlock{
    public ShotlockUltimaCannon(String registryName, int order, int cooldown, int max) {
        super(registryName, order, cooldown, max);
    }

    @Override
    public void onUse(Player player, List<Entity> targetList) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        playerData.setLimitCooldownTicks(cooldown);
        PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
        PrismRainCoreEntity core = new PrismRainCoreEntity(player.level(), player, targetList, getDamage(player));
        core.setPos(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(core);
    }
}
