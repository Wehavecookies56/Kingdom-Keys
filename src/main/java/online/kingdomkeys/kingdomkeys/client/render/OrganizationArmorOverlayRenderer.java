package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@OnlyIn(Dist.CLIENT)
public class OrganizationArmorOverlayRenderer<
        T extends LivingEntity,
        M extends HumanoidModel<T>>
        extends RenderLayer<T, M> {

    /*
     * Vanilla outer armor is roughly 1.0.
     * Vanilla leggings are roughly 0.5.
     *
     * These are slightly larger so the overlay physically
     * sits outside the normal armor.
     */
    private static final float OUTER_SIZE = 1.0F;
    private static final float LEGGINGS_SIZE = 0.85F;

    public static final ModelLayerLocation OUTER_LAYER =
            new ModelLayerLocation(
                    KingdomKeys.rl("organization_overlay"),
                    "outer"
            );

    public static final ModelLayerLocation LEGGINGS_LAYER =
            new ModelLayerLocation(
                    KingdomKeys.rl("organization_overlay"),
                    "leggings"
            );

    private static final ResourceLocation OUTER_TEXTURE =
            KingdomKeys.rl(
                    "textures/models/armor/organization_layer_1_overlay.png"
            );

    private static final ResourceLocation LEGGINGS_TEXTURE =
            KingdomKeys.rl(
                    "textures/models/armor/organization_layer_2_overlay.png"
            );

    private final HumanoidArmorModel<T> outerModel;
    private final HumanoidArmorModel<T> leggingsModel;

    public OrganizationArmorOverlayRenderer(
            RenderLayerParent<T, M> parent,
            EntityModelSet modelSet
    ) {
        super(parent);

        this.outerModel =
                new HumanoidArmorModel<>(
                        modelSet.bakeLayer(OUTER_LAYER)
                );

        this.leggingsModel =
                new HumanoidArmorModel<>(
                        modelSet.bakeLayer(LEGGINGS_LAYER)
                );
    }

    public static LayerDefinition createOuterLayer() {
        return LayerDefinition.create(
                HumanoidArmorModel.createBodyLayer(
                        new CubeDeformation(OUTER_SIZE)
                ),
                64,
                32
        );
    }

    public static LayerDefinition createLeggingsLayer() {
        return LayerDefinition.create(
                HumanoidArmorModel.createBodyLayer(
                        new CubeDeformation(LEGGINGS_SIZE)
                ),
                64,
                32
        );
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        renderChest(
                poseStack,
                buffer,
                packedLight,
                entity
        );

        renderLeggings(
                poseStack,
                buffer,
                packedLight,
                entity
        );

        renderBoots(
                poseStack,
                buffer,
                packedLight,
                entity
        );

        // Intentionally no helmet.
        // You wanted the head removed.
    }

    private void renderChest(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity
    ) {

        ItemStack stack =
                entity.getItemBySlot(EquipmentSlot.CHEST);

        if (stack.getItem() !=
                ModItems.organizationRobe_Chestplate.get()) {
            return;
        }

        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderType.armorCutoutNoCull(
                                OUTER_TEXTURE
                        )
                );

        /*
         * Do this exactly the way KK's
         * KeybladeArmorRenderer does it.
         */
        outerModel.body.copyFrom(
                getParentModel().body
        );

        outerModel.rightArm.copyFrom(
                getParentModel().rightArm
        );

        outerModel.leftArm.copyFrom(
                getParentModel().leftArm
        );

        outerModel.body.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        outerModel.rightArm.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        outerModel.leftArm.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );
    }

    private void renderLeggings(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity
    ) {

        ItemStack stack =
                entity.getItemBySlot(EquipmentSlot.LEGS);

        if (stack.getItem() !=
                ModItems.organizationRobe_Leggings.get()) {
            return;
        }

        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderType.armorCutoutNoCull(
                                LEGGINGS_TEXTURE
                        )
                );

        /*
         * Waist/body stays close to the normal leggings.
         */
        leggingsModel.body.copyFrom(
                getParentModel().body
        );

        /*
         * Actual legs use the larger outer model so
         * the overlay isn't buried underneath the boots.
         */
        outerModel.rightLeg.copyFrom(
                getParentModel().rightLeg
        );

        outerModel.leftLeg.copyFrom(
                getParentModel().leftLeg
        );

        // Waist portion of layer_2
        leggingsModel.body.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        // Right leg outer layer
        outerModel.rightLeg.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        // Left leg outer layer
        outerModel.leftLeg.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );
    }

    private void renderBoots(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T entity
    ) {

        ItemStack stack =
                entity.getItemBySlot(EquipmentSlot.FEET);

        if (stack.getItem() !=
                ModItems.organizationRobe_Boots.get()) {
            return;
        }

        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderType.armorCutoutNoCull(
                                OUTER_TEXTURE
                        )
                );

        outerModel.rightLeg.copyFrom(
                getParentModel().rightLeg
        );

        outerModel.leftLeg.copyFrom(
                getParentModel().leftLeg
        );

        outerModel.rightLeg.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );

        outerModel.leftLeg.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );
    }

    @Override
    protected ResourceLocation getTextureLocation(T entity) {
        return OUTER_TEXTURE;
    }
}