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

public class ShadowGlobModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "shadowglob"), "main");
	private final ModelPart Body1;
	private final ModelPart Body2;
	private final ModelPart Body3;
	private final ModelPart Body4;
	private final ModelPart Body5;

	public ShadowGlobModel(ModelPart root) {
		this.Body1 = root.getChild("Body1");
		this.Body2 = Body1.getChild("Body2");
		this.Body3 = Body1.getChild("Body3");
		this.Body4 = Body1.getChild("Body4");
		this.Body5 = Body1.getChild("Body5");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body1 = partdefinition.addOrReplaceChild("Body1", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -10.0F, 20.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));

		PartDefinition Body5 = Body1.addOrReplaceChild("Body5", CubeListBuilder.create().texOffs(0, 0).addBox(-8.5F, -1.5F, -8.5F, 17.0F, 3.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition Body2 = Body1.addOrReplaceChild("Body2", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -1.5F, -10.0F, 20.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition Body3 = Body1.addOrReplaceChild("Body3", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, -1.5F, -9.5F, 19.0F, 3.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 0.0F));

		PartDefinition Body4 = Body1.addOrReplaceChild("Body4", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, -1.5F, -9.5F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!Minecraft.getInstance().isPaused()) {
			/*double[] animationBody2 = null;
			double[] animationBody3 = null;
			double[] animationBody4 = null;
			double[] animationBody5 = null;

			if(animType == 0 || animType == 1)
			{
				animationBody2 = EntityHelper.generateAnimationArray(0, -0.1, 0.1, 0.01, 1);
				animationBody3 = EntityHelper.generateAnimationArray(0, -0.1, 0.1, 0.012, 1);
				animationBody4 = EntityHelper.generateAnimationArray(0, -0.1, 0.1, 0.014, 1);
				animationBody5 = EntityHelper.generateAnimationArray(0, -0.1, 0.1, 0.016, 1);
			}
			else if(animType == 2)
			{
				animationBody2 = EntityHelper.generateAnimationArray(0, -0.1, 0.15, 0.02, 1);
				animationBody3 = EntityHelper.generateAnimationArray(0, -0.1, 0.15, 0.018, 1);
				animationBody4 = EntityHelper.generateAnimationArray(0, -0.1, 0.15, 0.016, 1);
				animationBody5 = EntityHelper.generateAnimationArray(0, -0.1, 0.15, 0.014, 1);
			}

			int direction = 0;

			if(entity.hurtResistantTime > 10)
			{
				if(animDir == 0) direction = 1;
				else direction = -1;

				if(animType == 0)
				{
				    if(frame < animationBody2.length)
				    	this.Body2.offsetX = (float) animationBody2[(int) frame] * direction;

				    if(frame < animationBody3.length)
				    	this.Body3.offsetX = (float) animationBody3[(int) frame] * direction;

				   	if(frame < animationBody4.length)
				    	this.Body4.offsetX = (float) animationBody4[(int) frame] * direction;

				    if(frame < animationBody5.length)
				    	this.Body5.offsetX = (float) animationBody5[(int) frame] * direction;
				}
				else if(animType == 1)
				{
			    	if(frame < animationBody2.length)
			    		this.Body2.offsetZ = (float) animationBody2[(int) frame] * direction;

			    	if(frame < animationBody3.length)
			    		this.Body3.offsetZ = (float) animationBody3[(int) frame] * direction;

			    	if(frame < animationBody4.length)
			    		this.Body4.offsetZ = (float) animationBody4[(int) frame] * direction;

			    	if(frame < animationBody5.length)
			    		this.Body5.offsetZ = (float) animationBody5[(int) frame] * direction;
				}
				else if(animType == 2)
				{
			    	if(frame < animationBody2.length)
			    		this.Body2.offsetY = (float) animationBody2[(int) frame];

			    	if(frame < animationBody3.length)
			    		this.Body3.offsetY = (float) animationBody3[(int) frame];

			    	if(frame < animationBody4.length)
			    		this.Body4.offsetY = (float) animationBody4[(int) frame];

			    	if(frame < animationBody5.length)
			    		this.Body5.offsetY = (float) animationBody5[(int) frame];
				}

	    		frame += 0.8;
			}
			else
			{
				this.Body2.offsetX = 0;
				this.Body3.offsetX = 0;
				this.Body4.offsetX = 0;
				this.Body5.offsetX = 0;

				this.Body2.offsetZ = 0;
				this.Body3.offsetZ = 0;
				this.Body4.offsetZ = 0;
				this.Body5.offsetZ = 0;

				this.Body2.offsetY = 0;
				this.Body3.offsetY = 0;
				this.Body4.offsetY = 0;
				this.Body5.offsetY = 0;

				frame = 0;

				animType = new Random().nextInt(3);
				animDir = new Random().nextInt(2);
			}*/
		}
	}

	protected float degToRad(double degrees) {
		return (float) (degrees * (double) Math.PI / 180);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body1.render(poseStack, buffer, packedLight, packedOverlay);
	}
}