package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;

// Worlds are not drawn here, but rather in the Level Render event
public class WorldMarkerEntityRenderer extends EntityRenderer<WorldMarkerEntity> {

	public WorldMarkerEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

	@Override
	public void render(WorldMarkerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
	}

	@Override
	public boolean shouldRender(WorldMarkerEntity entity, Frustum frustum, double x, double y, double z) {
		return false;
	}

	@Override
	public ResourceLocation getTextureLocation(WorldMarkerEntity entity) {
		return KingdomKeys.rl("textures/worldmap/missing.png");
	}
}
