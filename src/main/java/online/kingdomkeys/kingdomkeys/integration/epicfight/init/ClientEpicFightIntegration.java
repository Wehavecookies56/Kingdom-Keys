package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedArmourLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedShoulderLayerRenderer;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;

public class ClientEpicFightIntegration {

    public static void init() {
        EpicFightClientEventHooks.Render.PREPARE_MODEL_TO_RENDER.registerEvent(PatchedArmourLayerRenderer::clearModels);
        EpicFightClientEventHooks.Render.PREPARE_MODEL_TO_RENDER.registerEvent(PatchedShoulderLayerRenderer::clearModels);
    }

}
