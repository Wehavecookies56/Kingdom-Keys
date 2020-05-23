package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public class MagicCure extends Magic {
	String name;

	public MagicCure(String registryName, int cost) {
		super(registryName, cost);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player) {
		 //	PacketDispatcher.sendToAllAround(new SpawnCureParticles(this, 1), player, 64.0D);
		player.heal(7);
		
		//TODO For the Party System
		/* IPlayerCapabilities props = ModCapabilities.get(player);
		Set<LivingEntity> allies = props.getAllies();
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity && allies.contains(e)) {
                	e.heal(7);
                }
            }
        }*/
		player.swingArm(Hand.MAIN_HAND);
	}

}
