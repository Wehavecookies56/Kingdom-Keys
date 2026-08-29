package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.render.OrganizationArmorOverlayRenderer;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedOrganizationArmorOverlayRenderer<
        E extends LivingEntity,
        T extends LivingEntityPatch<E>,
        M extends HumanoidModel<E>>
        extends PatchedLayer<
        E,
        T,
        M,
        OrganizationArmorOverlayRenderer<E, M>> {

    private static final ResourceLocation LAYER_1_TEXTURE =
            KingdomKeys.rl(
                    "textures/models/armor/organization_layer_1_overlay.png"
            );

    private static final ResourceLocation LAYER_2_TEXTURE =
            KingdomKeys.rl(
                    "textures/models/armor/organization_layer_2_overlay.png"
            );

    private static final RenderType LAYER_1_RENDER_TYPE =
            EpicFightRenderTypes.getTriangulated(
                    EpicFightRenderTypes.armorCutoutNoCull(
                            LAYER_1_TEXTURE
                    )
            );

    private static final RenderType LAYER_2_RENDER_TYPE =
            EpicFightRenderTypes.getTriangulated(
                    EpicFightRenderTypes.armorCutoutNoCull(
                            LAYER_2_TEXTURE
                    )
            );

    /*
     * false = normal Epic Fight third person renderer
     * true  = Epic Fight animated first person renderer
     */
    private final boolean firstPerson;

    /*
     * Epic Fight cannot directly animate Minecraft ModelParts.
     * These are the Epic Fight skinned-mesh versions of our
     * inflated armor models.
     */
    private SkinnedMesh chestMesh;
    private SkinnedMesh armMesh;
    private SkinnedMesh waistMesh;
    private SkinnedMesh legMesh;

    public PatchedOrganizationArmorOverlayRenderer(
            boolean firstPerson
    ) {
        this.firstPerson = firstPerson;
    }

    @Override
    protected void renderLayer(
            T entityPatch,
            E entity,
            OrganizationArmorOverlayRenderer<E, M> vanillaLayer,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            OpenMatrix4f[] poses,
            float bob,
            float yRot,
            float xRot,
            float partialTicks
    ) {

        ItemStack chest =
                entity.getItemBySlot(EquipmentSlot.CHEST);

        ItemStack leggings =
                entity.getItemBySlot(EquipmentSlot.LEGS);

        ItemStack boots =
                entity.getItemBySlot(EquipmentSlot.FEET);

        boolean hasChest =
                chest.getItem()
                        == ModItems.organizationRobe_Chestplate.get();

        boolean hasLeggings =
                leggings.getItem()
                        == ModItems.organizationRobe_Leggings.get();

        boolean hasBoots =
                boots.getItem()
                        == ModItems.organizationRobe_Boots.get();

        /*
         * Don't bake anything unless the entity is actually
         * wearing part of the Organization coat.
         */
        if (!hasChest && !hasLeggings && !hasBoots) {
            return;
        }

        ensureMeshes();

        /*
         * CHEST
         *
         * Third person:
         *     body + both arms
         *
         * First person:
         *     arms only
         */
        if (hasChest) {

            if (firstPerson) {
                draw(
                        armMesh,
                        LAYER_1_RENDER_TYPE,
                        poseStack,
                        buffer,
                        packedLight,
                        poses
                );
            } else {
                draw(
                        chestMesh,
                        LAYER_1_RENDER_TYPE,
                        poseStack,
                        buffer,
                        packedLight,
                        poses
                );
            }
        }

        /*
         * LEGGINGS
         *
         * This intentionally matches what we did in the
         * vanilla OrganizationArmorOverlayRenderer:
         *
         * waist = smaller leggings shell
         * legs  = larger outer shell
         */
        if (hasLeggings) {

            if (!firstPerson) {
                draw(
                        waistMesh,
                        LAYER_2_RENDER_TYPE,
                        poseStack,
                        buffer,
                        packedLight,
                        poses
                );
            }

            draw(
                    legMesh,
                    LAYER_2_RENDER_TYPE,
                    poseStack,
                    buffer,
                    packedLight,
                    poses
            );
        }

        /*
         * BOOTS
         *
         * Uses the outer leg geometry with layer_1 texture.
         */
        if (hasBoots) {
            draw(
                    legMesh,
                    LAYER_1_RENDER_TYPE,
                    poseStack,
                    buffer,
                    packedLight,
                    poses
            );
        }
    }

    /**
     * Build Epic Fight-compatible skinned meshes from the exact
     * same model layers used by OrganizationArmorOverlayRenderer.
     */
    private void ensureMeshes() {

        if (chestMesh == null) {
            chestMesh = bakeMesh(
                    OrganizationArmorOverlayRenderer.OUTER_LAYER,
                    true,
                    true,
                    false
            );
        }

        if (armMesh == null) {
            armMesh = bakeMesh(
                    OrganizationArmorOverlayRenderer.OUTER_LAYER,
                    false,
                    true,
                    false
            );
        }

        if (waistMesh == null) {
            waistMesh = bakeMesh(
                    OrganizationArmorOverlayRenderer.LEGGINGS_LAYER,
                    true,
                    false,
                    false
            );
        }

        if (legMesh == null) {
            legMesh = bakeMesh(
                    OrganizationArmorOverlayRenderer.OUTER_LAYER,
                    false,
                    false,
                    true
            );
        }
    }

    /**
     * Converts one of our Minecraft HumanoidArmorModels into
     * an Epic Fight SkinnedMesh.
     *
     * body = render torso
     * arms = render both arms
     * legs = render both legs
     */
    private static SkinnedMesh bakeMesh(
            ModelLayerLocation layer,
            boolean body,
            boolean arms,
            boolean legs
    ) {

        HumanoidArmorModel<LivingEntity> model =
                new HumanoidArmorModel<>(
                        Minecraft.getInstance()
                                .getEntityModels()
                                .bakeLayer(layer)
                );

        /*
         * Start completely invisible, then enable only
         * the geometry this mesh should contain.
         */
        model.setAllVisible(false);

        model.body.visible = body;

        model.rightArm.visible = arms;
        model.leftArm.visible = arms;

        model.rightLeg.visible = legs;
        model.leftLeg.visible = legs;

        /*
         * This is the important bit.
         *
         * Epic Fight converts our Minecraft cubes into
         * vertices weighted to its BIPED armature.
         */
        return HumanoidModelBaker.VANILLA_TRANSFORMER
                .transformArmorModel(model);
    }

    private static void draw(
            SkinnedMesh mesh,
            RenderType renderType,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            OpenMatrix4f[] poses
    ) {

        if (mesh == null) {
            return;
        }

        VertexConsumer vertexConsumer =
                buffer.getBuffer(renderType);

        mesh.drawPosed(
                poseStack,
                vertexConsumer,
                Mesh.DrawingFunction.NEW_ENTITY,
                packedLight,
                1.0F,
                1.0F,
                1.0F,
                1.0F,
                OverlayTexture.NO_OVERLAY,
                Armatures.BIPED.get(),
                poses
        );
    }
}