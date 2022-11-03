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

/**
 * DirePlant - WYND
 * Created using Tabula 7.0.0
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */
public class DirePlantModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "direplant"), "main");
    private final ModelPart body;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart neck;
    private final ModelPart face1;
    private final ModelPart petal1;
    private final ModelPart petal2;
    private final ModelPart petal3;
    private final ModelPart petal4;
    private final ModelPart leaf1;
    private final ModelPart leaf3;
    private final ModelPart leaf2;
    private final ModelPart leaf4;

    public DirePlantModel(ModelPart root) {
        this.body = root.getChild("body");
        this.body2 = body.getChild("body2");
        this.body3 = body2.getChild("body3");
        this.neck = body3.getChild("neck");
        this.face1 = neck.getChild("face1");
        this.petal1 = face1.getChild("petal1");
        this.petal2 = face1.getChild("petal2");
        this.petal3 = face1.getChild("petal3");
        this.petal4 = face1.getChild("petal4");
        this.leaf1 = root.getChild("leaf1");
        this.leaf3 = root.getChild("leaf3");
        this.leaf2 = root.getChild("leaf2");
        this.leaf4 = root.getChild("leaf4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 15).addBox(-2.5F, -8.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body3 = body2.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -16.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck = body3.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -16.0F, -3.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition face1 = neck.addOrReplaceChild("face1", CubeListBuilder.create().texOffs(0, 37).addBox(-3.5F, -19.0F, -4.0F, 7.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition petal1 = face1.addOrReplaceChild("petal1", CubeListBuilder.create().texOffs(18, 38).addBox(8.9F, -19.8F, -3.5F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition petal2 = face1.addOrReplaceChild("petal2", CubeListBuilder.create().texOffs(28, 38).addBox(8.9F, 2.2F, -3.5F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition petal4 = face1.addOrReplaceChild("petal4", CubeListBuilder.create().texOffs(51, 38).addBox(8.9F, 13.8F, -3.5F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition petal3 = face1.addOrReplaceChild("petal3", CubeListBuilder.create().texOffs(40, 38).addBox(8.9F, -8.2F, -3.5F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition leaf1 = partdefinition.addOrReplaceChild("leaf1", CubeListBuilder.create().texOffs(29, 0).addBox(-1.5F, -5.0F, -3.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 20.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition leaf3 = partdefinition.addOrReplaceChild("leaf3", CubeListBuilder.create().texOffs(28, 21).addBox(-2.5F, -5.0F, 0.0F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 21.0F, -5.1F, 0.6109F, 0.0F, 0.0F));

        PartDefinition leaf2 = partdefinition.addOrReplaceChild("leaf2", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, -5.0F, -3.0F, 0.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3F, 21.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition leaf4 = partdefinition.addOrReplaceChild("leaf4", CubeListBuilder.create().texOffs(45, 21).addBox(-2.5F, -5.0F, 0.0F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, 21.0F, 5.0F, -0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
        leaf1.render(poseStack, buffer, packedLight, packedOverlay);
        leaf3.render(poseStack, buffer, packedLight, packedOverlay);
        leaf2.render(poseStack, buffer, packedLight, packedOverlay);
        leaf4.render(poseStack, buffer, packedLight, packedOverlay);
    }
}