package online.kingdomkeys.kingdomkeys.client.render.org;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserCircleCoreEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class LaserCircleEntityRenderer extends EntityRenderer<LaserCircleCoreEntity> {

	public LaserCircleEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(LaserCircleCoreEntity entity) {
		return KingdomKeys.rl("textures/entity/models/cube.png");
	}

}