package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.Angle;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.ModelAnimation;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.AssassinEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Assassin - Abelatox
 * Created using Tabula 8.0.0
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */

public class AssassinModel<T extends AssassinEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "assassin"), "main");

    private final ModelPart bodyBot;
    private final ModelPart bodyTop;
    private final ModelPart rightArm;
    private final ModelPart rightArmSpike4;
    private final ModelPart rightArmSpike5;
    private final ModelPart rightArmSpike0;
    private final ModelPart rightArmSpike1;
    private final ModelPart rightArmSpike2;
    private final ModelPart rightArmSpike3;
    private final ModelPart leftArm;
    private final ModelPart leftArmSpike4;
    private final ModelPart leftArmSpike5;
    private final ModelPart leftArmSpike0;
    private final ModelPart leftArmSpike1;
    private final ModelPart leftArmSpike2;
    private final ModelPart leftArmSpike3;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart face;
    private final ModelPart helmet;
    private final ModelPart horn1;
    private final ModelPart horn2;
    private final ModelPart rightLegTop;
    private final ModelPart rightLegBot;
    private final ModelPart rightFootTop;
    private final ModelPart rightFootBottom;
    private final ModelPart leftLegTop;
    private final ModelPart leftLegBot;
    private final ModelPart leftFootTop;
    private final ModelPart leftFootBottom;
    private final ModelPart skirtBack;
    private final ModelPart skirtTop;

    public AssassinModel(ModelPart root) {
        this.bodyBot = root.getChild("bodyBot");
        this.bodyTop = bodyBot.getChild("bodyTop");
        this.rightArm = bodyTop.getChild("rightArm");
        this.rightArmSpike4 = rightArm.getChild("rightArmSpike4");
        this.rightArmSpike5 = rightArmSpike4.getChild("rightArmSpike5");
        this.rightArmSpike0 = rightArm.getChild("rightArmSpike0");
        this.rightArmSpike1 = rightArmSpike0.getChild("rightArmSpike1");
        this.rightArmSpike2 = rightArmSpike1.getChild("rightArmSpike2");
        this.rightArmSpike3 = rightArmSpike2.getChild("rightArmSpike3");
        this.leftArm = bodyTop.getChild("leftArm");
        this.leftArmSpike4 = leftArm.getChild("leftArmSpike4");
        this.leftArmSpike5 = leftArmSpike4.getChild("leftArmSpike5");
        this.leftArmSpike0 = leftArm.getChild("leftArmSpike0");
        this.leftArmSpike1 = leftArmSpike0.getChild("leftArmSpike1");
        this.leftArmSpike2 = leftArmSpike1.getChild("leftArmSpike2");
        this.leftArmSpike3 = leftArmSpike2.getChild("leftArmSpike3");
        this.neck = bodyTop.getChild("neck");
        this.head = neck.getChild("head");
        this.face = head.getChild("face");
        this.helmet = head.getChild("helmet");
        this.horn1 = helmet.getChild("horn1");
        this.horn2 = helmet.getChild("horn2");
        this.rightLegTop = bodyBot.getChild("rightLegTop");
        this.rightLegBot = rightLegTop.getChild("rightLegBot");
        this.rightFootTop = rightLegBot.getChild("rightFootTop");
        this.rightFootBottom = rightFootTop.getChild("rightFootBottom");
        this.leftLegTop = bodyBot.getChild("leftLegTop");
        this.leftLegBot = leftLegTop.getChild("leftLegBot");
        this.leftFootTop = leftLegBot.getChild("leftFootTop");
        this.leftFootBottom = leftFootTop.getChild("leftFootBottom");
        this.skirtBack = bodyBot.getChild("skirtBack");
        this.skirtTop = skirtBack.getChild("skirtTop");

        ModelAnimation leftLegTopAnim = new ModelAnimation(leftLegTop, -35, -55, -15, 0, true, Angle.X, rightLegTop);
        animation.add(leftLegTopAnim);
    }

    List<ModelAnimation> animation = new ArrayList<ModelAnimation>();
    protected double distanceMovedTotal = 0.0D;

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bodyBot = partdefinition.addOrReplaceChild("bodyBot", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 10.4F, 0.0F));

        PartDefinition bodyTop = bodyBot.addOrReplaceChild("bodyTop", CubeListBuilder.create().texOffs(15, 0).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -1.2F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rightArm = bodyTop.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(22, 8).addBox(-1.5F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.100000024F, 0.0F, -0.100000024F)), PartPose.offsetAndRotation(3.0F, 1.1F, 1.1F, -1.9199F, 0.0F, -3.1416F));

        PartDefinition rightArmSpike4 = rightArm.addOrReplaceChild("rightArmSpike4", CubeListBuilder.create().texOffs(36, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.100000024F, 0.0F, 0.100000024F)), PartPose.offset(-0.5F, 8.0F, 0.0F));

        PartDefinition rightArmSpike5 = rightArmSpike4.addOrReplaceChild("rightArmSpike5", CubeListBuilder.create().texOffs(36, 8).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.19999999F, 0.0F, -0.19999999F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition rightArmSpike0 = rightArm.addOrReplaceChild("rightArmSpike0", CubeListBuilder.create().texOffs(46, 17).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(-0.8F, 0.5F, 0.0F));

        PartDefinition rightArmSpike1 = rightArmSpike0.addOrReplaceChild("rightArmSpike1", CubeListBuilder.create().texOffs(46, 24).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition rightArmSpike2 = rightArmSpike1.addOrReplaceChild("rightArmSpike2", CubeListBuilder.create().texOffs(46, 17).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition rightArmSpike3 = rightArmSpike2.addOrReplaceChild("rightArmSpike3", CubeListBuilder.create().texOffs(46, 24).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leftArm = bodyTop.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(22, 8).addBox(-1.5F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.100000024F, 0.0F, -0.100000024F)), PartPose.offsetAndRotation(-3.0F, 1.1F, 1.1F, 4.7124F, 0.0F, 0.0F));

        PartDefinition leftArmSpike0 = leftArm.addOrReplaceChild("leftArmSpike0", CubeListBuilder.create().texOffs(46, 17).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(-0.8F, 0.5F, 0.0F));

        PartDefinition leftArmSpike1 = leftArmSpike0.addOrReplaceChild("leftArmSpike1", CubeListBuilder.create().texOffs(46, 24).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leftArmSpike2 = leftArmSpike1.addOrReplaceChild("leftArmSpike2", CubeListBuilder.create().texOffs(46, 17).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leftArmSpike3 = leftArmSpike2.addOrReplaceChild("leftArmSpike3", CubeListBuilder.create().texOffs(46, 24).mirror().addBox(-6.4F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, new CubeDeformation(-2.2F, -1.9F, -0.49F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leftArmSpike4 = leftArm.addOrReplaceChild("leftArmSpike4", CubeListBuilder.create().texOffs(36, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.100000024F, 0.0F, 0.100000024F)), PartPose.offset(-0.5F, 8.0F, 0.0F));

        PartDefinition leftArmSpike5 = leftArmSpike4.addOrReplaceChild("leftArmSpike5", CubeListBuilder.create().texOffs(36, 8).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.19999999F, 0.0F, -0.19999999F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition neck = bodyTop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(35, 0).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.6F, -0.6F, 0.7854F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(45, 0).addBox(-2.5F, -2.0F, -0.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition face = head.addOrReplaceChild("face", CubeListBuilder.create().texOffs(28, 20).addBox(-2.5F, -0.5F, -0.5F, 5.0F, 9.0F, 1.0F, new CubeDeformation(-1.0F, 0.0F, -0.4F)), PartPose.offsetAndRotation(-1.0F, -2.2F, 0.8F, -2.0944F, 0.0F, 0.0F));

        PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(47, 8).addBox(-2.0F, -2.6F, 1.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        PartDefinition horn1 = helmet.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(35, 12).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(-0.3F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -3.4F, 2.0F, -0.8727F, 0.0F, 0.0F));

        PartDefinition horn2 = helmet.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(41, 13).addBox(-0.5F, -8.0F, 0.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(-0.3F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition rightLegTop = bodyBot.addOrReplaceChild("rightLegTop", CubeListBuilder.create().texOffs(0, 9).addBox(-0.2F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.20000005F, 0.0F, -0.20000005F)), PartPose.offsetAndRotation(-1.6F, 5.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition rightLegBot = rightLegTop.addOrReplaceChild("rightLegBot", CubeListBuilder.create().texOffs(9, 9).addBox(-0.25F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(-0.29999995F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 4.8F, 1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition rightFootTop = rightLegBot.addOrReplaceChild("rightFootTop", CubeListBuilder.create().texOffs(0, 18).addBox(0.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition rightFootBottom = rightFootTop.addOrReplaceChild("rightFootBottom", CubeListBuilder.create().texOffs(6, 15).addBox(0.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.100000024F, -0.29999998F, 0.0F)), PartPose.offset(0.0F, 0.8F, 0.0F));

        PartDefinition skirtBack = bodyBot.addOrReplaceChild("skirtBack", CubeListBuilder.create().texOffs(0, 23).addBox(-2.0F, 0.0F, -0.4F, 4.0F, 8.0F, 1.0F, new CubeDeformation(0.099999905F, 0.0F, -0.39999998F)), PartPose.offset(-2.0F, 3.5F, 2.9F));

        PartDefinition skirtTop = skirtBack.addOrReplaceChild("skirtTop", CubeListBuilder.create().texOffs(11, 23).addBox(-2.0F, 0.0F, -3.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.099999905F, 0.0F, -0.4000001F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftLegTop = bodyBot.addOrReplaceChild("leftLegTop", CubeListBuilder.create().texOffs(0, 9).addBox(-0.2F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.20000005F, 0.0F, -0.20000005F)), PartPose.offsetAndRotation(-4.0F, 5.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition leftLegBot = leftLegTop.addOrReplaceChild("leftLegBot", CubeListBuilder.create().texOffs(9, 9).addBox(-0.25F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(-0.29999995F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 4.8F, 1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition leftFootTop = leftLegBot.addOrReplaceChild("leftFootTop", CubeListBuilder.create().texOffs(0, 18).addBox(0.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 3.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition leftFootBottom = leftFootTop.addOrReplaceChild("leftFootBottom", CubeListBuilder.create().texOffs(6, 15).addBox(0.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.100000024F, -0.29999998F, 0.0F)), PartPose.offset(0.0F, 0.8F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        updateDistanceMovedTotal(ent);
        if (ent.distanceToSqr(ent.xOld, ent.yOld, ent.zOld) > 0) {
            for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
                ModelAnimation m = animation.get(i);

                if(m != null && m.model != null) {
                    if(m.increasing) { //animation increase
                        m.actVal += 2;
                        if(m.actVal >= m.maxVal) {
                            m.increasing = false;
                        }
                    } else { //Animation decrease
                        m.actVal -= 2;
                        if(m.actVal <= m.minVal) {
                            m.increasing = true;
                        }
                    }
                    m.model.xRot = (float) Math.toRadians(m.actVal);
                    if(m.modelCounterpart != null) {
                        m.modelCounterpart.xRot = (float) Math.toRadians(m.defVal*2-m.actVal);
                    }
                }
            }

        } else {
            this.leftLegTop.xRot = this.rightLegTop.xRot = (float) Math.toRadians(-35);
            this.leftLegBot.xRot = this.rightLegBot.xRot = (float) Math.toRadians(10);
            this.leftFootTop.xRot = this.rightFootTop.xRot = (float) Math.toRadians(25);
        }

		if (EntityHelper.getState(ent) == 0) { //Standing
			//this.rightArm.rotateAngleX =  (float) Math.toRadians(0);
			this.rightArm.yRot = (float) Math.toRadians(0);
			this.rightArm.zRot = (float) Math.toRadians(180);

			this.leftArm.xRot =  (float) Math.toRadians(270);
			this.leftArm.yRot = (float) Math.toRadians(0);
			this.leftArm.zRot = (float) Math.toRadians(0);

		} else if (EntityHelper.getState(ent) == 1) { //Underground
			this.rightArm.xRot =  (float) Math.toRadians(270);
			this.rightArm.yRot = (float) Math.toRadians(90 - ent.tickCount*50);
			this.rightArm.zRot = (float) Math.toRadians(90);

			this.leftArm.xRot =  (float) Math.toRadians(270);
			this.leftArm.yRot = (float) Math.toRadians(-90 - ent.tickCount*50);
			this.leftArm.zRot = (float) Math.toRadians(90);

		} else if (EntityHelper.getState(ent) == 2) { //Exploding
			this.leftLegTop.xRot = (float) Math.toRadians(-135);
			this.rightLegTop.xRot = (float) Math.toRadians(-135);
			this.leftLegBot.xRot = (float) Math.toRadians(90);
			this.rightLegBot.xRot = (float) Math.toRadians(90);

			this.leftArm.yRot = (float) Math.toRadians(110);
			this.rightArm.yRot = (float) Math.toRadians(110);
		}
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bodyBot.render(poseStack, buffer, packedLight, packedOverlay);
    }

    protected void updateDistanceMovedTotal(Entity e) {
        distanceMovedTotal += e.distanceToSqr(e.xOld, e.yOld, e.zOld);
    }

    protected double getDistanceMovedTotal() {
        return (distanceMovedTotal);
    }
}