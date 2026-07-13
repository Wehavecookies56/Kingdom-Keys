package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;

public class TrainingDummyModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("training_dummy"), "main");
    private final ModelPart bone;

    public TrainingDummyModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(98, 0).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-3.0F, -20.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-4.0F, -36.0F, -5.0F, 8.0F, 16.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(58, 42).addBox(20.0F, -35.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, 38).addBox(-26.0F, -35.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 22).addBox(4.0F, -36.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(34, 30).addBox(-20.0F, -36.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-5.0F, -46.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(92, 17).addBox(-4.5F, -49.0F, -4.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(100, 47).addBox(-3.5F, -55.0F, -3.5F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition back_skirt_r1 = bone.addOrReplaceChild("back_skirt_r1", CubeListBuilder.create().texOffs(80, 54).addBox(-7.0F, -8.0F, 5.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -17.0F, -13.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition right_skirt_r1 = bone.addOrReplaceChild("right_skirt_r1", CubeListBuilder.create().texOffs(0, 47).addBox(2.0F, -8.0F, -5.0F, 2.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition left_skirt_r1 = bone.addOrReplaceChild("left_skirt_r1", CubeListBuilder.create().texOffs(106, 0).addBox(15.0F, -5.0F, -5.0F, 2.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }


    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof TrainingDummyEntity dummy) {
            //Interpolation
            float ticks = dummy.getHitTicks() - (ageInTicks - entity.tickCount);

            if (ticks > 0) {
                float progress = ticks / 15.0F;
                float t = 1.0F - progress;

                float strength = dummy.getHitStrength();

                float dirX = dummy.getHitDirX();
                float dirZ = dummy.getHitDirZ();

                //Amount of bounces
                float bounces = 2.0F + strength * 2.5F;

                // Start strong and smooth out
                float ease = 1.0F - (t * t);

                //Sinus-like oscillation
                float oscillation = (float) Math.sin(t * Math.PI * bounces);

                //Reduce intensity with time
                float decay = (1.0F - t);

                float finalStrength = oscillation * decay * strength;

                //Bounce
                this.bone.xRot = dirZ * finalStrength * 0.35F;
                this.bone.zRot = dirX * finalStrength * 0.35F;

                // Squash, y scale change
                this.bone.yScale = 1.0F - 0.15F * ease * strength;

                this.bone.yRot = oscillation * 0.1F * strength;
            } else {
                this.bone.xRot = 0;
                this.bone.yRot = 0;
                this.bone.zRot = 0;
                this.bone.yScale = 1.0F;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        bone.render(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, colour);
    }
}

