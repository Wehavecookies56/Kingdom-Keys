package online.kingdomkeys.kingdomkeys.integration.epicfight;

import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.ClientEngine;

public class EpicFightRendering {
    private EpicFightRendering() {}
    public static void patchedRenderersEventModify(PatchedRenderersEvent.Modify event) {
        //PatchedLivingEntityRenderer playerRenderer = (PatchedLivingEntityRenderer) event.get(EntityType.PLAYER);
        //playerRenderer.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
        //playerRenderer.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(Meshes.BIPED, false));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(true));
        //playerRenderer.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>(Meshes.BIPED));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>());
    }

}
