package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CardItemRenderer extends ItemEntityRenderer {

	public CardItemRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(ItemEntity entity, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight) {
		pose.pushPose();
		{

			float time = entity.tickCount + partialTick;

			float intro = Mth.clamp(time / 60.0F, 0.0F, 1.0F);

			float bounce = Mth.abs(Mth.sin(time * 0.22F)) * 1F * intro;

			pose.translate(0, 0.1F + bounce, 0);

			ItemStack itemstack = entity.getItem();
			BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), null, entity.getId());

			float spin = entity.getSpin(partialTick);
			pose.mulPose(Axis.YP.rotation(spin));
			//itemRenderer.render(itemstack, ItemDisplayContext.GROUND, false, pose, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
			renderMultipleFromCount(this.itemRenderer, pose, buffer, packedLight, itemstack, bakedmodel, true, entity.level().random);

		}
		pose.popPose();
	}
}