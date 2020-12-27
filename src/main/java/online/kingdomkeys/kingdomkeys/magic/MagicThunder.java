package online.kingdomkeys.kingdomkeys.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.server.ServerWorld;

public class MagicThunder extends Magic {

	public MagicThunder(String registryName, int cost, int order) {
		super(registryName, cost, false, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player) {
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(6.0D, 4.0D, 6.0D).offset(-3.0D, -1.0D, -3.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity) {
                	LightningBoltEntity shot = new LightningBoltEntity(player.world, e.getPosX(), e.getPosY(), e.getPosZ(), false);
            		((ServerWorld)player.world).addLightningBolt(shot);
                }
            }
        } else {
        	int x = (int) player.getPosX();
        	int z = (int) player.getPosZ();
        	
        	for(int i = 0; i<4; i++) {
        	int posX = x + player.world.rand.nextInt(6) - 3;
        	int posZ = z + player.world.rand.nextInt(6) - 3;
        	
        	LightningBoltEntity shot = new LightningBoltEntity(player.world, posX, player.world.getHeight(Type.WORLD_SURFACE, posX, posZ), posZ, false);
        	
    		((ServerWorld)player.world).addLightningBolt(shot);
        	}

        }
		player.swingArm(Hand.MAIN_HAND);
	}

}
