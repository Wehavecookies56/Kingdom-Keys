package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.shotlock.UltimaCannonShotEntity;

import java.util.List;

public class ShotlockUltimaCannon extends Shotlock {
    public ShotlockUltimaCannon(ResourceLocation registryName, int order) {
        super(registryName, order);
    }

    @Override
    public void doPartialShotlock(Player player, List<Entity> targetList) {
        doFullShotlock(player,targetList);
    }

    @Override
    public void doFullShotlock(Player player, List<Entity> targetList) {
        UltimaCannonShotEntity shot = new UltimaCannonShotEntity(player.level(), player, targetList.get(0), getDamage(player));

        Vec3 lookAngle = player.getLookAngle();
        Vec3 flatForward = new Vec3(lookAngle.x, lookAngle.y, lookAngle.z).normalize();
        Vec3 spawnPos = player.position().add(flatForward.scale(2D));

        shot.setPos(spawnPos.x, spawnPos.y + player.getEyeHeight(), spawnPos.z);
        shot.setColor(13353527);
        player.level().addFreshEntity(shot);
    }
}