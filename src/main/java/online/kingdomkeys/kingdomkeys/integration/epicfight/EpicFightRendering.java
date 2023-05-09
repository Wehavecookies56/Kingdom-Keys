package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class EpicFightRendering {
    private EpicFightRendering() {}
    public static void patchedRenderersEventModify(PatchedRenderersEvent.Modify event) {
        PatchedLivingEntityRenderer playerRenderer = (PatchedLivingEntityRenderer) event.get(EntityType.PLAYER);
        playerRenderer.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
    }

}
