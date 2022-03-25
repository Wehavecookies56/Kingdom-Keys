/*
package online.kingdomkeys.kingdomkeys.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GummiShipModel<T extends Entity> extends EntityModel<T> {
	public static final byte DIMENSIONS = 9;
    
	public ModelPart[] parts = new ModelPart[729];

    public GummiShipModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        
        this.parts[0] = new ModelPart(this, 0, 0);
        this.parts[0].setPos(-64F, -64F, -64F);
        this.parts[0].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[1] = new ModelPart(this, 0, 0);
        this.parts[1].setPos(-64F, -64F, -48F);
        this.parts[1].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[2] = new ModelPart(this, 0, 0);
        this.parts[2].setPos(-64F, -64F, -32F);
        this.parts[2].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[3] = new ModelPart(this, 0, 0);
        this.parts[3].setPos(-64F, -64F, -16F);
        this.parts[3].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[4] = new ModelPart(this, 0, 0);
        this.parts[4].setPos(-64F, -64F, 0F);
        this.parts[4].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[5] = new ModelPart(this, 0, 0);
        this.parts[5].setPos(-64F, -64F, 16F);
        this.parts[5].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[6] = new ModelPart(this, 0, 0);
        this.parts[6].setPos(-64F, -64F, 32F);
        this.parts[6].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[7] = new ModelPart(this, 0, 0);
        this.parts[7].setPos(-64F, -64F, 48F);
        this.parts[7].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[8] = new ModelPart(this, 0, 0);
        this.parts[8].setPos(-64F, -64F, 64F);
        this.parts[8].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[9] = new ModelPart(this, 0, 0);
        this.parts[9].setPos(-64F, -48F, -64F);
        this.parts[9].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[10] = new ModelPart(this, 0, 0);
        this.parts[10].setPos(-64F, -48F, -48F);
        this.parts[10].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[11] = new ModelPart(this, 0, 0);
        this.parts[11].setPos(-64F, -48F, -32F);
        this.parts[11].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[12] = new ModelPart(this, 0, 0);
        this.parts[12].setPos(-64F, -48F, -16F);
        this.parts[12].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[13] = new ModelPart(this, 0, 0);
        this.parts[13].setPos(-64F, -48F, 0F);
        this.parts[13].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[14] = new ModelPart(this, 0, 0);
        this.parts[14].setPos(-64F, -48F, 16F);
        this.parts[14].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[15] = new ModelPart(this, 0, 0);
        this.parts[15].setPos(-64F, -48F, 32F);
        this.parts[15].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[16] = new ModelPart(this, 0, 0);
        this.parts[16].setPos(-64F, -48F, 48F);
        this.parts[16].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[17] = new ModelPart(this, 0, 0);
        this.parts[17].setPos(-64F, -48F, 64F);
        this.parts[17].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[18] = new ModelPart(this, 0, 0);
        this.parts[18].setPos(-64F, -32F, -64F);
        this.parts[18].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[19] = new ModelPart(this, 0, 0);
        this.parts[19].setPos(-64F, -32F, -48F);
        this.parts[19].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[20] = new ModelPart(this, 0, 0);
        this.parts[20].setPos(-64F, -32F, -32F);
        this.parts[20].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[21] = new ModelPart(this, 0, 0);
        this.parts[21].setPos(-64F, -32F, -16F);
        this.parts[21].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[22] = new ModelPart(this, 0, 0);
        this.parts[22].setPos(-64F, -32F, 0F);
        this.parts[22].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[23] = new ModelPart(this, 0, 0);
        this.parts[23].setPos(-64F, -32F, 16F);
        this.parts[23].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[24] = new ModelPart(this, 0, 0);
        this.parts[24].setPos(-64F, -32F, 32F);
        this.parts[24].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[25] = new ModelPart(this, 0, 0);
        this.parts[25].setPos(-64F, -32F, 48F);
        this.parts[25].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[26] = new ModelPart(this, 0, 0);
        this.parts[26].setPos(-64F, -32F, 64F);
        this.parts[26].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[27] = new ModelPart(this, 0, 0);
        this.parts[27].setPos(-64F, -16F, -64F);
        this.parts[27].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[28] = new ModelPart(this, 0, 0);
        this.parts[28].setPos(-64F, -16F, -48F);
        this.parts[28].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[29] = new ModelPart(this, 0, 0);
        this.parts[29].setPos(-64F, -16F, -32F);
        this.parts[29].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[30] = new ModelPart(this, 0, 0);
        this.parts[30].setPos(-64F, -16F, -16F);
        this.parts[30].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[31] = new ModelPart(this, 0, 0);
        this.parts[31].setPos(-64F, -16F, 0F);
        this.parts[31].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[32] = new ModelPart(this, 0, 0);
        this.parts[32].setPos(-64F, -16F, 16F);
        this.parts[32].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[33] = new ModelPart(this, 0, 0);
        this.parts[33].setPos(-64F, -16F, 32F);
        this.parts[33].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[34] = new ModelPart(this, 0, 0);
        this.parts[34].setPos(-64F, -16F, 48F);
        this.parts[34].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[35] = new ModelPart(this, 0, 0);
        this.parts[35].setPos(-64F, -16F, 64F);
        this.parts[35].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[36] = new ModelPart(this, 0, 0);
        this.parts[36].setPos(-64F, 0F, -64F);
        this.parts[36].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[37] = new ModelPart(this, 0, 0);
        this.parts[37].setPos(-64F, 0F, -48F);
        this.parts[37].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[38] = new ModelPart(this, 0, 0);
        this.parts[38].setPos(-64F, 0F, -32F);
        this.parts[38].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[39] = new ModelPart(this, 0, 0);
        this.parts[39].setPos(-64F, 0F, -16F);
        this.parts[39].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[40] = new ModelPart(this, 0, 0);
        this.parts[40].setPos(-64F, 0F, 0F);
        this.parts[40].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[41] = new ModelPart(this, 0, 0);
        this.parts[41].setPos(-64F, 0F, 16F);
        this.parts[41].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[42] = new ModelPart(this, 0, 0);
        this.parts[42].setPos(-64F, 0F, 32F);
        this.parts[42].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[43] = new ModelPart(this, 0, 0);
        this.parts[43].setPos(-64F, 0F, 48F);
        this.parts[43].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[44] = new ModelPart(this, 0, 0);
        this.parts[44].setPos(-64F, 0F, 64F);
        this.parts[44].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[45] = new ModelPart(this, 0, 0);
        this.parts[45].setPos(-64F, 16F, -64F);
        this.parts[45].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[46] = new ModelPart(this, 0, 0);
        this.parts[46].setPos(-64F, 16F, -48F);
        this.parts[46].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[47] = new ModelPart(this, 0, 0);
        this.parts[47].setPos(-64F, 16F, -32F);
        this.parts[47].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[48] = new ModelPart(this, 0, 0);
        this.parts[48].setPos(-64F, 16F, -16F);
        this.parts[48].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[49] = new ModelPart(this, 0, 0);
        this.parts[49].setPos(-64F, 16F, 0F);
        this.parts[49].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[50] = new ModelPart(this, 0, 0);
        this.parts[50].setPos(-64F, 16F, 16F);
        this.parts[50].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[51] = new ModelPart(this, 0, 0);
        this.parts[51].setPos(-64F, 16F, 32F);
        this.parts[51].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[52] = new ModelPart(this, 0, 0);
        this.parts[52].setPos(-64F, 16F, 48F);
        this.parts[52].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[53] = new ModelPart(this, 0, 0);
        this.parts[53].setPos(-64F, 16F, 64F);
        this.parts[53].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[54] = new ModelPart(this, 0, 0);
        this.parts[54].setPos(-64F, 32F, -64F);
        this.parts[54].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[55] = new ModelPart(this, 0, 0);
        this.parts[55].setPos(-64F, 32F, -48F);
        this.parts[55].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[56] = new ModelPart(this, 0, 0);
        this.parts[56].setPos(-64F, 32F, -32F);
        this.parts[56].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[57] = new ModelPart(this, 0, 0);
        this.parts[57].setPos(-64F, 32F, -16F);
        this.parts[57].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[58] = new ModelPart(this, 0, 0);
        this.parts[58].setPos(-64F, 32F, 0F);
        this.parts[58].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[59] = new ModelPart(this, 0, 0);
        this.parts[59].setPos(-64F, 32F, 16F);
        this.parts[59].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[60] = new ModelPart(this, 0, 0);
        this.parts[60].setPos(-64F, 32F, 32F);
        this.parts[60].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[61] = new ModelPart(this, 0, 0);
        this.parts[61].setPos(-64F, 32F, 48F);
        this.parts[61].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[62] = new ModelPart(this, 0, 0);
        this.parts[62].setPos(-64F, 32F, 64F);
        this.parts[62].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[63] = new ModelPart(this, 0, 0);
        this.parts[63].setPos(-64F, 48F, -64F);
        this.parts[63].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[64] = new ModelPart(this, 0, 0);
        this.parts[64].setPos(-64F, 48F, -48F);
        this.parts[64].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[65] = new ModelPart(this, 0, 0);
        this.parts[65].setPos(-64F, 48F, -32F);
        this.parts[65].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[66] = new ModelPart(this, 0, 0);
        this.parts[66].setPos(-64F, 48F, -16F);
        this.parts[66].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[67] = new ModelPart(this, 0, 0);
        this.parts[67].setPos(-64F, 48F, 0F);
        this.parts[67].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[68] = new ModelPart(this, 0, 0);
        this.parts[68].setPos(-64F, 48F, 16F);
        this.parts[68].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[69] = new ModelPart(this, 0, 0);
        this.parts[69].setPos(-64F, 48F, 32F);
        this.parts[69].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[70] = new ModelPart(this, 0, 0);
        this.parts[70].setPos(-64F, 48F, 48F);
        this.parts[70].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[71] = new ModelPart(this, 0, 0);
        this.parts[71].setPos(-64F, 48F, 64F);
        this.parts[71].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[72] = new ModelPart(this, 0, 0);
        this.parts[72].setPos(-64F, 64F, -64F);
        this.parts[72].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[73] = new ModelPart(this, 0, 0);
        this.parts[73].setPos(-64F, 64F, -48F);
        this.parts[73].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[74] = new ModelPart(this, 0, 0);
        this.parts[74].setPos(-64F, 64F, -32F);
        this.parts[74].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[75] = new ModelPart(this, 0, 0);
        this.parts[75].setPos(-64F, 64F, -16F);
        this.parts[75].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[76] = new ModelPart(this, 0, 0);
        this.parts[76].setPos(-64F, 64F, 0F);
        this.parts[76].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[77] = new ModelPart(this, 0, 0);
        this.parts[77].setPos(-64F, 64F, 16F);
        this.parts[77].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[78] = new ModelPart(this, 0, 0);
        this.parts[78].setPos(-64F, 64F, 32F);
        this.parts[78].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[79] = new ModelPart(this, 0, 0);
        this.parts[79].setPos(-64F, 64F, 48F);
        this.parts[79].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[80] = new ModelPart(this, 0, 0);
        this.parts[80].setPos(-64F, 64F, 64F);
        this.parts[80].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[81] = new ModelPart(this, 0, 0);
        this.parts[81].setPos(-48F, -64F, -64F);
        this.parts[81].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[82] = new ModelPart(this, 0, 0);
        this.parts[82].setPos(-48F, -64F, -48F);
        this.parts[82].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[83] = new ModelPart(this, 0, 0);
        this.parts[83].setPos(-48F, -64F, -32F);
        this.parts[83].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[84] = new ModelPart(this, 0, 0);
        this.parts[84].setPos(-48F, -64F, -16F);
        this.parts[84].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[85] = new ModelPart(this, 0, 0);
        this.parts[85].setPos(-48F, -64F, 0F);
        this.parts[85].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[86] = new ModelPart(this, 0, 0);
        this.parts[86].setPos(-48F, -64F, 16F);
        this.parts[86].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[87] = new ModelPart(this, 0, 0);
        this.parts[87].setPos(-48F, -64F, 32F);
        this.parts[87].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[88] = new ModelPart(this, 0, 0);
        this.parts[88].setPos(-48F, -64F, 48F);
        this.parts[88].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[89] = new ModelPart(this, 0, 0);
        this.parts[89].setPos(-48F, -64F, 64F);
        this.parts[89].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[90] = new ModelPart(this, 0, 0);
        this.parts[90].setPos(-48F, -48F, -64F);
        this.parts[90].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[91] = new ModelPart(this, 0, 0);
        this.parts[91].setPos(-48F, -48F, -48F);
        this.parts[91].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[92] = new ModelPart(this, 0, 0);
        this.parts[92].setPos(-48F, -48F, -32F);
        this.parts[92].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[93] = new ModelPart(this, 0, 0);
        this.parts[93].setPos(-48F, -48F, -16F);
        this.parts[93].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[94] = new ModelPart(this, 0, 0);
        this.parts[94].setPos(-48F, -48F, 0F);
        this.parts[94].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[95] = new ModelPart(this, 0, 0);
        this.parts[95].setPos(-48F, -48F, 16F);
        this.parts[95].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[96] = new ModelPart(this, 0, 0);
        this.parts[96].setPos(-48F, -48F, 32F);
        this.parts[96].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[97] = new ModelPart(this, 0, 0);
        this.parts[97].setPos(-48F, -48F, 48F);
        this.parts[97].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[98] = new ModelPart(this, 0, 0);
        this.parts[98].setPos(-48F, -48F, 64F);
        this.parts[98].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[99] = new ModelPart(this, 0, 0);
        this.parts[99].setPos(-48F, -32F, -64F);
        this.parts[99].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[100] = new ModelPart(this, 0, 0);
        this.parts[100].setPos(-48F, -32F, -48F);
        this.parts[100].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[101] = new ModelPart(this, 0, 0);
        this.parts[101].setPos(-48F, -32F, -32F);
        this.parts[101].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[102] = new ModelPart(this, 0, 0);
        this.parts[102].setPos(-48F, -32F, -16F);
        this.parts[102].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[103] = new ModelPart(this, 0, 0);
        this.parts[103].setPos(-48F, -32F, 0F);
        this.parts[103].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[104] = new ModelPart(this, 0, 0);
        this.parts[104].setPos(-48F, -32F, 16F);
        this.parts[104].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[105] = new ModelPart(this, 0, 0);
        this.parts[105].setPos(-48F, -32F, 32F);
        this.parts[105].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[106] = new ModelPart(this, 0, 0);
        this.parts[106].setPos(-48F, -32F, 48F);
        this.parts[106].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[107] = new ModelPart(this, 0, 0);
        this.parts[107].setPos(-48F, -32F, 64F);
        this.parts[107].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[108] = new ModelPart(this, 0, 0);
        this.parts[108].setPos(-48F, -16F, -64F);
        this.parts[108].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[109] = new ModelPart(this, 0, 0);
        this.parts[109].setPos(-48F, -16F, -48F);
        this.parts[109].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[110] = new ModelPart(this, 0, 0);
        this.parts[110].setPos(-48F, -16F, -32F);
        this.parts[110].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[111] = new ModelPart(this, 0, 0);
        this.parts[111].setPos(-48F, -16F, -16F);
        this.parts[111].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[112] = new ModelPart(this, 0, 0);
        this.parts[112].setPos(-48F, -16F, 0F);
        this.parts[112].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[113] = new ModelPart(this, 0, 0);
        this.parts[113].setPos(-48F, -16F, 16F);
        this.parts[113].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[114] = new ModelPart(this, 0, 0);
        this.parts[114].setPos(-48F, -16F, 32F);
        this.parts[114].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[115] = new ModelPart(this, 0, 0);
        this.parts[115].setPos(-48F, -16F, 48F);
        this.parts[115].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[116] = new ModelPart(this, 0, 0);
        this.parts[116].setPos(-48F, -16F, 64F);
        this.parts[116].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[117] = new ModelPart(this, 0, 0);
        this.parts[117].setPos(-48F, 0F, -64F);
        this.parts[117].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[118] = new ModelPart(this, 0, 0);
        this.parts[118].setPos(-48F, 0F, -48F);
        this.parts[118].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[119] = new ModelPart(this, 0, 0);
        this.parts[119].setPos(-48F, 0F, -32F);
        this.parts[119].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[120] = new ModelPart(this, 0, 0);
        this.parts[120].setPos(-48F, 0F, -16F);
        this.parts[120].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[121] = new ModelPart(this, 0, 0);
        this.parts[121].setPos(-48F, 0F, 0F);
        this.parts[121].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[122] = new ModelPart(this, 0, 0);
        this.parts[122].setPos(-48F, 0F, 16F);
        this.parts[122].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[123] = new ModelPart(this, 0, 0);
        this.parts[123].setPos(-48F, 0F, 32F);
        this.parts[123].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[124] = new ModelPart(this, 0, 0);
        this.parts[124].setPos(-48F, 0F, 48F);
        this.parts[124].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[125] = new ModelPart(this, 0, 0);
        this.parts[125].setPos(-48F, 0F, 64F);
        this.parts[125].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[126] = new ModelPart(this, 0, 0);
        this.parts[126].setPos(-48F, 16F, -64F);
        this.parts[126].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[127] = new ModelPart(this, 0, 0);
        this.parts[127].setPos(-48F, 16F, -48F);
        this.parts[127].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[128] = new ModelPart(this, 0, 0);
        this.parts[128].setPos(-48F, 16F, -32F);
        this.parts[128].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[129] = new ModelPart(this, 0, 0);
        this.parts[129].setPos(-48F, 16F, -16F);
        this.parts[129].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[130] = new ModelPart(this, 0, 0);
        this.parts[130].setPos(-48F, 16F, 0F);
        this.parts[130].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[131] = new ModelPart(this, 0, 0);
        this.parts[131].setPos(-48F, 16F, 16F);
        this.parts[131].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[132] = new ModelPart(this, 0, 0);
        this.parts[132].setPos(-48F, 16F, 32F);
        this.parts[132].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[133] = new ModelPart(this, 0, 0);
        this.parts[133].setPos(-48F, 16F, 48F);
        this.parts[133].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[134] = new ModelPart(this, 0, 0);
        this.parts[134].setPos(-48F, 16F, 64F);
        this.parts[134].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[135] = new ModelPart(this, 0, 0);
        this.parts[135].setPos(-48F, 32F, -64F);
        this.parts[135].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[136] = new ModelPart(this, 0, 0);
        this.parts[136].setPos(-48F, 32F, -48F);
        this.parts[136].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[137] = new ModelPart(this, 0, 0);
        this.parts[137].setPos(-48F, 32F, -32F);
        this.parts[137].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[138] = new ModelPart(this, 0, 0);
        this.parts[138].setPos(-48F, 32F, -16F);
        this.parts[138].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[139] = new ModelPart(this, 0, 0);
        this.parts[139].setPos(-48F, 32F, 0F);
        this.parts[139].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[140] = new ModelPart(this, 0, 0);
        this.parts[140].setPos(-48F, 32F, 16F);
        this.parts[140].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[141] = new ModelPart(this, 0, 0);
        this.parts[141].setPos(-48F, 32F, 32F);
        this.parts[141].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[142] = new ModelPart(this, 0, 0);
        this.parts[142].setPos(-48F, 32F, 48F);
        this.parts[142].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[143] = new ModelPart(this, 0, 0);
        this.parts[143].setPos(-48F, 32F, 64F);
        this.parts[143].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[144] = new ModelPart(this, 0, 0);
        this.parts[144].setPos(-48F, 48F, -64F);
        this.parts[144].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[145] = new ModelPart(this, 0, 0);
        this.parts[145].setPos(-48F, 48F, -48F);
        this.parts[145].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[146] = new ModelPart(this, 0, 0);
        this.parts[146].setPos(-48F, 48F, -32F);
        this.parts[146].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[147] = new ModelPart(this, 0, 0);
        this.parts[147].setPos(-48F, 48F, -16F);
        this.parts[147].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[148] = new ModelPart(this, 0, 0);
        this.parts[148].setPos(-48F, 48F, 0F);
        this.parts[148].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[149] = new ModelPart(this, 0, 0);
        this.parts[149].setPos(-48F, 48F, 16F);
        this.parts[149].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[150] = new ModelPart(this, 0, 0);
        this.parts[150].setPos(-48F, 48F, 32F);
        this.parts[150].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[151] = new ModelPart(this, 0, 0);
        this.parts[151].setPos(-48F, 48F, 48F);
        this.parts[151].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[152] = new ModelPart(this, 0, 0);
        this.parts[152].setPos(-48F, 48F, 64F);
        this.parts[152].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[153] = new ModelPart(this, 0, 0);
        this.parts[153].setPos(-48F, 64F, -64F);
        this.parts[153].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[154] = new ModelPart(this, 0, 0);
        this.parts[154].setPos(-48F, 64F, -48F);
        this.parts[154].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[155] = new ModelPart(this, 0, 0);
        this.parts[155].setPos(-48F, 64F, -32F);
        this.parts[155].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[156] = new ModelPart(this, 0, 0);
        this.parts[156].setPos(-48F, 64F, -16F);
        this.parts[156].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[157] = new ModelPart(this, 0, 0);
        this.parts[157].setPos(-48F, 64F, 0F);
        this.parts[157].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[158] = new ModelPart(this, 0, 0);
        this.parts[158].setPos(-48F, 64F, 16F);
        this.parts[158].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[159] = new ModelPart(this, 0, 0);
        this.parts[159].setPos(-48F, 64F, 32F);
        this.parts[159].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[160] = new ModelPart(this, 0, 0);
        this.parts[160].setPos(-48F, 64F, 48F);
        this.parts[160].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[161] = new ModelPart(this, 0, 0);
        this.parts[161].setPos(-48F, 64F, 64F);
        this.parts[161].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[162] = new ModelPart(this, 0, 0);
        this.parts[162].setPos(-32F, -64F, -64F);
        this.parts[162].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[163] = new ModelPart(this, 0, 0);
        this.parts[163].setPos(-32F, -64F, -48F);
        this.parts[163].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[164] = new ModelPart(this, 0, 0);
        this.parts[164].setPos(-32F, -64F, -32F);
        this.parts[164].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[165] = new ModelPart(this, 0, 0);
        this.parts[165].setPos(-32F, -64F, -16F);
        this.parts[165].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[166] = new ModelPart(this, 0, 0);
        this.parts[166].setPos(-32F, -64F, 0F);
        this.parts[166].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[167] = new ModelPart(this, 0, 0);
        this.parts[167].setPos(-32F, -64F, 16F);
        this.parts[167].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[168] = new ModelPart(this, 0, 0);
        this.parts[168].setPos(-32F, -64F, 32F);
        this.parts[168].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[169] = new ModelPart(this, 0, 0);
        this.parts[169].setPos(-32F, -64F, 48F);
        this.parts[169].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[170] = new ModelPart(this, 0, 0);
        this.parts[170].setPos(-32F, -64F, 64F);
        this.parts[170].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[171] = new ModelPart(this, 0, 0);
        this.parts[171].setPos(-32F, -48F, -64F);
        this.parts[171].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[172] = new ModelPart(this, 0, 0);
        this.parts[172].setPos(-32F, -48F, -48F);
        this.parts[172].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[173] = new ModelPart(this, 0, 0);
        this.parts[173].setPos(-32F, -48F, -32F);
        this.parts[173].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[174] = new ModelPart(this, 0, 0);
        this.parts[174].setPos(-32F, -48F, -16F);
        this.parts[174].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[175] = new ModelPart(this, 0, 0);
        this.parts[175].setPos(-32F, -48F, 0F);
        this.parts[175].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[176] = new ModelPart(this, 0, 0);
        this.parts[176].setPos(-32F, -48F, 16F);
        this.parts[176].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[177] = new ModelPart(this, 0, 0);
        this.parts[177].setPos(-32F, -48F, 32F);
        this.parts[177].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[178] = new ModelPart(this, 0, 0);
        this.parts[178].setPos(-32F, -48F, 48F);
        this.parts[178].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[179] = new ModelPart(this, 0, 0);
        this.parts[179].setPos(-32F, -48F, 64F);
        this.parts[179].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[180] = new ModelPart(this, 0, 0);
        this.parts[180].setPos(-32F, -32F, -64F);
        this.parts[180].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[181] = new ModelPart(this, 0, 0);
        this.parts[181].setPos(-32F, -32F, -48F);
        this.parts[181].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[182] = new ModelPart(this, 0, 0);
        this.parts[182].setPos(-32F, -32F, -32F);
        this.parts[182].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[183] = new ModelPart(this, 0, 0);
        this.parts[183].setPos(-32F, -32F, -16F);
        this.parts[183].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[184] = new ModelPart(this, 0, 0);
        this.parts[184].setPos(-32F, -32F, 0F);
        this.parts[184].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[185] = new ModelPart(this, 0, 0);
        this.parts[185].setPos(-32F, -32F, 16F);
        this.parts[185].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[186] = new ModelPart(this, 0, 0);
        this.parts[186].setPos(-32F, -32F, 32F);
        this.parts[186].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[187] = new ModelPart(this, 0, 0);
        this.parts[187].setPos(-32F, -32F, 48F);
        this.parts[187].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[188] = new ModelPart(this, 0, 0);
        this.parts[188].setPos(-32F, -32F, 64F);
        this.parts[188].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[189] = new ModelPart(this, 0, 0);
        this.parts[189].setPos(-32F, -16F, -64F);
        this.parts[189].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[190] = new ModelPart(this, 0, 0);
        this.parts[190].setPos(-32F, -16F, -48F);
        this.parts[190].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[191] = new ModelPart(this, 0, 0);
        this.parts[191].setPos(-32F, -16F, -32F);
        this.parts[191].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[192] = new ModelPart(this, 0, 0);
        this.parts[192].setPos(-32F, -16F, -16F);
        this.parts[192].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[193] = new ModelPart(this, 0, 0);
        this.parts[193].setPos(-32F, -16F, 0F);
        this.parts[193].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[194] = new ModelPart(this, 0, 0);
        this.parts[194].setPos(-32F, -16F, 16F);
        this.parts[194].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[195] = new ModelPart(this, 0, 0);
        this.parts[195].setPos(-32F, -16F, 32F);
        this.parts[195].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[196] = new ModelPart(this, 0, 0);
        this.parts[196].setPos(-32F, -16F, 48F);
        this.parts[196].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[197] = new ModelPart(this, 0, 0);
        this.parts[197].setPos(-32F, -16F, 64F);
        this.parts[197].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[198] = new ModelPart(this, 0, 0);
        this.parts[198].setPos(-32F, 0F, -64F);
        this.parts[198].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[199] = new ModelPart(this, 0, 0);
        this.parts[199].setPos(-32F, 0F, -48F);
        this.parts[199].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[200] = new ModelPart(this, 0, 0);
        this.parts[200].setPos(-32F, 0F, -32F);
        this.parts[200].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[201] = new ModelPart(this, 0, 0);
        this.parts[201].setPos(-32F, 0F, -16F);
        this.parts[201].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[202] = new ModelPart(this, 0, 0);
        this.parts[202].setPos(-32F, 0F, 0F);
        this.parts[202].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[203] = new ModelPart(this, 0, 0);
        this.parts[203].setPos(-32F, 0F, 16F);
        this.parts[203].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[204] = new ModelPart(this, 0, 0);
        this.parts[204].setPos(-32F, 0F, 32F);
        this.parts[204].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[205] = new ModelPart(this, 0, 0);
        this.parts[205].setPos(-32F, 0F, 48F);
        this.parts[205].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[206] = new ModelPart(this, 0, 0);
        this.parts[206].setPos(-32F, 0F, 64F);
        this.parts[206].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[207] = new ModelPart(this, 0, 0);
        this.parts[207].setPos(-32F, 16F, -64F);
        this.parts[207].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[208] = new ModelPart(this, 0, 0);
        this.parts[208].setPos(-32F, 16F, -48F);
        this.parts[208].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[209] = new ModelPart(this, 0, 0);
        this.parts[209].setPos(-32F, 16F, -32F);
        this.parts[209].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[210] = new ModelPart(this, 0, 0);
        this.parts[210].setPos(-32F, 16F, -16F);
        this.parts[210].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[211] = new ModelPart(this, 0, 0);
        this.parts[211].setPos(-32F, 16F, 0F);
        this.parts[211].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[212] = new ModelPart(this, 0, 0);
        this.parts[212].setPos(-32F, 16F, 16F);
        this.parts[212].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[213] = new ModelPart(this, 0, 0);
        this.parts[213].setPos(-32F, 16F, 32F);
        this.parts[213].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[214] = new ModelPart(this, 0, 0);
        this.parts[214].setPos(-32F, 16F, 48F);
        this.parts[214].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[215] = new ModelPart(this, 0, 0);
        this.parts[215].setPos(-32F, 16F, 64F);
        this.parts[215].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[216] = new ModelPart(this, 0, 0);
        this.parts[216].setPos(-32F, 32F, -64F);
        this.parts[216].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[217] = new ModelPart(this, 0, 0);
        this.parts[217].setPos(-32F, 32F, -48F);
        this.parts[217].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[218] = new ModelPart(this, 0, 0);
        this.parts[218].setPos(-32F, 32F, -32F);
        this.parts[218].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[219] = new ModelPart(this, 0, 0);
        this.parts[219].setPos(-32F, 32F, -16F);
        this.parts[219].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[220] = new ModelPart(this, 0, 0);
        this.parts[220].setPos(-32F, 32F, 0F);
        this.parts[220].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[221] = new ModelPart(this, 0, 0);
        this.parts[221].setPos(-32F, 32F, 16F);
        this.parts[221].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[222] = new ModelPart(this, 0, 0);
        this.parts[222].setPos(-32F, 32F, 32F);
        this.parts[222].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[223] = new ModelPart(this, 0, 0);
        this.parts[223].setPos(-32F, 32F, 48F);
        this.parts[223].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[224] = new ModelPart(this, 0, 0);
        this.parts[224].setPos(-32F, 32F, 64F);
        this.parts[224].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[225] = new ModelPart(this, 0, 0);
        this.parts[225].setPos(-32F, 48F, -64F);
        this.parts[225].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[226] = new ModelPart(this, 0, 0);
        this.parts[226].setPos(-32F, 48F, -48F);
        this.parts[226].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[227] = new ModelPart(this, 0, 0);
        this.parts[227].setPos(-32F, 48F, -32F);
        this.parts[227].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[228] = new ModelPart(this, 0, 0);
        this.parts[228].setPos(-32F, 48F, -16F);
        this.parts[228].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[229] = new ModelPart(this, 0, 0);
        this.parts[229].setPos(-32F, 48F, 0F);
        this.parts[229].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[230] = new ModelPart(this, 0, 0);
        this.parts[230].setPos(-32F, 48F, 16F);
        this.parts[230].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[231] = new ModelPart(this, 0, 0);
        this.parts[231].setPos(-32F, 48F, 32F);
        this.parts[231].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[232] = new ModelPart(this, 0, 0);
        this.parts[232].setPos(-32F, 48F, 48F);
        this.parts[232].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[233] = new ModelPart(this, 0, 0);
        this.parts[233].setPos(-32F, 48F, 64F);
        this.parts[233].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[234] = new ModelPart(this, 0, 0);
        this.parts[234].setPos(-32F, 64F, -64F);
        this.parts[234].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[235] = new ModelPart(this, 0, 0);
        this.parts[235].setPos(-32F, 64F, -48F);
        this.parts[235].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[236] = new ModelPart(this, 0, 0);
        this.parts[236].setPos(-32F, 64F, -32F);
        this.parts[236].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[237] = new ModelPart(this, 0, 0);
        this.parts[237].setPos(-32F, 64F, -16F);
        this.parts[237].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[238] = new ModelPart(this, 0, 0);
        this.parts[238].setPos(-32F, 64F, 0F);
        this.parts[238].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[239] = new ModelPart(this, 0, 0);
        this.parts[239].setPos(-32F, 64F, 16F);
        this.parts[239].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[240] = new ModelPart(this, 0, 0);
        this.parts[240].setPos(-32F, 64F, 32F);
        this.parts[240].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[241] = new ModelPart(this, 0, 0);
        this.parts[241].setPos(-32F, 64F, 48F);
        this.parts[241].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[242] = new ModelPart(this, 0, 0);
        this.parts[242].setPos(-32F, 64F, 64F);
        this.parts[242].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[243] = new ModelPart(this, 0, 0);
        this.parts[243].setPos(-16F, -64F, -64F);
        this.parts[243].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[244] = new ModelPart(this, 0, 0);
        this.parts[244].setPos(-16F, -64F, -48F);
        this.parts[244].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[245] = new ModelPart(this, 0, 0);
        this.parts[245].setPos(-16F, -64F, -32F);
        this.parts[245].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[246] = new ModelPart(this, 0, 0);
        this.parts[246].setPos(-16F, -64F, -16F);
        this.parts[246].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[247] = new ModelPart(this, 0, 0);
        this.parts[247].setPos(-16F, -64F, 0F);
        this.parts[247].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[248] = new ModelPart(this, 0, 0);
        this.parts[248].setPos(-16F, -64F, 16F);
        this.parts[248].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[249] = new ModelPart(this, 0, 0);
        this.parts[249].setPos(-16F, -64F, 32F);
        this.parts[249].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[250] = new ModelPart(this, 0, 0);
        this.parts[250].setPos(-16F, -64F, 48F);
        this.parts[250].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[251] = new ModelPart(this, 0, 0);
        this.parts[251].setPos(-16F, -64F, 64F);
        this.parts[251].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[252] = new ModelPart(this, 0, 0);
        this.parts[252].setPos(-16F, -48F, -64F);
        this.parts[252].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[253] = new ModelPart(this, 0, 0);
        this.parts[253].setPos(-16F, -48F, -48F);
        this.parts[253].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[254] = new ModelPart(this, 0, 0);
        this.parts[254].setPos(-16F, -48F, -32F);
        this.parts[254].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[255] = new ModelPart(this, 0, 0);
        this.parts[255].setPos(-16F, -48F, -16F);
        this.parts[255].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[256] = new ModelPart(this, 0, 0);
        this.parts[256].setPos(-16F, -48F, 0F);
        this.parts[256].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[257] = new ModelPart(this, 0, 0);
        this.parts[257].setPos(-16F, -48F, 16F);
        this.parts[257].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[258] = new ModelPart(this, 0, 0);
        this.parts[258].setPos(-16F, -48F, 32F);
        this.parts[258].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[259] = new ModelPart(this, 0, 0);
        this.parts[259].setPos(-16F, -48F, 48F);
        this.parts[259].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[260] = new ModelPart(this, 0, 0);
        this.parts[260].setPos(-16F, -48F, 64F);
        this.parts[260].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[261] = new ModelPart(this, 0, 0);
        this.parts[261].setPos(-16F, -32F, -64F);
        this.parts[261].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[262] = new ModelPart(this, 0, 0);
        this.parts[262].setPos(-16F, -32F, -48F);
        this.parts[262].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[263] = new ModelPart(this, 0, 0);
        this.parts[263].setPos(-16F, -32F, -32F);
        this.parts[263].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[264] = new ModelPart(this, 0, 0);
        this.parts[264].setPos(-16F, -32F, -16F);
        this.parts[264].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[265] = new ModelPart(this, 0, 0);
        this.parts[265].setPos(-16F, -32F, 0F);
        this.parts[265].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[266] = new ModelPart(this, 0, 0);
        this.parts[266].setPos(-16F, -32F, 16F);
        this.parts[266].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[267] = new ModelPart(this, 0, 0);
        this.parts[267].setPos(-16F, -32F, 32F);
        this.parts[267].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[268] = new ModelPart(this, 0, 0);
        this.parts[268].setPos(-16F, -32F, 48F);
        this.parts[268].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[269] = new ModelPart(this, 0, 0);
        this.parts[269].setPos(-16F, -32F, 64F);
        this.parts[269].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[270] = new ModelPart(this, 0, 0);
        this.parts[270].setPos(-16F, -16F, -64F);
        this.parts[270].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[271] = new ModelPart(this, 0, 0);
        this.parts[271].setPos(-16F, -16F, -48F);
        this.parts[271].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[272] = new ModelPart(this, 0, 0);
        this.parts[272].setPos(-16F, -16F, -32F);
        this.parts[272].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[273] = new ModelPart(this, 0, 0);
        this.parts[273].setPos(-16F, -16F, -16F);
        this.parts[273].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[274] = new ModelPart(this, 0, 0);
        this.parts[274].setPos(-16F, -16F, 0F);
        this.parts[274].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[275] = new ModelPart(this, 0, 0);
        this.parts[275].setPos(-16F, -16F, 16F);
        this.parts[275].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[276] = new ModelPart(this, 0, 0);
        this.parts[276].setPos(-16F, -16F, 32F);
        this.parts[276].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[277] = new ModelPart(this, 0, 0);
        this.parts[277].setPos(-16F, -16F, 48F);
        this.parts[277].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[278] = new ModelPart(this, 0, 0);
        this.parts[278].setPos(-16F, -16F, 64F);
        this.parts[278].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[279] = new ModelPart(this, 0, 0);
        this.parts[279].setPos(-16F, 0F, -64F);
        this.parts[279].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[280] = new ModelPart(this, 0, 0);
        this.parts[280].setPos(-16F, 0F, -48F);
        this.parts[280].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[281] = new ModelPart(this, 0, 0);
        this.parts[281].setPos(-16F, 0F, -32F);
        this.parts[281].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[282] = new ModelPart(this, 0, 0);
        this.parts[282].setPos(-16F, 0F, -16F);
        this.parts[282].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[283] = new ModelPart(this, 0, 0);
        this.parts[283].setPos(-16F, 0F, 0F);
        this.parts[283].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[284] = new ModelPart(this, 0, 0);
        this.parts[284].setPos(-16F, 0F, 16F);
        this.parts[284].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[285] = new ModelPart(this, 0, 0);
        this.parts[285].setPos(-16F, 0F, 32F);
        this.parts[285].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[286] = new ModelPart(this, 0, 0);
        this.parts[286].setPos(-16F, 0F, 48F);
        this.parts[286].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[287] = new ModelPart(this, 0, 0);
        this.parts[287].setPos(-16F, 0F, 64F);
        this.parts[287].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[288] = new ModelPart(this, 0, 0);
        this.parts[288].setPos(-16F, 16F, -64F);
        this.parts[288].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[289] = new ModelPart(this, 0, 0);
        this.parts[289].setPos(-16F, 16F, -48F);
        this.parts[289].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[290] = new ModelPart(this, 0, 0);
        this.parts[290].setPos(-16F, 16F, -32F);
        this.parts[290].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[291] = new ModelPart(this, 0, 0);
        this.parts[291].setPos(-16F, 16F, -16F);
        this.parts[291].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[292] = new ModelPart(this, 0, 0);
        this.parts[292].setPos(-16F, 16F, 0F);
        this.parts[292].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[293] = new ModelPart(this, 0, 0);
        this.parts[293].setPos(-16F, 16F, 16F);
        this.parts[293].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[294] = new ModelPart(this, 0, 0);
        this.parts[294].setPos(-16F, 16F, 32F);
        this.parts[294].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[295] = new ModelPart(this, 0, 0);
        this.parts[295].setPos(-16F, 16F, 48F);
        this.parts[295].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[296] = new ModelPart(this, 0, 0);
        this.parts[296].setPos(-16F, 16F, 64F);
        this.parts[296].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[297] = new ModelPart(this, 0, 0);
        this.parts[297].setPos(-16F, 32F, -64F);
        this.parts[297].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[298] = new ModelPart(this, 0, 0);
        this.parts[298].setPos(-16F, 32F, -48F);
        this.parts[298].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[299] = new ModelPart(this, 0, 0);
        this.parts[299].setPos(-16F, 32F, -32F);
        this.parts[299].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[300] = new ModelPart(this, 0, 0);
        this.parts[300].setPos(-16F, 32F, -16F);
        this.parts[300].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[301] = new ModelPart(this, 0, 0);
        this.parts[301].setPos(-16F, 32F, 0F);
        this.parts[301].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[302] = new ModelPart(this, 0, 0);
        this.parts[302].setPos(-16F, 32F, 16F);
        this.parts[302].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[303] = new ModelPart(this, 0, 0);
        this.parts[303].setPos(-16F, 32F, 32F);
        this.parts[303].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[304] = new ModelPart(this, 0, 0);
        this.parts[304].setPos(-16F, 32F, 48F);
        this.parts[304].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[305] = new ModelPart(this, 0, 0);
        this.parts[305].setPos(-16F, 32F, 64F);
        this.parts[305].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[306] = new ModelPart(this, 0, 0);
        this.parts[306].setPos(-16F, 48F, -64F);
        this.parts[306].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[307] = new ModelPart(this, 0, 0);
        this.parts[307].setPos(-16F, 48F, -48F);
        this.parts[307].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[308] = new ModelPart(this, 0, 0);
        this.parts[308].setPos(-16F, 48F, -32F);
        this.parts[308].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[309] = new ModelPart(this, 0, 0);
        this.parts[309].setPos(-16F, 48F, -16F);
        this.parts[309].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[310] = new ModelPart(this, 0, 0);
        this.parts[310].setPos(-16F, 48F, 0F);
        this.parts[310].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[311] = new ModelPart(this, 0, 0);
        this.parts[311].setPos(-16F, 48F, 16F);
        this.parts[311].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[312] = new ModelPart(this, 0, 0);
        this.parts[312].setPos(-16F, 48F, 32F);
        this.parts[312].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[313] = new ModelPart(this, 0, 0);
        this.parts[313].setPos(-16F, 48F, 48F);
        this.parts[313].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[314] = new ModelPart(this, 0, 0);
        this.parts[314].setPos(-16F, 48F, 64F);
        this.parts[314].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[315] = new ModelPart(this, 0, 0);
        this.parts[315].setPos(-16F, 64F, -64F);
        this.parts[315].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[316] = new ModelPart(this, 0, 0);
        this.parts[316].setPos(-16F, 64F, -48F);
        this.parts[316].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[317] = new ModelPart(this, 0, 0);
        this.parts[317].setPos(-16F, 64F, -32F);
        this.parts[317].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[318] = new ModelPart(this, 0, 0);
        this.parts[318].setPos(-16F, 64F, -16F);
        this.parts[318].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[319] = new ModelPart(this, 0, 0);
        this.parts[319].setPos(-16F, 64F, 0F);
        this.parts[319].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[320] = new ModelPart(this, 0, 0);
        this.parts[320].setPos(-16F, 64F, 16F);
        this.parts[320].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[321] = new ModelPart(this, 0, 0);
        this.parts[321].setPos(-16F, 64F, 32F);
        this.parts[321].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[322] = new ModelPart(this, 0, 0);
        this.parts[322].setPos(-16F, 64F, 48F);
        this.parts[322].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[323] = new ModelPart(this, 0, 0);
        this.parts[323].setPos(-16F, 64F, 64F);
        this.parts[323].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[324] = new ModelPart(this, 0, 0);
        this.parts[324].setPos(0F, -64F, -64F);
        this.parts[324].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[325] = new ModelPart(this, 0, 0);
        this.parts[325].setPos(0F, -64F, -48F);
        this.parts[325].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[326] = new ModelPart(this, 0, 0);
        this.parts[326].setPos(0F, -64F, -32F);
        this.parts[326].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[327] = new ModelPart(this, 0, 0);
        this.parts[327].setPos(0F, -64F, -16F);
        this.parts[327].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[328] = new ModelPart(this, 0, 0);
        this.parts[328].setPos(0F, -64F, 0F);
        this.parts[328].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[329] = new ModelPart(this, 0, 0);
        this.parts[329].setPos(0F, -64F, 16F);
        this.parts[329].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[330] = new ModelPart(this, 0, 0);
        this.parts[330].setPos(0F, -64F, 32F);
        this.parts[330].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[331] = new ModelPart(this, 0, 0);
        this.parts[331].setPos(0F, -64F, 48F);
        this.parts[331].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[332] = new ModelPart(this, 0, 0);
        this.parts[332].setPos(0F, -64F, 64F);
        this.parts[332].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[333] = new ModelPart(this, 0, 0);
        this.parts[333].setPos(0F, -48F, -64F);
        this.parts[333].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[334] = new ModelPart(this, 0, 0);
        this.parts[334].setPos(0F, -48F, -48F);
        this.parts[334].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[335] = new ModelPart(this, 0, 0);
        this.parts[335].setPos(0F, -48F, -32F);
        this.parts[335].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[336] = new ModelPart(this, 0, 0);
        this.parts[336].setPos(0F, -48F, -16F);
        this.parts[336].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[337] = new ModelPart(this, 0, 0);
        this.parts[337].setPos(0F, -48F, 0F);
        this.parts[337].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[338] = new ModelPart(this, 0, 0);
        this.parts[338].setPos(0F, -48F, 16F);
        this.parts[338].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[339] = new ModelPart(this, 0, 0);
        this.parts[339].setPos(0F, -48F, 32F);
        this.parts[339].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[340] = new ModelPart(this, 0, 0);
        this.parts[340].setPos(0F, -48F, 48F);
        this.parts[340].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[341] = new ModelPart(this, 0, 0);
        this.parts[341].setPos(0F, -48F, 64F);
        this.parts[341].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[342] = new ModelPart(this, 0, 0);
        this.parts[342].setPos(0F, -32F, -64F);
        this.parts[342].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[343] = new ModelPart(this, 0, 0);
        this.parts[343].setPos(0F, -32F, -48F);
        this.parts[343].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[344] = new ModelPart(this, 0, 0);
        this.parts[344].setPos(0F, -32F, -32F);
        this.parts[344].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[345] = new ModelPart(this, 0, 0);
        this.parts[345].setPos(0F, -32F, -16F);
        this.parts[345].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[346] = new ModelPart(this, 0, 0);
        this.parts[346].setPos(0F, -32F, 0F);
        this.parts[346].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[347] = new ModelPart(this, 0, 0);
        this.parts[347].setPos(0F, -32F, 16F);
        this.parts[347].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[348] = new ModelPart(this, 0, 0);
        this.parts[348].setPos(0F, -32F, 32F);
        this.parts[348].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[349] = new ModelPart(this, 0, 0);
        this.parts[349].setPos(0F, -32F, 48F);
        this.parts[349].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[350] = new ModelPart(this, 0, 0);
        this.parts[350].setPos(0F, -32F, 64F);
        this.parts[350].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[351] = new ModelPart(this, 0, 0);
        this.parts[351].setPos(0F, -16F, -64F);
        this.parts[351].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[352] = new ModelPart(this, 0, 0);
        this.parts[352].setPos(0F, -16F, -48F);
        this.parts[352].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[353] = new ModelPart(this, 0, 0);
        this.parts[353].setPos(0F, -16F, -32F);
        this.parts[353].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[354] = new ModelPart(this, 0, 0);
        this.parts[354].setPos(0F, -16F, -16F);
        this.parts[354].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[355] = new ModelPart(this, 0, 0);
        this.parts[355].setPos(0F, -16F, 0F);
        this.parts[355].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[356] = new ModelPart(this, 0, 0);
        this.parts[356].setPos(0F, -16F, 16F);
        this.parts[356].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[357] = new ModelPart(this, 0, 0);
        this.parts[357].setPos(0F, -16F, 32F);
        this.parts[357].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[358] = new ModelPart(this, 0, 0);
        this.parts[358].setPos(0F, -16F, 48F);
        this.parts[358].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[359] = new ModelPart(this, 0, 0);
        this.parts[359].setPos(0F, -16F, 64F);
        this.parts[359].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[360] = new ModelPart(this, 0, 0);
        this.parts[360].setPos(0F, 0F, -64F);
        this.parts[360].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[361] = new ModelPart(this, 0, 0);
        this.parts[361].setPos(0F, 0F, -48F);
        this.parts[361].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[362] = new ModelPart(this, 0, 0);
        this.parts[362].setPos(0F, 0F, -32F);
        this.parts[362].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[363] = new ModelPart(this, 0, 0);
        this.parts[363].setPos(0F, 0F, -16F);
        this.parts[363].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[364] = new ModelPart(this, 0, 0);
        this.parts[364].setPos(0F, 0F, 0F);
        this.parts[364].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[365] = new ModelPart(this, 0, 0);
        this.parts[365].setPos(0F, 0F, 16F);
        this.parts[365].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[366] = new ModelPart(this, 0, 0);
        this.parts[366].setPos(0F, 0F, 32F);
        this.parts[366].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[367] = new ModelPart(this, 0, 0);
        this.parts[367].setPos(0F, 0F, 48F);
        this.parts[367].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[368] = new ModelPart(this, 0, 0);
        this.parts[368].setPos(0F, 0F, 64F);
        this.parts[368].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[369] = new ModelPart(this, 0, 0);
        this.parts[369].setPos(0F, 16F, -64F);
        this.parts[369].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[370] = new ModelPart(this, 0, 0);
        this.parts[370].setPos(0F, 16F, -48F);
        this.parts[370].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[371] = new ModelPart(this, 0, 0);
        this.parts[371].setPos(0F, 16F, -32F);
        this.parts[371].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[372] = new ModelPart(this, 0, 0);
        this.parts[372].setPos(0F, 16F, -16F);
        this.parts[372].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[373] = new ModelPart(this, 0, 0);
        this.parts[373].setPos(0F, 16F, 0F);
        this.parts[373].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[374] = new ModelPart(this, 0, 0);
        this.parts[374].setPos(0F, 16F, 16F);
        this.parts[374].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[375] = new ModelPart(this, 0, 0);
        this.parts[375].setPos(0F, 16F, 32F);
        this.parts[375].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[376] = new ModelPart(this, 0, 0);
        this.parts[376].setPos(0F, 16F, 48F);
        this.parts[376].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[377] = new ModelPart(this, 0, 0);
        this.parts[377].setPos(0F, 16F, 64F);
        this.parts[377].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[378] = new ModelPart(this, 0, 0);
        this.parts[378].setPos(0F, 32F, -64F);
        this.parts[378].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[379] = new ModelPart(this, 0, 0);
        this.parts[379].setPos(0F, 32F, -48F);
        this.parts[379].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[380] = new ModelPart(this, 0, 0);
        this.parts[380].setPos(0F, 32F, -32F);
        this.parts[380].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[381] = new ModelPart(this, 0, 0);
        this.parts[381].setPos(0F, 32F, -16F);
        this.parts[381].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[382] = new ModelPart(this, 0, 0);
        this.parts[382].setPos(0F, 32F, 0F);
        this.parts[382].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[383] = new ModelPart(this, 0, 0);
        this.parts[383].setPos(0F, 32F, 16F);
        this.parts[383].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[384] = new ModelPart(this, 0, 0);
        this.parts[384].setPos(0F, 32F, 32F);
        this.parts[384].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[385] = new ModelPart(this, 0, 0);
        this.parts[385].setPos(0F, 32F, 48F);
        this.parts[385].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[386] = new ModelPart(this, 0, 0);
        this.parts[386].setPos(0F, 32F, 64F);
        this.parts[386].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[387] = new ModelPart(this, 0, 0);
        this.parts[387].setPos(0F, 48F, -64F);
        this.parts[387].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[388] = new ModelPart(this, 0, 0);
        this.parts[388].setPos(0F, 48F, -48F);
        this.parts[388].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[389] = new ModelPart(this, 0, 0);
        this.parts[389].setPos(0F, 48F, -32F);
        this.parts[389].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[390] = new ModelPart(this, 0, 0);
        this.parts[390].setPos(0F, 48F, -16F);
        this.parts[390].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[391] = new ModelPart(this, 0, 0);
        this.parts[391].setPos(0F, 48F, 0F);
        this.parts[391].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[392] = new ModelPart(this, 0, 0);
        this.parts[392].setPos(0F, 48F, 16F);
        this.parts[392].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[393] = new ModelPart(this, 0, 0);
        this.parts[393].setPos(0F, 48F, 32F);
        this.parts[393].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[394] = new ModelPart(this, 0, 0);
        this.parts[394].setPos(0F, 48F, 48F);
        this.parts[394].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[395] = new ModelPart(this, 0, 0);
        this.parts[395].setPos(0F, 48F, 64F);
        this.parts[395].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[396] = new ModelPart(this, 0, 0);
        this.parts[396].setPos(0F, 64F, -64F);
        this.parts[396].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[397] = new ModelPart(this, 0, 0);
        this.parts[397].setPos(0F, 64F, -48F);
        this.parts[397].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[398] = new ModelPart(this, 0, 0);
        this.parts[398].setPos(0F, 64F, -32F);
        this.parts[398].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[399] = new ModelPart(this, 0, 0);
        this.parts[399].setPos(0F, 64F, -16F);
        this.parts[399].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[400] = new ModelPart(this, 0, 0);
        this.parts[400].setPos(0F, 64F, 0F);
        this.parts[400].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[401] = new ModelPart(this, 0, 0);
        this.parts[401].setPos(0F, 64F, 16F);
        this.parts[401].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[402] = new ModelPart(this, 0, 0);
        this.parts[402].setPos(0F, 64F, 32F);
        this.parts[402].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[403] = new ModelPart(this, 0, 0);
        this.parts[403].setPos(0F, 64F, 48F);
        this.parts[403].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[404] = new ModelPart(this, 0, 0);
        this.parts[404].setPos(0F, 64F, 64F);
        this.parts[404].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[405] = new ModelPart(this, 0, 0);
        this.parts[405].setPos(16F, -64F, -64F);
        this.parts[405].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[406] = new ModelPart(this, 0, 0);
        this.parts[406].setPos(16F, -64F, -48F);
        this.parts[406].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[407] = new ModelPart(this, 0, 0);
        this.parts[407].setPos(16F, -64F, -32F);
        this.parts[407].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[408] = new ModelPart(this, 0, 0);
        this.parts[408].setPos(16F, -64F, -16F);
        this.parts[408].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[409] = new ModelPart(this, 0, 0);
        this.parts[409].setPos(16F, -64F, 0F);
        this.parts[409].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[410] = new ModelPart(this, 0, 0);
        this.parts[410].setPos(16F, -64F, 16F);
        this.parts[410].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[411] = new ModelPart(this, 0, 0);
        this.parts[411].setPos(16F, -64F, 32F);
        this.parts[411].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[412] = new ModelPart(this, 0, 0);
        this.parts[412].setPos(16F, -64F, 48F);
        this.parts[412].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[413] = new ModelPart(this, 0, 0);
        this.parts[413].setPos(16F, -64F, 64F);
        this.parts[413].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[414] = new ModelPart(this, 0, 0);
        this.parts[414].setPos(16F, -48F, -64F);
        this.parts[414].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[415] = new ModelPart(this, 0, 0);
        this.parts[415].setPos(16F, -48F, -48F);
        this.parts[415].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[416] = new ModelPart(this, 0, 0);
        this.parts[416].setPos(16F, -48F, -32F);
        this.parts[416].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[417] = new ModelPart(this, 0, 0);
        this.parts[417].setPos(16F, -48F, -16F);
        this.parts[417].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[418] = new ModelPart(this, 0, 0);
        this.parts[418].setPos(16F, -48F, 0F);
        this.parts[418].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[419] = new ModelPart(this, 0, 0);
        this.parts[419].setPos(16F, -48F, 16F);
        this.parts[419].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[420] = new ModelPart(this, 0, 0);
        this.parts[420].setPos(16F, -48F, 32F);
        this.parts[420].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[421] = new ModelPart(this, 0, 0);
        this.parts[421].setPos(16F, -48F, 48F);
        this.parts[421].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[422] = new ModelPart(this, 0, 0);
        this.parts[422].setPos(16F, -48F, 64F);
        this.parts[422].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[423] = new ModelPart(this, 0, 0);
        this.parts[423].setPos(16F, -32F, -64F);
        this.parts[423].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[424] = new ModelPart(this, 0, 0);
        this.parts[424].setPos(16F, -32F, -48F);
        this.parts[424].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[425] = new ModelPart(this, 0, 0);
        this.parts[425].setPos(16F, -32F, -32F);
        this.parts[425].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[426] = new ModelPart(this, 0, 0);
        this.parts[426].setPos(16F, -32F, -16F);
        this.parts[426].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[427] = new ModelPart(this, 0, 0);
        this.parts[427].setPos(16F, -32F, 0F);
        this.parts[427].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[428] = new ModelPart(this, 0, 0);
        this.parts[428].setPos(16F, -32F, 16F);
        this.parts[428].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[429] = new ModelPart(this, 0, 0);
        this.parts[429].setPos(16F, -32F, 32F);
        this.parts[429].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[430] = new ModelPart(this, 0, 0);
        this.parts[430].setPos(16F, -32F, 48F);
        this.parts[430].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[431] = new ModelPart(this, 0, 0);
        this.parts[431].setPos(16F, -32F, 64F);
        this.parts[431].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[432] = new ModelPart(this, 0, 0);
        this.parts[432].setPos(16F, -16F, -64F);
        this.parts[432].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[433] = new ModelPart(this, 0, 0);
        this.parts[433].setPos(16F, -16F, -48F);
        this.parts[433].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[434] = new ModelPart(this, 0, 0);
        this.parts[434].setPos(16F, -16F, -32F);
        this.parts[434].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[435] = new ModelPart(this, 0, 0);
        this.parts[435].setPos(16F, -16F, -16F);
        this.parts[435].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[436] = new ModelPart(this, 0, 0);
        this.parts[436].setPos(16F, -16F, 0F);
        this.parts[436].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[437] = new ModelPart(this, 0, 0);
        this.parts[437].setPos(16F, -16F, 16F);
        this.parts[437].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[438] = new ModelPart(this, 0, 0);
        this.parts[438].setPos(16F, -16F, 32F);
        this.parts[438].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[439] = new ModelPart(this, 0, 0);
        this.parts[439].setPos(16F, -16F, 48F);
        this.parts[439].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[440] = new ModelPart(this, 0, 0);
        this.parts[440].setPos(16F, -16F, 64F);
        this.parts[440].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[441] = new ModelPart(this, 0, 0);
        this.parts[441].setPos(16F, 0F, -64F);
        this.parts[441].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[442] = new ModelPart(this, 0, 0);
        this.parts[442].setPos(16F, 0F, -48F);
        this.parts[442].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[443] = new ModelPart(this, 0, 0);
        this.parts[443].setPos(16F, 0F, -32F);
        this.parts[443].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[444] = new ModelPart(this, 0, 0);
        this.parts[444].setPos(16F, 0F, -16F);
        this.parts[444].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[445] = new ModelPart(this, 0, 0);
        this.parts[445].setPos(16F, 0F, 0F);
        this.parts[445].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[446] = new ModelPart(this, 0, 0);
        this.parts[446].setPos(16F, 0F, 16F);
        this.parts[446].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[447] = new ModelPart(this, 0, 0);
        this.parts[447].setPos(16F, 0F, 32F);
        this.parts[447].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[448] = new ModelPart(this, 0, 0);
        this.parts[448].setPos(16F, 0F, 48F);
        this.parts[448].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[449] = new ModelPart(this, 0, 0);
        this.parts[449].setPos(16F, 0F, 64F);
        this.parts[449].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[450] = new ModelPart(this, 0, 0);
        this.parts[450].setPos(16F, 16F, -64F);
        this.parts[450].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[451] = new ModelPart(this, 0, 0);
        this.parts[451].setPos(16F, 16F, -48F);
        this.parts[451].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[452] = new ModelPart(this, 0, 0);
        this.parts[452].setPos(16F, 16F, -32F);
        this.parts[452].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[453] = new ModelPart(this, 0, 0);
        this.parts[453].setPos(16F, 16F, -16F);
        this.parts[453].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[454] = new ModelPart(this, 0, 0);
        this.parts[454].setPos(16F, 16F, 0F);
        this.parts[454].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[455] = new ModelPart(this, 0, 0);
        this.parts[455].setPos(16F, 16F, 16F);
        this.parts[455].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[456] = new ModelPart(this, 0, 0);
        this.parts[456].setPos(16F, 16F, 32F);
        this.parts[456].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[457] = new ModelPart(this, 0, 0);
        this.parts[457].setPos(16F, 16F, 48F);
        this.parts[457].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[458] = new ModelPart(this, 0, 0);
        this.parts[458].setPos(16F, 16F, 64F);
        this.parts[458].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[459] = new ModelPart(this, 0, 0);
        this.parts[459].setPos(16F, 32F, -64F);
        this.parts[459].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[460] = new ModelPart(this, 0, 0);
        this.parts[460].setPos(16F, 32F, -48F);
        this.parts[460].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[461] = new ModelPart(this, 0, 0);
        this.parts[461].setPos(16F, 32F, -32F);
        this.parts[461].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[462] = new ModelPart(this, 0, 0);
        this.parts[462].setPos(16F, 32F, -16F);
        this.parts[462].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[463] = new ModelPart(this, 0, 0);
        this.parts[463].setPos(16F, 32F, 0F);
        this.parts[463].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[464] = new ModelPart(this, 0, 0);
        this.parts[464].setPos(16F, 32F, 16F);
        this.parts[464].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[465] = new ModelPart(this, 0, 0);
        this.parts[465].setPos(16F, 32F, 32F);
        this.parts[465].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[466] = new ModelPart(this, 0, 0);
        this.parts[466].setPos(16F, 32F, 48F);
        this.parts[466].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[467] = new ModelPart(this, 0, 0);
        this.parts[467].setPos(16F, 32F, 64F);
        this.parts[467].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[468] = new ModelPart(this, 0, 0);
        this.parts[468].setPos(16F, 48F, -64F);
        this.parts[468].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[469] = new ModelPart(this, 0, 0);
        this.parts[469].setPos(16F, 48F, -48F);
        this.parts[469].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[470] = new ModelPart(this, 0, 0);
        this.parts[470].setPos(16F, 48F, -32F);
        this.parts[470].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[471] = new ModelPart(this, 0, 0);
        this.parts[471].setPos(16F, 48F, -16F);
        this.parts[471].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[472] = new ModelPart(this, 0, 0);
        this.parts[472].setPos(16F, 48F, 0F);
        this.parts[472].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[473] = new ModelPart(this, 0, 0);
        this.parts[473].setPos(16F, 48F, 16F);
        this.parts[473].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[474] = new ModelPart(this, 0, 0);
        this.parts[474].setPos(16F, 48F, 32F);
        this.parts[474].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[475] = new ModelPart(this, 0, 0);
        this.parts[475].setPos(16F, 48F, 48F);
        this.parts[475].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[476] = new ModelPart(this, 0, 0);
        this.parts[476].setPos(16F, 48F, 64F);
        this.parts[476].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[477] = new ModelPart(this, 0, 0);
        this.parts[477].setPos(16F, 64F, -64F);
        this.parts[477].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[478] = new ModelPart(this, 0, 0);
        this.parts[478].setPos(16F, 64F, -48F);
        this.parts[478].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[479] = new ModelPart(this, 0, 0);
        this.parts[479].setPos(16F, 64F, -32F);
        this.parts[479].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[480] = new ModelPart(this, 0, 0);
        this.parts[480].setPos(16F, 64F, -16F);
        this.parts[480].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[481] = new ModelPart(this, 0, 0);
        this.parts[481].setPos(16F, 64F, 0F);
        this.parts[481].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[482] = new ModelPart(this, 0, 0);
        this.parts[482].setPos(16F, 64F, 16F);
        this.parts[482].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[483] = new ModelPart(this, 0, 0);
        this.parts[483].setPos(16F, 64F, 32F);
        this.parts[483].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[484] = new ModelPart(this, 0, 0);
        this.parts[484].setPos(16F, 64F, 48F);
        this.parts[484].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[485] = new ModelPart(this, 0, 0);
        this.parts[485].setPos(16F, 64F, 64F);
        this.parts[485].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[486] = new ModelPart(this, 0, 0);
        this.parts[486].setPos(32F, -64F, -64F);
        this.parts[486].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[487] = new ModelPart(this, 0, 0);
        this.parts[487].setPos(32F, -64F, -48F);
        this.parts[487].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[488] = new ModelPart(this, 0, 0);
        this.parts[488].setPos(32F, -64F, -32F);
        this.parts[488].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[489] = new ModelPart(this, 0, 0);
        this.parts[489].setPos(32F, -64F, -16F);
        this.parts[489].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[490] = new ModelPart(this, 0, 0);
        this.parts[490].setPos(32F, -64F, 0F);
        this.parts[490].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[491] = new ModelPart(this, 0, 0);
        this.parts[491].setPos(32F, -64F, 16F);
        this.parts[491].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[492] = new ModelPart(this, 0, 0);
        this.parts[492].setPos(32F, -64F, 32F);
        this.parts[492].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[493] = new ModelPart(this, 0, 0);
        this.parts[493].setPos(32F, -64F, 48F);
        this.parts[493].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[494] = new ModelPart(this, 0, 0);
        this.parts[494].setPos(32F, -64F, 64F);
        this.parts[494].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[495] = new ModelPart(this, 0, 0);
        this.parts[495].setPos(32F, -48F, -64F);
        this.parts[495].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[496] = new ModelPart(this, 0, 0);
        this.parts[496].setPos(32F, -48F, -48F);
        this.parts[496].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[497] = new ModelPart(this, 0, 0);
        this.parts[497].setPos(32F, -48F, -32F);
        this.parts[497].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[498] = new ModelPart(this, 0, 0);
        this.parts[498].setPos(32F, -48F, -16F);
        this.parts[498].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[499] = new ModelPart(this, 0, 0);
        this.parts[499].setPos(32F, -48F, 0F);
        this.parts[499].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[500] = new ModelPart(this, 0, 0);
        this.parts[500].setPos(32F, -48F, 16F);
        this.parts[500].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[501] = new ModelPart(this, 0, 0);
        this.parts[501].setPos(32F, -48F, 32F);
        this.parts[501].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[502] = new ModelPart(this, 0, 0);
        this.parts[502].setPos(32F, -48F, 48F);
        this.parts[502].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[503] = new ModelPart(this, 0, 0);
        this.parts[503].setPos(32F, -48F, 64F);
        this.parts[503].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[504] = new ModelPart(this, 0, 0);
        this.parts[504].setPos(32F, -32F, -64F);
        this.parts[504].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[505] = new ModelPart(this, 0, 0);
        this.parts[505].setPos(32F, -32F, -48F);
        this.parts[505].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[506] = new ModelPart(this, 0, 0);
        this.parts[506].setPos(32F, -32F, -32F);
        this.parts[506].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[507] = new ModelPart(this, 0, 0);
        this.parts[507].setPos(32F, -32F, -16F);
        this.parts[507].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[508] = new ModelPart(this, 0, 0);
        this.parts[508].setPos(32F, -32F, 0F);
        this.parts[508].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[509] = new ModelPart(this, 0, 0);
        this.parts[509].setPos(32F, -32F, 16F);
        this.parts[509].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[510] = new ModelPart(this, 0, 0);
        this.parts[510].setPos(32F, -32F, 32F);
        this.parts[510].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[511] = new ModelPart(this, 0, 0);
        this.parts[511].setPos(32F, -32F, 48F);
        this.parts[511].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[512] = new ModelPart(this, 0, 0);
        this.parts[512].setPos(32F, -32F, 64F);
        this.parts[512].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[513] = new ModelPart(this, 0, 0);
        this.parts[513].setPos(32F, -16F, -64F);
        this.parts[513].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[514] = new ModelPart(this, 0, 0);
        this.parts[514].setPos(32F, -16F, -48F);
        this.parts[514].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[515] = new ModelPart(this, 0, 0);
        this.parts[515].setPos(32F, -16F, -32F);
        this.parts[515].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[516] = new ModelPart(this, 0, 0);
        this.parts[516].setPos(32F, -16F, -16F);
        this.parts[516].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[517] = new ModelPart(this, 0, 0);
        this.parts[517].setPos(32F, -16F, 0F);
        this.parts[517].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[518] = new ModelPart(this, 0, 0);
        this.parts[518].setPos(32F, -16F, 16F);
        this.parts[518].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[519] = new ModelPart(this, 0, 0);
        this.parts[519].setPos(32F, -16F, 32F);
        this.parts[519].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[520] = new ModelPart(this, 0, 0);
        this.parts[520].setPos(32F, -16F, 48F);
        this.parts[520].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[521] = new ModelPart(this, 0, 0);
        this.parts[521].setPos(32F, -16F, 64F);
        this.parts[521].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[522] = new ModelPart(this, 0, 0);
        this.parts[522].setPos(32F, 0F, -64F);
        this.parts[522].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[523] = new ModelPart(this, 0, 0);
        this.parts[523].setPos(32F, 0F, -48F);
        this.parts[523].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[524] = new ModelPart(this, 0, 0);
        this.parts[524].setPos(32F, 0F, -32F);
        this.parts[524].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[525] = new ModelPart(this, 0, 0);
        this.parts[525].setPos(32F, 0F, -16F);
        this.parts[525].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[526] = new ModelPart(this, 0, 0);
        this.parts[526].setPos(32F, 0F, 0F);
        this.parts[526].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[527] = new ModelPart(this, 0, 0);
        this.parts[527].setPos(32F, 0F, 16F);
        this.parts[527].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[528] = new ModelPart(this, 0, 0);
        this.parts[528].setPos(32F, 0F, 32F);
        this.parts[528].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[529] = new ModelPart(this, 0, 0);
        this.parts[529].setPos(32F, 0F, 48F);
        this.parts[529].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[530] = new ModelPart(this, 0, 0);
        this.parts[530].setPos(32F, 0F, 64F);
        this.parts[530].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[531] = new ModelPart(this, 0, 0);
        this.parts[531].setPos(32F, 16F, -64F);
        this.parts[531].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[532] = new ModelPart(this, 0, 0);
        this.parts[532].setPos(32F, 16F, -48F);
        this.parts[532].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[533] = new ModelPart(this, 0, 0);
        this.parts[533].setPos(32F, 16F, -32F);
        this.parts[533].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[534] = new ModelPart(this, 0, 0);
        this.parts[534].setPos(32F, 16F, -16F);
        this.parts[534].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[535] = new ModelPart(this, 0, 0);
        this.parts[535].setPos(32F, 16F, 0F);
        this.parts[535].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[536] = new ModelPart(this, 0, 0);
        this.parts[536].setPos(32F, 16F, 16F);
        this.parts[536].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[537] = new ModelPart(this, 0, 0);
        this.parts[537].setPos(32F, 16F, 32F);
        this.parts[537].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[538] = new ModelPart(this, 0, 0);
        this.parts[538].setPos(32F, 16F, 48F);
        this.parts[538].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[539] = new ModelPart(this, 0, 0);
        this.parts[539].setPos(32F, 16F, 64F);
        this.parts[539].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[540] = new ModelPart(this, 0, 0);
        this.parts[540].setPos(32F, 32F, -64F);
        this.parts[540].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[541] = new ModelPart(this, 0, 0);
        this.parts[541].setPos(32F, 32F, -48F);
        this.parts[541].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[542] = new ModelPart(this, 0, 0);
        this.parts[542].setPos(32F, 32F, -32F);
        this.parts[542].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[543] = new ModelPart(this, 0, 0);
        this.parts[543].setPos(32F, 32F, -16F);
        this.parts[543].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[544] = new ModelPart(this, 0, 0);
        this.parts[544].setPos(32F, 32F, 0F);
        this.parts[544].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[545] = new ModelPart(this, 0, 0);
        this.parts[545].setPos(32F, 32F, 16F);
        this.parts[545].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[546] = new ModelPart(this, 0, 0);
        this.parts[546].setPos(32F, 32F, 32F);
        this.parts[546].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[547] = new ModelPart(this, 0, 0);
        this.parts[547].setPos(32F, 32F, 48F);
        this.parts[547].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[548] = new ModelPart(this, 0, 0);
        this.parts[548].setPos(32F, 32F, 64F);
        this.parts[548].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[549] = new ModelPart(this, 0, 0);
        this.parts[549].setPos(32F, 48F, -64F);
        this.parts[549].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[550] = new ModelPart(this, 0, 0);
        this.parts[550].setPos(32F, 48F, -48F);
        this.parts[550].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[551] = new ModelPart(this, 0, 0);
        this.parts[551].setPos(32F, 48F, -32F);
        this.parts[551].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[552] = new ModelPart(this, 0, 0);
        this.parts[552].setPos(32F, 48F, -16F);
        this.parts[552].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[553] = new ModelPart(this, 0, 0);
        this.parts[553].setPos(32F, 48F, 0F);
        this.parts[553].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[554] = new ModelPart(this, 0, 0);
        this.parts[554].setPos(32F, 48F, 16F);
        this.parts[554].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[555] = new ModelPart(this, 0, 0);
        this.parts[555].setPos(32F, 48F, 32F);
        this.parts[555].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[556] = new ModelPart(this, 0, 0);
        this.parts[556].setPos(32F, 48F, 48F);
        this.parts[556].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[557] = new ModelPart(this, 0, 0);
        this.parts[557].setPos(32F, 48F, 64F);
        this.parts[557].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[558] = new ModelPart(this, 0, 0);
        this.parts[558].setPos(32F, 64F, -64F);
        this.parts[558].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[559] = new ModelPart(this, 0, 0);
        this.parts[559].setPos(32F, 64F, -48F);
        this.parts[559].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[560] = new ModelPart(this, 0, 0);
        this.parts[560].setPos(32F, 64F, -32F);
        this.parts[560].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[561] = new ModelPart(this, 0, 0);
        this.parts[561].setPos(32F, 64F, -16F);
        this.parts[561].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[562] = new ModelPart(this, 0, 0);
        this.parts[562].setPos(32F, 64F, 0F);
        this.parts[562].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[563] = new ModelPart(this, 0, 0);
        this.parts[563].setPos(32F, 64F, 16F);
        this.parts[563].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[564] = new ModelPart(this, 0, 0);
        this.parts[564].setPos(32F, 64F, 32F);
        this.parts[564].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[565] = new ModelPart(this, 0, 0);
        this.parts[565].setPos(32F, 64F, 48F);
        this.parts[565].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[566] = new ModelPart(this, 0, 0);
        this.parts[566].setPos(32F, 64F, 64F);
        this.parts[566].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[567] = new ModelPart(this, 0, 0);
        this.parts[567].setPos(48F, -64F, -64F);
        this.parts[567].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[568] = new ModelPart(this, 0, 0);
        this.parts[568].setPos(48F, -64F, -48F);
        this.parts[568].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[569] = new ModelPart(this, 0, 0);
        this.parts[569].setPos(48F, -64F, -32F);
        this.parts[569].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[570] = new ModelPart(this, 0, 0);
        this.parts[570].setPos(48F, -64F, -16F);
        this.parts[570].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[571] = new ModelPart(this, 0, 0);
        this.parts[571].setPos(48F, -64F, 0F);
        this.parts[571].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[572] = new ModelPart(this, 0, 0);
        this.parts[572].setPos(48F, -64F, 16F);
        this.parts[572].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[573] = new ModelPart(this, 0, 0);
        this.parts[573].setPos(48F, -64F, 32F);
        this.parts[573].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[574] = new ModelPart(this, 0, 0);
        this.parts[574].setPos(48F, -64F, 48F);
        this.parts[574].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[575] = new ModelPart(this, 0, 0);
        this.parts[575].setPos(48F, -64F, 64F);
        this.parts[575].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[576] = new ModelPart(this, 0, 0);
        this.parts[576].setPos(48F, -48F, -64F);
        this.parts[576].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[577] = new ModelPart(this, 0, 0);
        this.parts[577].setPos(48F, -48F, -48F);
        this.parts[577].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[578] = new ModelPart(this, 0, 0);
        this.parts[578].setPos(48F, -48F, -32F);
        this.parts[578].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[579] = new ModelPart(this, 0, 0);
        this.parts[579].setPos(48F, -48F, -16F);
        this.parts[579].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[580] = new ModelPart(this, 0, 0);
        this.parts[580].setPos(48F, -48F, 0F);
        this.parts[580].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[581] = new ModelPart(this, 0, 0);
        this.parts[581].setPos(48F, -48F, 16F);
        this.parts[581].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[582] = new ModelPart(this, 0, 0);
        this.parts[582].setPos(48F, -48F, 32F);
        this.parts[582].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[583] = new ModelPart(this, 0, 0);
        this.parts[583].setPos(48F, -48F, 48F);
        this.parts[583].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[584] = new ModelPart(this, 0, 0);
        this.parts[584].setPos(48F, -48F, 64F);
        this.parts[584].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[585] = new ModelPart(this, 0, 0);
        this.parts[585].setPos(48F, -32F, -64F);
        this.parts[585].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[586] = new ModelPart(this, 0, 0);
        this.parts[586].setPos(48F, -32F, -48F);
        this.parts[586].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[587] = new ModelPart(this, 0, 0);
        this.parts[587].setPos(48F, -32F, -32F);
        this.parts[587].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[588] = new ModelPart(this, 0, 0);
        this.parts[588].setPos(48F, -32F, -16F);
        this.parts[588].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[589] = new ModelPart(this, 0, 0);
        this.parts[589].setPos(48F, -32F, 0F);
        this.parts[589].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[590] = new ModelPart(this, 0, 0);
        this.parts[590].setPos(48F, -32F, 16F);
        this.parts[590].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[591] = new ModelPart(this, 0, 0);
        this.parts[591].setPos(48F, -32F, 32F);
        this.parts[591].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[592] = new ModelPart(this, 0, 0);
        this.parts[592].setPos(48F, -32F, 48F);
        this.parts[592].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[593] = new ModelPart(this, 0, 0);
        this.parts[593].setPos(48F, -32F, 64F);
        this.parts[593].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[594] = new ModelPart(this, 0, 0);
        this.parts[594].setPos(48F, -16F, -64F);
        this.parts[594].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[595] = new ModelPart(this, 0, 0);
        this.parts[595].setPos(48F, -16F, -48F);
        this.parts[595].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[596] = new ModelPart(this, 0, 0);
        this.parts[596].setPos(48F, -16F, -32F);
        this.parts[596].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[597] = new ModelPart(this, 0, 0);
        this.parts[597].setPos(48F, -16F, -16F);
        this.parts[597].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[598] = new ModelPart(this, 0, 0);
        this.parts[598].setPos(48F, -16F, 0F);
        this.parts[598].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[599] = new ModelPart(this, 0, 0);
        this.parts[599].setPos(48F, -16F, 16F);
        this.parts[599].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[600] = new ModelPart(this, 0, 0);
        this.parts[600].setPos(48F, -16F, 32F);
        this.parts[600].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[601] = new ModelPart(this, 0, 0);
        this.parts[601].setPos(48F, -16F, 48F);
        this.parts[601].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[602] = new ModelPart(this, 0, 0);
        this.parts[602].setPos(48F, -16F, 64F);
        this.parts[602].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[603] = new ModelPart(this, 0, 0);
        this.parts[603].setPos(48F, 0F, -64F);
        this.parts[603].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[604] = new ModelPart(this, 0, 0);
        this.parts[604].setPos(48F, 0F, -48F);
        this.parts[604].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[605] = new ModelPart(this, 0, 0);
        this.parts[605].setPos(48F, 0F, -32F);
        this.parts[605].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[606] = new ModelPart(this, 0, 0);
        this.parts[606].setPos(48F, 0F, -16F);
        this.parts[606].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[607] = new ModelPart(this, 0, 0);
        this.parts[607].setPos(48F, 0F, 0F);
        this.parts[607].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[608] = new ModelPart(this, 0, 0);
        this.parts[608].setPos(48F, 0F, 16F);
        this.parts[608].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[609] = new ModelPart(this, 0, 0);
        this.parts[609].setPos(48F, 0F, 32F);
        this.parts[609].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[610] = new ModelPart(this, 0, 0);
        this.parts[610].setPos(48F, 0F, 48F);
        this.parts[610].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[611] = new ModelPart(this, 0, 0);
        this.parts[611].setPos(48F, 0F, 64F);
        this.parts[611].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[612] = new ModelPart(this, 0, 0);
        this.parts[612].setPos(48F, 16F, -64F);
        this.parts[612].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[613] = new ModelPart(this, 0, 0);
        this.parts[613].setPos(48F, 16F, -48F);
        this.parts[613].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[614] = new ModelPart(this, 0, 0);
        this.parts[614].setPos(48F, 16F, -32F);
        this.parts[614].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[615] = new ModelPart(this, 0, 0);
        this.parts[615].setPos(48F, 16F, -16F);
        this.parts[615].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[616] = new ModelPart(this, 0, 0);
        this.parts[616].setPos(48F, 16F, 0F);
        this.parts[616].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[617] = new ModelPart(this, 0, 0);
        this.parts[617].setPos(48F, 16F, 16F);
        this.parts[617].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[618] = new ModelPart(this, 0, 0);
        this.parts[618].setPos(48F, 16F, 32F);
        this.parts[618].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[619] = new ModelPart(this, 0, 0);
        this.parts[619].setPos(48F, 16F, 48F);
        this.parts[619].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[620] = new ModelPart(this, 0, 0);
        this.parts[620].setPos(48F, 16F, 64F);
        this.parts[620].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[621] = new ModelPart(this, 0, 0);
        this.parts[621].setPos(48F, 32F, -64F);
        this.parts[621].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[622] = new ModelPart(this, 0, 0);
        this.parts[622].setPos(48F, 32F, -48F);
        this.parts[622].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[623] = new ModelPart(this, 0, 0);
        this.parts[623].setPos(48F, 32F, -32F);
        this.parts[623].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[624] = new ModelPart(this, 0, 0);
        this.parts[624].setPos(48F, 32F, -16F);
        this.parts[624].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[625] = new ModelPart(this, 0, 0);
        this.parts[625].setPos(48F, 32F, 0F);
        this.parts[625].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[626] = new ModelPart(this, 0, 0);
        this.parts[626].setPos(48F, 32F, 16F);
        this.parts[626].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[627] = new ModelPart(this, 0, 0);
        this.parts[627].setPos(48F, 32F, 32F);
        this.parts[627].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[628] = new ModelPart(this, 0, 0);
        this.parts[628].setPos(48F, 32F, 48F);
        this.parts[628].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[629] = new ModelPart(this, 0, 0);
        this.parts[629].setPos(48F, 32F, 64F);
        this.parts[629].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[630] = new ModelPart(this, 0, 0);
        this.parts[630].setPos(48F, 48F, -64F);
        this.parts[630].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[631] = new ModelPart(this, 0, 0);
        this.parts[631].setPos(48F, 48F, -48F);
        this.parts[631].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[632] = new ModelPart(this, 0, 0);
        this.parts[632].setPos(48F, 48F, -32F);
        this.parts[632].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[633] = new ModelPart(this, 0, 0);
        this.parts[633].setPos(48F, 48F, -16F);
        this.parts[633].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[634] = new ModelPart(this, 0, 0);
        this.parts[634].setPos(48F, 48F, 0F);
        this.parts[634].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[635] = new ModelPart(this, 0, 0);
        this.parts[635].setPos(48F, 48F, 16F);
        this.parts[635].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[636] = new ModelPart(this, 0, 0);
        this.parts[636].setPos(48F, 48F, 32F);
        this.parts[636].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[637] = new ModelPart(this, 0, 0);
        this.parts[637].setPos(48F, 48F, 48F);
        this.parts[637].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[638] = new ModelPart(this, 0, 0);
        this.parts[638].setPos(48F, 48F, 64F);
        this.parts[638].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[639] = new ModelPart(this, 0, 0);
        this.parts[639].setPos(48F, 64F, -64F);
        this.parts[639].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[640] = new ModelPart(this, 0, 0);
        this.parts[640].setPos(48F, 64F, -48F);
        this.parts[640].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[641] = new ModelPart(this, 0, 0);
        this.parts[641].setPos(48F, 64F, -32F);
        this.parts[641].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[642] = new ModelPart(this, 0, 0);
        this.parts[642].setPos(48F, 64F, -16F);
        this.parts[642].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[643] = new ModelPart(this, 0, 0);
        this.parts[643].setPos(48F, 64F, 0F);
        this.parts[643].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[644] = new ModelPart(this, 0, 0);
        this.parts[644].setPos(48F, 64F, 16F);
        this.parts[644].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[645] = new ModelPart(this, 0, 0);
        this.parts[645].setPos(48F, 64F, 32F);
        this.parts[645].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[646] = new ModelPart(this, 0, 0);
        this.parts[646].setPos(48F, 64F, 48F);
        this.parts[646].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[647] = new ModelPart(this, 0, 0);
        this.parts[647].setPos(48F, 64F, 64F);
        this.parts[647].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[648] = new ModelPart(this, 0, 0);
        this.parts[648].setPos(64F, -64F, -64F);
        this.parts[648].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[649] = new ModelPart(this, 0, 0);
        this.parts[649].setPos(64F, -64F, -48F);
        this.parts[649].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[650] = new ModelPart(this, 0, 0);
        this.parts[650].setPos(64F, -64F, -32F);
        this.parts[650].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[651] = new ModelPart(this, 0, 0);
        this.parts[651].setPos(64F, -64F, -16F);
        this.parts[651].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[652] = new ModelPart(this, 0, 0);
        this.parts[652].setPos(64F, -64F, 0F);
        this.parts[652].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[653] = new ModelPart(this, 0, 0);
        this.parts[653].setPos(64F, -64F, 16F);
        this.parts[653].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[654] = new ModelPart(this, 0, 0);
        this.parts[654].setPos(64F, -64F, 32F);
        this.parts[654].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[655] = new ModelPart(this, 0, 0);
        this.parts[655].setPos(64F, -64F, 48F);
        this.parts[655].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[656] = new ModelPart(this, 0, 0);
        this.parts[656].setPos(64F, -64F, 64F);
        this.parts[656].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[657] = new ModelPart(this, 0, 0);
        this.parts[657].setPos(64F, -48F, -64F);
        this.parts[657].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[658] = new ModelPart(this, 0, 0);
        this.parts[658].setPos(64F, -48F, -48F);
        this.parts[658].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[659] = new ModelPart(this, 0, 0);
        this.parts[659].setPos(64F, -48F, -32F);
        this.parts[659].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[660] = new ModelPart(this, 0, 0);
        this.parts[660].setPos(64F, -48F, -16F);
        this.parts[660].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[661] = new ModelPart(this, 0, 0);
        this.parts[661].setPos(64F, -48F, 0F);
        this.parts[661].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[662] = new ModelPart(this, 0, 0);
        this.parts[662].setPos(64F, -48F, 16F);
        this.parts[662].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[663] = new ModelPart(this, 0, 0);
        this.parts[663].setPos(64F, -48F, 32F);
        this.parts[663].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[664] = new ModelPart(this, 0, 0);
        this.parts[664].setPos(64F, -48F, 48F);
        this.parts[664].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[665] = new ModelPart(this, 0, 0);
        this.parts[665].setPos(64F, -48F, 64F);
        this.parts[665].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[666] = new ModelPart(this, 0, 0);
        this.parts[666].setPos(64F, -32F, -64F);
        this.parts[666].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[667] = new ModelPart(this, 0, 0);
        this.parts[667].setPos(64F, -32F, -48F);
        this.parts[667].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[668] = new ModelPart(this, 0, 0);
        this.parts[668].setPos(64F, -32F, -32F);
        this.parts[668].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[669] = new ModelPart(this, 0, 0);
        this.parts[669].setPos(64F, -32F, -16F);
        this.parts[669].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[670] = new ModelPart(this, 0, 0);
        this.parts[670].setPos(64F, -32F, 0F);
        this.parts[670].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[671] = new ModelPart(this, 0, 0);
        this.parts[671].setPos(64F, -32F, 16F);
        this.parts[671].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[672] = new ModelPart(this, 0, 0);
        this.parts[672].setPos(64F, -32F, 32F);
        this.parts[672].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[673] = new ModelPart(this, 0, 0);
        this.parts[673].setPos(64F, -32F, 48F);
        this.parts[673].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[674] = new ModelPart(this, 0, 0);
        this.parts[674].setPos(64F, -32F, 64F);
        this.parts[674].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[675] = new ModelPart(this, 0, 0);
        this.parts[675].setPos(64F, -16F, -64F);
        this.parts[675].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[676] = new ModelPart(this, 0, 0);
        this.parts[676].setPos(64F, -16F, -48F);
        this.parts[676].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[677] = new ModelPart(this, 0, 0);
        this.parts[677].setPos(64F, -16F, -32F);
        this.parts[677].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[678] = new ModelPart(this, 0, 0);
        this.parts[678].setPos(64F, -16F, -16F);
        this.parts[678].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[679] = new ModelPart(this, 0, 0);
        this.parts[679].setPos(64F, -16F, 0F);
        this.parts[679].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[680] = new ModelPart(this, 0, 0);
        this.parts[680].setPos(64F, -16F, 16F);
        this.parts[680].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[681] = new ModelPart(this, 0, 0);
        this.parts[681].setPos(64F, -16F, 32F);
        this.parts[681].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[682] = new ModelPart(this, 0, 0);
        this.parts[682].setPos(64F, -16F, 48F);
        this.parts[682].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[683] = new ModelPart(this, 0, 0);
        this.parts[683].setPos(64F, -16F, 64F);
        this.parts[683].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[684] = new ModelPart(this, 0, 0);
        this.parts[684].setPos(64F, 0F, -64F);
        this.parts[684].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[685] = new ModelPart(this, 0, 0);
        this.parts[685].setPos(64F, 0F, -48F);
        this.parts[685].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[686] = new ModelPart(this, 0, 0);
        this.parts[686].setPos(64F, 0F, -32F);
        this.parts[686].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[687] = new ModelPart(this, 0, 0);
        this.parts[687].setPos(64F, 0F, -16F);
        this.parts[687].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[688] = new ModelPart(this, 0, 0);
        this.parts[688].setPos(64F, 0F, 0F);
        this.parts[688].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[689] = new ModelPart(this, 0, 0);
        this.parts[689].setPos(64F, 0F, 16F);
        this.parts[689].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[690] = new ModelPart(this, 0, 0);
        this.parts[690].setPos(64F, 0F, 32F);
        this.parts[690].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[691] = new ModelPart(this, 0, 0);
        this.parts[691].setPos(64F, 0F, 48F);
        this.parts[691].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[692] = new ModelPart(this, 0, 0);
        this.parts[692].setPos(64F, 0F, 64F);
        this.parts[692].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[693] = new ModelPart(this, 0, 0);
        this.parts[693].setPos(64F, 16F, -64F);
        this.parts[693].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[694] = new ModelPart(this, 0, 0);
        this.parts[694].setPos(64F, 16F, -48F);
        this.parts[694].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[695] = new ModelPart(this, 0, 0);
        this.parts[695].setPos(64F, 16F, -32F);
        this.parts[695].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[696] = new ModelPart(this, 0, 0);
        this.parts[696].setPos(64F, 16F, -16F);
        this.parts[696].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[697] = new ModelPart(this, 0, 0);
        this.parts[697].setPos(64F, 16F, 0F);
        this.parts[697].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[698] = new ModelPart(this, 0, 0);
        this.parts[698].setPos(64F, 16F, 16F);
        this.parts[698].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[699] = new ModelPart(this, 0, 0);
        this.parts[699].setPos(64F, 16F, 32F);
        this.parts[699].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[700] = new ModelPart(this, 0, 0);
        this.parts[700].setPos(64F, 16F, 48F);
        this.parts[700].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[701] = new ModelPart(this, 0, 0);
        this.parts[701].setPos(64F, 16F, 64F);
        this.parts[701].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[702] = new ModelPart(this, 0, 0);
        this.parts[702].setPos(64F, 32F, -64F);
        this.parts[702].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[703] = new ModelPart(this, 0, 0);
        this.parts[703].setPos(64F, 32F, -48F);
        this.parts[703].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[704] = new ModelPart(this, 0, 0);
        this.parts[704].setPos(64F, 32F, -32F);
        this.parts[704].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[705] = new ModelPart(this, 0, 0);
        this.parts[705].setPos(64F, 32F, -16F);
        this.parts[705].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[706] = new ModelPart(this, 0, 0);
        this.parts[706].setPos(64F, 32F, 0F);
        this.parts[706].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[707] = new ModelPart(this, 0, 0);
        this.parts[707].setPos(64F, 32F, 16F);
        this.parts[707].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[708] = new ModelPart(this, 0, 0);
        this.parts[708].setPos(64F, 32F, 32F);
        this.parts[708].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[709] = new ModelPart(this, 0, 0);
        this.parts[709].setPos(64F, 32F, 48F);
        this.parts[709].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[710] = new ModelPart(this, 0, 0);
        this.parts[710].setPos(64F, 32F, 64F);
        this.parts[710].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[711] = new ModelPart(this, 0, 0);
        this.parts[711].setPos(64F, 48F, -64F);
        this.parts[711].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[712] = new ModelPart(this, 0, 0);
        this.parts[712].setPos(64F, 48F, -48F);
        this.parts[712].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[713] = new ModelPart(this, 0, 0);
        this.parts[713].setPos(64F, 48F, -32F);
        this.parts[713].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[714] = new ModelPart(this, 0, 0);
        this.parts[714].setPos(64F, 48F, -16F);
        this.parts[714].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[715] = new ModelPart(this, 0, 0);
        this.parts[715].setPos(64F, 48F, 0F);
        this.parts[715].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[716] = new ModelPart(this, 0, 0);
        this.parts[716].setPos(64F, 48F, 16F);
        this.parts[716].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[717] = new ModelPart(this, 0, 0);
        this.parts[717].setPos(64F, 48F, 32F);
        this.parts[717].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[718] = new ModelPart(this, 0, 0);
        this.parts[718].setPos(64F, 48F, 48F);
        this.parts[718].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[719] = new ModelPart(this, 0, 0);
        this.parts[719].setPos(64F, 48F, 64F);
        this.parts[719].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[720] = new ModelPart(this, 0, 0);
        this.parts[720].setPos(64F, 64F, -64F);
        this.parts[720].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[721] = new ModelPart(this, 0, 0);
        this.parts[721].setPos(64F, 64F, -48F);
        this.parts[721].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[722] = new ModelPart(this, 0, 0);
        this.parts[722].setPos(64F, 64F, -32F);
        this.parts[722].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[723] = new ModelPart(this, 0, 0);
        this.parts[723].setPos(64F, 64F, -16F);
        this.parts[723].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[724] = new ModelPart(this, 0, 0);
        this.parts[724].setPos(64F, 64F, 0F);
        this.parts[724].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[725] = new ModelPart(this, 0, 0);
        this.parts[725].setPos(64F, 64F, 16F);
        this.parts[725].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[726] = new ModelPart(this, 0, 0);
        this.parts[726].setPos(64F, 64F, 32F);
        this.parts[726].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[727] = new ModelPart(this, 0, 0);
        this.parts[727].setPos(64F, 64F, 48F);
        this.parts[727].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);

        this.parts[728] = new ModelPart(this, 0, 0);
        this.parts[728].setPos(64F, 64F, 64F);
        this.parts[728].addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F);
		
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(this.parts[0]).forEach((modelRenderer) -> {
			modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
}
*/