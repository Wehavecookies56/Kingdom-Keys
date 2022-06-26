package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class EpicFightRendering {

    public static void patchedRenderersEventModify(PatchedRenderersEvent.Modify event) {
        PatchedLivingEntityRenderer renderer = (PatchedLivingEntityRenderer) event.get(EntityType.PLAYER);
        renderer.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
    }

}
