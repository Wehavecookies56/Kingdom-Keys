package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseElementalMusicalHeartlessEntity;

/**
 * Dusk - WYND
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */
public class ElementalMusicalHeartlessModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "elementalmusicalheartless"), "main");

    private final ModelPart Body;
    private final ModelPart RightLeg;
    private final ModelPart RightLegDetail1;
    private final ModelPart RightLegDetail2;
    private final ModelPart RightLegDetail3;
    private final ModelPart LeftLeg;
    private final ModelPart LeftLegDetail1;
    private final ModelPart LeftLegDetail2;
    private final ModelPart LeftLegDetail3;
    private final ModelPart Body1;
    private final ModelPart Body2;
    private final ModelPart Collar1;
    private final ModelPart Collar2;
    private final ModelPart Collar3;
    private final ModelPart Collar4;
    private final ModelPart Head;
    private final ModelPart Hat;
    private final ModelPart HatDetail1;
    private final ModelPart HatDetail2;
    private final ModelPart HatDetail3;
    private final ModelPart HatDetail4;
    private final ModelPart Hat1;
    private final ModelPart HatTop1;
    private final ModelPart HatTop2;
    private final ModelPart HatTop3;
    private final ModelPart HatTop4;
    private final ModelPart HatTop5;

    private boolean canAnimate = true;
    private double frame;

    public ElementalMusicalHeartlessModel(ModelPart root) {
        this.Body = root.getChild("Body");
        this.RightLeg = Body.getChild("RightLeg");
        this.RightLegDetail1 = RightLeg.getChild("RightLegDetail1");
        this.RightLegDetail2 = RightLeg.getChild("RightLegDetail2");
        this.RightLegDetail3 = RightLeg.getChild("RightLegDetail3");
        this.LeftLeg = Body.getChild("LeftLeg");
        this.LeftLegDetail1 = LeftLeg.getChild("LeftLegDetail1");
        this.LeftLegDetail2 = LeftLeg.getChild("LeftLegDetail2");
        this.LeftLegDetail3 = LeftLeg.getChild("LeftLegDetail3");
        this.Body1 = Body.getChild("Body1");
        this.Body2 = Body1.getChild("Body2");
        this.Collar1 = Body2.getChild("Collar1");
        this.Collar2 = Body2.getChild("Collar2");
        this.Collar3 = Body2.getChild("Collar3");
        this.Collar4 = Body2.getChild("Collar4");
        this.Head = Body2.getChild("Head");
        this.Hat = Head.getChild("Hat");
        this.HatDetail1 = Hat.getChild("HatDetail1");
        this.HatDetail2 = Hat.getChild("HatDetail2");
        this.HatDetail3 = Hat.getChild("HatDetail3");
        this.HatDetail4 = Hat.getChild("HatDetail4");
        this.Hat1 = Hat.getChild("Hat1");
        this.HatTop1 = Hat1.getChild("HatTop1");
        this.HatTop2 = HatTop1.getChild("HatTop2");
        this.HatTop3 = HatTop2.getChild("HatTop3");
        this.HatTop4 = HatTop3.getChild("HatTop4");
        this.HatTop5 = HatTop4.getChild("HatTop5");

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.1F, 0.0F));

        PartDefinition RightLeg = Body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 0.5F));

        PartDefinition RightLegDetail3 = RightLeg.addOrReplaceChild("RightLegDetail3", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0F));

        PartDefinition RightLegDetail1 = RightLeg.addOrReplaceChild("RightLegDetail1", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition RightLegDetail2 = RightLeg.addOrReplaceChild("RightLegDetail2", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition LeftLeg = Body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, 0.5F));

        PartDefinition LeftLegDetail1 = LeftLeg.addOrReplaceChild("LeftLegDetail1", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition LeftLegDetail2 = LeftLeg.addOrReplaceChild("LeftLegDetail2", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition LeftLegDetail3 = LeftLeg.addOrReplaceChild("LeftLegDetail3", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0F));

        PartDefinition Body1 = Body.addOrReplaceChild("Body1", CubeListBuilder.create().texOffs(0, 12).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition Body2 = Body1.addOrReplaceChild("Body2", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition Collar4 = Body2.addOrReplaceChild("Collar4", CubeListBuilder.create().texOffs(29, -3).addBox(0.0F, -1.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -1.0F, 0.0F));

        PartDefinition Collar1 = Body2.addOrReplaceChild("Collar1", CubeListBuilder.create().texOffs(29, 0).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -3.0F));

        PartDefinition Head = Body2.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 53).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition Hat = Head.addOrReplaceChild("Hat", CubeListBuilder.create().texOffs(0, 34).addBox(-3.5F, -0.5F, -2.5F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -3.5F, -0.5F));

        PartDefinition HatDetail4 = Hat.addOrReplaceChild("HatDetail4", CubeListBuilder.create().texOffs(29, 5).addBox(0.0F, -0.5F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 2.0F, 0.5F));

        PartDefinition HatDetail2 = Hat.addOrReplaceChild("HatDetail2", CubeListBuilder.create().texOffs(29, 6).addBox(-3.0F, -0.5F, 0.0F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, 3.5F));

        PartDefinition Hat1 = Hat.addOrReplaceChild("Hat1", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -0.5F, 0.5F));

        PartDefinition HatTop1 = Hat1.addOrReplaceChild("HatTop1", CubeListBuilder.create().texOffs(28, 34).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition HatTop2 = HatTop1.addOrReplaceChild("HatTop2", CubeListBuilder.create().texOffs(28, 30).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -4.2F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition HatTop3 = HatTop2.addOrReplaceChild("HatTop3", CubeListBuilder.create().texOffs(28, 25).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition HatTop4 = HatTop3.addOrReplaceChild("HatTop4", CubeListBuilder.create().texOffs(28, 20).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 3.3F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition HatTop5 = HatTop4.addOrReplaceChild("HatTop5", CubeListBuilder.create().texOffs(22, 23).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition HatDetail1 = Hat.addOrReplaceChild("HatDetail1", CubeListBuilder.create().texOffs(29, 6).addBox(-3.0F, -0.5F, 0.0F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, -2.5F));

        PartDefinition HatDetail3 = Hat.addOrReplaceChild("HatDetail3", CubeListBuilder.create().texOffs(29, 5).addBox(0.0F, -0.5F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 2.0F, 0.5F));

        PartDefinition Collar3 = Body2.addOrReplaceChild("Collar3", CubeListBuilder.create().texOffs(29, -3).addBox(0.0F, -1.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -1.0F, 0.0F));

        PartDefinition Collar2 = Body2.addOrReplaceChild("Collar2", CubeListBuilder.create().texOffs(29, 0).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 3.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        double[] animationShootFire = new double[]
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360};
        double[] animationShootBlizzard = new double[]
                {0, -10, -20, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 60, 50, 40, 30, 20, 10, 5, 0};
        //TODO aero and thunder

        double[] animationMeleeAttack = new double[]
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400, 410, 420, 430, 440, 450, 460, 470, 480, 490, 500, 510, 520, 530};


        if(!Minecraft.getInstance().isPaused()) {
            if(EntityHelper.getState(entityIn) == 1) {
                if (entityIn instanceof BaseElementalMusicalHeartlessEntity) {
                    BaseElementalMusicalHeartlessEntity entity = (BaseElementalMusicalHeartlessEntity) entityIn;
                    if (entity.getElementToUse() == BaseElementalMusicalHeartlessEntity.Element.FIRE) {
                        if (frame < animationShootFire.length) {
                            this.Hat1.y = this.HatTop1.y = -0.2F;
                            this.Hat1.yRot = degToRad(animationShootFire[(int) frame]);
                            this.HatTop1.yRot = degToRad(animationShootFire[(animationShootFire.length - 1) - (int) frame]) * 2;
                        } else {
                            this.Hat1.yRot = this.HatTop1.yRot = degToRad(0);
                            this.Hat1.y = this.HatTop1.y = 0F;
                            frame = 0;
                            EntityHelper.setState(entityIn, 0);
                        }

                        this.frame += 0.7;
                    } else if (entity.getElementToUse() == BaseElementalMusicalHeartlessEntity.Element.BLIZZARD) {
                        if (frame < animationShootBlizzard.length) {
                            this.Body.xRot = degToRad(animationShootBlizzard[(int) frame]);

                            if (frame > animationShootBlizzard.length - 16) {
                                this.Hat1.y = this.HatTop1.y = -0.2F;
                                this.Hat1.yRot = degToRad(animationShootFire[(int) frame]);
                                this.HatTop1.yRot = degToRad(animationShootFire[(animationShootFire.length - 1) - (int) frame]) * 2;
                            }
                        } else {
                            this.Hat1.yRot = this.HatTop1.yRot = degToRad(0);
                            this.Hat1.y = this.HatTop1.y = 0F;
                            this.Body.xRot = degToRad(0);
                            frame = 0;
                            EntityHelper.setState(entity, 0);
                        }

                        this.frame += 0.6;
                    }
                }
            }
            else if(EntityHelper.getState(entityIn) == 2) {
                if(frame < animationMeleeAttack.length) {
                    this.Hat1.y = this.HatTop1.y = -0.6F;
                    this.Body.xRot = degToRad(85);
                    this.Body.yRot = degToRad(animationMeleeAttack[(int) frame]);
                }
                else {
                    this.Body.xRot = degToRad(0);
                    this.Hat1.yRot = this.Body.yRot = degToRad(0);
                    this.Hat1.y = this.HatTop1.y = 0F;
                    frame = 0;
                    EntityHelper.setState(entityIn, 0);
                }

                this.frame += 1.2;
            }
            else if(EntityHelper.getState(entityIn) == 3) {
                if(frame < animationMeleeAttack.length) {
                    this.Body.xRot = degToRad(90);
                    this.Body.zRot = degToRad(animationMeleeAttack[(int) frame]);
                }
                else {
                    this.Body.xRot = this.Body.zRot = degToRad(0);
                    frame = 0;
                    EntityHelper.setState(entityIn, 0);
                }

                this.frame += 1.2;
            }
        }
    }

    protected float degToRad(double degrees) {
        return (float) (degrees * (double)Math.PI / 180);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Body.render(poseStack, buffer, packedLight, packedOverlay);
    }
}