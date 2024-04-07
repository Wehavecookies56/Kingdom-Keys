package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.event.entity.player.PlayerEvent;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.patched.entity.PatchedEntityRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class EpicFightRendering {
    private EpicFightRendering() {}
    public static void patchedRenderersEventModify(PatchedRenderersEvent.Modify event) {
        //PatchedLivingEntityRenderer playerRenderer = (PatchedLivingEntityRenderer) event.get(EntityType.PLAYER);
        //playerRenderer.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
        //playerRenderer.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(Meshes.BIPED, false));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(Meshes.BIPED, true));
        //playerRenderer.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>(Meshes.BIPED));
        ClientEngine.getInstance().renderEngine.getFirstPersonRenderer().addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>(Meshes.BIPED));
    }

}
