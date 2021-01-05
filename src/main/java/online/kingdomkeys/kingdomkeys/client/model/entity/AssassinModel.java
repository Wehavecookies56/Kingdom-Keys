package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.netty.util.internal.MathUtil;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * Assassin - Abelatox
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AssassinModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer bodyBot;
    public ModelRenderer leftLegTop;
    public ModelRenderer rightLegTop;
    public ModelRenderer bodyTop;
    public ModelRenderer leftLegBot;
    public ModelRenderer leftFootTop;
    public ModelRenderer leftFootBottom;
    public ModelRenderer rightLegBot;
    public ModelRenderer rightFootTop;
    public ModelRenderer rightFootBottom;
    public ModelRenderer neck;
    public ModelRenderer leftArm;
    public ModelRenderer rightArm;
    public ModelRenderer head;

    public AssassinModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.rightLegTop = new ModelRenderer(this, 0, 9);
        this.rightLegTop.setRotationPoint(1.6F, 5.0F, 0.0F);
        this.rightLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(rightLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 35, 0);
        this.neck.setRotationPoint(-1.0F, -0.6F, -0.6F);
        this.neck.addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.7853981633974483F, 0.0F, 0.0F);
        this.leftArm = new ModelRenderer(this, 22, 8);
        this.leftArm.setRotationPoint(3.0F, 1.1F, 1.1F);
        this.leftArm.addBox(-0.5F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftArm, 0.0F, 0.17453292519943295F, 0.17453292519943295F);
        this.head = new ModelRenderer(this, 45, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-0.5F, -3.0F, -0.5F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.bodyBot = new ModelRenderer(this, 0, 0);
        this.bodyBot.setRotationPoint(-2.0F, 3.0F, 0.0F);
        this.bodyBot.addBox(0.0F, 0.0F, 0.0F, 4.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.rightLegBot = new ModelRenderer(this, 9, 9);
        this.rightLegBot.setRotationPoint(0.0F, 4.8F, 1.0F);
        this.rightLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(rightLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightFootBottom = new ModelRenderer(this, 6, 15);
        this.rightFootBottom.setRotationPoint(0.0F, 0.8F, 0.0F);
        this.rightFootBottom.addBox(-1.25F, 0.5F, -4.0F, 1.0F, 1.0F, 4.0F, -0.1F, -0.3F, 0.0F);
        this.leftFootBottom = new ModelRenderer(this, 6, 15);
        this.leftFootBottom.setRotationPoint(0.0F, 0.8F, 0.0F);
        this.leftFootBottom.addBox(-1.25F, 0.5F, -4.0F, 1.0F, 1.0F, 4.0F, -0.1F, -0.3F, 0.0F);
        this.leftLegTop = new ModelRenderer(this, 0, 9);
        this.leftLegTop.setRotationPoint(4.0F, 5.0F, 0.0F);
        this.leftLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(leftLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.leftFootTop = new ModelRenderer(this, 0, 18);
        this.leftFootTop.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.leftFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.rightFootTop = new ModelRenderer(this, 0, 18);
        this.rightFootTop.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.rightFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.rightArm = new ModelRenderer(this, 22, 8);
        this.rightArm.setRotationPoint(-3.0F, 1.1F, 1.1F);
        this.rightArm.addBox(-0.5F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightArm, 0.0F, 0.17453292519943295F, 3.141592653589793F);
        this.bodyTop = new ModelRenderer(this, 15, 0);
        this.bodyTop.setRotationPoint(2.0F, 0.0F, -1.2F);
        this.bodyTop.addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bodyTop, 0.7853981633974483F, 0.0F, 0.0F);
        this.leftLegBot = new ModelRenderer(this, 9, 9);
        this.leftLegBot.setRotationPoint(0.0F, 4.8F, 1.0F);
        this.leftLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(leftLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.bodyBot.addChild(this.rightLegTop);
        this.bodyTop.addChild(this.neck);
        this.bodyTop.addChild(this.leftArm);
        this.neck.addChild(this.head);
        this.rightLegTop.addChild(this.rightLegBot);
        this.rightFootTop.addChild(this.rightFootBottom);
        this.leftFootTop.addChild(this.leftFootBottom);
        this.bodyBot.addChild(this.leftLegTop);
        this.leftLegBot.addChild(this.leftFootTop);
        this.rightLegBot.addChild(this.rightFootTop);
        this.bodyTop.addChild(this.rightArm);
        this.bodyBot.addChild(this.bodyTop);
        this.leftLegTop.addChild(this.leftLegBot);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.bodyBot).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
	public void setRotationAngles(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		if (EntityHelper.getState(ent) == 0) {
			this.rightArm.rotateAngleY = (float) Math.toRadians(70);
			this.rightArm.rotateAngleZ = (float) Math.toRadians(90);
			
			this.leftArm.rotateAngleY = (float) Math.toRadians(70);
			this.leftArm.rotateAngleZ = (float) Math.toRadians(90);
			
		} else if (EntityHelper.getState(ent) == 1) {
			this.rightArm.rotateAngleY = -90;
			this.rightArm.rotateAngleZ = 70;

			this.leftArm.rotateAngleY = -90;
			this.leftArm.rotateAngleZ = 90;
		}

    }


    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
