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
 * ModelMinuteBomb - Nathan
 * Created using Tabula 7.0.0
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */
public class BombModel<T extends Entity> extends EntityModel<T> {
    public float[] modelScale = new float[] { 1.0F, 1.2F, 1.0F };
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "bomb"), "main");
    private final ModelPart headMain;
    private final ModelPart BottemHead;
    private final ModelPart TopHead;
    private final ModelPart Wick1;
    private final ModelPart wick2;
    private final ModelPart wick3;
    private final ModelPart wick4;
    private final ModelPart wick5;
    private final ModelPart body;
    private final ModelPart leftleg;
    private final ModelPart rightleg;

    public BombModel(ModelPart root) {
        this.headMain = root.getChild("headMain");
        this.BottemHead = headMain.getChild("BottemHead");
        this.TopHead = headMain.getChild("TopHead");
        this.Wick1 = TopHead.getChild("Wick1");
        this.wick2 = Wick1.getChild("wick2");
        this.wick3 = wick2.getChild("wick3");
        this.wick4 = wick3.getChild("wick4");
        this.wick5 = wick4.getChild("wick5");
        this.body = root.getChild("body");
        this.leftleg = body.getChild("leftleg");
        this.rightleg = body.getChild("rightleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition headMain = partdefinition.addOrReplaceChild("headMain", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.8F, 0.0F));

        PartDefinition BottemHead = headMain.addOrReplaceChild("BottemHead", CubeListBuilder.create().texOffs(37, 11).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.2F, 0.0F, -0.0136F, 0.0F, 0.0F));

        PartDefinition TopHead = headMain.addOrReplaceChild("TopHead", CubeListBuilder.create().texOffs(24, 0).addBox(-2.5F, -1.0F, -3.5F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.9F, 0.5F, -0.0099F, -0.0099F, 0.0F));

        PartDefinition Wick1 = TopHead.addOrReplaceChild("Wick1", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.6F, -2.5F, -0.6F));

        PartDefinition wick2 = Wick1.addOrReplaceChild("wick2", CubeListBuilder.create().texOffs(3, 13).addBox(-2.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition wick3 = wick2.addOrReplaceChild("wick3", CubeListBuilder.create().texOffs(10, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition wick4 = wick3.addOrReplaceChild("wick4", CubeListBuilder.create().texOffs(14, 13).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition wick5 = wick4.addOrReplaceChild("wick5", CubeListBuilder.create().texOffs(25, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 2.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.9F, 0.0F));

        PartDefinition leftleg = body.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(21, 23).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, 4.4F, 0.0F));

        PartDefinition rightleg = body.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(14, 24).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.3F, 4.4F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1F / modelScale[0], 1F / modelScale[1], 1F / modelScale[2]);
        headMain.render(poseStack, buffer, packedLight, packedOverlay);
        body.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}