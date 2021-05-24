package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ShadowGlobModel<T extends Entity> extends EntityModel<T> {
	public ModelRenderer Body1;
	public ModelRenderer Body2;
	public ModelRenderer Body3;
	public ModelRenderer Body4;
	public ModelRenderer Body5;

	public ShadowGlobModel()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;
		this.Body1 = new ModelRenderer(this, 0, 0);
		this.Body1.setRotationPoint(0.0F, 21.0F, 0.0F);
		this.Body1.addBox(-10.0F, 0.0F, -10.0F, 20, 3, 20, 0.0F);
		this.Body5 = new ModelRenderer(this, 0, 0);
		this.Body5.setRotationPoint(0.0F, -9.5F, 0.0F);
		this.Body5.addBox(-8.5F, -1.5F, -8.5F, 17, 3, 17, 0.0F);
		this.Body2 = new ModelRenderer(this, 0, 0);
		this.Body2.setRotationPoint(0.0F, -1.5F, 0.0F);
		this.Body2.addBox(-10.0F, -1.5F, -10.0F, 20, 3, 20, 0.0F);
		this.Body3 = new ModelRenderer(this, 0, 0);
		this.Body3.setRotationPoint(0.0F, -4.5F, 0.0F);
		this.Body3.addBox(-9.5F, -1.5F, -9.5F, 19, 3, 19, 0.0F);
		this.Body4 = new ModelRenderer(this, 0, 0);
		this.Body4.setRotationPoint(0.0F, -6.5F, 0.0F);
		this.Body4.addBox(-9.5F, -1.5F, -9.5F, 19, 2, 19, 0.0F);
		this.Body1.addChild(this.Body5);
		this.Body1.addChild(this.Body2);
		this.Body1.addChild(this.Body3);
		this.Body1.addChild(this.Body4);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.Body1.render(matrixStackIn,bufferIn,packedLightIn,packedOverlayIn);
	}
	
	private double frame;
	private int animType = 0;
	private int animDir = 0;
	
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!Minecraft.getInstance().isGamePaused())
		{	
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
	
	protected float degToRad(double degrees)
	{
		return (float) (degrees * (double) Math.PI / 180);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
