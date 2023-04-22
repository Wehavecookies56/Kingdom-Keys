package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class StopModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "stop"), "main");
	private final ModelPart ClockNumbers;
	private final ModelPart SpikesTop;
	private final ModelPart SpikesBottom;
	private final ModelPart bb_main;


    public StopModel(ModelPart root) {
		this.ClockNumbers = root.getChild("ClockNumbers");
		this.SpikesTop = root.getChild("SpikesTop");
		this.SpikesBottom = root.getChild("SpikesBottom");
		this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition ClockNumbers = partdefinition.addOrReplaceChild("ClockNumbers", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Number12_r1 = ClockNumbers.addOrReplaceChild("Number12_r1", CubeListBuilder.create().texOffs(0, 0).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		PartDefinition Number11_r1 = ClockNumbers.addOrReplaceChild("Number11_r1", CubeListBuilder.create().texOffs(0, 6).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0908F, 0.0F));

		PartDefinition Number10_r1 = ClockNumbers.addOrReplaceChild("Number10_r1", CubeListBuilder.create().texOffs(8, 0).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.6144F, 0.0F));

		PartDefinition Number9_r1 = ClockNumbers.addOrReplaceChild("Number9_r1", CubeListBuilder.create().texOffs(8, 6).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.138F, 0.0F));

		PartDefinition Number8_r1 = ClockNumbers.addOrReplaceChild("Number8_r1", CubeListBuilder.create().texOffs(0, 12).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.6616F, 0.0F));

		PartDefinition Number7_r1 = ClockNumbers.addOrReplaceChild("Number7_r1", CubeListBuilder.create().texOffs(8, 12).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.098F, 0.0F));

		PartDefinition Number6_r1 = ClockNumbers.addOrReplaceChild("Number6_r1", CubeListBuilder.create().texOffs(16, 0).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5744F, 0.0F));

		PartDefinition Number5_r1 = ClockNumbers.addOrReplaceChild("Number5_r1", CubeListBuilder.create().texOffs(16, 6).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.0508F, 0.0F));

		PartDefinition Number4_r1 = ClockNumbers.addOrReplaceChild("Number4_r1", CubeListBuilder.create().texOffs(16, 12).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5272F, 0.0F));

		PartDefinition Number3_r1 = ClockNumbers.addOrReplaceChild("Number3_r1", CubeListBuilder.create().texOffs(0, 18).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.0036F, 0.0F));

		PartDefinition Number2_r1 = ClockNumbers.addOrReplaceChild("Number2_r1", CubeListBuilder.create().texOffs(8, 18).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition Number1_r1 = ClockNumbers.addOrReplaceChild("Number1_r1", CubeListBuilder.create().texOffs(16, 18).addBox(9.0F, -15.0F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.0436F, 0.0F));

		PartDefinition SpikesTop = partdefinition.addOrReplaceChild("SpikesTop", CubeListBuilder.create().texOffs(0, 0).addBox(5.0F, -21.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -21.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -21.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -21.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition SpikesBottom = partdefinition.addOrReplaceChild("SpikesBottom", CubeListBuilder.create().texOffs(0, 0).addBox(5.0F, -21.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -21.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -21.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -21.0F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 40.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(24, 20).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		ClockNumbers.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		SpikesTop.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		SpikesBottom.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}