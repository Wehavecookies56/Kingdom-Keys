package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class DriveLayerRenderer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M> {
	private PlayerRenderer renderPlayer;

	public DriveLayerRenderer(IEntityRenderer<T, M> entityRendererIn) {
		super(entityRendererIn);
		this.renderPlayer = (PlayerRenderer) entityRendererIn;
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ResourceLocation texture = null;
		String drive = ModCapabilities.getPlayer((PlayerEntity) entitylivingbaseIn).getActiveDriveForm();

		switch (drive) {
		case Strings.Form_Valor:
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/valor.png");
			break;
		case Strings.Form_Wisdom:
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/wisdom.png");
			break;
		case Strings.Form_Limit:
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/limit.png");
			break;
		case Strings.Form_Master:
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/master.png");
			break;
		case Strings.Form_Final:
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/final.png");
			break;
		case Strings.Form_Anti:
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/anti.png");
			break;
		}

		if (texture != null) {
			IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, RenderType.getEntityCutoutNoCull(texture), false, false);
			renderPlayer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);
		}
	}
}
