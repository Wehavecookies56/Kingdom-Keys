package online.kingdomkeys.kingdomkeys.handler;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.render.IKKRender;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class ClientEvents {

	@SubscribeEvent
	public void RenderEntity(RenderPlayerEvent.Pre event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			IPlayerCapabilities props = ModCapabilities.get((PlayerEntity) player);
			IKKRender render = Utils.getRender(props);
			
			if (render != null) {
				//event.setCanceled(true);
				render.doRender(event.getEntityLiving(), 0.0625F, event.getMatrixStack(), event.getBuffers(), event.getLight(), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		}
	}
}
