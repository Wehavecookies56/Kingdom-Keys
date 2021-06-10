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
	public static final byte DIMENSIONS = 5;
    
	public ModelRenderer[] parts = new ModelRenderer[125];

    public GummiShipModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.parts[0] = new ModelRenderer(this, 0, 0);
        this.parts[0].setRotationPoint(-32F, -32F, -32F);
        this.parts[0].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[1] = new ModelRenderer(this, 0, 0);
        this.parts[1].setRotationPoint(-32F, -32F, -16F);
        this.parts[1].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[2] = new ModelRenderer(this, 0, 0);
        this.parts[2].setRotationPoint(-32F, -32F, 0F);
        this.parts[2].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[3] = new ModelRenderer(this, 0, 0);
        this.parts[3].setRotationPoint(-32F, -32F, 16F);
        this.parts[3].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[4] = new ModelRenderer(this, 0, 0);
        this.parts[4].setRotationPoint(-32F, -32F, 32F);
        this.parts[4].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[5] = new ModelRenderer(this, 0, 0);
        this.parts[5].setRotationPoint(-32F, -16F, -32F);
        this.parts[5].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[6] = new ModelRenderer(this, 0, 0);
        this.parts[6].setRotationPoint(-32F, -16F, -16F);
        this.parts[6].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[7] = new ModelRenderer(this, 0, 0);
        this.parts[7].setRotationPoint(-32F, -16F, 0F);
        this.parts[7].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[8] = new ModelRenderer(this, 0, 0);
        this.parts[8].setRotationPoint(-32F, -16F, 16F);
        this.parts[8].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[9] = new ModelRenderer(this, 0, 0);
        this.parts[9].setRotationPoint(-32F, -16F, 32F);
        this.parts[9].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[10] = new ModelRenderer(this, 0, 0);
        this.parts[10].setRotationPoint(-32F, 0F, -32F);
        this.parts[10].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[11] = new ModelRenderer(this, 0, 0);
        this.parts[11].setRotationPoint(-32F, 0F, -16F);
        this.parts[11].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[12] = new ModelRenderer(this, 0, 0);
        this.parts[12].setRotationPoint(-32F, 0F, 0F);
        this.parts[12].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[13] = new ModelRenderer(this, 0, 0);
        this.parts[13].setRotationPoint(-32F, 0F, 16F);
        this.parts[13].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[14] = new ModelRenderer(this, 0, 0);
        this.parts[14].setRotationPoint(-32F, 0F, 32F);
        this.parts[14].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[15] = new ModelRenderer(this, 0, 0);
        this.parts[15].setRotationPoint(-32F, 16F, -32F);
        this.parts[15].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[16] = new ModelRenderer(this, 0, 0);
        this.parts[16].setRotationPoint(-32F, 16F, -16F);
        this.parts[16].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[17] = new ModelRenderer(this, 0, 0);
        this.parts[17].setRotationPoint(-32F, 16F, 0F);
        this.parts[17].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[18] = new ModelRenderer(this, 0, 0);
        this.parts[18].setRotationPoint(-32F, 16F, 16F);
        this.parts[18].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[19] = new ModelRenderer(this, 0, 0);
        this.parts[19].setRotationPoint(-32F, 16F, 32F);
        this.parts[19].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[20] = new ModelRenderer(this, 0, 0);
        this.parts[20].setRotationPoint(-32F, 32F, -32F);
        this.parts[20].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[21] = new ModelRenderer(this, 0, 0);
        this.parts[21].setRotationPoint(-32F, 32F, -16F);
        this.parts[21].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[22] = new ModelRenderer(this, 0, 0);
        this.parts[22].setRotationPoint(-32F, 32F, 0F);
        this.parts[22].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[23] = new ModelRenderer(this, 0, 0);
        this.parts[23].setRotationPoint(-32F, 32F, 16F);
        this.parts[23].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[24] = new ModelRenderer(this, 0, 0);
        this.parts[24].setRotationPoint(-32F, 32F, 32F);
        this.parts[24].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[25] = new ModelRenderer(this, 0, 0);
        this.parts[25].setRotationPoint(-16F, -32F, -32F);
        this.parts[25].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[26] = new ModelRenderer(this, 0, 0);
        this.parts[26].setRotationPoint(-16F, -32F, -16F);
        this.parts[26].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[27] = new ModelRenderer(this, 0, 0);
        this.parts[27].setRotationPoint(-16F, -32F, 0F);
        this.parts[27].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[28] = new ModelRenderer(this, 0, 0);
        this.parts[28].setRotationPoint(-16F, -32F, 16F);
        this.parts[28].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[29] = new ModelRenderer(this, 0, 0);
        this.parts[29].setRotationPoint(-16F, -32F, 32F);
        this.parts[29].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[30] = new ModelRenderer(this, 0, 0);
        this.parts[30].setRotationPoint(-16F, -16F, -32F);
        this.parts[30].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[31] = new ModelRenderer(this, 0, 0);
        this.parts[31].setRotationPoint(-16F, -16F, -16F);
        this.parts[31].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[32] = new ModelRenderer(this, 0, 0);
        this.parts[32].setRotationPoint(-16F, -16F, 0F);
        this.parts[32].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[33] = new ModelRenderer(this, 0, 0);
        this.parts[33].setRotationPoint(-16F, -16F, 16F);
        this.parts[33].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[34] = new ModelRenderer(this, 0, 0);
        this.parts[34].setRotationPoint(-16F, -16F, 32F);
        this.parts[34].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[35] = new ModelRenderer(this, 0, 0);
        this.parts[35].setRotationPoint(-16F, 0F, -32F);
        this.parts[35].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[36] = new ModelRenderer(this, 0, 0);
        this.parts[36].setRotationPoint(-16F, 0F, -16F);
        this.parts[36].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[37] = new ModelRenderer(this, 0, 0);
        this.parts[37].setRotationPoint(-16F, 0F, 0F);
        this.parts[37].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[38] = new ModelRenderer(this, 0, 0);
        this.parts[38].setRotationPoint(-16F, 0F, 16F);
        this.parts[38].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[39] = new ModelRenderer(this, 0, 0);
        this.parts[39].setRotationPoint(-16F, 0F, 32F);
        this.parts[39].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[40] = new ModelRenderer(this, 0, 0);
        this.parts[40].setRotationPoint(-16F, 16F, -32F);
        this.parts[40].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[41] = new ModelRenderer(this, 0, 0);
        this.parts[41].setRotationPoint(-16F, 16F, -16F);
        this.parts[41].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[42] = new ModelRenderer(this, 0, 0);
        this.parts[42].setRotationPoint(-16F, 16F, 0F);
        this.parts[42].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[43] = new ModelRenderer(this, 0, 0);
        this.parts[43].setRotationPoint(-16F, 16F, 16F);
        this.parts[43].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[44] = new ModelRenderer(this, 0, 0);
        this.parts[44].setRotationPoint(-16F, 16F, 32F);
        this.parts[44].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[45] = new ModelRenderer(this, 0, 0);
        this.parts[45].setRotationPoint(-16F, 32F, -32F);
        this.parts[45].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[46] = new ModelRenderer(this, 0, 0);
        this.parts[46].setRotationPoint(-16F, 32F, -16F);
        this.parts[46].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[47] = new ModelRenderer(this, 0, 0);
        this.parts[47].setRotationPoint(-16F, 32F, 0F);
        this.parts[47].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[48] = new ModelRenderer(this, 0, 0);
        this.parts[48].setRotationPoint(-16F, 32F, 16F);
        this.parts[48].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[49] = new ModelRenderer(this, 0, 0);
        this.parts[49].setRotationPoint(-16F, 32F, 32F);
        this.parts[49].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[50] = new ModelRenderer(this, 0, 0);
        this.parts[50].setRotationPoint(0F, -32F, -32F);
        this.parts[50].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[51] = new ModelRenderer(this, 0, 0);
        this.parts[51].setRotationPoint(0F, -32F, -16F);
        this.parts[51].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[52] = new ModelRenderer(this, 0, 0);
        this.parts[52].setRotationPoint(0F, -32F, 0F);
        this.parts[52].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[53] = new ModelRenderer(this, 0, 0);
        this.parts[53].setRotationPoint(0F, -32F, 16F);
        this.parts[53].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[54] = new ModelRenderer(this, 0, 0);
        this.parts[54].setRotationPoint(0F, -32F, 32F);
        this.parts[54].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[55] = new ModelRenderer(this, 0, 0);
        this.parts[55].setRotationPoint(0F, -16F, -32F);
        this.parts[55].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[56] = new ModelRenderer(this, 0, 0);
        this.parts[56].setRotationPoint(0F, -16F, -16F);
        this.parts[56].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[57] = new ModelRenderer(this, 0, 0);
        this.parts[57].setRotationPoint(0F, -16F, 0F);
        this.parts[57].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[58] = new ModelRenderer(this, 0, 0);
        this.parts[58].setRotationPoint(0F, -16F, 16F);
        this.parts[58].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[59] = new ModelRenderer(this, 0, 0);
        this.parts[59].setRotationPoint(0F, -16F, 32F);
        this.parts[59].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[60] = new ModelRenderer(this, 0, 0);
        this.parts[60].setRotationPoint(0F, 0F, -32F);
        this.parts[60].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[61] = new ModelRenderer(this, 0, 0);
        this.parts[61].setRotationPoint(0F, 0F, -16F);
        this.parts[61].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[62] = new ModelRenderer(this, 0, 0);
        this.parts[62].setRotationPoint(0F, 0F, 0F);
        this.parts[62].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[63] = new ModelRenderer(this, 0, 0);
        this.parts[63].setRotationPoint(0F, 0F, 16F);
        this.parts[63].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[64] = new ModelRenderer(this, 0, 0);
        this.parts[64].setRotationPoint(0F, 0F, 32F);
        this.parts[64].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[65] = new ModelRenderer(this, 0, 0);
        this.parts[65].setRotationPoint(0F, 16F, -32F);
        this.parts[65].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[66] = new ModelRenderer(this, 0, 0);
        this.parts[66].setRotationPoint(0F, 16F, -16F);
        this.parts[66].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[67] = new ModelRenderer(this, 0, 0);
        this.parts[67].setRotationPoint(0F, 16F, 0F);
        this.parts[67].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[68] = new ModelRenderer(this, 0, 0);
        this.parts[68].setRotationPoint(0F, 16F, 16F);
        this.parts[68].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[69] = new ModelRenderer(this, 0, 0);
        this.parts[69].setRotationPoint(0F, 16F, 32F);
        this.parts[69].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[70] = new ModelRenderer(this, 0, 0);
        this.parts[70].setRotationPoint(0F, 32F, -32F);
        this.parts[70].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[71] = new ModelRenderer(this, 0, 0);
        this.parts[71].setRotationPoint(0F, 32F, -16F);
        this.parts[71].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[72] = new ModelRenderer(this, 0, 0);
        this.parts[72].setRotationPoint(0F, 32F, 0F);
        this.parts[72].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[73] = new ModelRenderer(this, 0, 0);
        this.parts[73].setRotationPoint(0F, 32F, 16F);
        this.parts[73].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[74] = new ModelRenderer(this, 0, 0);
        this.parts[74].setRotationPoint(0F, 32F, 32F);
        this.parts[74].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[75] = new ModelRenderer(this, 0, 0);
        this.parts[75].setRotationPoint(16F, -32F, -32F);
        this.parts[75].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[76] = new ModelRenderer(this, 0, 0);
        this.parts[76].setRotationPoint(16F, -32F, -16F);
        this.parts[76].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[77] = new ModelRenderer(this, 0, 0);
        this.parts[77].setRotationPoint(16F, -32F, 0F);
        this.parts[77].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[78] = new ModelRenderer(this, 0, 0);
        this.parts[78].setRotationPoint(16F, -32F, 16F);
        this.parts[78].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[79] = new ModelRenderer(this, 0, 0);
        this.parts[79].setRotationPoint(16F, -32F, 32F);
        this.parts[79].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[80] = new ModelRenderer(this, 0, 0);
        this.parts[80].setRotationPoint(16F, -16F, -32F);
        this.parts[80].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[81] = new ModelRenderer(this, 0, 0);
        this.parts[81].setRotationPoint(16F, -16F, -16F);
        this.parts[81].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[82] = new ModelRenderer(this, 0, 0);
        this.parts[82].setRotationPoint(16F, -16F, 0F);
        this.parts[82].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[83] = new ModelRenderer(this, 0, 0);
        this.parts[83].setRotationPoint(16F, -16F, 16F);
        this.parts[83].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[84] = new ModelRenderer(this, 0, 0);
        this.parts[84].setRotationPoint(16F, -16F, 32F);
        this.parts[84].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[85] = new ModelRenderer(this, 0, 0);
        this.parts[85].setRotationPoint(16F, 0F, -32F);
        this.parts[85].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[86] = new ModelRenderer(this, 0, 0);
        this.parts[86].setRotationPoint(16F, 0F, -16F);
        this.parts[86].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[87] = new ModelRenderer(this, 0, 0);
        this.parts[87].setRotationPoint(16F, 0F, 0F);
        this.parts[87].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[88] = new ModelRenderer(this, 0, 0);
        this.parts[88].setRotationPoint(16F, 0F, 16F);
        this.parts[88].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[89] = new ModelRenderer(this, 0, 0);
        this.parts[89].setRotationPoint(16F, 0F, 32F);
        this.parts[89].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[90] = new ModelRenderer(this, 0, 0);
        this.parts[90].setRotationPoint(16F, 16F, -32F);
        this.parts[90].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[91] = new ModelRenderer(this, 0, 0);
        this.parts[91].setRotationPoint(16F, 16F, -16F);
        this.parts[91].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[92] = new ModelRenderer(this, 0, 0);
        this.parts[92].setRotationPoint(16F, 16F, 0F);
        this.parts[92].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[93] = new ModelRenderer(this, 0, 0);
        this.parts[93].setRotationPoint(16F, 16F, 16F);
        this.parts[93].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[94] = new ModelRenderer(this, 0, 0);
        this.parts[94].setRotationPoint(16F, 16F, 32F);
        this.parts[94].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[95] = new ModelRenderer(this, 0, 0);
        this.parts[95].setRotationPoint(16F, 32F, -32F);
        this.parts[95].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[96] = new ModelRenderer(this, 0, 0);
        this.parts[96].setRotationPoint(16F, 32F, -16F);
        this.parts[96].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[97] = new ModelRenderer(this, 0, 0);
        this.parts[97].setRotationPoint(16F, 32F, 0F);
        this.parts[97].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[98] = new ModelRenderer(this, 0, 0);
        this.parts[98].setRotationPoint(16F, 32F, 16F);
        this.parts[98].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[99] = new ModelRenderer(this, 0, 0);
        this.parts[99].setRotationPoint(16F, 32F, 32F);
        this.parts[99].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[100] = new ModelRenderer(this, 0, 0);
        this.parts[100].setRotationPoint(32F, -32F, -32F);
        this.parts[100].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[101] = new ModelRenderer(this, 0, 0);
        this.parts[101].setRotationPoint(32F, -32F, -16F);
        this.parts[101].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[102] = new ModelRenderer(this, 0, 0);
        this.parts[102].setRotationPoint(32F, -32F, 0F);
        this.parts[102].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[103] = new ModelRenderer(this, 0, 0);
        this.parts[103].setRotationPoint(32F, -32F, 16F);
        this.parts[103].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[104] = new ModelRenderer(this, 0, 0);
        this.parts[104].setRotationPoint(32F, -32F, 32F);
        this.parts[104].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[105] = new ModelRenderer(this, 0, 0);
        this.parts[105].setRotationPoint(32F, -16F, -32F);
        this.parts[105].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[106] = new ModelRenderer(this, 0, 0);
        this.parts[106].setRotationPoint(32F, -16F, -16F);
        this.parts[106].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[107] = new ModelRenderer(this, 0, 0);
        this.parts[107].setRotationPoint(32F, -16F, 0F);
        this.parts[107].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[108] = new ModelRenderer(this, 0, 0);
        this.parts[108].setRotationPoint(32F, -16F, 16F);
        this.parts[108].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[109] = new ModelRenderer(this, 0, 0);
        this.parts[109].setRotationPoint(32F, -16F, 32F);
        this.parts[109].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[110] = new ModelRenderer(this, 0, 0);
        this.parts[110].setRotationPoint(32F, 0F, -32F);
        this.parts[110].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[111] = new ModelRenderer(this, 0, 0);
        this.parts[111].setRotationPoint(32F, 0F, -16F);
        this.parts[111].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[112] = new ModelRenderer(this, 0, 0);
        this.parts[112].setRotationPoint(32F, 0F, 0F);
        this.parts[112].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[113] = new ModelRenderer(this, 0, 0);
        this.parts[113].setRotationPoint(32F, 0F, 16F);
        this.parts[113].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[114] = new ModelRenderer(this, 0, 0);
        this.parts[114].setRotationPoint(32F, 0F, 32F);
        this.parts[114].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[115] = new ModelRenderer(this, 0, 0);
        this.parts[115].setRotationPoint(32F, 16F, -32F);
        this.parts[115].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[116] = new ModelRenderer(this, 0, 0);
        this.parts[116].setRotationPoint(32F, 16F, -16F);
        this.parts[116].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[117] = new ModelRenderer(this, 0, 0);
        this.parts[117].setRotationPoint(32F, 16F, 0F);
        this.parts[117].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[118] = new ModelRenderer(this, 0, 0);
        this.parts[118].setRotationPoint(32F, 16F, 16F);
        this.parts[118].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[119] = new ModelRenderer(this, 0, 0);
        this.parts[119].setRotationPoint(32F, 16F, 32F);
        this.parts[119].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[120] = new ModelRenderer(this, 0, 0);
        this.parts[120].setRotationPoint(32F, 32F, -32F);
        this.parts[120].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[121] = new ModelRenderer(this, 0, 0);
        this.parts[121].setRotationPoint(32F, 32F, -16F);
        this.parts[121].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[122] = new ModelRenderer(this, 0, 0);
        this.parts[122].setRotationPoint(32F, 32F, 0F);
        this.parts[122].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[123] = new ModelRenderer(this, 0, 0);
        this.parts[123].setRotationPoint(32F, 32F, 16F);
        this.parts[123].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[124] = new ModelRenderer(this, 0, 0);
        this.parts[124].setRotationPoint(32F, 32F, 32F);
        this.parts[124].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);
		
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
