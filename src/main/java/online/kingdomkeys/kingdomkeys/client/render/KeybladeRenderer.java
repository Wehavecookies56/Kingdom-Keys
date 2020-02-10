/*package online.kingdomkeys.kingdomkeys.client.render;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.LightUtil;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.KeybladeModel;

@OnlyIn(Dist.CLIENT)
public class KeybladeRenderer extends ItemStackTileEntityRenderer {

    private ModelResourceLocation modelLoc = new ModelResourceLocation(KingdomKeys.MODID + ":item/kingdom_key", "inventory");
    private ResourceLocation textureLoc = new ResourceLocation(KingdomKeys.MODID + "textures/item/models/keyblades/kingdom_key.png");

    public static final Map<ItemCameraTransforms.TransformType, float[]> transformationMatrix;

    static {
        transformationMatrix = new HashMap<>();
        transformationMatrix.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, new float[]{1.0F, 0.0F, 0.0F, 0.0F,    0.0F, 1.0F, 0.0F, 0.0F,    0.0F, 0.0F, 1.0F, 0.0F,    0.0F, 0.0F, 0.0F, 1.0F});
    }

    public void setModelLoc(ModelResourceLocation modelLoc) {
        this.modelLoc = modelLoc;
    }

    public void setTextureLoc(ResourceLocation textureLoc) {
        this.textureLoc = textureLoc;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(modelLoc);
        KingdomKeys.LOGGER.info("TEST");
        if (model instanceof KeybladeModel) {
            KeybladeModel keybladeModel = (KeybladeModel)model;
            render(keybladeModel);
        }
    }


    public void render(KeybladeModel model) {
        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        GlStateManager.pushMatrix();
        KingdomKeys.LOGGER.info("TEST");
        switch (model.transformType) {
            case FIRST_PERSON_RIGHT_HAND:
                KingdomKeys.LOGGER.info("TEST");
                GlStateManager.translatef(0, -0.1F, 0);
                GlStateManager.rotatef(180, 1, 1, 0);
                GlStateManager.rotatef(170, 0,0, 1);
                GlStateManager.scalef(0.013F, 0.013F, 0.013F);
                break;
            default:
                break;
        }
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        Minecraft.getInstance().getTextureManager().bindTexture(textureLoc);
        bb.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        for (BakedQuad quad : model.getQuads(null, null, Minecraft.getInstance().world.rand)) {
            LightUtil.renderQuadColor(bb, quad, -1);
            Vec3i normals = quad.getFace().getDirectionVec();
            bb.putNormal((float)normals.getX(), (float)normals.getY(), (float)normals.getZ());
        }
        Tessellator.getInstance().draw();

        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.disableCull();

    }



}
*/