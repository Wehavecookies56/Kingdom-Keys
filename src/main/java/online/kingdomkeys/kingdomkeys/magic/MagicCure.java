package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class MagicCure extends Magic {

	public MagicCure(String registryName, int cost, int order) {
		super(registryName, cost, true, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		((ServerWorld) player.world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), player.getPosX(), player.getPosY()+2.3D, player.getPosZ(), 5, 0D, 0D, 0D, 0D);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		player.heal(playerData.getMaxHP()/3);
		
		//TODO For the Party System
		/* IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		Set<LivingEntity> allies = playerData.getAllies();
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity && allies.contains(e)) {
                	e.heal(7);
                }
            }
        }*/
		caster.swingArm(Hand.MAIN_HAND);
	}

}
