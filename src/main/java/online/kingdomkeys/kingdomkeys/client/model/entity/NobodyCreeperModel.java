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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * Creeper - Wynd
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */

public class NobodyCreeperModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "nobodycreeper"), "main");
    public final ModelPart BodyLower;
    public final ModelPart BodyMiddle;
    public final ModelPart LeftLegUpper;
    public final ModelPart LeftLegMiddle;
    public final ModelPart LeftFootUpper;
    public final ModelPart LeftFootLower;
    public final ModelPart RightLegUpper;
    public final ModelPart RightLegMiddle;
    public final ModelPart RightFootUpper;
    public final ModelPart RightFootLower;
    public final ModelPart Neck;
    public final ModelPart Head;
    public final ModelPart HeadPoint;
    public final ModelPart HeadPoint2;
    public final ModelPart BodyUpper;
    public final ModelPart RightArmDetail;
    public final ModelPart RightArmUpper;
    public final ModelPart RightArmLower;
    public final ModelPart RightFootDetail;
    public final ModelPart RightFoot;
    public final ModelPart LeftArmDetail;
    public final ModelPart LeftArmUpper;
    public final ModelPart LeftArmLower;
    public final ModelPart LeftFootDetail;
    public final ModelPart LeftFoot;
    public final ModelPart Spear_Handle;
    public final ModelPart Spear_Detail1;
    public final ModelPart Spear_Detail2;
    public final ModelPart Spear_Head1;
    public final ModelPart Spear_Head2;
    public final ModelPart Spear_Head3;
    public final ModelPart Spear_Wing1;
    public final ModelPart Spear_Wing11;
    public final ModelPart Spear_Wing111;
    public final ModelPart Spear_Wing2;
    public final ModelPart Spear_Wing22;
    public final ModelPart Spear_Wing222;
    public final ModelPart Sword_Handle1;
    public final ModelPart Sword_Handle2;
    public final ModelPart Sword_Handle3;
    public final ModelPart Sword_Handle4;
    public final ModelPart Sword_Handle5;
    public final ModelPart Sword_Handle6;
    public final ModelPart Sword_Blade;
    public final ModelPart Sword_Blade2;
    public final ModelPart Sword_Blade3;
    public final ModelPart Sword_Blade4;
    public final ModelPart Sword_Blade5;

    private int cycleIndex;
    private boolean canAnimate = true;
    private double frame;

    public NobodyCreeperModel(ModelPart root) {
        this.BodyLower = root.getChild("BodyLower");
        this.BodyMiddle = BodyLower.getChild("BodyMiddle");
        this.LeftLegUpper = BodyLower.getChild("LeftLegUpper");
        this.LeftLegMiddle = LeftLegUpper.getChild("LeftLegMiddle");
        this.LeftFootUpper = LeftLegMiddle.getChild("LeftFootUpper");
        this.LeftFootLower = LeftFootUpper.getChild("LeftFootLower");
        this.RightLegUpper = BodyLower.getChild("RightLegUpper");
        this.RightLegMiddle = RightLegUpper.getChild("RightLegMiddle");
        this.RightFootUpper = RightLegMiddle.getChild("RightFootUpper");
        this.RightFootLower = RightFootUpper.getChild("RightFootLower");
        this.Neck = BodyLower.getChild("Neck");
        this.Head = Neck.getChild("Head");
        this.HeadPoint = Head.getChild("HeadPoint");
        this.HeadPoint2 = HeadPoint.getChild("HeadPoint2");
        this.BodyUpper = BodyLower.getChild("BodyUpper");
        this.RightArmDetail = root.getChild("RightArmDetail");
        this.RightArmUpper = RightArmDetail.getChild("RightArmUpper");
        this.RightArmLower = RightArmUpper.getChild("RightArmLower");
        this.RightFootDetail = RightArmLower.getChild("RightFootDetail");
        this.RightFoot = RightArmLower.getChild("RightFoot");
        this.LeftArmDetail = root.getChild("LeftArmDetail");
        this.LeftArmUpper = LeftArmDetail.getChild("LeftArmUpper");
        this.LeftArmLower = LeftArmUpper.getChild("LeftArmLower");
        this.LeftFootDetail = LeftArmLower.getChild("LeftFootDetail");
        this.LeftFoot = LeftArmLower.getChild("LeftFoot");
        this.Spear_Handle = root.getChild("Spear_Handle");
        this.Spear_Detail1 = Spear_Handle.getChild("Spear_Detail1");
        this.Spear_Detail2 = Spear_Handle.getChild("Spear_Detail2");
        this.Spear_Head1 = Spear_Handle.getChild("Spear_Head1");
        this.Spear_Head2 = Spear_Head1.getChild("Spear_Head2");
        this.Spear_Head3 = Spear_Head2.getChild("Spear_Head3");
        this.Spear_Wing1 = Spear_Head1.getChild("Spear_Wing1");
        this.Spear_Wing11 = Spear_Wing1.getChild("Spear_Wing11");
        this.Spear_Wing111 = Spear_Wing11.getChild("Spear_Wing111");
        this.Spear_Wing2 = Spear_Head1.getChild("Spear_Wing2");
        this.Spear_Wing22 = Spear_Wing2.getChild("Spear_Wing22");
        this.Spear_Wing222 = Spear_Wing22.getChild("Spear_Wing222");
        this.Sword_Handle1 = root.getChild("Sword_Handle1");
        this.Sword_Handle2 = Sword_Handle1.getChild("Sword_Handle2");
        this.Sword_Handle3 = Sword_Handle2.getChild("Sword_Handle3");
        this.Sword_Handle4 = Sword_Handle2.getChild("Sword_Handle4");
        this.Sword_Handle5 = Sword_Handle2.getChild("Sword_Handle5");
        this.Sword_Handle6 = Sword_Handle2.getChild("Sword_Handle6");
        this.Sword_Blade = Sword_Handle2.getChild("Sword_Blade");
        this.Sword_Blade2 = Sword_Blade.getChild("Sword_Blade2");
        this.Sword_Blade3 = Sword_Blade.getChild("Sword_Blade3");
        this.Sword_Blade4 = Sword_Blade.getChild("Sword_Blade4");
        this.Sword_Blade5 = Sword_Blade4.getChild("Sword_Blade5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition BodyLower = partdefinition.addOrReplaceChild("BodyLower", CubeListBuilder.create().texOffs(32, 23).addBox(-2.0F, 0.0F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 8.6F, 0.0F));

        PartDefinition BodyMiddle = BodyLower.addOrReplaceChild("BodyMiddle", CubeListBuilder.create().texOffs(31, 17).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftLegUpper = BodyLower.addOrReplaceChild("LeftLegUpper", CubeListBuilder.create().texOffs(23, 29).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.5F, 0.0F, -0.2276F, 0.0F, 0.0F));

        PartDefinition LeftLegMiddle = LeftLegUpper.addOrReplaceChild("LeftLegMiddle", CubeListBuilder.create().texOffs(36, 30).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftFootUpper = LeftLegMiddle.addOrReplaceChild("LeftFootUpper", CubeListBuilder.create().texOffs(25, 38).addBox(-0.5F, 6.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftFootLower = LeftFootUpper.addOrReplaceChild("LeftFootLower", CubeListBuilder.create().texOffs(30, 38).addBox(-1.0F, 2.1F, 9.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.2292F, 0.0F, 0.0F));

        PartDefinition RightLegUpper = BodyLower.addOrReplaceChild("RightLegUpper", CubeListBuilder.create().texOffs(23, 29).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 1.5F, 0.0F, -0.2276F, 0.0F, 0.0F));

        PartDefinition RightLegMiddle = RightLegUpper.addOrReplaceChild("RightLegMiddle", CubeListBuilder.create().texOffs(36, 30).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightFootUpper = RightLegMiddle.addOrReplaceChild("RightFootUpper", CubeListBuilder.create().texOffs(25, 38).addBox(-0.5F, 6.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightFootLower = RightFootUpper.addOrReplaceChild("RightFootLower", CubeListBuilder.create().texOffs(30, 38).addBox(-1.0F, 2.1F, 9.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.2292F, 0.0F, 0.0F));

        PartDefinition Neck = BodyLower.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(19, 11).addBox(-1.0F, -3.0F, -0.9F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.6F, -0.2F, 0.4554F, 0.0F, 0.0F));

        PartDefinition Head = Neck.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(18, 0).addBox(-1.5F, -3.9F, -3.6F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2731F, 0.0F, 0.0F));

        PartDefinition HeadPoint = Head.addOrReplaceChild("HeadPoint", CubeListBuilder.create().texOffs(30, 0).addBox(-1.5F, -4.5F, -4.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadPoint2 = HeadPoint.addOrReplaceChild("HeadPoint2", CubeListBuilder.create().texOffs(37, 5).addBox(-1.5F, -5.0F, -5.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition BodyUpper = BodyLower.addOrReplaceChild("BodyUpper", CubeListBuilder.create().texOffs(31, 10).addBox(-2.5F, -5.0F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightArmDetail = partdefinition.addOrReplaceChild("RightArmDetail", CubeListBuilder.create().texOffs(0, 26).addBox(0.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, 5.7F, -1.0F));

        PartDefinition RightArmUpper = RightArmDetail.addOrReplaceChild("RightArmUpper", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-0.4F, -1.8F, -1.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0016F));

        PartDefinition RightArmLower = RightArmUpper.addOrReplaceChild("RightArmLower", CubeListBuilder.create().texOffs(0, 20).addBox(4.0F, -4.2F, -1.5F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5009F));

        PartDefinition RightFootDetail = RightArmLower.addOrReplaceChild("RightFootDetail", CubeListBuilder.create().texOffs(12, 36).mirror().addBox(11.55F, -13.9F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5009F));

        PartDefinition RightFoot = RightArmLower.addOrReplaceChild("RightFoot", CubeListBuilder.create().texOffs(12, 26).addBox(16.1F, -8.5F, -2.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0911F));

        PartDefinition Spear_Handle = partdefinition.addOrReplaceChild("Spear_Handle", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -10.0F, -0.5F, 1.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Spear_Detail1 = Spear_Handle.addOrReplaceChild("Spear_Detail1", CubeListBuilder.create().texOffs(5, 11).addBox(-1.0F, 11.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Spear_Head1 = Spear_Handle.addOrReplaceChild("Spear_Head1", CubeListBuilder.create().texOffs(7, 18).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition Spear_Head2 = Spear_Head1.addOrReplaceChild("Spear_Head2", CubeListBuilder.create().texOffs(0, 28).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition Spear_Head3 = Spear_Head2.addOrReplaceChild("Spear_Head3", CubeListBuilder.create().texOffs(0, 35).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition Spear_Wing1 = Spear_Head1.addOrReplaceChild("Spear_Wing1", CubeListBuilder.create().texOffs(4, 0).addBox(0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.5F, 0.0F));

        PartDefinition Spear_Wing11 = Spear_Wing1.addOrReplaceChild("Spear_Wing11", CubeListBuilder.create().texOffs(9, 5).addBox(0.5F, 1.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.5F, 0.0F));

        PartDefinition Spear_Wing111 = Spear_Wing11.addOrReplaceChild("Spear_Wing111", CubeListBuilder.create().texOffs(10, 0).addBox(1.5F, 2.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.5F, 0.0F));

        PartDefinition Spear_Wing2 = Spear_Head1.addOrReplaceChild("Spear_Wing2", CubeListBuilder.create().texOffs(4, 0).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 0.5F, 0.0F));

        PartDefinition Spear_Wing22 = Spear_Wing2.addOrReplaceChild("Spear_Wing22", CubeListBuilder.create().texOffs(9, 5).addBox(-3.0F, 1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Spear_Wing222 = Spear_Wing22.addOrReplaceChild("Spear_Wing222", CubeListBuilder.create().texOffs(10, 0).addBox(-4.0F, 3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Spear_Detail2 = Spear_Handle.addOrReplaceChild("Spear_Detail2", CubeListBuilder.create().texOffs(5, 11).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArmDetail = partdefinition.addOrReplaceChild("LeftArmDetail", CubeListBuilder.create().texOffs(0, 26).addBox(0.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6F, 5.7F, -1.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition LeftArmUpper = LeftArmDetail.addOrReplaceChild("LeftArmUpper", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-0.4F, -1.8F, -1.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0016F));

        PartDefinition LeftArmLower = LeftArmUpper.addOrReplaceChild("LeftArmLower", CubeListBuilder.create().texOffs(0, 20).addBox(4.0F, -4.2F, -1.5F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5009F));

        PartDefinition LeftFoot = LeftArmLower.addOrReplaceChild("LeftFoot", CubeListBuilder.create().texOffs(12, 26).addBox(16.1F, -8.5F, -2.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0911F));

        PartDefinition LeftFootDetail = LeftArmLower.addOrReplaceChild("LeftFootDetail", CubeListBuilder.create().texOffs(12, 36).mirror().addBox(11.55F, -13.9F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5009F));

        PartDefinition Sword_Handle1 = partdefinition.addOrReplaceChild("Sword_Handle1", CubeListBuilder.create().texOffs(14, 44).addBox(-0.5F, -6.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition Sword_Handle2 = Sword_Handle1.addOrReplaceChild("Sword_Handle2", CubeListBuilder.create().texOffs(10, 37).addBox(-2.5F, -1.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));

        PartDefinition Sword_Handle6 = Sword_Handle2.addOrReplaceChild("Sword_Handle6", CubeListBuilder.create().texOffs(13, 52).addBox(-1.0F, 7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Handle3 = Sword_Handle2.addOrReplaceChild("Sword_Handle3", CubeListBuilder.create().texOffs(3, 37).addBox(2.5F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Blade = Sword_Handle2.addOrReplaceChild("Sword_Blade", CubeListBuilder.create().texOffs(11, 26).addBox(-2.0F, -9.0F, -0.5F, 4.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Blade3 = Sword_Blade.addOrReplaceChild("Sword_Blade3", CubeListBuilder.create().texOffs(14, 19).addBox(-2.0F, -14.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Blade4 = Sword_Blade.addOrReplaceChild("Sword_Blade4", CubeListBuilder.create().texOffs(11, 14).addBox(-2.0F, -16.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Blade5 = Sword_Blade4.addOrReplaceChild("Sword_Blade5", CubeListBuilder.create().texOffs(13, 10).addBox(-1.0F, -18.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Blade2 = Sword_Blade.addOrReplaceChild("Sword_Blade2", CubeListBuilder.create().texOffs(14, 19).addBox(1.0F, -14.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Handle4 = Sword_Handle2.addOrReplaceChild("Sword_Handle4", CubeListBuilder.create().texOffs(23, 37).addBox(-4.5F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Sword_Handle5 = Sword_Handle2.addOrReplaceChild("Sword_Handle5", CubeListBuilder.create().texOffs(13, 41).addBox(-1.0F, 1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
/*
    	  Manually get these rotations ( in degrees ) while having tabula opened
    	  (or if it's easy like in this case just add +10)

    	  feel free to add more rotations for the sword if you feel like it's too short :)
    	 */
        double[] animationSwordFirstHit = new double[]
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360};
        double[] animationSwordSecondHit = new double[]
                {180, 170, 160, 150, 140, 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 0, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -110, -120, -130, -140, -150, -160, -170, -180, -190, -200, -210, -220, -230, -240};

        // another thing to mention is that going under 5 degrees difference from frame-1 to frame-2 (so for example [0, 5, 10, 15] instead of [0, 10, 20, 30]) won't make the animation smoother or nicer...especially not for long animations
        // but in case of an animation with 5-10 frames you might wanna go from 1 to 1, it really depends on your specific case
        double[] animationLegHit = new double[]
                {28, 30, 40, 50, 60, 70, 60, 50, 40, 30, 20, 10, 0, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -90, -80, -70, -60, -50, -40, -30, -20, -10, 0, 10, 20, 28};


        // we need to check if minecraft is paused otherwise the animation will run while the game is paused (doesn't look that good)
        if(!Minecraft.getInstance().isPaused())
        {
            if(EntityHelper.getState(entity) == 0)
            {
                this.BodyLower.xRot = Mth.cos(limbSwing * 0.8F) * 2.0F * limbSwingAmount;

                this.RightArmDetail.xRot = Mth.cos(limbSwing * 1.8F) * 0.9F * limbSwingAmount;
                this.LeftArmDetail.xRot = Mth.cos(limbSwing * 1.8F + (float) Math.PI) * 0.9F * limbSwingAmount;
            }
            else if (EntityHelper.getState(entity) == 1) // if the sword AI is active we begin the animation
            {
	    		/*
	    		  small bits of math here but I hope it's understandable even if it's not the nicest looking code :'(
	    		  frame is double variable that gets incremented each frame (last line frame += 0.5) it's also used to "scroll" through the animation frames and thus controls the speed of the animation

	    		 (really hope this is understandable cuz I don't really know how to explain it....)

	    		 Important thing to note is that by default vanilla uses radians instead of degrees .... so I have this fancy degToRad (formula stolen from stackoverflow obviously) to transform degrees in radians for readability
	    		 */
                if(frame < animationSwordFirstHit.length)
                {
                    this.Sword_Handle1.zRot = degToRad(animationSwordFirstHit[(int) frame]);
                }
                else if(frame > animationSwordFirstHit.length - 1 && frame < animationSwordFirstHit.length + animationSwordSecondHit.length)
                {
                    this.Sword_Handle1.xRot = degToRad(-90);
                    this.Sword_Handle1.zRot = degToRad(180);
                    this.Sword_Handle1.yRot = degToRad(animationSwordSecondHit[(int) frame - animationSwordFirstHit.length]);
                }
                else
                {
	    			/*
	    			  When the animation is finished, that means when the frame counter goes beyond the possible animatio frames, we reset the rotations (not really visible but it's for future animations)
	    			  reset the frame counter (same reason), and the state to make it look like a mob again
	    			 */
                    this.Sword_Handle1.xRot = this.Sword_Handle1.yRot = this.Sword_Handle1.zRot = degToRad(0);
                    frame = 0;
                    EntityHelper.setState(entity, 0);
                }

                frame += 0.5;
            }
            else if(EntityHelper.getState(entity) == 3) // if the sword AI is active we begin the animation
            {
                if(frame < animationLegHit.length)
                {
                    this.BodyLower.xRot = degToRad(animationLegHit[(int) frame]);
                }
                else
                {
                    this.BodyLower.xRot = degToRad(28);
                    frame = 0;
                    EntityHelper.setState(entity, 0);
                }

                frame += 0.6;
            }
        }
    }

    protected float degToRad(double degrees)
    {
        return (float) (degrees * (double)Math.PI / 180) ;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    }
}