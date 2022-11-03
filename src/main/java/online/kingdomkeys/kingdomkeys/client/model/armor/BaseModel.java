package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Function;

public abstract class BaseModel extends Model {

    public BaseModel(Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
    }

    protected VertexConsumer getVertexBuilder(@Nonnull MultiBufferSource renderer, @Nonnull RenderType renderType, boolean hasEffect) {
		return ItemRenderer.getFoilBuffer(renderer, renderType, false, hasEffect);
	}

    protected void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

}
