package online.kingdomkeys.kingdomkeys.client.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

@OnlyIn(Dist.CLIENT)
public class KeybladeModel implements IBakedModel {

    public IBakedModel obj;

    public ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.NONE;

    public KeybladeModel(IBakedModel obj) {
        this.obj = obj;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        if (side == null) {
            List<BakedQuad> quads = obj.getQuads(null, null, rand);
            return quads;
        }
        return Collections.emptyList();    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(Items.IRON_SWORD);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        transformType = cameraTransformType;
        return ForgeHooksClient.handlePerspective(obj, cameraTransformType);
    }
}
