package online.kingdomkeys.kingdomkeys.client.render.org;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeCoreEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class LaserDomeEntityRenderer extends EntityRenderer<LaserDomeCoreEntity> {

	public LaserDomeEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(LaserDomeCoreEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/cube.png");
	}

}