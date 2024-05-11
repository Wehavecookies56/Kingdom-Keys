package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.entity.shotlock.UltimaCannonShotEntity;

import java.util.List;

public class ShotlockUltimaCannon extends Shotlock {
    public ShotlockUltimaCannon(String registryName, int order, int cooldown, int max) {
        super(registryName, order, cooldown, max);
    }

    @Override
    public void doPartialShotlock(Player player, List<Entity> targetList) {
        doFullShotlock(player,targetList);
    }

    @Override
    public void doFullShotlock(Player player, List<Entity> targetList) {
        UltimaCannonShotEntity shot = new UltimaCannonShotEntity(player.level(), player, targetList.get(0), getDamage(player));
        shot.setPos(player.getX(), player.getY()+3, player.getZ());
        player.level().addFreshEntity(shot);
    }
}
