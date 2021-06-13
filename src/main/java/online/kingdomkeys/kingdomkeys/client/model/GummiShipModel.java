package online.kingdomkeys.kingdomkeys.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GummiShipModel<T extends Entity> extends EntityModel<T> {
	public static final byte DIMENSIONS = 9;
    
	public ModelRenderer[] parts = new ModelRenderer[729];

    public GummiShipModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.parts[0] = new ModelRenderer(this, 0, 0);
        this.parts[0].setRotationPoint(-64F, -64F, -64F);
        this.parts[0].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[1] = new ModelRenderer(this, 0, 0);
        this.parts[1].setRotationPoint(-64F, -64F, -48F);
        this.parts[1].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[2] = new ModelRenderer(this, 0, 0);
        this.parts[2].setRotationPoint(-64F, -64F, -32F);
        this.parts[2].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[3] = new ModelRenderer(this, 0, 0);
        this.parts[3].setRotationPoint(-64F, -64F, -16F);
        this.parts[3].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[4] = new ModelRenderer(this, 0, 0);
        this.parts[4].setRotationPoint(-64F, -64F, 0F);
        this.parts[4].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[5] = new ModelRenderer(this, 0, 0);
        this.parts[5].setRotationPoint(-64F, -64F, 16F);
        this.parts[5].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[6] = new ModelRenderer(this, 0, 0);
        this.parts[6].setRotationPoint(-64F, -64F, 32F);
        this.parts[6].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[7] = new ModelRenderer(this, 0, 0);
        this.parts[7].setRotationPoint(-64F, -64F, 48F);
        this.parts[7].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[8] = new ModelRenderer(this, 0, 0);
        this.parts[8].setRotationPoint(-64F, -64F, 64F);
        this.parts[8].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[9] = new ModelRenderer(this, 0, 0);
        this.parts[9].setRotationPoint(-64F, -48F, -64F);
        this.parts[9].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[10] = new ModelRenderer(this, 0, 0);
        this.parts[10].setRotationPoint(-64F, -48F, -48F);
        this.parts[10].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[11] = new ModelRenderer(this, 0, 0);
        this.parts[11].setRotationPoint(-64F, -48F, -32F);
        this.parts[11].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[12] = new ModelRenderer(this, 0, 0);
        this.parts[12].setRotationPoint(-64F, -48F, -16F);
        this.parts[12].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[13] = new ModelRenderer(this, 0, 0);
        this.parts[13].setRotationPoint(-64F, -48F, 0F);
        this.parts[13].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[14] = new ModelRenderer(this, 0, 0);
        this.parts[14].setRotationPoint(-64F, -48F, 16F);
        this.parts[14].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[15] = new ModelRenderer(this, 0, 0);
        this.parts[15].setRotationPoint(-64F, -48F, 32F);
        this.parts[15].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[16] = new ModelRenderer(this, 0, 0);
        this.parts[16].setRotationPoint(-64F, -48F, 48F);
        this.parts[16].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[17] = new ModelRenderer(this, 0, 0);
        this.parts[17].setRotationPoint(-64F, -48F, 64F);
        this.parts[17].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[18] = new ModelRenderer(this, 0, 0);
        this.parts[18].setRotationPoint(-64F, -32F, -64F);
        this.parts[18].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[19] = new ModelRenderer(this, 0, 0);
        this.parts[19].setRotationPoint(-64F, -32F, -48F);
        this.parts[19].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[20] = new ModelRenderer(this, 0, 0);
        this.parts[20].setRotationPoint(-64F, -32F, -32F);
        this.parts[20].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[21] = new ModelRenderer(this, 0, 0);
        this.parts[21].setRotationPoint(-64F, -32F, -16F);
        this.parts[21].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[22] = new ModelRenderer(this, 0, 0);
        this.parts[22].setRotationPoint(-64F, -32F, 0F);
        this.parts[22].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[23] = new ModelRenderer(this, 0, 0);
        this.parts[23].setRotationPoint(-64F, -32F, 16F);
        this.parts[23].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[24] = new ModelRenderer(this, 0, 0);
        this.parts[24].setRotationPoint(-64F, -32F, 32F);
        this.parts[24].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[25] = new ModelRenderer(this, 0, 0);
        this.parts[25].setRotationPoint(-64F, -32F, 48F);
        this.parts[25].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[26] = new ModelRenderer(this, 0, 0);
        this.parts[26].setRotationPoint(-64F, -32F, 64F);
        this.parts[26].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[27] = new ModelRenderer(this, 0, 0);
        this.parts[27].setRotationPoint(-64F, -16F, -64F);
        this.parts[27].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[28] = new ModelRenderer(this, 0, 0);
        this.parts[28].setRotationPoint(-64F, -16F, -48F);
        this.parts[28].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[29] = new ModelRenderer(this, 0, 0);
        this.parts[29].setRotationPoint(-64F, -16F, -32F);
        this.parts[29].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[30] = new ModelRenderer(this, 0, 0);
        this.parts[30].setRotationPoint(-64F, -16F, -16F);
        this.parts[30].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[31] = new ModelRenderer(this, 0, 0);
        this.parts[31].setRotationPoint(-64F, -16F, 0F);
        this.parts[31].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[32] = new ModelRenderer(this, 0, 0);
        this.parts[32].setRotationPoint(-64F, -16F, 16F);
        this.parts[32].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[33] = new ModelRenderer(this, 0, 0);
        this.parts[33].setRotationPoint(-64F, -16F, 32F);
        this.parts[33].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[34] = new ModelRenderer(this, 0, 0);
        this.parts[34].setRotationPoint(-64F, -16F, 48F);
        this.parts[34].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[35] = new ModelRenderer(this, 0, 0);
        this.parts[35].setRotationPoint(-64F, -16F, 64F);
        this.parts[35].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[36] = new ModelRenderer(this, 0, 0);
        this.parts[36].setRotationPoint(-64F, 0F, -64F);
        this.parts[36].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[37] = new ModelRenderer(this, 0, 0);
        this.parts[37].setRotationPoint(-64F, 0F, -48F);
        this.parts[37].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[38] = new ModelRenderer(this, 0, 0);
        this.parts[38].setRotationPoint(-64F, 0F, -32F);
        this.parts[38].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[39] = new ModelRenderer(this, 0, 0);
        this.parts[39].setRotationPoint(-64F, 0F, -16F);
        this.parts[39].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[40] = new ModelRenderer(this, 0, 0);
        this.parts[40].setRotationPoint(-64F, 0F, 0F);
        this.parts[40].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[41] = new ModelRenderer(this, 0, 0);
        this.parts[41].setRotationPoint(-64F, 0F, 16F);
        this.parts[41].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[42] = new ModelRenderer(this, 0, 0);
        this.parts[42].setRotationPoint(-64F, 0F, 32F);
        this.parts[42].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[43] = new ModelRenderer(this, 0, 0);
        this.parts[43].setRotationPoint(-64F, 0F, 48F);
        this.parts[43].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[44] = new ModelRenderer(this, 0, 0);
        this.parts[44].setRotationPoint(-64F, 0F, 64F);
        this.parts[44].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[45] = new ModelRenderer(this, 0, 0);
        this.parts[45].setRotationPoint(-64F, 16F, -64F);
        this.parts[45].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[46] = new ModelRenderer(this, 0, 0);
        this.parts[46].setRotationPoint(-64F, 16F, -48F);
        this.parts[46].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[47] = new ModelRenderer(this, 0, 0);
        this.parts[47].setRotationPoint(-64F, 16F, -32F);
        this.parts[47].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[48] = new ModelRenderer(this, 0, 0);
        this.parts[48].setRotationPoint(-64F, 16F, -16F);
        this.parts[48].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[49] = new ModelRenderer(this, 0, 0);
        this.parts[49].setRotationPoint(-64F, 16F, 0F);
        this.parts[49].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[50] = new ModelRenderer(this, 0, 0);
        this.parts[50].setRotationPoint(-64F, 16F, 16F);
        this.parts[50].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[51] = new ModelRenderer(this, 0, 0);
        this.parts[51].setRotationPoint(-64F, 16F, 32F);
        this.parts[51].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[52] = new ModelRenderer(this, 0, 0);
        this.parts[52].setRotationPoint(-64F, 16F, 48F);
        this.parts[52].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[53] = new ModelRenderer(this, 0, 0);
        this.parts[53].setRotationPoint(-64F, 16F, 64F);
        this.parts[53].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[54] = new ModelRenderer(this, 0, 0);
        this.parts[54].setRotationPoint(-64F, 32F, -64F);
        this.parts[54].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[55] = new ModelRenderer(this, 0, 0);
        this.parts[55].setRotationPoint(-64F, 32F, -48F);
        this.parts[55].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[56] = new ModelRenderer(this, 0, 0);
        this.parts[56].setRotationPoint(-64F, 32F, -32F);
        this.parts[56].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[57] = new ModelRenderer(this, 0, 0);
        this.parts[57].setRotationPoint(-64F, 32F, -16F);
        this.parts[57].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[58] = new ModelRenderer(this, 0, 0);
        this.parts[58].setRotationPoint(-64F, 32F, 0F);
        this.parts[58].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[59] = new ModelRenderer(this, 0, 0);
        this.parts[59].setRotationPoint(-64F, 32F, 16F);
        this.parts[59].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[60] = new ModelRenderer(this, 0, 0);
        this.parts[60].setRotationPoint(-64F, 32F, 32F);
        this.parts[60].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[61] = new ModelRenderer(this, 0, 0);
        this.parts[61].setRotationPoint(-64F, 32F, 48F);
        this.parts[61].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[62] = new ModelRenderer(this, 0, 0);
        this.parts[62].setRotationPoint(-64F, 32F, 64F);
        this.parts[62].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[63] = new ModelRenderer(this, 0, 0);
        this.parts[63].setRotationPoint(-64F, 48F, -64F);
        this.parts[63].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[64] = new ModelRenderer(this, 0, 0);
        this.parts[64].setRotationPoint(-64F, 48F, -48F);
        this.parts[64].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[65] = new ModelRenderer(this, 0, 0);
        this.parts[65].setRotationPoint(-64F, 48F, -32F);
        this.parts[65].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[66] = new ModelRenderer(this, 0, 0);
        this.parts[66].setRotationPoint(-64F, 48F, -16F);
        this.parts[66].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[67] = new ModelRenderer(this, 0, 0);
        this.parts[67].setRotationPoint(-64F, 48F, 0F);
        this.parts[67].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[68] = new ModelRenderer(this, 0, 0);
        this.parts[68].setRotationPoint(-64F, 48F, 16F);
        this.parts[68].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[69] = new ModelRenderer(this, 0, 0);
        this.parts[69].setRotationPoint(-64F, 48F, 32F);
        this.parts[69].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[70] = new ModelRenderer(this, 0, 0);
        this.parts[70].setRotationPoint(-64F, 48F, 48F);
        this.parts[70].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[71] = new ModelRenderer(this, 0, 0);
        this.parts[71].setRotationPoint(-64F, 48F, 64F);
        this.parts[71].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[72] = new ModelRenderer(this, 0, 0);
        this.parts[72].setRotationPoint(-64F, 64F, -64F);
        this.parts[72].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[73] = new ModelRenderer(this, 0, 0);
        this.parts[73].setRotationPoint(-64F, 64F, -48F);
        this.parts[73].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[74] = new ModelRenderer(this, 0, 0);
        this.parts[74].setRotationPoint(-64F, 64F, -32F);
        this.parts[74].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[75] = new ModelRenderer(this, 0, 0);
        this.parts[75].setRotationPoint(-64F, 64F, -16F);
        this.parts[75].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[76] = new ModelRenderer(this, 0, 0);
        this.parts[76].setRotationPoint(-64F, 64F, 0F);
        this.parts[76].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[77] = new ModelRenderer(this, 0, 0);
        this.parts[77].setRotationPoint(-64F, 64F, 16F);
        this.parts[77].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[78] = new ModelRenderer(this, 0, 0);
        this.parts[78].setRotationPoint(-64F, 64F, 32F);
        this.parts[78].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[79] = new ModelRenderer(this, 0, 0);
        this.parts[79].setRotationPoint(-64F, 64F, 48F);
        this.parts[79].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[80] = new ModelRenderer(this, 0, 0);
        this.parts[80].setRotationPoint(-64F, 64F, 64F);
        this.parts[80].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[81] = new ModelRenderer(this, 0, 0);
        this.parts[81].setRotationPoint(-48F, -64F, -64F);
        this.parts[81].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[82] = new ModelRenderer(this, 0, 0);
        this.parts[82].setRotationPoint(-48F, -64F, -48F);
        this.parts[82].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[83] = new ModelRenderer(this, 0, 0);
        this.parts[83].setRotationPoint(-48F, -64F, -32F);
        this.parts[83].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[84] = new ModelRenderer(this, 0, 0);
        this.parts[84].setRotationPoint(-48F, -64F, -16F);
        this.parts[84].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[85] = new ModelRenderer(this, 0, 0);
        this.parts[85].setRotationPoint(-48F, -64F, 0F);
        this.parts[85].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[86] = new ModelRenderer(this, 0, 0);
        this.parts[86].setRotationPoint(-48F, -64F, 16F);
        this.parts[86].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[87] = new ModelRenderer(this, 0, 0);
        this.parts[87].setRotationPoint(-48F, -64F, 32F);
        this.parts[87].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[88] = new ModelRenderer(this, 0, 0);
        this.parts[88].setRotationPoint(-48F, -64F, 48F);
        this.parts[88].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[89] = new ModelRenderer(this, 0, 0);
        this.parts[89].setRotationPoint(-48F, -64F, 64F);
        this.parts[89].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[90] = new ModelRenderer(this, 0, 0);
        this.parts[90].setRotationPoint(-48F, -48F, -64F);
        this.parts[90].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[91] = new ModelRenderer(this, 0, 0);
        this.parts[91].setRotationPoint(-48F, -48F, -48F);
        this.parts[91].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[92] = new ModelRenderer(this, 0, 0);
        this.parts[92].setRotationPoint(-48F, -48F, -32F);
        this.parts[92].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[93] = new ModelRenderer(this, 0, 0);
        this.parts[93].setRotationPoint(-48F, -48F, -16F);
        this.parts[93].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[94] = new ModelRenderer(this, 0, 0);
        this.parts[94].setRotationPoint(-48F, -48F, 0F);
        this.parts[94].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[95] = new ModelRenderer(this, 0, 0);
        this.parts[95].setRotationPoint(-48F, -48F, 16F);
        this.parts[95].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[96] = new ModelRenderer(this, 0, 0);
        this.parts[96].setRotationPoint(-48F, -48F, 32F);
        this.parts[96].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[97] = new ModelRenderer(this, 0, 0);
        this.parts[97].setRotationPoint(-48F, -48F, 48F);
        this.parts[97].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[98] = new ModelRenderer(this, 0, 0);
        this.parts[98].setRotationPoint(-48F, -48F, 64F);
        this.parts[98].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[99] = new ModelRenderer(this, 0, 0);
        this.parts[99].setRotationPoint(-48F, -32F, -64F);
        this.parts[99].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[100] = new ModelRenderer(this, 0, 0);
        this.parts[100].setRotationPoint(-48F, -32F, -48F);
        this.parts[100].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[101] = new ModelRenderer(this, 0, 0);
        this.parts[101].setRotationPoint(-48F, -32F, -32F);
        this.parts[101].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[102] = new ModelRenderer(this, 0, 0);
        this.parts[102].setRotationPoint(-48F, -32F, -16F);
        this.parts[102].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[103] = new ModelRenderer(this, 0, 0);
        this.parts[103].setRotationPoint(-48F, -32F, 0F);
        this.parts[103].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[104] = new ModelRenderer(this, 0, 0);
        this.parts[104].setRotationPoint(-48F, -32F, 16F);
        this.parts[104].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[105] = new ModelRenderer(this, 0, 0);
        this.parts[105].setRotationPoint(-48F, -32F, 32F);
        this.parts[105].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[106] = new ModelRenderer(this, 0, 0);
        this.parts[106].setRotationPoint(-48F, -32F, 48F);
        this.parts[106].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[107] = new ModelRenderer(this, 0, 0);
        this.parts[107].setRotationPoint(-48F, -32F, 64F);
        this.parts[107].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[108] = new ModelRenderer(this, 0, 0);
        this.parts[108].setRotationPoint(-48F, -16F, -64F);
        this.parts[108].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[109] = new ModelRenderer(this, 0, 0);
        this.parts[109].setRotationPoint(-48F, -16F, -48F);
        this.parts[109].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[110] = new ModelRenderer(this, 0, 0);
        this.parts[110].setRotationPoint(-48F, -16F, -32F);
        this.parts[110].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[111] = new ModelRenderer(this, 0, 0);
        this.parts[111].setRotationPoint(-48F, -16F, -16F);
        this.parts[111].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[112] = new ModelRenderer(this, 0, 0);
        this.parts[112].setRotationPoint(-48F, -16F, 0F);
        this.parts[112].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[113] = new ModelRenderer(this, 0, 0);
        this.parts[113].setRotationPoint(-48F, -16F, 16F);
        this.parts[113].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[114] = new ModelRenderer(this, 0, 0);
        this.parts[114].setRotationPoint(-48F, -16F, 32F);
        this.parts[114].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[115] = new ModelRenderer(this, 0, 0);
        this.parts[115].setRotationPoint(-48F, -16F, 48F);
        this.parts[115].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[116] = new ModelRenderer(this, 0, 0);
        this.parts[116].setRotationPoint(-48F, -16F, 64F);
        this.parts[116].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[117] = new ModelRenderer(this, 0, 0);
        this.parts[117].setRotationPoint(-48F, 0F, -64F);
        this.parts[117].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[118] = new ModelRenderer(this, 0, 0);
        this.parts[118].setRotationPoint(-48F, 0F, -48F);
        this.parts[118].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[119] = new ModelRenderer(this, 0, 0);
        this.parts[119].setRotationPoint(-48F, 0F, -32F);
        this.parts[119].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[120] = new ModelRenderer(this, 0, 0);
        this.parts[120].setRotationPoint(-48F, 0F, -16F);
        this.parts[120].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[121] = new ModelRenderer(this, 0, 0);
        this.parts[121].setRotationPoint(-48F, 0F, 0F);
        this.parts[121].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[122] = new ModelRenderer(this, 0, 0);
        this.parts[122].setRotationPoint(-48F, 0F, 16F);
        this.parts[122].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[123] = new ModelRenderer(this, 0, 0);
        this.parts[123].setRotationPoint(-48F, 0F, 32F);
        this.parts[123].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[124] = new ModelRenderer(this, 0, 0);
        this.parts[124].setRotationPoint(-48F, 0F, 48F);
        this.parts[124].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[125] = new ModelRenderer(this, 0, 0);
        this.parts[125].setRotationPoint(-48F, 0F, 64F);
        this.parts[125].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[126] = new ModelRenderer(this, 0, 0);
        this.parts[126].setRotationPoint(-48F, 16F, -64F);
        this.parts[126].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[127] = new ModelRenderer(this, 0, 0);
        this.parts[127].setRotationPoint(-48F, 16F, -48F);
        this.parts[127].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[128] = new ModelRenderer(this, 0, 0);
        this.parts[128].setRotationPoint(-48F, 16F, -32F);
        this.parts[128].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[129] = new ModelRenderer(this, 0, 0);
        this.parts[129].setRotationPoint(-48F, 16F, -16F);
        this.parts[129].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[130] = new ModelRenderer(this, 0, 0);
        this.parts[130].setRotationPoint(-48F, 16F, 0F);
        this.parts[130].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[131] = new ModelRenderer(this, 0, 0);
        this.parts[131].setRotationPoint(-48F, 16F, 16F);
        this.parts[131].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[132] = new ModelRenderer(this, 0, 0);
        this.parts[132].setRotationPoint(-48F, 16F, 32F);
        this.parts[132].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[133] = new ModelRenderer(this, 0, 0);
        this.parts[133].setRotationPoint(-48F, 16F, 48F);
        this.parts[133].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[134] = new ModelRenderer(this, 0, 0);
        this.parts[134].setRotationPoint(-48F, 16F, 64F);
        this.parts[134].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[135] = new ModelRenderer(this, 0, 0);
        this.parts[135].setRotationPoint(-48F, 32F, -64F);
        this.parts[135].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[136] = new ModelRenderer(this, 0, 0);
        this.parts[136].setRotationPoint(-48F, 32F, -48F);
        this.parts[136].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[137] = new ModelRenderer(this, 0, 0);
        this.parts[137].setRotationPoint(-48F, 32F, -32F);
        this.parts[137].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[138] = new ModelRenderer(this, 0, 0);
        this.parts[138].setRotationPoint(-48F, 32F, -16F);
        this.parts[138].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[139] = new ModelRenderer(this, 0, 0);
        this.parts[139].setRotationPoint(-48F, 32F, 0F);
        this.parts[139].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[140] = new ModelRenderer(this, 0, 0);
        this.parts[140].setRotationPoint(-48F, 32F, 16F);
        this.parts[140].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[141] = new ModelRenderer(this, 0, 0);
        this.parts[141].setRotationPoint(-48F, 32F, 32F);
        this.parts[141].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[142] = new ModelRenderer(this, 0, 0);
        this.parts[142].setRotationPoint(-48F, 32F, 48F);
        this.parts[142].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[143] = new ModelRenderer(this, 0, 0);
        this.parts[143].setRotationPoint(-48F, 32F, 64F);
        this.parts[143].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[144] = new ModelRenderer(this, 0, 0);
        this.parts[144].setRotationPoint(-48F, 48F, -64F);
        this.parts[144].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[145] = new ModelRenderer(this, 0, 0);
        this.parts[145].setRotationPoint(-48F, 48F, -48F);
        this.parts[145].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[146] = new ModelRenderer(this, 0, 0);
        this.parts[146].setRotationPoint(-48F, 48F, -32F);
        this.parts[146].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[147] = new ModelRenderer(this, 0, 0);
        this.parts[147].setRotationPoint(-48F, 48F, -16F);
        this.parts[147].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[148] = new ModelRenderer(this, 0, 0);
        this.parts[148].setRotationPoint(-48F, 48F, 0F);
        this.parts[148].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[149] = new ModelRenderer(this, 0, 0);
        this.parts[149].setRotationPoint(-48F, 48F, 16F);
        this.parts[149].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[150] = new ModelRenderer(this, 0, 0);
        this.parts[150].setRotationPoint(-48F, 48F, 32F);
        this.parts[150].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[151] = new ModelRenderer(this, 0, 0);
        this.parts[151].setRotationPoint(-48F, 48F, 48F);
        this.parts[151].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[152] = new ModelRenderer(this, 0, 0);
        this.parts[152].setRotationPoint(-48F, 48F, 64F);
        this.parts[152].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[153] = new ModelRenderer(this, 0, 0);
        this.parts[153].setRotationPoint(-48F, 64F, -64F);
        this.parts[153].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[154] = new ModelRenderer(this, 0, 0);
        this.parts[154].setRotationPoint(-48F, 64F, -48F);
        this.parts[154].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[155] = new ModelRenderer(this, 0, 0);
        this.parts[155].setRotationPoint(-48F, 64F, -32F);
        this.parts[155].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[156] = new ModelRenderer(this, 0, 0);
        this.parts[156].setRotationPoint(-48F, 64F, -16F);
        this.parts[156].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[157] = new ModelRenderer(this, 0, 0);
        this.parts[157].setRotationPoint(-48F, 64F, 0F);
        this.parts[157].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[158] = new ModelRenderer(this, 0, 0);
        this.parts[158].setRotationPoint(-48F, 64F, 16F);
        this.parts[158].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[159] = new ModelRenderer(this, 0, 0);
        this.parts[159].setRotationPoint(-48F, 64F, 32F);
        this.parts[159].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[160] = new ModelRenderer(this, 0, 0);
        this.parts[160].setRotationPoint(-48F, 64F, 48F);
        this.parts[160].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[161] = new ModelRenderer(this, 0, 0);
        this.parts[161].setRotationPoint(-48F, 64F, 64F);
        this.parts[161].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[162] = new ModelRenderer(this, 0, 0);
        this.parts[162].setRotationPoint(-32F, -64F, -64F);
        this.parts[162].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[163] = new ModelRenderer(this, 0, 0);
        this.parts[163].setRotationPoint(-32F, -64F, -48F);
        this.parts[163].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[164] = new ModelRenderer(this, 0, 0);
        this.parts[164].setRotationPoint(-32F, -64F, -32F);
        this.parts[164].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[165] = new ModelRenderer(this, 0, 0);
        this.parts[165].setRotationPoint(-32F, -64F, -16F);
        this.parts[165].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[166] = new ModelRenderer(this, 0, 0);
        this.parts[166].setRotationPoint(-32F, -64F, 0F);
        this.parts[166].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[167] = new ModelRenderer(this, 0, 0);
        this.parts[167].setRotationPoint(-32F, -64F, 16F);
        this.parts[167].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[168] = new ModelRenderer(this, 0, 0);
        this.parts[168].setRotationPoint(-32F, -64F, 32F);
        this.parts[168].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[169] = new ModelRenderer(this, 0, 0);
        this.parts[169].setRotationPoint(-32F, -64F, 48F);
        this.parts[169].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[170] = new ModelRenderer(this, 0, 0);
        this.parts[170].setRotationPoint(-32F, -64F, 64F);
        this.parts[170].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[171] = new ModelRenderer(this, 0, 0);
        this.parts[171].setRotationPoint(-32F, -48F, -64F);
        this.parts[171].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[172] = new ModelRenderer(this, 0, 0);
        this.parts[172].setRotationPoint(-32F, -48F, -48F);
        this.parts[172].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[173] = new ModelRenderer(this, 0, 0);
        this.parts[173].setRotationPoint(-32F, -48F, -32F);
        this.parts[173].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[174] = new ModelRenderer(this, 0, 0);
        this.parts[174].setRotationPoint(-32F, -48F, -16F);
        this.parts[174].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[175] = new ModelRenderer(this, 0, 0);
        this.parts[175].setRotationPoint(-32F, -48F, 0F);
        this.parts[175].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[176] = new ModelRenderer(this, 0, 0);
        this.parts[176].setRotationPoint(-32F, -48F, 16F);
        this.parts[176].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[177] = new ModelRenderer(this, 0, 0);
        this.parts[177].setRotationPoint(-32F, -48F, 32F);
        this.parts[177].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[178] = new ModelRenderer(this, 0, 0);
        this.parts[178].setRotationPoint(-32F, -48F, 48F);
        this.parts[178].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[179] = new ModelRenderer(this, 0, 0);
        this.parts[179].setRotationPoint(-32F, -48F, 64F);
        this.parts[179].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[180] = new ModelRenderer(this, 0, 0);
        this.parts[180].setRotationPoint(-32F, -32F, -64F);
        this.parts[180].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[181] = new ModelRenderer(this, 0, 0);
        this.parts[181].setRotationPoint(-32F, -32F, -48F);
        this.parts[181].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[182] = new ModelRenderer(this, 0, 0);
        this.parts[182].setRotationPoint(-32F, -32F, -32F);
        this.parts[182].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[183] = new ModelRenderer(this, 0, 0);
        this.parts[183].setRotationPoint(-32F, -32F, -16F);
        this.parts[183].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[184] = new ModelRenderer(this, 0, 0);
        this.parts[184].setRotationPoint(-32F, -32F, 0F);
        this.parts[184].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[185] = new ModelRenderer(this, 0, 0);
        this.parts[185].setRotationPoint(-32F, -32F, 16F);
        this.parts[185].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[186] = new ModelRenderer(this, 0, 0);
        this.parts[186].setRotationPoint(-32F, -32F, 32F);
        this.parts[186].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[187] = new ModelRenderer(this, 0, 0);
        this.parts[187].setRotationPoint(-32F, -32F, 48F);
        this.parts[187].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[188] = new ModelRenderer(this, 0, 0);
        this.parts[188].setRotationPoint(-32F, -32F, 64F);
        this.parts[188].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[189] = new ModelRenderer(this, 0, 0);
        this.parts[189].setRotationPoint(-32F, -16F, -64F);
        this.parts[189].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[190] = new ModelRenderer(this, 0, 0);
        this.parts[190].setRotationPoint(-32F, -16F, -48F);
        this.parts[190].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[191] = new ModelRenderer(this, 0, 0);
        this.parts[191].setRotationPoint(-32F, -16F, -32F);
        this.parts[191].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[192] = new ModelRenderer(this, 0, 0);
        this.parts[192].setRotationPoint(-32F, -16F, -16F);
        this.parts[192].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[193] = new ModelRenderer(this, 0, 0);
        this.parts[193].setRotationPoint(-32F, -16F, 0F);
        this.parts[193].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[194] = new ModelRenderer(this, 0, 0);
        this.parts[194].setRotationPoint(-32F, -16F, 16F);
        this.parts[194].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[195] = new ModelRenderer(this, 0, 0);
        this.parts[195].setRotationPoint(-32F, -16F, 32F);
        this.parts[195].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[196] = new ModelRenderer(this, 0, 0);
        this.parts[196].setRotationPoint(-32F, -16F, 48F);
        this.parts[196].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[197] = new ModelRenderer(this, 0, 0);
        this.parts[197].setRotationPoint(-32F, -16F, 64F);
        this.parts[197].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[198] = new ModelRenderer(this, 0, 0);
        this.parts[198].setRotationPoint(-32F, 0F, -64F);
        this.parts[198].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[199] = new ModelRenderer(this, 0, 0);
        this.parts[199].setRotationPoint(-32F, 0F, -48F);
        this.parts[199].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[200] = new ModelRenderer(this, 0, 0);
        this.parts[200].setRotationPoint(-32F, 0F, -32F);
        this.parts[200].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[201] = new ModelRenderer(this, 0, 0);
        this.parts[201].setRotationPoint(-32F, 0F, -16F);
        this.parts[201].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[202] = new ModelRenderer(this, 0, 0);
        this.parts[202].setRotationPoint(-32F, 0F, 0F);
        this.parts[202].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[203] = new ModelRenderer(this, 0, 0);
        this.parts[203].setRotationPoint(-32F, 0F, 16F);
        this.parts[203].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[204] = new ModelRenderer(this, 0, 0);
        this.parts[204].setRotationPoint(-32F, 0F, 32F);
        this.parts[204].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[205] = new ModelRenderer(this, 0, 0);
        this.parts[205].setRotationPoint(-32F, 0F, 48F);
        this.parts[205].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[206] = new ModelRenderer(this, 0, 0);
        this.parts[206].setRotationPoint(-32F, 0F, 64F);
        this.parts[206].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[207] = new ModelRenderer(this, 0, 0);
        this.parts[207].setRotationPoint(-32F, 16F, -64F);
        this.parts[207].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[208] = new ModelRenderer(this, 0, 0);
        this.parts[208].setRotationPoint(-32F, 16F, -48F);
        this.parts[208].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[209] = new ModelRenderer(this, 0, 0);
        this.parts[209].setRotationPoint(-32F, 16F, -32F);
        this.parts[209].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[210] = new ModelRenderer(this, 0, 0);
        this.parts[210].setRotationPoint(-32F, 16F, -16F);
        this.parts[210].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[211] = new ModelRenderer(this, 0, 0);
        this.parts[211].setRotationPoint(-32F, 16F, 0F);
        this.parts[211].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[212] = new ModelRenderer(this, 0, 0);
        this.parts[212].setRotationPoint(-32F, 16F, 16F);
        this.parts[212].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[213] = new ModelRenderer(this, 0, 0);
        this.parts[213].setRotationPoint(-32F, 16F, 32F);
        this.parts[213].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[214] = new ModelRenderer(this, 0, 0);
        this.parts[214].setRotationPoint(-32F, 16F, 48F);
        this.parts[214].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[215] = new ModelRenderer(this, 0, 0);
        this.parts[215].setRotationPoint(-32F, 16F, 64F);
        this.parts[215].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[216] = new ModelRenderer(this, 0, 0);
        this.parts[216].setRotationPoint(-32F, 32F, -64F);
        this.parts[216].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[217] = new ModelRenderer(this, 0, 0);
        this.parts[217].setRotationPoint(-32F, 32F, -48F);
        this.parts[217].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[218] = new ModelRenderer(this, 0, 0);
        this.parts[218].setRotationPoint(-32F, 32F, -32F);
        this.parts[218].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[219] = new ModelRenderer(this, 0, 0);
        this.parts[219].setRotationPoint(-32F, 32F, -16F);
        this.parts[219].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[220] = new ModelRenderer(this, 0, 0);
        this.parts[220].setRotationPoint(-32F, 32F, 0F);
        this.parts[220].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[221] = new ModelRenderer(this, 0, 0);
        this.parts[221].setRotationPoint(-32F, 32F, 16F);
        this.parts[221].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[222] = new ModelRenderer(this, 0, 0);
        this.parts[222].setRotationPoint(-32F, 32F, 32F);
        this.parts[222].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[223] = new ModelRenderer(this, 0, 0);
        this.parts[223].setRotationPoint(-32F, 32F, 48F);
        this.parts[223].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[224] = new ModelRenderer(this, 0, 0);
        this.parts[224].setRotationPoint(-32F, 32F, 64F);
        this.parts[224].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[225] = new ModelRenderer(this, 0, 0);
        this.parts[225].setRotationPoint(-32F, 48F, -64F);
        this.parts[225].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[226] = new ModelRenderer(this, 0, 0);
        this.parts[226].setRotationPoint(-32F, 48F, -48F);
        this.parts[226].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[227] = new ModelRenderer(this, 0, 0);
        this.parts[227].setRotationPoint(-32F, 48F, -32F);
        this.parts[227].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[228] = new ModelRenderer(this, 0, 0);
        this.parts[228].setRotationPoint(-32F, 48F, -16F);
        this.parts[228].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[229] = new ModelRenderer(this, 0, 0);
        this.parts[229].setRotationPoint(-32F, 48F, 0F);
        this.parts[229].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[230] = new ModelRenderer(this, 0, 0);
        this.parts[230].setRotationPoint(-32F, 48F, 16F);
        this.parts[230].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[231] = new ModelRenderer(this, 0, 0);
        this.parts[231].setRotationPoint(-32F, 48F, 32F);
        this.parts[231].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[232] = new ModelRenderer(this, 0, 0);
        this.parts[232].setRotationPoint(-32F, 48F, 48F);
        this.parts[232].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[233] = new ModelRenderer(this, 0, 0);
        this.parts[233].setRotationPoint(-32F, 48F, 64F);
        this.parts[233].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[234] = new ModelRenderer(this, 0, 0);
        this.parts[234].setRotationPoint(-32F, 64F, -64F);
        this.parts[234].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[235] = new ModelRenderer(this, 0, 0);
        this.parts[235].setRotationPoint(-32F, 64F, -48F);
        this.parts[235].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[236] = new ModelRenderer(this, 0, 0);
        this.parts[236].setRotationPoint(-32F, 64F, -32F);
        this.parts[236].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[237] = new ModelRenderer(this, 0, 0);
        this.parts[237].setRotationPoint(-32F, 64F, -16F);
        this.parts[237].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[238] = new ModelRenderer(this, 0, 0);
        this.parts[238].setRotationPoint(-32F, 64F, 0F);
        this.parts[238].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[239] = new ModelRenderer(this, 0, 0);
        this.parts[239].setRotationPoint(-32F, 64F, 16F);
        this.parts[239].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[240] = new ModelRenderer(this, 0, 0);
        this.parts[240].setRotationPoint(-32F, 64F, 32F);
        this.parts[240].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[241] = new ModelRenderer(this, 0, 0);
        this.parts[241].setRotationPoint(-32F, 64F, 48F);
        this.parts[241].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[242] = new ModelRenderer(this, 0, 0);
        this.parts[242].setRotationPoint(-32F, 64F, 64F);
        this.parts[242].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[243] = new ModelRenderer(this, 0, 0);
        this.parts[243].setRotationPoint(-16F, -64F, -64F);
        this.parts[243].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[244] = new ModelRenderer(this, 0, 0);
        this.parts[244].setRotationPoint(-16F, -64F, -48F);
        this.parts[244].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[245] = new ModelRenderer(this, 0, 0);
        this.parts[245].setRotationPoint(-16F, -64F, -32F);
        this.parts[245].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[246] = new ModelRenderer(this, 0, 0);
        this.parts[246].setRotationPoint(-16F, -64F, -16F);
        this.parts[246].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[247] = new ModelRenderer(this, 0, 0);
        this.parts[247].setRotationPoint(-16F, -64F, 0F);
        this.parts[247].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[248] = new ModelRenderer(this, 0, 0);
        this.parts[248].setRotationPoint(-16F, -64F, 16F);
        this.parts[248].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[249] = new ModelRenderer(this, 0, 0);
        this.parts[249].setRotationPoint(-16F, -64F, 32F);
        this.parts[249].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[250] = new ModelRenderer(this, 0, 0);
        this.parts[250].setRotationPoint(-16F, -64F, 48F);
        this.parts[250].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[251] = new ModelRenderer(this, 0, 0);
        this.parts[251].setRotationPoint(-16F, -64F, 64F);
        this.parts[251].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[252] = new ModelRenderer(this, 0, 0);
        this.parts[252].setRotationPoint(-16F, -48F, -64F);
        this.parts[252].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[253] = new ModelRenderer(this, 0, 0);
        this.parts[253].setRotationPoint(-16F, -48F, -48F);
        this.parts[253].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[254] = new ModelRenderer(this, 0, 0);
        this.parts[254].setRotationPoint(-16F, -48F, -32F);
        this.parts[254].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[255] = new ModelRenderer(this, 0, 0);
        this.parts[255].setRotationPoint(-16F, -48F, -16F);
        this.parts[255].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[256] = new ModelRenderer(this, 0, 0);
        this.parts[256].setRotationPoint(-16F, -48F, 0F);
        this.parts[256].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[257] = new ModelRenderer(this, 0, 0);
        this.parts[257].setRotationPoint(-16F, -48F, 16F);
        this.parts[257].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[258] = new ModelRenderer(this, 0, 0);
        this.parts[258].setRotationPoint(-16F, -48F, 32F);
        this.parts[258].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[259] = new ModelRenderer(this, 0, 0);
        this.parts[259].setRotationPoint(-16F, -48F, 48F);
        this.parts[259].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[260] = new ModelRenderer(this, 0, 0);
        this.parts[260].setRotationPoint(-16F, -48F, 64F);
        this.parts[260].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[261] = new ModelRenderer(this, 0, 0);
        this.parts[261].setRotationPoint(-16F, -32F, -64F);
        this.parts[261].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[262] = new ModelRenderer(this, 0, 0);
        this.parts[262].setRotationPoint(-16F, -32F, -48F);
        this.parts[262].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[263] = new ModelRenderer(this, 0, 0);
        this.parts[263].setRotationPoint(-16F, -32F, -32F);
        this.parts[263].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[264] = new ModelRenderer(this, 0, 0);
        this.parts[264].setRotationPoint(-16F, -32F, -16F);
        this.parts[264].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[265] = new ModelRenderer(this, 0, 0);
        this.parts[265].setRotationPoint(-16F, -32F, 0F);
        this.parts[265].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[266] = new ModelRenderer(this, 0, 0);
        this.parts[266].setRotationPoint(-16F, -32F, 16F);
        this.parts[266].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[267] = new ModelRenderer(this, 0, 0);
        this.parts[267].setRotationPoint(-16F, -32F, 32F);
        this.parts[267].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[268] = new ModelRenderer(this, 0, 0);
        this.parts[268].setRotationPoint(-16F, -32F, 48F);
        this.parts[268].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[269] = new ModelRenderer(this, 0, 0);
        this.parts[269].setRotationPoint(-16F, -32F, 64F);
        this.parts[269].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[270] = new ModelRenderer(this, 0, 0);
        this.parts[270].setRotationPoint(-16F, -16F, -64F);
        this.parts[270].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[271] = new ModelRenderer(this, 0, 0);
        this.parts[271].setRotationPoint(-16F, -16F, -48F);
        this.parts[271].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[272] = new ModelRenderer(this, 0, 0);
        this.parts[272].setRotationPoint(-16F, -16F, -32F);
        this.parts[272].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[273] = new ModelRenderer(this, 0, 0);
        this.parts[273].setRotationPoint(-16F, -16F, -16F);
        this.parts[273].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[274] = new ModelRenderer(this, 0, 0);
        this.parts[274].setRotationPoint(-16F, -16F, 0F);
        this.parts[274].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[275] = new ModelRenderer(this, 0, 0);
        this.parts[275].setRotationPoint(-16F, -16F, 16F);
        this.parts[275].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[276] = new ModelRenderer(this, 0, 0);
        this.parts[276].setRotationPoint(-16F, -16F, 32F);
        this.parts[276].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[277] = new ModelRenderer(this, 0, 0);
        this.parts[277].setRotationPoint(-16F, -16F, 48F);
        this.parts[277].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[278] = new ModelRenderer(this, 0, 0);
        this.parts[278].setRotationPoint(-16F, -16F, 64F);
        this.parts[278].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[279] = new ModelRenderer(this, 0, 0);
        this.parts[279].setRotationPoint(-16F, 0F, -64F);
        this.parts[279].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[280] = new ModelRenderer(this, 0, 0);
        this.parts[280].setRotationPoint(-16F, 0F, -48F);
        this.parts[280].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[281] = new ModelRenderer(this, 0, 0);
        this.parts[281].setRotationPoint(-16F, 0F, -32F);
        this.parts[281].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[282] = new ModelRenderer(this, 0, 0);
        this.parts[282].setRotationPoint(-16F, 0F, -16F);
        this.parts[282].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[283] = new ModelRenderer(this, 0, 0);
        this.parts[283].setRotationPoint(-16F, 0F, 0F);
        this.parts[283].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[284] = new ModelRenderer(this, 0, 0);
        this.parts[284].setRotationPoint(-16F, 0F, 16F);
        this.parts[284].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[285] = new ModelRenderer(this, 0, 0);
        this.parts[285].setRotationPoint(-16F, 0F, 32F);
        this.parts[285].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[286] = new ModelRenderer(this, 0, 0);
        this.parts[286].setRotationPoint(-16F, 0F, 48F);
        this.parts[286].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[287] = new ModelRenderer(this, 0, 0);
        this.parts[287].setRotationPoint(-16F, 0F, 64F);
        this.parts[287].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[288] = new ModelRenderer(this, 0, 0);
        this.parts[288].setRotationPoint(-16F, 16F, -64F);
        this.parts[288].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[289] = new ModelRenderer(this, 0, 0);
        this.parts[289].setRotationPoint(-16F, 16F, -48F);
        this.parts[289].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[290] = new ModelRenderer(this, 0, 0);
        this.parts[290].setRotationPoint(-16F, 16F, -32F);
        this.parts[290].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[291] = new ModelRenderer(this, 0, 0);
        this.parts[291].setRotationPoint(-16F, 16F, -16F);
        this.parts[291].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[292] = new ModelRenderer(this, 0, 0);
        this.parts[292].setRotationPoint(-16F, 16F, 0F);
        this.parts[292].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[293] = new ModelRenderer(this, 0, 0);
        this.parts[293].setRotationPoint(-16F, 16F, 16F);
        this.parts[293].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[294] = new ModelRenderer(this, 0, 0);
        this.parts[294].setRotationPoint(-16F, 16F, 32F);
        this.parts[294].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[295] = new ModelRenderer(this, 0, 0);
        this.parts[295].setRotationPoint(-16F, 16F, 48F);
        this.parts[295].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[296] = new ModelRenderer(this, 0, 0);
        this.parts[296].setRotationPoint(-16F, 16F, 64F);
        this.parts[296].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[297] = new ModelRenderer(this, 0, 0);
        this.parts[297].setRotationPoint(-16F, 32F, -64F);
        this.parts[297].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[298] = new ModelRenderer(this, 0, 0);
        this.parts[298].setRotationPoint(-16F, 32F, -48F);
        this.parts[298].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[299] = new ModelRenderer(this, 0, 0);
        this.parts[299].setRotationPoint(-16F, 32F, -32F);
        this.parts[299].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[300] = new ModelRenderer(this, 0, 0);
        this.parts[300].setRotationPoint(-16F, 32F, -16F);
        this.parts[300].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[301] = new ModelRenderer(this, 0, 0);
        this.parts[301].setRotationPoint(-16F, 32F, 0F);
        this.parts[301].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[302] = new ModelRenderer(this, 0, 0);
        this.parts[302].setRotationPoint(-16F, 32F, 16F);
        this.parts[302].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[303] = new ModelRenderer(this, 0, 0);
        this.parts[303].setRotationPoint(-16F, 32F, 32F);
        this.parts[303].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[304] = new ModelRenderer(this, 0, 0);
        this.parts[304].setRotationPoint(-16F, 32F, 48F);
        this.parts[304].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[305] = new ModelRenderer(this, 0, 0);
        this.parts[305].setRotationPoint(-16F, 32F, 64F);
        this.parts[305].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[306] = new ModelRenderer(this, 0, 0);
        this.parts[306].setRotationPoint(-16F, 48F, -64F);
        this.parts[306].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[307] = new ModelRenderer(this, 0, 0);
        this.parts[307].setRotationPoint(-16F, 48F, -48F);
        this.parts[307].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[308] = new ModelRenderer(this, 0, 0);
        this.parts[308].setRotationPoint(-16F, 48F, -32F);
        this.parts[308].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[309] = new ModelRenderer(this, 0, 0);
        this.parts[309].setRotationPoint(-16F, 48F, -16F);
        this.parts[309].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[310] = new ModelRenderer(this, 0, 0);
        this.parts[310].setRotationPoint(-16F, 48F, 0F);
        this.parts[310].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[311] = new ModelRenderer(this, 0, 0);
        this.parts[311].setRotationPoint(-16F, 48F, 16F);
        this.parts[311].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[312] = new ModelRenderer(this, 0, 0);
        this.parts[312].setRotationPoint(-16F, 48F, 32F);
        this.parts[312].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[313] = new ModelRenderer(this, 0, 0);
        this.parts[313].setRotationPoint(-16F, 48F, 48F);
        this.parts[313].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[314] = new ModelRenderer(this, 0, 0);
        this.parts[314].setRotationPoint(-16F, 48F, 64F);
        this.parts[314].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[315] = new ModelRenderer(this, 0, 0);
        this.parts[315].setRotationPoint(-16F, 64F, -64F);
        this.parts[315].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[316] = new ModelRenderer(this, 0, 0);
        this.parts[316].setRotationPoint(-16F, 64F, -48F);
        this.parts[316].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[317] = new ModelRenderer(this, 0, 0);
        this.parts[317].setRotationPoint(-16F, 64F, -32F);
        this.parts[317].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[318] = new ModelRenderer(this, 0, 0);
        this.parts[318].setRotationPoint(-16F, 64F, -16F);
        this.parts[318].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[319] = new ModelRenderer(this, 0, 0);
        this.parts[319].setRotationPoint(-16F, 64F, 0F);
        this.parts[319].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[320] = new ModelRenderer(this, 0, 0);
        this.parts[320].setRotationPoint(-16F, 64F, 16F);
        this.parts[320].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[321] = new ModelRenderer(this, 0, 0);
        this.parts[321].setRotationPoint(-16F, 64F, 32F);
        this.parts[321].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[322] = new ModelRenderer(this, 0, 0);
        this.parts[322].setRotationPoint(-16F, 64F, 48F);
        this.parts[322].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[323] = new ModelRenderer(this, 0, 0);
        this.parts[323].setRotationPoint(-16F, 64F, 64F);
        this.parts[323].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[324] = new ModelRenderer(this, 0, 0);
        this.parts[324].setRotationPoint(0F, -64F, -64F);
        this.parts[324].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[325] = new ModelRenderer(this, 0, 0);
        this.parts[325].setRotationPoint(0F, -64F, -48F);
        this.parts[325].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[326] = new ModelRenderer(this, 0, 0);
        this.parts[326].setRotationPoint(0F, -64F, -32F);
        this.parts[326].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[327] = new ModelRenderer(this, 0, 0);
        this.parts[327].setRotationPoint(0F, -64F, -16F);
        this.parts[327].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[328] = new ModelRenderer(this, 0, 0);
        this.parts[328].setRotationPoint(0F, -64F, 0F);
        this.parts[328].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[329] = new ModelRenderer(this, 0, 0);
        this.parts[329].setRotationPoint(0F, -64F, 16F);
        this.parts[329].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[330] = new ModelRenderer(this, 0, 0);
        this.parts[330].setRotationPoint(0F, -64F, 32F);
        this.parts[330].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[331] = new ModelRenderer(this, 0, 0);
        this.parts[331].setRotationPoint(0F, -64F, 48F);
        this.parts[331].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[332] = new ModelRenderer(this, 0, 0);
        this.parts[332].setRotationPoint(0F, -64F, 64F);
        this.parts[332].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[333] = new ModelRenderer(this, 0, 0);
        this.parts[333].setRotationPoint(0F, -48F, -64F);
        this.parts[333].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[334] = new ModelRenderer(this, 0, 0);
        this.parts[334].setRotationPoint(0F, -48F, -48F);
        this.parts[334].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[335] = new ModelRenderer(this, 0, 0);
        this.parts[335].setRotationPoint(0F, -48F, -32F);
        this.parts[335].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[336] = new ModelRenderer(this, 0, 0);
        this.parts[336].setRotationPoint(0F, -48F, -16F);
        this.parts[336].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[337] = new ModelRenderer(this, 0, 0);
        this.parts[337].setRotationPoint(0F, -48F, 0F);
        this.parts[337].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[338] = new ModelRenderer(this, 0, 0);
        this.parts[338].setRotationPoint(0F, -48F, 16F);
        this.parts[338].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[339] = new ModelRenderer(this, 0, 0);
        this.parts[339].setRotationPoint(0F, -48F, 32F);
        this.parts[339].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[340] = new ModelRenderer(this, 0, 0);
        this.parts[340].setRotationPoint(0F, -48F, 48F);
        this.parts[340].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[341] = new ModelRenderer(this, 0, 0);
        this.parts[341].setRotationPoint(0F, -48F, 64F);
        this.parts[341].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[342] = new ModelRenderer(this, 0, 0);
        this.parts[342].setRotationPoint(0F, -32F, -64F);
        this.parts[342].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[343] = new ModelRenderer(this, 0, 0);
        this.parts[343].setRotationPoint(0F, -32F, -48F);
        this.parts[343].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[344] = new ModelRenderer(this, 0, 0);
        this.parts[344].setRotationPoint(0F, -32F, -32F);
        this.parts[344].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[345] = new ModelRenderer(this, 0, 0);
        this.parts[345].setRotationPoint(0F, -32F, -16F);
        this.parts[345].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[346] = new ModelRenderer(this, 0, 0);
        this.parts[346].setRotationPoint(0F, -32F, 0F);
        this.parts[346].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[347] = new ModelRenderer(this, 0, 0);
        this.parts[347].setRotationPoint(0F, -32F, 16F);
        this.parts[347].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[348] = new ModelRenderer(this, 0, 0);
        this.parts[348].setRotationPoint(0F, -32F, 32F);
        this.parts[348].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[349] = new ModelRenderer(this, 0, 0);
        this.parts[349].setRotationPoint(0F, -32F, 48F);
        this.parts[349].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[350] = new ModelRenderer(this, 0, 0);
        this.parts[350].setRotationPoint(0F, -32F, 64F);
        this.parts[350].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[351] = new ModelRenderer(this, 0, 0);
        this.parts[351].setRotationPoint(0F, -16F, -64F);
        this.parts[351].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[352] = new ModelRenderer(this, 0, 0);
        this.parts[352].setRotationPoint(0F, -16F, -48F);
        this.parts[352].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[353] = new ModelRenderer(this, 0, 0);
        this.parts[353].setRotationPoint(0F, -16F, -32F);
        this.parts[353].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[354] = new ModelRenderer(this, 0, 0);
        this.parts[354].setRotationPoint(0F, -16F, -16F);
        this.parts[354].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[355] = new ModelRenderer(this, 0, 0);
        this.parts[355].setRotationPoint(0F, -16F, 0F);
        this.parts[355].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[356] = new ModelRenderer(this, 0, 0);
        this.parts[356].setRotationPoint(0F, -16F, 16F);
        this.parts[356].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[357] = new ModelRenderer(this, 0, 0);
        this.parts[357].setRotationPoint(0F, -16F, 32F);
        this.parts[357].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[358] = new ModelRenderer(this, 0, 0);
        this.parts[358].setRotationPoint(0F, -16F, 48F);
        this.parts[358].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[359] = new ModelRenderer(this, 0, 0);
        this.parts[359].setRotationPoint(0F, -16F, 64F);
        this.parts[359].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[360] = new ModelRenderer(this, 0, 0);
        this.parts[360].setRotationPoint(0F, 0F, -64F);
        this.parts[360].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[361] = new ModelRenderer(this, 0, 0);
        this.parts[361].setRotationPoint(0F, 0F, -48F);
        this.parts[361].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[362] = new ModelRenderer(this, 0, 0);
        this.parts[362].setRotationPoint(0F, 0F, -32F);
        this.parts[362].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[363] = new ModelRenderer(this, 0, 0);
        this.parts[363].setRotationPoint(0F, 0F, -16F);
        this.parts[363].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[364] = new ModelRenderer(this, 0, 0);
        this.parts[364].setRotationPoint(0F, 0F, 0F);
        this.parts[364].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[365] = new ModelRenderer(this, 0, 0);
        this.parts[365].setRotationPoint(0F, 0F, 16F);
        this.parts[365].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[366] = new ModelRenderer(this, 0, 0);
        this.parts[366].setRotationPoint(0F, 0F, 32F);
        this.parts[366].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[367] = new ModelRenderer(this, 0, 0);
        this.parts[367].setRotationPoint(0F, 0F, 48F);
        this.parts[367].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[368] = new ModelRenderer(this, 0, 0);
        this.parts[368].setRotationPoint(0F, 0F, 64F);
        this.parts[368].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[369] = new ModelRenderer(this, 0, 0);
        this.parts[369].setRotationPoint(0F, 16F, -64F);
        this.parts[369].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[370] = new ModelRenderer(this, 0, 0);
        this.parts[370].setRotationPoint(0F, 16F, -48F);
        this.parts[370].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[371] = new ModelRenderer(this, 0, 0);
        this.parts[371].setRotationPoint(0F, 16F, -32F);
        this.parts[371].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[372] = new ModelRenderer(this, 0, 0);
        this.parts[372].setRotationPoint(0F, 16F, -16F);
        this.parts[372].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[373] = new ModelRenderer(this, 0, 0);
        this.parts[373].setRotationPoint(0F, 16F, 0F);
        this.parts[373].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[374] = new ModelRenderer(this, 0, 0);
        this.parts[374].setRotationPoint(0F, 16F, 16F);
        this.parts[374].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[375] = new ModelRenderer(this, 0, 0);
        this.parts[375].setRotationPoint(0F, 16F, 32F);
        this.parts[375].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[376] = new ModelRenderer(this, 0, 0);
        this.parts[376].setRotationPoint(0F, 16F, 48F);
        this.parts[376].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[377] = new ModelRenderer(this, 0, 0);
        this.parts[377].setRotationPoint(0F, 16F, 64F);
        this.parts[377].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[378] = new ModelRenderer(this, 0, 0);
        this.parts[378].setRotationPoint(0F, 32F, -64F);
        this.parts[378].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[379] = new ModelRenderer(this, 0, 0);
        this.parts[379].setRotationPoint(0F, 32F, -48F);
        this.parts[379].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[380] = new ModelRenderer(this, 0, 0);
        this.parts[380].setRotationPoint(0F, 32F, -32F);
        this.parts[380].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[381] = new ModelRenderer(this, 0, 0);
        this.parts[381].setRotationPoint(0F, 32F, -16F);
        this.parts[381].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[382] = new ModelRenderer(this, 0, 0);
        this.parts[382].setRotationPoint(0F, 32F, 0F);
        this.parts[382].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[383] = new ModelRenderer(this, 0, 0);
        this.parts[383].setRotationPoint(0F, 32F, 16F);
        this.parts[383].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[384] = new ModelRenderer(this, 0, 0);
        this.parts[384].setRotationPoint(0F, 32F, 32F);
        this.parts[384].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[385] = new ModelRenderer(this, 0, 0);
        this.parts[385].setRotationPoint(0F, 32F, 48F);
        this.parts[385].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[386] = new ModelRenderer(this, 0, 0);
        this.parts[386].setRotationPoint(0F, 32F, 64F);
        this.parts[386].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[387] = new ModelRenderer(this, 0, 0);
        this.parts[387].setRotationPoint(0F, 48F, -64F);
        this.parts[387].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[388] = new ModelRenderer(this, 0, 0);
        this.parts[388].setRotationPoint(0F, 48F, -48F);
        this.parts[388].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[389] = new ModelRenderer(this, 0, 0);
        this.parts[389].setRotationPoint(0F, 48F, -32F);
        this.parts[389].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[390] = new ModelRenderer(this, 0, 0);
        this.parts[390].setRotationPoint(0F, 48F, -16F);
        this.parts[390].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[391] = new ModelRenderer(this, 0, 0);
        this.parts[391].setRotationPoint(0F, 48F, 0F);
        this.parts[391].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[392] = new ModelRenderer(this, 0, 0);
        this.parts[392].setRotationPoint(0F, 48F, 16F);
        this.parts[392].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[393] = new ModelRenderer(this, 0, 0);
        this.parts[393].setRotationPoint(0F, 48F, 32F);
        this.parts[393].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[394] = new ModelRenderer(this, 0, 0);
        this.parts[394].setRotationPoint(0F, 48F, 48F);
        this.parts[394].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[395] = new ModelRenderer(this, 0, 0);
        this.parts[395].setRotationPoint(0F, 48F, 64F);
        this.parts[395].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[396] = new ModelRenderer(this, 0, 0);
        this.parts[396].setRotationPoint(0F, 64F, -64F);
        this.parts[396].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[397] = new ModelRenderer(this, 0, 0);
        this.parts[397].setRotationPoint(0F, 64F, -48F);
        this.parts[397].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[398] = new ModelRenderer(this, 0, 0);
        this.parts[398].setRotationPoint(0F, 64F, -32F);
        this.parts[398].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[399] = new ModelRenderer(this, 0, 0);
        this.parts[399].setRotationPoint(0F, 64F, -16F);
        this.parts[399].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[400] = new ModelRenderer(this, 0, 0);
        this.parts[400].setRotationPoint(0F, 64F, 0F);
        this.parts[400].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[401] = new ModelRenderer(this, 0, 0);
        this.parts[401].setRotationPoint(0F, 64F, 16F);
        this.parts[401].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[402] = new ModelRenderer(this, 0, 0);
        this.parts[402].setRotationPoint(0F, 64F, 32F);
        this.parts[402].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[403] = new ModelRenderer(this, 0, 0);
        this.parts[403].setRotationPoint(0F, 64F, 48F);
        this.parts[403].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[404] = new ModelRenderer(this, 0, 0);
        this.parts[404].setRotationPoint(0F, 64F, 64F);
        this.parts[404].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[405] = new ModelRenderer(this, 0, 0);
        this.parts[405].setRotationPoint(16F, -64F, -64F);
        this.parts[405].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[406] = new ModelRenderer(this, 0, 0);
        this.parts[406].setRotationPoint(16F, -64F, -48F);
        this.parts[406].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[407] = new ModelRenderer(this, 0, 0);
        this.parts[407].setRotationPoint(16F, -64F, -32F);
        this.parts[407].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[408] = new ModelRenderer(this, 0, 0);
        this.parts[408].setRotationPoint(16F, -64F, -16F);
        this.parts[408].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[409] = new ModelRenderer(this, 0, 0);
        this.parts[409].setRotationPoint(16F, -64F, 0F);
        this.parts[409].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[410] = new ModelRenderer(this, 0, 0);
        this.parts[410].setRotationPoint(16F, -64F, 16F);
        this.parts[410].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[411] = new ModelRenderer(this, 0, 0);
        this.parts[411].setRotationPoint(16F, -64F, 32F);
        this.parts[411].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[412] = new ModelRenderer(this, 0, 0);
        this.parts[412].setRotationPoint(16F, -64F, 48F);
        this.parts[412].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[413] = new ModelRenderer(this, 0, 0);
        this.parts[413].setRotationPoint(16F, -64F, 64F);
        this.parts[413].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[414] = new ModelRenderer(this, 0, 0);
        this.parts[414].setRotationPoint(16F, -48F, -64F);
        this.parts[414].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[415] = new ModelRenderer(this, 0, 0);
        this.parts[415].setRotationPoint(16F, -48F, -48F);
        this.parts[415].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[416] = new ModelRenderer(this, 0, 0);
        this.parts[416].setRotationPoint(16F, -48F, -32F);
        this.parts[416].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[417] = new ModelRenderer(this, 0, 0);
        this.parts[417].setRotationPoint(16F, -48F, -16F);
        this.parts[417].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[418] = new ModelRenderer(this, 0, 0);
        this.parts[418].setRotationPoint(16F, -48F, 0F);
        this.parts[418].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[419] = new ModelRenderer(this, 0, 0);
        this.parts[419].setRotationPoint(16F, -48F, 16F);
        this.parts[419].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[420] = new ModelRenderer(this, 0, 0);
        this.parts[420].setRotationPoint(16F, -48F, 32F);
        this.parts[420].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[421] = new ModelRenderer(this, 0, 0);
        this.parts[421].setRotationPoint(16F, -48F, 48F);
        this.parts[421].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[422] = new ModelRenderer(this, 0, 0);
        this.parts[422].setRotationPoint(16F, -48F, 64F);
        this.parts[422].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[423] = new ModelRenderer(this, 0, 0);
        this.parts[423].setRotationPoint(16F, -32F, -64F);
        this.parts[423].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[424] = new ModelRenderer(this, 0, 0);
        this.parts[424].setRotationPoint(16F, -32F, -48F);
        this.parts[424].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[425] = new ModelRenderer(this, 0, 0);
        this.parts[425].setRotationPoint(16F, -32F, -32F);
        this.parts[425].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[426] = new ModelRenderer(this, 0, 0);
        this.parts[426].setRotationPoint(16F, -32F, -16F);
        this.parts[426].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[427] = new ModelRenderer(this, 0, 0);
        this.parts[427].setRotationPoint(16F, -32F, 0F);
        this.parts[427].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[428] = new ModelRenderer(this, 0, 0);
        this.parts[428].setRotationPoint(16F, -32F, 16F);
        this.parts[428].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[429] = new ModelRenderer(this, 0, 0);
        this.parts[429].setRotationPoint(16F, -32F, 32F);
        this.parts[429].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[430] = new ModelRenderer(this, 0, 0);
        this.parts[430].setRotationPoint(16F, -32F, 48F);
        this.parts[430].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[431] = new ModelRenderer(this, 0, 0);
        this.parts[431].setRotationPoint(16F, -32F, 64F);
        this.parts[431].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[432] = new ModelRenderer(this, 0, 0);
        this.parts[432].setRotationPoint(16F, -16F, -64F);
        this.parts[432].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[433] = new ModelRenderer(this, 0, 0);
        this.parts[433].setRotationPoint(16F, -16F, -48F);
        this.parts[433].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[434] = new ModelRenderer(this, 0, 0);
        this.parts[434].setRotationPoint(16F, -16F, -32F);
        this.parts[434].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[435] = new ModelRenderer(this, 0, 0);
        this.parts[435].setRotationPoint(16F, -16F, -16F);
        this.parts[435].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[436] = new ModelRenderer(this, 0, 0);
        this.parts[436].setRotationPoint(16F, -16F, 0F);
        this.parts[436].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[437] = new ModelRenderer(this, 0, 0);
        this.parts[437].setRotationPoint(16F, -16F, 16F);
        this.parts[437].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[438] = new ModelRenderer(this, 0, 0);
        this.parts[438].setRotationPoint(16F, -16F, 32F);
        this.parts[438].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[439] = new ModelRenderer(this, 0, 0);
        this.parts[439].setRotationPoint(16F, -16F, 48F);
        this.parts[439].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[440] = new ModelRenderer(this, 0, 0);
        this.parts[440].setRotationPoint(16F, -16F, 64F);
        this.parts[440].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[441] = new ModelRenderer(this, 0, 0);
        this.parts[441].setRotationPoint(16F, 0F, -64F);
        this.parts[441].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[442] = new ModelRenderer(this, 0, 0);
        this.parts[442].setRotationPoint(16F, 0F, -48F);
        this.parts[442].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[443] = new ModelRenderer(this, 0, 0);
        this.parts[443].setRotationPoint(16F, 0F, -32F);
        this.parts[443].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[444] = new ModelRenderer(this, 0, 0);
        this.parts[444].setRotationPoint(16F, 0F, -16F);
        this.parts[444].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[445] = new ModelRenderer(this, 0, 0);
        this.parts[445].setRotationPoint(16F, 0F, 0F);
        this.parts[445].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[446] = new ModelRenderer(this, 0, 0);
        this.parts[446].setRotationPoint(16F, 0F, 16F);
        this.parts[446].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[447] = new ModelRenderer(this, 0, 0);
        this.parts[447].setRotationPoint(16F, 0F, 32F);
        this.parts[447].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[448] = new ModelRenderer(this, 0, 0);
        this.parts[448].setRotationPoint(16F, 0F, 48F);
        this.parts[448].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[449] = new ModelRenderer(this, 0, 0);
        this.parts[449].setRotationPoint(16F, 0F, 64F);
        this.parts[449].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[450] = new ModelRenderer(this, 0, 0);
        this.parts[450].setRotationPoint(16F, 16F, -64F);
        this.parts[450].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[451] = new ModelRenderer(this, 0, 0);
        this.parts[451].setRotationPoint(16F, 16F, -48F);
        this.parts[451].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[452] = new ModelRenderer(this, 0, 0);
        this.parts[452].setRotationPoint(16F, 16F, -32F);
        this.parts[452].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[453] = new ModelRenderer(this, 0, 0);
        this.parts[453].setRotationPoint(16F, 16F, -16F);
        this.parts[453].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[454] = new ModelRenderer(this, 0, 0);
        this.parts[454].setRotationPoint(16F, 16F, 0F);
        this.parts[454].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[455] = new ModelRenderer(this, 0, 0);
        this.parts[455].setRotationPoint(16F, 16F, 16F);
        this.parts[455].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[456] = new ModelRenderer(this, 0, 0);
        this.parts[456].setRotationPoint(16F, 16F, 32F);
        this.parts[456].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[457] = new ModelRenderer(this, 0, 0);
        this.parts[457].setRotationPoint(16F, 16F, 48F);
        this.parts[457].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[458] = new ModelRenderer(this, 0, 0);
        this.parts[458].setRotationPoint(16F, 16F, 64F);
        this.parts[458].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[459] = new ModelRenderer(this, 0, 0);
        this.parts[459].setRotationPoint(16F, 32F, -64F);
        this.parts[459].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[460] = new ModelRenderer(this, 0, 0);
        this.parts[460].setRotationPoint(16F, 32F, -48F);
        this.parts[460].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[461] = new ModelRenderer(this, 0, 0);
        this.parts[461].setRotationPoint(16F, 32F, -32F);
        this.parts[461].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[462] = new ModelRenderer(this, 0, 0);
        this.parts[462].setRotationPoint(16F, 32F, -16F);
        this.parts[462].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[463] = new ModelRenderer(this, 0, 0);
        this.parts[463].setRotationPoint(16F, 32F, 0F);
        this.parts[463].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[464] = new ModelRenderer(this, 0, 0);
        this.parts[464].setRotationPoint(16F, 32F, 16F);
        this.parts[464].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[465] = new ModelRenderer(this, 0, 0);
        this.parts[465].setRotationPoint(16F, 32F, 32F);
        this.parts[465].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[466] = new ModelRenderer(this, 0, 0);
        this.parts[466].setRotationPoint(16F, 32F, 48F);
        this.parts[466].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[467] = new ModelRenderer(this, 0, 0);
        this.parts[467].setRotationPoint(16F, 32F, 64F);
        this.parts[467].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[468] = new ModelRenderer(this, 0, 0);
        this.parts[468].setRotationPoint(16F, 48F, -64F);
        this.parts[468].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[469] = new ModelRenderer(this, 0, 0);
        this.parts[469].setRotationPoint(16F, 48F, -48F);
        this.parts[469].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[470] = new ModelRenderer(this, 0, 0);
        this.parts[470].setRotationPoint(16F, 48F, -32F);
        this.parts[470].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[471] = new ModelRenderer(this, 0, 0);
        this.parts[471].setRotationPoint(16F, 48F, -16F);
        this.parts[471].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[472] = new ModelRenderer(this, 0, 0);
        this.parts[472].setRotationPoint(16F, 48F, 0F);
        this.parts[472].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[473] = new ModelRenderer(this, 0, 0);
        this.parts[473].setRotationPoint(16F, 48F, 16F);
        this.parts[473].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[474] = new ModelRenderer(this, 0, 0);
        this.parts[474].setRotationPoint(16F, 48F, 32F);
        this.parts[474].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[475] = new ModelRenderer(this, 0, 0);
        this.parts[475].setRotationPoint(16F, 48F, 48F);
        this.parts[475].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[476] = new ModelRenderer(this, 0, 0);
        this.parts[476].setRotationPoint(16F, 48F, 64F);
        this.parts[476].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[477] = new ModelRenderer(this, 0, 0);
        this.parts[477].setRotationPoint(16F, 64F, -64F);
        this.parts[477].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[478] = new ModelRenderer(this, 0, 0);
        this.parts[478].setRotationPoint(16F, 64F, -48F);
        this.parts[478].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[479] = new ModelRenderer(this, 0, 0);
        this.parts[479].setRotationPoint(16F, 64F, -32F);
        this.parts[479].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[480] = new ModelRenderer(this, 0, 0);
        this.parts[480].setRotationPoint(16F, 64F, -16F);
        this.parts[480].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[481] = new ModelRenderer(this, 0, 0);
        this.parts[481].setRotationPoint(16F, 64F, 0F);
        this.parts[481].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[482] = new ModelRenderer(this, 0, 0);
        this.parts[482].setRotationPoint(16F, 64F, 16F);
        this.parts[482].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[483] = new ModelRenderer(this, 0, 0);
        this.parts[483].setRotationPoint(16F, 64F, 32F);
        this.parts[483].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[484] = new ModelRenderer(this, 0, 0);
        this.parts[484].setRotationPoint(16F, 64F, 48F);
        this.parts[484].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[485] = new ModelRenderer(this, 0, 0);
        this.parts[485].setRotationPoint(16F, 64F, 64F);
        this.parts[485].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[486] = new ModelRenderer(this, 0, 0);
        this.parts[486].setRotationPoint(32F, -64F, -64F);
        this.parts[486].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[487] = new ModelRenderer(this, 0, 0);
        this.parts[487].setRotationPoint(32F, -64F, -48F);
        this.parts[487].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[488] = new ModelRenderer(this, 0, 0);
        this.parts[488].setRotationPoint(32F, -64F, -32F);
        this.parts[488].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[489] = new ModelRenderer(this, 0, 0);
        this.parts[489].setRotationPoint(32F, -64F, -16F);
        this.parts[489].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[490] = new ModelRenderer(this, 0, 0);
        this.parts[490].setRotationPoint(32F, -64F, 0F);
        this.parts[490].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[491] = new ModelRenderer(this, 0, 0);
        this.parts[491].setRotationPoint(32F, -64F, 16F);
        this.parts[491].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[492] = new ModelRenderer(this, 0, 0);
        this.parts[492].setRotationPoint(32F, -64F, 32F);
        this.parts[492].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[493] = new ModelRenderer(this, 0, 0);
        this.parts[493].setRotationPoint(32F, -64F, 48F);
        this.parts[493].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[494] = new ModelRenderer(this, 0, 0);
        this.parts[494].setRotationPoint(32F, -64F, 64F);
        this.parts[494].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[495] = new ModelRenderer(this, 0, 0);
        this.parts[495].setRotationPoint(32F, -48F, -64F);
        this.parts[495].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[496] = new ModelRenderer(this, 0, 0);
        this.parts[496].setRotationPoint(32F, -48F, -48F);
        this.parts[496].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[497] = new ModelRenderer(this, 0, 0);
        this.parts[497].setRotationPoint(32F, -48F, -32F);
        this.parts[497].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[498] = new ModelRenderer(this, 0, 0);
        this.parts[498].setRotationPoint(32F, -48F, -16F);
        this.parts[498].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[499] = new ModelRenderer(this, 0, 0);
        this.parts[499].setRotationPoint(32F, -48F, 0F);
        this.parts[499].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[500] = new ModelRenderer(this, 0, 0);
        this.parts[500].setRotationPoint(32F, -48F, 16F);
        this.parts[500].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[501] = new ModelRenderer(this, 0, 0);
        this.parts[501].setRotationPoint(32F, -48F, 32F);
        this.parts[501].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[502] = new ModelRenderer(this, 0, 0);
        this.parts[502].setRotationPoint(32F, -48F, 48F);
        this.parts[502].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[503] = new ModelRenderer(this, 0, 0);
        this.parts[503].setRotationPoint(32F, -48F, 64F);
        this.parts[503].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[504] = new ModelRenderer(this, 0, 0);
        this.parts[504].setRotationPoint(32F, -32F, -64F);
        this.parts[504].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[505] = new ModelRenderer(this, 0, 0);
        this.parts[505].setRotationPoint(32F, -32F, -48F);
        this.parts[505].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[506] = new ModelRenderer(this, 0, 0);
        this.parts[506].setRotationPoint(32F, -32F, -32F);
        this.parts[506].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[507] = new ModelRenderer(this, 0, 0);
        this.parts[507].setRotationPoint(32F, -32F, -16F);
        this.parts[507].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[508] = new ModelRenderer(this, 0, 0);
        this.parts[508].setRotationPoint(32F, -32F, 0F);
        this.parts[508].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[509] = new ModelRenderer(this, 0, 0);
        this.parts[509].setRotationPoint(32F, -32F, 16F);
        this.parts[509].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[510] = new ModelRenderer(this, 0, 0);
        this.parts[510].setRotationPoint(32F, -32F, 32F);
        this.parts[510].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[511] = new ModelRenderer(this, 0, 0);
        this.parts[511].setRotationPoint(32F, -32F, 48F);
        this.parts[511].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[512] = new ModelRenderer(this, 0, 0);
        this.parts[512].setRotationPoint(32F, -32F, 64F);
        this.parts[512].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[513] = new ModelRenderer(this, 0, 0);
        this.parts[513].setRotationPoint(32F, -16F, -64F);
        this.parts[513].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[514] = new ModelRenderer(this, 0, 0);
        this.parts[514].setRotationPoint(32F, -16F, -48F);
        this.parts[514].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[515] = new ModelRenderer(this, 0, 0);
        this.parts[515].setRotationPoint(32F, -16F, -32F);
        this.parts[515].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[516] = new ModelRenderer(this, 0, 0);
        this.parts[516].setRotationPoint(32F, -16F, -16F);
        this.parts[516].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[517] = new ModelRenderer(this, 0, 0);
        this.parts[517].setRotationPoint(32F, -16F, 0F);
        this.parts[517].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[518] = new ModelRenderer(this, 0, 0);
        this.parts[518].setRotationPoint(32F, -16F, 16F);
        this.parts[518].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[519] = new ModelRenderer(this, 0, 0);
        this.parts[519].setRotationPoint(32F, -16F, 32F);
        this.parts[519].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[520] = new ModelRenderer(this, 0, 0);
        this.parts[520].setRotationPoint(32F, -16F, 48F);
        this.parts[520].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[521] = new ModelRenderer(this, 0, 0);
        this.parts[521].setRotationPoint(32F, -16F, 64F);
        this.parts[521].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[522] = new ModelRenderer(this, 0, 0);
        this.parts[522].setRotationPoint(32F, 0F, -64F);
        this.parts[522].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[523] = new ModelRenderer(this, 0, 0);
        this.parts[523].setRotationPoint(32F, 0F, -48F);
        this.parts[523].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[524] = new ModelRenderer(this, 0, 0);
        this.parts[524].setRotationPoint(32F, 0F, -32F);
        this.parts[524].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[525] = new ModelRenderer(this, 0, 0);
        this.parts[525].setRotationPoint(32F, 0F, -16F);
        this.parts[525].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[526] = new ModelRenderer(this, 0, 0);
        this.parts[526].setRotationPoint(32F, 0F, 0F);
        this.parts[526].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[527] = new ModelRenderer(this, 0, 0);
        this.parts[527].setRotationPoint(32F, 0F, 16F);
        this.parts[527].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[528] = new ModelRenderer(this, 0, 0);
        this.parts[528].setRotationPoint(32F, 0F, 32F);
        this.parts[528].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[529] = new ModelRenderer(this, 0, 0);
        this.parts[529].setRotationPoint(32F, 0F, 48F);
        this.parts[529].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[530] = new ModelRenderer(this, 0, 0);
        this.parts[530].setRotationPoint(32F, 0F, 64F);
        this.parts[530].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[531] = new ModelRenderer(this, 0, 0);
        this.parts[531].setRotationPoint(32F, 16F, -64F);
        this.parts[531].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[532] = new ModelRenderer(this, 0, 0);
        this.parts[532].setRotationPoint(32F, 16F, -48F);
        this.parts[532].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[533] = new ModelRenderer(this, 0, 0);
        this.parts[533].setRotationPoint(32F, 16F, -32F);
        this.parts[533].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[534] = new ModelRenderer(this, 0, 0);
        this.parts[534].setRotationPoint(32F, 16F, -16F);
        this.parts[534].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[535] = new ModelRenderer(this, 0, 0);
        this.parts[535].setRotationPoint(32F, 16F, 0F);
        this.parts[535].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[536] = new ModelRenderer(this, 0, 0);
        this.parts[536].setRotationPoint(32F, 16F, 16F);
        this.parts[536].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[537] = new ModelRenderer(this, 0, 0);
        this.parts[537].setRotationPoint(32F, 16F, 32F);
        this.parts[537].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[538] = new ModelRenderer(this, 0, 0);
        this.parts[538].setRotationPoint(32F, 16F, 48F);
        this.parts[538].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[539] = new ModelRenderer(this, 0, 0);
        this.parts[539].setRotationPoint(32F, 16F, 64F);
        this.parts[539].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[540] = new ModelRenderer(this, 0, 0);
        this.parts[540].setRotationPoint(32F, 32F, -64F);
        this.parts[540].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[541] = new ModelRenderer(this, 0, 0);
        this.parts[541].setRotationPoint(32F, 32F, -48F);
        this.parts[541].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[542] = new ModelRenderer(this, 0, 0);
        this.parts[542].setRotationPoint(32F, 32F, -32F);
        this.parts[542].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[543] = new ModelRenderer(this, 0, 0);
        this.parts[543].setRotationPoint(32F, 32F, -16F);
        this.parts[543].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[544] = new ModelRenderer(this, 0, 0);
        this.parts[544].setRotationPoint(32F, 32F, 0F);
        this.parts[544].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[545] = new ModelRenderer(this, 0, 0);
        this.parts[545].setRotationPoint(32F, 32F, 16F);
        this.parts[545].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[546] = new ModelRenderer(this, 0, 0);
        this.parts[546].setRotationPoint(32F, 32F, 32F);
        this.parts[546].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[547] = new ModelRenderer(this, 0, 0);
        this.parts[547].setRotationPoint(32F, 32F, 48F);
        this.parts[547].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[548] = new ModelRenderer(this, 0, 0);
        this.parts[548].setRotationPoint(32F, 32F, 64F);
        this.parts[548].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[549] = new ModelRenderer(this, 0, 0);
        this.parts[549].setRotationPoint(32F, 48F, -64F);
        this.parts[549].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[550] = new ModelRenderer(this, 0, 0);
        this.parts[550].setRotationPoint(32F, 48F, -48F);
        this.parts[550].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[551] = new ModelRenderer(this, 0, 0);
        this.parts[551].setRotationPoint(32F, 48F, -32F);
        this.parts[551].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[552] = new ModelRenderer(this, 0, 0);
        this.parts[552].setRotationPoint(32F, 48F, -16F);
        this.parts[552].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[553] = new ModelRenderer(this, 0, 0);
        this.parts[553].setRotationPoint(32F, 48F, 0F);
        this.parts[553].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[554] = new ModelRenderer(this, 0, 0);
        this.parts[554].setRotationPoint(32F, 48F, 16F);
        this.parts[554].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[555] = new ModelRenderer(this, 0, 0);
        this.parts[555].setRotationPoint(32F, 48F, 32F);
        this.parts[555].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[556] = new ModelRenderer(this, 0, 0);
        this.parts[556].setRotationPoint(32F, 48F, 48F);
        this.parts[556].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[557] = new ModelRenderer(this, 0, 0);
        this.parts[557].setRotationPoint(32F, 48F, 64F);
        this.parts[557].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[558] = new ModelRenderer(this, 0, 0);
        this.parts[558].setRotationPoint(32F, 64F, -64F);
        this.parts[558].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[559] = new ModelRenderer(this, 0, 0);
        this.parts[559].setRotationPoint(32F, 64F, -48F);
        this.parts[559].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[560] = new ModelRenderer(this, 0, 0);
        this.parts[560].setRotationPoint(32F, 64F, -32F);
        this.parts[560].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[561] = new ModelRenderer(this, 0, 0);
        this.parts[561].setRotationPoint(32F, 64F, -16F);
        this.parts[561].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[562] = new ModelRenderer(this, 0, 0);
        this.parts[562].setRotationPoint(32F, 64F, 0F);
        this.parts[562].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[563] = new ModelRenderer(this, 0, 0);
        this.parts[563].setRotationPoint(32F, 64F, 16F);
        this.parts[563].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[564] = new ModelRenderer(this, 0, 0);
        this.parts[564].setRotationPoint(32F, 64F, 32F);
        this.parts[564].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[565] = new ModelRenderer(this, 0, 0);
        this.parts[565].setRotationPoint(32F, 64F, 48F);
        this.parts[565].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[566] = new ModelRenderer(this, 0, 0);
        this.parts[566].setRotationPoint(32F, 64F, 64F);
        this.parts[566].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[567] = new ModelRenderer(this, 0, 0);
        this.parts[567].setRotationPoint(48F, -64F, -64F);
        this.parts[567].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[568] = new ModelRenderer(this, 0, 0);
        this.parts[568].setRotationPoint(48F, -64F, -48F);
        this.parts[568].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[569] = new ModelRenderer(this, 0, 0);
        this.parts[569].setRotationPoint(48F, -64F, -32F);
        this.parts[569].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[570] = new ModelRenderer(this, 0, 0);
        this.parts[570].setRotationPoint(48F, -64F, -16F);
        this.parts[570].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[571] = new ModelRenderer(this, 0, 0);
        this.parts[571].setRotationPoint(48F, -64F, 0F);
        this.parts[571].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[572] = new ModelRenderer(this, 0, 0);
        this.parts[572].setRotationPoint(48F, -64F, 16F);
        this.parts[572].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[573] = new ModelRenderer(this, 0, 0);
        this.parts[573].setRotationPoint(48F, -64F, 32F);
        this.parts[573].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[574] = new ModelRenderer(this, 0, 0);
        this.parts[574].setRotationPoint(48F, -64F, 48F);
        this.parts[574].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[575] = new ModelRenderer(this, 0, 0);
        this.parts[575].setRotationPoint(48F, -64F, 64F);
        this.parts[575].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[576] = new ModelRenderer(this, 0, 0);
        this.parts[576].setRotationPoint(48F, -48F, -64F);
        this.parts[576].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[577] = new ModelRenderer(this, 0, 0);
        this.parts[577].setRotationPoint(48F, -48F, -48F);
        this.parts[577].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[578] = new ModelRenderer(this, 0, 0);
        this.parts[578].setRotationPoint(48F, -48F, -32F);
        this.parts[578].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[579] = new ModelRenderer(this, 0, 0);
        this.parts[579].setRotationPoint(48F, -48F, -16F);
        this.parts[579].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[580] = new ModelRenderer(this, 0, 0);
        this.parts[580].setRotationPoint(48F, -48F, 0F);
        this.parts[580].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[581] = new ModelRenderer(this, 0, 0);
        this.parts[581].setRotationPoint(48F, -48F, 16F);
        this.parts[581].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[582] = new ModelRenderer(this, 0, 0);
        this.parts[582].setRotationPoint(48F, -48F, 32F);
        this.parts[582].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[583] = new ModelRenderer(this, 0, 0);
        this.parts[583].setRotationPoint(48F, -48F, 48F);
        this.parts[583].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[584] = new ModelRenderer(this, 0, 0);
        this.parts[584].setRotationPoint(48F, -48F, 64F);
        this.parts[584].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[585] = new ModelRenderer(this, 0, 0);
        this.parts[585].setRotationPoint(48F, -32F, -64F);
        this.parts[585].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[586] = new ModelRenderer(this, 0, 0);
        this.parts[586].setRotationPoint(48F, -32F, -48F);
        this.parts[586].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[587] = new ModelRenderer(this, 0, 0);
        this.parts[587].setRotationPoint(48F, -32F, -32F);
        this.parts[587].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[588] = new ModelRenderer(this, 0, 0);
        this.parts[588].setRotationPoint(48F, -32F, -16F);
        this.parts[588].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[589] = new ModelRenderer(this, 0, 0);
        this.parts[589].setRotationPoint(48F, -32F, 0F);
        this.parts[589].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[590] = new ModelRenderer(this, 0, 0);
        this.parts[590].setRotationPoint(48F, -32F, 16F);
        this.parts[590].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[591] = new ModelRenderer(this, 0, 0);
        this.parts[591].setRotationPoint(48F, -32F, 32F);
        this.parts[591].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[592] = new ModelRenderer(this, 0, 0);
        this.parts[592].setRotationPoint(48F, -32F, 48F);
        this.parts[592].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[593] = new ModelRenderer(this, 0, 0);
        this.parts[593].setRotationPoint(48F, -32F, 64F);
        this.parts[593].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[594] = new ModelRenderer(this, 0, 0);
        this.parts[594].setRotationPoint(48F, -16F, -64F);
        this.parts[594].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[595] = new ModelRenderer(this, 0, 0);
        this.parts[595].setRotationPoint(48F, -16F, -48F);
        this.parts[595].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[596] = new ModelRenderer(this, 0, 0);
        this.parts[596].setRotationPoint(48F, -16F, -32F);
        this.parts[596].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[597] = new ModelRenderer(this, 0, 0);
        this.parts[597].setRotationPoint(48F, -16F, -16F);
        this.parts[597].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[598] = new ModelRenderer(this, 0, 0);
        this.parts[598].setRotationPoint(48F, -16F, 0F);
        this.parts[598].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[599] = new ModelRenderer(this, 0, 0);
        this.parts[599].setRotationPoint(48F, -16F, 16F);
        this.parts[599].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[600] = new ModelRenderer(this, 0, 0);
        this.parts[600].setRotationPoint(48F, -16F, 32F);
        this.parts[600].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[601] = new ModelRenderer(this, 0, 0);
        this.parts[601].setRotationPoint(48F, -16F, 48F);
        this.parts[601].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[602] = new ModelRenderer(this, 0, 0);
        this.parts[602].setRotationPoint(48F, -16F, 64F);
        this.parts[602].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[603] = new ModelRenderer(this, 0, 0);
        this.parts[603].setRotationPoint(48F, 0F, -64F);
        this.parts[603].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[604] = new ModelRenderer(this, 0, 0);
        this.parts[604].setRotationPoint(48F, 0F, -48F);
        this.parts[604].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[605] = new ModelRenderer(this, 0, 0);
        this.parts[605].setRotationPoint(48F, 0F, -32F);
        this.parts[605].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[606] = new ModelRenderer(this, 0, 0);
        this.parts[606].setRotationPoint(48F, 0F, -16F);
        this.parts[606].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[607] = new ModelRenderer(this, 0, 0);
        this.parts[607].setRotationPoint(48F, 0F, 0F);
        this.parts[607].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[608] = new ModelRenderer(this, 0, 0);
        this.parts[608].setRotationPoint(48F, 0F, 16F);
        this.parts[608].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[609] = new ModelRenderer(this, 0, 0);
        this.parts[609].setRotationPoint(48F, 0F, 32F);
        this.parts[609].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[610] = new ModelRenderer(this, 0, 0);
        this.parts[610].setRotationPoint(48F, 0F, 48F);
        this.parts[610].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[611] = new ModelRenderer(this, 0, 0);
        this.parts[611].setRotationPoint(48F, 0F, 64F);
        this.parts[611].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[612] = new ModelRenderer(this, 0, 0);
        this.parts[612].setRotationPoint(48F, 16F, -64F);
        this.parts[612].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[613] = new ModelRenderer(this, 0, 0);
        this.parts[613].setRotationPoint(48F, 16F, -48F);
        this.parts[613].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[614] = new ModelRenderer(this, 0, 0);
        this.parts[614].setRotationPoint(48F, 16F, -32F);
        this.parts[614].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[615] = new ModelRenderer(this, 0, 0);
        this.parts[615].setRotationPoint(48F, 16F, -16F);
        this.parts[615].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[616] = new ModelRenderer(this, 0, 0);
        this.parts[616].setRotationPoint(48F, 16F, 0F);
        this.parts[616].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[617] = new ModelRenderer(this, 0, 0);
        this.parts[617].setRotationPoint(48F, 16F, 16F);
        this.parts[617].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[618] = new ModelRenderer(this, 0, 0);
        this.parts[618].setRotationPoint(48F, 16F, 32F);
        this.parts[618].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[619] = new ModelRenderer(this, 0, 0);
        this.parts[619].setRotationPoint(48F, 16F, 48F);
        this.parts[619].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[620] = new ModelRenderer(this, 0, 0);
        this.parts[620].setRotationPoint(48F, 16F, 64F);
        this.parts[620].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[621] = new ModelRenderer(this, 0, 0);
        this.parts[621].setRotationPoint(48F, 32F, -64F);
        this.parts[621].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[622] = new ModelRenderer(this, 0, 0);
        this.parts[622].setRotationPoint(48F, 32F, -48F);
        this.parts[622].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[623] = new ModelRenderer(this, 0, 0);
        this.parts[623].setRotationPoint(48F, 32F, -32F);
        this.parts[623].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[624] = new ModelRenderer(this, 0, 0);
        this.parts[624].setRotationPoint(48F, 32F, -16F);
        this.parts[624].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[625] = new ModelRenderer(this, 0, 0);
        this.parts[625].setRotationPoint(48F, 32F, 0F);
        this.parts[625].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[626] = new ModelRenderer(this, 0, 0);
        this.parts[626].setRotationPoint(48F, 32F, 16F);
        this.parts[626].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[627] = new ModelRenderer(this, 0, 0);
        this.parts[627].setRotationPoint(48F, 32F, 32F);
        this.parts[627].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[628] = new ModelRenderer(this, 0, 0);
        this.parts[628].setRotationPoint(48F, 32F, 48F);
        this.parts[628].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[629] = new ModelRenderer(this, 0, 0);
        this.parts[629].setRotationPoint(48F, 32F, 64F);
        this.parts[629].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[630] = new ModelRenderer(this, 0, 0);
        this.parts[630].setRotationPoint(48F, 48F, -64F);
        this.parts[630].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[631] = new ModelRenderer(this, 0, 0);
        this.parts[631].setRotationPoint(48F, 48F, -48F);
        this.parts[631].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[632] = new ModelRenderer(this, 0, 0);
        this.parts[632].setRotationPoint(48F, 48F, -32F);
        this.parts[632].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[633] = new ModelRenderer(this, 0, 0);
        this.parts[633].setRotationPoint(48F, 48F, -16F);
        this.parts[633].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[634] = new ModelRenderer(this, 0, 0);
        this.parts[634].setRotationPoint(48F, 48F, 0F);
        this.parts[634].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[635] = new ModelRenderer(this, 0, 0);
        this.parts[635].setRotationPoint(48F, 48F, 16F);
        this.parts[635].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[636] = new ModelRenderer(this, 0, 0);
        this.parts[636].setRotationPoint(48F, 48F, 32F);
        this.parts[636].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[637] = new ModelRenderer(this, 0, 0);
        this.parts[637].setRotationPoint(48F, 48F, 48F);
        this.parts[637].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[638] = new ModelRenderer(this, 0, 0);
        this.parts[638].setRotationPoint(48F, 48F, 64F);
        this.parts[638].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[639] = new ModelRenderer(this, 0, 0);
        this.parts[639].setRotationPoint(48F, 64F, -64F);
        this.parts[639].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[640] = new ModelRenderer(this, 0, 0);
        this.parts[640].setRotationPoint(48F, 64F, -48F);
        this.parts[640].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[641] = new ModelRenderer(this, 0, 0);
        this.parts[641].setRotationPoint(48F, 64F, -32F);
        this.parts[641].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[642] = new ModelRenderer(this, 0, 0);
        this.parts[642].setRotationPoint(48F, 64F, -16F);
        this.parts[642].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[643] = new ModelRenderer(this, 0, 0);
        this.parts[643].setRotationPoint(48F, 64F, 0F);
        this.parts[643].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[644] = new ModelRenderer(this, 0, 0);
        this.parts[644].setRotationPoint(48F, 64F, 16F);
        this.parts[644].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[645] = new ModelRenderer(this, 0, 0);
        this.parts[645].setRotationPoint(48F, 64F, 32F);
        this.parts[645].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[646] = new ModelRenderer(this, 0, 0);
        this.parts[646].setRotationPoint(48F, 64F, 48F);
        this.parts[646].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[647] = new ModelRenderer(this, 0, 0);
        this.parts[647].setRotationPoint(48F, 64F, 64F);
        this.parts[647].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[648] = new ModelRenderer(this, 0, 0);
        this.parts[648].setRotationPoint(64F, -64F, -64F);
        this.parts[648].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[649] = new ModelRenderer(this, 0, 0);
        this.parts[649].setRotationPoint(64F, -64F, -48F);
        this.parts[649].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[650] = new ModelRenderer(this, 0, 0);
        this.parts[650].setRotationPoint(64F, -64F, -32F);
        this.parts[650].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[651] = new ModelRenderer(this, 0, 0);
        this.parts[651].setRotationPoint(64F, -64F, -16F);
        this.parts[651].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[652] = new ModelRenderer(this, 0, 0);
        this.parts[652].setRotationPoint(64F, -64F, 0F);
        this.parts[652].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[653] = new ModelRenderer(this, 0, 0);
        this.parts[653].setRotationPoint(64F, -64F, 16F);
        this.parts[653].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[654] = new ModelRenderer(this, 0, 0);
        this.parts[654].setRotationPoint(64F, -64F, 32F);
        this.parts[654].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[655] = new ModelRenderer(this, 0, 0);
        this.parts[655].setRotationPoint(64F, -64F, 48F);
        this.parts[655].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[656] = new ModelRenderer(this, 0, 0);
        this.parts[656].setRotationPoint(64F, -64F, 64F);
        this.parts[656].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[657] = new ModelRenderer(this, 0, 0);
        this.parts[657].setRotationPoint(64F, -48F, -64F);
        this.parts[657].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[658] = new ModelRenderer(this, 0, 0);
        this.parts[658].setRotationPoint(64F, -48F, -48F);
        this.parts[658].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[659] = new ModelRenderer(this, 0, 0);
        this.parts[659].setRotationPoint(64F, -48F, -32F);
        this.parts[659].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[660] = new ModelRenderer(this, 0, 0);
        this.parts[660].setRotationPoint(64F, -48F, -16F);
        this.parts[660].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[661] = new ModelRenderer(this, 0, 0);
        this.parts[661].setRotationPoint(64F, -48F, 0F);
        this.parts[661].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[662] = new ModelRenderer(this, 0, 0);
        this.parts[662].setRotationPoint(64F, -48F, 16F);
        this.parts[662].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[663] = new ModelRenderer(this, 0, 0);
        this.parts[663].setRotationPoint(64F, -48F, 32F);
        this.parts[663].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[664] = new ModelRenderer(this, 0, 0);
        this.parts[664].setRotationPoint(64F, -48F, 48F);
        this.parts[664].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[665] = new ModelRenderer(this, 0, 0);
        this.parts[665].setRotationPoint(64F, -48F, 64F);
        this.parts[665].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[666] = new ModelRenderer(this, 0, 0);
        this.parts[666].setRotationPoint(64F, -32F, -64F);
        this.parts[666].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[667] = new ModelRenderer(this, 0, 0);
        this.parts[667].setRotationPoint(64F, -32F, -48F);
        this.parts[667].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[668] = new ModelRenderer(this, 0, 0);
        this.parts[668].setRotationPoint(64F, -32F, -32F);
        this.parts[668].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[669] = new ModelRenderer(this, 0, 0);
        this.parts[669].setRotationPoint(64F, -32F, -16F);
        this.parts[669].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[670] = new ModelRenderer(this, 0, 0);
        this.parts[670].setRotationPoint(64F, -32F, 0F);
        this.parts[670].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[671] = new ModelRenderer(this, 0, 0);
        this.parts[671].setRotationPoint(64F, -32F, 16F);
        this.parts[671].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[672] = new ModelRenderer(this, 0, 0);
        this.parts[672].setRotationPoint(64F, -32F, 32F);
        this.parts[672].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[673] = new ModelRenderer(this, 0, 0);
        this.parts[673].setRotationPoint(64F, -32F, 48F);
        this.parts[673].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[674] = new ModelRenderer(this, 0, 0);
        this.parts[674].setRotationPoint(64F, -32F, 64F);
        this.parts[674].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[675] = new ModelRenderer(this, 0, 0);
        this.parts[675].setRotationPoint(64F, -16F, -64F);
        this.parts[675].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[676] = new ModelRenderer(this, 0, 0);
        this.parts[676].setRotationPoint(64F, -16F, -48F);
        this.parts[676].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[677] = new ModelRenderer(this, 0, 0);
        this.parts[677].setRotationPoint(64F, -16F, -32F);
        this.parts[677].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[678] = new ModelRenderer(this, 0, 0);
        this.parts[678].setRotationPoint(64F, -16F, -16F);
        this.parts[678].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[679] = new ModelRenderer(this, 0, 0);
        this.parts[679].setRotationPoint(64F, -16F, 0F);
        this.parts[679].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[680] = new ModelRenderer(this, 0, 0);
        this.parts[680].setRotationPoint(64F, -16F, 16F);
        this.parts[680].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[681] = new ModelRenderer(this, 0, 0);
        this.parts[681].setRotationPoint(64F, -16F, 32F);
        this.parts[681].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[682] = new ModelRenderer(this, 0, 0);
        this.parts[682].setRotationPoint(64F, -16F, 48F);
        this.parts[682].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[683] = new ModelRenderer(this, 0, 0);
        this.parts[683].setRotationPoint(64F, -16F, 64F);
        this.parts[683].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[684] = new ModelRenderer(this, 0, 0);
        this.parts[684].setRotationPoint(64F, 0F, -64F);
        this.parts[684].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[685] = new ModelRenderer(this, 0, 0);
        this.parts[685].setRotationPoint(64F, 0F, -48F);
        this.parts[685].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[686] = new ModelRenderer(this, 0, 0);
        this.parts[686].setRotationPoint(64F, 0F, -32F);
        this.parts[686].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[687] = new ModelRenderer(this, 0, 0);
        this.parts[687].setRotationPoint(64F, 0F, -16F);
        this.parts[687].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[688] = new ModelRenderer(this, 0, 0);
        this.parts[688].setRotationPoint(64F, 0F, 0F);
        this.parts[688].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[689] = new ModelRenderer(this, 0, 0);
        this.parts[689].setRotationPoint(64F, 0F, 16F);
        this.parts[689].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[690] = new ModelRenderer(this, 0, 0);
        this.parts[690].setRotationPoint(64F, 0F, 32F);
        this.parts[690].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[691] = new ModelRenderer(this, 0, 0);
        this.parts[691].setRotationPoint(64F, 0F, 48F);
        this.parts[691].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[692] = new ModelRenderer(this, 0, 0);
        this.parts[692].setRotationPoint(64F, 0F, 64F);
        this.parts[692].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[693] = new ModelRenderer(this, 0, 0);
        this.parts[693].setRotationPoint(64F, 16F, -64F);
        this.parts[693].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[694] = new ModelRenderer(this, 0, 0);
        this.parts[694].setRotationPoint(64F, 16F, -48F);
        this.parts[694].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[695] = new ModelRenderer(this, 0, 0);
        this.parts[695].setRotationPoint(64F, 16F, -32F);
        this.parts[695].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[696] = new ModelRenderer(this, 0, 0);
        this.parts[696].setRotationPoint(64F, 16F, -16F);
        this.parts[696].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[697] = new ModelRenderer(this, 0, 0);
        this.parts[697].setRotationPoint(64F, 16F, 0F);
        this.parts[697].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[698] = new ModelRenderer(this, 0, 0);
        this.parts[698].setRotationPoint(64F, 16F, 16F);
        this.parts[698].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[699] = new ModelRenderer(this, 0, 0);
        this.parts[699].setRotationPoint(64F, 16F, 32F);
        this.parts[699].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[700] = new ModelRenderer(this, 0, 0);
        this.parts[700].setRotationPoint(64F, 16F, 48F);
        this.parts[700].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[701] = new ModelRenderer(this, 0, 0);
        this.parts[701].setRotationPoint(64F, 16F, 64F);
        this.parts[701].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[702] = new ModelRenderer(this, 0, 0);
        this.parts[702].setRotationPoint(64F, 32F, -64F);
        this.parts[702].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[703] = new ModelRenderer(this, 0, 0);
        this.parts[703].setRotationPoint(64F, 32F, -48F);
        this.parts[703].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[704] = new ModelRenderer(this, 0, 0);
        this.parts[704].setRotationPoint(64F, 32F, -32F);
        this.parts[704].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[705] = new ModelRenderer(this, 0, 0);
        this.parts[705].setRotationPoint(64F, 32F, -16F);
        this.parts[705].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[706] = new ModelRenderer(this, 0, 0);
        this.parts[706].setRotationPoint(64F, 32F, 0F);
        this.parts[706].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[707] = new ModelRenderer(this, 0, 0);
        this.parts[707].setRotationPoint(64F, 32F, 16F);
        this.parts[707].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[708] = new ModelRenderer(this, 0, 0);
        this.parts[708].setRotationPoint(64F, 32F, 32F);
        this.parts[708].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[709] = new ModelRenderer(this, 0, 0);
        this.parts[709].setRotationPoint(64F, 32F, 48F);
        this.parts[709].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[710] = new ModelRenderer(this, 0, 0);
        this.parts[710].setRotationPoint(64F, 32F, 64F);
        this.parts[710].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[711] = new ModelRenderer(this, 0, 0);
        this.parts[711].setRotationPoint(64F, 48F, -64F);
        this.parts[711].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[712] = new ModelRenderer(this, 0, 0);
        this.parts[712].setRotationPoint(64F, 48F, -48F);
        this.parts[712].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[713] = new ModelRenderer(this, 0, 0);
        this.parts[713].setRotationPoint(64F, 48F, -32F);
        this.parts[713].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[714] = new ModelRenderer(this, 0, 0);
        this.parts[714].setRotationPoint(64F, 48F, -16F);
        this.parts[714].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[715] = new ModelRenderer(this, 0, 0);
        this.parts[715].setRotationPoint(64F, 48F, 0F);
        this.parts[715].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[716] = new ModelRenderer(this, 0, 0);
        this.parts[716].setRotationPoint(64F, 48F, 16F);
        this.parts[716].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[717] = new ModelRenderer(this, 0, 0);
        this.parts[717].setRotationPoint(64F, 48F, 32F);
        this.parts[717].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[718] = new ModelRenderer(this, 0, 0);
        this.parts[718].setRotationPoint(64F, 48F, 48F);
        this.parts[718].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[719] = new ModelRenderer(this, 0, 0);
        this.parts[719].setRotationPoint(64F, 48F, 64F);
        this.parts[719].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[720] = new ModelRenderer(this, 0, 0);
        this.parts[720].setRotationPoint(64F, 64F, -64F);
        this.parts[720].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[721] = new ModelRenderer(this, 0, 0);
        this.parts[721].setRotationPoint(64F, 64F, -48F);
        this.parts[721].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[722] = new ModelRenderer(this, 0, 0);
        this.parts[722].setRotationPoint(64F, 64F, -32F);
        this.parts[722].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[723] = new ModelRenderer(this, 0, 0);
        this.parts[723].setRotationPoint(64F, 64F, -16F);
        this.parts[723].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[724] = new ModelRenderer(this, 0, 0);
        this.parts[724].setRotationPoint(64F, 64F, 0F);
        this.parts[724].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[725] = new ModelRenderer(this, 0, 0);
        this.parts[725].setRotationPoint(64F, 64F, 16F);
        this.parts[725].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[726] = new ModelRenderer(this, 0, 0);
        this.parts[726].setRotationPoint(64F, 64F, 32F);
        this.parts[726].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[727] = new ModelRenderer(this, 0, 0);
        this.parts[727].setRotationPoint(64F, 64F, 48F);
        this.parts[727].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[728] = new ModelRenderer(this, 0, 0);
        this.parts[728].setRotationPoint(64F, 64F, 64F);
        this.parts[728].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);
		
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(this.parts[0]).forEach((modelRenderer) -> {
			modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
