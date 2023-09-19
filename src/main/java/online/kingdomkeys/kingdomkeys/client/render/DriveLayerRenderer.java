package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

@OnlyIn(Dist.CLIENT)
public class DriveLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
	private PlayerRenderer renderPlayer;

	public DriveLayerRenderer(RenderLayerParent<T, M> entityRendererIn) {
		super(entityRendererIn);
		this.renderPlayer = (PlayerRenderer) entityRendererIn;
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(ModConfigs.showDriveForms && entitylivingbaseIn != null && ModCapabilities.getPlayer((Player) entitylivingbaseIn) != null) {
			if(!ModCapabilities.getPlayer((Player) entitylivingbaseIn).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				String drive = ModCapabilities.getPlayer((Player) entitylivingbaseIn).getActiveDriveForm();
				DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(drive));
				
				if (form.getTextureLocation() != null) {
					VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityTranslucent(form.getTextureLocation()), false, false);
					renderPlayer.getModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0F);
				}
			}
		}
	}
}
