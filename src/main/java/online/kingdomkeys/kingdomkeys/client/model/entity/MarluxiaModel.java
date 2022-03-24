package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * PlayerModel - Either Mojang or a mod author (Taken From Memory)
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class MarluxiaModel<T extends Entity> extends BipedModel<LivingEntity> {

	private final ModelRenderer hair;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer cube_r19;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer cube_r22;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;
	private final ModelRenderer cube_r25;
	private final ModelRenderer cube_r26;
	private final ModelRenderer cube_r27;
	private final ModelRenderer cube_r28;
	private final ModelRenderer cube_r29;
	private final ModelRenderer Cube_r30;
	
    public MarluxiaModel() {
		super(0, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
       // this.bipedHeadwear = new ModelRenderer(this, 32, 0);
       // this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F);
       // this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 32, 48);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        
        hair = new ModelRenderer(this);
		hair.setRotationPoint(0.0F, 24.0F, 0.0F);
		hair.setTextureOffset(0, 0).addBox(-4.0F, -32.0F, -3.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		hair.setTextureOffset(0, 0).addBox(-4.0F, -32.5F, -3.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.6417F, -0.3013F, -1.2132F);
		cube_r1.setTextureOffset(27, 1).addBox(23.0F, -18.0F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r2);
		setRotationAngle(cube_r2, 1.1202F, -0.3281F, -1.4586F);
		cube_r2.setTextureOffset(27, 1).addBox(24.0F, -14.0F, 0.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r2.setTextureOffset(27, 1).addBox(26.0F, -14.0F, -1.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r3);
		setRotationAngle(cube_r3, 1.6142F, -0.3555F, -1.5007F);
		cube_r3.setTextureOffset(27, 1).addBox(26.0F, -14.0F, -0.75F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r4);
		setRotationAngle(cube_r4, -2.8639F, 0.0619F, -1.0233F);
		cube_r4.setTextureOffset(27, 1).addBox(18.0F, 16.0F, -6.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r5);
		setRotationAngle(cube_r5, 1.4897F, 0.0485F, -1.1976F);
		cube_r5.setTextureOffset(25, 2).addBox(22.0F, 2.0F, 14.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r6);
		setRotationAngle(cube_r6, 2.0133F, 0.0485F, -1.1976F);
		cube_r6.setTextureOffset(27, 1).addBox(23.0F, 11.0F, 10.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r7);
		setRotationAngle(cube_r7, 2.2667F, -0.0261F, -1.3482F);
		cube_r7.setTextureOffset(27, 1).addBox(24.0F, 5.0F, 8.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r8);
		setRotationAngle(cube_r8, 2.2756F, 0.2119F, -1.592F);
		cube_r8.setTextureOffset(27, 1).addBox(19.0F, 6.0F, -8.8F, 11.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r9);
		setRotationAngle(cube_r9, 2.1011F, 0.2119F, -1.592F);
		cube_r9.setTextureOffset(27, 1).addBox(19.0F, 6.0F, -8.8F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r9.setTextureOffset(27, 1).addBox(19.0F, 7.0F, -7.8F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r9.setTextureOffset(27, 1).addBox(19.0F, 8.65F, -3.8F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r9.setTextureOffset(27, 0).addBox(19.0F, 9.65F, -4.8F, 11.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r10);
		setRotationAngle(cube_r10, 1.2729F, -0.335F, -1.8521F);
		cube_r10.setTextureOffset(27, 1).addBox(23.0F, -8.5F, -15.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.929F, -0.1011F, -1.8319F);
		cube_r11.setTextureOffset(27, 1).addBox(22.0F, 5.0F, -10.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r11.setTextureOffset(27, 1).addBox(24.0F, 2.0F, -12.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r12);
		setRotationAngle(cube_r12, 1.279F, 0.0661F, -1.8822F);
		cube_r12.setTextureOffset(27, 1).addBox(22.0F, 6.5F, -11.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r12.setTextureOffset(27, 1).addBox(23.0F, 9.0F, -10.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r13);
		setRotationAngle(cube_r13, 1.2958F, 0.03F, -2.0061F);
		cube_r13.setTextureOffset(27, 1).addBox(21.0F, 7.0F, -14.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r14);
		setRotationAngle(cube_r14, 2.0967F, 0.0413F, -1.2846F);
		cube_r14.setTextureOffset(27, 1).addBox(23.0F, 5.0F, 10.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r14.setTextureOffset(27, 1).addBox(23.0F, 7.0F, 8.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r14.setTextureOffset(24, 0).addBox(22.0F, 8.0F, 8.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r15);
		setRotationAngle(cube_r15, 2.0529F, 0.1009F, -1.3005F);
		cube_r15.setTextureOffset(27, 1).addBox(22.0F, 11.0F, 5.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r16);
		setRotationAngle(cube_r16, -2.0319F, 0.1657F, -1.6866F);
		cube_r16.setTextureOffset(27, 1).addBox(23.0F, -12.0F, 0.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r17 = new ModelRenderer(this);
		cube_r17.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r17);
		setRotationAngle(cube_r17, 1.2993F, -0.1498F, -2.0009F);
		cube_r17.setTextureOffset(27, 1).addBox(23.0F, -2.0F, -17.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r18 = new ModelRenderer(this);
		cube_r18.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r18);
		setRotationAngle(cube_r18, 1.2496F, -0.1379F, -1.9584F);
		cube_r18.setTextureOffset(27, 1).addBox(23.0F, 0.0F, -16.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r19 = new ModelRenderer(this);
		cube_r19.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r19);
		setRotationAngle(cube_r19, 1.2993F, -0.1498F, -2.0009F);
		cube_r19.setTextureOffset(27, 1).addBox(22.0F, 0.0F, -16.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r20 = new ModelRenderer(this);
		cube_r20.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r20);
		setRotationAngle(cube_r20, 1.2932F, -0.1379F, -1.9584F);
		cube_r20.setTextureOffset(27, 1).addBox(21.0F, 0.0F, -14.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r20.setTextureOffset(27, 1).addBox(23.0F, 1.0F, -15.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r21 = new ModelRenderer(this);
		cube_r21.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r21);
		setRotationAngle(cube_r21, 1.2781F, -0.1011F, -1.8319F);
		cube_r21.setTextureOffset(27, 1).addBox(22.0F, -2.0F, -12.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r21.setTextureOffset(27, 1).addBox(23.0F, -1.0F, -12.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r22 = new ModelRenderer(this);
		cube_r22.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r22);
		setRotationAngle(cube_r22, 1.1185F, 0.0085F, -1.8503F);
		cube_r22.setTextureOffset(27, 1).addBox(22.0F, 8.0F, -9.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r23 = new ModelRenderer(this);
		cube_r23.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r23);
		setRotationAngle(cube_r23, 2.0574F, 0.2119F, -1.592F);
		cube_r23.setTextureOffset(27, 1).addBox(20.0F, 6.0F, -7.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r23.setTextureOffset(27, 1).addBox(18.0F, 8.0F, -4.2F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r23.setTextureOffset(27, 1).addBox(18.0F, 9.0F, -5.2F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r23.setTextureOffset(27, 1).addBox(23.0F, 8.0F, -6.5F, 7.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r23.setTextureOffset(27, 1).addBox(20.0F, 7.0F, -8.5F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r24 = new ModelRenderer(this);
		cube_r24.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r24);
		setRotationAngle(cube_r24, 2.0574F, 0.2119F, -1.592F);
		cube_r24.setTextureOffset(27, 1).addBox(20.0F, 9.0F, -2.2F, 9.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r24.setTextureOffset(27, 1).addBox(20.0F, 10.0F, -3.2F, 9.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r25 = new ModelRenderer(this);
		cube_r25.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r25);
		setRotationAngle(cube_r25, 2.2756F, 0.2119F, -1.592F);
		cube_r25.setTextureOffset(27, 1).addBox(18.0F, 6.0F, -7.2F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r25.setTextureOffset(27, 1).addBox(18.0F, 5.0F, -8.2F, 11.0F, 1.0F, 1.0F, 0.0F, false);
		cube_r25.setTextureOffset(27, 1).addBox(18.0F, 7.0F, -8.2F, 11.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r26 = new ModelRenderer(this);
		cube_r26.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r26);
		setRotationAngle(cube_r26, 1.2145F, 0.3813F, -1.8024F);
		cube_r26.setTextureOffset(27, 1).addBox(19.0F, 17.0F, -2.5F, 8.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r27 = new ModelRenderer(this);
		cube_r27.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r27);
		setRotationAngle(cube_r27, 2.1118F, 0.4785F, -1.433F);
		cube_r27.setTextureOffset(27, 1).addBox(18.0F, 16.0F, -5.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r28 = new ModelRenderer(this);
		cube_r28.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r28);
		setRotationAngle(cube_r28, 1.4812F, 0.0299F, -1.4151F);
		cube_r28.setTextureOffset(27, 1).addBox(25.0F, -1.0F, 7.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r29 = new ModelRenderer(this);
		cube_r29.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(cube_r29);
		setRotationAngle(cube_r29, 1.2563F, -0.4598F, -1.8093F);
		cube_r29.setTextureOffset(27, 1).addBox(23.0F, -13.0F, -13.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		Cube_r30 = new ModelRenderer(this);
		Cube_r30.setRotationPoint(0.0F, 0.0F, 0.0F);
		hair.addChild(Cube_r30);
		setRotationAngle(Cube_r30, 1.1827F, -0.4967F, -1.6371F);
		Cube_r30.setTextureOffset(27, 1).addBox(23.0F, -16.0F, -9.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		/*bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.setTextureOffset(0, 16).addBox(0.0F, -12.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		bb_main.setTextureOffset(0, 16).addBox(-4.0F, -12.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		bb_main.setTextureOffset(16, 16).addBox(-4.0F, -24.0F, -1.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		bb_main.setTextureOffset(32, 48).addBox(4.0F, -24.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		bb_main.setTextureOffset(40, 16).addBox(-8.0F, -24.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);*/
		this.bipedHead.addChild(hair);
    }       
    
    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    	super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    	this.bipedHeadwear.showModel = true;
		//Hair.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);

    }

   /* @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.leftArm, this.leftLeg, this.rightLeg, this.head, this.body, this.rightArm).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }
*/
    @Override
    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	
    	super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    	
    	if(EntityHelper.getState(entityIn) == 1) {
    		this.bipedRightArm.rotateAngleZ = (float) Math.toRadians(20);
    		this.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(-20);
    		this.bipedLeftArm.rotateAngleX = (float) Math.toRadians(0);

    		this.bipedRightLeg.rotateAngleX = 0;
    		this.bipedLeftLeg.rotateAngleX = 0;
    		
    		this.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(10);
    		this.bipedLeftLeg.rotateAngleZ = (float) Math.toRadians(-10);	
    	}
    	
    	if(EntityHelper.getState(entityIn) == 3) {
    		this.bipedRightArm.rotateAngleZ = (float) Math.toRadians(20);
    		this.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(-20);
    		this.bipedLeftArm.rotateAngleX = (float) Math.toRadians(0);
    		this.bipedRightArm.rotateAngleX = (float) Math.toRadians(0);


    		this.bipedRightLeg.rotateAngleX = 0;
    		this.bipedLeftLeg.rotateAngleX = 0;
    		
    		this.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(10);
    		this.bipedLeftLeg.rotateAngleZ = (float) Math.toRadians(-10);	
    	}
    }
    
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
