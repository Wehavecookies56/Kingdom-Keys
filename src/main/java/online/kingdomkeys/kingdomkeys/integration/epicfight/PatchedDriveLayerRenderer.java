package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedDriveLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {
    private static final ResourceLocation ANTI_EYES = KingdomKeys.rl("textures/models/armor/anti_eyes.png");

    private final boolean forceFirstPerson;

    public PatchedDriveLayerRenderer(boolean forceFirstPerson) {
        this.forceFirstPerson = forceFirstPerson;
    }

    @Override
    public void renderLayer(T entityPatch, E entity, RenderLayer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, OpenMatrix4f[] openMatrix4fs, float bob, float v, float v1, float v2) {
        if (!ModConfigs.showDriveForms || !(entity instanceof Player player)) {
            return;
        }

        PlayerData data = PlayerData.get(player);
        if (data == null || data.isFormActive(ModDriveForms.NONE)) {
            return;
        }

        ResourceLocation drive = data.getActiveDriveForm();
        DriveForm form = ModDriveForms.registry.get(drive);

        if (form == null) {
            return;
        }

        ResourceLocation texture = form.getTextureLocation(player);
        if (texture == null) {
            return;
        }

        HumanoidMesh model = getModel(entity);

        boolean firstPerson = !ClientUtils.renderingEntityInGui && (this.forceFirstPerson || isCameraEntityInFirstPerson(entity));

        boolean headHidden = model.head.isHidden();
        boolean hatHidden = model.hat.isHidden();

        if (firstPerson) {
            model.head.setHidden(true);
            model.hat.setHidden(true);
        }

        try {
            model.draw(poseStack, multiBufferSource, EpicFightRenderTypes.entityCutoutNoCull(texture), i, 1, 1, 1, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), openMatrix4fs);

            if (form.equals(ModDriveForms.ANTI.get())) {
                model.draw(poseStack, multiBufferSource, EpicFightRenderTypes.eyes(ANTI_EYES), 15728640, 1, 1, 1, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), openMatrix4fs);
            }
        } finally {
            model.head.setHidden(headHidden);
            model.hat.setHidden(hatHidden);
        }
    }

    private static boolean isCameraEntityInFirstPerson(LivingEntity e) {
        Minecraft mc = Minecraft.getInstance();
        return mc.options.getCameraType().isFirstPerson() && mc.getCameraEntity() == e;
    }

    public HumanoidMesh getModel(E e) {
        if (!(e instanceof AbstractClientPlayer client)) {
            return Meshes.BIPED.get();
        }

        return client.getSkin().model() == PlayerSkin.Model.WIDE ? Meshes.BIPED.get() : Meshes.ALEX.get();
    }
}
