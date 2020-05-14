package online.kingdomkeys.kingdomkeys.handler;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class ClientEvents {

	/*
	 * @SubscribeEvent public void RenderEntity(RenderPlayerEvent.Pre event) { if
	 * (event.getEntityLiving() instanceof PlayerEntity) { PlayerEntity player =
	 * (PlayerEntity) event.getEntityLiving(); IPlayerCapabilities props =
	 * ModCapabilities.get((PlayerEntity) player); IKKRender render =
	 * Utils.getRender(props);
	 * 
	 * if (render != null) { // event.setCanceled(true);
	 * //render.doRender(event.getEntityLiving(), 0.0625F, event.getMatrixStack(),
	 * event.getBuffers(), event.getLight(), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	 * } } }
	 */

	@SubscribeEvent
	public void RenderEntity(RenderLivingEvent.Pre event) {
		IGlobalCapabilities gProps = ModCapabilities.getGlobal(event.getEntity());
		if (gProps != null) {
			if (gProps.getFlatTicks() > 0) {
				MatrixStack mat = event.getMatrixStack();
				mat.scale(1.5F, 0.01F, 1.5F);
			}
		}
	}

	float yaw = 0, pitch = 0;

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		IGlobalCapabilities gProps = ModCapabilities.getGlobal(event.getEntityLiving());
		if (gProps != null) {
			if (gProps.getStoppedTicks() > 0) {
				event.getEntityLiving().rotationPitch = pitch;
				event.getEntityLiving().rotationYaw = yaw;
				event.setCanceled(true);
			} else {
				yaw = event.getEntityLiving().rotationYaw;
				pitch = event.getEntityLiving().rotationPitch;
			}
		}
	}

}
