package online.kingdomkeys.kingdomkeys.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;

public class MagicStop extends Magic {
	String name;

	public MagicStop(String registryName, int cost, int order) {
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
                	IGlobalCapabilities props = ModCapabilities.getGlobal((LivingEntity) e);
                	props.setStoppedTicks(100); //Stop
                	if(e instanceof ServerPlayerEntity)
                		PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(props), (ServerPlayerEntity) e);
                }
            }
        }
		player.swingArm(Hand.MAIN_HAND);	
	}

}
