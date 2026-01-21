package online.kingdomkeys.kingdomkeys.integration.epicfight;

import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import yesman.epicfight.api.client.neoevent.PatchedRenderersEvent;
import yesman.epicfight.client.events.engine.RenderEngine;

public class EpicFightRendering {
    private EpicFightRendering() {}
    public static void patchedRenderersEventModify(PatchedRenderersEvent.Modify event) {
        //PatchedLivingEntityRenderer playerRenderer = (PatchedLivingEntityRenderer) event.get(EntityType.PLAYER);
        //playerRenderer.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
        RenderEngine.getInstance().getFirstPersonRenderer().addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
        //playerRenderer.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(Meshes.BIPED, false));
        RenderEngine.getInstance().getFirstPersonRenderer().addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(true));
        //playerRenderer.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>(Meshes.BIPED));
        RenderEngine.getInstance().getFirstPersonRenderer().addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>());
    }

}
