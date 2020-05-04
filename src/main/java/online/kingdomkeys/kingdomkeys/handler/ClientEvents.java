package online.kingdomkeys.kingdomkeys.handler;

import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class ClientEvents {

	/*@SubscribeEvent
	public void RenderEntity(RenderPlayerEvent.Pre event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			IPlayerCapabilities props = ModCapabilities.get((PlayerEntity) player);
			IKKRender render = Utils.getRender(props);

			if (render != null) {
				// event.setCanceled(true);
				//render.doRender(event.getEntityLiving(), 0.0625F, event.getMatrixStack(), event.getBuffers(), event.getLight(), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		}
	}*/

	float yaw = 0, pitch = 0;

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		IGlobalCapabilities gProps = ModCapabilities.getGlobal(event.getEntityLiving());
		if (gProps != null) {
			if (gProps.getStoppedTicks() > 0) {
				// gProps.subStoppedTicks(1);
				// event.getEntityLiving().getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
				event.getEntityLiving().rotationPitch = pitch;
				event.getEntityLiving().rotationYaw = yaw;
				//event.getEntityLiving().setMotion(0, 0, 0);
				// event.getEntityLiving().setPosition(event.getEntityLiving().prevPosX,
				// event.getEntityLiving().prevPosY, event.getEntityLiving().prevPosZ);
			//	event.getEntityLiving().setPosition(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ());
				//event.getEntityLiving().setMotion(0, 0, 0);
	        //	event.getEntityLiving().velocityChanged = true;
	        	event.setCanceled(true);
			} else {
				yaw = event.getEntityLiving().rotationYaw;
				pitch = event.getEntityLiving().rotationPitch;
				//System.out.println("updating yaw and pitch");
			}
		}
	}

}
