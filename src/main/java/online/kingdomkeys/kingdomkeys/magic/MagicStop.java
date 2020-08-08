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
		super(registryName, cost, false, order);
		this.name = registryName;
	}

	@Override
	public void onUse(PlayerEntity player) {
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(6.0D, 3.0D, 6.0D).offset(-3.0D, -1.0D, -3.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);
                if (e instanceof LivingEntity) {
                	IGlobalCapabilities globalData = ModCapabilities.getGlobal((LivingEntity) e);
					globalData.setStoppedTicks(100); //Stop
					globalData.setStopCaster(player.getDisplayName().getFormattedText());
                	if(e instanceof ServerPlayerEntity)
                		PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayerEntity) e);
                }
            }
        }
		player.swingArm(Hand.MAIN_HAND);	
	}

}
