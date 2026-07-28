package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

public class MoogleModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("moogle"), "main");
    private final ModelPart OrgCoat;
    private final ModelPart Head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart Body;
    private final ModelPart leftArm;
    private final ModelPart leftRight;
    private final ModelPart PomPom;

    /**
     * Opaque ARGB tint for the pompom, set by the renderer before drawing, or
     * {@link MoogleEntity#NO_POMPOM_DYE} to draw it with the plain texture.
     */
    public int pompomColor = MoogleEntity.NO_POMPOM_DYE;

    public MoogleModel(ModelPart root) {
        this.OrgCoat = root.getChild("OrgCoat");
        this.Head = root.getChild("Head");
        this.PomPom = this.Head.getChild("PomPom");
        this.leftLeg = root.getChild("leftLeg");
        this.rightLeg = root.getChild("rightLeg");
        this.Body = root.getChild("Body");
        this.leftArm = root.getChild("leftArm");
        this.leftRight = root.getChild("leftRight");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition OrgCoat = partdefinition.addOrReplaceChild("OrgCoat", CubeListBuilder.create().texOffs(0, 27).addBox(-2.3F, -2.5F, -1.5F, 4.6F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -12.0F, -2.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(26, 9).addBox(-1.0F, -10.2F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(1.0F, -13.0F, -1.0F, 1.5F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).mirror().addBox(-2.5F, -13.0F, -1.0F, 1.5F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition PomPom = Head.addOrReplaceChild("PomPom", CubeListBuilder.create().texOffs(26, 1).addBox(0.0F, -15.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.0F, -16.5F, 1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(14, 15).addBox(-1.95F, -2.6F, -1.0F, 1.75F, 2.6F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(14, 15).addBox(0.2F, -2.6F, -1.0F, 1.75F, 2.6F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 9).addBox(-2.35F, -7.0F, -1.5F, 4.7F, 4.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition wingL_r1 = Body.addOrReplaceChild("wingL_r1", CubeListBuilder.create().texOffs(26, 26).addBox(1.0F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(26, 26).mirror().addBox(-1.0F, -2.0F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leftArm_r1 = leftArm.addOrReplaceChild("leftArm_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-0.75F, -1.0F, -0.75F, 1.5F, 3.05F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.25F, -5.7F, 0.0F, 0.0F, 0.0F, -0.5672F));

        PartDefinition leftRight = partdefinition.addOrReplaceChild("leftRight", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightArm_r1 = leftRight.addOrReplaceChild("rightArm_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-0.75F, -1.0F, -0.75F, 1.5F, 3.05F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, -5.7F, 0.0F, 0.0F, 0.0F, 0.5672F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
    	poseStack.translate(0,-0.5,0);
        OrgCoat.render(poseStack, buffer, packedLight, packedOverlay, colour);

        boolean dyed = pompomColor != MoogleEntity.NO_POMPOM_DYE;
        // The pompom is a child of the head, so it normally rides along with it. To give it its own
        // colour it is skipped during the head pass and drawn again under the head's transform.
        PomPom.skipDraw = dyed;
        Head.render(poseStack, buffer, packedLight, packedOverlay, colour);
        if (dyed) {
            //Keep whatever alpha the caller asked for (the projector moogle draws semi-transparent).
            int tint = (colour & 0xFF000000) | (pompomColor & 0x00FFFFFF);
            PomPom.skipDraw = false;
            poseStack.pushPose();
            Head.translateAndRotate(poseStack);
            PomPom.render(poseStack, buffer, packedLight, packedOverlay, tint);
            poseStack.popPose();
        }

        leftLeg.render(poseStack, buffer, packedLight, packedOverlay, colour);
        rightLeg.render(poseStack, buffer, packedLight, packedOverlay, colour);
        Body.render(poseStack, buffer, packedLight, packedOverlay, colour);
        leftArm.render(poseStack, buffer, packedLight, packedOverlay, colour);
        leftRight.render(poseStack, buffer, packedLight, packedOverlay, colour);
    }
}