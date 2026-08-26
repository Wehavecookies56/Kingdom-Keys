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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

@OnlyIn(Dist.CLIENT)
public class DriveLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
	private final PlayerRenderer renderPlayer;
	private static final ResourceLocation ANTI_EYES = KingdomKeys.rl("textures/models/armor/anti_eyes.png");

	public DriveLayerRenderer(RenderLayerParent<T, M> entityRendererIn) {
		super(entityRendererIn);
		this.renderPlayer = (PlayerRenderer) entityRendererIn;
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(ModConfigs.showDriveForms && entitylivingbaseIn != null && PlayerData.get((Player) entitylivingbaseIn) != null) {
			if(!PlayerData.get((Player) entitylivingbaseIn).isFormActive(ModDriveForms.NONE)) {
				ResourceLocation drive = PlayerData.get((Player) entitylivingbaseIn).getActiveDriveForm();
				DriveForm form = ModDriveForms.registry.get(drive);
				
				if (form.getTextureLocation((Player) entitylivingbaseIn) != null) {
					VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(form.getTextureLocation((Player) entitylivingbaseIn)), false, false);
					renderPlayer.getModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
					if(form.equals(ModDriveForms.ANTI.get())) {
						VertexConsumer vertex = ItemRenderer.getFoilBuffer(bufferIn, RenderType.eyes(ANTI_EYES), false, false);
						renderPlayer.getModel().renderToBuffer(matrixStackIn, vertex, 15728640, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
					}
				}
			}
		}
	}
}
