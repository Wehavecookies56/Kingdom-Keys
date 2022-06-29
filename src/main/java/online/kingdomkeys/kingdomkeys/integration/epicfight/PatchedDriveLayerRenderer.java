package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import yesman.epicfight.api.client.model.ClientModel;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedDriveLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {

    @Override
    public void renderLayer(T t, E e, RenderLayer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, OpenMatrix4f[] openMatrix4fs, float v, float v1, float v2) {
        if(ModConfigs.showDriveForms && e != null && ModCapabilities.getPlayer((Player) e) != null) {
            if(!ModCapabilities.getPlayer((Player) e).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
                String drive = ModCapabilities.getPlayer((Player) e).getActiveDriveForm();
                DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(drive));
                if (form.getTextureLocation() != null) {
                    VertexConsumer vertexConsumer = EpicFightRenderTypes.getArmorVertexBuilder(multiBufferSource, EpicFightRenderTypes.animatedArmor(form.getTextureLocation(), true), false);
                    ClientModel model = getModel(e);
                    model.drawAnimatedModel(poseStack, vertexConsumer, i, 1, 1, 1, 1, OverlayTexture.NO_OVERLAY, openMatrix4fs);
                }
            }
        }
    }

    public ClientModel getModel(E e) {
        boolean firstPerson = Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        boolean defaultSkin = DefaultPlayerSkin.getSkinModelName(e.getUUID()).equals("default");
        if (firstPerson) {
            if (defaultSkin) {
                return ClientModels.LOGICAL_CLIENT.playerFirstPerson;
            } else {
                return ClientModels.LOGICAL_CLIENT.playerFirstPersonAlex;
            }
        } else {
            if (defaultSkin) {
                return ClientModels.LOGICAL_CLIENT.biped;
            } else {
                return ClientModels.LOGICAL_CLIENT.bipedAlex;
            }
        }
    }
}
