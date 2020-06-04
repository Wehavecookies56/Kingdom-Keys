package online.kingdomkeys.kingdomkeys.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;

public class MagicThunder extends Magic {
	String name;

	public MagicThunder(String registryName, int cost, int order) {
		super(registryName, cost, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player) {
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(8.0D, 4.0D, 8.0D).offset(-4.0D, -1.0D, -4.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity) {
            		ThrowableEntity shot = new ThunderEntity(player.world, player, e.getPosX(),e.getPosY(),e.getPosZ());
            		shot.shoot(player, 90, player.rotationYaw, 0, 3F, 0);
            		player.world.addEntity(shot);
                }
            }
        }
		player.swingArm(Hand.MAIN_HAND);
	}

}
