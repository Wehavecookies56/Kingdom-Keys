package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.renderer.FirstPersonRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class EpicFightRendering {

    public static void patchedRenderersEventModify(PatchedRenderersEvent.Modify event) {
        PatchedLivingEntityRenderer playerRenderer = (PatchedLivingEntityRenderer) event.get(EntityType.PLAYER);
        playerRenderer.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
        ClientEngine.instance.renderEngine.getFirstPersonRenderer().addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
    }

}
