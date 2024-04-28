package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedDriveLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>, AM extends  AnimatedMesh> extends PatchedLayer<E, T, M, RenderLayer<E, M>, AM> {

    public PatchedDriveLayerRenderer(AM mesh) { super(mesh); }

    @Override
    public void renderLayer(T t, E e, RenderLayer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, OpenMatrix4f[] openMatrix4fs, float bob, float v, float v1, float v2) {
        if(ModConfigs.showDriveForms && e != null && !ModCapabilities.getPlayer((Player) e).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
            String drive = ModCapabilities.getPlayer((Player) e).getActiveDriveForm();
            DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(drive));
            if (form.getTextureLocation() != null) {
                VertexConsumer vertexConsumer = EpicFightRenderTypes.getArmorFoilBufferTriangles(multiBufferSource, RenderType.armorCutoutNoCull(form.getTextureLocation()), true, false);
                HumanoidMesh model = getModel(e);
                model.drawModelWithPose(poseStack, vertexConsumer, i, 1, 1, 1, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED, openMatrix4fs);
            }

        }
    }

    public HumanoidMesh getModel(E e) {
        boolean defaultSkin = ((AbstractClientPlayer)e).getModelName().equals("default");
        if (defaultSkin) {
            return Meshes.BIPED;
        } else {
            return Meshes.ALEX;
        }
    }
}
