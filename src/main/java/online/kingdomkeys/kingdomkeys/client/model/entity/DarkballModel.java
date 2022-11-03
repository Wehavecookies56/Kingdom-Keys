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
 * HeartlessDarkball - WYND
 * Created using Tabula 7.0.0
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */
public class DarkballModel<T extends Entity> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "darkball"), "main");
	private final ModelPart body1;
	private final ModelPart bodyDown1;
	private final ModelPart bodyDown2;
	private final ModelPart bodyUp1;
	private final ModelPart bodyUp2;
	private final ModelPart bodyLeft1;
	private final ModelPart bodyLeft2;
	private final ModelPart bodyRight1;
	private final ModelPart bodyRight2;
	private final ModelPart bodyFront1;
	private final ModelPart bodyFront2;
	private final ModelPart bodyBack1;
	private final ModelPart bodyBack2;
	private final ModelPart antenna11;
	private final ModelPart antenna12;
	private final ModelPart antenna13;
	private final ModelPart antenna14;
	private final ModelPart antenna141;
	private final ModelPart antenna142;
	private final ModelPart antenna143;
	private final ModelPart antenna21;
	private final ModelPart antenna22;
	private final ModelPart antenna23;
	private final ModelPart antenna24;
	private final ModelPart antenna241;
	private final ModelPart antenna242;
	private final ModelPart antenna243;
	private final ModelPart antenna31;
	private final ModelPart antenna32;
	private final ModelPart antenna33;
	private final ModelPart antenna34;
	private final ModelPart antenna341;
	private final ModelPart antenna342;
	private final ModelPart antenna343;

	public DarkballModel(ModelPart root) {
		this.body1 = root.getChild("body1");
		this.bodyDown1 = body1.getChild("bodyDown1");
		this.bodyDown2 = body1.getChild("bodyDown2");
		this.bodyUp1 = body1.getChild("bodyUp1");
		this.bodyUp2 = body1.getChild("bodyUp2");
		this.bodyLeft1 = body1.getChild("bodyLeft1");
		this.bodyLeft2 = body1.getChild("bodyLeft2");
		this.bodyRight1 = body1.getChild("bodyRight1");
		this.bodyRight2 = body1.getChild("bodyRight2");
		this.bodyFront1 = body1.getChild("bodyFront1");
		this.bodyFront2 = body1.getChild("bodyFront2");
		this.bodyBack1 = body1.getChild("bodyBack1");
		this.bodyBack2 = body1.getChild("bodyBack2");
		this.antenna11 = root.getChild("antenna11");
		this.antenna12 = antenna11.getChild("antenna12");
		this.antenna13 = antenna12.getChild("antenna13");
		this.antenna14 = antenna13.getChild("antenna14");
		this.antenna141 = antenna14.getChild("antenna141");
		this.antenna142 = antenna14.getChild("antenna142");
		this.antenna143 = antenna14.getChild("antenna143");
		this.antenna21 = root.getChild("antenna21");
		this.antenna22 = antenna21.getChild("antenna22");
		this.antenna23 = antenna22.getChild("antenna23");
		this.antenna24 = antenna23.getChild("antenna24");
		this.antenna241 = antenna24.getChild("antenna241");
		this.antenna242 = antenna24.getChild("antenna242");
		this.antenna243 = antenna24.getChild("antenna243");
		this.antenna31 = root.getChild("antenna31");
		this.antenna32 = antenna31.getChild("antenna32");
		this.antenna33 = antenna32.getChild("antenna33");
		this.antenna34 = antenna33.getChild("antenna34");
		this.antenna341 = antenna34.getChild("antenna341");
		this.antenna342 = antenna34.getChild("antenna342");
		this.antenna343 = antenna34.getChild("antenna343");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(6, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.1F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition bodyDown1 = body1.addOrReplaceChild("bodyDown1", CubeListBuilder.create().texOffs(0, 51).addBox(-4.5F, 4.5F, -4.5F, 9.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyUp2 = body1.addOrReplaceChild("bodyUp2", CubeListBuilder.create().texOffs(32, 49).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyFront2 = body1.addOrReplaceChild("bodyFront2", CubeListBuilder.create().texOffs(38, 22).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyBack1 = body1.addOrReplaceChild("bodyBack1", CubeListBuilder.create().texOffs(0, 21).addBox(-4.5F, -4.5F, 4.5F, 9.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyBack2 = body1.addOrReplaceChild("bodyBack2", CubeListBuilder.create().texOffs(20, 22).addBox(-4.0F, -4.0F, 5.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyRight2 = body1.addOrReplaceChild("bodyRight2", CubeListBuilder.create().texOffs(20, 34).addBox(5.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyLeft1 = body1.addOrReplaceChild("bodyLeft1", CubeListBuilder.create().texOffs(0, 32).addBox(-5.5F, -4.5F, -4.5F, 1.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyDown2 = body1.addOrReplaceChild("bodyDown2", CubeListBuilder.create().texOffs(32, 49).addBox(-4.0F, 5.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyFront1 = body1.addOrReplaceChild("bodyFront1", CubeListBuilder.create().texOffs(0, 21).addBox(-4.5F, -4.5F, -5.5F, 9.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyLeft2 = body1.addOrReplaceChild("bodyLeft2", CubeListBuilder.create().texOffs(20, 34).addBox(-6.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyRight1 = body1.addOrReplaceChild("bodyRight1", CubeListBuilder.create().texOffs(0, 32).addBox(4.5F, -4.5F, -4.5F, 1.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bodyUp1 = body1.addOrReplaceChild("bodyUp1", CubeListBuilder.create().texOffs(0, 51).addBox(-4.5F, -5.5F, -4.5F, 9.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition antenna21 = partdefinition.addOrReplaceChild("antenna21", CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, -3.2F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8F, 2.7F, -0.5F, 0.0F, 1.57F, 0.6807F));

		PartDefinition antenna22 = antenna21.addOrReplaceChild("antenna22", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -5.8F, -1.7F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3643F, 0.0F, 0.0F));

		PartDefinition antenna23 = antenna22.addOrReplaceChild("antenna23", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -7.1F, -4.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5463F, 0.0F, 0.0F));

		PartDefinition antenna24 = antenna23.addOrReplaceChild("antenna24", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -5.8F, -7.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5463F, 0.0F, 0.0F));

		PartDefinition antenna242 = antenna24.addOrReplaceChild("antenna242", CubeListBuilder.create().texOffs(42, 0).addBox(0.4F, -7.6F, -8.1F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1367F, 0.2276F, 0.0456F));

		PartDefinition antenna243 = antenna24.addOrReplaceChild("antenna243", CubeListBuilder.create().texOffs(42, 0).addBox(0.3F, -8.0F, -7.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1367F, 0.182F, -0.182F));

		PartDefinition antenna241 = antenna24.addOrReplaceChild("antenna241", CubeListBuilder.create().texOffs(42, 0).addBox(1.1F, -5.6F, -7.9F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4554F, 0.3643F, 0.0F));

		PartDefinition antenna31 = partdefinition.addOrReplaceChild("antenna31", CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, -3.2F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 13.5F, -0.5F, 2.7751F, -3.1416F, 0.0F));

		PartDefinition antenna32 = antenna31.addOrReplaceChild("antenna32", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -5.8F, -1.7F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3049F, 0.0F, 0.0F));

		PartDefinition antenna33 = antenna32.addOrReplaceChild("antenna33", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -7.1F, -4.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5515F, 0.0F, 0.0F));

		PartDefinition antenna34 = antenna33.addOrReplaceChild("antenna34", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -5.8F, -7.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5463F, 0.0F, 0.0F));

		PartDefinition antenna342 = antenna34.addOrReplaceChild("antenna342", CubeListBuilder.create().texOffs(42, 0).addBox(0.4F, -7.6F, -8.1F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1367F, 0.2276F, 0.0456F));

		PartDefinition antenna343 = antenna34.addOrReplaceChild("antenna343", CubeListBuilder.create().texOffs(42, 0).addBox(0.3F, -8.0F, -7.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1367F, 0.182F, -0.182F));

		PartDefinition antenna341 = antenna34.addOrReplaceChild("antenna341", CubeListBuilder.create().texOffs(42, 0).addBox(1.1F, -5.6F, -7.9F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4554F, 0.3643F, 0.0F));

		PartDefinition antenna11 = partdefinition.addOrReplaceChild("antenna11", CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, -3.2F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9F, 2.8F, 0.9F, 0.0F, -1.57F, -0.7679F));

		PartDefinition antenna12 = antenna11.addOrReplaceChild("antenna12", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -5.8F, -1.7F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3643F, 0.0F, 0.0F));

		PartDefinition antenna13 = antenna12.addOrReplaceChild("antenna13", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -7.1F, -4.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5463F, 0.0F, 0.0F));

		PartDefinition antenna14 = antenna13.addOrReplaceChild("antenna14", CubeListBuilder.create().texOffs(0, 0).addBox(-1.9F, -5.8F, -7.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5463F, 0.0F, 0.0F));

		PartDefinition antenna142 = antenna14.addOrReplaceChild("antenna142", CubeListBuilder.create().texOffs(42, 0).addBox(0.4F, -7.6F, -8.1F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1367F, 0.2276F, 0.0456F));

		PartDefinition antenna141 = antenna14.addOrReplaceChild("antenna141", CubeListBuilder.create().texOffs(42, 0).addBox(1.1F, -5.6F, -7.9F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4554F, 0.3643F, 0.0F));

		PartDefinition antenna143 = antenna14.addOrReplaceChild("antenna143", CubeListBuilder.create().texOffs(42, 0).addBox(0.3F, -8.0F, -7.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1367F, 0.182F, -0.182F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body1.render(poseStack, buffer, packedLight, packedOverlay);
		antenna21.render(poseStack, buffer, packedLight, packedOverlay);
		antenna31.render(poseStack, buffer, packedLight, packedOverlay);
		antenna11.render(poseStack, buffer, packedLight, packedOverlay);
	}
}